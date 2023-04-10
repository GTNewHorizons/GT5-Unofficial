package gregtech.api.util;

import static gregtech.api.enums.Mods.GregTech;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * A utility to save all kinds of data that is a function of any chunk.
 * <p>
 * GregTech takes care of saving and loading the data from disk, and an efficient mechanism to locate it. Subclass only
 * need to define the exact scheme of each element data (by overriding the three protected abstract method)
 * <p>
 * Oh, there is no limit on how large your data is, though you'd not have the familiar NBT interface, but DataOutput
 * should be reasonably common anyway.
 * <p>
 * It should be noted this class is NOT thread safe.
 * <p>
 * Element cannot be null.
 * <p>
 * TODO: Implement automatic region unloading.
 *
 * @param <T> data element type
 * @author glease
 */
@ParametersAreNonnullByDefault
public abstract class GT_ChunkAssociatedData<T extends GT_ChunkAssociatedData.IData> {

    private static final Map<String, GT_ChunkAssociatedData<?>> instances = new ConcurrentHashMap<>();
    private static final int IO_PARALLELISM = Math.min(
        8,
        Math.max(
            1,
            Runtime.getRuntime()
                .availableProcessors() * 2
                / 3));
    private static final ExecutorService IO_WORKERS = Executors.newWorkStealingPool(IO_PARALLELISM);
    private static final Pattern FILE_PATTERN = Pattern.compile("(.+)\\.(-?\\d+)\\.(-?\\d+)\\.dat");

    static {
        // register event handler
        new EventHandler();
    }

    protected final String mId;
    protected final Class<T> elementtype;
    private final int regionLength;
    private final int version;
    private final boolean saveDefaults;
    /**
     * Data is stored as a `(world id -> (super region id -> super region data))` hash map. where super region's size is
     * determined by regionSize. Here it is called super region, to not confuse with vanilla's regions.
     */
    private final Map<Integer, Map<ChunkCoordIntPair, SuperRegion>> masterMap = new ConcurrentHashMap<>();

    /**
     * Initialize this instance.
     *
     * @param aId          An arbitrary, but globally unique identifier for what this data is
     * @param elementType  The class of this element type. Used to create arrays.
     * @param regionLength The length of one super region. Each super region will contain
     *                     {@code regionLength * regionLength} chunks
     * @param version      An integer marking the version of this data. Useful later when the data's serialized form
     *                     changed.
     */
    protected GT_ChunkAssociatedData(String aId, Class<T> elementType, int regionLength, byte version,
        boolean saveDefaults) {
        if (regionLength * regionLength > Short.MAX_VALUE || regionLength <= 0)
            throw new IllegalArgumentException("Region invalid: " + regionLength);
        if (!IData.class.isAssignableFrom(elementType)) throw new IllegalArgumentException("Data type invalid");
        if (aId.contains(".")) throw new IllegalArgumentException("ID cannot contains dot");
        this.mId = aId;
        this.elementtype = elementType;
        this.regionLength = regionLength;
        this.version = version;
        this.saveDefaults = saveDefaults;
        if (instances.putIfAbsent(aId, this) != null)
            throw new IllegalArgumentException("Duplicate GT_ChunkAssociatedData: " + aId);
    }

    private ChunkCoordIntPair getRegionID(int aChunkX, int aChunkZ) {
        return new ChunkCoordIntPair(Math.floorDiv(aChunkX, regionLength), Math.floorDiv(aChunkZ, regionLength));
    }

    /**
     * Get a reference to data of the chunk that tile entity is in. The returned reference should be mutable.
     */
    public final T get(IGregTechTileEntity tileEntity) {
        return get(tileEntity.getWorld(), tileEntity.getXCoord() >> 4, tileEntity.getZCoord() >> 4);
    }

    public final T get(Chunk chunk) {
        return get(chunk.worldObj, chunk.xPosition, chunk.zPosition);
    }

