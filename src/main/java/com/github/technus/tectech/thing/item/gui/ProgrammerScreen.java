package com.github.technus.tectech.thing.item.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ProgrammerScreen extends GuiScreen {
    private NBTTagCompound tag;

    public ProgrammerScreen(EntityPlayer player){
        tag=player.getHeldItem().getTagCompound();
    }
}
