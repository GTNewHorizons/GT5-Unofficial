package goodgenerator.crossmod.nei;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import goodgenerator.crossmod.LoadedList;

public class IMCForNEI {

    public static void IMCSender() {
        sendHandler("gg.recipe.neutron_activator", "gregtech:gt.blockmachines:32013");
        sendCatalyst("gg.recipe.neutron_activator", "gregtech:gt.blockmachines:32013");

        sendHandler("gg.recipe.extreme_heat_exchanger", "gregtech:gt.blockmachines:32017");
        sendCatalyst("gg.recipe.extreme_heat_exchanger", "gregtech:gt.blockmachines:32017");

        sendHandler("gg.recipe.precise_assembler", "gregtech:gt.blockmachines:32018");
        sendCatalyst("gg.recipe.precise_assembler", "gregtech:gt.blockmachines:32018");
        sendCatalyst("gt.recipe.assembler", "gregtech:gt.blockmachines:32018");

        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32019", -10);
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32020", -10);
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32021", -10);
        if (LoadedList.GTPP) {
            sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32022", -10);
            sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32023", -10);
        }

        sendHandler("gg.recipe.componentassemblyline", "gregtech:gt.blockmachines:32026", 2);
        sendCatalyst("gg.recipe.componentassemblyline", "gregtech:gt.blockmachines:32026");
    }

    private static void sendHandler(String aName, String aBlock) {
        sendHandler(aName, aBlock, 1);
    }

    private static void sendHandler(String aName, String aBlock, int maxRecipesPerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "Good Generator");
        aNBT.setString("modId", "GoodGenerator");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", maxRecipesPerPage);
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
