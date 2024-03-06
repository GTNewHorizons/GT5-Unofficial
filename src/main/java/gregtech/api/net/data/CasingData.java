package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.multitileentity.enums.PartMode;
import io.netty.buffer.ByteBuf;

public class CasingData extends PacketData<MultiTileEntityProcess> {

    public static final int CASING_DATA_ID = 4;

    private int currentMode;
    private int allowedModes;
    private int controllerX;
    private int controllerY;
    private int controllerZ;

    public CasingData() {}

    public CasingData(int currentMode, int allowedModes, int controllerX, int controllerY, int controllerZ) {
        this.currentMode = currentMode;
        this.allowedModes = allowedModes;
        this.controllerX = controllerX;
        this.controllerY = controllerY;
        this.controllerZ = controllerZ;
    }

    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {
        currentMode = in.readInt();
        allowedModes = in.readInt();
        controllerX = in.readInt();
        controllerY = in.readInt();
        controllerZ = in.readInt();
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {
        out.writeInt(currentMode);
        out.writeInt(allowedModes);
        out.writeInt(controllerX);
        out.writeInt(controllerY);
        out.writeInt(controllerZ);
    }

    @Override
    public int getId() {
        return CASING_DATA_ID;
    }

    @Override
    public void process(MultiTileEntityProcess processData) {
        processData.giveCasingData(allowedModes, currentMode, controllerX, controllerY, controllerZ);
    }

}
