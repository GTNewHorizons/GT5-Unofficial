package goodgenerator.crossmod.nei;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        sendHandler("goodgenerator.crossmod.nei.NeutronActivatorHandler", "gregtech:gt.blockmachines:32013");
        sendHandler("goodgenerator.crossmod.nei.ExtremeHeatExchangerHandler", "gregtech:gt.blockmachines:32017");
        sendHandler("goodgenerator.crossmod.nei.PreciseAssemblerHandler", "gregtech:gt.blockmachines:32018");

        sendCatalyst("gg.recipe.neutron_activator", "gregtech:gt.blockmachines:32013");
        sendCatalyst("gg.recipe.extreme_heat_exchanger", "gregtech:gt.blockmachines:32017");
        sendCatalyst("gg.recipe.precise_assembler", "gregtech:gt.blockmachines:32018");
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32019", -1);
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32020", -1);
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32021", -1);
        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32022", -1);
//        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:32023"); // Compact Fusion MK-V
        sendCatalyst("gt.recipe.assembler", "gregtech:gt.blockmachines:32018");
    }

    private static void sendHandler(String aName, String aBlock) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "Good Generator");
        aNBT.setString("modId", "GoodGenerator");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 1);
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
