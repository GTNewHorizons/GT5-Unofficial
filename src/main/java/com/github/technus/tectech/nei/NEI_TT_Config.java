package com.github.technus.tectech.nei;

import codechicken.nei.api.IConfigureNEI;
import com.github.technus.tectech.recipe.TT_recipe;
import cpw.mods.fml.common.FMLCommonHandler;

public class NEI_TT_Config implements IConfigureNEI {//must be NEI*Config
    public static boolean sIsAdded = true;
    public static TT_NEI_ResearchHandler TT_RH;
    public static TT_NEI_ScannerHandler TT_SH;

    public void loadConfig() {
        sIsAdded = false;
        if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TT_RH = new TT_NEI_ResearchHandler(TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes);
            TT_SH = new TT_NEI_ScannerHandler(TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes);
        }
        sIsAdded = true;
    }

    public String getName() {
        return "TecTech NEI Plugin";
    }

    public String getVersion() {
        return "(1.0)";
    }
}
