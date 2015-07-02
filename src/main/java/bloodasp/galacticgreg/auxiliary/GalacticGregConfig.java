package bloodasp.galacticgreg.auxiliary;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.BlockMetaComb;
import cpw.mods.fml.common.registry.GameRegistry;

public class GalacticGregConfig extends ConfigManager {
	
	public GalacticGregConfig(File pConfigBaseDirectory,
			String pModCollectionDirectory, String pModID) {
		super(pConfigBaseDirectory, pModCollectionDirectory, pModID);

	}

	public boolean ProfileOreGen; 
	public boolean ReportOreGenFailures;
	public boolean PrintDebugMessagesToFMLLog;
	public boolean PrintTraceMessagesToFMLLog;
	
	public boolean RegisterVanillaDim;
	public boolean RegisterGalacticCraftCore;
	public boolean RegisterGalacticCraftPlanets;
	public boolean RegisterGalaxySpace;
	public boolean LootChestsEnabled;
	public boolean EnableAEExportCommand;
	public boolean SchematicsEnabled;
	public boolean ProperConfigured;
	public String LootChestItemOverride;
	public boolean QuietMode;
	
	public BlockMetaComb CustomLootChest;
	
	@Override
	protected void PreInit() {
		ProfileOreGen = false;
		ReportOreGenFailures = false;
		PrintDebugMessagesToFMLLog = false;
		PrintTraceMessagesToFMLLog = false;
		
		LootChestsEnabled = true;
		
		RegisterVanillaDim = true;
		RegisterGalacticCraftCore = true;      
		RegisterGalacticCraftPlanets = true;
		RegisterGalaxySpace = true;
		
		// Default false, as it is WiP
		EnableAEExportCommand = false;
		SchematicsEnabled = false;
		
		ProperConfigured = false;
		LootChestItemOverride = "";
		QuietMode = false;
	}

	@Override
	protected void Init() {
		ProfileOreGen = _mainConfig.getBoolean("ProfileOreGen", "Debug", ProfileOreGen, "Enable to profile oregen and register the ingame command ggregprofiler");
		ReportOreGenFailures = _mainConfig.getBoolean("ReportOreGenFailures", "Debug", ReportOreGenFailures, "Report if a ore tileentity could not be placed");
		PrintDebugMessagesToFMLLog = _mainConfig.getBoolean("PrintDebugMessagesToFMLLog", "Debug", PrintDebugMessagesToFMLLog, "Enable debug output, not recommended for servers");
		PrintTraceMessagesToFMLLog = _mainConfig.getBoolean("PrintTraceMessagesToFMLLog", "Debug", PrintTraceMessagesToFMLLog, "Enable trace output. Warning: This will produce gazillions of log entries");
		QuietMode = _mainConfig.getBoolean("QuietMode", "Debug", QuietMode, "In quiet-mode only errors, warnings and fatals will be printed to the logfile/console");
		
		RegisterVanillaDim = _mainConfig.getBoolean("RegisterVanillaDim", "BuildInMods", RegisterVanillaDim, "Enable to register the build-in dimension definition for TheEnd - Asteroids");
		RegisterGalacticCraftCore = _mainConfig.getBoolean("RegisterGalacticCraftCore", "BuildInMods", RegisterGalacticCraftCore, "Enable to register the build-in dimension definition for GalacticCraft Core (The moon)");
		RegisterGalacticCraftPlanets = _mainConfig.getBoolean("RegisterGalacticCraftPlanets", "BuildInMods", RegisterGalacticCraftPlanets, "Enable to register the build-in dimension definition for GalacticCraft Planets (Mars, asteroids)");
		RegisterGalaxySpace = _mainConfig.getBoolean("RegisterGalaxySpace", "BuildInMods", RegisterGalaxySpace, "Enable to register the build-in dimension definition for GalaxySpace by BlesseNtumble");
		
		LootChestsEnabled = _mainConfig.getBoolean("LootChestsEnabled", "Extras", LootChestsEnabled, "Enables/disables the dungeon-chest generator system for asteroids. New config values will be generated if set to true");
		EnableAEExportCommand = _mainConfig.getBoolean("EnableAEExportCommand", "Extras", EnableAEExportCommand, "If set to true, you can export any structure stored on a AE2 spatial storage disk. (Can't be spawned yet, WiP). Requires SchematicsEnabled to be true");
		SchematicsEnabled = _mainConfig.getBoolean("SchematicsEnabled", "Extras", SchematicsEnabled, "Enable the experimental Schematics-handler to spawn exported schematics in dimensions. This is WiP, use at own risk");
		ProperConfigured = _mainConfig.getBoolean("IHaveConfiguredEverything", "main", ProperConfigured, "Set this to true to confirm that you've read the warnings about the massive change in WorldConfig.cfg and you backed-up / configured everything properly"); 
		LootChestItemOverride = _mainConfig.getString("CustomLootChest", "Extras", LootChestItemOverride, "Define the chest you wish to use as LootChest. use the <ModID>:<Name>:<meta> format or leave empty for the default Minecraft Chest");
		
		GalacticGreg.Logger.setDebugOutput(PrintDebugMessagesToFMLLog);
		GalacticGreg.Logger.setTraceOutput(PrintTraceMessagesToFMLLog);
		GalacticGreg.Logger.setQuietMode(QuietMode);
	}

	@Override
	protected void PostInit() {
		
	}
	
	public boolean serverPostInit()
	{
		CustomLootChest = new BlockMetaComb(Blocks.chest);
		try
		{
			if (LootChestItemOverride != "")
			{
				String[] args = LootChestItemOverride.split(":");
				String tMod;
				String tName;
				int tMeta;
				
				if (args.length >= 2)
				{
					tMod = args[0];
					tName = args[1];
					if (args.length == 3)
						tMeta = Integer.parseInt(args[2]);
					else
						tMeta = 0;
					
					Block tBlock = GameRegistry.findBlock(tMod, tName);
					if (tBlock != null)
					{
						GalacticGreg.Logger.debug("Found valid ChestOverride: %s. LootChest replaced", LootChestItemOverride);
						CustomLootChest = new BlockMetaComb(tBlock, tMeta);
					}
				}
			}
			
			return true;
		}
		catch (Exception e)
		{
			GalacticGreg.Logger.error("Unable to find custom chest override %s. Make sure item exists. Defaulting to Minecraft:chest", LootChestItemOverride);
			e.printStackTrace();
			return false;
		}
	}
}
