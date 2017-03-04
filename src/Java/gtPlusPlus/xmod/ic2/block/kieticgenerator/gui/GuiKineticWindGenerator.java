package gtPlusPlus.xmod.ic2.block.kieticgenerator.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiKineticWindGenerator
extends GuiContainer
{
	public ContainerWindKineticGenerator container;
	public String name;

	public GuiKineticWindGenerator(final ContainerWindKineticGenerator container1)
	{
		super(container1);

		this.container = container1;
		this.name = StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.name");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2)
	{
		this.fontRendererObj.drawString(this.name, (this.xSize - this.fontRendererObj.getStringWidth(this.name)) / 2, 6, 4210752);
		if (this.container.base.checkrotor())
		{
			if (!this.container.base.rotorspace())
			{
				this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotorspace"), 20, 52, 2157374);
			}
			else if ((this.container.base.checkrotor()) && (!this.container.base.guiisminWindStrength()))
			{
				this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.windweak1"), 27, 52, 2157374);
				this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.windweak2"), 24, 69, 2157374);
			}
			else
			{
				this.fontRendererObj.drawString(StatCollector.translateToLocalFormatted("ic2.WindKineticGenerator.gui.output", new Object[] { Integer.valueOf(this.container.base.getKuOutput()) }), 55, 52, 2157374);
				this.fontRendererObj.drawString(this.container.base.getRotorhealth() + " %", 46, 70, 2157374);
				if (this.container.base.guiisoverload())
				{
					GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft, par2 - this.guiTop, StatCollector.translateToLocal("ic2.WindKineticGenerator.error.overload"), 44, 20, 79, 45);
					GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft, par2 - this.guiTop, StatCollector.translateToLocal("ic2.WindKineticGenerator.error.overload2"), 102, 20, 131, 45);
				}
			}
		}
		else {
			this.fontRendererObj.drawString(StatCollector.translateToLocal("ic2.WindKineticGenerator.gui.rotormiss"), 27, 52, 2157374);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int x, final int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		final int j = (this.width - this.xSize) / 2;
		final int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
		if ((this.container.base.guiisoverload()) && (this.container.base.checkrotor()))
		{
			this.drawTexturedModalRect(j + 44, k + 20, 176, 0, 30, 26);
			this.drawTexturedModalRect(j + 102, k + 20, 176, 0, 30, 26);
		}
	}

	private static final ResourceLocation background = new ResourceLocation(IC2.textureDomain, "textures/gui/GUIWindKineticGenerator.png");
}
