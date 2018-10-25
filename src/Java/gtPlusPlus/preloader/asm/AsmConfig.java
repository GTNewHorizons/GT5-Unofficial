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
			prop = config.get("debug", "enableChunkDebugging", false);
			prop.comment = "Enable/Disable Chunk Debugging Features, Must Be enabled on Client and Server.";
			prop.setLanguageKey("gtpp.enableChunkDebugging").setRequiresMcRestart(true);
			enableChunkDebugging = prop.getBoolean(true);
			propOrderDebug.add(prop.getName());
			
			prop = config.get("debug", "enableGtNbtFix", true);
			prop.comment = "Enable/Disable GT NBT Persistency Fix";
			prop.setLanguageKey("gtpp.enableGtNbtFix").setRequiresMcRestart(true);
			enableGtNbtFix = prop.getBoolean(true);
			propOrderDebug.add(prop.getName());
			

			//General Features
			prop = config.get("general", "enableTiConFluidLighting", true);
			prop.comment = "Enable/Disable Brightness Visuals for Tinkers Fluids, only required on the Client.";
			prop.setLanguageKey("gtpp.enableTiConFluidLighting").setRequiresMcRestart(true);
			enableTiConFluidLighting = prop.getBoolean(true);
			propOrder.add(prop.getName());
			
			prop = config.get("general", "enableGtTooltipFix", true);
			prop.comment = "Enable/Disable Custom GT Tooltips";
			prop.setLanguageKey("gtpp.enableGtTooltipFix").setRequiresMcRestart(true);
			enableGtTooltipFix = prop.getBoolean(true);
			propOrder.add(prop.getName());
			

			config.setCategoryPropertyOrder("general", propOrder);
			config.setCategoryPropertyOrder("debug", propOrderDebug);
			if (config.hasChanged()) {
				config.save();
			}

			System.out.println("[GT++ ASM] Chunk Debugging - Enabled: "+enableChunkDebugging);
			System.out.println("[GT++ ASM] Gt Nbt Fix - Enabled: "+enableGtNbtFix);
			System.out.println("[GT++ ASM] TiCon Fluid Lighting - Enabled: "+enableTiConFluidLighting);
			System.out.println("[GT++ ASM] Gt Tooltip Fix - Enabled: "+enableGtTooltipFix);
			
		} catch (Exception var3) {
			FMLLog.log(Level.ERROR, var3, "GT++ ASM has a problem loading it's config", new Object[0]);
		}

	}
}