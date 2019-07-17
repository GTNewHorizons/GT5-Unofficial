package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.font.TecTechFontRender;
import com.github.technus.tectech.Util;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.Locale;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_ParamAdv extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_ParamAdv(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_ParamAdv(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "ParametrizerAdv.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (mContainer != null) {
            TecTechFontRender.INSTANCE.drawSplitString("Parameters X: " + ((GT_Container_ParamAdv) mContainer).param, 46, 7, 167, 0xffffff);
            Locale locale = Locale.getDefault();
            TecTechFontRender.INSTANCE.drawSplitString("\u24EA\u2b07" + String.format(locale, "%+.5E", (((GT_Container_ParamAdv) mContainer).input0f)), 46, 16, 167, 0x22ddff);
            TecTechFontRender.INSTANCE.drawSplitString("\u2460\u2b07" + String.format(locale, "%+.5E", (((GT_Container_ParamAdv) mContainer).input1f)), 46, 24, 167, 0x00ffff);
            TecTechFontRender.INSTANCE.drawSplitString("\u24EA\u2b06" + String.format(locale, "%+.5E", (((GT_Container_ParamAdv) mContainer).value0f)), 46, 33, 167, 0x00bbff);
            TecTechFontRender.INSTANCE.drawSplitString("\u2460\u2b06" + String.format(locale, "%+.5E", (((GT_Container_ParamAdv) mContainer).value1f)), 46, 41, 167, 0x0077ff);
            GL11.glPushMatrix();
            GL11.glScalef(.5f,.5f,.5f);
            TecTechFontRender.INSTANCE.drawSplitString("\u24EA\u2b06" + Util.longBitsToShortString(Double.doubleToLongBits(((GT_Container_ParamAdv) mContainer).value0f)), 92, 100, 334, 0x00bbff);
            TecTechFontRender.INSTANCE.drawSplitString("\u2460\u2b06" + Util.longBitsToShortString(Double.doubleToLongBits(((GT_Container_ParamAdv) mContainer).value1f)), 92, 116, 334, 0x0077ff);
            GL11.glPopMatrix();
            TecTechFontRender.INSTANCE.drawSplitString("Pointer " + Integer.toHexString(((GT_Container_ParamAdv) mContainer).pointer | 0x10000).substring(1), 46, 66, 167, 0x0033ff);
        } else {
            TecTechFontRender.INSTANCE.drawSplitString("Parameters X", 46, 7, 167, 0xffffff);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
