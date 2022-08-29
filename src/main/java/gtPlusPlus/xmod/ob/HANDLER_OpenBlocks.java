package gtPlusPlus.xmod.ob;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;

public class HANDLER_OpenBlocks {

    public static void preInit() {
        if (LoadedMods.OpenBlocks) {}
    }

    public static void init() {
        if (LoadedMods.OpenBlocks) {
            GliderHandler.populateBlacklist();
        }
    }

    public static void postInit() {
        if (LoadedMods.OpenBlocks) {
            Utils.registerEvent(new GliderHandler());
        }
    }
}
