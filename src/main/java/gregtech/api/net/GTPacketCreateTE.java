package gregtech.api.net;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import io.netty.buffer.ByteBuf;

public class GTPacketCreateTE extends GTPacket {

    private int mX, mZ, mC0, mC1, mC2, mC3, mC4, mC5;
    private short mY, mID, mRID;
    private byte mTexture, mTexturePage, mUpdate, mRedstone, mColor;

    public GTPacketCreateTE() {
        super();
    }

    public GTPacketCreateTE(int aX, short aY, int aZ, short aRID, short aID, int aC0, int aC1, int aC2, int aC3,
        int aC4, int aC5, byte aTexture, byte aTexturePage, byte aUpdate, byte aRedstone, byte aColor) {
        super();
        mX = aX;
        mY = aY;
        mZ = aZ;
        mC0 = aC0;
        mC1 = aC1;
        mC2 = aC2;
        mC3 = aC3;
        mC4 = aC4;
        mC5 = aC5;
        mRID = aRID;
        mID = aID;
        mTexture = aTexture;
        mTexturePage = aTexturePage;
        mUpdate = aUpdate;
        mRedstone = aRedstone;
        mColor = aColor;
    }

    public GTPacketCreateTE(int aX, short aY, int aZ, short aID, int aC0, int aC1, int aC2, int aC3, int aC4, int aC5,
        byte aTexture, byte aTexturePage, byte aUpdate, byte aRedstone, byte aColor) {
        this(
            aX,
            aY,
            aZ,
            (short) 0,
            aID,
            aC0,
            aC1,
            aC2,
            aC3,
            aC4,
            aC5,
            aTexture,
            aTexturePage,
            aUpdate,
            aRedstone,
            aColor);
    }

    public GTPacketCreateTE(int aX, short aY, int aZ, short aID, int aC0, int aC1, int aC2, int aC3, int aC4, int aC5,
        byte aTexture, byte aUpdate, byte aRedstone, byte aColor) {
        this(aX, aY, aZ, (short) 0, aID, aC0, aC1, aC2, aC3, aC4, aC5, aTexture, (byte) 0, aUpdate, aRedstone, aColor);
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.CREATE_TILE_ENTITY.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeShort(mRID);
        aOut.writeShort(mID);

        aOut.writeInt(mC0);
        aOut.writeInt(mC1);
        aOut.writeInt(mC2);
        aOut.writeInt(mC3);
        aOut.writeInt(mC4);
        aOut.writeInt(mC5);

        aOut.writeByte(mTexture);
        aOut.writeByte(mTexturePage);
        aOut.writeByte(mUpdate);
        aOut.writeByte(mRedstone);
        aOut.writeByte(mColor);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketCreateTE(
            // Coords
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            // Registry & ID
            aData.readShort(),
            aData.readShort(),
            // Covers
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            // Everything else
            aData.readByte(),
            aData.readByte(),
            aData.readByte(),
            aData.readByte(),
            aData.readByte());
    }

    @Override
    public void process(IBlockAccess world) {
        if (world == null) return;
        if (world instanceof WorldClient) {
            final TileEntity tileEntity = world.getTileEntity(mX, mY, mZ);
            if (tileEntity == null) {
                try {
                    if (mTexturePage != 0) // It's a MetaTileEntity
                    {
                        BaseMetaTileEntity newTileEntity = getBaseMetaTileEntity();
                        ((WorldClient) world).setTileEntity(mX, mY, mZ, newTileEntity);
                    } else // Otherwise it's a MetaPipeEntity
                    {
                        BaseMetaPipeEntity newTileEntity = getBaseMetaPipeEntity();
                        ((WorldClient) world).setTileEntity(mX, mY, mZ, newTileEntity);
                    }
                } catch (Exception e) {
                    GTMod.GT_FML_LOGGER.error("Exception creating Tile Entity at ({}, {}, {})", mX, mY, mZ);
                }
            } else {
                try {
                    if (tileEntity instanceof BaseMetaTileEntity)
                        ((BaseMetaTileEntity) tileEntity).receiveMetaTileEntityData(
                            mID,
                            mC0,
                            mC1,
                            mC2,
                            mC3,
                            mC4,
                            mC5,
                            mTexture,
                            mTexturePage,
                            mUpdate,
                            mRedstone,
                            mColor);
                    else if (tileEntity instanceof BaseMetaPipeEntity)
                        ((BaseMetaPipeEntity) tileEntity).receiveMetaTileEntityData(
                            mID,
                            mC0,
                            mC1,
                            mC2,
                            mC3,
                            mC4,
                            mC5,
                            mTexture,
                            mUpdate,
                            mRedstone,
                            mColor);
                } catch (Exception e) {
                    GTMod.GT_FML_LOGGER.error(
                        "Exception setting tile entity data for tile entity {} at ({}, {}, {})",
                        tileEntity,
                        mX,
                        mY,
                        mZ);
                }
            }
        }
    }

    @NotNull
    private BaseMetaPipeEntity getBaseMetaPipeEntity() {
        BaseMetaPipeEntity newTileEntity = new BaseMetaPipeEntity();
        newTileEntity
            .receiveMetaTileEntityData(mID, mC0, mC1, mC2, mC3, mC4, mC5, mTexture, mUpdate, mRedstone, mColor);
        return newTileEntity;
    }

    @NotNull
    private BaseMetaTileEntity getBaseMetaTileEntity() {
        BaseMetaTileEntity newTileEntity = new BaseMetaTileEntity();
        newTileEntity.receiveMetaTileEntityData(
            mID,
            mC0,
            mC1,
            mC2,
            mC3,
            mC4,
            mC5,
            mTexture,
            mTexturePage,
            mUpdate,
            mRedstone,
            mColor);
        return newTileEntity;
    }
}
