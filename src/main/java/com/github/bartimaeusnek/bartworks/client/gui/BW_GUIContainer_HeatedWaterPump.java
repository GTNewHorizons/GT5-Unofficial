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
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_HeatedWaterPump;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BW_GUIContainer_HeatedWaterPump extends GuiContainer {
    private static final ResourceLocation furnaceGuiTextures =
            new ResourceLocation(MainMod.MOD_ID + ":textures/GUI/GUIheatedPump.png");
    final BW_Container_HeatedWaterPump container;

    public BW_GUIContainer_HeatedWaterPump(Container p_i1072_1_) {
        super(p_i1072_1_);
        this.container = (BW_Container_HeatedWaterPump) p_i1072_1_;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BW_GUIContainer_HeatedWaterPump.furnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        //      this.drawString(this.fontRendererObj, "Fuel:" + container.fuel + " Water:" + container.water, k, l,
        // 0xffffff);

        if (this.container.fuel > 0) {
            this.mc.getTextureManager().bindTexture(BW_GUIContainer_HeatedWaterPump.furnaceGuiTextures);
            int ik = Math.max(this.container.maxfuel, 200);
            int i1 = ((this.container.fuel * 13) / ik);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 13 - i1, 14, i1 + 1);
        }

        //        if (this.container.water > 0){
        //            this.mc.getTextureManager().bindTexture( new ResourceLocation("textures/blocks/water_still.png"));
        //            this.drawTexturedModalRect(k+85,l+32,32,32,18,18);
        //        }

    }
}
