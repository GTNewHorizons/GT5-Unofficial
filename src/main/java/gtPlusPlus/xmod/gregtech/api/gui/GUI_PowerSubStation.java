package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_PowerSubStation extends GT_GUIContainerMetaTile_Machine {
	public String mNEI;
	String mName;
	boolean[] mRepairStatus = new boolean[6];

	public final CONTAINER_PowerSubStation mPowerContainer;

	public GUI_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aNEI) {
		super(new CONTAINER_PowerSubStation(aInventoryPlayer, aTileEntity, false), CORE.RES_PATH_GUI + "PowerSubStation.png");
		this.mName = aName;
		this.mNEI = aNEI;
		mPowerContainer = (CONTAINER_PowerSubStation) mContainer;
		/** The X size of the inventory window in pixels. */
		this.xSize = 196;
		/** The Y size of the inventory window in pixels. */
		this.ySize = 191;		
	}

	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(this.mName, 8, -10, 16448255);
		if (this.mPowerContainer != null) {

			//this.fontRendererObj.drawString("Error Code: " + mPowerContainer.mDisplayErrorCode, 10, 142, 16448255);


			if (((this.mContainer).mDisplayErrorCode & 1) != 0) {
				mRepairStatus[0] = false;
			}
			else {
				mRepairStatus[0] = true;
			}
			if ((mPowerContainer.mDisplayErrorCode & 2) != 0) {
				mRepairStatus[1] = false;
			}
			else {
				mRepairStatus[1] = true;
			}
			if ((mPowerContainer.mDisplayErrorCode & 4) != 0) {
				mRepairStatus[2] = false;
			}
			else {
				mRepairStatus[2] = true;
			}
			if ((mPowerContainer.mDisplayErrorCode & 8) != 0) {
				mRepairStatus[3] = false;
			}
			else {
				mRepairStatus[3] = true;
			}
			if ((mPowerContainer.mDisplayErrorCode & 16) != 0) {
				mRepairStatus[4] = false;
			}
			else {
				mRepairStatus[4] = true;
			}
			if ((mPowerContainer.mDisplayErrorCode & 32) != 0) {
				mRepairStatus[5] = false;
			}
			else {
				mRepairStatus[5] = true;
			}
			if (mPowerContainer.mDisplayErrorCode == 0) {
				if (mPowerContainer.mActive == 0) {
					this.fontRendererObj.drawString("Turn on with Mallet", 10, 8, 16448255);
				}
				else {
					this.fontRendererObj.drawString("Running perfectly", 10, 8, 16448255);
				}
			}			

			this.fontRendererObj.drawString("In", 178, 10, 16448255);
			this.fontRendererObj.drawString("Out", 176, 28, 16448255);
			this.fontRendererObj.drawString("Avg In: "+GT_Utility.formatNumbers(this.mPowerContainer.mAverageEuAdded.getValue())+" EU", 10, 20, 16448255);
			this.fontRendererObj.drawString("Avg Out: "+GT_Utility.formatNumbers(this.mPowerContainer.mAverageEuConsumed.getValue())+" EU", 10, 30, 16448255);

			final double tScale2 = MathUtils.findPercentage(this.mPowerContainer.mStoredEU.getValue(), this.mPowerContainer.mMaxStoredEU.getValue());
			final int tScale = (int) (tScale2 * 2.55);
			this.fontRendererObj.drawString("Stored:", 10, 132, 16448255);
			this.fontRendererObj.drawString(GT_Utility.formatNumbers(this.mPowerContainer.mStoredEU.getValue()) + " EU", 10, 142, Utils.rgbtoHexValue((255 - tScale), (0 + tScale), 0));
			this.fontRendererObj.drawString(GT_Utility.formatNumbers(tScale2)+"%", 70, 155, 16448255);

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
		if (this.mPowerContainer != null) {
			final double tScale = MathUtils.findPercentage(this.mPowerContainer.mStoredEU.getValue(), this.mPowerContainer.mMaxStoredEU.getValue()) / 100;
			this.drawTexturedModalRect(x + 5, y + 156, 0, 251, Math.min(147, (int) (tScale * 148.0)), 5);

			// A1
			this.drawTexturedModalRect(x + 154, y + 76, 238, (!mRepairStatus[0] ? 0 : 18), 18, 18);
			// A2
			this.drawTexturedModalRect(x + 154 + 20, y + 76, 238, (!mRepairStatus[1] ? 0 : 18), 18, 18);
			// B1
			this.drawTexturedModalRect(x + 154, y + 76 + 18, 238, (!mRepairStatus[2] ? 0 : 18), 18, 18);
			// B2
			this.drawTexturedModalRect(x + 154 + 20, y + 76 + 18, 238, (!mRepairStatus[3] ? 0 : 18), 18, 18);
			// C1
			this.drawTexturedModalRect(x + 154, y + 76 + 36, 238, (!mRepairStatus[4] ? 0 : 18), 18, 18);
			// C2
			this.drawTexturedModalRect(x + 154 + 20, y + 76 + 36, 238, (!mRepairStatus[5] ? 0 : 18), 18, 18);


		}
	}
}
