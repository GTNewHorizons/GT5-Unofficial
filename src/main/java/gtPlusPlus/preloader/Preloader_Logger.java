package gtPlusPlus.preloader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Preloader_Logger {

    private Preloader_Logger() {}

    // Logging Functions
    public static final Logger MODLOGGER = LogManager.getLogger("GT++ ASM");

    public static Logger getLogger() {
        return MODLOGGER;
    }

    // Non-Dev Comments

    public static void INFO(final String s, final String s2) {
        INFO(s);
        INFO(s2);
    }

    public static void INFO(final String s) {
        MODLOGGER.info(s);
    }

    // Developer Comments
    public static void WARNING(final String s) {
        MODLOGGER.warn(s);
    }

    // Errors
    public static void ERROR(final String s) {
        MODLOGGER.fatal(s);
    }

    public static void LOG(String string, Level info, String string2) {
        if (info.equals(Level.INFO)) {
            INFO("[" + string + "] " + string2);
        }
        if (info.equals(Level.WARN)) {
            WARNING("[" + string + "] " + string2);
        }
        if (info.equals(Level.ERROR)) {
            ERROR("[" + string + "] " + string2);
        }
    }
}
