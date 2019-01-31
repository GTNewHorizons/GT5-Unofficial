/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.gui;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_LESU;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_LESU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_GUIContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GT_GUIContainer_LESU extends GT_GUIContainer {

    public static final ResourceLocation texture = new ResourceLocation(MainMod.MOD_ID, "textures/GT2/gui/LESU.png");
    protected GT_Container_LESU mContainer;
    private GT_TileEntity_LESU c;

    public GT_GUIContainer_LESU(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_LESU(aInventoryPlayer, aTileEntity), texture.getResourceDomain());
        mContainer = ((GT_Container_LESU) this.inventorySlots);
        c = ((GT_TileEntity_LESU) (this.mContainer.mTileEntity.getMetaTileEntity()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        this.drawString(this.fontRendererObj, "L.E.S.U.", 11, 8, 16448255);
        if (this.mContainer != null) {
            String percell = String.valueOf(ConfigHandler.energyPerCell).substring(1);
            this.drawString(this.fontRendererObj, "EU: " + String.valueOf(this.mContainer.mEnergy), 11, 16, 16448255);
            this.drawString(this.fontRendererObj, "MAX: " + (this.c.getBaseMetaTileEntity().isActive() ? String.valueOf(this.mContainer.mOutput) + percell : Integer.toString(0)), 11, 24, 16448255);
            this.drawString(this.fontRendererObj, "MAX EU/t IN: " + String.valueOf(this.mContainer.mInput), 11, 32, 16448255);
            this.drawString(this.fontRendererObj, "EU/t OUT: " + String.valueOf(this.mContainer.mOutput), 11, 40, 16448255);
            this.drawString(this.fontRendererObj, "AMP/t IN/OUT: " + String.valueOf(c.getBaseMetaTileEntity().getInputAmperage()), 11, 48, 16448255);
            if (c.maxEUStore() >= Long.MAX_VALUE - 1) {
                this.drawString(this.fontRendererObj, "Maximum Capacity!", 11, 56, Color.YELLOW.getRGB());
            }
            if (!this.c.getBaseMetaTileEntity().isActive()) {
                this.drawString(this.fontRendererObj, "Multiple Controllers!", 11, 56, Color.RED.getRGB());
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null) {
            final long tScale = this.mContainer.mEnergy / Math.max(1, c.maxEUStore() / 116);
            this.drawTexturedModalRect(x + 8, y + 73, 0, 251, (int) tScale, 5);
        }
    }
}
