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
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BW_GUIContainer_Windmill extends GT_GUIContainer_MultiMachine {

    public BW_GUIContainer_Windmill(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(aInventoryPlayer, aTileEntity, aName, null);
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (!(this.mContainer instanceof GT_Container_MultiMachine))
            return;

        if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 64) != 0)
            this.fontRendererObj.drawString(this.trans("138", "Incomplete Structure."), 92, 22, 16448255);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor3f(0.5f, 0.25f, 0.07f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(MainMod.modID, "textures/GUI/GUI_Windmill.png"));

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer.mMaxProgressTime > 0) {
            this.drawTexturedModalRect(x + 152, y + 5, 176, 0, 16, 15);
            this.drawTexturedModalRect(x + 53, y + 63, 176, 16, 13, 17);
        }

        if (((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode == 0) {
            if (((GT_Container_MultiMachine) this.mContainer).mActive == 0) {
                GL11.glColor3f(1f, 1f, 1f);
                this.drawTexturedModalRect(x + 66, y + 66, 176, 33, 15, 15);
            }
        }


    }
}
