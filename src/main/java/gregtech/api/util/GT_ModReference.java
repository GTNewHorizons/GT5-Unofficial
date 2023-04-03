package gregtech.api.util;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Railcraft;

import cpw.mods.fml.common.Loader;

@Deprecated // use gregtech.api.enums.ModIDs instead
public enum GT_ModReference {

    RAILCRAFT(Railcraft.ID),
    GTPP(GTPlusPlus.ID),;

    private final boolean loaded;
    private final String modID;

    GT_ModReference(String modID) {
        loaded = Loader.isModLoaded(modID);
        this.modID = modID;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public String getModID() {
        return modID;
    }
}
