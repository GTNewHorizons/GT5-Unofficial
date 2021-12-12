package gtPlusPlus.xmod.gregtech.api.gui;


import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_Hatch_Muffler_Advanced extends GT_GUIContainerMetaTile_Machine {

	String mName = "";
	long maxPower = 0;
	long storedPower = 0;

	public GUI_Hatch_Muffler_Advanced(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aTextureFile) {
		super(new CONTAINER_Hatch_Muffler_Advanced(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		this.mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(this.mName, 8, 8, 16448255);
		/*
		 * ReflectionUtils.setField(this.fontRendererObj, "underlineStyle", true);
		 * ReflectionUtils.setField(this.fontRendererObj, "italicStyle", true);
		 * ReflectionUtils.setField(this.fontRendererObj, "boldStyle", true); boolean
		 * isBold = ReflectionUtils.getField(this.fontRendererObj, "boldStyle");
		 * this.fontRendererObj.drawString("Insert Air Filters - Bold: "+isBold, 8, 18,
		 * 16448255);
		 */
		if (this.mContainer != null) {
			//this.maxPower = ((CONTAINER_TreeFarmer)this.mContainer).maxEU;
			//this.storedPower = ((CONTAINER_TreeFarmer)this.mContainer).storedEU;			
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		//this.maxPower = ((CONTAINER_TreeFarmer)this.mContainer).maxEU;
		//this.storedPower = ((CONTAINER_TreeFarmer)this.mContainer).storedEU;
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
