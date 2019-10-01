package com.github.technus.tectech.loader.gui;

import com.github.technus.tectech.thing.item.gui.ProgrammerScreen;
import com.github.technus.tectech.thing.item.gui.ScanDisplayScreen;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by danie_000 on 17.12.2017.
 */
public class ModGuiHandler implements IGuiHandler {
    public static final int SCAN_DISPLAY_SCREEN_ID =0;
    public static final int PROGRAMMER_DISPLAY_SCREEN_ID =1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == SCAN_DISPLAY_SCREEN_ID) {
            return new ScanDisplayScreen(player);
        }else if(ID==PROGRAMMER_DISPLAY_SCREEN_ID){
            return new ProgrammerScreen(player);
        }
        return null;
    }
}
