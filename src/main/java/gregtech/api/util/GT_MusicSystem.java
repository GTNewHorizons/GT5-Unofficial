package gregtech.api.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector4i;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jorbis.VorbisFile;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_MusicSystemData;
import gregtech.client.ElectricJukeboxSound;
import gregtech.common.items.GT_WirelessHeadphones;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_BetterJukebox;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * A system that keeps track of jukebox music tracks playing in different locations.
 * Compared to vanilla jukebox handling, this allows music to resume playing after reloading the chunk the jukeboxes are
 * in.
 * It also allows the headphone item to modify the hearing range of a given disc, including other dimensions.
 * <p>
 * Vector4i coordinates point to X,Y,Z,Dimension of the source
 *
 * @author eigenraven
 */
public final class GT_MusicSystem {

    private GT_MusicSystem() {}

    public static final class MusicSource {

        /** Flag keeping track of when the data of this source needs to be sent to clients again */
        public boolean modified;
        public final UUID sourceID;
        /** Currently playing track */
        public ResourceLocation currentRecord;
        /** Headphone range */
        public GT_MetaTileEntity_BetterJukebox.HeadphoneLimit headphoneLimit;
        /**
         * {@link System#currentTimeMillis()} at the time this record started playing, in server time
         */
        public long startedPlayingAtMs;
        /** Time the record was playing for at the time it was serialized. */
        public long playingForMs;
        /** The origin of this source used for wireless headphone range calculations */
        public final Vector4i originPosition = new Vector4i();
        /** Number of blocks from {@link MusicSource#originPosition} the headphones can work */
        public int headphoneBlockRange;
        /** Densely packed parameters for each "emitter" associated with the source, for fast iteration */
        public int[] emitterParameters; // [{x,y,z,dim,volume}, {x, ...}, {x, ...}, ...]
        /** Offsets into the parameters array */
        public static final int EMITTER_X = 0;
        /** Offsets into the parameters array */
        public static final int EMITTER_Y = 1;
        /** Offsets into the parameters array */
        public static final int EMITTER_Z = 2;
        /** Offsets into the parameters array */
        public static final int EMITTER_DIMENSION = 3;
        /** 100 times the floating point "volume" used by the sound system for range calculations */
        public static final int EMITTER_VOLUME_X_100 = 4;
        /** Iteration stride for packed emitter parameters */
        public static final int EMITTER_STRIDE = EMITTER_VOLUME_X_100 + 1;

        public MusicSource(UUID sourceID) {
            this.sourceID = sourceID;
        }

        public void resizeEmitterArray(int count) {
            int len = count * EMITTER_STRIDE;
            if (emitterParameters == null || emitterParameters.length != len) {
                if (emitterParameters == null) {
                    emitterParameters = new int[len];
                } else {
                    emitterParameters = Arrays.copyOf(emitterParameters, len);
                }
                modified = true;
            }
        }

        public void setEmitter(int index, Vector4i position, float volume) {
            int arrIndex = index * EMITTER_STRIDE;
            if (arrIndex < 0 || arrIndex >= emitterParameters.length) {
                throw new IndexOutOfBoundsException(
                    "Trying to access emitter with index " + index
                        + " in an array of "
                        + emitterParameters.length / EMITTER_STRIDE);
            }

            if (emitterParameters[arrIndex + EMITTER_X] != position.x) {
                modified = true;
                emitterParameters[arrIndex + EMITTER_X] = position.x;
            }
            if (emitterParameters[arrIndex + EMITTER_Y] != position.y) {
                modified = true;
                emitterParameters[arrIndex + EMITTER_Y] = position.y;
            }
            if (emitterParameters[arrIndex + EMITTER_Z] != position.z) {
                modified = true;
                emitterParameters[arrIndex + EMITTER_Z] = position.z;
            }
            if (emitterParameters[arrIndex + EMITTER_DIMENSION] != position.w) {
                modified = true;
                emitterParameters[arrIndex + EMITTER_DIMENSION] = position.w;
            }
            final int intVolume = (int) (volume * 100.0f);
            if (emitterParameters[arrIndex + EMITTER_VOLUME_X_100] != intVolume) {
                modified = true;
                emitterParameters[arrIndex + EMITTER_VOLUME_X_100] = intVolume;
            }
        }

