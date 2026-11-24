package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.pack;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackY;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackZ;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkZ;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet2D;
import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet3D;

import gregtech.api.GregTechAPI;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

/// Finds the open cells that water from a volume would flow into, starting from a set of seed cells.
///
/// From the seeds, water spreads sideways, downwards and back up, but never above the ceiling. Each spread cell
/// remembers the seed it came from. If water would reach an open cell more than {@link #MAX_SPREAD} blocks sideways or
/// {@link #MAX_DEPTH} blocks below its seed, or an unloaded chunk, the connected region it belongs to leaks and is
/// discarded. The remaining (sealed) regions are returned.
///
/// Water doesn't flow into other dam water. Instead, the cells where the fill touches other dam water are recorded as
/// contacts, so that the volumes can be merged once the touching cells are filled.
///
/// This only reads the world.
public final class VolumeFill {

    public static final int MAX_SPREAD = 32;
    public static final int MAX_DEPTH = 8;

    private enum CellState {
        OPEN,
        WALL,
        UNLOADED,
        /// Dam water that is not part of the volume being filled.
        FOREIGN,
    }

    private static final ForgeDirection[] DIRECTIONS = ForgeDirection.VALID_DIRECTIONS;

    private final World world;
    private final WaterVolume volume;
    private final int ceilingY;

    /// Spread cell -> the seed it was reached from.
    private final Long2LongOpenHashMap origins = new Long2LongOpenHashMap();
    /// Spread cells that touch a leak.
    private final HashSet3D leaking = new HashSet3D();
    /// Spread cell -> the foreign dam water cells next to it.
    private final Long2ObjectOpenHashMap<LongArrayList> contacts = new Long2ObjectOpenHashMap<>();
    /// Foreign dam water cells directly above the volume's top layer, which touch the volume itself.
    private final LongArrayList seedContacts = new LongArrayList();

    /// Sealed cells below the ceiling.
    public final HashSet3D pockets = new HashSet3D();
    /// Sealed cells at the ceiling.
    public final HashSet3D surface = new HashSet3D();

    private boolean leaked;

    private VolumeFill(World world, WaterVolume volume, int ceilingY) {
        this.world = world;
        this.volume = volume;
        this.ceilingY = ceilingY;
    }

    /// Fills the layer above the volume's top layer, seeded from every open cell directly above it.
    ///
    /// Assumption: the volume is not empty.
    @NotNull
    public static VolumeFill growth(@NotNull World world, @NotNull WaterVolume volume) {
        VolumeFill fill = new VolumeFill(world, volume, volume.getTopY() + 1);

        if (fill.ceilingY < world.getHeight()) {
            fill.seedAboveTopLayer();
            fill.run();
        }

        return fill;
    }

    /// Fills from a single open cell next to the volume, such as a hole in its wall. Water never rises above the
    /// volume's top layer.
    ///
    /// Assumption: the volume is not empty.
    @NotNull
    public static VolumeFill fromCell(@NotNull World world, @NotNull WaterVolume volume, int x, int y, int z) {
        VolumeFill fill = new VolumeFill(world, volume, volume.getTopY());

        long seed = pack(x, y, z);

        switch (fill.getCellState(seed)) {
            case OPEN -> {
                fill.origins.put(seed, seed);
                fill.run();
            }
            case UNLOADED -> fill.leaked = true;
            default -> {}
        }

        return fill;
    }

    public boolean isEmpty() {
        return pockets.isEmpty() && surface.isEmpty();
    }

    /// Whether any region was discarded because it leaked.
    public boolean hasLeaked() {
        return leaked;
    }

    /// The foreign dam water cells that touch the given filled cells or the volume itself.
    ///
    /// Assumption: the given cells are a subset of this fill's sealed cells.
    @NotNull
    public LongOpenHashSet getContacts(@NotNull HashSet3D filled) {
        LongOpenHashSet found = new LongOpenHashSet(seedContacts);

        LongIterator iter = filled.iterator();

        while (iter.hasNext()) {
            LongArrayList cellContacts = contacts.get(iter.nextLong());

            if (cellContacts != null) found.addAll(cellContacts);
        }

        return found;
    }

