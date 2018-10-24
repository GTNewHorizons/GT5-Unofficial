package gtPlusPlus.xmod.galacticraft.system.core.space;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.xmod.galacticraft.system.objects.DimensionSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.PlanetGenerator;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldChunkManagerSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class BaseGalacticDimension {

	private final WorldChunkManagerGalactic mGlobalChunkManager;
	private final WorldProviderGalactic mWorldProvider;
	private final Class<? extends IChunkProvider> mChunkProvider;
	private final PlanetGenerator mPlanet;

	public BaseGalacticDimension(PlanetGenerator aPlanet, BiomeGenBase aBiomeForDim, Class<? extends IChunkProvider> aChunkProvider, DimensionSettings aSettings) {
		mPlanet = aPlanet;
		mGlobalChunkManager = new WorldChunkManagerGalactic(aBiomeForDim);
		mChunkProvider = aChunkProvider;
		mWorldProvider = new WorldProviderGalactic(aSettings);
	}

	public class WorldChunkManagerGalactic extends WorldChunkManagerSpace {
		private final BiomeGenBase mBiome;

		public WorldChunkManagerGalactic(BiomeGenBase aDimBiome) {
			mBiome = aDimBiome;
		}

		public BiomeGenBase getBiome() {
			return mBiome;
		}
	}

	public class WorldProviderGalactic extends WorldProviderSpace implements IExitHeight, ISolarLevel, ITeleportType {

		private final int mTierRequirement;
		private final PlanetGenerator mPlanet;
		private final boolean mAtmosphere;
		private final int mPressure;
		private final boolean mSolarRadiation;
		private final float mCloudHeight;
		private final float mGravity;
		private final float mMeteorFreq;
		private final boolean mCanRainOrSnow;
		private final long mDayLength;
		private final Class<? extends IChunkProvider> mChunkProvider;
		
		public WorldProviderGalactic(DimensionSettings aSettings) {
			mPlanet = aSettings.getPlanet();
			mTierRequirement = aSettings.getTierRequirement();
			mAtmosphere = aSettings.hasAtmosphere();
			mPressure = aSettings.getPressure();
			mSolarRadiation = aSettings.hasSolarRadiation();
			mCloudHeight = aSettings.getCloudHeight();
			mGravity = aSettings.getGravity();
			mMeteorFreq = aSettings.getMeteorFreq();
			mCanRainOrSnow = aSettings.hasRainOrSnow();
			mDayLength = aSettings.getDayLength();
			mChunkProvider = aSettings.getChunkProvider();
		}

		public WorldProviderGalactic(PlanetGenerator aPlanet, Class<? extends IChunkProvider> aChunkProvider, int aTierRequirement, boolean aHasBreathableAtmo,
				int aPressure, boolean aSolarRadiation, float aCloudHeight, float aGravity, float aMeteorFreq, boolean aCanRainOrSnow, long aDayLength) {
			mPlanet = aPlanet;
			mTierRequirement = aTierRequirement;
			mAtmosphere = aHasBreathableAtmo;
			mPressure = aPressure;
			mSolarRadiation = aSolarRadiation;
			mCloudHeight = aCloudHeight;
			mGravity = aGravity;
			mMeteorFreq = aMeteorFreq;
			mCanRainOrSnow = aCanRainOrSnow;
			mDayLength = aDayLength;
			mChunkProvider = aChunkProvider;
		}

		public boolean canSpaceshipTierPass(int tier) {
			return tier >= mTierRequirement;
		}

		@SideOnly(Side.CLIENT)
		public float getCloudHeight() {
			return mCloudHeight;
		}

		public double getHorizon() {
			return 44.0D;
		}

		public float getFallDamageModifier() {
			return 0.16F;
		}

		public double getFuelUsageMultiplier() {
			return 0.8D;
		}

		public float getGravity() {
			return mGravity;
		}

		public double getMeteorFrequency() {
			return mMeteorFreq;
		}

		public float getSoundVolReductionAmount() {
			return Float.MAX_VALUE;
		}

		public float getThermalLevelModifier() {
			return 0.0F;
		}

		public float getWindLevel() {
			return 0.6F;
		}

		public boolean canRainOrSnow() {
			return mCanRainOrSnow;
		}

		public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
			return false;
		}

		public CelestialBody getCelestialBody() {
			return mPlanet.getPlanet();
		}

		public Class<? extends IChunkProvider> getChunkProviderClass() {
			return mChunkProvider;
		}

		public long getDayLength() {
			return mDayLength;
		}

		public boolean hasBreathableAtmosphere() {
			return mAtmosphere;
		}

		public Vector3 getFogColor() {
			float f = 1.0F - this.getStarBrightness(1.0F);
			return new Vector3((double) (0.65882355F * f), (double) (0.84705883F * f), (double) (1.0F * f));
		}

		public Vector3 getSkyColor() {
			float f = 1.0F - this.getStarBrightness(1.0F);
			return new Vector3((double) (0.25882354F * f), (double) (0.6666667F * f), (double) (1.0F * f));
		}

		public boolean isSkyColored() {
			return true;
		}

		public Class<? extends WorldChunkManagerGalactic> getWorldChunkManagerClass() {
			return WorldChunkManagerGalactic.class;
		}

		public boolean hasSunset() {
			return false;
		}

		public boolean shouldForceRespawn() {
			return !ConfigManagerCore.forceOverworldRespawn;
		}

		public double getSolarEnergyMultiplier() {
			return 0.8D;
		}

		public double getYCoordinateToTeleport() {
			return 800.0D;
		}

		public Vector3 getEntitySpawnLocation(WorldServer arg0, Entity arg1) {
			return new Vector3(arg1.posX, ConfigManagerCore.disableLander ? 250.0D : 900.0D, arg1.posZ);
		}

		public Vector3 getParaChestSpawnLocation(WorldServer arg0, EntityPlayerMP arg1, Random arg2) {
			if (ConfigManagerCore.disableLander) {
				double x = (arg2.nextDouble() * 2.0D - 1.0D) * 5.0D;
				double z = (arg2.nextDouble() * 2.0D - 1.0D) * 5.0D;
				return new Vector3(x, 220.0D, z);
			} else {
				return null;
			}
		}

		public Vector3 getPlayerSpawnLocation(WorldServer arg0, EntityPlayerMP arg1) {
			if (arg1 != null) {
				GCPlayerStats stats = GCPlayerStats.get(arg1);
				return new Vector3(stats.coordsTeleportedFromX, ConfigManagerCore.disableLander ? 250.0D : 900.0D,
						stats.coordsTeleportedFromZ);
			} else {
				return null;
			}
		}

		public void onSpaceDimensionChanged(World arg0, EntityPlayerMP player, boolean arg2) {
			if (player != null && GCPlayerStats.get(player).teleportCooldown <= 0) {
				if (player.capabilities.isFlying) {
					player.capabilities.isFlying = false;
				}

				EntityLandingBalloons lander = new EntityLandingBalloons(player);
				if (!arg0.isRemote) {
					arg0.spawnEntityInWorld(lander);
				}

				GCPlayerStats.get(player).teleportCooldown = 10;
			}

		}

		public boolean useParachute() {
			return ConfigManagerCore.disableLander;
		}

		@SideOnly(Side.CLIENT)
		public float getStarBrightness(float par1) {
			float var2 = this.worldObj.getCelestialAngle(par1);
			float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.25F);
			if (var3 < 0.0F) {
				var3 = 0.0F;
			}

			if (var3 > 1.0F) {
				var3 = 1.0F;
			}

			return var3 * var3 * 0.5F + 0.3F;
		}

		@SideOnly(Side.CLIENT)
		public float getSunBrightness(float par1) {
			float f1 = this.worldObj.getCelestialAngle(1.0F);
			float f2 = 1.25F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.2F);
			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			if (f2 > 1.0F) {
				f2 = 1.0F;
			}

			f2 = 1.2F - f2;
			return f2 * 0.2F;
		}

		public void setupAdventureSpawn(EntityPlayerMP player) {
		}

		public int AtmosphericPressure() {
			return mPressure;
		}

		public boolean SolarRadiation() {
			return mSolarRadiation;
		}
	}

	public synchronized final WorldChunkManagerGalactic getGlobalChunkManager() {
		return mGlobalChunkManager;
	}

	public synchronized final WorldProviderGalactic getWorldProvider() {
		return mWorldProvider;
	}
	
	public synchronized final Class<? extends WorldProviderSpace> getWorldProviderClass() {
		return mWorldProvider.getClass();
	}

	public synchronized final Class<? extends IChunkProvider> getChunkProvider() {
		return mChunkProvider;
	}

	public synchronized final PlanetGenerator getPlanet() {
		return mPlanet;
	}



}
