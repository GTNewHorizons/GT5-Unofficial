package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import org.lwjgl.opengl.GL11;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUI_FrameHousing extends GuiContainer {
	private final ResourceLocation			texture	= new ResourceLocation(CORE.MODID,
			"textures/gui/machine_Charger.png");

	private final InventoryPlayer			inventory;
	private final TileAlvearyFrameHousing	te;

	public GUI_FrameHousing(final TileAlvearyFrameHousing te, final EntityPlayer player) {
		super(new CONTAINER_FrameHousing(te, player));
		this.inventory = player.inventory;
		this.te = te;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(I18n.format("Alveary Frame Housing"),
				this.xSize / 2 - this.fontRendererObj.getStringWidth(I18n.format("Alveary Frame Housing")) / 2, 6,
				4210752, false);
		this.fontRendererObj.drawString(I18n.format(this.inventory.getInventoryName()), 8, this.ySize - 96 + 2,
				4210752);
		// fontRendererObj.drawString(I18n.format("Charge:"+te.getCharge()+"~"),
		// 8, ySize - 96 + 2, 4210752);
		// fontRendererObj.drawString(I18n.format("Progress:"+te.getProgress()+"ticks"),
		// 80, ySize - 96 + 2, 4210752);
	}
}