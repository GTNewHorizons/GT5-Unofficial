package gregtech.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import io.netty.buffer.ByteBuf;

public class PacketOres extends GTPacket {

    private int mX;
    private int mZ;
    private short mY;
    private short mMetaData;

    public PacketOres() {
        super();
    }

    public PacketOres(int aX, short aY, int aZ, short aMetaData) {
        super();
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
    public GTPacket decode(ByteArrayDataInput aData) {
        return new PacketOres(aData.readInt(), aData.readShort(), aData.readInt(), aData.readShort());
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
        return GTPacketTypes.ORES.id;
    }
}
