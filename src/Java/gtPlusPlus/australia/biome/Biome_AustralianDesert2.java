package gtPlusPlus.australia.biome;

import java.lang.reflect.Field;
import java.util.Random;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_AustralianDesert2 {

	public static BiomeGenAustralianDesert2 biome = new BiomeGenAustralianDesert2();

	public Object instance;

	public static Block blockTopLayer;
	public static Block blockSecondLayer;
	public static Block blockMainFiller = Blocks.stone;
	public static Block blockSecondaryFiller;
	public static Block blockFluidLakes;

	public Biome_AustralianDesert2() {
	}

	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DESERT);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DRY);
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.HOT);
		BiomeManager.addSpawnBiome(biome);
	}

	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
	}

	public void registerRenderers() {
	}

	public int addFuel(ItemStack fuel) {
		return 0;
	}

	public void serverLoad(FMLServerStartingEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {
	}

	static class BiomeGenAustralianDesert2 extends BiomeGenBase {
		@SuppressWarnings("unchecked")
		public BiomeGenAustralianDesert2() {
			super(CORE.AUSTRALIA_BIOME_DESERT_2_ID);
			this.setBiomeName("Australian Desert II");
			this.setBiomeID();
			this.enableRain = true;
			this.enableSnow = false;
			this.topBlock = blockTopLayer;
			this.fillerBlock = blockSecondLayer;
			//this.theBiomeDecorator = new BiomeGenerator_Custom();
			this.theBiomeDecorator.generateLakes = true;
			this.theBiomeDecorator.treesPerChunk = 40;
			this.theBiomeDecorator.flowersPerChunk = 0;
			this.theBiomeDecorator.grassPerChunk = 0;
			this.theBiomeDecorator.deadBushPerChunk = 63;
			this.theBiomeDecorator.mushroomsPerChunk = 0;
			this.theBiomeDecorator.reedsPerChunk = 42;
			this.theBiomeDecorator.cactiPerChunk = 84;
			this.theBiomeDecorator.sandPerChunk = 84;
			this.rainfall = 0.1F;
			this.waterColorMultiplier = 13434879;
			setHeight(new BiomeGenBase.Height(0.10F, 0.35F));
			this.rootHeight = -0.15f; //Ground level

			this.spawnableMonsterList.clear();
			this.spawnableCreatureList.clear();
			this.spawnableWaterCreatureList.clear();
			this.spawnableCaveCreatureList.clear();

			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityCow.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySilverfish.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 1, 5));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 5, 1, 5));
			
			this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 5, 1, 5));

		}

		private synchronized boolean setBiomeID() {
			BiomeGenBase[] mTempList;
			try {
				Field mInternalBiomeList = ReflectionUtils.getField(BiomeGenBase.class, "biomeList");
				Field mClone = mInternalBiomeList;
				mTempList = (BiomeGenBase[]) mInternalBiomeList.get(null);
				if (mTempList != null){
					mTempList[CORE.AUSTRALIA_BIOME_DESERT_2_ID] = this;
					mInternalBiomeList.set(null, mTempList);
					if (mTempList != mInternalBiomeList.get(null)){
						ReflectionUtils.setFinalStatic(mInternalBiomeList, mTempList);
						Logger.REFLECTION("Set Biome ID for "+this.biomeName+" Biome internally in 'biomeList' field from "+BiomeGenBase.class.getCanonicalName()+".");						
						return true;
					}
					else {
						Logger.REFLECTION("Failed to set Biome ID for "+this.biomeName+" Biome internally in 'biomeList' field from "+BiomeGenBase.class.getCanonicalName()+".");					
					}
				}
				return false;
			}
			catch (Exception e) {
				Logger.REFLECTION("Could not access 'biomeList' field in "+BiomeGenBase.class.getCanonicalName()+".");
				return false;
			}			
		}

		@SideOnly(Side.CLIENT)
		public int getBiomeGrassColor()	{
			return 6697728;
		}

		@SideOnly(Side.CLIENT)
		public int getBiomeFoliageColor() {
			return 6697728;
		}

		@SideOnly(Side.CLIENT)
		public int getSkyColorByTemp(float par1) {
			return 13421772;
		}

		@SuppressWarnings({ "unchecked", "unused" })
		private boolean addToMonsterSpawnLists(Class<?> EntityClass, int a, int b, int c){
			//this.spawnableMonsterList.add(new SpawnListEntry(EntityClass, a, b, c));
			this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityClass, a, b, c));
			return true;
		}
		
		@Override
		public WorldGenAbstractTree func_150567_a(Random par1Random){
		    return (WorldGenAbstractTree)(getRandomWorldGenForTrees(par1Random));
		}

		//TODO - DOES THIS WORK?
		public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
		{
			return new Tree();
		}

		class Tree
		extends WorldGenerator
		{
			private final int minTreeHeight;
			private final boolean vinesGrow;
			private final int metaWood;
			private final int metaLeaves;

			public Tree()
			{
				super();
				this.minTreeHeight = 5;
				this.metaWood = 0;
				this.metaLeaves = 0;
				this.vinesGrow = false;
			}

			public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
			{
				int var6 = par2Random.nextInt(3) + this.minTreeHeight;
				boolean var7 = true;
				if ((par4 >= 1) && (par4 + var6 + 1 <= 256))
				{
					for (int var8 = par4; var8 <= par4 + 1 + var6; var8++)
					{
						byte var9 = 1;
						if (var8 == par4) {
							var9 = 0;
						}
						if (var8 >= par4 + 1 + var6 - 2) {
							var9 = 2;
						}
						for (int var10 = par3 - var9; (var10 <= par3 + var9) && (var7); var10++) {
							for (int var11 = par5 - var9; (var11 <= par5 + var9) && (var7); var11++) {
								if ((var8 >= 0) && (var8 < 256))
								{
									Block var12s = par1World.getBlock(var10, var8, var11);
									int var12 = Block.getIdFromBlock(var12s);
									if ((var12 != 0) && (var12s != Blocks.air) && (var12s != Blocks.grass) && (var12s != Blocks.dirt) && (var12s != Blocks.cactus)) {
										var7 = false;
									}
								}
								else
								{
									var7 = false;
								}
							}
						}
					}
					if (!var7) {
						return false;
					}
					Block var8s = par1World.getBlock(par3, par4 - 1, par5);
					int var8 = Block.getIdFromBlock(var8s);
					if (((var8s == Blocks.grass) || (var8s == Blocks.dirt)) && (par4 < 256 - var6 - 1))
					{
						par1World.setBlock(par3, par4 - 1, par5, Blocks.dirt, 0, 2);
						byte var9 = 3;
						byte var18 = 0;
						for (int var11 = par4 - var9 + var6; var11 <= par4 + var6; var11++)
						{
							int var12 = var11 - (par4 + var6);
							int var13 = var18 + 1 - var12 / 2;
							for (int var14 = par3 - var13; var14 <= par3 + var13; var14++)
							{
								int var15 = var14 - par3;
								for (int var16 = par5 - var13; var16 <= par5 + var13; var16++)
								{
									int var17 = var16 - par5;
									if ((Math.abs(var15) != var13) || (Math.abs(var17) != var13) || ((par2Random.nextInt(2) != 0) && (var12 != 0))) {
										par1World.setBlock(var14, var11, var16, Blocks.air, this.metaLeaves, 2);
									}
								}
							}
						}
						for (int var11 = 0; var11 < var6; var11++)
						{
							Block var12s = par1World.getBlock(par3, par4 + var11, par5);
							int var12 = Block.getIdFromBlock(var12s);
							if ((var12 == 0) || (var12s == Blocks.air))
							{
								par1World.setBlock(par3, par4 + var11, par5, Blocks.cactus, this.metaWood, 2);
								if ((this.vinesGrow) && (var11 > 0))
								{
									if ((par2Random.nextInt(3) > 0) && (par1World.isAirBlock(par3 - 1, par4 + var11, par5))) {
										par1World.setBlock(par3 - 1, par4 + var11, par5, Blocks.air, 8, 2);
									}
									if ((par2Random.nextInt(3) > 0) && (par1World.isAirBlock(par3 + 1, par4 + var11, par5))) {
										par1World.setBlock(par3 + 1, par4 + var11, par5, Blocks.air, 2, 2);
									}
									if ((par2Random.nextInt(3) > 0) && (par1World.isAirBlock(par3, par4 + var11, par5 - 1))) {
										par1World.setBlock(par3, par4 + var11, par5 - 1, Blocks.air, 1, 2);
									}
									if ((par2Random.nextInt(3) > 0) && (par1World.isAirBlock(par3, par4 + var11, par5 + 1))) {
										par1World.setBlock(par3, par4 + var11, par5 + 1, Blocks.air, 4, 2);
									}
								}
							}
						}
						if (this.vinesGrow)
						{
							for (int var11 = par4 - 3 + var6; var11 <= par4 + var6; var11++)
							{
								int var12 = var11 - (par4 + var6);
								int var13 = 2 - var12 / 2;
								for (int var14 = par3 - var13; var14 <= par3 + var13; var14++) {
									for (int var15 = par5 - var13; var15 <= par5 + var13; var15++) {
										if (par1World.getBlock(var14, var11, var15) == Blocks.air)
										{
											if ((par2Random.nextInt(4) == 0) && 
													(Block.getIdFromBlock(par1World.getBlock(var14 - 1, var11, var15)) == 0)) {
												growVines(par1World, var14 - 1, var11, var15, 8);
											}
											if ((par2Random.nextInt(4) == 0) && 
													(Block.getIdFromBlock(par1World.getBlock(var14 + 1, var11, var15)) == 0)) {
												growVines(par1World, var14 + 1, var11, var15, 2);
											}
											if ((par2Random.nextInt(4) == 0) && 
													(Block.getIdFromBlock(par1World.getBlock(var14, var11, var15 - 1)) == 0)) {
												growVines(par1World, var14, var11, var15 - 1, 1);
											}
											if ((par2Random.nextInt(4) == 0) && 
													(Block.getIdFromBlock(par1World.getBlock(var14, var11, var15 + 1)) == 0)) {
												growVines(par1World, var14, var11, var15 + 1, 4);
											}
										}
									}
								}
							}
							if ((par2Random.nextInt(5) == 0) && (var6 > 5)) {
								for (int var11 = 0; var11 < 2; var11++) {
									for (int var12 = 0; var12 < 4; var12++) {
										if (par2Random.nextInt(4 - var11) == 0)
										{
											int var13 = par2Random.nextInt(3);
											par1World.setBlock(par3 + net.minecraft.util.Direction.offsetX[net.minecraft.util.Direction.rotateOpposite[var12]], par4 + var6 - 5 + var11, par5 + net.minecraft.util.Direction.offsetZ[net.minecraft.util.Direction.rotateOpposite[var12]], Blocks.air, var13 << 2 | var12, 2);
										}
									}
								}
							}
						}
						return true;
					}
					return false;
				}
				return false;
			}

			private void growVines(World par1World, int par2, int par3, int par4, int par5)
			{
				par1World.setBlock(par2, par3, par4, Blocks.vine, par5, 2);
				int var6 = 4;
				for (;;)
				{
					par3--;
					if ((Block.getIdFromBlock(par1World.getBlock(par2, par3, par4)) != 0) || (var6 <= 0)) {
						return;
					}
					par1World.setBlock(par2, par3, par4, Blocks.air, par5, 2);
					var6--;
				}
			}
		}

	}

}
