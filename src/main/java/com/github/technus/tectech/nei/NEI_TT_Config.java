package com.github.technus.tectech.nei;

import codechicken.nei.api.IConfigureNEI;
import com.github.technus.tectech.recipe.TT_recipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class NEI_TT_Config implements IConfigureNEI { // must be NEI*Config
    public static boolean sIsAdded = true;
    public static TT_NEI_ResearchHandler TT_RH;
    public static TT_NEI_ScannerHandler TT_SH;
    public static TT_NEI_EyeOfHarmonyHandler TT_EOH;

    @Override
    public void loadConfig() {
        sIsAdded = false;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TT_RH = new TT_NEI_ResearchHandler(TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes);
            TT_SH = new TT_NEI_ScannerHandler(TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes);
            TT_EOH = new TT_NEI_EyeOfHarmonyHandler(TT_recipe.GT_Recipe_MapTT.sEyeofHarmonyRecipes);

            sendHandler("gt.recipe.eyeofharmony", "gregtech:gt.blockmachines:15410", 1);
            sendCatalyst("gt.recipe.eyeofharmony", "gregtech:gt.blockmachines:15410");


        }
        sIsAdded = true;
    }

    @Override
    public String getName() {
        return "TecTech NEI Plugin";
    }

    @Override
    public String getVersion() {
        return "(1.0)";
    }

    private static void sendHandler(String aName, String aBlock, int aMaxRecipesPerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "GregTech");
        aNBT.setString("modId", "gregtech");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", aMaxRecipesPerPage);
        aNBT.setInteger("yShift", 6);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }

    private static void sendHandler(String aName, String aBlock) {
        sendHandler(aName, aBlock, 2);
    }

    private static void sendCatalyst(String aName, String aStack, int aPriority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", aName);
        aNBT.setString("itemName", aStack);
        aNBT.setInteger("priority", aPriority);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String aName, String aStack) {
        sendCatalyst(aName, aStack, 0);
    }
}
