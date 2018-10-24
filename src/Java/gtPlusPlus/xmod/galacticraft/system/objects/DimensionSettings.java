package gtPlusPlus.xmod.galacticraft.system.objects;

import net.minecraft.world.chunk.IChunkProvider;

public class DimensionSettings {
	
	private final int TierRequirement;
	private final PlanetGenerator Planet;
	private final boolean Atmosphere;
	private final int Pressure;
	private final boolean SolarRadiation;
	private final float CloudHeight;
	private final float Gravity;
	private final float MeteorFreq;
	private final boolean CanRainOrSnow;
	private final long DayLength;
	private final Class<? extends IChunkProvider> ChunkProvider;

	public DimensionSettings(PlanetGenerator aPlanet, Class<? extends IChunkProvider> aChunkProvider, int aTierRequirement, boolean aHasBreathableAtmo,
			int aPressure, boolean aSolarRadiation, float aCloudHeight, float aGravity, float aMeteorFreq, boolean aCanRainOrSnow, long aDayLength) {
		Planet = aPlanet;
		TierRequirement = aTierRequirement;
		Atmosphere = aHasBreathableAtmo;
		Pressure = aPressure;
		SolarRadiation = aSolarRadiation;
		CloudHeight = aCloudHeight;
		Gravity = aGravity;
		MeteorFreq = aMeteorFreq;
		CanRainOrSnow = aCanRainOrSnow;
		DayLength = aDayLength;
		ChunkProvider = aChunkProvider;
	}

	public synchronized final int getTierRequirement() {
		return TierRequirement;
	}

	public synchronized final PlanetGenerator getPlanet() {
		return Planet;
	}

	public synchronized final boolean hasAtmosphere() {
		return Atmosphere;
	}

	public synchronized final int getPressure() {
		return Pressure;
	}

	public synchronized final boolean hasSolarRadiation() {
		return SolarRadiation;
	}

	public synchronized final float getCloudHeight() {
		return CloudHeight;
	}

	public synchronized final float getGravity() {
		return Gravity;
	}

	public synchronized final float getMeteorFreq() {
		return MeteorFreq;
	}

	public synchronized final boolean hasRainOrSnow() {
		return CanRainOrSnow;
	}

	public synchronized final long getDayLength() {
		return DayLength;
	}

	public synchronized final Class<? extends IChunkProvider> getChunkProvider() {
		return ChunkProvider;
	}
}
