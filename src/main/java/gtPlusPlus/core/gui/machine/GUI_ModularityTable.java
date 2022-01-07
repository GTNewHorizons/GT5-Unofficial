package gtPlusPlus.core.gui.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;

@SideOnly(Side.CLIENT)
public class GUI_ModularityTable extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/ModularityTable.png");
	private Container_ModularityTable mThisContainer;
	private TileEntityModularityTable mThisTile;
	private int mRecipeTime = -1;
	
	public GUI_ModularityTable(final InventoryPlayer player_inventory, final TileEntityModularityTable tile){
		super(new Container_ModularityTable(player_inventory, tile));
		this.mThisContainer = tile.getContainer();
		this.mThisTile = tile;
		if (this.mThisTile.getRecipeTime() > -1){
			this.mRecipeTime = this.mThisTile.getRecipeTime();
		}
	}


	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		if (this.mThisTile.getRecipeTime() > -1){
			this.mRecipeTime = this.mThisTile.getRecipeTime();
		}
		//Utils.LOG_INFO("Container: "+this.mRecipeTime);
		this.fontRendererObj.drawString(I18n.format("Modularity Table", new Object[0]), 8, 6, 4210752);
		if (mRecipeTime > -1){
			this.fontRendererObj.drawString(I18n.format("Time", new Object[0]), 84, 42, 4210752);	
			this.fontRendererObj.drawString(I18n.format("Remaining", new Object[0]), 84, 50, 4210752);	
			this.fontRendererObj.drawString(I18n.format(""+this.mRecipeTime+" Ticks", new Object[0]), 84, 58, 4210752);			
		}
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

	}


	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}


	//This method is called when the Gui is first called!
	@Override
	public void initGui(){
		//You have to add this line for the Gui to function properly!
		super.initGui();

		//The parameters of GuiButton are(id, x, y, width, height, text);
		//this.buttonList.add(new GuiButton( 1, 367, 132, 18, 18, "X"));
		//this.buttonList.add(new GuiButton( 2, 385, 132, 18, 18, "Y"));
		//NOTE: the id always has to be different or else it might get called twice or never!

		//Add any other buttons here too!
	}

	@Override
	protected void actionPerformed(final GuiButton B){


	}

}