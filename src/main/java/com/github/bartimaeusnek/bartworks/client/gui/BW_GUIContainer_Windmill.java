/*
 * Copyright (c) 2018-2020 bartimaeusnek
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
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_Windmill;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class BW_GUIContainer_Windmill extends GT_GUIContainerMetaTile_Machine {

    public BW_GUIContainer_Windmill(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new BW_Container_Windmill(aInventoryPlayer, aTileEntity), "");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (!(this.mContainer instanceof GT_Container_MultiMachine)) return;

        if ((this.mContainer.mDisplayErrorCode & 64) != 0)
            this.fontRendererObj.drawString(this.trans("138", "Incomplete Structure."), 92, 22, 16448255);
    }

    public String trans(String aKey, String aEnglish) {
        return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
    }

    private static final int DIVIDER = 125;
    private byte last = 0;

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        this.mc.getTextureManager().bindTexture(new ResourceLocation(MainMod.MOD_ID, "textures/GUI/GUI_Windmill.png"));

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer.mMaxProgressTime > 0) {
            if (System.currentTimeMillis() / DIVIDER % 40 == 30)
                this.drawTexturedModalRect(x + 85, y + 27, 176, (last = 96), 32, 32);
            else if (System.currentTimeMillis() / DIVIDER % 40 == 20)
                this.drawTexturedModalRect(x + 85, y + 27, 176, (last = 64), 32, 32);
            else if (System.currentTimeMillis() / DIVIDER % 40 == 10)
                this.drawTexturedModalRect(x + 85, y + 27, 176, (last = 32), 32, 32);
            else if (System.currentTimeMillis() / DIVIDER % 40 == 0)
                this.drawTexturedModalRect(x + 85, y + 27, 176, (last = 0), 32, 32);
            else this.drawTexturedModalRect(x + 85, y + 27, 176, last, 32, 32);
        }

        // Soft Mallet
        if (this.mContainer.mDisplayErrorCode == 0) {
            if (this.mContainer.mActive == 0) {
                this.drawTexturedModalRect(x + 66, y + 66, 176, 128, 15, 15);
            }
        }
    }
}
