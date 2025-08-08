package gregtech.api.net;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import io.netty.buffer.ByteBuf;

public class GTPacketPollution extends GTPacket {

    private ChunkCoordIntPair chunk;
    private int pollution;

    public GTPacketPollution() {
        super();
    }

    public GTPacketPollution(ChunkCoordIntPair chunk, int pollution) {
        super();
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
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketPollution(new ChunkCoordIntPair(aData.readInt(), aData.readInt()), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        GTMod.clientProxy()
            .processChunkPollutionPacket(chunk, pollution);
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.POLLUTION.id;
    }
}
