package com.github.technus.tectech.loader;

import com.github.technus.tectech.thing.item.gui.ScanDisplayScreen;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by danie_000 on 17.12.2017.
 */
public class ModGuiHandler implements IGuiHandler {
    public static final int SCAN_DISPLAY_SCREEN_ID =0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case SCAN_DISPLAY_SCREEN_ID:
                return new ScanDisplayScreen(player);
            default: return null;
        }
    }
}
