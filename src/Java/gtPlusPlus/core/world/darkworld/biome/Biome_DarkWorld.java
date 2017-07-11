package gtPlusPlus.core.world.darkworld.biome;

import java.util.Random;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_DarkWorld {

	public static BiomeGenbiomeDarkWorld biome = new BiomeGenbiomeDarkWorld();

	public Object instance;

	public Biome_DarkWorld() {
	}

	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.SPOOKY);
		BiomeManager.addSpawnBiome(biome);
		// BiomeManager.desertBiomes.add(new BiomeManager.BiomeEntry(biome,
		// 10));
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

	static class BiomeGenbiomeDarkWorld extends BiomeGenBase {
		@SuppressWarnings("unchecked")
		public BiomeGenbiomeDarkWorld() {
			super(CORE.DARKBIOME_ID);
			Utils.LOG_INFO("Dark World Temperature Category: "+getTempCategory());
			setBiomeName("Dark World");
			topBlock = Dimension_DarkWorld.blockTopLayer;
			fillerBlock = Dimension_DarkWorld.blockSecondLayer;
			theBiomeDecorator.generateLakes = true;
			theBiomeDecorator.treesPerChunk = 50;
			theBiomeDecorator.flowersPerChunk = 4;
			theBiomeDecorator.grassPerChunk = 10;
			theBiomeDecorator.deadBushPerChunk = 25;
			theBiomeDecorator.mushroomsPerChunk = 5;
			theBiomeDecorator.reedsPerChunk = 1;
			theBiomeDecorator.cactiPerChunk = 1;
			theBiomeDecorator.sandPerChunk = 8;
			enableRain = true;
			enableSnow = false;
			rainfall = 0.7F;
			setHeight(new BiomeGenBase.Height(0.15F, 0.65F));
			waterColorMultiplier = 0x2d0b2d;
			rootHeight = 48; //Ground level

			this.spawnableMonsterList.clear();
			this.spawnableCreatureList.clear();
			this.spawnableWaterCreatureList.clear();
			this.spawnableCaveCreatureList.clear();
			this.spawnableMonsterList.add(new SpawnListEntry(EntityBat.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityBlaze.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityCaveSpider.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityGiantZombie.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityMagmaCube.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntitySquid.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityWolf.class, 5, 1, 5));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 5, 1, 5));

		}

		@SideOnly(Side.CLIENT)
		public int getBiomeGrassColor() {
			return 0x111f11;
		}

		@SideOnly(Side.CLIENT)
		public int getBiomeFoliageColor() {
			return 0x111f11;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int getSkyColorByTemp(float par1) {
			return 0x333333;
		}

	}

}
