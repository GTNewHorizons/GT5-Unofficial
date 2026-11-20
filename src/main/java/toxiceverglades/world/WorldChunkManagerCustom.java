
package toxiceverglades.world;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;

import toxiceverglades.biome.BiomeEverglades;

public class WorldChunkManagerCustom extends WorldChunkManagerHell {

    public WorldChunkManagerCustom(long seed, WorldType worldType) {
        super(BiomeEverglades.biome, ((BiomeGenBase) BiomeEverglades.biome).getIntRainfall() / 65536.0F);
    }
}
