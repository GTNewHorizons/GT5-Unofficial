package gtPlusPlus.xmod.galacticraft.system.objects;

public class WorldProviderSettings {

	private PlanetGenerator mPlanet;
	private final DimensionSettings mDimSettings;
	private final BiomeSettings mBiomeSettings;

	public WorldProviderSettings(DimensionSettings d, BiomeSettings b) {		
		mPlanet = d.getPlanet();
		mDimSettings = d;
		mBiomeSettings = b;
	}

	public synchronized final PlanetGenerator getPlanet() {
		return mPlanet;
	}
	
	public synchronized final void setPlanet(PlanetGenerator aPlanet) {
		mPlanet = aPlanet;
	}

	public synchronized final DimensionSettings getDimSettings() {
		return mDimSettings;
	}

	public synchronized final BiomeSettings getBiomeSettings() {
		return mBiomeSettings;
	}
}
