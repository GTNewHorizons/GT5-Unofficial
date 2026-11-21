package toxiceverglades.biome;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;

public final class GenLayerEverglades {

    private GenLayerEverglades() {}

    public static GenLayer[] makeTheWorld(long seed, WorldType type) {
        GenLayer biomes = new GenLayerBiomes(1L);
        biomes.initWorldGenSeed(seed);

        return new GenLayer[] { biomes, biomes };
    }
}
