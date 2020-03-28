import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.common.base.Strings;

import net.minecraftforge.gradle.GradleStartCommon;

public class GradleStartClient extends GradleStart {

	public static void main(String[] args) {

		// hack natives.
		try {
			GradleStartCommon.LOGGER.info("Injecting Natives!");
			hack();
			GradleStartCommon.LOGGER.info("Natives Injected!");
			// launch
			Method launch = GradleStartCommon.class.getDeclaredMethod("launch", String[].class);
			if (launch != null) {
				launch.setAccessible(true);
				GradleStart aStart = new GradleStart();
				GradleStartCommon.LOGGER.info("Launching!");
				launch.invoke(aStart, new Object[]{args});
			}			
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static final void hack() {
		GradleStartCommon.LOGGER.info("[FG_1.3] Doing Classloader hack.");
		String paths = System.getProperty("java.library.path");
		String nativesDir = "@@NATIVESDIR@@";

		if (Strings.isNullOrEmpty(paths))
			paths = nativesDir;
		else
			paths += File.pathSeparator + nativesDir;
		
		hackNativesFixed(paths);
		//addLibraryPath(nativesDir);	
		//addLibraryPath(paths);		
	}
	
	private static final void hackNativesFixed(String paths) {
		System.setProperty("java.library.path", paths);
		// hack the classloader now.
		try
		{
			String aPathData = System.getProperty("java.library.path");
			final Method initializePathMethod = ClassLoader.class.getDeclaredMethod("initializePath", String.class);
			GradleStartCommon.LOGGER.info("[FG_1.3] Setting private method 'initializePath' to be accessible.");
			initializePathMethod.setAccessible(true);
			GradleStartCommon.LOGGER.info("[FG_1.3] Invoking 'initializePath' with arg 'java.library.path'.");
			GradleStartCommon.LOGGER.info("[FG_1.3] Path Value: "+aPathData);
			final Object usrPathsValue = initializePathMethod.invoke(null, "java.library.path");
			final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
			GradleStartCommon.LOGGER.info("[FG_1.3] Setting private field 'usr_paths' to be accessible.");
			usrPathsField.setAccessible(true);
			GradleStartCommon.LOGGER.info("[FG_1.3] Injecting path data.");
			usrPathsField.set(null, usrPathsValue);
			GradleStartCommon.LOGGER.info("[FG_1.3] Finished Classloader hack.");
		}
		catch(Throwable t) {
			GradleStartCommon.LOGGER.info("[FG_1.3] Error handling Classloader hack, printing stack trace.");
			t.printStackTrace();
		};
	}
	
	public static void addLibraryPath(String pathToAdd){
	    Field usrPathsField;
		try {
			usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		    usrPathsField.setAccessible(true);

		    //get array of paths
		    final String[] paths = (String[])usrPathsField.get(null);

		    //check if the path to add is already present
		    for(String path : paths) {
		        if(path.equals(pathToAdd)) {
					GradleStartCommon.LOGGER.info("[FG_1.3] Found existing PATH data, skipping.");
		            return;
		        }
		    }
			GradleStartCommon.LOGGER.info("[FG_1.3] Injecting path data. ["+pathToAdd+"]");
		    //add the new path
		    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		    newPaths[newPaths.length-1] = pathToAdd;
		    usrPathsField.set(null, newPaths);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			GradleStartCommon.LOGGER.info("[FG_1.3] Error handling Classloader hack, printing stack trace.");
			e.printStackTrace();
		}

	}

}
