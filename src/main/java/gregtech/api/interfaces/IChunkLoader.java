package gregtech.api.interfaces;

import net.minecraft.world.ChunkCoordIntPair;

// This interface is implemented by the machines that actively load a working chunk
public interface IChunkLoader {
    // return a working chunk coordinates, may be null
    ChunkCoordIntPair getActiveChunk();
}
