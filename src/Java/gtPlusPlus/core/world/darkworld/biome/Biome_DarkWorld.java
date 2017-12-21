package gtPlusPlus.core.world.darkworld.biome;

import java.lang.reflect.Field;
import java.util.Random;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public class Biome_DarkWorld {

	public static BiomeGenDarkWorld biome = new BiomeGenDarkWorld();

	public Object instance;

	public Biome_DarkWorld() {
	}

	public void load() {
		BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DEAD);
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

	static class BiomeGenDarkWorld extends BiomeGenBase {
		@SuppressWarnings("unchecked")
		public BiomeGenDarkWorld() {
			super(CORE.DARKBIOME_ID);
			this.setBiomeID();
			this.theBiomeDecorator = new BiomeGenerator_Custom();
			Logger.INFO("Dark World Temperature Category: "+getTempCategory());
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
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySickBlaze.class, 100, 4, 4));
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityStaballoyConstruct.class, 80, 1, 2));

			//Animals
			this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 10, 4, 4));
			this.spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 10, 8, 8));

		}

		private synchronized boolean setBiomeID() {
			BiomeGenBase[] mTempList;
			try {
				Field mInternalBiomeList = ReflectionUtils.getField(BiomeGenBase.class, "biomeList");
				Field mClone = mInternalBiomeList;
				mTempList = (BiomeGenBase[]) mInternalBiomeList.get(null);
				if (mTempList != null){
					mTempList[CORE.DARKBIOME_ID] = this;
					mInternalBiomeList.set(null, mTempList);
					if (mClone != mInternalBiomeList && mClone.hashCode() != mInternalBiomeList.hashCode()){
						ReflectionUtils.setFinalStatic(mInternalBiomeList, mTempList);
						Logger.REFLECTION("Set Biome ID for Dark World Biome internally in 'biomeList' field from "+BiomeGenBase.class.getCanonicalName()+".");						
						return true;
					}
					else {
						Logger.REFLECTION("Failed to set Biome ID for Dark World Biome internally in 'biomeList' field from "+BiomeGenBase.class.getCanonicalName()+".");					
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

		@SuppressWarnings({ "unchecked", "unused" })
		private boolean addToMonsterSpawnLists(Class<?> EntityClass, int a, int b, int c){
			//this.spawnableMonsterList.add(new SpawnListEntry(EntityClass, a, b, c));
			this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityClass, a, b, c));
			return true;
		}

	}

}
