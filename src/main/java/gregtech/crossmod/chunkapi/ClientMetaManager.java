package gregtech.crossmod.chunkapi;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.falsepattern.chunk.api.DataManager;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.interfaces.IBlockWithClientMeta;
import gregtech.common.data.GTCoilTracker;

public class ClientMetaManager implements DataManager.PacketDataManager, DataManager.BlockPacketDataManager {

    @Override
    public int maxPacketSize() {
        return 1;
    }

    @Override
    public void writeToBuffer(Chunk chunk, int subChunkMask, boolean forceUpdate, ByteBuffer buffer) {

    }

    @Override
    public void readFromBuffer(Chunk chunk, int subChunkMask, boolean forceUpdate, ByteBuffer buffer) {
        World theWorld = chunk.worldObj;
        GTCoilTracker coilTracker = GTCoilTracker.getTracker(theWorld);
        if (coilTracker == null) return;

        long chunkPos = CoordinatePacker.pack(chunk.xPosition, 0, chunk.zPosition);
        if (coilTracker.activeBlocksByChunk.containsKey(chunkPos)) {
            for (long block : coilTracker.activeBlocksByChunk.get(chunkPos)) {
                int x = CoordinatePacker.unpackX(block);
                int y = CoordinatePacker.unpackY(block);
                int z = CoordinatePacker.unpackZ(block);
                Block current = theWorld.getBlock(x, y, z);
                if (current instanceof IBlockWithClientMeta clientMetaBlock) {
                    theWorld.setBlock(x, y, z, current, clientMetaBlock.getClientMeta(theWorld, x, y, z), 3);
                }
            }
        }
    }

    @Override
    public void writeBlockToPacket(Chunk chunk, int x, int y, int z, S23PacketBlockChange packet) {

    }

    @Override
    public void readBlockFromPacket(Chunk chunk, int x, int y, int z, S23PacketBlockChange packet) {
        World theWorld = chunk.worldObj;
        Block current = theWorld.getBlock(x, y, z);
        if (current instanceof IBlockWithClientMeta clientMetaBlock) {
            theWorld.setBlock(x, y, z, current, clientMetaBlock.getClientMeta(theWorld, x, y, z), 3);
        }
    }

    @Override
    public void writeBlockPacketToBuffer(S23PacketBlockChange packet, PacketBuffer buffer) throws IOException {

    }

    @Override
    public void readBlockPacketFromBuffer(S23PacketBlockChange packet, PacketBuffer buffer) throws IOException {

    }

    @Override
    public String domain() {
        return "gregtech";
    }

    @Override
    public String id() {
        return "clientmeta";
    }
}
