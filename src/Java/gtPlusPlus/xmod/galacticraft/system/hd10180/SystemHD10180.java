package gtPlusPlus.xmod.galacticraft.system.hd10180;

import gtPlusPlus.xmod.galacticraft.system.BaseSolarSystem;
import gtPlusPlus.xmod.galacticraft.system.core.dim.BasicChunkProviderGalactic;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.b.dim.WorldProviderHD10180B;
import gtPlusPlus.xmod.galacticraft.system.objects.BiomeSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.DimensionSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.PlanetGenerator;
import gtPlusPlus.xmod.galacticraft.system.objects.WorldProviderSettings;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class SystemHD10180 extends BaseSolarSystem {
	
	public void preInit() {
		//TCBlocks.initialize();
	}

	public void registrycelestial() {

	}

	@Override
	public void initSolarSystem() {
		
		SolarSystem aSystemHD10180 = createSolarSystem("HD10180", "hydrus", new Vector3(2.0D, -1.0D, 2.0D));
		this.registerSolarSystem(aSystemHD10180);
		
		Star aMainStar = this.createStar("HD10180-A", -1);
		this.setMainStarForSolarSystem(aMainStar);

		//Planet B
		PlanetGenerator B = this.createPlanet("HD10180-B", new float[] {0.2f, 0.2f, 0.2f}, 3.1415927F, 1f, 2f, 11.861994F, null);
		DimensionSettings Planet_B_Settings = new DimensionSettings(B, BasicChunkProviderGalactic.class, 5, true, 1, false, 240f, 0.1f, 0.2f, false, 48000L);
		BiomeSettings Planet_B_Biome = new BiomeSettings("HD10180-B", 255, 0.1f, 0.2f);
		this.registerPlanet(new WorldProviderHD10180B(new WorldProviderSettings(Planet_B_Settings, Planet_B_Biome)).getDim());
		
	}
}