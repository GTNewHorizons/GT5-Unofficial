package gregtech.common.worldgen;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.world.GTWorldgen;
import gregtech.common.blocks.rubbertree.RubberTreeWorldGenerator;

public class WorldgenRubberTrees extends GTWorldgen {

    /**
     * 6273391754566516237
     * 1 chance on 10 per eligible chunk
     */
    private static final int CHANCE_PER_ELIGIBLE_CHUNK = 10;
    private static final int MIN_TRUNK_OFFSET = 2; // 2 = 1 bloc between logs
    private static final int MAX_TRUNK_OFFSET = 5; // 5 = 4 blocs between logs

    public WorldgenRubberTrees() {
        super("rubber_trees", GregTechAPI.sWorldgenList, true);
    }

    @Override
    public boolean executeWorldgen(World world, Random random, String biomeName, int chunkX, int chunkZ,
        IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (world == null || world.provider == null || world.provider.dimensionId != 0) {
            return false;
        }

        // Only one attempt of tree group per chunk
        if (nextIntForChunk(world, chunkX, chunkZ) != 0) {
            return false;
        }

        Random groveRng = new Random(makeChunkSeed(world, chunkX, chunkZ, 10));

        int clusterSize = chooseClusterSize(groveRng);

        // We keep the center of the grove roughly in the middle of the chunk
        int baseX = chunkX + 6 + groveRng.nextInt(3); // 6..8
        int baseZ = chunkZ + 6 + groveRng.nextInt(3); // 6..8

        List<int[]> offsets = buildClusterOffsets(clusterSize, groveRng);

        boolean generatedAny = false;

        for (int[] offset : offsets) {
            int x = baseX + offset[0];
            int z = baseZ + offset[1];
            int y = findGroundY(world, x, z);

            if (y < 1 || y >= 240) {
                continue;
            }

            BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
            if (!isValidRubberTreeBiome(biome)) {
                continue;
            }

            if (!canPlantSaplingHere(world, x, y, z)) {
                continue;
            }

            if (new RubberTreeWorldGenerator(true).generate(world, groveRng, x, y, z)) {
                generatedAny = true;
            }
        }

        return generatedAny;
    }

    /**
     * Only valable biome type = FOREST Temperate
     */
    private boolean isValidRubberTreeBiome(BiomeGenBase biome) {
        if (biome == null) {
            return false;
        }

        return BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SAVANNA)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.CONIFEROUS)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WET)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DRY)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.HOT)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.COLD)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SPOOKY)
            && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DENSE);
    }

    private boolean canPlantSaplingHere(World world, int x, int y, int z) {
        if (!(GregTechAPI.sBlockRubberSapling instanceof BlockSapling)) {
            return false;
        }

        final Block blockAt = world.getBlock(x, y, z);
        if (blockAt != null && !blockAt.isAir(world, x, y, z)
            && !blockAt.isLeaves(world, x, y, z)
            && !blockAt.canBeReplacedByLeaves(world, x, y, z)) {
            return false;
        }

        final Block soil = world.getBlock(x, y - 1, z);
        return soil != null && soil
            .canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) GregTechAPI.sBlockRubberSapling);
    }

    /**
     * Find the true ground by descending through the air, leaves, wood, blocks can be replaced by leaves
     * and replaceable materials (flowers, tall garces etc)
     */
    private int findGroundY(@NotNull World world, int x, int z) {
        int y = world.getHeightValue(x, z);

        while (y > 1) {
            final Block below = world.getBlock(x, y - 1, z);
            if (below == null || below.isAir(world, x, y, z)
                || below.isLeaves(world, x, y, z)
                || below.isWood(world, x, y - 1, z)
                || below.canBeReplacedByLeaves(world, x, y - 1, z)
                || below.getMaterial()
                    .isReplaceable()) {
                y--;
                continue;
            }
            return y;
        }

        return -1;
    }

    /**
     * For determinist tree generation based on world seed
     */
    private long makeChunkSeed(@NotNull World world, int chunkX, int chunkZ, int salt) {
        long seed = world.getSeed();
        seed ^= (long) chunkX * 341873128712L;
        seed ^= (long) chunkZ * 132897987541L;
        seed ^= (long) salt * 42317861L;
        return seed;
    }

    private int nextIntForChunk(World world, int chunkX, int chunkZ) {
        return new Random(makeChunkSeed(world, chunkX, chunkZ, 0)).nextInt(CHANCE_PER_ELIGIBLE_CHUNK);
    }

    /**
     * Theoretically
     * 45 % of chance spawn Two tree in chunk
     * 55 % of chance spawn Three tree in chunk
     * In reality, this has been revised downwards because a number of scenarios can prevent the appearance of trees.
     * ~35 % of chance spawn One tree in chunk
     * ~50 % of chance spawn Two tree in chunk
     * ~15 % of chance spawn Three tree in chunk
     */
    private int chooseClusterSize(@NotNull Random random) {
        int roll = random.nextInt(100);

        if (roll <= 45) {
            return 2;
        }

        return 3;
    }

    private @NotNull List<int[]> buildClusterOffsets(int clusterSize, Random random) {
        List<int[]> result = new ArrayList<>();
        Set<Long> used = new HashSet<>();

        // Anchor point of the grove
        result.add(new int[] { 0, 0 });
        used.add(packOffset(0, 0));

        while (result.size() < clusterSize) {
            boolean added = false;

            // We try several times to find a good position for the next tree
            for (int attempt = 0; attempt < 40 && !added; attempt++) {
                // We choose a tree that is already in place as an anchor point
                int[] anchor = result.get(random.nextInt(result.size()));

                int[] delta = randomOffset(random);

                int newX = anchor[0] + delta[0];
                int newZ = anchor[1] + delta[1];

                long key = packOffset(newX, newZ);
                if (used.contains(key)) {
                    continue;
                }

                result.add(new int[] { newX, newZ });
                used.add(key);
                added = true;
            }

            // Safety: if we can't place a new tree, we stop generate group tree
            if (!added) {
                break;
            }
        }

        return result;
    }

    @Contract("_ -> new")
    private int @NotNull [] randomOffset(Random random) {
        int distanceX = chooseTreeSpacing(random);
        int distanceZ = chooseTreeSpacing(random);

        // Randomize signe offset
        if (distanceX != 0 && random.nextBoolean()) {
            distanceX = -distanceX;
        }
        if (distanceZ != 0 && random.nextBoolean()) {
            distanceZ = -distanceZ;
        }

        return new int[] { distanceX, distanceZ };
    }

    private int chooseTreeSpacing(@NotNull Random random) {
        return MIN_TRUNK_OFFSET + random.nextInt(MAX_TRUNK_OFFSET - MIN_TRUNK_OFFSET + 1);
    }

    /**
     * Create a predictable unique key form coords x and z
     */
    private long packOffset(int x, int z) {
        return (((long) x) << 32) ^ (z & 0xffffffffL);
    }
}
