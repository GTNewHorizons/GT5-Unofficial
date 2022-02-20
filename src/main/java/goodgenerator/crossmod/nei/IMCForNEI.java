package goodgenerator.crossmod.nei;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        setNBTAndSend("goodgenerator.crossmod.nei.NeutronActivatorHandler", "gregtech:gt.blockmachines:32013");
        setNBTAndSend("goodgenerator.crossmod.nei.ExtremeHeatExchangerHandler", "gregtech:gt.blockmachines:32017");
        setNBTAndSend("goodgenerator.crossmod.nei.PreciseAssemblerHandler", "gregtech:gt.blockmachines:32018");
    }

    private static void setNBTAndSend(String aName, String aBlock) {
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
}