    public final T get(World world, ChunkCoordIntPair coord) {
        return get(world, coord.chunkXPos, coord.chunkZPos);
    }

    public final T get(World world, int chunkX, int chunkZ) {
        SuperRegion region = masterMap.computeIfAbsent(world.provider.dimensionId, ignored -> new ConcurrentHashMap<>())
            .computeIfAbsent(getRegionID(chunkX, chunkZ), c -> new SuperRegion(world, c));
        return region.get(Math.floorMod(chunkX, regionLength), Math.floorMod(chunkZ, regionLength));
    }

    protected final void set(World world, int chunkX, int chunkZ, T data) {
        SuperRegion region = masterMap.computeIfAbsent(world.provider.dimensionId, ignored -> new ConcurrentHashMap<>())
            .computeIfAbsent(getRegionID(chunkX, chunkZ), c -> new SuperRegion(world, c));
        region.set(Math.floorMod(chunkX, regionLength), Math.floorMod(chunkZ, regionLength), data);
    }

    protected final boolean isCreated(int dimId, int chunkX, int chunkZ) {
        Map<ChunkCoordIntPair, SuperRegion> dimData = masterMap.getOrDefault(dimId, null);
        if (dimData == null) return false;

        SuperRegion region = dimData.getOrDefault(getRegionID(chunkX, chunkZ), null);
        if (region == null) return false;

        return region.isCreated(Math.floorMod(chunkX, regionLength), Math.floorMod(chunkZ, regionLength));
    }

    public void clear() {
        if (GT_Values.debugWorldData) {
            long dirtyRegionCount = masterMap.values()
                .stream()
                .flatMap(
                    m -> m.values()
                        .stream())
                .filter(SuperRegion::isDirty)
                .count();
            if (dirtyRegionCount > 0) GT_Log.out.println(
                "Clearing ChunkAssociatedData with " + dirtyRegionCount + " regions dirty. Data might have been lost!");
        }
        masterMap.clear();
    }

    public void save() {
        saveRegions(
            masterMap.values()
                .stream()
                .flatMap(
                    m -> m.values()
                        .stream()));
    }

    public void save(World world) {
        Map<ChunkCoordIntPair, SuperRegion> map = masterMap.get(world.provider.dimensionId);
        if (map != null) saveRegions(
            map.values()
                .stream());
    }

    private void saveRegions(Stream<SuperRegion> stream) {
        stream.filter(SuperRegion::isDirty)
            .map(c -> (Runnable) c::save)
            .map(r -> CompletableFuture.runAsync(r, IO_WORKERS))
            .reduce(CompletableFuture::allOf)
            .ifPresent(f -> {
                try {
                    f.get();
                } catch (Exception e) {
                    GT_Log.err.println("Data save error: " + mId);
                    e.printStackTrace(GT_Log.err);
                }
            });
    }

    protected abstract void writeElement(DataOutput output, T element, World world, int chunkX, int chunkZ)
        throws IOException;

    protected abstract T readElement(DataInput input, int version, World world, int chunkX, int chunkZ)
        throws IOException;

    protected abstract T createElement(World world, int chunkX, int chunkZ);

    /**
     * Clear all mappings, regardless of whether they are dirty
     */
    public static void clearAll() {
        for (GT_ChunkAssociatedData<?> d : instances.values()) d.clear();
    }

    /**
     * Save all mappings
     */
    public static void saveAll() {
        for (GT_ChunkAssociatedData<?> d : instances.values()) d.save();
    }

