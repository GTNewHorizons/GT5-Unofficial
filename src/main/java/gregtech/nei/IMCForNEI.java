package gregtech.nei;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;

public class IMCForNEI {

    public static void IMCSender() {
        if (!NotEnoughItems.isModLoaded()) {
            return;
        }

        sendHandler("gt.recipe.transcendentplasmamixerrecipes", "gregtech:gt.blockmachines:1006", 1);

        sendHandler("gt.recipe.plasmaforge", "gregtech:gt.blockmachines:1004", 1);

        // overwrite yShift to 6
        sendHandler("gt.recipe.fakeAssemblylineProcess", "gregtech:gt.blockmachines:1170");
        sendHandler("gt.recipe.nanoforge", "gregtech:gt.blockmachines:357");
        sendHandler("gt.recipe.pcbfactory", "gregtech:gt.blockmachines:356");
        sendHandler("gt.recipe.ic2nuke", "IC2:blockGenerator:5");
    }

    private static void sendHandler(String aName, String aBlock, int aMaxRecipesPerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "GregTech");
        aNBT.setString("modId", GregTech.ID);
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
}
