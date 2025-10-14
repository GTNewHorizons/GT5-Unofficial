package gregtech.common.worldgen;

import java.util.BitSet;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.util.GTUtility;
import gregtech.api.util.LRUCache;
import gregtech.mixin.interfaces.accessors.HEEChunkProviderAccessor;
import gregtech.mixin.interfaces.accessors.MapGenIslandAccessor;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;

/**
 * Checks for HEE islands within a radius.
 * GalaxySpace asteroids tend to generate on top of them, so we don't want to generate asteroid seeds within the range
 * of an island.
 */
public class HEEIslandScanner {

    private static final int REGION_WIDTH = 32;
    private static final int ISLAND_RADIUS = 7;

    private static final LRUCache<IntIntPair, RegionInfo> PER_REGION_CACHE = new LRUCache<>(
        64,
        HEEIslandScanner::checkForIslands);

    private static class RegionInfo {

        public final int chunkX, chunkZ;
        public final BitSet presence = new BitSet();

        public RegionInfo(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        public final boolean check(int minX, int maxX, int minZ, int maxZ) {
            if (presence.isEmpty()) return false;
            if (minX < chunkX && minZ < chunkZ && maxX >= chunkX + REGION_WIDTH && maxZ >= chunkZ + REGION_WIDTH)
                return false;

            minX = GTUtility.clamp(minX - chunkX, 0, REGION_WIDTH - 1);
            maxX = GTUtility.clamp(maxX - chunkX, 0, REGION_WIDTH - 1);
            minZ = GTUtility.clamp(minZ - chunkZ, 0, REGION_WIDTH - 1);
            maxZ = GTUtility.clamp(maxZ - chunkZ, 0, REGION_WIDTH - 1);

            for (int z = minZ; z <= maxZ; z++) {
                int start = getIndex(minX, z);
                int end = getIndex(maxX, z);

                // If there is an island in this row, this will return its index
                int next = presence.nextSetBit(start);

                // If next == -1, then there are no islands past 'start' and there's no point in checking further
                if (next == -1) return false;

                // If the next closest island in the bit set is <= the end, then it's contained within this row
                if (next <= end) return true;
            }

            return false;
        }

        private static int getIndex(int relX, int relZ) {
            return relZ * REGION_WIDTH + relX;
        }
    }

    private static RegionInfo checkForIslands(IntIntPair regionCoord) {
        RegionInfo info = new RegionInfo(regionCoord.leftInt() << 5, regionCoord.rightInt() << 5);

        WorldServer theEnd = DimensionManager.getWorld(1);

        if (theEnd == null) return info;
        if (!(theEnd.theChunkProviderServer.currentChunkProvider instanceof HEEChunkProviderAccessor heeProvider))
            return info;

        MapGenIslandAccessor islandGen = (MapGenIslandAccessor) heeProvider.gt5u$getIslandGen();

        // Scan each chunk in this region to see if it will generate an island
        for (int z = 0; z < 32; z++) {
            for (int x = 0; x < 32; x++) {
                // x/z are centred, but the seed isn't, so we offset the seed coords by the radius of the island.
                // This isn't perfect since islands can generate off-centre but it works well enough
                int seedX = x + info.chunkX - ISLAND_RADIUS;
                int seedZ = z + info.chunkZ - ISLAND_RADIUS;

                if (islandGen.gt5u$canSpawnStructureAtCoords(seedX, seedZ)) {
                    info.presence.set(RegionInfo.getIndex(x, z));
                }
            }
        }

        return info;
    }

    private static final IntIntMutablePair POOLED_COORD = IntIntMutablePair.of(0, 0);

    public static boolean isWithinRangeOfIsland(int chunkX, int chunkZ) {
        int chunkMinX = chunkX - ISLAND_RADIUS;
        int chunkMaxX = chunkX + ISLAND_RADIUS;
        int chunkMinZ = chunkZ - ISLAND_RADIUS;
        int chunkMaxZ = chunkZ + ISLAND_RADIUS;

        int regionMinX = chunkMinX >> 5;
        int regionMaxX = chunkMaxX >> 5;
        int regionMinZ = chunkMinZ >> 5;
        int regionMaxZ = chunkMaxZ >> 5;

        for (int z = regionMinZ; z <= regionMaxZ; z++) {
            for (int x = regionMinX; x <= regionMaxX; x++) {
                POOLED_COORD.left(x)
                    .right(z);

                RegionInfo region = PER_REGION_CACHE.get(POOLED_COORD);

                if (region.check(chunkMinX, chunkMaxX, chunkMinZ, chunkMaxZ)) return true;
            }
        }

        return false;
    }

    public static void clearCache() {
        PER_REGION_CACHE.clear();
    }
}
