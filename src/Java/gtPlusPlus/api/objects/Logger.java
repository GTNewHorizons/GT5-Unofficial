package gtPlusPlus.api.objects;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.proxy.ClientProxy;
import gtPlusPlus.preloader.asm.AsmConfig;

public class Logger {

	public Logger(String string) {

	}

	// Logging Functions
	public static final org.apache.logging.log4j.Logger modLogger = Logger.makeLogger();

	// Generate GT++ Logger
	public static org.apache.logging.log4j.Logger makeLogger() {
		final org.apache.logging.log4j.Logger gtPlusPlusLogger = LogManager.getLogger("GT++");
		return gtPlusPlusLogger;
	}

	private static final boolean enabled = !AsmConfig.disableAllLogging;

	public static final org.apache.logging.log4j.Logger getLogger(){
		return modLogger;
	}

	// Non-Dev Comments
	public static void INFO(final String s) {
		if (enabled) {
			modLogger.info(s);
		}
	}

	// Non-Dev Comments
	public static void MACHINE_INFO(final String s) {
		if (enabled) {

			boolean localPlayer = false;
			try {
				if (ClientProxy.playerName != null){
					if (ClientProxy.playerName.toLowerCase().contains("draknyte1")){
						localPlayer = true;
					}
				}
			}
			catch (final Throwable t){

			}

			if (CORE.ConfigSwitches.MACHINE_INFO || localPlayer) {
				final String name1 = gtPlusPlus.core.util.reflect.ReflectionUtils.getMethodName(2);
				modLogger.info("Machine Info: " + s + " | " + name1);
			}
		}
	}

	// Developer Comments
	public static void WARNING(final String s) {
		if (enabled) {
			if (CORE.DEBUG) {
				modLogger.warn(s);
			}
		}
	}

	// Errors
	public static void ERROR(final String s) {
		if (enabled) {
			if (CORE.DEBUG) {
				modLogger.fatal(s);
			}
		}
	}

	// Developer Logger
	public static void SPECIFIC_WARNING(final String whatToLog, final String msg, final int line) {
		if (enabled) {
			// if (!CORE.DEBUG){
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
		if (enabled) {
			if (CORE.DEVENV || CORE.DEBUG) {
				modLogger.info("[Bees] "+s);
			}
		}
	}
	/**
	 * Special Logger for Debugging Bee related content
	 */
	public static void DEBUG_BEES(final String s) {
		if (enabled) {
			if (CORE.DEVENV || CORE.DEBUG) {
				modLogger.info("[Debug][Bees] "+s);
			}
		}
	}



	/**
	 * Special Logger for Materials related content
	 */
	public static void MATERIALS(final String s) {
		if (enabled) {
			if (/* CORE.DEVENV || */CORE.DEBUG) {
				modLogger.info("[Materials] "+s);
			}
		}
	}
	/**
	 * Special Logger for Debugging Materials related content
	 */
	public static void DEBUG_MATERIALS(final String s) {
		if (enabled) {
			if (CORE.DEVENV || CORE.DEBUG) {
				modLogger.info("[Debug][Materials] "+s);
			}
		}
	}

	/**
	 * Special Logger for Reflection related content
	 */
	public static void REFLECTION(final String s) {
		if (enabled) {
			if (CORE.DEVENV || CORE.DEBUG) {
				modLogger.info("[Reflection] "+s);
			}
		}
	}


	/**
	 * Special Logger for Darkworld related content
	 */
	public static void WORLD(final String s) {
		if (enabled) {
			if (CORE.DEVENV || CORE.DEBUG) {
				modLogger.info("[WorldGen] "+s);
			}
		}
	}

	public static void RECIPE(String string) {
		if (enabled) {
			if (/*CORE.DEVENV || */CORE.DEBUG) {
				modLogger.info("[Recipe] "+string);		
			}
		}
	}	

	public static void SPACE(final String s) {
		if (enabled) {
			modLogger.info("[Space] "+s);
		}
	}



}
