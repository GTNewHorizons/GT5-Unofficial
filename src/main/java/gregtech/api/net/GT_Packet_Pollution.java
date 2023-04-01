package gregtech.api.net;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.GT_Client;
import io.netty.buffer.ByteBuf;

public class GT_Packet_Pollution extends GT_Packet_New {

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
    public void encode(ByteBuf aOut) {
        aOut.writeInt(chunk.chunkXPos)
            .writeInt(chunk.chunkZPos)
            .writeInt(pollution);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_Pollution(new ChunkCoordIntPair(aData.readInt(), aData.readInt()), aData.readInt());
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
