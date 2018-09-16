package gtPlusPlus.xmod.ob;

import net.minecraft.inventory.IInventory;
import openblocks.common.tileentity.TileEntitySprinkler;
import openmods.api.IInventoryCallback;

public class CallbackObject implements IInventoryCallback {
	
	private TileEntitySprinkler mTile;
	
	CallbackObject(TileEntitySprinkler aTile){
		mTile = aTile;		
	}

	@Override
	public void onInventoryChanged(IInventory inventory, int slotNumber) {
		mTile.updateEntity();
	}

}
