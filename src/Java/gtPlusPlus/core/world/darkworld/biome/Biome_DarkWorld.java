package gtPlusPlus.core.world.darkworld.biome;

import java.util.Random;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
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
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DEAD);
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
			this.theBiomeDecorator = new BiomeGenerator_Custom();
			Utils.LOG_INFO("Dark World Temperature Category: "+getTempCategory());
			this.setBiomeName("Dark World");
			this.topBlock = Dimension_DarkWorld.blockTopLayer;
			this.fillerBlock = Dimension_DarkWorld.blockSecondLayer;
			this.enableRain = true;
			this.enableSnow = false;
			this.rainfall = 0.7F;
			this.setHeight(new BiomeGenBase.Height(0.3F, 0.5F));
			this.heightVariation = 0.4F;
			this.waterColorMultiplier = 0x17290A;
			this.rootHeight = -0.25f; //Ground level

			this.spawnableMonsterList.clear();
			this.spawnableCreatureList.clear();
			this.spawnableWaterCreatureList.clear();
			this.spawnableCaveCreatureList.clear();
			
			//Enemies
			this.spawnableMonsterList.add(new SpawnListEntry(EntitySickBlaze.class, 10, 4, 10));			
			this.spawnableMonsterList.add(new SpawnListEntry(EntitySickBlaze.class, 60, 1, 2));
			this.spawnableMonsterList.add(new SpawnListEntry(EntityStaballoyConstruct.class, 30, 1, 2));
			//this.spawnableMonsterList.add(new SpawnListEntry(EntityStaballoyConstruct.class, 5, 1, 5));
			
			addToMonsterSpawnLists(EntityBlaze.class, 5, 1, 5);
			addToMonsterSpawnLists(EntityCaveSpider.class, 5, 1, 5);
			addToMonsterSpawnLists(EntityCreeper.class, 4, 1, 2);
			addToMonsterSpawnLists(EntityEnderman.class, 5, 1, 5);
			addToMonsterSpawnLists(EntitySkeleton.class, 5, 1, 5);
			addToMonsterSpawnLists(EntitySpider.class, 5, 1, 5);
			addToMonsterSpawnLists(EntityZombie.class, 5, 1, 5);

			//Passive
			this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 5, 5, 10));
			this.spawnableCreatureList.add(new SpawnListEntry(EntityBat.class, 4, 4, 8));
			this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 10));
			
			//Water
			this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 5, 1, 10));
			
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
			return 0xF67A14;
		}
		
		@SuppressWarnings("unchecked")
		private boolean addToMonsterSpawnLists(Class<?> EntityClass, int a, int b, int c){
			//this.spawnableMonsterList.add(new SpawnListEntry(EntityClass, a, b, c));
			this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityClass, a, b, c));
			return true;
		}

	}

}
