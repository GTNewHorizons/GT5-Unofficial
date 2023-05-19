package goodgenerator.crossmod;

import cpw.mods.fml.common.Loader;

public class LoadedList {

    public static boolean GTPP;
    public static boolean GTNH_CORE;
    public static boolean BOTDUSTRIES;
    public static boolean THAUMIC_BASES;
    public static boolean THAUMIC_TINKERER;
    public static boolean AUTOMAGY;
    public static boolean WITCHING_GADGETS;
    public static boolean THAUMIC_ENERGISTICS;

    public static void init() {
        GTPP = Loader.isModLoaded("miscutils");
        GTNH_CORE = Loader.isModLoaded("dreamcraft");
        BOTDUSTRIES = Loader.isModLoaded("botdustries");
        THAUMIC_BASES = Loader.isModLoaded("thaumicbases");
        THAUMIC_TINKERER = Loader.isModLoaded("ThaumicTinkerer");
        AUTOMAGY = Loader.isModLoaded("Automagy");
        WITCHING_GADGETS = Loader.isModLoaded("WitchingGadgets");
        THAUMIC_ENERGISTICS = Loader.isModLoaded("thaumicenergistics");
    }
}
