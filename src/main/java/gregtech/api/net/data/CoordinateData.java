package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class CoordinateData extends PacketData<MultiTileEntityProcess> {

    public final static int COORDINATE_DATA_ID = 0;

    private int x, y, z;

    public CoordinateData(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordinateData() {}

    @Override
    public int getId() {
        return COORDINATE_DATA_ID;
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
    }

    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        processData.giveCoordinates(x, y, z);
    }

}
