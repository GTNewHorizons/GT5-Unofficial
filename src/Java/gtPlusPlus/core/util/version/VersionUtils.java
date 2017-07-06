package gtPlusPlus.core.util.version;

import java.util.Arrays;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.networking.NetworkUtils;
import gtPlusPlus.core.util.version.Version.SUFFIX;

public class VersionUtils {

	public static Version getVersionObjectFromGithub(){
		String str = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
		String versionNumber = str.substring(0, str.indexOf('-'));
		Object[] version = Arrays.stream(versionNumber.split("\\.")).map(Integer::parseInt).toArray(size -> new Object[size]);
		String versionType = str.substring(str.indexOf('-') + 1, str.length()).toLowerCase();

		SUFFIX suffix;
		if (versionType.toLowerCase().equals("alpha")){
			suffix = SUFFIX.Alpha;
		}
		else if (versionType.toLowerCase().equals("beta")){
			suffix = SUFFIX.Beta;
		}
		else if (versionType.toLowerCase().equals("prerelease")){
			suffix = SUFFIX.Prerelease;
		}
		else {
			suffix = SUFFIX.Release;
		}
		Utils.LOG_INFO("Recommended Version of GT++ [According to Master Version File] is "+version[0]+"."+version[1]+"."+version[2]+"-"+suffix.getSuffix()+".");
		return new Version((int)version[0], (int)version[1], (int)version[2], suffix);
	}
	
	public static String getVersionObjectAsString(Version version){
			return (version.Major+"."+version.Minor+"."+version.Minor2+"-"+version.Suffix);
	}

	public static boolean isModUpToDate(){
		if (CORE.VERSION.isUpdateRequired(CORE.MASTER_VERSION)){
			return false;
		}
		return true;		
	}

}
