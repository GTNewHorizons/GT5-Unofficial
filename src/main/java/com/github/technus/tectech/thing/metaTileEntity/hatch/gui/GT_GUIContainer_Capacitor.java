package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_Capacitor extends GT_GUIContainerMetaTile_Machine {
    private final String mName;

    public GT_GUIContainer_Capacitor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_Capacitor(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/4by4.png");
        mName = aName;
    }

    public GT_GUIContainer_Capacitor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
        super(new GT_Container_Capacitor(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/" + aBackground + "4by4.png");
        this.mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
