package gtPlusPlus.core.gui.machine;

import gtPlusPlus.core.container.Container_WorkbenchAdvanced;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUI_WorkbenchAdvanced extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/AdvancedCraftingTable.png");

	public GUI_WorkbenchAdvanced(InventoryPlayer player_inventory, TileEntityWorkbenchAdvanced tile){
		super(new Container_WorkbenchAdvanced(player_inventory, tile));
	}


	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		//this.fontRendererObj.drawString(I18n.format("Workbench", new Object[0]), 28, 6, 4210752);
		//this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){       
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);       
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);       
		int x = (width - xSize) / 2;       
		int y = (height - ySize) / 2;       
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}