package gtPlusPlus.xmod.ob;

import static gregtech.api.enums.Mods.OpenBlocks;

import gtPlusPlus.core.util.Utils;

public class HANDLER_OpenBlocks {

    public static void init() {
        if (OpenBlocks.isModLoaded()) {
            GliderHandler.populateBlacklist();
        }
    }

    public static void postInit() {
        if (OpenBlocks.isModLoaded()) {
            Utils.registerEvent(new GliderHandler());
        }
    }
}
