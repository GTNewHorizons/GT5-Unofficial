package gregtech.common.misc;

import static net.minecraftforge.common.util.Constants.NBT.TAG_BYTE_ARRAY;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.events.MetricsCoverDataEvent;
import gregtech.common.events.MetricsCoverHostDeconstructedEvent;
import gregtech.common.events.MetricsCoverSelfDestructEvent;

/**
 * Catches and provides data transmitted from deployed Metrics Transmitter covers. Only stores one result per frequency
 * at a time. Metrics covers are intended to overwrite an old result every time they emit a new event.
 * <br />
 * <br />
 * This information is only partially persisted; frequencies that are in a non-operational state will be written to
 * disk, while operational frequencies are volatile. The assumption is that any frequency with a broadcasting card will,
 * fairly quickly, re-assert its presence. Conversely, one-time events like deconstruction or self-destruction can occur
 * while the card is in a container, rotting on the ground, etc.
 */
public class GlobalMetricsCoverDatabase extends WorldSavedData {

    private static GlobalMetricsCoverDatabase INSTANCE;

    private static final Map<UUID, Data> DATABASE = new ConcurrentHashMap<>();
    private static final String DATA_NAME = "GregTech_MetricsCoverDatabase";
    public static final String DECONSTRUCTED_KEY = "GregTech_MetricsCoverDatabase_Deconstructed";
    public static final String SELF_DESTRUCTED_KEY = "GregTech_MetricsCoverDatabase_SelfDestructed";

    public GlobalMetricsCoverDatabase() {
        super(DATA_NAME);
    }

    public GlobalMetricsCoverDatabase(String name) {
        super(name);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveMetricsData(MetricsCoverDataEvent event) {
        store(event.getFrequency(), State.OPERATIONAL, event.getPayload());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveDeconstructed(MetricsCoverHostDeconstructedEvent event) {
        store(event.getFrequency(), State.DECONSTRUCTED, null);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveSelfDestruct(MetricsCoverSelfDestructEvent event) {
        store(event.getFrequency(), State.SELF_DESTRUCTED, null);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            DATABASE.clear();

            final MapStorage storage = event.world.mapStorage;
            INSTANCE = (GlobalMetricsCoverDatabase) storage.loadData(GlobalMetricsCoverDatabase.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new GlobalMetricsCoverDatabase();
                storage.setData(DATA_NAME, INSTANCE);
            }

            INSTANCE.markDirty();
        }
    }

    /**
     * Get the data for a frequency, if it exists.
     *
     * @param frequency The UUID corresponding to the frequency to retrieve.
     * @return An Optional with the frequency's data, or an empty Optional if it doesn't exist.
     */
    public static Optional<Data> getData(UUID frequency) {
        return Optional.ofNullable(DATABASE.get(frequency));
    }

    /**
     * Once a card has received the fact that it has self-destructed, this method can be called to free up its spot
     * in the database. Does nothing if the frequency is missing or is not in a self-destructed state.
     *
     * @param frequency The UUID corresponding to the frequency to possibly cull.
     */
    public static void clearSelfDestructedFrequency(UUID frequency) {
        getData(frequency).ifPresent(data -> {
            if (data.getState() == State.SELF_DESTRUCTED) {
                DATABASE.remove(frequency);
                tryMarkDirty();
            }
        });
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        final NBTTagList deconstructed = nbtTagCompound.getTagList(DECONSTRUCTED_KEY, TAG_BYTE_ARRAY);
        final NBTTagList selfDestructed = nbtTagCompound.getTagList(SELF_DESTRUCTED_KEY, TAG_BYTE_ARRAY);

        if (deconstructed != null) {
            for (int i = 0; i < deconstructed.tagCount(); i++) {
                NBTTagByteArray byteArray = (NBTTagByteArray) deconstructed.removeTag(0);
                DATABASE.put(reconstituteUUID(byteArray.func_150292_c()), new Data(State.DECONSTRUCTED, null));
            }
        }

        if (selfDestructed != null) {
            for (int i = 0; i < selfDestructed.tagCount(); i++) {
                NBTTagByteArray byteArray = (NBTTagByteArray) selfDestructed.removeTag(0);
                DATABASE.put(reconstituteUUID(byteArray.func_150292_c()), new Data(State.SELF_DESTRUCTED, null));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        // We only care about persisting frequencies that aren't operational.
        final NBTTagList deconstructed = new NBTTagList();
        final NBTTagList selfDestructed = new NBTTagList();
        DATABASE.forEach((uuid, data) -> {
            switch (data.getState()) {
                case DECONSTRUCTED -> deconstructed.appendTag(new NBTTagByteArray(dumpUUID(uuid)));
                case SELF_DESTRUCTED -> selfDestructed.appendTag(new NBTTagByteArray(dumpUUID(uuid)));
            }
        });

        if (deconstructed.tagCount() > 0) {
            nbtTagCompound.setTag(DECONSTRUCTED_KEY, deconstructed);
        }
        if (selfDestructed.tagCount() > 0) {
            nbtTagCompound.setTag(SELF_DESTRUCTED_KEY, selfDestructed);
        }
    }

    /**
     * Stores the new result and flag the static {@link MapStorage} instance as dirty if the information updated. Will
     * not flag dirty for any data in the {@link State#OPERATIONAL OPERATIONAL} state since they aren't stored.
     *
     * @param frequency Maps to a unique deployed cover.
     * @param state     The new cover state.
     * @param payload   A list of strings to display on the information panel, if the card is slotted properly.
     */
    private static void store(@NotNull UUID frequency, @NotNull State state, @Nullable List<String> payload) {
        final Data newData = new Data(state, payload);
        final Data oldData = DATABASE.put(frequency, newData);

        if (state != State.OPERATIONAL && (oldData == null || oldData != newData)) {
            tryMarkDirty();
        }
    }

    private static void tryMarkDirty() {
        if (INSTANCE != null) {
            INSTANCE.markDirty();
        }
    }

    private static byte[] dumpUUID(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    private static UUID reconstituteUUID(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    /** Data transmitted by a Metrics Transmitter cover. */
    public static class Data {

        private final State state;
        private final List<String> payload;

        public Data(@NotNull State state, @Nullable List<String> payload) {
            this.state = state;
            this.payload = payload;
        }

        /**
         * Retrieves the payload for this data. Only present if the frequency is in an operational state. Will be
         * cleared if the frequency goes into a deconstructed or self-destructed state.
         *
         * @return The data if present, or an empty Optional otherwise.
         */
        public Optional<List<String>> getPayload() {
            return Optional.ofNullable(payload);
        }

        /**
         * Provides the payload as a stream.
         *
         * @return A stream of the payload, or an empty stream if the payload is missing (e.g., when frequency is not in
         *         an operational state.)
         */
        public Stream<String> getPayloadStream() {
            if (payload == null) {
                return Stream.empty();
            }

            return payload.stream();
        }

        /**
         * Gets the state of the frequency.
         *
         * @return The state
         */
        public State getState() {
            return state;
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, payload);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof final Data otherData)) {
                return false;
            }

            return this.state == otherData.state && Objects.equals(this.payload, otherData.payload);
        }
    }

    public enum State {
        /** The machine is online and broadcasting metrics. */
        OPERATIONAL,
        /**
         * The machine was picked up, but the cover is still attached. Will transition to operational state if the
         * machine is placed back down and started up again.
         */
        DECONSTRUCTED,
        /**
         * Cover was removed from its host machine. Any frequency in this state will no longer get updates nor leave
         * this state.
         */
        SELF_DESTRUCTED
    }
}
