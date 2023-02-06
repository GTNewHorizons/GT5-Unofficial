package gregtech.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GT_Packet_New;
import io.netty.buffer.ByteBuf;

public class GT_Packet_Ores extends GT_Packet_New {

    private int mX;
    private int mZ;
    private short mY;
    private short mMetaData;

    public GT_Packet_Ores() {
        super(true);
    }

    public GT_Packet_Ores(int aX, short aY, int aZ, short aMetaData) {
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
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_Ores(aData.readInt(), aData.readShort(), aData.readInt(), aData.readShort());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld != null) {
            TileEntity tTileEntity = aWorld.getTileEntity(this.mX, this.mY, this.mZ);
            if ((tTileEntity instanceof GT_TileEntity_Ores)) {
                ((GT_TileEntity_Ores) tTileEntity).mMetaData = this.mMetaData;
            }
            if (((aWorld instanceof World)) && (((World) aWorld).isRemote)) {
                ((World) aWorld).markBlockForUpdate(this.mX, this.mY, this.mZ);
            }
        }
    }

    @Override
    public byte getPacketID() {
        return 3;
    }
}
