/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.nei;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import kubatech.Tags;
import kubatech.api.LoaderReference;

public class IMCForNEI {

    public static void IMCSender() {
        if (LoaderReference.EnderIO) sendCatalyst("mobsinfo.mobhandler", "gregtech:gt.blockmachines:14201");
    }

    private static void sendHandler(String aName, String aBlock, int width, int height, int maxrecipesperpage,
        int yshift) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", Tags.MODNAME);
        aNBT.setString("modId", Tags.MODID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", height);
        aNBT.setInteger("handlerWidth", width);
        aNBT.setInteger("maxRecipesPerPage", maxrecipesperpage);
        aNBT.setInteger("yShift", yshift);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }

    private static void sendGTStyledHandler(String aName, String aBlock) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", Tags.MODNAME);
        aNBT.setString("modId", Tags.MODID);
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
