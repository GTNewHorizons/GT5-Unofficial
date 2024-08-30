package com.detrav.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.detrav.events.DetravLoginEventHandler;
import com.detrav.gui.DetravScannerGUI;

import cpw.mods.fml.common.network.IGuiHandler;

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
            case DetravScannerGUI.GUI_ID:
                return null;
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravScannerGUI.GUI_ID:
                return new DetravScannerGUI();
            default:
                return null;
        }
    }

    public void openProspectorGUI() {
        // just Client code
    }

    public void onPreInit() {

    }

    public void sendPlayerExeption(String s) {

    }
}
