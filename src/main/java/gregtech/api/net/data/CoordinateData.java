package gregtech.api.net.data;

import javax.annotation.Nonnull;

import net.minecraft.util.ChunkCoordinates;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class CoordinateData extends PacketData<MultiTileEntityProcess> {

    public final static int COORDINATE_DATA_ID = 0;

    private ChunkCoordinates coords;

    public CoordinateData(ChunkCoordinates coords) {
        this.coords = coords;
    }

    public CoordinateData(int x, int y, int z) {
        this(new ChunkCoordinates(x, y, z));
    }

    public CoordinateData() {}

    @Override
    public int getId() {
        return COORDINATE_DATA_ID;
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeInt(coords.posX);
        out.writeInt(coords.posY);
        out.writeInt(coords.posZ);
    }

    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        coords = new ChunkCoordinates(in.readInt(), in.readInt(), in.readInt());
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        if (coords == null) return;
        processData.giveCoordinates(coords);
    }

}
