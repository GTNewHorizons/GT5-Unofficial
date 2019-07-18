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

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_RadioHatch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

@SideOnly(Side.CLIENT)
public class GT_GUIContainer_RadioHatch extends GT_GUIContainerMetaTile_Machine {
    private static int maxSv;
    private final String mName;

    public GT_GUIContainer_RadioHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_RadioHatch(aInventoryPlayer, aTileEntity), MainMod.MOD_ID + ":textures/GUI/RadHatch.png");
        this.mName = "Radio Hatch";
        this.mContainer.detectAndSendChanges();
        GT_GUIContainer_RadioHatch.maxSv = BioVatLogicAdder.RadioHatch.getMaxSv();
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        long timer = ByteBuffer.wrap(((GT_Container_RadioHatch) this.mContainer).teTimer).getLong();
        double maxT = ((GT_Container_RadioHatch) this.mContainer).mass * (GT_MetaTileEntity_RadioHatch.calcDecayTicks(((GT_Container_RadioHatch) this.mContainer).sievert));
        double rem = maxT - timer % maxT;

        this.fontRendererObj.drawString(this.mName, 8, 4, 4210752);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(MainMod.MOD_ID + ":textures/GUI/RadHatch.png"));
        if (((GT_Container_RadioHatch) this.mContainer).mass > 0) {
            GL11.glColor3f(((GT_Container_RadioHatch) this.mContainer).r / 255f, ((GT_Container_RadioHatch) this.mContainer).g / 255f, ((GT_Container_RadioHatch) this.mContainer).b / 255f);
            this.drawTexturedModalRect(124, 18, 124, 18, 16, 48);
        }
        GL11.glColor3f(1f, 1f, 1f);
        this.drawTexturedModalRect(124, 18, 176, 0, 16, 48 - (int) Math.ceil(48 * (rem / maxT)));
        this.drawTexturedModalRect(65, 13, 192, 0, (48 * (((GT_Container_RadioHatch) this.mContainer).sv)) / (GT_GUIContainer_RadioHatch.maxSv), 16);


//        this.fontRendererObj.drawString("Sv: " + ((GT_Container_RadioHatch) mContainer).sievert, 8, 50, BW_ColorUtil.getColorFromRGBArray(new short[]{((GT_Container_RadioHatch) mContainer).r, ((GT_Container_RadioHatch) mContainer).g, ((GT_Container_RadioHatch) mContainer).b}));
//        this.fontRendererObj.drawString("Kg: " + ((GT_Container_RadioHatch) mContainer).mass, 8, 68, BW_ColorUtil.getColorFromRGBArray(new short[]{((GT_Container_RadioHatch) mContainer).r, ((GT_Container_RadioHatch) mContainer).g, ((GT_Container_RadioHatch) mContainer).b}));
//        this.fontRendererObj.drawString("Time: " + timer, 8, 76, BW_ColorUtil.getColorFromRGBArray(new short[]{((GT_Container_RadioHatch) mContainer).r, ((GT_Container_RadioHatch) mContainer).g, ((GT_Container_RadioHatch) mContainer).b}));

    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

}
