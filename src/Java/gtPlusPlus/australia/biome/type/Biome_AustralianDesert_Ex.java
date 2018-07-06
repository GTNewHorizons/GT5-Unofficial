package gtPlusPlus.australia.biome.type;

import java.util.Random;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_AustralianDesert_Ex extends BiomeGenDesert {

    public Biome_AustralianDesert_Ex(int aID)
    {
        super(aID);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand;
        this.fillerBlock = Blocks.sand;
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;        
        this.setColor(16421912);
        this.setBiomeName("Australian Desert III");
        this.setDisableRain();
        this.setTemperatureRainfall(2.0F, 0.0F);
        this.setHeight(height_LowPlains);        
        this.spawnableCreatureList.clear();
    }

	public static Biome_AustralianDesert_Ex biome = new Biome_AustralianDesert_Ex(CORE.AUSTRALIA_BIOME_DESERT_3_ID);
	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DRY);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.HOT);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DESERT);
		BiomeManager.addSpawnBiome(biome);
	}

    public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_)
    {
        super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);

        if (p_76728_2_.nextInt(1000) == 0)
        {
            int k = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
            int l = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
            WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
            worldgendesertwells.generate(p_76728_1_, p_76728_2_, k, p_76728_1_.getHeightValue(k, l) + 1, l);
        }
    }
}