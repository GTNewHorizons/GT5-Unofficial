package gtPlusPlus.xmod.galacticraft.system.core.space;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.galacticraft.system.objects.IPlanetBlockRegister;
import gtPlusPlus.xmod.galacticraft.system.objects.PlanetGenerator;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody.ScalableDistance;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import net.minecraft.util.ResourceLocation;

public abstract class BaseSolarSystem {
	
	public final String mSystemName;
	private SolarSystem mSolarSystem;
	private Star mStar;
	private AutoMap<Planet> mPlanetMap = new AutoMap<Planet>();

	
	public BaseSolarSystem(String aSystemName) {
		mSystemName = aSystemName;
		Logger.SPACE("Creating new Solar System: "+aSystemName);
	}
	
	public SolarSystem getSystem() {
		return mSolarSystem;
	}
	
	public Star getStar() {
		return mStar;
	}
	
	public AutoMap<Planet> getPlanets(){
		return mPlanetMap;
	}
	
	public abstract void preInit();

	public final void init() {
		Logger.SPACE("Initialising planetary masses within "+mSystemName);
		initSolarSystem();
	}
	
	public abstract void initSolarSystem();

	public static void registryteleport(Class<? extends WorldProviderSpace> aWorldProvider, ITeleportType aWorldProviderInstance) {
		Logger.SPACE("Registering world teleporter for "+aWorldProvider.getName());
		GalacticraftRegistry.registerTeleportType(aWorldProvider, aWorldProviderInstance);
	}
	
	public boolean registerSolarSystem(SolarSystem aSystem) {
		this.mSolarSystem = aSystem;
		Logger.SPACE("Registering "+mSystemName);
		return GalaxyRegistry.registerSolarSystem(aSystem);
	}

	public boolean registerPlanet(BaseGalacticDimension aDimension) {
		return registerPlanet(aDimension.getPlanet().getPlanet(), aDimension.getWorldProviderClass(), aDimension.getWorldProvider());
	}
	public boolean registerPlanet(Planet aPlanet, Class<? extends WorldProviderSpace> aWorldProvider, ITeleportType aWorldProviderInstance) {
		try {
			Logger.SPACE("Registering "+aPlanet.getLocalizedName());
			mPlanetMap.put(aPlanet);
			GalaxyRegistry.registerPlanet(aPlanet);		
			registryteleport(aWorldProvider, aWorldProviderInstance);
			GalacticraftRegistry.registerRocketGui(aWorldProvider,	new ResourceLocation(CORE.MODID, "textures/space/RocketGui.png"));
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}
	
	public SolarSystem createSolarSystem(String aSystemName, String aParentGalaxyName, Vector3 aMapPosition) {
		Logger.SPACE("Creating Solar System in GC using information from "+mSystemName);
		SolarSystem aSolarSystem = (new SolarSystem(aSystemName, aParentGalaxyName)).setMapPosition(aMapPosition);
		return aSolarSystem;
	}
	
	public Star createStar(String aStarName, int aTierRequired) {
		Logger.SPACE("Creating new Star named "+aStarName);
		Star aStar = (Star) (new Star(aStarName)).setParentSolarSystem(getSystem()).setTierRequired(aTierRequired);
		aStar.setBodyIcon(getGalacticTexture(aStarName));
		return aStar;
	}
	
	public PlanetGenerator createPlanet(String aPlanetName, int aTier, float[] aRingRGB, float aPhaseShift, float aRelativeDistanceFromCentMin, float aRelativeDistanceFromCentMax, float aRelativeOrbitTime, IPlanetBlockRegister aPlanetBlocks) {		
		Logger.SPACE("Creating "+aPlanetName);
		Planet aNewPlanet = (new Planet(aPlanetName)).setParentSolarSystem(getSystem());
		aNewPlanet.setRingColorRGB(aRingRGB[0], aRingRGB[1], aRingRGB[2]);
		aNewPlanet.setPhaseShift(aPhaseShift);
		aNewPlanet.setBodyIcon(getGalacticTexture(aPlanetName));
		aNewPlanet.setRelativeDistanceFromCenter(new ScalableDistance(aRelativeDistanceFromCentMin, aRelativeDistanceFromCentMax));
		aNewPlanet.setRelativeOrbitTime(aRelativeOrbitTime);
		if (aTier > 0)
		aNewPlanet.setTierRequired(aTier);
		PlanetGenerator aPlanet = new PlanetGenerator(aNewPlanet, aPlanetBlocks);
		return aPlanet;
	}
	
	public void setMainStarForSolarSystem(Star aStar) {
		this.mStar = aStar;
		getSystem().setMainStar(aStar);
		Logger.SPACE("Setting "+aStar.getName()+" as main Star for "+getSystem().getName()+" within the "+getSystem().getLocalizedParentGalaxyName()+" Galaxy.");
	}
	
	private ResourceLocation getGalacticTexture(String aName) {
		String aText = getSystem().getUnlocalizedName();
		aText = aText.replace("solarsystem.", "");
		aName = aName.replace(aText+"-", "");
		
		ResourceLocation aVal = new ResourceLocation(CORE.MODID, "textures/space/"+aText.toLowerCase()+"/"+aName+".png");	
		Logger.SPACE("Trying to obtain ResourceLocation for "+aVal.toString());		
		return aVal;
	}
	
	
}