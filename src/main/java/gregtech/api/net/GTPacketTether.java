package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.client.TetherLineRenderer;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import io.netty.buffer.ByteBuf;

public class GTPacketTether extends GTPacket {

    private int x;
    private int y;
    private int z;

    public GTPacketTether() {}

    public GTPacketTether(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GTPacketTether(MTEMagLevPylon pylon) {
        IGregTechTileEntity mte = pylon.getBaseMetaTileEntity();
        this.x = mte.getXCoord();
        this.y = mte.getYCoord();
        this.z = mte.getZCoord();
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TETHER.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        return new GTPacketTether(x, y, z);
    }

    @Override
    public void process(IBlockAccess world) {
        if (world != null) {
            TetherLineRenderer.addTetherLineRenderer(x, y, z);
        }
    }
}
