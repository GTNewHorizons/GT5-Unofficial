package com.github.bartimaeusnek.bartworks.client.gui;

import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BW_GUIContainer_RotorBlock extends GuiWindKineticGenerator
{
    public ContainerWindKineticGenerator container;
    public String name;

    public BW_GUIContainer_RotorBlock(ContainerWindKineticGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = StatCollector.translateToLocal("tile.BWRotorBlock.name");
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRendererObj.drawString(this.name, (this.xSize - this.fontRendererObj.getStringWidth(this.name)) / 2, 6, 2157374);
        if (this.container.base.checkrotor()) {
            if (!this.container.base.rotorspace()) {
                this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotorspace"), 27, 52, 2157374);
            } else if (this.container.base.checkrotor() && !this.container.base.guiisminWindStrength()) {
                this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.windweak1"), 27, 52, 2157374);
            } else {
                this.fontRendererObj.drawString(this.container.base.getRotorhealth() + " %", 46, 52, 2157374);
                if (this.container.base.guiisoverload()) {
                    GuiTooltipHelper.drawAreaTooltip(p_146979_1_ - this.guiLeft, p_146979_2_ - this.guiTop, StatCollector.translateToLocal("ic2.WindKineticGenerator.error.overload"), 44, 27, 79, 52);
                }
            }
        } else {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotormiss"), 27, 52, 2157374);
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor3f(0.5f,0.25f,0.07f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(MainMod.modID, "textures/GUI/GUIPrimitiveKUBox.png"));
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.container.base.guiisoverload() && this.container.base.checkrotor()) {
            this.drawTexturedModalRect(j + 44, k + 20, 176, 0, 30, 26);
            this.drawTexturedModalRect(j + 102, k + 20, 176, 0, 30, 26);
        }
    }
}
