package toxiceverglades.biome;

import java.util.Arrays;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiomes extends GenLayer {

    public GenLayerBiomes(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth) {
        int[] biomes = IntCache.getIntCache(width * depth);
        Arrays.fill(biomes, 0, width * depth, BiomeEverglades.biome.biomeID);
        return biomes;
    }
}
