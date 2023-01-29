package gtPlusPlus.xmod.sc2;

import vswe.stevescarts.ModuleData.GppModuleData;
import gtPlusPlus.core.lib.LoadedMods;

public class HANDLER_SC2 {

    public static synchronized void preInit() {
        if (LoadedMods.StevesCarts) {
            GppModuleData.loadGpp();
        }
    }

    public static void init() {}

    public static void postInit() {}
}
