package gtPlusPlus.core.gui.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import gtPlusPlus.core.container.Container_HeliumGenerator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityHeliumGenerator;

@SideOnly(Side.CLIENT)
public class GUI_HeliumGenerator extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation(CORE.MODID, "textures/gui/helium_collector_gui.png");

	public GUI_HeliumGenerator(final InventoryPlayer player_inventory, final TileEntityHeliumGenerator te){
		super(new Container_HeliumGenerator(player_inventory, te));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		//this.fontRendererObj.drawString(I18n.format("", new Object[0]), 28, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(guiTexture);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

}