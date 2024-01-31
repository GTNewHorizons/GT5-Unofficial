package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class CommonData extends PacketData<MultiTileEntityProcess>{

    public static final int COMMON_DATA_ID = 2;

    private byte redstone;
    private byte color;
    private byte commonData;
    
    public CommonData() {}

    public CommonData(byte redstone, byte color, byte commonData) {
        this.redstone = redstone;
        this.color = color;
        this.commonData = commonData;
    }
    
    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        redstone = in.readByte();
        color = in.readByte();
        commonData = in.readByte();    
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeByte(redstone);
        out.writeByte(color);
        out.writeByte(commonData);        
    }

    @Override
    public int getId() {
        return COMMON_DATA_ID;
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        // TODO Auto-generated method stub
        
    }
    
}
