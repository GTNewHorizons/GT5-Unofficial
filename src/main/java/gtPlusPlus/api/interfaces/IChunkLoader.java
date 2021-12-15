package gtPlusPlus.api.interfaces;

import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;

public interface IChunkLoader {

	public long getTicksRemaining();
	
	public void setTicksRemaining(long aTicks);	

	public ChunkCoordIntPair getResidingChunk();
	
	public void setResidingChunk(ChunkCoordIntPair aCurrentChunk);

	public boolean getChunkLoadingActive();
	
	public void setChunkLoadingActive(boolean aActive);

	public boolean getDoesWorkChunkNeedReload();
	
	public void setDoesWorkChunkNeedReload(boolean aActive);

	public boolean addChunkToLoadedList(ChunkCoordIntPair aActiveChunk);
	
	public boolean removeChunkFromLoadedList(ChunkCoordIntPair aActiveChunk);
	
	public Set<ChunkCoordIntPair> getManagedChunks();
	
	public int getChunkloaderTier();
	
}
