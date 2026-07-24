package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.common.blocks.FrameShapeBlock;
import io.netty.buffer.ByteBuf;

/// Server -> Client : Full state of a frame-box tile entity.
///
/// [FrameShapeBlock] never creates tile entities itself, so after a chunk load the client has no tile
/// entity for the vanilla description packet to land on and the packet is dropped. The frame tile entity
/// therefore describes itself with this packet instead; the client handler creates the tile entity when
/// it is missing and applies the same initial data the description packet would have carried.
public class GTPacketFrameEntity extends GTPacket {

    private int mX;
    private short mY;
    private int mZ;
    private byte[] mData;

    public GTPacketFrameEntity() {
        super();
    }

    public GTPacketFrameEntity(BaseMetaPipeEntity tile) {
        super();
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();
        this.mData = tile.getInitialDataForClient();
    }

    private GTPacketFrameEntity(int x, short y, int z, byte[] data) {
        super();
        this.mX = x;
        this.mY = y;
        this.mZ = z;
        this.mData = data;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.FRAME_ENTITY.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        aOut.writeShort(mData.length);
        aOut.writeBytes(mData);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        int x = aData.readInt();
        short y = aData.readShort();
        int z = aData.readInt();
        byte[] data = new byte[aData.readShort()];
        aData.readFully(data);
        return new GTPacketFrameEntity(x, y, z, data);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (!(aWorld instanceof World world) || !world.isRemote) return;
        if (!world.blockExists(mX, mY, mZ)) return;
        if (!(world.getBlock(mX, mY, mZ) instanceof FrameShapeBlock)) return;
        TileEntity te = world.getTileEntity(mX, mY, mZ);
        BaseMetaPipeEntity base;
        if (te instanceof BaseMetaPipeEntity existing) {
            base = existing;
        } else {
            base = new BaseMetaPipeEntity();
            world.setTileEntity(mX, mY, mZ, base);
        }
        base.receiveInitialDataOnClient(mData);
        world.markBlockForUpdate(mX, mY, mZ);
    }
}
