/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.util.GuiTooltipHelper;

@SideOnly(Side.CLIENT)
public class GuiContainerRotorBlock extends GuiWindKineticGenerator {

    public ContainerWindKineticGenerator container;
    public String name;

    public GuiContainerRotorBlock(ContainerWindKineticGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = StatCollector.translateToLocal("tile.BWRotorBlock.0.name");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj
            .drawString(this.name, (this.xSize - this.fontRendererObj.getStringWidth(this.name)) / 2, 6, 2157374);
        if (this.container.base.checkrotor()) {
            if (!this.container.base.rotorspace()) {
                this.fontRendererObj.drawString(
                    StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotorspace"),
                    27,
                    52,
                    2157374);
            } else if (this.container.base.checkrotor() && !this.container.base.guiisminWindStrength()) {
                this.fontRendererObj.drawString(
                    StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.windweak1"),
                    27,
                    52,
                    2157374);
            } else {
                this.fontRendererObj.drawString(this.container.base.getRotorhealth() + " %", 46, 52, 2157374);
                if (this.container.base.guiisoverload()) {
                    GuiTooltipHelper.drawAreaTooltip(
                        mouseX - this.guiLeft,
                        mouseY - this.guiTop,
                        StatCollector.translateToLocal("ic2.WindKineticGenerator.error.overload"),
                        44,
                        27,
                        79,
                        52);
                }
            }
        } else {
            this.fontRendererObj
                .drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotormiss"), 27, 52, 2157374);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor3f(0.5f, 0.25f, 0.07f);
        this.mc.getTextureManager()
            .bindTexture(new ResourceLocation(MainMod.MOD_ID, "textures/GUI/GUIPrimitiveKUBox.png"));
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.container.base.guiisoverload() && this.container.base.checkrotor()) {
            this.drawTexturedModalRect(j + 44, k + 20, 176, 0, 30, 26);
            this.drawTexturedModalRect(j + 102, k + 20, 176, 0, 30, 26);
        }
    }
}
