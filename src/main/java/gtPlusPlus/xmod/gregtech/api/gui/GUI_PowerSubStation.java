package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;

public class GUI_PowerSubStation extends GT_GUIContainerMetaTile_Machine {
	public String mNEI;
	String mName;
	boolean[] mRepairStatus = new boolean[6];
	private static IIcon[] mGregToolIcons = new IIcon[6];

	public GUI_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aNEI) {
		super(new CONTAINER_PowerSubStation(aInventoryPlayer, aTileEntity, false), CORE.RES_PATH_GUI + "PowerSubStation.png");
		this.mName = aName;
		this.mNEI = aNEI;
		/** The X size of the inventory window in pixels. */
		this.xSize = 196;
		/** The Y size of the inventory window in pixels. */
		this.ySize = 191;		
		for (IIcon g : mGregToolIcons) {
			if (g == null) {
				getToolTextures();
			}
		}		
	}

	private static final boolean getToolTextures() {
		mGregToolIcons[0] = Textures.ItemIcons.WRENCH.getIcon();
		mGregToolIcons[1] = Textures.ItemIcons.HANDLE_SCREWDRIVER.getIcon();
		mGregToolIcons[2] = Textures.ItemIcons.MORTAR.getIcon();
		mGregToolIcons[3] = Textures.ItemIcons.JACKHAMMER.getIcon();
		mGregToolIcons[4] = Textures.ItemIcons.HANDLE_SOLDERING.getIcon();
		mGregToolIcons[5] = Textures.ItemIcons.CROWBAR.getIcon();		
		return true;
	}

	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(this.mName, 8, -10, 16448255);
		if (this.mContainer != null) {	

			this.fontRendererObj.drawString("Error Code: "+((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode, 10, 142, 16448255);

			this.fontRendererObj.drawString("In", 178, 10, 16448255);
			this.fontRendererObj.drawString("Out", 176, 28, 16448255);
			
			if (((this.mContainer).mDisplayErrorCode & 1) != 0) {
				this.fontRendererObj.drawString("Pipe is loose.", 10, 8, 16448255);
				mRepairStatus[0] = false;
			}
			else {
				mRepairStatus[0] = true;
			}			
			if ((((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode & 2) != 0) {
				this.fontRendererObj.drawString("Screws are missing.", 10, 16, 16448255);
				mRepairStatus[1] = false;
			}
			else {
				mRepairStatus[1] = true;
			}		
			if ((((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode & 4) != 0) {
				this.fontRendererObj.drawString("Something is stuck.", 10, 24, 16448255);
				mRepairStatus[2] = false;
			}
			else {
				mRepairStatus[2] = true;
			}			
			if ((((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode & 8) != 0) {
				this.fontRendererObj.drawString("Platings are dented.", 10, 32, 16448255);
				mRepairStatus[3] = false;
			}
			else {
				mRepairStatus[3] = true;
			}		
			if ((((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode & 16) != 0) {
				this.fontRendererObj.drawString("Circuitry burned out.", 10, 40, 16448255);
				mRepairStatus[4] = false;
			}
			else {
				mRepairStatus[4] = true;
			}			
			if ((((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode & 32) != 0) {
				this.fontRendererObj.drawString("That doesn't belong there.", 10, 48, 16448255);
				mRepairStatus[5] = false;
			}
			else {
				mRepairStatus[5] = true;
			}			
			if (((CONTAINER_PowerSubStation) this.mContainer).mDisplayErrorCode == 0) {
				if (((CONTAINER_PowerSubStation) this.mContainer).mActive == 0) {
					this.fontRendererObj.drawString(
							"Hit with Soft Hammer to (re-)start the Machine if it doesn't start.", -70, 8, 16448255);
				} else {
					this.fontRendererObj.drawString("Running perfectly.", 10, 8, 16448255);
				}
			}
			if (this.mContainer.mEnergy > 160000000 && this.mContainer.mEnergy < 160010000) {
				this.fontRendererObj.drawString("160,000,000 EU", 50, 155, 16711680);
			} else if (this.mContainer.mEnergy > 320000000 && this.mContainer.mEnergy < 320010000) {
				this.fontRendererObj.drawString("320,000,000 EU", 50, 155, 16711680);
			} else if (this.mContainer.mEnergy > 640000000 && this.mContainer.mEnergy < 640010000) {
				this.fontRendererObj.drawString("640,000,000 EU", 50, 155, 16711680);
			} else {
				this.fontRendererObj.drawString(GT_Utility.formatNumbers((long) this.mContainer.mEnergy) + " EU", 
						50,	155, 16711680);
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {		
		/** The X size of the inventory window in pixels. */
		if (this.xSize != 196)
			this.xSize = 196;
		/** The Y size of the inventory window in pixels. */
		if (this.ySize != 191)
			this.ySize = 191;		
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);		
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		if (this.mContainer != null) {
			final double tScale = this.mContainer.mEnergy / this.mContainer.mStorage;
			this.drawTexturedModalRect(x + 5, y + 156, 0, 251, Math.min(147, (int) (tScale * 148.0)), 5);


			//A1
			this.drawTexturedModalRect(x + 154, y + 76, 238, (!mRepairStatus[0] ? 0 : 18), 18, 18);
			//A2
			this.drawTexturedModalRect(x + 154 + 20, y + 76, 238, (!mRepairStatus[1] ? 0 : 18), 18, 18);
			//B1
			this.drawTexturedModalRect(x + 154, y + 76 + 18, 238, (!mRepairStatus[2] ? 0 : 18), 18, 18);
			//B2
			this.drawTexturedModalRect(x + 154 + 20, y + 76 + 18, 238, (!mRepairStatus[3] ? 0 : 18), 18, 18);
			//C1
			this.drawTexturedModalRect(x + 154, y + 76 + 36, 238, (!mRepairStatus[4] ? 0 : 18), 18, 18);
			//C2
			this.drawTexturedModalRect(x + 154 + 20, y + 76 + 36, 238, (!mRepairStatus[5] ? 0 : 18), 18, 18);


			if (mGregToolIcons[0] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154, y + 76, mGregToolIcons[0], 18, 18);
			}
			if (mGregToolIcons[1] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154 + 20, y + 76, mGregToolIcons[1], 18, 18);
			}
			if (mGregToolIcons[2] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154, y + 76 + 18, mGregToolIcons[2], 9, 9);
			}
			if (mGregToolIcons[3] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154 + 20, y + 76 + 18, mGregToolIcons[3], 9, 9);
			}
			if (mGregToolIcons[4] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154, y + 76 + 36, mGregToolIcons[4], 9, 9);
			}
			if (mGregToolIcons[5] != null) {
				//this.drawTexturedModelRectFromIcon(x + 154 + 20, y + 76 + 36, mGregToolIcons[5], 9, 9);
			}

			/*//Maint Done
			this.drawTexturedModalRect(x + 154, y + 76, 238, 0, 18, 18);
			//Maint Required
			this.drawTexturedModalRect(x + 154 + 20, y + 76, 238, 18, 18, 18);*/

		}
	}
}