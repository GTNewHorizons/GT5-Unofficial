package gtPlusPlus.xmod.sc2;

import static gregtech.api.enums.Mods.StevesCarts2;

import vswe.stevescarts.ModuleData.GppModuleData;

public class HANDLER_SC2 {

    public static synchronized void preInit() {
        if (StevesCarts2.isModLoaded()) {
            GppModuleData.loadGpp();
        }
    }
}
