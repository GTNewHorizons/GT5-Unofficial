package gtPlusPlus.core.gui.machine;

import gtPlusPlus.core.container.Container_FishTrap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUI_FishTrap extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/FishTrap.png");

	public GUI_FishTrap(InventoryPlayer player_inventory, TileEntityFishTrap te){
		super(new Container_FishTrap(player_inventory, te));
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


	//This method is called when the Gui is first called!
	@Override
	public void initGui()
	{
		//You have to add this line for the Gui to function properly!
		super.initGui();

		//The parameters of GuiButton are(id, x, y, width, height, text);
		//this.buttonList.add(new GuiButton( 1, 367, 132, 18, 18, "X"));
		//this.buttonList.add(new GuiButton( 2, 385, 132, 18, 18, "Y"));
		//NOTE: the id always has to be different or else it might get called twice or never!

		//Add any other buttons here too!
	}

}