package gtPlusPlus.preloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class CORE_Preloader {

    public static final String NAME = "GT++ Preloader";
    public static final String MODID = "GT++_Preloader";
    public static final String VERSION = "0.5-Beta";
    public static final List<ArtifactVersion> DEPENDENCIES;
    public static final String JAVA_VERSION = System.getProperty("java.version");

    public static File MC_DIR;
    public static boolean DEV_ENVIRONMENT = false;
    public static boolean DEBUG_MODE = false;
    public static boolean enableOldGTcircuits = false;
    public static int enableWatchdogBGM = 0;

    public static void setMinecraftDirectory(File aDir) {
        MC_DIR = aDir;
    }

    static {
        ArrayList<ArtifactVersion> deps = new ArrayList<>();
        // deps.add("required-before:gregtech;");
        DEPENDENCIES = Collections.unmodifiableList(deps);
    }
}
