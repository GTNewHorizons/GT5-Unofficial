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
import gregtech.api.net.ClientMetaTrackerRegistry;
import gregtech.api.net.IClientMetaTracker;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public class ClientMetaManager implements DataManager.PacketDataManager, DataManager.BlockPacketDataManager {

    @Override
    public int maxPacketSize() {
        return 1024 * 10; // I really don't know how big to make this tbh.
    }

    @Override
    public void writeToBuffer(Chunk chunk, int subChunkMask, boolean forceUpdate, ByteBuffer buffer) {
        for (IClientMetaTracker tracker : ClientMetaTrackerRegistry.getAllTrackers()) {
            World theWorld = chunk.worldObj;
            LongSet blockList = tracker.getTrackedBlocksByChunk(chunk);

            if (blockList == null) continue;
            buffer.putInt(blockList.size());
            for (long packedBlockPos : blockList) {
                buffer.putLong(packedBlockPos);
                int x = CoordinatePacker.unpackX(packedBlockPos);
                int y = CoordinatePacker.unpackY(packedBlockPos);
                int z = CoordinatePacker.unpackZ(packedBlockPos);
                Block current = theWorld.getBlock(x, y, z);
                if (current instanceof IBlockWithClientMeta clientMetaBlock) {
                    buffer.putShort((short) clientMetaBlock.getClientMeta(theWorld, x, y, z));
                }
            }
        }
    }

    @Override
    public void readFromBuffer(Chunk chunk, int subChunkMask, boolean forceUpdate, ByteBuffer buffer) {
        World theWorld = chunk.worldObj;

        if (buffer.remaining() < 4) return;
        int count = buffer.getInt();
        for (int i = 0; i < count; i++) {
            if (buffer.remaining() < 10) return; // 1 long + 1 short
            long packedBlockPos = buffer.getLong();
            int x = CoordinatePacker.unpackX(packedBlockPos);
            int y = CoordinatePacker.unpackY(packedBlockPos);
            int z = CoordinatePacker.unpackZ(packedBlockPos);
            short meta = buffer.getShort();
            Block current = theWorld.getBlock(x, y, z);
            if (current instanceof IBlockWithClientMeta) {
                theWorld.setBlockMetadataWithNotify(x, y, z, meta, 2);
            }
        }
    }

    @Override
    public void writeBlockToPacket(Chunk chunk, int x, int y, int z, S23PacketBlockChange packet) {
        World theWorld = chunk.worldObj;
        IClientMetaTracker tracker = ClientMetaTrackerRegistry.get(packet.field_148883_d);
        if (tracker != null) {
            Long2IntOpenHashMap map = tracker.getTrackedBlocks(theWorld);
            if (map != null
                && map.containsKey(CoordinatePacker.pack((chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z))) {
                if (packet.field_148883_d instanceof IBlockWithClientMeta clientMetaBlock) {
                    packet.field_148884_e = clientMetaBlock
                        .getClientMeta(theWorld, (chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z);
                }
            }
        }
    }

    @Override
    public void readBlockFromPacket(Chunk chunk, int x, int y, int z, S23PacketBlockChange packet) {
        World theWorld = chunk.worldObj;
        Block current = theWorld.getBlock(x, y, z);
        if (current instanceof IBlockWithClientMeta) {
            theWorld.setBlockMetadataWithNotify(x, y, z, packet.func_148881_g(), 2);
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
