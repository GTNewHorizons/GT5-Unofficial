package gregtech.common.worldgen;

import net.minecraft.util.ChunkCoordinates;

import com.brandon3055.draconicevolution.api.GeneratorOverride;

/**
 * Checks whether a chunk falls within range of a Draconic Evolution chaos island, so that GalaxySpace asteroid aren't
 * generated on top of them.
 */
public final class ChaosIslandLocator {

    private ChaosIslandLocator() {}

    /**
     * @param chunkX chunk X coordinate
     * @param chunkZ chunk Z coordinate
     * @param radius exclusion radius in blocks, measured from the chunk center to the island center. A value of 0 or
     *               less disables the check.
     * @return whether the chunk center is within {@code radius} blocks of a chaos island
     */
    public static boolean isWithinRange(int chunkX, int chunkZ, int radius) {
        if (radius <= 0) return false;

        ChunkCoordinates center = GeneratorOverride.getNearestChaosIslandCenter(chunkX, chunkZ);

        if (center == null) return false;

        long blockX = (long) chunkX * 16 + 8;
        long blockZ = (long) chunkZ * 16 + 8;

        long dx = blockX - center.posX;
        long dz = blockZ - center.posZ;

        return dx * dx + dz * dz <= (long) radius * radius;
    }
}
