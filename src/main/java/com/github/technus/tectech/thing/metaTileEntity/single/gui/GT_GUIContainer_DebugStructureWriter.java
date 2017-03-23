package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_DebugStructureWriter extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_DebugStructureWriter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DebugStructureWriter(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Teleporter.png");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString("StructPrint", 46, 8, 16448255);
        if (this.mContainer != null) {
            GT_Container_DebugStructureWriter dsw= (GT_Container_DebugStructureWriter)mContainer;
            if(dsw.numbers==null)return;
            this.fontRendererObj.drawString(dsw.size?"Size":"Offset", 46, 16, 16448255);
            this.fontRendererObj.drawString("A: " + dsw.numbers[dsw.size?3:0], 46, 24, 16448255);
            this.fontRendererObj.drawString("B: " + dsw.numbers[dsw.size?4:1], 46, 32, 16448255);
            this.fontRendererObj.drawString("C: " + dsw.numbers[dsw.size?5:2], 46, 40, 16448255);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
