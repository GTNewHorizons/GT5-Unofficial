package client.gui;

import org.lwjgl.opengl.GL11;

import common.container.Container_ItemProxyEndpoint;
import kekztech.KekzCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class Gui_ItemProxyEndpoint extends GuiContainer {
	
	private final ResourceLocation texture = new ResourceLocation(KekzCore.MODID, "textures/gui/ItemTechReceiverNode.png");
	
	private final InventoryPlayer inventory;
	private final IInventory te;
	
	public Gui_ItemProxyEndpoint(TileEntity te, EntityPlayer player) {
		super(new Container_ItemProxyEndpoint(te, player));
		inventory = player.inventory;
		this.te = (IInventory) te;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		final int x = (super.width - super.xSize) / 2;
		final int y = (super.height - super.ySize) / 2;
		
		super.drawTexturedModalRect(x, y, 0, 0, super.xSize, super.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
		super.fontRendererObj.drawString(
				I18n.format(te.getInventoryName()),
				(super.xSize / 2) - (fontRendererObj.getStringWidth(I18n.format(te.getInventoryName())) / 2),
				6, 4210752, false);
		super.fontRendererObj.drawString(
				I18n.format(inventory.getInventoryName()), 8, super.ySize - 96 + 2, 4210752);
	}
}
