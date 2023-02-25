package gregtech.api.util;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;

public enum GT_ModReference {

    RAILCRAFT(GT_Values.MOD_ID_RC),
    GTPP(GT_Values.MOD_ID_GTPP),;

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
