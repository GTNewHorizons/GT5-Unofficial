package gtPlusPlus.core.gui.machine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.container.Container_RoundRobinator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GUI_RoundRobinator extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/RoundRobinator.png");
	private TileEntityRoundRobinator mTile;
	private Container_RoundRobinator mContainer;
	private static final Method mDrawItemStack;
	
	static {
		mDrawItemStack = ReflectionUtils.getMethod(GuiContainer.class, "drawItemStack", new Class[] {ItemStack.class, int.class, int.class, String.class});
	}

	public GUI_RoundRobinator(final InventoryPlayer player_inventory, final TileEntityRoundRobinator te){
		this(new Container_RoundRobinator(player_inventory, te));
		mTile = te;
	}
	
	private GUI_RoundRobinator(final Container_RoundRobinator aContainer){
		super(aContainer);
		mContainer = aContainer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j){
		super.drawGuiContainerForegroundLayer(i, j);		
		
		int xStart = 134;
		int yStart = 31;
		mTile = this.mContainer.tile_entity;
		
		int tier = mContainer.mTier;	
		int aTickRate = mContainer.mTickRate;	
		
		fontRendererObj.drawString("Round Robinator", 85, 4, Utils.rgbtoHexValue(50, 150, 50));
		fontRendererObj.drawString("Tier: "+tier, 85, 12, Utils.rgbtoHexValue(50, 150, 50));
		fontRendererObj.drawString("Rate: 1 Item/"+aTickRate+"t", 85, 20, Utils.rgbtoHexValue(50, 150, 50));
		
		boolean[] aStates = new boolean[] {mContainer.mSide_1 == 0 ? false : true, mContainer.mSide_2 == 0 ? false : true, mContainer.mSide_3 == 0 ? false : true,mContainer.mSide_4 == 0 ? false : true};
		
		fontRendererObj.drawString("West: "+(aStates[0] ? "Active" : "Disabled"), 5, 5, Utils.rgbtoHexValue(50, 50, 50));
		fontRendererObj.drawString("North: "+(aStates[1] ? "Active" : "Disabled"), 5, 15, Utils.rgbtoHexValue(50, 50, 50));
		fontRendererObj.drawString("South: "+(aStates[2] ? "Active" : "Disabled"), 5, 25, Utils.rgbtoHexValue(50, 50, 50));
		fontRendererObj.drawString("East: "+(aStates[3] ? "Active" : "Disabled"), 5, 35, Utils.rgbtoHexValue(50, 50, 50));
		fontRendererObj.drawString("Toggling South will visually", 5, 65, Utils.rgbtoHexValue(150, 50, 50));
		fontRendererObj.drawString("toggle East, This is a visual bug.", 5, 74, Utils.rgbtoHexValue(150, 50, 50));
		drawStatus(aStates[0], xStart, yStart);
		drawStatus(aStates[1], xStart+18, yStart);
		drawStatus(aStates[2], xStart, yStart+18);
		drawStatus(aStates[3], xStart+18, yStart+18);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j){
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		
		
	}

	private static ItemStack aGreenGlass;
	private static ItemStack aRedGlass;
	
	private void drawStatus(boolean aStateActive, int x, int y) {
		if (aGreenGlass == null) {
			Item pane = ItemUtils.getSimpleStack(Blocks.stained_glass_pane).getItem();
			aGreenGlass = ItemUtils.simpleMetaStack(pane, 5, 1);
		}
		if (aRedGlass == null) {
			Item pane = ItemUtils.getSimpleStack(Blocks.stained_glass_pane).getItem();
			aRedGlass = ItemUtils.simpleMetaStack(pane, 14, 1);
		}
		if (mDrawItemStack != null) {
			try {
				if (aStateActive) {
					mDrawItemStack.invoke(this, new Object[]{aGreenGlass, x, y, ""});
				}
				else {
					mDrawItemStack.invoke(this, new Object[]{aRedGlass, x, y, ""});
				}
			}
			catch (Throwable t) {}
		}
	}

	//This method is called when the Gui is first called!
	@Override
	public void initGui(){
		super.initGui();
	}

}