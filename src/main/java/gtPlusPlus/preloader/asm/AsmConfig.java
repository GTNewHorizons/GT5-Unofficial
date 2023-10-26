package gtPlusPlus.preloader.asm;

import java.io.File;
import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import gtPlusPlus.preloader.Preloader_Logger;

public class AsmConfig {

    public static Configuration config;

    public static boolean enableOreDictPatch;
    public static boolean enableGtTooltipFix;
    public static boolean enableGtNbtFix;
    public static boolean enableGtCharcoalPitFix;
    public static boolean enableCofhPatch;
    public static boolean enableTcAspectSafety;
    public static boolean enabledLwjglKeybindingFix;
    public static boolean enabledFixEntitySetHealth;
    public static boolean enableThaumicTinkererRepairFix;

    public static boolean disableAllLogging;
    public static boolean debugMode;

    static {
        config = new Configuration(new File("config/GTplusplus/asm.cfg"));
        syncConfig(true);
    }

    public static void syncConfig(boolean load) {
        ArrayList<String> propOrder = new ArrayList<>();
        ArrayList<String> propOrderDebug = new ArrayList<>();

        try {
            if (!config.isChild && load) {
                config.load();
            }

            Property prop;

            // Debug
            prop = config.get("debug", "disableAllLogging", true);
            prop.comment = "Disables ALL logging from GT++.";
            prop.setLanguageKey("gtpp.disableAllLogging").setRequiresMcRestart(false);
            disableAllLogging = prop.getBoolean(true);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "debugMode", false);
            prop.comment = "Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)";
            prop.setLanguageKey("gtpp.debugMode").setRequiresMcRestart(false);
            debugMode = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enabledFixEntitySetHealth", false);
            prop.comment = "Enable/Disable entity setHealth() fix.";
            prop.setLanguageKey("gtpp.enabledFixEntitySetHealth").setRequiresMcRestart(true);
            enabledFixEntitySetHealth = prop.getBoolean(false);
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

            prop = config.get("debug", "enableOreDictPatch", false);
            prop.comment = "Enable/Disable Forge OreDictionary Patch (Useful for Development)";
            prop.setLanguageKey("gtpp.enableOreDictPatch").setRequiresMcRestart(true);
            enableOreDictPatch = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableThaumicTinkererRepairFix", false);
            prop.comment = "Enable/Disable Patch for Thaumic Repairer";
            prop.setLanguageKey("gtpp.enableThaumicTinkererRepairFix").setRequiresMcRestart(true);
            enableThaumicTinkererRepairFix = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            // General Features
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

            prop = config.get("general", "enableGtCharcoalPitFix", true);
            prop.comment = "Makes the Charcoal Pile Igniter work better.";
            prop.setLanguageKey("gtpp.enableGtCharcoalPitFix").setRequiresMcRestart(true);
            enableGtCharcoalPitFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            // TC Aspect Safety
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

            Preloader_Logger.INFO("Gt Nbt Fix - Enabled: " + enableGtNbtFix);
            Preloader_Logger.INFO("Gt Tooltip Fix - Enabled: " + enableGtTooltipFix);
            Preloader_Logger.INFO("COFH Patch - Enabled: " + enableCofhPatch);
            Preloader_Logger.INFO("Thaumcraft Aspect Safety Patch - Enabled: " + enableTcAspectSafety);
            Preloader_Logger
                    .INFO("Fix bad usage of EntityLivingBase.setHealth Patch - Enabled: " + enabledFixEntitySetHealth);

        } catch (Exception var3) {
            FMLLog.log(Level.ERROR, var3, "GT++ ASM had a problem loading it's config", new Object[0]);
        }
    }
}
