package com.elisis.gtnhlanth.xmod.nei;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMC {

	public static void IMCSender() {
        setNBTAndSend("com.elisis.gtnhlanth.xmod.nei.DigesterHandler", "gregtech:gt.blockmachines:10500");
        setNBTAndSend("com.elisis.gtnhlanth.xmod.nei.DisstankHandler", "gregtech:gt.blockmachines:10501");
        
    }

    private static void setNBTAndSend(String aName, String aBlock) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", "GTNH: Lanthanides");
        aNBT.setString("modId", "gtnhlanth");
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 1);
        aNBT.setInteger("yShift", 6);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }
	
}
