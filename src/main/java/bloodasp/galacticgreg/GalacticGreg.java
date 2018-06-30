package bloodasp.galacticgreg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bloodasp.galacticgreg.auxiliary.GalacticGregConfig;
import bloodasp.galacticgreg.auxiliary.LogHelper;
import bloodasp.galacticgreg.auxiliary.ProfilingStorage;
import bloodasp.galacticgreg.command.AEStorageCommand;
import bloodasp.galacticgreg.command.ProfilingCommand;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import bloodasp.galacticgreg.schematics.SpaceSchematicHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = GalacticGreg.MODID, version = GalacticGreg.VERSION, dependencies = "required-after:GalacticraftCore; required-after:gregtech@5.09.32.30;", acceptableRemoteVersions="*")
public class GalacticGreg {
	public static final List<GT_Worldgen_GT_Ore_SmallPieces_Space> smallOreWorldgenList = new ArrayList();
	public static final List<GT_Worldgen_GT_Ore_Layer_Space> oreVeinWorldgenList = new ArrayList();

	public static final String NICE_MODID = "GalacticGreg";
	public static final String MODID = "galacticgreg";
	
	public static final String VERSION = "GRADLETOKEN_VERSION";

	public static final LogHelper Logger = new LogHelper(NICE_MODID);
	public static ProfilingStorage Profiler = new ProfilingStorage();
	public static SpaceSchematicHandler SchematicHandler;

	public static Random GalacticRandom = null;
	
	public static GalacticGregConfig GalacticConfig = null;
	
	/**
	 * Preload phase. Read config values and set various features.. n stuff... 
	 * @param aEvent
	 */
	@EventHandler
	public void onPreLoad(FMLPreInitializationEvent aEvent) {
		GalacticConfig = new GalacticGregConfig(aEvent.getModConfigurationDirectory(), NICE_MODID, NICE_MODID);
		if (!GalacticConfig.LoadConfig())
			GalacticGreg.Logger.warn("Something went wrong while reading GalacticGregs config file. Things will be wonky..");
		
		GalacticRandom = new Random(System.currentTimeMillis());
		
		if (GalacticConfig.SchematicsEnabled)
			SchematicHandler = new SpaceSchematicHandler(aEvent.getModConfigurationDirectory());			

		Logger.trace("Leaving PRELOAD");
	}
	
	/**
	 * Postload phase. Mods can add their custom definition to our api in their own PreLoad or Init-phase
	 * Once GalacticGregRegistry.InitRegistry() is called, no changes are accepted.
	 * (Well you can with reflection, but on a "normal" way it's not possible)
	 * @param aEvent
	 */
	@EventHandler
	public void onPostLoad(FMLPostInitializationEvent aEvent) {
		Logger.trace("Entering POSTLOAD");

		if (!GalacticGregRegistry.InitRegistry())
			throw new RuntimeException("GalacticGreg registry has been finalized from a 3rd-party mod, this is forbidden!");
	
		//new WorldGenGaGT().run(); DO NOT UNCOMMENT, was moved to gregtech.loaders.postload.GT_Worldgenloader
		
		GalacticConfig.serverPostInit();
		
		Logger.trace("Leaving POSTLOAD");
	}
	
	/**
	 * If oregen profiling is enabled, then register the command
	 * @param pEvent
	 */
	@EventHandler
	public void serverLoad(FMLServerStartingEvent pEvent)
	{
		Logger.trace("Entering SERVERLOAD");
		
		if (GalacticConfig.ProfileOreGen)
			pEvent.registerServerCommand(new ProfilingCommand());
		
		if (Loader.isModLoaded("appliedenergistics2") && GalacticConfig.EnableAEExportCommand && GalacticConfig.SchematicsEnabled)
			pEvent.registerServerCommand(new AEStorageCommand());
		
		Logger.trace("Leaving SERVERLOAD");
	}
}
