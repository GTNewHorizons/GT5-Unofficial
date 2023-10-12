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
        cooldown = new RandomCooldown(minUpdateCD, maxUpdateCD);
    }

    public void triggerBlockUpdate(World world, WorldCoord pos) {

        // int currTime = getServerTimeInTicks();

        // if (currTime > lastTimeUpdated) {
        // alreadyTriggeredChunks.clear();
        // lastTimeUpdated = currTime;
        // }

        // Chunk chunk = world.getChunkFromBlockCoords(pos.x, pos.z);
        // ChunkCoordIntPair chunkCoords = chunk.getChunkCoordIntPair();

        // if (alreadyTriggeredChunks.contains(chunkCoords)) return;

        // alreadyTriggeredChunks.add(chunkCoords);
        // world.markBlockForUpdate(pos.x, pos.y, pos.z);

        Chunk chunk = world.getChunkFromBlockCoords(pos.x, pos.z);
        ChunkCoordIntPair chunkCoords = chunk.getChunkCoordIntPair();

        if (!alreadyTriggeredChunks.containsKey(chunkCoords)) {

            RandomCooldown newCooldown = new RandomCooldown(minUpdateCD, maxUpdateCD);
            newCooldown.set();

            alreadyTriggeredChunks.put(chunkCoords, newCooldown);
            world.markBlockForUpdate(pos.x, pos.y, pos.z);

            // GT_Log.out.println(
            // String.format(
            // "updating chunk [x = %d, z = %d] on time = %d",
            // chunkCoords.chunkXPos,
            // chunkCoords.chunkZPos,
            // getServerTimeInTicks()));

            return;
        }

        RandomCooldown chunkCooldown = alreadyTriggeredChunks.get(chunkCoords);

        if (!chunkCooldown.hasPassed()) return;

        world.markBlockForUpdate(pos.x, pos.y, pos.z);
        chunkCooldown.set();

        // GT_Log.out.println(
        // String.format(
        // "updating chunk [x = %d, z = %d] on time = %d",
        // chunkCoords.chunkXPos,
        // chunkCoords.chunkZPos,
        // getServerTimeInTicks()));
    }

    // private int getServerTimeInTicks() {
    // return MinecraftServer.getServer()
    // .getTickCounter();
    // }

    int minUpdateCD;
    int maxUpdateCD;
    RandomCooldown cooldown;
    HashMap<ChunkCoordIntPair, RandomCooldown> alreadyTriggeredChunks;
    int lastTimeUpdated = 0;
}
