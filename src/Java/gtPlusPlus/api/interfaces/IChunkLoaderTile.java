package gtPlusPlus.api.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface IChunkLoaderTile extends IInventory, IChunkLoader{

	 long getTicksRemaining();	 
	
}
