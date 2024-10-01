package gtPlusPlus.api.objects;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.asm.GTCorePlugin;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.core.config.Configuration;

public class Logger {

    public Logger() {}

    // Logging Functions
    public static final org.apache.logging.log4j.Logger modLogger = Logger.makeLogger();

    // Generate GT++ Logger
    public static org.apache.logging.log4j.Logger makeLogger() {
        return LogManager.getLogger("GT++");
    }

    private static final boolean enabled = !ASMConfiguration.debug.disableAllLogging;

    public static org.apache.logging.log4j.Logger getLogger() {
        return modLogger;
    }

    // Non-Dev Comments
    public static void INFO(final String s) {
        if (enabled) {
            modLogger.info(s);
        }
    }

    // Non-Dev Comments
    public static void MACHINE_INFO(String s, Object... args) {
        if (enabled) {
            if (Configuration.debug.MachineInfo || GTCorePlugin.isDevEnv()) {
                modLogger.info("Machine Info: " + s + " ", args);
            }
        }
    }

    // Developer Comments
    public static void WARNING(final String s) {
        if (enabled) {
            if (ASMConfiguration.debug.debugMode) {
                modLogger.warn(s);
            }
        }
    }

    // Errors
    public static void ERROR(final String s) {
        if (enabled) {
            if (ASMConfiguration.debug.debugMode) {
                modLogger.fatal(s);
            }
        }
    }

    // Developer Logger
    public static void SPECIFIC_WARNING(final String whatToLog, final String msg, final int line) {
        if (enabled) {
            // if (!CORE_Preloader.DEBUG_MODE){
            FMLLog.warning("GT++ |" + line + "| " + whatToLog + " | " + msg);
            // }
        }
    }

    // ASM Comments
    public static void LOG_ASM(final String s) {
        if (enabled) {
            FMLRelaunchLog.info("[Special ASM Logging] ", s);
        }
    }

    /**
     * Special Loggers
     */

    /**
     * Special Logger for Bee related content
     */
    public static void BEES(final String s) {
        modLogger.info("[Bees] " + s);
    }

    /**
     * Special Logger for Debugging Bee related content
     */
    public static void DEBUG_BEES(final String s) {
        if (enabled) {
            if (GTCorePlugin.isDevEnv() || ASMConfiguration.debug.debugMode) {
                modLogger.info("[Debug][Bees] " + s);
            }
        }
    }

    /**
     * Special Logger for Materials related content
     */
    public static void MATERIALS(final String s) {
        if (enabled) {
            if (GTCorePlugin.isDevEnv() || ASMConfiguration.debug.debugMode) {
                modLogger.info("[Materials] " + s);
            }
        }
    }

    /**
     * Special Logger for Debugging Materials related content
     */
    public static void DEBUG_MATERIALS(final String s) {
        if (enabled) {
            if (GTCorePlugin.isDevEnv() || ASMConfiguration.debug.debugMode) {
                modLogger.info("[Debug][Materials] " + s);
            }
        }
    }

    /**
     * Special Logger for Reflection related content
     */
    public static void REFLECTION(final String s) {
        if (enabled) {
            if (GTCorePlugin.isDevEnv() || ASMConfiguration.debug.debugMode) {
                modLogger.info("[Reflection] " + s);
            }
        }
    }

    /**
     * Special Logger for Darkworld related content
     */
    public static void WORLD(final String s) {
        if (enabled) {
            if (GTCorePlugin.isDevEnv() || ASMConfiguration.debug.debugMode) {
                modLogger.info("[WorldGen] " + s);
            }
        }
    }

    public static void RECIPE(String string) {
        if (enabled) {
            if (
            /* GTCorePlugin.isDevEnv() || */ ASMConfiguration.debug.debugMode) {
                modLogger.info("[Recipe] " + string);
            }
        }
    }

    public static void SPACE(final String s) {
        if (enabled) {
            modLogger.info("[Space] " + s);
        }
    }
}
