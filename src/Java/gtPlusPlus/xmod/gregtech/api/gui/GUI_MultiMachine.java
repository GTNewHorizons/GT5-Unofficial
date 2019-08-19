package gtPlusPlus.xmod.gregtech.api.gui;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 * <p/>
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GUI_MultiMachine extends GT_GUIContainerMetaTile_Machine {

	String mName = "";

	public GUI_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aTextureFile) {
		super(new CONTAINER_MultiMachine_NoPlayerInventory(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		this.mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {		
		fontRendererObj.drawString(this.mName+"!", 6, 7, 16448255);
		if (this.mContainer != null) {

			boolean aWrench = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 1) != 0;
			boolean aScrewdriver = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 2) != 0;
			boolean aMallet = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 4) != 0;
			boolean aHammer = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 8) != 0;
			boolean aSoldering = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 16) != 0;
			boolean aCrowbar = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 32) != 0;			
			
			

			//net.minecraft.client.gui.inventory.GuiContainer.drawItemStack(ItemStack, int, int, String)
			Method aDrawItemStack = ReflectionUtils.getMethod(GuiContainer.class, "drawItemStack", new Class[] {ItemStack.class, int.class, int.class, String.class});
			if (aDrawItemStack != null) {

				try {
					
					if ((((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 64) != 0) {
						this.fontRendererObj.drawString("Incomplete Structure.", 10, 16, 16448255); // Move down 8px
					}
					else {
						Field aStringData = ReflectionUtils.getField(this.mContainer.getClass(), "oTileDescription");
						if (aStringData != null) {
							String[] aData = (String[]) aStringData.get(this.mContainer);
							int aIndex = 0;
							this.fontRendererObj.drawString(""+aStringData.getType().getSimpleName(), 6, 16+(aIndex++*8), 16448255); // Move down 8px	
							if (aData != null && aData.length > 0) {
								for (String s : aData) {
									this.fontRendererObj.drawString(s, 6, 16+(aIndex++*8), 16448255); // Move down 8px						
								}
							}
							else {
								this.fontRendererObj.drawString("Unable to get Info Data from Tile Entity | "+(aData != null), 6, 16+(aIndex++*8), 16448255); // Move down 8px							
							}
						}
						else {	
							this.fontRendererObj.drawString("Unable to get Info Data from Tile Entity | Bad", 6, 16, 16448255); // Move down 8px						
						
							
						}
						
					}	
					
					
					
					
					
					
					
					//Migrate to static block

					Materials GOOD = Materials.Uranium;
					Materials BAD = Materials.Plutonium;
					
					ItemStack aWrenchStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, (aWrench ? BAD : GOOD), Materials.Tungsten, null);
					ItemStack aCrowbarStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, (aCrowbar ? BAD : GOOD), Materials.Tungsten, null);
					ItemStack aHammerStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, (aHammer ? BAD : GOOD), Materials.Tungsten, null);
					ItemStack aMalletStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOFTHAMMER, 1, (aMallet ? BAD : GOOD), Materials.Tungsten, null);
					ItemStack aScrewdriverStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, (aScrewdriver ? BAD : GOOD), Materials.Tungsten, null);
					ItemStack aSolderingStack = GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, (aSoldering ? BAD : GOOD), Materials.Tungsten, null);
					
					ItemStack[] aToolStacks2 = new ItemStack[] {
							aWrenchStack,
							aCrowbarStack,
							aHammerStack,
							aMalletStack,
							aScrewdriverStack,
							aSolderingStack
					};
					
					int aIndex = 0;
					for (aIndex = 0; aIndex < 6; aIndex++) {
						
						int x = 156;
						int y = 112 - (18*3) + (aIndex * 18);		
						aDrawItemStack.invoke(this, new Object[] {
								aToolStacks2[aIndex] != null ? aToolStacks2[aIndex] : ItemUtils.getErrorStack(1, "Bad Times"),
										x,
										y,
								""+(aIndex == 2 ? "H" : aIndex == 3 ? "M" : "") // Stacksize Overlay
						});			
						this.fontRendererObj.drawString("", 10, 64, 16448255);		
					}
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}








			//ItemStack aWrenchStack = ItemUtils.getSimpleStack(GregTech_API.sWrenchList., i)



			if (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode == 0) {
				if (((CONTAINER_MultiMachine) this.mContainer).mActive == 0) {
					this.fontRendererObj.drawString("Hit with Soft Hammer", 10, 16, 16448255);
					this.fontRendererObj.drawString("to (re-)start the Machine", 10, 24, 16448255);
					this.fontRendererObj.drawString("if it doesn't start.", 10, 32, 16448255);
				} else {
					this.fontRendererObj.drawString("Running perfectly.", 10, 16, 16448255);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
