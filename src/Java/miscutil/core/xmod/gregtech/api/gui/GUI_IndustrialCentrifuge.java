package miscutil.core.xmod.gregtech.api.gui;


import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 * <p/>
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GUI_IndustrialCentrifuge extends GT_GUIContainerMetaTile_Machine {

	String mName = "";
	private Object tempStack;
	private List itemSlots;
	private short counter = 0;

	public GUI_IndustrialCentrifuge(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
		super(new CONTAINER_IndustrialCentrifuge(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(mName, 10, 8, 16448255);

		if (mContainer != null) {
			try {
				counter++;
				if (tempStack == null || counter > 200){
					if (itemSlots == null){
						itemSlots = ((CONTAINER_IndustrialCentrifuge) mContainer).inventorySlots;
					}
					if (itemSlots != null){
						if (itemSlots.get(0) == null){
							fontRendererObj.drawString("No Item in item slot.", 10, 48, 16448255);
						}
						else {
							tempStack = itemSlots.get(0);
							counter = 0;
						}
					} 


				}        		 
				if (tempStack != null){
					fontRendererObj.drawString("Item in item slot.", 10, 48, 16448255);
				}

			}
			catch (Throwable e){
				Utils.LOG_INFO("NULL");
			}

			fontRendererObj.drawString("Debug Counter: "+counter, 10, 56, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 1) != 0)
				fontRendererObj.drawString("Pipe is loose.", 10, 16, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 2) != 0)
				fontRendererObj.drawString("Screws are missing.", 10, 24, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 4) != 0)
				fontRendererObj.drawString("Something is stuck.", 10, 32, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 8) != 0)
				fontRendererObj.drawString("Platings are dented.", 10, 40, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 32) != 0)
				fontRendererObj.drawString("That doesn't belong there.", 10, 56, 16448255);
			if ((((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode & 64) != 0)
				fontRendererObj.drawString("Incomplete Structure.", 10, 64, 16448255);

			if (((CONTAINER_IndustrialCentrifuge) mContainer).mDisplayErrorCode == 0) {
				if (((CONTAINER_IndustrialCentrifuge) mContainer).mActive == 0) {
					fontRendererObj.drawString("Hit with Soft Hammer", 10, 16, 16448255);
					fontRendererObj.drawString("to (re-)start the Machine", 10, 24, 16448255);
					fontRendererObj.drawString("if it doesn't start.", 10, 32, 16448255);
				} else {
					fontRendererObj.drawString("Running perfectly.", 10, 16, 16448255);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
