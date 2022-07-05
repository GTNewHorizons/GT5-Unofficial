package gregtech.nei;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        if (!Loader.isModLoaded("NotEnoughItems")) {
            return;
        }

        sendHandler("gt.recipe.plasmaforge", "gregtech:gt.blockmachines:1004");
        sendCatalyst("gt.recipe.plasmaforge", "gregtech:gt.blockmachines:1004");

        sendHandler("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1193");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1193");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1194");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1195");
    }

    private static void sendHandler(String aName, String aBlock) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "GregTech");
        aNBT.setString("modId", "gregtech");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
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
