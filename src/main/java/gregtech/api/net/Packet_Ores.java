package gregtech.api.net;

import gregtech.common.blocks.TileEntityOres;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class Packet_Ores extends Packet_New {

    private int mX;
    private int mZ;
    private short mY;
    private short mMetaData;

    public Packet_Ores() {
        super(true);
    }

    public Packet_Ores(int aX, short aY, int aZ, short aMetaData) {
        super(false);
        this.mX = aX;
        this.mY = aY;
        this.mZ = aZ;
        this.mMetaData = aMetaData;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.mX);
        aOut.writeShort(this.mY);
        aOut.writeInt(this.mZ);
        aOut.writeShort(this.mMetaData);
    }

    @Override
    public Packet_New decode(ByteArrayDataInput aData) {
        return new Packet_Ores(aData.readInt(), aData.readShort(), aData.readInt(), aData.readShort());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld != null) {
            TileEntity tTileEntity = aWorld.getTileEntity(this.mX, this.mY, this.mZ);
            if ((tTileEntity instanceof TileEntityOres)) {
                ((TileEntityOres) tTileEntity).mMetaData = this.mMetaData;
            }
            if (((aWorld instanceof World)) && (((World) aWorld).isRemote)) {
                ((World) aWorld).markBlockForUpdate(this.mX, this.mY, this.mZ);
            }
        }
    }

    @Override
    public byte getPacketID() {
        return PacketTypes.ORES.id;
    }
}
