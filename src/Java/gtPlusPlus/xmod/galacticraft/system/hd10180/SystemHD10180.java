package gtPlusPlus.xmod.galacticraft.system.hd10180;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.galacticraft.system.core.dim.BasicChunkProviderGalactic;
import gtPlusPlus.xmod.galacticraft.system.core.space.BaseSolarSystem;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.b.blocks.BlockRegistrationHD10180B;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.b.dim.WorldProviderHD10180B;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.c.blocks.BlockRegistrationHD10180C;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.c.dim.WorldProviderHD10180C;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.d.blocks.BlockRegistrationHD10180D;
import gtPlusPlus.xmod.galacticraft.system.hd10180.planets.d.dim.WorldProviderHD10180D;
import gtPlusPlus.xmod.galacticraft.system.objects.BiomeSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.DimensionSettings;
import gtPlusPlus.xmod.galacticraft.system.objects.PlanetGenerator;
import gtPlusPlus.xmod.galacticraft.system.objects.WorldProviderSettings;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class SystemHD10180 extends BaseSolarSystem {
	
	public SystemHD10180() {
		super("HD10180");
	}

	public void preInit() {
		//Init Blocks
		BlockRegistrationHD10180B.initialize();
	}	

	@Override
	public void initSolarSystem() {		
		//Lets pick a nice place
		SolarSystem aSystemHD10180 = createSolarSystem(mSystemName, "milkyWay", new Vector3(-1.2D, 0.0D, 0.0D));
		this.registerSolarSystem(aSystemHD10180);
		//turn the lights on
		Star aMainStar = this.createStar(mSystemName+"-A",3);
		this.setMainStarForSolarSystem(aMainStar);
		
		//Planet B
		PlanetGenerator B = this.createPlanet(mSystemName+"-B", 5, new float[] {0.2f, 0.2f, 0.2f}, CORE.PI/2, 0.3f, 0.3f, 0.24096386F, new BlockRegistrationHD10180B());
		DimensionSettings Planet_B_Settings = new DimensionSettings(B, BasicChunkProviderGalactic.class, 5, false, 1, false, 240f, 0.1f, 0.2f, false, 12000L);
		BiomeSettings Planet_B_Biome = new BiomeSettings(mSystemName+"-B", 255, 0.1f, 0.2f);
		this.registerPlanet(new WorldProviderHD10180B(new WorldProviderSettings(Planet_B_Settings, Planet_B_Biome)).getDim());	

		//Planet C
		PlanetGenerator C = this.createPlanet(mSystemName+"-C", 4, new float[] {0.2f, 0.2f, 0.8f}, CORE.PI/2+0.45f, 0.5f, 0.5f, 2.861994F, new BlockRegistrationHD10180C());
		DimensionSettings Planet_C_Settings = new DimensionSettings(C, BasicChunkProviderGalactic.class, 4, true, 1, false, 120f, 2f, 2f, false, 24000L);
		BiomeSettings Planet_C_Biome = new BiomeSettings(mSystemName+"-C", 255, 0.1f, 0.5f);
		this.registerPlanet(new WorldProviderHD10180C(new WorldProviderSettings(Planet_C_Settings, Planet_C_Biome)).getDim());	

		//Planet D
		PlanetGenerator D = this.createPlanet(mSystemName+"-D", 3, new float[] {0.2f, 0.2f, 0.2f}, CORE.PI-0.55f, 1.2f, 1.2f, 16.861994F, new BlockRegistrationHD10180D());
		DimensionSettings Planet_D_Settings = new DimensionSettings(D, BasicChunkProviderGalactic.class, 3, true, 1, false, 240f, 1f, 0.0f, false, 48000L);
		BiomeSettings Planet_D_Biome = new BiomeSettings(mSystemName+"-D", 255, 0.2f, 0.4f);
		this.registerPlanet(new WorldProviderHD10180D(new WorldProviderSettings(Planet_D_Settings, Planet_D_Biome)).getDim());	
	}
}