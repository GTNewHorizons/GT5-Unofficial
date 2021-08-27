package GoodGenerator.CrossMod.NEI;

import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        NBTTagCompound info = new NBTTagCompound();
        setNBTInfo(info, "gg.recipe.neutron_activator", "gregtech:gt.blockmachines:32013");
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", info);
    }

    private static void setNBTInfo(NBTTagCompound aNBT, String aName, String aBlock) {
        aNBT.setString("handler", aName);
        aNBT.setString("modName", GoodGenerator.MOD_NAME);
        aNBT.setString("modId", GoodGenerator.MOD_ID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 1);
    }
}
