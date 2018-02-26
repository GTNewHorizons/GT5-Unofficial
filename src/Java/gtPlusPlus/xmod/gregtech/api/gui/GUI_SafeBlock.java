package gtPlusPlus.xmod.gregtech.api.gui;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.lib.CORE;

public class GUI_SafeBlock
extends GT_GUIContainerMetaTile_Machine {
	public GUI_SafeBlock(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(new CONTAINER_SafeBlock(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "SafeBlock.png");
	}

	//String UUID = ((CONTAINER_SafeBlock)this.mContainer).ownerUUID.toString();
	boolean blockStatus = ((CONTAINER_SafeBlock)this.mContainer).blockStatus;
	//String tempPlayer;

	private void updateVars(){
		//UUID = ((CONTAINER_SafeBlock)this.mContainer).ownerUUID;
		this.blockStatus = ((CONTAINER_SafeBlock)this.mContainer).blockStatus;
		// tempPlayer = PlayerCache.lookupPlayerByUUID(UUID);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2)
	{
		this.updateVars();
		//this.fontRendererObj.drawString("Owner: "+ tempPlayer, 64, 72, 4210752);
		//this.fontRendererObj.drawString(": "+ UUID.toLowerCase(), 44, 82, 4210752);
		this.fontRendererObj.drawString("Safe Status", 76, 61, 4210752);
		if (this.blockStatus){
			this.fontRendererObj.drawString("Locked", 88, 73, 4210752);
		}
		else {
			this.fontRendererObj.drawString("Unlocked", 82, 73, 4210752);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		/*String UUID = ((CONTAINER_SafeBlock)this.mContainer).UUID;
        this.fontRendererObj.drawString("Owner UUID: "+ UUID, 8, 12, 4210752);*/

	}
}
