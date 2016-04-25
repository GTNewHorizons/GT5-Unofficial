package miscutil.core.util.uptime;

import java.util.logging.Level;


import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

public class UptimeLog {
	
	//UptimeLog.log("INFO", "text");
	//(ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE and WARNING)
	
	private static Logger logger = Logger.getLogger(Uptime.MODID);
	
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
		logger.log(logNotif, message);
	}
	
}
