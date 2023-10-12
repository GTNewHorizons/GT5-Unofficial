package gregtech.api.util;

import java.util.HashMap;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import appeng.api.util.WorldCoord;

public class BlockUpdateHandler {

    public BlockUpdateHandler(int aMinUpdateCD, int aMaxUpdateCD) {

        this.minUpdateCD = aMinUpdateCD;
        this.maxUpdateCD = aMaxUpdateCD;

        alreadyTriggeredChunks = new HashMap<ChunkCoordIntPair, RandomCooldown>();
    }

    public boolean triggerBlockUpdate(World world, WorldCoord pos) {

        ChunkCoordIntPair chunkCoords = getBlockChunkCoords(world, pos);

        if (!alreadyTriggeredChunks.containsKey(chunkCoords)) {
            alreadyTriggeredChunks.put(chunkCoords, new RandomCooldown(minUpdateCD, maxUpdateCD));
        }

        RandomCooldown chunkCooldown = alreadyTriggeredChunks.get(chunkCoords);

        if (!chunkCooldown.hasPassed()) return false;

        world.markBlockForUpdate(pos.x, pos.y, pos.z);
        chunkCooldown.set();

        return true;
    }

    public int getLastTimeUpdated(World world, WorldCoord pos) {

        ChunkCoordIntPair chunkCoords = getBlockChunkCoords(world, pos);

        if (!alreadyTriggeredChunks.containsKey(chunkCoords)) return 0;

        return alreadyTriggeredChunks.get(chunkCoords)
            .getLastTimeStarted();
    }

    private ChunkCoordIntPair getBlockChunkCoords(World world, WorldCoord pos) {

        Chunk chunk = world.getChunkFromBlockCoords(pos.x, pos.z);
        return chunk.getChunkCoordIntPair();
    }

    private int minUpdateCD;
    private int maxUpdateCD;
    private HashMap<ChunkCoordIntPair, RandomCooldown> alreadyTriggeredChunks;

}
