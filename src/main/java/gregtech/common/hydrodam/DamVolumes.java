package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackY;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackZ;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.packChunk;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkZ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnegative;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet2D;
import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet3D;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D;

import gregtech.api.GregTechAPI;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

/// The dam water volumes in one dimension. This is the only class that places or removes dam water blocks.
///
/// Volumes are the source of truth. Dam water blocks are a view of them: a block's metadata says which volume it
/// belongs to, but a volume's cells and litres are only changed through this class.
public final class DamVolumes extends WorldSavedData {

    private static final String DATA_NAME = "gt_hydrodam_volumes";

    /// How long a volume that failed to grow waits before trying again, in ticks.
    private static final long EXPANSION_RETRY_DELAY = 100;

    public static final double LEAK_LITRES_PER_TICK = 50;

    private final Int2ObjectOpenHashMap<WaterVolume> volumes = new Int2ObjectOpenHashMap<>();

    /// Volume id -> the world time at which it may next try to grow. Not saved.
    private final Int2LongOpenHashMap expansionRetryTimes = new Int2LongOpenHashMap();

    /// Dam water blocks whose neighbours changed. Checked at the end of the tick. Not saved.
    private final HashSet3D pendingNeighbourChecks = new HashSet3D();

    /// Chunks that were loaded since the last tick, packed with {@link CoordinatePacker2D#packChunk}. Their dam water
    /// blocks are compared against the volumes at the end of the tick. Not saved.
    private final LongOpenHashSet pendingChunks = new LongOpenHashSet();

    /// Volumes whose connectivity must be checked at the end of the tick. Not saved.
    private final IntOpenHashSet pendingSplitChecks = new IntOpenHashSet();

    /// Absorbed volume id -> the id of the volume it was merged into, for merges during the current tick. Not saved.
    private final Int2IntOpenHashMap mergedInto = new Int2IntOpenHashMap();

    private final DamRain rain = new DamRain();
    private final DamLevelSync levelSync = new DamLevelSync();
    private final DamFlowTracker flowTracker = new DamFlowTracker();

    private World world;

    /// True while this class is changing dam water blocks, so that their removal callbacks are ignored.
    private boolean mutating;

    public DamVolumes(String name) {
        super(name);
    }

    /// Assumption: called on the server.
    @NotNull
    public static DamVolumes get(@NotNull World world) {
        DamVolumes volumes = (DamVolumes) world.perWorldStorage.loadData(DamVolumes.class, DATA_NAME);

        if (volumes == null) {
            volumes = new DamVolumes(DATA_NAME);
            world.perWorldStorage.setData(DATA_NAME, volumes);
        }

        volumes.world = world;

        return volumes;
    }

    @Nullable
    public WaterVolume getVolume(int id) {
        return volumes.get(id);
    }

    @NotNull
    public Collection<WaterVolume> getVolumes() {
        return Collections.unmodifiableCollection(volumes.values());
    }

    /// The volume that owns the dam water block at the given position, if any.
    @Nullable
    public WaterVolume getVolumeAt(int x, int y, int z) {
        if (!world.blockExists(x, y, z)) return null;
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockDamWater) return null;

        WaterVolume volume = volumes.get(DamWaterMeta.getVolumeId(world.getBlockMetadata(x, y, z)));

