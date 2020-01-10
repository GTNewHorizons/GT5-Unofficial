package gtPlusPlus.preloader.asm;

import cpw.mods.fml.common.FMLLog;
import java.io.File;
import java.util.ArrayList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

public class AsmConfig {
	
	public static boolean loaded;
	public static Configuration config;
	
	public static boolean enableTiConFluidLighting;
	public static boolean enableGtTooltipFix;
	public static boolean enableGtNbtFix;
	public static boolean enableChunkDebugging;
	public static boolean enableCofhPatch;
	public static boolean enableGcFuelChanges;
	public static boolean enableRcFlowFix;
	public static boolean enableRcItemDupeFix;
	public static boolean enableTcAspectSafety;
	public static boolean enabledLwjglKeybindingFix;
	public static boolean enabledFixEntitySetHealth;
	
	public static boolean disableAllLogging;

	public AsmConfig(File file) {
		if (!loaded) {
			config = new Configuration(file);
			syncConfig(true);
		}

	}

	public static void syncConfig(boolean load) {
		ArrayList<String> propOrder = new ArrayList<String>();
		ArrayList<String> propOrderDebug = new ArrayList<String>();

		try {
			if (!config.isChild && load) {
				config.load();
			}

			Property prop;	
			
			//Debug			
			prop = config.get("debug", "disableAllLogging", false);
			prop.comment = "Disables ALL logging from GT++.";
			prop.setLanguageKey("gtpp.disableAllLogging").setRequiresMcRestart(false);
			disableAllLogging = prop.getBoolean(false);
			propOrderDebug.add(prop.getName());
			
			prop = config.get("debug", "enabledFixEntitySetHealth", false);
			prop.comment = "Enable/Disable entity setHealth() fix.";
			prop.setLanguageKey("gtpp.enabledFixEntitySetHealth").setRequiresMcRestart(true);
			enabledFixEntitySetHealth = prop.getBoolean(false);
			propOrderDebug.add(prop.getName());
			
			prop = config.get("debug", "enableChunkDebugging", false);
			prop.comment = "Enable/Disable Chunk Debugging Features, Must Be enabled on Client and Server.";
			prop.setLanguageKey("gtpp.enableChunkDebugging").setRequiresMcRestart(true);
			enableChunkDebugging = prop.getBoolean(false);
			propOrderDebug.add(prop.getName());
			
			prop = config.get("debug", "enableGtNbtFix", true);
			prop.comment = "Enable/Disable GT NBT Persistency Fix";
			prop.setLanguageKey("gtpp.enableGtNbtFix").setRequiresMcRestart(true);
			enableGtNbtFix = prop.getBoolean(true);
			propOrderDebug.add(prop.getName());
			
			prop = config.get("debug", "enableCofhPatch", false);
			prop.comment = "Enable/Disable COFH OreDictionaryArbiter Patch (Useful for Development)";
			prop.setLanguageKey("gtpp.enableCofhPatch").setRequiresMcRestart(true);
			enableCofhPatch = prop.getBoolean(false);
			propOrderDebug.add(prop.getName());
			
			
			

			//General Features
			prop = config.get("general", "enableTiConFluidLighting", true);
			prop.comment = "Enable/Disable Brightness Visuals for Tinkers Fluids, only required on the Client.";
			prop.setLanguageKey("gtpp.enableTiConFluidLighting").setRequiresMcRestart(true);
			enableTiConFluidLighting = prop.getBoolean(true);
			propOrder.add(prop.getName());

			prop = config.get("general", "enabledLwjglKeybindingFix", true);
			prop.comment = "Prevents the game crashing from having invalid keybinds. https://github.com/alkcorp/GTplusplus/issues/544";
			prop.setLanguageKey("gtpp.enabledLwjglKeybindingFix").setRequiresMcRestart(true);
			enabledLwjglKeybindingFix = prop.getBoolean(true);
			propOrder.add(prop.getName());			
			
			prop = config.get("general", "enableGtTooltipFix", true);
			prop.comment = "Enable/Disable Custom GT Tooltips";
			prop.setLanguageKey("gtpp.enableGtTooltipFix").setRequiresMcRestart(true);
			enableGtTooltipFix = prop.getBoolean(true);
			propOrder.add(prop.getName());
			
			prop = config.get("general", "enableGcFuelChanges", true);
			prop.comment = "Enable/Disable changes to Galacticraft Rocket Fuels.";
			prop.setLanguageKey("gtpp.enableGcFuelChanges").setRequiresMcRestart(true);			
			//Disabled because Broken
			//enableGcFuelChanges = prop.getBoolean(true);			
			enableGcFuelChanges = false;			
			propOrder.add(prop.getName());
			
			
			//Railcraft Tank fix
			prop = config.get("general", "enableRcFlowFix", true);
			prop.comment = "Quadruples max RC IO rates on tanks";
			prop.setLanguageKey("gtpp.enableRcFlowFix").setRequiresMcRestart(true);
			enableRcFlowFix = prop.getBoolean(true);
			propOrder.add(prop.getName());
			
			//Railcraft Dupe Fix			
			prop = config.get("general", "enableRcItemDupeFix", true);
			prop.comment = "Fixes possible negative itemstacks";
			prop.setLanguageKey("gtpp.enableRcItemDupeFix").setRequiresMcRestart(true);
			enableRcItemDupeFix = prop.getBoolean(true);
			propOrder.add(prop.getName());
			
			
			//TC Aspect Safety			
			prop = config.get("general", "enableTcAspectSafety", true);
			prop.comment = "Fixes small oversights in Thaumcraft 4.";
			prop.setLanguageKey("gtpp.enableTcAspectSafety").setRequiresMcRestart(true);
			enableTcAspectSafety = prop.getBoolean(true);
			propOrder.add(prop.getName());
			

			config.setCategoryPropertyOrder("general", propOrder);
			config.setCategoryPropertyOrder("debug", propOrderDebug);
			if (config.hasChanged()) {
				config.save();
			}

			FMLLog.log(Level.INFO, "[GT++ ASM] Chunk Debugging - Enabled: "+enableChunkDebugging, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Gt Nbt Fix - Enabled: "+enableGtNbtFix, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] TiCon Fluid Lighting - Enabled: "+enableTiConFluidLighting, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Gt Tooltip Fix - Enabled: "+enableGtTooltipFix, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] COFH Patch - Enabled: "+enableCofhPatch, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Gc Fuel Changes Patch - Enabled: "+enableGcFuelChanges, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Railcraft Fluid Flow Patch - Enabled: "+enableRcFlowFix, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Thaumcraft Aspect Safety Patch - Enabled: "+enableTcAspectSafety, new Object[0]);
			FMLLog.log(Level.INFO, "[GT++ ASM] Fix bad usage of EntityLivingBase.setHealth Patch - Enabled: "+enabledFixEntitySetHealth, new Object[0]);
			
		} catch (Exception var3) {
			FMLLog.log(Level.ERROR, var3, "GT++ ASM had a problem loading it's config", new Object[0]);
		}

	}
}