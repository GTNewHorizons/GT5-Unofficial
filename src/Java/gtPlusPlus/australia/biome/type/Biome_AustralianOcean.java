package gtPlusPlus.australia.biome.type;

import java.util.Random;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_AustralianOcean extends BiomeGenOcean {

    public Biome_AustralianOcean(int p_i1985_1_)
    {
        super(p_i1985_1_);
        this.setColor(48);
        this.setBiomeName("Australian Ocean");
        this.setHeight(height_DeepOceans);
        this.spawnableCreatureList.clear();
    }

	public static Biome_AustralianOcean biome = new Biome_AustralianOcean(CORE.AUSTRALIA_BIOME_OCEAN_ID);
	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.COLD);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.OCEAN);
		BiomeManager.addSpawnBiome(biome);
	}

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }

    public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        super.genTerrainBlocks(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }
}