package gtPlusPlus.xmod.gregtech.api.gui;


import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_TreeFarmer extends GT_GUIContainerMetaTile_Machine {

	String mName = "";
	long maxPower = 0;
	long storedPower = 0;

	public GUI_TreeFarmer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
		super(new CONTAINER_TreeFarmer(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(mName, 64, 6, 16448255);
		if (mContainer != null) {			
			maxPower = ((CONTAINER_TreeFarmer)this.mContainer).maxEU;
			storedPower = ((CONTAINER_TreeFarmer)this.mContainer).storedEU;
			if (((CONTAINER_TreeFarmer) mContainer).mDisplayErrorCode == 0) {
				fontRendererObj.drawString("Current Power Stored: "+storedPower+"EU", 10, 52, 16448255);
				fontRendererObj.drawString("Max Power Storage: "+maxPower+"EU", 10, 60, 16448255);
				fontRendererObj.drawString("Current operations left: "+(storedPower/32), 10, 68, 16448255);                
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		maxPower = ((CONTAINER_TreeFarmer)this.mContainer).maxEU;
		storedPower = ((CONTAINER_TreeFarmer)this.mContainer).storedEU;
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
