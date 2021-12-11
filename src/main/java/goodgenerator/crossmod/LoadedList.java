package goodgenerator.crossmod;

import cpw.mods.fml.common.Loader;

public class LoadedList {

    public static boolean GTPP;
    public static boolean GTNH_CORE;
    public static boolean BOTDUSTRIES;

    public static void init() {
        GTPP = Loader.isModLoaded("miscutils");
        GTNH_CORE = Loader.isModLoaded("dreamcraft");
        BOTDUSTRIES = Loader.isModLoaded("botdustries");
    }

}
