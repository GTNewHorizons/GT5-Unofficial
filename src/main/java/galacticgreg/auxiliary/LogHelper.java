package galacticgreg.auxiliary;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

/**
 * Generic LogHelper to print stuff to the console
 *
 * @author Namikon
 */
public final class LogHelper {

    private ArrayList<String> _mReportedCategories = new ArrayList<>();
    private boolean doDebugLogs = false;
    private boolean doTraceLogs = false;
    private boolean quietMode = false;
    private String _mModID = "";

    private final static String STR_NOCAT = "ihaznocathegory";
    private final static String STR_TOKEN_ONETIMEMESSAGE = " OTM";

    public LogHelper(String pModID) {
        _mModID = pModID;
    }

    /**
     * If true, only error/fatal/warn messages will be printed
     *
     * @param pEnabled
     */
    public void setQuietMode(boolean pEnabled) {
        quietMode = pEnabled;
    }

    /**
     * Enable/Disable debug logs
     *
     * @param pEnabled
     */
    public void setDebugOutput(boolean pEnabled) {
        doDebugLogs = pEnabled;
    }

    /**
     * Enable/Disable trace logs
     *
     * @param pEnabled
     */
    public void setTraceOutput(boolean pEnabled) {
        doTraceLogs = pEnabled;
    }

    /**
     * Resets all One-Time categories, so they will be displayed again
     */
    public void ResetCategories() {
        _mReportedCategories = new ArrayList<>();
    }

    /**
     * Print a log-message with built-in String.format(x) support. This message will only appear once. usefull for
     * error/warnings within loops
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param pLogLevel The logLevel for this message
     * @param pMessage  The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void log(String pCategory, Level pLogLevel, String pMessage, Object... args) {
        if (pLogLevel == Level.DEBUG && !doDebugLogs) return;

        if (pLogLevel == Level.TRACE && !doTraceLogs) return;

        if (pLogLevel != Level.ERROR && pLogLevel != Level.FATAL && pLogLevel != Level.WARN) if (quietMode) return;

        String tt = "";
        if (!pCategory.equals(STR_NOCAT)) {
            tt = STR_TOKEN_ONETIMEMESSAGE;
            if (_mReportedCategories.contains(pCategory)) return;
            else {
                _mReportedCategories.add(pCategory);
            }
        }

        FMLLog.log(_mModID.toUpperCase() + tt, pLogLevel, pMessage, args);
    }

    /**
     * Prints a one-time message with Category ALL
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_all(String pCategory, String object, Object... args) {
        log(pCategory, Level.ALL, object, args);
    }

    /**
     * Prints a one-time message with Category DEBUG
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_debug(String pCategory, String object, Object... args) {
        log(pCategory, Level.DEBUG, object, args);
    }

    /**
     * Prints a one-time message with Category ERROR
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_error(String pCategory, String object, Object... args) {
        log(pCategory, Level.ERROR, object, args);
    }

    /**
     * Prints a one-time message with Category FATAL
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_fatal(String pCategory, String object, Object... args) {
        log(pCategory, Level.FATAL, object, args);
    }

    /**
     * Prints a one-time message with Category INFO
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_info(String pCategory, String object, Object... args) {
        log(pCategory, Level.INFO, object, args);
    }

    /**
     * Prints a one-time message with Category OFF
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_off(String pCategory, String object, Object... args) {
        log(pCategory, Level.OFF, object, args);
    }

    /**
     * Prints a one-time message with Category TRACE
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_trace(String pCategory, String object, Object... args) {
        log(pCategory, Level.TRACE, object, args);
    }

    /**
     * Prints a one-time message with Category WARN
     *
     * @param pCategory The category for this message. Used to identify the function, use an easy to memorize name. Will
     *                  never be displayed
     * @param object    The log message
     * @param args      Optional args, if you've used format-specifier in pMessage
     */
    public void ot_warn(String pCategory, String object, Object... args) {
        log(pCategory, Level.WARN, object, args);
    }

    /**
     * Prints a message with Category ALL
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void all(String object, Object... args) {
        log(STR_NOCAT, Level.ALL, object, args);
    }

    /**
     * Prints a message with Category DEBUG
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void debug(String object, Object... args) {
        log(STR_NOCAT, Level.DEBUG, object, args);
    }

    /**
     * Prints a message with Category ERROR
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void error(String object, Object... args) {
        log(STR_NOCAT, Level.ERROR, object, args);
    }

    /**
     * Prints a message with Category FATAL
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void fatal(String object, Object... args) {
        log(STR_NOCAT, Level.FATAL, object, args);
    }

    /**
     * Prints a message with Category INFO
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void info(String object, Object... args) {
        log(STR_NOCAT, Level.INFO, object, args);
    }

    /**
     * Prints a message with Category OFF
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void off(String object, Object... args) {
        log(STR_NOCAT, Level.OFF, object, args);
    }

    /**
     * Prints a message with Category TRACE
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void trace(String object, Object... args) {
        log(STR_NOCAT, Level.TRACE, object, args);
    }

    /**
     * Prints a message with Category WARN
     *
     * @param object The log message
     * @param args   Optional args, if you've used format-specifier in pMessage
     */
    public void warn(String object, Object... args) {
        log(STR_NOCAT, Level.WARN, object, args);
    }
}