        return volume != null && volume.contains(x, y, z) ? volume : null;
    }

    /// The volume that water poured at the given position would fall into: the first dam water straight below it,
    /// through at most {@link VolumeFill#MAX_DEPTH} blocks that can hold water.
    ///
    /// Assumption: the position can hold water.
    @Nullable
    public WaterVolume getVolumeBelow(int x, int y, int z) {
        for (int depth = 1; depth <= VolumeFill.MAX_DEPTH; depth++) {
            int below = y - depth;

            if (below < 0 || !world.blockExists(x, below, z)) return null;

            WaterVolume volume = getVolumeAt(x, below, z);

            if (volume != null) return volume;
            if (!VolumeFill.canHoldWater(world, x, below, z)) return null;
        }

        return null;
    }

    /// Creates a volume holding the given water, starting from a single cell at the given position. The starting cell
    /// ignores the spreading limits. From there, water flows into its surroundings like it would through an opening:
    /// enclosed space at or below the starting cell is filled, open edges become leaks, and water that doesn't fit
    /// grows the volume upwards.
    ///
    /// Assumption: litres is positive.
    ///
    /// @return the new volume, or null if the position can't hold water or every volume id is in use
    @Nullable
    public WaterVolume createVolume(int x, int y, int z, double litres) {
        if (!world.blockExists(x, y, z)) return null;
        if (!VolumeFill.canHoldWater(world, x, y, z)) return null;

        int id = allocateId();

        if (id == DamWaterMeta.UNASSIGNED_ID) return null;

        WaterVolume volume = new WaterVolume(id);

        volumes.put(id, volume);
        placeCell(volume, x, y, z);
        volume.setLitres(litres);

        volume = checkNeighbours(volume, x, y, z);

        if (volumes.get(volume.getId()) == volume) addLitres(volume, 0);

        markDirty();

        return follow(volume);
    }

    /// The volume that the given volume was merged into during the current tick, or the volume itself. Null if it was
    /// deleted.
    @Nullable
    public WaterVolume follow(@NotNull WaterVolume volume) {
        int id = volume.getId();

        if (volumes.get(id) == volume) return volume;

        // Without a merge this tick, the volume was deleted; its id may since belong to another volume.
        if (!mergedInto.containsKey(id)) return null;

        while (mergedInto.containsKey(id)) {
            id = mergedInto.get(id);
        }

        return volumes.get(id);
    }

    /// Deletes the volume, all of its dam water blocks and its leaks.
    public void deleteVolume(@NotNull WaterVolume volume) {
        while (!volume.isEmpty()) {
            int y = volume.getTopY();

            clearLayer(volume, y, volume.removeTopLayer());
        }

        for (long leak : volume.getLeaks()
            .toLongArray()) {
            removeLeak(volume, unpackX(leak), unpackY(leak), unpackZ(leak));
        }

        forget(volume);

        markDirty();
    }

    /// Removes the volume from the registry without touching its blocks.
    private void forget(WaterVolume volume) {
        volumes.remove(volume.getId());
        expansionRetryTimes.remove(volume.getId());
        pendingSplitChecks.remove(volume.getId());
    }

    /// Adds water to the volume, growing it as needed. Water that doesn't fit is voided. Growing can merge the volume
    /// into another one; see {@link #follow(WaterVolume)}.
    ///
    /// @return the litres that were stored
    public double addLitres(@NotNull WaterVolume volume, @Nonnegative double litres) {
        volume.setLitres(volume.getLitres() + litres);

        double voided = 0;

        while (volume.getLitres() > volume.getCapacity()) {
            WaterVolume grown = tryExpand(volume);

            if (grown == null) {
                voided = volume.getLitres() - volume.getCapacity();
                volume.setLitres(volume.getCapacity());
                break;
            }

            volume = grown;
        }

        settle(volume);
        markDirty();

        return litres - voided;
    }

    /// Removes water from the volume, shrinking it as needed. The volume is deleted once it holds no water.
    ///
    /// @return the litres that were removed
    public double removeLitres(@NotNull WaterVolume volume, @Nonnegative double litres) {
        double removed = Math.min(litres, volume.getLitres());

        volume.setLitres(volume.getLitres() - removed);

        settle(volume);
        markDirty();

        return removed;
    }

    /// Called when a dam water block is removed by something other than this class. The removed cell's water is
    /// voided.
    public void onCellRemoved(int x, int y, int z, int meta) {
        if (mutating) return;

        WaterVolume volume = volumes.get(DamWaterMeta.getVolumeId(meta));

        if (volume == null || !volume.contains(x, y, z)) return;

        volume.setLitres(volume.getLitres() - volume.getLitresInCell(y));
        volume.removeCell(x, y, z);
        pendingSplitChecks.add(volume.getId());

        settle(volume);
        markDirty();
    }

    /// Called when a neighbour of the dam water block at the given position changed. The neighbours are checked at
    /// the end of the tick.
    public void queueNeighbourCheck(int x, int y, int z) {
        pendingNeighbourChecks.add(x, y, z);
    }

    /// Called when a chunk is loaded. Its dam water blocks are checked against the volumes at the end of the tick.
    public void queueChunk(int chunkX, int chunkZ) {
        pendingChunks.add(packChunk(chunkX, chunkZ));
    }

    /// The volume's net change in litres per tick from every source, averaged over the last second. Positive when it
    /// is filling.
    public double getNetLitresPerTick(@NotNull WaterVolume volume) {
        return flowTracker.getNetLitresPerTick(volume);
    }

    /// The ticks until the volume may try to grow again after a failed attempt, or 0 if it may grow now.
    public long getTicksUntilExpansionRetry(@NotNull WaterVolume volume) {
        return Math.max(0, expansionRetryTimes.get(volume.getId()) - world.getTotalWorldTime());
    }

    /// Sends every volume's level to a player who just entered this dimension.
    public void sendLevelsTo(@NotNull EntityPlayerMP player) {
        levelSync.sendAll(world, this, player);
    }

    /// Assumption: called once per tick, after the world has ticked.
    public void tick() {
        if (volumes.isEmpty()) {
            pendingNeighbourChecks.clear();
            pendingChunks.clear();
            pendingSplitChecks.clear();
            mergedInto.clear();
            levelSync.tick(world, this);
            return;
        }

        reconcilePendingChunks();
        runPendingNeighbourChecks();
        runPendingSplitChecks();

        for (WaterVolume volume : new ArrayList<>(volumes.values())) {
            if (volumes.get(volume.getId()) == volume) updateLeaks(volume);
        }

        rain.tick(world, this);
        levelSync.tick(world, this);
        flowTracker.tick(world, this);

        mergedInto.clear();
    }

    /// Drops empty top layers until the water level sits in the top layer, and deletes the volume if it has no water.
    private void settle(WaterVolume volume) {
        if (volume.isEmpty() || volume.getLitres() <= 0) {
            deleteVolume(volume);
            return;
        }

        while (volume.getLayerCount() > 1 && volume.getLitres() <= volume.getCapacityBelowTop()) {
            int y = volume.getTopY();

            clearLayer(volume, y, volume.removeTopLayer());
            pendingSplitChecks.add(volume.getId());
        }
    }

    /// Grows the volume. Pockets below the new layer are filled first; the new layer is only added once there are none.
    /// A volume can't grow if any part of the new layer leaks, since its water would spill out first.
    ///
    /// If the volume touches other dam water while growing, it is merged with it.
    ///
    /// @return the grown volume (which may have been merged into another), or null if it couldn't grow
    @Nullable
    private WaterVolume tryExpand(WaterVolume volume) {
        long now = world.getTotalWorldTime();

        if (now < expansionRetryTimes.get(volume.getId())) return null;

        VolumeFill fill = VolumeFill.growth(world, volume);

        HashSet3D placed = fill.pockets.isEmpty() ? fill.surface : fill.pockets;
        LongOpenHashSet contacts = fill.getContacts(placed);

        if (fill.hasLeaked() || (placed.isEmpty() && contacts.isEmpty())) {
            expansionRetryTimes.put(volume.getId(), now + EXPANSION_RETRY_DELAY);
            return null;
        }

        placeCells(volume, placed);

        WaterVolume merged = mergeWithContacts(volume, contacts);

        // Contacts with orphaned blocks merge with nothing, so the volume may not have grown at all.
        if (placed.isEmpty() && merged == volume) {
            expansionRetryTimes.put(volume.getId(), now + EXPANSION_RETRY_DELAY);
            return null;
        }

        return merged;
    }

    private void runPendingNeighbourChecks() {
        long[] cells = pendingNeighbourChecks.toLongArray();

        pendingNeighbourChecks.clear();

        for (long cell : cells) {
            int x = unpackX(cell);
            int y = unpackY(cell);
            int z = unpackZ(cell);

            WaterVolume volume = getVolumeAt(x, y, z);

            if (volume != null) checkNeighbours(volume, x, y, z);
        }
    }

    /// Lets water flow into any new openings next to the given cell, and merges with any other dam water next to it.
    /// Openings that can't be filled become leaks, and leaks that were closed off are removed.
    ///
    /// @return the volume (which may have been merged into another)
    private WaterVolume checkNeighbours(WaterVolume volume, int x, int y, int z) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int nx = x + dir.offsetX;
            int ny = y + dir.offsetY;
            int nz = z + dir.offsetZ;

            if (volume.contains(nx, ny, nz)) continue;
            if (!world.blockExists(nx, ny, nz)) continue;

            if (world.getBlock(nx, ny, nz) == GregTechAPI.sBlockDamWater) {
                WaterVolume other = resolveVolumeAt(nx, ny, nz);

                if (other != null) volume = merge(volume, other);
            } else if (!VolumeFill.canHoldWater(world, nx, ny, nz)) {
                removeLeak(volume, nx, ny, nz);
                continue;
            } else if (ny > volume.getTopY()) {
                expansionRetryTimes.remove(volume.getId());
                continue;
            } else if (!volume.getLeaks()
                .contains(nx, ny, nz)) {
                    volume = fillFrom(volume, nx, ny, nz);
                }

            // Filling and merging can shrink or delete the volume, which may remove this cell.
            if (volumes.get(volume.getId()) != volume || !volume.contains(x, y, z)) return volume;
        }

        return volume;
    }

    /// Lets the volume's water flow into an opening. If the opening leads somewhere that can't be filled, it becomes a
    /// leak instead. The volume's litres don't change, so its level drops by the volume it gained.
    ///
    /// @return the volume (which may have been merged into another)
    private WaterVolume fillFrom(WaterVolume volume, int x, int y, int z) {
        VolumeFill fill = VolumeFill.fromCell(world, volume, x, y, z);

        markDirty();

        if (fill.hasLeaked()) {
            volume.addLeak(x, y, z);
            return volume;
        }

        LongOpenHashSet contacts = fill.getContacts(fill.pockets);
        contacts.addAll(fill.getContacts(fill.surface));

        placeCells(volume, fill.pockets);
        placeCells(volume, fill.surface);
        settle(volume);

        return mergeWithContacts(volume, contacts);
    }

    /// Merges the volume with the volumes that own the given dam water cells.
    ///
    /// @return the volume (which may have been merged into another)
    private WaterVolume mergeWithContacts(WaterVolume volume, LongOpenHashSet contacts) {
        LongIterator iter = contacts.iterator();

        while (iter.hasNext()) {
            long cell = iter.nextLong();

            WaterVolume other = resolveVolumeAt(unpackX(cell), unpackY(cell), unpackZ(cell));

            if (other != null) volume = merge(volume, other);
        }

        return volume;
    }

    /// Merges two volumes into the one with the lower id. Their litres, cells and leaks are combined, and the water
    /// settles to a common level.
    ///
    /// @return the merged volume
    private WaterVolume merge(WaterVolume a, WaterVolume b) {
        if (a == b) return a;

        WaterVolume kept = a.getId() < b.getId() ? a : b;
        WaterVolume absorbed = kept == a ? b : a;

        absorbed.forEachCell((x, y, z) -> {
            kept.addCell(x, y, z);
            setVolumeMeta(x, y, z, kept.getId());
        });

        LongIterator leaks = absorbed.getLeaks()
            .iterator();

        while (leaks.hasNext()) {
            long leak = leaks.nextLong();

            kept.addLeak(unpackX(leak), unpackY(leak), unpackZ(leak));
        }

        kept.setLitres(kept.getLitres() + absorbed.getLitres());

        forget(absorbed);
        mergedInto.put(absorbed.getId(), kept.getId());
        expansionRetryTimes.remove(kept.getId());

        settle(kept);
        markDirty();

        return kept;
    }

    private void runPendingSplitChecks() {
        int[] ids = pendingSplitChecks.toIntArray();

        pendingSplitChecks.clear();

        for (int id : ids) {
            WaterVolume volume = volumes.get(id);

            if (volume == null) continue;

            List<HashSet3D> components = VolumeComponents.find(volume);

            if (components.size() > 1) split(volume, components);
        }
    }

    /// Splits off every component but the first (the largest) into a new volume. Each piece keeps the water that was in
    /// its cells, and the leaks next to it.
    private void split(WaterVolume volume, List<HashSet3D> components) {
        int topY = volume.getTopY();
        double topLayerFill = volume.getTopLayerFill();

        List<WaterVolume> pieces = new ArrayList<>();

        for (HashSet3D component : components.subList(1, components.size())) {
            int id = allocateId();

            if (id == DamWaterMeta.UNASSIGNED_ID) break;

            WaterVolume piece = new WaterVolume(id);
            double litres = 0;

            LongIterator iter = component.iterator();

            while (iter.hasNext()) {
                long cell = iter.nextLong();

                int x = unpackX(cell);
                int y = unpackY(cell);
                int z = unpackZ(cell);

                if (y < topY) {
                    litres += WaterVolume.LITRES_PER_CELL;
                } else if (y == topY) {
                    litres += topLayerFill * WaterVolume.LITRES_PER_CELL;
                }

                volume.removeCell(x, y, z);
                piece.addCell(x, y, z);
                setVolumeMeta(x, y, z, id);
            }

            piece.setLitres(litres);
            volume.setLitres(volume.getLitres() - litres);

            volumes.put(id, piece);
            pieces.add(piece);
        }

        moveLeaksToPieces(volume, pieces);

        settle(volume);

        for (WaterVolume piece : pieces) {
            settle(piece);
        }

        markDirty();
    }

    /// Gives each of the volume's leaks that no longer touch it to the piece it touches.
    private static void moveLeaksToPieces(WaterVolume volume, List<WaterVolume> pieces) {
        for (long leak : volume.getLeaks()
            .toLongArray()) {
            int x = unpackX(leak);
            int y = unpackY(leak);
            int z = unpackZ(leak);

            if (isNextToVolume(volume, x, y, z)) continue;

            for (WaterVolume piece : pieces) {
                if (isNextToVolume(piece, x, y, z)) {
                    volume.removeLeak(x, y, z);
                    piece.addLeak(x, y, z);
                    break;
                }
            }
        }
    }

    /// The volume that owns the dam water block at the given position. Unassigned blocks are adopted into a new
    /// volume, and blocks whose metadata is out of date are fixed.
    ///
    /// Assumption: the position is loaded and holds dam water.
    ///
    /// @return the owning volume, or null if no volume owns the block and it can't be adopted
    @Nullable
    private WaterVolume resolveVolumeAt(int x, int y, int z) {
        int id = DamWaterMeta.getVolumeId(world.getBlockMetadata(x, y, z));

        if (id == DamWaterMeta.UNASSIGNED_ID) return adoptUnassigned(x, y, z);

        WaterVolume volume = volumes.get(id);

        if (volume != null && volume.contains(x, y, z)) return volume;

        WaterVolume owner = findOwner(x, y, z);

        if (owner != null) setVolumeMeta(x, y, z, owner.getId());

        return owner;
    }

    /// Searches every volume for the one containing the given cell. Used when a block's metadata is out of date,
    /// because its volume was merged or split while its chunk was unloaded.
    @Nullable
    private WaterVolume findOwner(int x, int y, int z) {
        for (WaterVolume volume : volumes.values()) {
            if (volume.contains(x, y, z)) return volume;
        }

        return null;
    }

    /// Gives an unassigned dam water block its own full, single-cell volume. Its neighbours are checked at the end of
    /// the tick, so it spreads and merges like any other volume.
    ///
    /// Assumption: the position is loaded and holds unassigned dam water.
    ///
    /// @return the new volume, or null if every volume id is in use
    @Nullable
    private WaterVolume adoptUnassigned(int x, int y, int z) {
        int id = allocateId();

        if (id == DamWaterMeta.UNASSIGNED_ID) return null;

        WaterVolume volume = new WaterVolume(id);

        volume.addCell(x, y, z);
        volume.setLitres(WaterVolume.LITRES_PER_CELL);
        volumes.put(id, volume);

        setVolumeMeta(x, y, z, id);
        queueNeighbourCheck(x, y, z);

        markDirty();

        return volume;
    }

    /// Points the dam water block at the given position at a volume, if it is loaded.
    private void setVolumeMeta(int x, int y, int z, int id) {
        if (!world.blockExists(x, y, z)) return;
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockDamWater) return;

        world.setBlockMetadataWithNotify(x, y, z, DamWaterMeta.of(id), 2);
    }

    /// Drains water through the volume's active leaks, and shows each active leak as a vanilla water source.
    private void updateLeaks(WaterVolume volume) {
        if (volume.getLeaks()
            .isEmpty()) return;

        int activeLeaks = 0;

        for (long leak : volume.getLeaks()
            .toLongArray()) {
            int x = unpackX(leak);
            int y = unpackY(leak);
            int z = unpackZ(leak);

            boolean active = volume.isLeakActive(y);

            if (active) activeLeaks++;

            if (!world.blockExists(x, y, z)) continue;

            if (!VolumeFill.canHoldWater(world, x, y, z) || !isNextToVolume(volume, x, y, z)) {
                removeLeak(volume, x, y, z);
                continue;
            }

            if (active) {
                showLeak(x, y, z);
            } else {
                hideLeak(x, y, z);
            }
        }

        if (activeLeaks > 0) removeLitres(volume, activeLeaks * LEAK_LITRES_PER_TICK);
    }

    private void removeLeak(WaterVolume volume, int x, int y, int z) {
        if (!volume.removeLeak(x, y, z)) return;

        if (world.blockExists(x, y, z)) hideLeak(x, y, z);

        markDirty();
    }

    /// Assumption: the position is loaded and can hold water.
    private void showLeak(int x, int y, int z) {
        if (isWaterSource(x, y, z)) return;

        world.setBlock(x, y, z, Blocks.flowing_water, 0, 3);
    }

    /// Assumption: the position is loaded.
    private void hideLeak(int x, int y, int z) {
        if (isWaterSource(x, y, z)) world.setBlock(x, y, z, Blocks.air, 0, 3);
    }

    private boolean isWaterSource(int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        return (block == Blocks.water || block == Blocks.flowing_water) && world.getBlockMetadata(x, y, z) == 0;
    }

    private static boolean isNextToVolume(WaterVolume volume, int x, int y, int z) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (volume.contains(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) return true;
        }

        return false;
    }

    private void reconcilePendingChunks() {
        long[] chunks = pendingChunks.toLongArray();

        pendingChunks.clear();

        for (long packed : chunks) {
            int chunkX = unpackChunkX(packed);
            int chunkZ = unpackChunkZ(packed);

            if (!world.getChunkProvider()
                .chunkExists(chunkX, chunkZ)) continue;

            reconcileChunk(world.getChunkFromChunkCoords(chunkX, chunkZ));
        }
    }

    /// Brings the chunk's dam water blocks in line with the volumes. Blocks left behind when a volume shrank or was
    /// deleted while the chunk was unloaded are removed, blocks whose volume was merged or split are pointed at their
    /// new volume, and unassigned blocks are adopted.
    private void reconcileChunk(Chunk chunk) {
        Block damWater = GregTechAPI.sBlockDamWater;

        mutating = true;

        try {
            for (ExtendedBlockStorage section : chunk.getBlockStorageArray()) {
                if (section == null || section.isEmpty()) continue;

                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            if (section.getBlockByExtId(x, y, z) != damWater) continue;

                            int worldX = (chunk.xPosition << 4) + x;
                            int worldY = section.getYLocation() + y;
                            int worldZ = (chunk.zPosition << 4) + z;

                            reconcileBlock(section.getExtBlockMetadata(x, y, z), worldX, worldY, worldZ);
                        }
                    }
                }
            }
        } finally {
            mutating = false;
        }
    }

    /// Assumption: {@link #mutating} is set.
    private void reconcileBlock(int meta, int x, int y, int z) {
        WaterVolume volume = volumes.get(DamWaterMeta.getVolumeId(meta));

        if (volume != null && volume.contains(x, y, z)) return;

        if (resolveVolumeAt(x, y, z) == null) world.setBlock(x, y, z, Blocks.air, 0, 2);
    }

    private void placeCells(WaterVolume volume, HashSet3D cells) {
        LongIterator iter = cells.iterator();

        while (iter.hasNext()) {
            long cell = iter.nextLong();

            placeCell(volume, unpackX(cell), unpackY(cell), unpackZ(cell));
        }
    }

    /// Adds a cell to the volume and places its block. A leak at that position is filled in.
    private void placeCell(WaterVolume volume, int x, int y, int z) {
        volume.addCell(x, y, z);
        volume.removeLeak(x, y, z);

        mutating = true;

        try {
            world.setBlock(x, y, z, GregTechAPI.sBlockDamWater, DamWaterMeta.of(volume.getId()), 2);
        } finally {
            mutating = false;
        }
    }

    /// Removes the dam water blocks of a layer that was already removed from the volume. Blocks in unloaded chunks are
    /// left alone.
    private void clearLayer(WaterVolume volume, int y, HashSet2D layer) {
        LongIterator iter = layer.iterator();

        mutating = true;

        try {
            while (iter.hasNext()) {
                long xz = iter.nextLong();

                int x = unpackChunkX(xz);
                int z = unpackChunkZ(xz);

                if (!world.blockExists(x, y, z)) continue;
                if (world.getBlock(x, y, z) != GregTechAPI.sBlockDamWater) continue;
                if (DamWaterMeta.getVolumeId(world.getBlockMetadata(x, y, z)) != volume.getId()) continue;

                world.setBlock(x, y, z, Blocks.air, 0, 2);
            }
        } finally {
            mutating = false;
        }
    }

    /// @return the lowest unused id, or {@link DamWaterMeta#UNASSIGNED_ID} if every id is in use
    private int allocateId() {
        for (int id = 1; id <= DamWaterMeta.MAX_ID; id++) {
            if (!volumes.containsKey(id)) return id;
        }

        return DamWaterMeta.UNASSIGNED_ID;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        volumes.clear();

        NBTTagList volumeList = tag.getTagList("volumes", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < volumeList.tagCount(); i++) {
            WaterVolume volume = WaterVolume.readFromNBT(volumeList.getCompoundTagAt(i));

            volumes.put(volume.getId(), volume);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList volumeList = new NBTTagList();

        for (WaterVolume volume : volumes.values()) {
            volumeList.appendTag(volume.writeToNBT());
        }

        tag.setTag("volumes", volumeList);
    }
}
