package gtPlusPlus.australia.biome.type;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.australia.biome.CustomDecorator;
import gtPlusPlus.core.lib.CORE;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_AustralianOutback extends BiomeGenMesa
{
    private byte[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private boolean field_150626_aH;
    private boolean field_150620_aI;

    public Biome_AustralianOutback(int p_i45380_1_){
        super(p_i45380_1_, false, false);
        this.setColor(14238997);
        this.setBiomeName("Australian Outback");
        this.field_150626_aH = false;
        this.field_150620_aI = false;
        this.theBiomeDecorator.generateLakes = false;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0F, 0.0F);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand;
        this.field_150604_aj = 1;
        this.fillerBlock = Blocks.stained_hardened_clay;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 8;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        this.theBiomeDecorator.treesPerChunk = 5;        
    }

	public static Biome_AustralianOutback biome = new Biome_AustralianOutback(CORE.AUSTRALIA_BIOME_OUTBACK_ID);
	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DRY);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.SPARSE);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.HOT);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DESERT);
		BiomeManager.addSpawnBiome(biome);
	}

    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
    {
        return this.worldGeneratorTrees;
    }

    public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_)
    {
        super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
    }

    public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        if (this.field_150621_aC == null || this.field_150622_aD != p_150573_1_.getSeed())
        {
            this.func_150619_a(p_150573_1_.getSeed());
        }

        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != p_150573_1_.getSeed())
        {
            Random random1 = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(random1, 3);
            this.field_150624_aF = new NoiseGeneratorPerlin(random1, 3);
        }

        this.field_150622_aD = p_150573_1_.getSeed();
        double d5 = 0.0D;
        int k;
        int l;

        if (this.field_150626_aH)
        {
            k = (p_150573_5_ & -16) + (p_150573_6_ & 15);
            l = (p_150573_6_ & -16) + (p_150573_5_ & 15);
            double d1 = Math.min(Math.abs(p_150573_7_), this.field_150623_aE.func_151601_a((double)k * 0.25D, (double)l * 0.25D));

            if (d1 > 0.0D)
            {
                double d2 = 0.001953125D;
                double d3 = Math.abs(this.field_150624_aF.func_151601_a((double)k * d2, (double)l * d2));
                d5 = d1 * d1 * 2.5D;
                double d4 = Math.ceil(d3 * 50.0D) + 32.0D;

                if (d5 > d4)
                {
                    d5 = d4;
                }

                d5 += 64.0D;
            }
        }

        k = p_150573_5_ & 15;
        l = p_150573_6_ & 15;
        boolean flag = true;
        Block block = Blocks.stained_hardened_clay;
        Block block2 = this.fillerBlock;
        int i1 = (int)(p_150573_7_ / 3.0D + 3.0D + p_150573_2_.nextDouble() * 0.25D);
        boolean flag1 = Math.cos(p_150573_7_ / 3.0D * Math.PI) > 0.0D;
        int j1 = -1;
        boolean flag2 = false;
        int k1 = p_150573_3_.length / 256;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (l * 16 + k) * k1 + l1;

            if ((p_150573_3_[i2] == null || p_150573_3_[i2].getMaterial() == Material.air) && l1 < (int)d5)
            {
                p_150573_3_[i2] = Blocks.stone;
            }

            if (l1 <= 0 + p_150573_2_.nextInt(5))
            {
                p_150573_3_[i2] = Blocks.bedrock;
            }
            else
            {
                Block block1 = p_150573_3_[i2];

                if (block1 != null && block1.getMaterial() != Material.air)
                {
                    if (block1 == Blocks.stone)
                    {
                        byte b0;

                        if (j1 == -1)
                        {
                            flag2 = false;

                            if (i1 <= 0)
                            {
                                block = null;
                                block2 = Blocks.stone;
                            }
                            else if (l1 >= 59 && l1 <= 64)
                            {
                                block = Blocks.stained_hardened_clay;
                                block2 = this.fillerBlock;
                            }

                            if (l1 < 63 && (block == null || block.getMaterial() == Material.air))
                            {
                                block = Blocks.sandstone;
                            }

                            j1 = i1 + Math.max(0, l1 - 63);

                            if (l1 >= 62)
                            {
                                if (this.field_150620_aI && l1 > 86 + i1 * 2)
                                {
                                    if (flag1)
                                    {
                                        p_150573_3_[i2] = Blocks.dirt;
                                        p_150573_4_[i2] = 1;
                                    }
                                    else
                                    {
                                        p_150573_3_[i2] = Blocks.grass;
                                    }
                                }
                                else if (l1 > 66 + i1)
                                {
                                    b0 = 16;

                                    if (l1 >= 64 && l1 <= 127)
                                    {
                                        if (!flag1)
                                        {
                                            b0 = this.func_150618_d(p_150573_5_, l1, p_150573_6_);
                                        }
                                    }
                                    else
                                    {
                                        b0 = 1;
                                    }

                                    if (b0 < 16)
                                    {
                                        p_150573_3_[i2] = Blocks.stained_hardened_clay;
                                        p_150573_4_[i2] = (byte)b0;
                                    }
                                    else
                                    {
                                        p_150573_3_[i2] = Blocks.hardened_clay;
                                    }
                                }
                                else
                                {
                                    p_150573_3_[i2] = this.topBlock;
                                    p_150573_4_[i2] = (byte)this.field_150604_aj;
                                    flag2 = true;
                                }
                            }
                            else
                            {
                                p_150573_3_[i2] = block2;

                                if (block2 == Blocks.stained_hardened_clay)
                                {
                                    p_150573_4_[i2] = 1;
                                }
                            }
                        }
                        else if (j1 > 0)
                        {
                            --j1;

                            if (flag2)
                            {
                                p_150573_3_[i2] = Blocks.stained_hardened_clay;
                                p_150573_4_[i2] = 1;
                            }
                            else
                            {
                                b0 = this.func_150618_d(p_150573_5_, l1, p_150573_6_);

                                if (b0 < 16)
                                {
                                    p_150573_3_[i2] = Blocks.stained_hardened_clay;
                                    p_150573_4_[i2] = b0;
                                }
                                else
                                {
                                    p_150573_3_[i2] = Blocks.hardened_clay;
                                }
                            }
                        }
                    }
                }
                else
                {
                    j1 = -1;
                }
            }
        }
        super.genTerrainBlocks(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }

    public void func_150619_a(long p_150619_1_)
    {
        this.field_150621_aC = new byte[128];
        Arrays.fill(this.field_150621_aC, (byte)16);
        Random random = new Random(p_150619_1_);
        this.field_150625_aG = new NoiseGeneratorPerlin(random, 3);
        int j;

        for (j = 0; j < 128; ++j)
        {
            j += random.nextInt(8) + 1;

            if (j < 128)
            {
                this.field_150621_aC[j] = 1;
            }
        }

        j = random.nextInt(7) + 2;
        int k;
        int l;
        int i1;
        int j1;

        for (k = 0; k < j; ++k)
        {
            l = random.nextInt(7) + 1;
            i1 = random.nextInt(128);

            for (j1 = 0; i1 + j1 < 128 && j1 < l; ++j1)
            {
                this.field_150621_aC[i1 + j1] = 4;
            }
        }

        k = random.nextInt(6) + 2;
        int k1;

        for (l = 0; l < k; ++l)
        {
            i1 = random.nextInt(7) + 2;
            j1 = random.nextInt(128);

            for (k1 = 0; j1 + k1 < 128 && k1 < i1; ++k1)
            {
                this.field_150621_aC[j1 + k1] = 12;
            }
        }

        l = random.nextInt(7) + 2;

        for (i1 = 0; i1 < l; ++i1)
        {
            j1 = random.nextInt(5) + 1;
            k1 = random.nextInt(128);

            for (int l1 = 0; k1 + l1 < 128 && l1 < j1; ++l1)
            {
                this.field_150621_aC[k1 + l1] = 14;
            }
        }

        i1 = random.nextInt(8) + 3;
        j1 = 0;

        for (k1 = 0; k1 < i1; ++k1)
        {
            byte b0 = 1;
            j1 += random.nextInt(8) + 4;

            for (int i2 = 0; j1 + i2 < 128 && i2 < b0; ++i2)
            {
                this.field_150621_aC[j1 + i2] = 0;

                if (j1 + i2 > 1 && random.nextBoolean())
                {
                    this.field_150621_aC[j1 + i2 - 1] = 8;
                }

                if (j1 + i2 < 63 && random.nextBoolean())
                {
                    this.field_150621_aC[j1 + i2 + 1] = 8;
                }
            }
        }
    }

    /**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_)
    {
        return 10387789;
    }

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_)
    {
        return 9470285;
    }

    public byte func_150618_d(int p_150618_1_, int p_150618_2_, int p_150618_3_)
    {
        int l = (int)Math.round(this.field_150625_aG.func_151601_a((double)p_150618_1_ * 1.0D / 512.0D, (double)p_150618_1_ * 1.0D / 512.0D) * 2.0D);
        return this.field_150621_aC[(p_150618_2_ + l + 64) % 64];
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