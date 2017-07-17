package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.enums.GT_Values.VN;

public class GT_GUIContainer_DebugPowerGenerator extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_DebugPowerGenerator(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DebugPowerGenerator(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Teleporter.png");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString("PowerThing", 46, 8, 16448255);
        if (this.mContainer != null) {
            GT_Container_DebugPowerGenerator dpg = (GT_Container_DebugPowerGenerator) mContainer;
            this.fontRendererObj.drawString("EUT: " + dpg.EUT, 46, 24, 16448255);
            this.fontRendererObj.drawString("TIER: " + VN[GT_Utility.getTier(dpg.EUT<0?-dpg.EUT:dpg.EUT)], 46, 32, 16448255);
            this.fontRendererObj.drawString("AMP: " + dpg.AMP, 46, 40, 16448255);
            this.fontRendererObj.drawString("SUM: " + (long)dpg.AMP*dpg.EUT, 46, 48, 16448255);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
