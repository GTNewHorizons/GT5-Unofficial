package com.github.bartimaeusnek.bartworks.neiHandler;

import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        sendCatalyst("gt.recipe.largechemicalreactor", "gregtech:gt.blockmachines:13366", -10);
        sendCatalyst("gt.recipe.craker", "gregtech:gt.blockmachines:13367", -10);
        sendHandler("bw.recipe.htgr", "gregtech:gt.blockmachines:12791");
        sendCatalyst("bw.recipe.htgr", "gregtech:gt.blockmachines:12791");
        sendHandler("bw.recipe.radhatch", "gregtech:gt.blockmachines:12713");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12713");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12714");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12715");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12716");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12717");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12718");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12719");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12720");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12721");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12722");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12723");
        sendCatalyst("bw.recipe.radhatch", "gregtech:gt.blockmachines:12724");
    }

    private static void sendHandler(String name, String block) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", name);
        aNBT.setString("modName", MainMod.NAME);
        aNBT.setString("modId", MainMod.MOD_ID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", block);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 2);
        aNBT.setInteger("yShift", 6);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
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
