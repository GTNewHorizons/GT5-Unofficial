package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChunkCoordinates;

public class CasingData extends PacketData<MultiTileEntityProcess> {

    public static final int CASING_DATA_ID = 4;

    private int currentMode;
    private int allowedModes;
    private ChunkCoordinates controllerCoords;
    
    public CasingData() {}

    public CasingData(int currentMode, int allowedModes, ChunkCoordinates controllerCoords) {
        this.currentMode = currentMode;
        this.allowedModes = allowedModes;
        this.controllerCoords = controllerCoords;
    }
    
    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        currentMode = in.readInt();
        allowedModes = in.readInt();
        controllerCoords = new ChunkCoordinates(in.readInt(), in.readInt(), in.readInt());
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeInt(currentMode);
        out.writeInt(allowedModes);
        out.writeInt(controllerCoords.posX);
        out.writeInt(controllerCoords.posY);
        out.writeInt(controllerCoords.posZ);
    }

    @Override
    public int getId() {
        return CASING_DATA_ID;
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        
    }
    
}
