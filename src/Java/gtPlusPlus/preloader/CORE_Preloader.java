package gtPlusPlus.preloader;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.versioning.ArtifactVersion;
import scala.actors.threadpool.Arrays;

public class CORE_Preloader {
	public static final String NAME = "GT++ Preloader";
	public static final String MODID = "GT++_Preloader";
	public static final String VERSION = "0.1-Alpha";
	public static boolean enableOldGTcircuits = false;
	@SuppressWarnings("unchecked")
	public static List<ArtifactVersion> DEPENDENCIES = new ArrayList<>(Arrays.asList(new String[] {"required-before:gregtech;"}));
}
