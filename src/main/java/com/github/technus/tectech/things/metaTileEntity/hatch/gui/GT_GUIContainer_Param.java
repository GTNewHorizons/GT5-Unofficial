package com.github.technus.tectech.things.metaTileEntity.hatch.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static com.github.technus.tectech.TecTech.proxy;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public final class GT_GUIContainer_Param extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_Param(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_Param(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Parametrizer.png");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (this.mContainer != null) {
            proxy.renderUnicodeString("Parametrizer: " + ((GT_Container_Param) this.mContainer).param, 46, 7, 167, 0xffffff);
            proxy.renderUnicodeString("\u2460\u2b07" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_Param) this.mContainer).input1f), 46, 16, 167, 0x22ddff);
            proxy.renderUnicodeString("\u2461\u2b07" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_Param) this.mContainer).input2f), 46, 24, 167, 0x00ffff);
            proxy.renderUnicodeString("\u2460\u2b06" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_Param) this.mContainer).value1f), 46, 33, 167, 0x00bbff);
            proxy.renderUnicodeString("\u2461\u2b06" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_Param) this.mContainer).value2f), 46, 41, 167, 0x0077ff);
            proxy.renderUnicodeString("M\u2460: " + ((GT_Container_Param) this.mContainer).value1, 46, 50, 167, 0x00bbff);
            proxy.renderUnicodeString("M\u2461: " + ((GT_Container_Param) this.mContainer).value2, 46, 58, 167, 0x0077ff);
            proxy.renderUnicodeString("E\u2460\u2461: " + ((GT_Container_Param) this.mContainer).exponent, 46, 66, 167, 0x0033ff);
        } else {
            proxy.renderUnicodeString("Parametrizer", 46, 7, 167, 0xffffff);
        }
        //167
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
