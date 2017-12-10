package gtPlusPlus.core.util;

public class SystemUtils {

	private static OS SystemType;
		
	public static OS getOS(){
		if (SystemType != null){
			return SystemType;
		}
		else {
			SystemType = getOperatingSystem();
			return SystemType;
		}
	}
	
	public static boolean isWindows() {
		return (getOSString().indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (getOSString().indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (getOSString().indexOf("nix") >= 0 || getOSString().indexOf("nux") >= 0 || getOSString().indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		return (getOSString().indexOf("sunos") >= 0);
	}

	public static String getOSString(){
		try {
		return System.getProperty("os.name").toLowerCase();
		}
		catch (Throwable t){
			return "other";
		}
	}
	
	public static OS getOperatingSystem(){
		if (isMac()){
			return OS.MAC;
		}
		else if (isWindows()){
			return OS.WINDOWS;
		}
		else if (isUnix()){
			return OS.UNIX;
		}
		else if (isSolaris()){
			return OS.SOLARIS;
		}
		else {
			return OS.OTHER;
		}
	}
	
	public static enum OS {
		MAC(1),
		WINDOWS(2),
		UNIX(3),
		SOLARIS(4),
		OTHER(0);

		private int mID;
		private OS (final int ID){
			this.mID = ID;
		}

		public int getID() {
			return this.mID;
		}
	}
	
}
