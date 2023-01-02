package com.elisis.gtnhlanth.xmod.nei;

import codechicken.nei.api.IConfigureNEI;
import com.elisis.gtnhlanth.Tags;

public class NEI_Config implements IConfigureNEI {

    public static boolean isAdded = true;

    @Override
    public String getName() {
        return "GTNH: Lanthanides NEI";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }

    @Override
    public void loadConfig() {
        isAdded = false;
        isAdded = true;
    }
}