        /** x squared */
        private static int sq(int x) {
            return x * x;
        }

        /** @return Index of closest emitter in range, or -1 if none is nearby. */
        public int closestEmitter(int x, int y, int z, int dim) {
            int closest = -1;
            int closestDistanceSq = Integer.MAX_VALUE;
            final int emittersCount = emitterParameters.length / EMITTER_STRIDE;
            for (int i = 0; i < emittersCount; i++) {
                final int offset = i * EMITTER_STRIDE;
                final int eDim = emitterParameters[offset + EMITTER_DIMENSION];
                if (eDim != dim) {
                    continue;
                }
                final int eX = emitterParameters[offset + EMITTER_X];
                final int eY = emitterParameters[offset + EMITTER_Y];
                final int eZ = emitterParameters[offset + EMITTER_Z];
                final int distanceSq = sq(x - eX) + sq(y - eY) + sq(z - eZ);
                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closest = i;
                }
            }
            return closest;
        }

        public boolean inHeadphoneRange(int x, int y, int z, int dim) {
            return switch (headphoneLimit) {
                case BETWEEN_DIMENSIONS -> true;
                case INSIDE_DIMENSION -> dim == originPosition.w;
                case BLOCK_RANGE -> dim == originPosition.w
                    && originPosition.distanceSquared(x, y, z, dim) <= sq(headphoneBlockRange);
            };
        }

        public void encode(final ByteBuf target) {
            target.writeLong(sourceID.getMostSignificantBits());
            target.writeLong(sourceID.getLeastSignificantBits());
            if (currentRecord != null) {
                final int duration = getMusicRecordDurations().getOrDefault(currentRecord, Integer.MAX_VALUE);
                if (playingForMs >= duration) {
                    // Record already finished playing, let's not send it to the client anymore.
                    target.writeBoolean(false);
                } else {
                    target.writeBoolean(true);
                    ByteBufUtils.writeUTF8String(target, currentRecord.getResourceDomain());
                    ByteBufUtils.writeUTF8String(target, currentRecord.getResourcePath());
                }
            } else {
                target.writeBoolean(false);
            }
            target.writeByte((byte) headphoneLimit.ordinal());
            ByteBufUtils.writeVarInt(target, headphoneBlockRange, 5);
            target.writeLong(startedPlayingAtMs);
            target.writeLong(playingForMs);
            ByteBufUtils.writeVarInt(target, originPosition.x, 5);
            ByteBufUtils.writeVarInt(target, originPosition.y, 5);
            ByteBufUtils.writeVarInt(target, originPosition.z, 5);
            ByteBufUtils.writeVarInt(target, originPosition.w, 5);
            ByteBufUtils.writeVarInt(target, emitterParameters.length, 5);
            for (int emitterParameter : emitterParameters) {
                ByteBufUtils.writeVarInt(target, emitterParameter, 5);
            }
        }

        public static MusicSource decode(final ByteBuf bytes) {
            final long uuidMsb = bytes.readLong();
            final long uuidLsb = bytes.readLong();
            final MusicSource source = new MusicSource(new UUID(uuidMsb, uuidLsb));
            final boolean hasRecord = bytes.readBoolean();
            if (hasRecord) {
                final String domain = ByteBufUtils.readUTF8String(bytes);
                final String path = ByteBufUtils.readUTF8String(bytes);
                source.currentRecord = new ResourceLocation(domain, path);
            }
            source.headphoneLimit = GT_MetaTileEntity_BetterJukebox.HeadphoneLimit.ENTRIES.get(bytes.readByte());
            source.headphoneBlockRange = ByteBufUtils.readVarInt(bytes, 5);
            source.startedPlayingAtMs = bytes.readLong();
            source.playingForMs = bytes.readLong();
            final int originX = ByteBufUtils.readVarInt(bytes, 5);
            final int originY = ByteBufUtils.readVarInt(bytes, 5);
            final int originZ = ByteBufUtils.readVarInt(bytes, 5);
            final int originW = ByteBufUtils.readVarInt(bytes, 5);
            source.originPosition.set(originX, originY, originZ, originW);
            final int emittersLength = ByteBufUtils.readVarInt(bytes, 5);
            source.emitterParameters = new int[emittersLength];
            for (int i = 0; i < emittersLength; i++) {
                source.emitterParameters[i] = ByteBufUtils.readVarInt(bytes, 5);
            }

            return source;
        }

