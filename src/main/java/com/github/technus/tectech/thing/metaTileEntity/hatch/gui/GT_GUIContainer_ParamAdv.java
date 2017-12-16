package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static com.github.technus.tectech.TecTech.proxy;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_ParamAdv extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_ParamAdv(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_ParamAdv(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "ParametrizerAdv.png");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (this.mContainer != null) {
            proxy.renderUnicodeString("Parametrizer X: " + ((GT_Container_ParamAdv) this.mContainer).param, 46, 7, 167, 0xffffff);
            if (((GT_Container_ParamAdv) this.mContainer).extra >= 0x10000) {
                proxy.renderUnicodeString("\u24EA\u2b07|I" + String.format(java.util.Locale.ENGLISH, "%d", Float.floatToIntBits(((GT_Container_ParamAdv) this.mContainer).input0f)), 46, 16, 167, 0x22ddff);
                proxy.renderUnicodeString("\u2460\u2b07|I" + String.format(java.util.Locale.ENGLISH, "%d", Float.floatToIntBits(((GT_Container_ParamAdv) this.mContainer).input1f)), 46, 24, 167, 0x00ffff);
                proxy.renderUnicodeString("\u24EA\u2b06|I" + String.format(java.util.Locale.ENGLISH, "%d", Float.floatToIntBits(((GT_Container_ParamAdv) this.mContainer).input0f)), 46, 33, 167, 0x00bbff);
                proxy.renderUnicodeString("\u2460\u2b06|I" + String.format(java.util.Locale.ENGLISH, "%d", Float.floatToIntBits(((GT_Container_ParamAdv) this.mContainer).input1f)), 46, 41, 167, 0x0077ff);
            } else {
                proxy.renderUnicodeString("\u24EA\u2b07|F" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_ParamAdv) this.mContainer).input0f), 46, 16, 167, 0x22ddff);
                proxy.renderUnicodeString("\u2460\u2b07|F" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_ParamAdv) this.mContainer).input1f), 46, 24, 167, 0x00ffff);
                proxy.renderUnicodeString("\u24EA\u2b06|F" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_ParamAdv) this.mContainer).value0f), 46, 33, 167, 0x00bbff);
                proxy.renderUnicodeString("\u2460\u2b06|F" + String.format(java.util.Locale.ENGLISH, "%+.5E", ((GT_Container_ParamAdv) this.mContainer).value1f), 46, 41, 167, 0x0077ff);
            }
            proxy.renderUnicodeString("\u24EA\u2b06|B" + ((GT_Container_ParamAdv) this.mContainer).data0, 46, 50, 167, 0x00bbff);
            proxy.renderUnicodeString("\u2460\u2b06|B" + ((GT_Container_ParamAdv) this.mContainer).data1, 46, 58, 167, 0x0077ff);
            proxy.renderUnicodeString("Pointer: 0x" + Integer.toHexString(((((GT_Container_ParamAdv) this.mContainer).extra & 0xFFFF)) | 0x10000).substring(1), 46, 66, 167, 0x0033ff);
        } else {
            proxy.renderUnicodeString("Parametrizer X", 46, 7, 167, 0xffffff);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
