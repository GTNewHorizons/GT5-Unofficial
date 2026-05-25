package gregtech.api.net;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public interface IClientMetaTracker {

    Long2IntOpenHashMap getTrackedBlocks(World world);

    LongSet getTrackedBlocksByChunk(Chunk chunk);

}
