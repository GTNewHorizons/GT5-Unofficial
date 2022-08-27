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
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_RadioHatch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BW_GUIContainer_RadLevel extends GuiContainer {
    final BW_Container_RadioHatch container;

    public BW_GUIContainer_RadLevel(Container p_i1072_1_) {
        super(p_i1072_1_);
        this.container = (BW_Container_RadioHatch) p_i1072_1_;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor3f(1, 1, 1);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(MainMod.MOD_ID, "textures/GUI/GUI_RSC.png"));
        this.drawTexturedModalRect(this.guiLeft - 79, this.guiTop, 0, 0, 256, 165);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        int y = 51 + (this.container.coverage / 2 - 3);
        int x = -63;
        if (this.container.coverage != 100 && this.container.coverage != 0)
            Gui.drawRect(x, y, x + 51, y + 1, 0xFF60D82E);
        Gui.drawRect(x, 48, x + 51, y, 0xFF8B8B8B);
        this.fontRendererObj.drawString(Byte.toString(this.container.coverage), 88 - 79, 50, 16448255);
    }
}
