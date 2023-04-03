package gregtech.nei;

import static gregtech.api.enums.ModIDs.GregTech;
import static gregtech.api.enums.ModIDs.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;

public class IMCForNEI {

    public static void IMCSender() {
        if (!NotEnoughItems.isModLoaded()) {
            return;
        }

        sendHandler("gt.recipe.transcendentplasmamixerrecipes", "gregtech:gt.blockmachines:1006", 1);
        sendCatalyst("gt.recipe.transcendentplasmamixerrecipes", "gregtech:gt.blockmachines:1006");

        sendHandler("gt.recipe.plasmaforge", "gregtech:gt.blockmachines:1004", 1);
        sendCatalyst("gt.recipe.plasmaforge", "gregtech:gt.blockmachines:1004");

        sendHandler("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1193");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1193");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1194");
        sendCatalyst("gt.recipe.complexfusionreactor", "gregtech:gt.blockmachines:1195");

        sendCatalyst("gt.recipe.gasturbinefuel", "gregtech:gt.blockmachines:1005", -1);
        sendCatalyst("gt.recipe.gasturbinefuel", "gregtech:gt.blockmachines:1118");
        sendCatalyst("gt.recipe.gasturbinefuel", "gregtech:gt.blockmachines:1119");

        // overwrite yShift to 6
        sendHandler("gt.recipe.fakeAssemblylineProcess", "gregtech:gt.blockmachines:1170");
        sendHandler("gt.recipe.nanoforge", "gregtech:gt.blockmachines:357");
        sendCatalyst("gt.recipe.nanoforge", "gregtech:gt.blockmachines:357");
        sendHandler("gt.recipe.pcbfactory", "gregtech:gt.blockmachines:356");
        sendCatalyst("gt.recipe.pcbfactory", "gregtech:gt.blockmachines:356");
        sendHandler("gt.recipe.ic2nuke", "IC2:blockGenerator:5");
        sendCatalyst("gt.recipe.ic2nuke", "IC2:blockGenerator:5");
    }

    private static void sendHandler(String aName, String aBlock, int aMaxRecipesPerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "GregTech");
        aNBT.setString("modId", GregTech.modID);
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
