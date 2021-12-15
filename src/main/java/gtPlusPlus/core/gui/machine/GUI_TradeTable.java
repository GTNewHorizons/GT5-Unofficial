package gtPlusPlus.core.gui.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;

@SideOnly(Side.CLIENT)
public class GUI_TradeTable extends GuiContainer {

	TileEntityTradeTable mThisTable;
	String mOwnerName;

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/ProjectTable.png");

	public GUI_TradeTable(final InventoryPlayer player_inventory, final TileEntityTradeTable te, final String mOwnerName){
		super(new Container_TradeTable(player_inventory, te));
		if (te.isServerSide()){
			mThisTable = te;
			this.mOwnerName = mOwnerName;
			Logger.INFO("Set valid TE in GUI");	
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		this.fontRendererObj.drawString(I18n.format("Owner - "+this.mOwnerName, new Object[0]), 28, 66, 4210752);
		//this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

}