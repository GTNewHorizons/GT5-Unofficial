package com.detrav.proxies;

import com.detrav.DetravScannerMod;

import com.detrav.enums.DetravItemList;
import com.detrav.events.DetravLoginEventHandler;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.items.DetravMetaGeneratedTool01;
import cpw.mods.fml.common.network.IGuiHandler;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.Ic2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class CommonProxy implements IGuiHandler {

    public void onLoad() {

    }

    public void onPostLoad() {
        DetravLoginEventHandler.register();
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return null;
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return new DetravGuiProPick();
            default:
                return null;
        }
    }


    public void openProPickGui()
    {
        //just Client code
    }


    public void onPreInit()
    {

    }

    public void sendPlayerExeption(String s) {

    }
}
