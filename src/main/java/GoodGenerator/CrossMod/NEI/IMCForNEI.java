package GoodGenerator.CrossMod.NEI;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        NBTTagCompound info = new NBTTagCompound();
        setNBTInfo(info, "GoodGenerator.CrossMod.NEI.NeutronActivatorHandler", "gregtech:gt.blockmachines:32013");
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", info);
    }

    private static void setNBTInfo(NBTTagCompound aNBT, String aName, String aBlock) {
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "Good Generator");
        aNBT.setString("modId", "GoodGenerator");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 1);
    }
}
