package gtPlusPlus.api.interfaces;

import net.minecraft.inventory.IInventory;

public interface IChunkLoader extends IInventory, net.minecraft.world.chunk.storage.IChunkLoader{

	 long getTicksRemaining();	 
	
}
