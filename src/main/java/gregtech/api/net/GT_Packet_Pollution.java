package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.common.GT_Client;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;

import java.nio.ByteBuffer;

public class GT_Packet_Pollution extends GT_Packet {
    private ChunkCoordIntPair chunk;
    private int pollution;

    public GT_Packet_Pollution() {
        super(true);
    }

    public GT_Packet_Pollution(ChunkCoordIntPair chunk, int pollution) {
        super(false);
        this.chunk = chunk;
        this.pollution = pollution;
    }

    @Override
    public byte[] encode() {
        return ByteBuffer
                .allocate(12)
                .putInt(chunk.chunkXPos)
                .putInt(chunk.chunkZPos)
                .putInt(pollution)
                .array();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new GT_Packet_Pollution(
                new ChunkCoordIntPair(aData.readInt(), aData.readInt()),
                aData.readInt()
        );
    }

    @Override
    public void process(IBlockAccess aWorld) {
        GT_Client.recieveChunkPollutionPacket(chunk, pollution);
    }

    @Override
    public byte getPacketID() {
        return 4;
    }
}