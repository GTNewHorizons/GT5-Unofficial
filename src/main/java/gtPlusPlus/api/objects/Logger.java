package gtPlusPlus.api.objects;

import org.apache.logging.log4j.LogManager;

import gtPlusPlus.core.config.ASMConfiguration;

public class Logger {

    // Logging Functions
    public static final org.apache.logging.log4j.Logger modLogger = LogManager.getLogger("GT++");

    private static final boolean enabled = !ASMConfiguration.debug.disableAllLogging;

    // Developer Comments
    // can't delete, used in cropsnh
    public static void WARNING(final String s) {
        if (enabled) {
            if (ASMConfiguration.debug.debugMode) {
                modLogger.warn(s);
            }
        }
    }
}
