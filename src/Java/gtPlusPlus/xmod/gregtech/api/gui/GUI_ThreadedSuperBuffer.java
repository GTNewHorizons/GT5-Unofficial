package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;

public class GUI_ThreadedSuperBuffer extends GT_GUIContainerMetaTile_Machine {
	
	int cacheTime = 0;
	
	public GUI_ThreadedSuperBuffer(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(new CONTAINER_ThreadedSuperBuffer(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/SuperBuffer.png");
	}
	
	private void updateVars(){
		this.cacheTime = ((CONTAINER_ThreadedSuperBuffer)this.mContainer).cacheTime;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2){
		this.updateVars();
		this.fontRendererObj.drawString("Time Remaining: "+cacheTime, 76, 61, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}