        public void setRecord(final ResourceLocation record) {
            setRecord(record, 0);
        }

        public void setRecord(final ResourceLocation record, long seekOffset) {
            modified = true;
            currentRecord = record;
            playingForMs = seekOffset;
            startedPlayingAtMs = System.currentTimeMillis() - seekOffset;
        }
    }

    public static final class ServerSystem {

        static final Object2ObjectOpenHashMap<UUID, MusicSource> musicSources = new Object2ObjectOpenHashMap<>(32);
        static boolean musicSourcesDirty = false;

        // Everything is synchronized to allow calling into here from the client when singleplayer synchronization is
        // needed.

        public static synchronized MusicSource registerOrGetMusicSource(UUID uuid) {
            return musicSources.computeIfAbsent(uuid, (UUID id) -> {
                musicSourcesDirty = true;
                return new MusicSource(id);
            });
        }

        public static synchronized void removeMusicSource(UUID uuid) {
            musicSources.remove(uuid);
            musicSourcesDirty = true;
        }

        public static synchronized void reset() {
            musicSources.clear();
            musicSourcesDirty = true;
        }

        public static synchronized ByteBuf serialize() {
            final ByteBuf out = Unpooled.buffer();
            ByteBufUtils.writeVarInt(out, musicSources.size(), 5);
            musicSources.forEach((uuid, source) -> source.encode(out));
            return out;
        }

        private static boolean tickAnyDirty;

        public static synchronized void tick() {
            final long now = System.currentTimeMillis();
            tickAnyDirty = false;
            musicSources.forEach((uuid, source) -> {
                source.playingForMs = now - source.startedPlayingAtMs;
                tickAnyDirty |= source.modified;
                source.modified = false;
            });
            if (tickAnyDirty || musicSourcesDirty) {
                musicSourcesDirty = false;
                GT_Values.NW.sendToAll(new GT_Packet_MusicSystemData(serialize()));
            }
        }

        static synchronized void onPauseMs(long pauseDurationMs) {
            musicSources.forEach((uuid, source) -> { source.startedPlayingAtMs += pauseDurationMs; });
        }
    }

    public static final class ClientSystem {

        private static final class ClientSourceData {

            /** Currently playing sound data */
            public ElectricJukeboxSound currentSound = null;
            /** Currently playing sound data */
            public ResourceLocation currentSoundResource = null;
            /**
             * Server's timer value of when the music started, mostly meaningless except for checking for replays of the
             * same music file.
             */
            public long originalStartTime = 0;
            /**
             * Computed client value of {@link System#currentTimeMillis()} of the time when the playback would have
             * started if it was synchronized with the server.
             */
            public long clientReferenceStartTime = 0;
            /** Flag for mark and sweep removal of outdated sounds */
            public boolean markFlag = false;

            public void resetMark() {
                markFlag = false;
            }

            public void mark() {
                markFlag = true;
            }

            public void clearSound(final Minecraft mc) {
                currentSoundResource = null;
                if (currentSound != null) {
                    mc.getSoundHandler()
                        .stopSound(currentSound);
                    currentSound = null;
                    originalStartTime = 0;
                }
            }

            public boolean equalSound(final MusicSource source) {
                if (source == null || source.currentRecord == null) {
                    return currentSoundResource == null;
                } else {
                    return source.currentRecord.equals(currentSoundResource)
                        && originalStartTime == source.startedPlayingAtMs;
                }
            }

            public void resetSound(final Minecraft mc, final MusicSource source, final boolean onHeadphones) {
                clearSound(mc);
                if (source == null || source.emitterParameters.length == 0) {
                    return;
                }
                int closestEmitter = onHeadphones ? 0
                    : source.closestEmitter(
                        (int) Math.floor(mc.thePlayer.posX),
                        (int) Math.floor(mc.thePlayer.posY),
                        (int) Math.floor(mc.thePlayer.posZ),
                        currentDimension);
                if (closestEmitter < 0) {
                    return;
                }
                this.currentSoundResource = source.currentRecord;
                this.originalStartTime = source.startedPlayingAtMs;
                this.clientReferenceStartTime = System.currentTimeMillis() - source.playingForMs;
                if (currentSoundResource != null) {
                    this.currentSound = makeRecord(source, closestEmitter);
                    if (onHeadphones) {
                        this.currentSound.volume = 1.0e20f;
                    }
                    mc.getSoundHandler()
                        .playSound(this.currentSound);
                }
            }

            public void updateSound(final Minecraft mc, final MusicSource source, final boolean onHeadphones) {
                if (source == null || currentSound == null || source.emitterParameters.length == 0) {
                    return;
                }
                int closestEmitter = onHeadphones ? 0
                    : source.closestEmitter(
                        (int) Math.floor(mc.thePlayer.posX),
                        (int) Math.floor(mc.thePlayer.posY),
                        (int) Math.floor(mc.thePlayer.posZ),
                        currentDimension);
                if (closestEmitter < 0) {
                    currentSound.volume = 0.0f;
                    return;
                }
                final int offset = closestEmitter * MusicSource.EMITTER_STRIDE;
                currentSound.xPosition = source.emitterParameters[offset + MusicSource.EMITTER_X];
                currentSound.yPosition = source.emitterParameters[offset + MusicSource.EMITTER_Y];
                currentSound.zPosition = source.emitterParameters[offset + MusicSource.EMITTER_Z];
                currentSound.volume = onHeadphones ? 1.0e20f
                    : source.emitterParameters[offset + MusicSource.EMITTER_VOLUME_X_100] / 100.0f;
            }
        }

        /** Latest music source list as synchronized from the server */
        public static final Object2ObjectOpenHashMap<UUID, MusicSource> musicSources = new Object2ObjectOpenHashMap<>();

        private static final Object2ObjectOpenHashMap<UUID, ClientSourceData> activelyPlayingMusic = new Object2ObjectOpenHashMap<>(
            16);

        private static final ObjectOpenHashSet<UUID> wornHeadphones = new ObjectOpenHashSet<>();

        private static int currentDimension = Integer.MIN_VALUE;

        private static boolean soundsPaused = false;
        private static long pauseTimeMs = 0;
        private static int tickCounter = 0;

        public static void loadUpdatedSources(ByteBuf bytes) {
            final int sourceCount = ByteBufUtils.readVarInt(bytes, 5);
            musicSources.clear();
            for (int i = 0; i < sourceCount; i++) {
                final MusicSource source = MusicSource.decode(bytes);
                musicSources.put(source.sourceID, source);
            }
        }

        private static ElectricJukeboxSound makeRecord(MusicSource source, int emitter) {
            final int x = source.emitterParameters[emitter * MusicSource.EMITTER_STRIDE + MusicSource.EMITTER_X];
            final int y = source.emitterParameters[emitter * MusicSource.EMITTER_STRIDE + MusicSource.EMITTER_Y];
            final int z = source.emitterParameters[emitter * MusicSource.EMITTER_STRIDE + MusicSource.EMITTER_Z];
            final float volume = source.emitterParameters[emitter * MusicSource.EMITTER_STRIDE
                + MusicSource.EMITTER_VOLUME_X_100] / 100.0f;
            return new ElectricJukeboxSound(source.currentRecord, volume, source.playingForMs, x, y, z);
        }

        public static void dumpAllRecordDurations() {
            try {
                final Minecraft mc = Minecraft.getMinecraft();
                final SoundRegistry sm = mc.getSoundHandler().sndRegistry;
                final SoundDurationsJson json = new SoundDurationsJson();
                @SuppressWarnings("unchecked")
                final Map<String, ItemRecord> allRecords = ItemRecord.field_150928_b;
                // Cursed hack because JOrbis does not support seeking in anything other than filesystem files.
                // This is only a dev tool, so it can be a bit slow and use real files here.
                final File tempFile = File.createTempFile("mcdecode", ".ogg");
                for (final ItemRecord record : allRecords.values()) {
                    try {
                        final ResourceLocation res = record.getRecordResource(record.recordName);
                        SoundEventAccessorComposite registryEntry = (SoundEventAccessorComposite) sm.getObject(res);
                        if (registryEntry == null) {
                            registryEntry = (SoundEventAccessorComposite) sm.getObject(
                                new ResourceLocation(res.getResourceDomain(), "records." + res.getResourcePath()));
                        }
                        final ResourceLocation realPath = registryEntry.func_148720_g()
                            .getSoundPoolEntryLocation();
                        try (final InputStream is = mc.getResourceManager()
                            .getResource(realPath)
                            .getInputStream(); final OutputStream os = FileUtils.openOutputStream(tempFile)) {
                            IOUtils.copy(is, os);
                            os.close();
                            final VorbisFile vf = new VorbisFile(tempFile.getAbsolutePath());
                            final float totalSeconds = vf.time_total(-1);
                            json.soundDurationsMs.put(res.toString(), (int) Math.ceil(totalSeconds * 1000.0f));
                        }
                    } catch (Exception e) {
                        GT_Mod.GT_FML_LOGGER.warn("Skipping {}", record.recordName, e);
                    }
                }
                GT_Mod.GT_FML_LOGGER.info(
                    "Sound durations json: \n{}",
                    new GsonBuilder().setPrettyPrinting()
                        .create()
                        .toJson(json));
                tempFile.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void reset() {
            musicSources.clear();
            tick();
        }

        public static void tick() {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.renderGlobal == null
                || mc.theWorld == null
                || mc.thePlayer == null
                || mc.theWorld.provider == null) {
                return;
            }
            tickCounter++;
            final long now = System.currentTimeMillis();
            currentDimension = mc.theWorld.provider.dimensionId;

            headphoneCheck: if ((tickCounter % 20) == 0) {
                wornHeadphones.clear();
                final IInventory baubles = BaublesApi.getBaubles(mc.thePlayer);
                if (baubles == null) {
                    break headphoneCheck;
                }
                final int baublesSize = baubles.getSizeInventory();
                for (int i = 0; i < baublesSize; i++) {
                    final ItemStack item = baubles.getStackInSlot(i);
                    if (item != null && item.getItem() instanceof GT_WirelessHeadphones headphones) {
                        final UUID id = headphones.getBoundJukeboxUUID(item);
                        if (id != null) {
                            wornHeadphones.add(id);
                        }
                    }
                }
            }

            activelyPlayingMusic.forEach((uuid, data) -> data.resetMark());

            // Update and mark all present music streams
            musicSources.forEach((uuid, musicSource) -> {
                final ClientSourceData data = activelyPlayingMusic
                    .computeIfAbsent(uuid, ignored -> new ClientSourceData());
                data.mark();
                if (data.currentSound != null && !mc.getSoundHandler()
                    .isSoundPlaying(data.currentSound)
                    && (now - data.clientReferenceStartTime)
                        < getMusicRecordDurations().getOrDefault(data.currentSoundResource, Integer.MAX_VALUE)) {
                    data.currentSound = null;
                    data.currentSoundResource = null;
                }
                final boolean onHeadphones = wornHeadphones.contains(uuid);
                if (!data.equalSound(musicSource)) {
                    data.resetSound(mc, musicSource, onHeadphones);
                } else {
                    data.updateSound(mc, musicSource, onHeadphones);
                }
            });

            // Sweep no longer present music streams
            final var entries = activelyPlayingMusic.object2ObjectEntrySet()
                .fastIterator();
            while (entries.hasNext()) {
                final ClientSourceData entry = entries.next()
                    .getValue();
                if (!entry.markFlag) {
                    entry.clearSound(mc);
                    entries.remove();
                }
            }
        }

        @ApiStatus.Internal
        public static void onSoundBatchStop() {
            // All music was forcibly stopped, we can forget about the currently playing music
            // and let the update loop re-start them on next tick
            long now = System.currentTimeMillis();
            activelyPlayingMusic.forEach((uuid, data) -> {
                data.currentSound = null;
                data.currentSoundResource = null;
                final MusicSource source = musicSources.get(uuid);
                if (source == null) {
                    return;
                }
                source.playingForMs = now - data.clientReferenceStartTime;
            });
        }

        @ApiStatus.Internal
        public static void onSoundBatchPause() {
            if (soundsPaused) {
                return;
            }
            soundsPaused = true;
            pauseTimeMs = System.currentTimeMillis();
        }

        @ApiStatus.Internal
        public static void onSoundBatchResume() {
            if (!soundsPaused) {
                return;
            }
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.renderGlobal == null
                || mc.theWorld == null
                || mc.thePlayer == null
                || mc.theWorld.provider == null) {
                return;
            }
            soundsPaused = false;

            if (!(mc.isSingleplayer() && !mc.getIntegratedServer()
                .getPublic())) {
                return;
            }
            final long now = System.currentTimeMillis();
            final long pauseDurationMs = now - pauseTimeMs;

            // We manipulate server state here, because we've checked this is singleplayer pausing.
            GT_MusicSystem.ServerSystem.onPauseMs(pauseDurationMs);
            musicSources.forEach((uuid, source) -> { source.startedPlayingAtMs += pauseDurationMs; });
            activelyPlayingMusic.forEach((uuid, data) -> {
                data.originalStartTime += pauseDurationMs;
                data.clientReferenceStartTime += pauseDurationMs;
            });

        }
    }

    private static final Object2IntOpenHashMap<ResourceLocation> musicRecordDurations = new Object2IntOpenHashMap<>();
    private static volatile boolean musicRecordsInitialized;

    /** For GSON consumption */
    public static class SoundDurationsJson {

        public Map<String, Integer> soundDurationsMs = new TreeMap<>();
    }

    public static Object2IntOpenHashMap<ResourceLocation> getMusicRecordDurations() {
        if (musicRecordsInitialized) {
            return musicRecordDurations;
        }
        // double-checked locking for efficiency
        synchronized (musicRecordDurations) {
            if (musicRecordsInitialized) {
                return musicRecordDurations;
            }

            final Gson gson = new Gson();

            try {
                final ArrayList<URL> candidates = Collections.list(
                    GT_MusicSystem.class.getClassLoader()
                        .getResources("soundmeta/durations.json"));
                final Path configPath = Launch.minecraftHome.toPath()
                    .resolve("config")
                    .resolve("soundmeta")
                    .resolve("durations.json");
                if (Files.exists(configPath)) {
                    candidates.add(
                        configPath.toUri()
                            .toURL());
                }
                for (final URL url : candidates) {
                    try {
                        final String objectJson = IOUtils.toString(url);
                        final SoundDurationsJson object = gson.fromJson(objectJson, SoundDurationsJson.class);
                        if (object == null || object.soundDurationsMs == null || object.soundDurationsMs.isEmpty()) {
                            continue;
                        }
                        for (final var entry : object.soundDurationsMs.entrySet()) {
                            musicRecordDurations.put(
                                new ResourceLocation(entry.getKey()),
                                entry.getValue()
                                    .intValue());
                        }
                    } catch (Exception e) {
                        GT_Mod.GT_FML_LOGGER.error("Could not parse sound durations from {}", url, e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            musicRecordsInitialized = true;
            return musicRecordDurations;
        }
    }

}
