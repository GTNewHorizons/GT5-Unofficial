package miscutil.core.util.uptime;

import java.util.logging.Level;
import java.util.logging.Logger;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import cpw.mods.fml.common.FMLLog;

public class UptimeLog {

	//UptimeLog.log("INFO", "text");
	//(ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE and WARNING)

	private static Logger logger = Logger.getLogger(CORE.MODID);

	public static void initLogger() {
		logger.setParent((Logger) FMLLog.getLogger());
	}
	
	
	public static void log(String logLevel, String message) {
		Level logNotif;
		if (logLevel.equals("INFO")){
			logNotif = Level.INFO;
		}
		else if (logLevel.equals("WARNING")){
			logNotif = Level.WARNING;
		}
		else if (logLevel.equals("SEVERE")){
			logNotif = Level.SEVERE;
		}
		else {
			logNotif = Level.CONFIG;
		}
		logWrapper(logLevel, message);
	}
	
	public static void log(String logLevel, String message, Object[] objectF) {
		Level logNotif;
		if (logLevel.equals("INFO")){
			logNotif = Level.INFO;
		}
		else if (logLevel.equals("WARNING")){
			logNotif = Level.WARNING;
		}
		else if (logLevel.equals("SEVERE")){
			logNotif = Level.SEVERE;
		}
		else {
			logNotif = Level.CONFIG;
		}
		logWrapper(logLevel, message);
	}

	private static void logWrapper(String level, String msg){
		if (level.equals("INFO")){
			Utils.LOG_INFO(msg);
		}
		else if (level.equals("WARNING")){
			Utils.LOG_WARNING(msg);
		} 
		else if (level.equals("SEVERE")){
			Utils.LOG_ERROR(msg);
		}
		else {
			Utils.LOG_INFO("Something Broke - "+msg);
		}
	}



}
