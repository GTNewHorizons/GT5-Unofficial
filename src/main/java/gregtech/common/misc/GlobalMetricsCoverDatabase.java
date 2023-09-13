package gregtech.common.misc;

import static net.minecraftforge.common.util.Constants.NBT.TAG_BYTE_ARRAY;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.GT_Cover_Metrics_Transmitter;
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

    /** Holds received metrics. */
    private static final Map<UUID, Data> DATABASE = new ConcurrentHashMap<>();
    /** Used to speed up event handlers dealing with block breaking and explosions. Not persisted. */
    private static final Map<Coordinates, Set<UUID>> REVERSE_LOOKUP = new ConcurrentHashMap<>();

    private static final String DATA_NAME = "GregTech_MetricsCoverDatabase";
    private static final String DECONSTRUCTED_KEY = "GregTech_MetricsCoverDatabase_Deconstructed";
    private static final String SELF_DESTRUCTED_KEY = "GregTech_MetricsCoverDatabase_SelfDestructed";

    public GlobalMetricsCoverDatabase() {
        this(DATA_NAME);
    }

    public GlobalMetricsCoverDatabase(String name) {
        super(name);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveMetricsData(MetricsCoverDataEvent event) {
        final Coordinates coordinates = event.getCoordinates();
        store(event.getFrequency(), State.OPERATIONAL, event.getPayload(), coordinates);

        if (!REVERSE_LOOKUP.containsKey(coordinates)) {
            REVERSE_LOOKUP.put(coordinates, new HashSet<>());
        }
        REVERSE_LOOKUP.get(coordinates)
            .add(event.getFrequency());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveHostDeconstructed(MetricsCoverHostDeconstructedEvent event) {
        cullReverseLookupEntry(event.getFrequency());
        store(event.getFrequency(), State.HOST_DECONSTRUCTED);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void receiveSelfDestruct(MetricsCoverSelfDestructEvent event) {
        cullReverseLookupEntry(event.getFrequency());
        store(event.getFrequency(), State.SELF_DESTRUCTED);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote || event.world.provider.dimensionId != 0) {
            return;
        }

        DATABASE.clear();

        final MapStorage storage = event.world.mapStorage;
        INSTANCE = (GlobalMetricsCoverDatabase) storage.loadData(GlobalMetricsCoverDatabase.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new GlobalMetricsCoverDatabase();
            storage.setData(DATA_NAME, INSTANCE);
        }

        INSTANCE.markDirty();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        final Coordinates coords = new Coordinates(event.world.provider.getDimensionName(), event.x, event.y, event.z);
        // In case someone else wants to listen to these, go the roundabout way.
        final Set<UUID> uuids = REVERSE_LOOKUP.get(coords);
        if (uuids != null) {
            uuids.forEach(
                uuid -> MinecraftForge.EVENT_BUS.post(
                    ForgeHooks.canHarvestBlock(event.block, event.getPlayer(), event.blockMetadata)
                        && !event.getPlayer().capabilities.isCreativeMode ? new MetricsCoverHostDeconstructedEvent(uuid)
                            : new MetricsCoverSelfDestructEvent(uuid)));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onExplosion(Detonate event) {
        final String dimensionName = event.world.provider.getDimensionName();

        event.getAffectedBlocks()
            .forEach(chunkPosition -> {
                final Set<UUID> uuids = REVERSE_LOOKUP.get(
                    new Coordinates(
                        dimensionName,
                        chunkPosition.chunkPosX,
                        chunkPosition.chunkPosY,
                        chunkPosition.chunkPosZ));

                if (uuids != null) {
                    uuids.forEach(uuid -> MinecraftForge.EVENT_BUS.post(new MetricsCoverSelfDestructEvent(uuid)));
                }
            });

        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof final EntityItem entityItem) {
                getCoverUUIDsFromItemStack(entityItem.getEntityItem())
                    .forEach(uuid -> MinecraftForge.EVENT_BUS.post(new MetricsCoverSelfDestructEvent(uuid)));
            }
        });
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemExpiration(ItemExpireEvent event) {
        getCoverUUIDsFromItemStack(event.entityItem.getEntityItem())
            .forEach(uuid -> MinecraftForge.EVENT_BUS.post(new MetricsCoverSelfDestructEvent(uuid)));
    }

    /**
     * Get the data for a frequency, if it exists.
     *
     * @param frequency The UUID corresponding to the frequency to retrieve.
     * @return An Optional with the frequency's data, or an empty Optional if it doesn't exist.
     */
    @NotNull
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

        for (int i = 0; i < deconstructed.tagCount(); i++) {
            final NBTTagByteArray byteArray = (NBTTagByteArray) deconstructed.removeTag(0);
            reconstituteUUID(byteArray.func_150292_c())
                .ifPresent(uuid -> DATABASE.put(uuid, new Data(State.HOST_DECONSTRUCTED)));
        }

        for (int i = 0; i < selfDestructed.tagCount(); i++) {
            final NBTTagByteArray byteArray = (NBTTagByteArray) selfDestructed.removeTag(0);
            reconstituteUUID(byteArray.func_150292_c())
                .ifPresent(uuid -> DATABASE.put(uuid, new Data(State.SELF_DESTRUCTED)));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        // We only care about persisting frequencies that aren't operational.
        final NBTTagList deconstructed = new NBTTagList();
        final NBTTagList selfDestructed = new NBTTagList();
        DATABASE.forEach((uuid, data) -> {
            switch (data.getState()) {
                case HOST_DECONSTRUCTED -> deconstructed.appendTag(new NBTTagByteArray(dumpUUID(uuid)));
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
     */
    private static void store(@NotNull UUID frequency, @NotNull State state) {
        store(frequency, state, null, null);
    }

    /**
     * Stores the new result and flag the static {@link MapStorage} instance as dirty if the information updated. Will
     * not flag dirty for any data in the {@link State#OPERATIONAL OPERATIONAL} state since they aren't stored.
     *
     * @param frequency   Maps to a unique deployed cover.
     * @param state       The new cover state.
     * @param payload     A list of strings to display on the information panel, if the card is slotted properly.
     * @param coordinates Coordinates of the active machine (including dimension.)
     */
    private static void store(@NotNull UUID frequency, @NotNull State state, @Nullable List<String> payload,
        @Nullable Coordinates coordinates) {
        final Data newData = new Data(state, payload, coordinates);
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

    @NotNull
    private static Optional<UUID> reconstituteUUID(byte[] bytes) throws IllegalArgumentException {
        if (bytes.length != 16) {
            return Optional.empty();
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return Optional.of(new UUID(buffer.getLong(), buffer.getLong()));
    }

    private static void cullReverseLookupEntry(UUID frequency) {
        getData(frequency).ifPresent(data -> {
            if (data.state == State.OPERATIONAL && REVERSE_LOOKUP.containsKey(data.coordinates)) {
                final Set<UUID> set = REVERSE_LOOKUP.get(data.coordinates);
                set.remove(frequency);
                if (set.isEmpty()) {
                    REVERSE_LOOKUP.remove(data.coordinates);
                }
            }
        });
    }

    private static Stream<UUID> getCoverUUIDsFromItemStack(final ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey(GT_Values.NBT.COVERS, TAG_COMPOUND)) {
            final NBTTagList tagList = stack.getTagCompound()
                .getTagList(GT_Values.NBT.COVERS, TAG_COMPOUND);
            return IntStream.range(0, tagList.tagCount())
                .mapToObj(tagList::getCompoundTagAt)
                .map(nbt -> new CoverInfo(null, nbt).getCoverData())
                .filter(
                    serializableObject -> serializableObject instanceof GT_Cover_Metrics_Transmitter.MetricsTransmitterData)
                .map(data -> ((GT_Cover_Metrics_Transmitter.MetricsTransmitterData) data).getFrequency());
        }
        return Stream.empty();
    }

    /**
     * Data transmitted by a Metrics Transmitter cover.
     * <p>
     * Since only negative states ({@link State#HOST_DECONSTRUCTED HOST_DECONSTRUCTED} and
     * {@link State#SELF_DESTRUCTED SELF DESTRUCTED}) are persisted, additional fields can be added to this data with
     * little consequence. Ensure that any new fields are nullable, and make any getter for these fields return an
     * {@link Optional}.
     */
    public static class Data {

        @NotNull
        private final State state;
        @Nullable
        private final List<String> payload;
        @Nullable
        private final Coordinates coordinates;

        public Data(@NotNull State state) {
            this.state = state;
            this.payload = null;
            this.coordinates = null;
        }

        public Data(@NotNull State state, @Nullable List<String> payload) {
            this.state = state;
            this.payload = payload;
            this.coordinates = null;
        }

        public Data(@NotNull State state, @Nullable List<String> payload, @Nullable Coordinates coordinates) {
            this.state = state;
            this.payload = payload;
            this.coordinates = coordinates;

        }

        /**
         * Retrieves the payload for this data. Only present if the frequency is in an
         * {@link State#OPERATIONAL operational} state. Will be cleared if the frequency goes into a
         * {@link State#HOST_DECONSTRUCTED host-deconstructed} or {@link State#SELF_DESTRUCTED self-destructed} state.
         *
         * @return The data if present, or an empty Optional otherwise.
         */
        @NotNull
        public Optional<List<String>> getPayload() {
            return Optional.ofNullable(payload);
        }

        /**
         * Gets the state of the frequency.
         *
         * @return The state
         */
        @NotNull
        public State getState() {
            return state;
        }

        /**
         * Gets the last known coordinates for the machine broadcasting metrics. Will only be present in an
         * {@link State#OPERATIONAL operational} state.
         *
         * @return The coordinates
         */
        @NotNull
        public Optional<Coordinates> getCoordinates() {
            return Optional.ofNullable(coordinates);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Data data = (Data) o;
            return state == data.state && Objects.equals(payload, data.payload)
                && Objects.equals(coordinates, data.coordinates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, payload, coordinates);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class Coordinates {

        private final String dimension;
        private final int x;
        private final int y;
        private final int z;

        public Coordinates(final String dimension, final int x, final int y, final int z) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public String getDimension() {
            return dimension;
        }

        public String getLocalizedCoordinates() {
            return StatCollector.translateToLocalFormatted(
                "gt.db.metrics_cover.coords",
                GT_Utility.formatNumbers(x),
                GT_Utility.formatNumbers(y),
                GT_Utility.formatNumbers(z));
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Coordinates that = (Coordinates) o;
            return x == that.x && y == that.y && z == that.z && Objects.equals(dimension, that.dimension);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dimension, x, y, z);
        }
    }

    public enum State {
        // NOTE: type cannot be 0, as NuclearControl returns a 0 when querying for an integer from an item stack's NBT
        // data when it really means null.

        /** The machine is online and broadcasting metrics. */
        OPERATIONAL(1),
        /**
         * The machine was picked up, but the cover is still attached. Will transition to operational state if the
         * machine is placed back down and started up again.
         */
        HOST_DECONSTRUCTED(2),
        /**
         * Cover was removed from its host machine, or machine was destroyed (in the limited number of ways we can
         * detect.) Any frequency in this state will no longer get updates nor leave this state.
         */
        SELF_DESTRUCTED(3);

        private static final Map<Integer, State> VALID_TYPE_INTEGERS = Arrays.stream(State.values())
            .collect(Collectors.toMap(State::getType, Function.identity()));
        private final int type;

        State(final int type) {
            if (type <= 0) {
                throw new IllegalArgumentException("A state must have a positive, nonzero type parameter.");
            }
            this.type = type;
        }

        @NotNull
        public static Optional<State> find(int candidate) {
            return Optional.ofNullable(VALID_TYPE_INTEGERS.get(candidate));
        }

        public int getType() {
            return type;
        }
    }
}
