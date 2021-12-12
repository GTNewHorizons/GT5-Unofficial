package gtPlusPlus.australia.biome.type;

import java.util.Random;

import gtPlusPlus.australia.biome.CustomDecorator;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_AustralianPlains extends BiomeGenPlains
{

    public Biome_AustralianPlains(int p_i1986_1_)
    {
        super(p_i1986_1_);
        this.setTemperatureRainfall(0.8F, 0.4F);
        this.setColor(9286496);
        this.setBiomeName("Plainlands");
        this.setHeight(height_LowPlains);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 20;
        this.flowers.clear();
        this.addFlower(Blocks.red_flower,    4,  3);
        this.addFlower(Blocks.red_flower,    5,  3);
        this.addFlower(Blocks.red_flower,    6,  3);
        this.addFlower(Blocks.red_flower,    7,  3);
        this.addFlower(Blocks.red_flower,    0, 20);
        this.addFlower(Blocks.red_flower,    3, 20);
        this.addFlower(Blocks.red_flower,    8, 20);
        this.addFlower(Blocks.yellow_flower, 0, 30);
    }

	public static Biome_AustralianPlains biome = new Biome_AustralianPlains(CORE.AUSTRALIA_BIOME_PLAINS_ID);
	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DRY);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.SPARSE);
		BiomeManager.addSpawnBiome(biome);
	}

    public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_)
    {
        double d0 = plantNoise.func_151601_a((double)p_150572_2_ / 200.0D, (double)p_150572_4_ / 200.0D);
        int l;

        if (d0 < -0.8D)
        {
            l = p_150572_1_.nextInt(4);
            return BlockFlower.field_149859_a[4 + l];
        }
        else if (p_150572_1_.nextInt(3) > 0)
        {
            l = p_150572_1_.nextInt(3);
            return l == 0 ? BlockFlower.field_149859_a[0] : (l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8]);
        }
        else
        {
            return BlockFlower.field_149858_b[0];
        }
    }

    public void decorate(World aWorld, Random aRand, int aX, int aZ)
    {
        double d0 = plantNoise.func_151601_a((double)(aX + 8) / 200.0D, (double)(aZ + 8) / 200.0D);
        int k;
        int l;
        int i1;
        int j1;

        if (d0 < -0.8D)
        {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        else
        {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            genTallFlowers.func_150548_a(2);

            for (k = 0; k < 7; ++k)
            {
                l = aX + aRand.nextInt(16) + 8;
                i1 = aZ + aRand.nextInt(16) + 8;
                j1 = aRand.nextInt(aWorld.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(aWorld, aRand, l, j1, i1);
            }
        }

        if (this.field_150628_aC)
        {
            genTallFlowers.func_150548_a(0);

            for (k = 0; k < 10; ++k)
            {
                l = aX + aRand.nextInt(16) + 8;
                i1 = aZ + aRand.nextInt(16) + 8;
                j1 = aRand.nextInt(aWorld.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(aWorld, aRand, l, j1, i1);
            }
        }

        this.theBiomeDecorator.decorateChunk(aWorld, aRand, this, aX, aZ);
    }

	 /**
    * Allocate a new BiomeDecorator for this BiomeGenBase
    */
	@Override
   public BiomeDecorator createBiomeDecorator()
   {
       return getModdedBiomeDecorator(new CustomDecorator());
   }
}