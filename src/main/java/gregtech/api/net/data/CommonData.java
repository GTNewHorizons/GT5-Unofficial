package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.util.ForgeDirection;

public class CommonData extends PacketData<MultiTileEntityProcess> {

    public static final int COMMON_DATA_ID = 2;

    private ForgeDirection facing;

    public CommonData() {}

    public CommonData(ForgeDirection facing) {
        this.facing = facing;
    }
    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        facing = ForgeDirection.getOrientation(in.readInt());
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeInt(facing.ordinal());
    }

    @Override
    public int getId() {
        return COMMON_DATA_ID;
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        processData.giveFacing(facing);
    }

}
