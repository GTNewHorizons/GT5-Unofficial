package gtPlusPlus.xmod.gregtech.api.gui;


import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 * <p/>
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GUI_MatterFab extends GT_GUIContainerMetaTile_Machine {

	String mName = "";
	public int mMatterProduced = 0;
	public int mScrapProduced = 0;
	public int mAmplifierProduced = 0;
	public int mScrapUsed = 0;
	public int mAmplifierUsed = 0;

	public GUI_MatterFab(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aTextureFile) {
		super(new CONTAINER_MatterFab(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		this.mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(this.mName, 10, 8, 16448255);



		if (this.mContainer != null) {
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 1) != 0) {
				this.fontRendererObj.drawString("Pipe is loose.", 10, 16, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 2) != 0) {
				this.fontRendererObj.drawString("Screws are missing.", 10, 24, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 4) != 0) {
				this.fontRendererObj.drawString("Something is stuck.", 10, 32, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 8) != 0) {
				this.fontRendererObj.drawString("Platings are dented.", 10, 40, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 16) != 0) {
				this.fontRendererObj.drawString("Circuitry burned out.", 10, 48, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 32) != 0) {
				this.fontRendererObj.drawString("That doesn't belong there.", 10, 56, 16448255);
			}
			if ((((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode & 64) != 0) {
				this.fontRendererObj.drawString("Incomplete Structure.", 10, 64, 16448255);
			}

			if (((CONTAINER_MatterFab) this.mContainer).mDisplayErrorCode == 0) {
				if (((CONTAINER_MatterFab) this.mContainer).mActive == 0) {
					this.fontRendererObj.drawString("Hit with Soft Hammer", 10, 16, 16448255);
					this.fontRendererObj.drawString("to (re-)start the Machine", 10, 24, 16448255);
					this.fontRendererObj.drawString("if it doesn't start.", 10, 32, 16448255);
				} else {
					CONTAINER_MatterFab aContainer = (CONTAINER_MatterFab) this.mContainer;
					
					this.mMatterProduced = aContainer.mMatterProduced;
					this.mAmplifierProduced = aContainer.mAmplifierProduced;
					this.mAmplifierUsed = aContainer.mAmplifierUsed;
					this.mScrapProduced = aContainer.mScrapProduced;
					this.mScrapUsed = aContainer.mScrapUsed;	
					
					this.fontRendererObj.drawString("Running perfectly.", 10, 16, 16448255);
					
					this.fontRendererObj.drawString("Scrap Produced: "+this.mScrapProduced, 10, 32, 16448255);
					this.fontRendererObj.drawString("Scrap Used: "+this.mScrapUsed, 10, 40, 16448255);
					this.fontRendererObj.drawString("UU-Amplifier Produced: "+this.mAmplifierProduced, 10, 48, 16448255);
					this.fontRendererObj.drawString("UU-Amplifier Used: "+this.mAmplifierUsed, 10, 56, 16448255);
					this.fontRendererObj.drawString("UU-Matter Fabricated: "+this.mMatterProduced, 10, 64, 16448255);
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