    /**
     * Load data for all chunks for a given world. Current data for that world will be discarded. If this is what you
     * intended, call {@link #save(World)} beforehand.
     * <p>
     * Be aware of the memory consumption though.
     */
    protected void loadAll(World w) {
        if (GT_Values.debugWorldData && masterMap.containsKey(w.provider.dimensionId)) GT_Log.err.println(
            "Reloading ChunkAssociatedData " + mId + " for world " + w.provider.dimensionId + " discards old data!");
        if (!getSaveDirectory(w).isDirectory())
            // nothing to load...
            return;
        try (Stream<Path> stream = Files.list(getSaveDirectory(w).toPath())) {
            Map<ChunkCoordIntPair, SuperRegion> worldData = stream.map(f -> {
                Matcher matcher = FILE_PATTERN.matcher(
                    f.getFileName()
                        .toString());
                return matcher.matches() ? matcher : null;
            })
                .filter(Objects::nonNull)
                .filter(m -> mId.equals(m.group(1)))
                .map(
                    m -> CompletableFuture.supplyAsync(
                        () -> new SuperRegion(w, Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))),
                        IO_WORKERS))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        GT_Log.err.println("Error loading region");
                        e.printStackTrace(GT_Log.err);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SuperRegion::getCoord, Function.identity()));
            masterMap.put(w.provider.dimensionId, worldData);
        } catch (IOException | UncheckedIOException e) {
            GT_Log.err.println("Error loading all region");
            e.printStackTrace(GT_Log.err);
        }
    }

    protected File getSaveDirectory(World w) {
        File base;
        if (w.provider.getSaveFolder() == null) base = w.getSaveHandler()
            .getWorldDirectory();
        else base = new File(
            w.getSaveHandler()
                .getWorldDirectory(),
            w.provider.getSaveFolder());
        return new File(base, GregTech.ID);
    }

    public interface IData {

        /**
         * @return Whether the data is different from chunk default
         */
        boolean isSameAsDefault();
    }

    protected final class SuperRegion {

        private final T[] data = createData();
        private final File backingStorage;
        private final WeakReference<World> world;
        /**
         * Be aware, this means region coord, not bottom-left chunk coord
         */
        private final ChunkCoordIntPair coord;

        private SuperRegion(World world, int regionX, int regionZ) {
            this.world = new WeakReference<>(world);
            this.coord = new ChunkCoordIntPair(regionX, regionZ);
            backingStorage = new File(getSaveDirectory(world), String.format("%s.%d.%d.dat", mId, regionX, regionZ));
            if (backingStorage.isFile()) load();
        }

        private SuperRegion(World world, ChunkCoordIntPair regionCoord) {
            this.world = new WeakReference<>(world);
            this.coord = regionCoord;
            backingStorage = new File(
                getSaveDirectory(world),
                String.format("%s.%d.%d.dat", mId, regionCoord.chunkXPos, regionCoord.chunkZPos));
            if (backingStorage.isFile()) load();
        }

        @SuppressWarnings("unchecked")
        private T[] createData() {
            return (T[]) Array.newInstance(elementtype, regionLength * regionLength);
        }

        public T get(int subRegionX, int subRegionZ) {
            int index = getIndex(subRegionX, subRegionZ);
            T datum = data[index];
            if (datum == null) {
                World world = Objects.requireNonNull(this.world.get());
                T newElem = createElement(
                    world,
                    coord.chunkXPos * regionLength + subRegionX,
                    coord.chunkZPos * regionLength + subRegionZ);
                data[index] = newElem;
                return newElem;
            }
            return datum;
        }

        public void set(int subRegionX, int subRegionZ, T data) {
            this.data[getIndex(subRegionX, subRegionZ)] = data;
        }

        public boolean isCreated(int subRegionX, int subRegionZ) {
            return this.data[getIndex(subRegionX, subRegionZ)] != null;
        }

        public ChunkCoordIntPair getCoord() {
            return coord;
        }

        private int getIndex(int subRegionX, int subRegionY) {
            return subRegionX * regionLength + subRegionY;
        }

        private int getChunkX(int index) {
            return index / regionLength + coord.chunkXPos * regionLength;
        }

        private int getChunkZ(int index) {
            return index % regionLength + coord.chunkZPos * regionLength;
        }

        public boolean isDirty() {
            for (T datum : data) {
                if (datum != null && !datum.isSameAsDefault()) return true;
            }
            return false;
        }

        public void save() {
            try {
                save0();
            } catch (IOException e) {
                GT_Log.err.println("Error saving data " + backingStorage.getPath());
                e.printStackTrace(GT_Log.err);
            }
        }

        private void save0() throws IOException {
            // noinspection ResultOfMethodCallIgnored
            backingStorage.getParentFile()
                .mkdirs();
            File tmpFile = getTmpFile();
            World world = Objects.requireNonNull(this.world.get(), "Attempting to save region of another world!");
            try (DataOutputStream output = new DataOutputStream(new FileOutputStream(tmpFile))) {
                int ptr = 0;
                boolean nullRange = data[0] == null;
                // write a magic byte as storage format version
                output.writeByte(0);
                // write a magic byte as data format version
                output.writeByte(version);
                output.writeBoolean(nullRange);
                while (ptr < data.length) {
                    // work out how long is this range
                    int rangeStart = ptr;
                    while (ptr < data.length
                        && (data[ptr] == null || (!saveDefaults && data[ptr].isSameAsDefault())) == nullRange) ptr++;
                    // write range length
                    output.writeShort(ptr - rangeStart);
                    if (!nullRange)
                        // write element data
                        for (int i = rangeStart; i < ptr; i++)
                            writeElement(output, data[i], world, getChunkX(ptr), getChunkZ(ptr));
                    // or not
                    nullRange = !nullRange;
                }
            }
            // first try to replace the destination file
            // since atomic operation, no need to keep the backup in place
            try {
                Files.move(
                    tmpFile.toPath(),
                    backingStorage.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                // in case some dumb system/jre combination would cause this
                // or if **somehow** two file inside the same directory belongs two separate filesystem
                FileUtils.copyFile(tmpFile, backingStorage);
            }
        }

        public void load() {
            try {
                loadFromFile(backingStorage);
            } catch (IOException | RuntimeException e) {
                GT_Log.err.println("Primary storage file broken in " + backingStorage.getPath());
                e.printStackTrace(GT_Log.err);
                // in case the primary storage is broken
                try {
                    loadFromFile(getTmpFile());
                } catch (IOException | RuntimeException e2) {
                    GT_Log.err.println("Backup storage file broken in " + backingStorage.getPath());
                    e2.printStackTrace(GT_Log.err);
                }
            }
        }

        private void loadFromFile(File file) throws IOException {
            World world = Objects.requireNonNull(this.world.get(), "Attempting to load region of another world!");
            try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
                byte b = input.readByte();
                if (b == 0) {
                    loadV0(input, world);
                } else {
                    GT_Log.err.printf("Unknown ChunkAssociatedData version %d\n", b);
                }
            }
        }

        private void loadV0(DataInput input, World world) throws IOException {
            int version = input.readByte();
            boolean nullRange = input.readBoolean();
            int ptr = 0;
            while (ptr != data.length) {
                int rangeEnd = ptr + input.readUnsignedShort();
                if (!nullRange) {
                    for (; ptr < rangeEnd; ptr++) {
                        data[ptr] = readElement(input, version, world, getChunkX(ptr), getChunkZ(ptr));
                    }
                } else {
                    Arrays.fill(data, ptr, rangeEnd, null);
                    ptr = rangeEnd;
                }
                nullRange = !nullRange;
            }
        }

        private File getTmpFile() {
            return new File(backingStorage.getParentFile(), backingStorage.getName() + ".tmp");
        }
    }

    public static class EventHandler {

        private EventHandler() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onWorldSave(WorldEvent.Save e) {
            for (GT_ChunkAssociatedData<?> d : instances.values()) {
                d.save(e.world);
            }
        }

        @SubscribeEvent
        public void onWorldUnload(WorldEvent.Unload e) {
            for (GT_ChunkAssociatedData<?> d : instances.values()) {
                // there is no need to explicitly do a save here
                // forge will send a WorldEvent.Save on server thread before this event is distributed
                d.masterMap.remove(e.world.provider.dimensionId);
            }
        }
    }
}
