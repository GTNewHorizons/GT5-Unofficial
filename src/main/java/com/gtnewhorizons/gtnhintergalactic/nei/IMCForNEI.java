package com.gtnewhorizons.gtnhintergalactic.nei;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.gtnhintergalactic.Tags;

import cpw.mods.fml.common.event.FMLInterModComms;

public class IMCForNEI {

    public static void IMCSender() {
        sendHandler(
                "com.gtnewhorizons.gtnhintergalactic.nei.SpacePumpModuleRecipeHandler",
                "gregtech:gt.blockmachines:14010",
                160,
                90,
                3);
        sendCatalyst(
                "com.gtnewhorizons.gtnhintergalactic.nei.SpacePumpModuleRecipeHandler",
                "gregtech:gt.blockmachines:14010");
        sendCatalyst(
                "com.gtnewhorizons.gtnhintergalactic.nei.SpacePumpModuleRecipeHandler",
                "gregtech:gt.blockmachines:14011");

        sendHandler(
                "com.gtnewhorizons.gtnhintergalactic.nei.GasSiphonRecipeHandler",
                "gregtech:gt.blockmachines:14002",
                160,
                90,
                3);
        sendCatalyst(
                "com.gtnewhorizons.gtnhintergalactic.nei.GasSiphonRecipeHandler",
                "gregtech:gt.blockmachines:14002");

        sendHandler("gt.recipe.spaceMining", "gregtech:gt.blockmachines:14007");
        sendCatalyst("gt.recipe.spaceMining", "gregtech:gt.blockmachines:14007");
        sendCatalyst("gt.recipe.spaceMining", "gregtech:gt.blockmachines:14008");
        sendCatalyst("gt.recipe.spaceMining", "gregtech:gt.blockmachines:14009");

        sendHandler("gt.recipe.spaceAssembler", "gregtech:gt.blockmachines:14004");
        sendCatalyst("gt.recipe.spaceAssembler", "gregtech:gt.blockmachines:14004");
        sendCatalyst("gt.recipe.spaceAssembler", "gregtech:gt.blockmachines:14005");
        sendCatalyst("gt.recipe.spaceAssembler", "gregtech:gt.blockmachines:14006");

        sendHandler("gt.recipe.spaceResearch", "gregtech:gt.blockmachines:14013");
        sendCatalyst("gt.recipe.spaceResearch", "gregtech:gt.blockmachines:14013");

        sendHandler("gt.recipe.fakespaceprojects", "gregtech:gt.blockmachines:14012", 160, 166, 1);
        sendCatalyst("gt.recipe.fakespaceprojects", "gregtech:gt.blockmachines:14012");
    }

    private static void sendHandler(String name, String itemStack, int handlerWidth, int handlerHeight,
            int recipePerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", name);
        aNBT.setString("modName", Tags.MODNAME);
        aNBT.setString("modId", Tags.MODID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", itemStack);
        aNBT.setInteger("handlerHeight", handlerHeight);
        aNBT.setInteger("handlerWidth", handlerWidth);
        aNBT.setInteger("maxRecipesPerPage", recipePerPage);
        aNBT.setInteger("yShift", 6);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }

    private static void sendHandler(String name, String itemStack) {
        sendHandler(name, itemStack, 160, 166, 2);
    }

    private static void sendCatalyst(String name, String itemStack, int priority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", name);
        aNBT.setString("itemName", itemStack);
        aNBT.setInteger("priority", priority);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String name, String itemStack) {
        sendCatalyst(name, itemStack, 0);
    }
}