    private void seedAboveTopLayer() {
        HashSet2D top = volume.getLayer(volume.getTopY());

        LongIterator iter = top.iterator();

        while (iter.hasNext()) {
            long xz = iter.nextLong();
            long seed = pack(unpackChunkX(xz), ceilingY, unpackChunkZ(xz));

            switch (getCellState(seed)) {
                case OPEN -> origins.put(seed, seed);
                case FOREIGN -> seedContacts.add(seed);
                default -> {}
            }
        }
    }

    /// Assumption: the seeds are in {@link #origins}.
    private void run() {
        spread();
        collectSealedRegions();
    }

    private void spread() {
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

        LongIterator seeds = origins.keySet()
            .iterator();

        while (seeds.hasNext()) {
            queue.enqueue(seeds.nextLong());
        }

        while (!queue.isEmpty()) {
            long cell = queue.dequeueLong();

            for (ForgeDirection dir : DIRECTIONS) {
                long neighbour = offset(cell, dir);

                if (unpackY(neighbour) > ceilingY) continue;
                if (origins.containsKey(neighbour)) continue;
                if (volume.contains(unpackX(neighbour), unpackY(neighbour), unpackZ(neighbour))) continue;

                CellState state = getCellState(neighbour);

                if (state == CellState.WALL) continue;

                if (state == CellState.FOREIGN) {
                    contacts.computeIfAbsent(cell, ignored -> new LongArrayList())
                        .add(neighbour);
                    continue;
                }

                long origin = origins.get(cell);

                if (state == CellState.UNLOADED || isOutOfBounds(origin, neighbour)) {
                    leaking.add(cell);
                    continue;
                }

                origins.put(neighbour, origin);
                queue.enqueue(neighbour);
            }
        }
    }

    /// Groups the spread cells into connected regions and keeps the regions that contain no leaking cells.
    private void collectSealedRegions() {
        HashSet3D visited = new HashSet3D();
        HashSet3D region = new HashSet3D();
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

        LongIterator iter = origins.keySet()
            .iterator();

        while (iter.hasNext()) {
            long start = iter.nextLong();

            if (!visited.add(start)) continue;

            region.clear();
            queue.enqueue(start);

            boolean sealed = true;

            while (!queue.isEmpty()) {
                long cell = queue.dequeueLong();

                region.add(cell);

                if (leaking.contains(cell)) sealed = false;

                for (ForgeDirection dir : DIRECTIONS) {
                    long neighbour = offset(cell, dir);

                    if (origins.containsKey(neighbour) && visited.add(neighbour)) {
                        queue.enqueue(neighbour);
                    }
                }
            }

            if (sealed) {
                addRegion(region);
            } else {
                leaked = true;
            }
        }
    }

    private void addRegion(HashSet3D region) {
        LongIterator iter = region.iterator();

        while (iter.hasNext()) {
            long cell = iter.nextLong();

            if (unpackY(cell) == ceilingY) {
                surface.add(cell);
            } else {
                pockets.add(cell);
            }
        }
    }

    private boolean isOutOfBounds(long origin, long cell) {
        int dx = Math.abs(unpackX(cell) - unpackX(origin));
        int dz = Math.abs(unpackZ(cell) - unpackZ(origin));
        int depth = unpackY(origin) - unpackY(cell);

        return dx > MAX_SPREAD || dz > MAX_SPREAD || depth > MAX_DEPTH;
    }

    private CellState getCellState(long cell) {
        int x = unpackX(cell);
        int y = unpackY(cell);
        int z = unpackZ(cell);

        if (y < 0 || y >= world.getHeight()) return CellState.WALL;
        if (!world.blockExists(x, y, z)) return CellState.UNLOADED;
        if (world.getBlock(x, y, z) == GregTechAPI.sBlockDamWater) return CellState.FOREIGN;

        return canHoldWater(world, x, y, z) ? CellState.OPEN : CellState.WALL;
    }

    /// Whether dam water may replace the block at the given position. Assumption: the position is loaded.
    public static boolean canHoldWater(@NotNull World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        if (block == GregTechAPI.sBlockDamWater) return false;
        if (block.isAir(world, x, y, z)) return true;

        Material material = block.getMaterial();

        if (material == Material.water) return true;
        if (material.isLiquid()) return false;

        return block.isReplaceable(world, x, y, z);
    }

    private static long offset(long cell, ForgeDirection dir) {
        return pack(unpackX(cell) + dir.offsetX, unpackY(cell) + dir.offsetY, unpackZ(cell) + dir.offsetZ);
    }
}
