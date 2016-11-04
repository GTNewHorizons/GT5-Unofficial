package gtPlusPlus.core.gui.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.container.Container_Workbench;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GUI_Workbench extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/BronzeCraftingTable.png");
	
	public boolean moveItemsToChest = false;
	public boolean moveItemsToCrafting = false;

	public GUI_Workbench(InventoryPlayer player_inventory, TileEntityWorkbench tile){
		super(new Container_Workbench(player_inventory, tile));
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

	@Override
	protected void actionPerformed(GuiButton B)
	{
		//If the button id is different, or you have mrs buttons, create another if block for that too!
		if(B.id == 1){
			System.out.println("Trying to empty crafting grid to the storage compartment.");
			//moveItemsToChest = true;
			((Container_Workbench) this.inventorySlots).moveCraftingToChest();
		}
		else if(B.id == 2){
			System.out.println("Trying to move items into the crafting grid.");
			//moveItemsToCrafting = true;
			((Container_Workbench) this.inventorySlots).moveChestToCrafting();
		}


	}

}