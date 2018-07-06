package gtPlusPlus.australia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.biome.Biome_AustralianDesert;
import gtPlusPlus.australia.biome.Biome_AustralianDesert2;
import gtPlusPlus.australia.block.AustraliaContentLoader;
import gtPlusPlus.australia.dimension.Dimension_Australia;
import gtPlusPlus.australia.gen.gt.WorldGen_Australia_Ores;
import gtPlusPlus.australia.gen.gt.WorldGen_GT_Australia_Base;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.Australia;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

@MCVersion(value = "1.7.10")
@Mod(modid = Australia.MODID, name = Australia.NAME, version = Australia.VERSION, dependencies = "required-after:Forge; after:dreamcraft; after:IC2; after:ihl; required-after:gregtech; required-after:miscutils;")
public class GTplusplus_Australia implements ActionListener {

	//Mod Instance
	@Mod.Instance(Australia.MODID)
	public static GTplusplus_Australia instance;
	public static GTPP_Config sCustomWorldgenFile = null;

	// Dark World Handler
	protected static volatile Biome_AustralianDesert Australian_Desert_Biome_1;
	protected static volatile Biome_AustralianDesert2 Australian_Desert_Biome_2;
	protected static volatile Dimension_Australia Australia_Dimension;
	public static int globalAustraliaPortalSpawnTimer = 0;

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Logger.INFO("Loading " + Australia.MODID + " V" + Australia.VERSION);

		//Setup
		setVars(event);

		Australia_Dimension = new Dimension_Australia();
		Australia_Dimension.instance = instance;		
		
		Australian_Desert_Biome_1 = (new Biome_AustralianDesert());
		Australian_Desert_Biome_2 = (new Biome_AustralianDesert2());		
		
		Australian_Desert_Biome_1.instance = instance;
		Australian_Desert_Biome_2.instance = instance;

		Australian_Desert_Biome_1.preInit(event);
		Australian_Desert_Biome_2.preInit(event);
		

		//Load/Set Custom Ore Gen
		sCustomWorldgenFile = new GTPP_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "Australia.cfg")));

	}

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Logger.INFO("Begin resource allocation for " + Australia.MODID + " V" +Australia.VERSION);

		//Load World and Biome
		GameRegistry.registerWorldGenerator(new WorldGen_GT_Australia_Base(), Short.MAX_VALUE);

		Australian_Desert_Biome_1.load();
		Australian_Desert_Biome_2.load();
		
		Australia_Dimension.load();

	}

	public static synchronized void GenerateOreMaterials() {

	}

	protected synchronized void setVars(FMLPreInitializationEvent event){	
		
		sCustomWorldgenFile = new GTPP_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "Australia.cfg")));
	
		if (DimensionManager.isDimensionRegistered(Dimension_Australia.DIMID)){
			Dimension_Australia.DIMID = DimensionManager.getNextFreeDimId();
		}

		/*
		 * Set World Generation Values
		 */
		WorldGen_Australia_Ores.generateValidOreVeins();
		WorldGen_GT_Australia_Base.oreveinPercentage = 32;
		WorldGen_GT_Australia_Base.oreveinAttempts = 16;
		WorldGen_GT_Australia_Base.oreveinMaxPlacementAttempts = 2;	
		if (CORE.DEBUG || CORE.DEVENV){
			WorldGen_GT_Australia_Base.debugWorldGen = true;
		}		
		AustraliaContentLoader.run();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		Australian_Desert_Biome_1.serverLoad(event);
		Australian_Desert_Biome_2.serverLoad(event);
	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		Logger.INFO("Finished loading Australia plugin for GT++.");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
