package gregtech.api.net;

import static gregtech.api.enums.GT_Values.B;

import com.google.common.io.ByteArrayDataInput;
import gregtech.GT_Mod;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;

public class GT_Packet_MultiTileEntity extends GT_Packet_New {
    public static final int COVERS = B[0], REDSTONE = B[1], MODES = B[2], CONTROLLER = B[3];

    private int features = 0;

    private int mX, mZ;
    private int mC0 = 0, mC1 = 0, mC2 = 0, mC3 = 0, mC4 = 0, mC5 = 0;
    private short mY, mID, mRID;
    private byte mCommonData, mTexturePage, mUpdate, mRedstone, mColor;
    private ChunkCoordinates mTargetPos = null;

    // MultiBlockPart
    private byte mMode;
    private int mAllowedModes;

    public GT_Packet_MultiTileEntity() {
        super(true);
    }

    // For multi tiles
    public GT_Packet_MultiTileEntity(
            int aFeatures, int aX, short aY, int aZ, short aRID, short aID, byte aCommonData, byte aColor) {
        super(false);
        features = aFeatures;

        mX = aX;
        mY = aY;
        mZ = aZ;
        mRID = aRID;
        mID = aID;
        mCommonData = aCommonData;
        mColor = aColor;
    }

    public void setCoverData(int aC0, int aC1, int aC2, int aC3, int aC4, int aC5) {
        features |= COVERS;

        mC0 = aC0;
        mC1 = aC1;
        mC2 = aC2;
        mC3 = aC3;
        mC4 = aC4;
        mC5 = aC5;
    }

    public void setRedstoneData(byte aRedstoneData) {
        features |= REDSTONE;

        mRedstone = aRedstoneData;
    }

    public void setModes(byte aMode, int aAllowedModes) {
        features |= MODES;
        mMode = aMode;
        mAllowedModes = aAllowedModes;
    }

    public void setTargetPos(int aX, short aY, int aZ) {
        features |= CONTROLLER;
        mTargetPos = new ChunkCoordinates(aX, aY, aZ);
    }

    @Override
    public void encode(ByteBuf aOut) {
        // Features
        aOut.writeInt(features);

        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeShort(mRID);
        aOut.writeShort(mID);
        aOut.writeByte(mCommonData);
        aOut.writeByte(mColor);

        if ((features & COVERS) == COVERS) {
            aOut.writeInt(mC0);
            aOut.writeInt(mC1);
            aOut.writeInt(mC2);
            aOut.writeInt(mC3);
            aOut.writeInt(mC4);
            aOut.writeInt(mC5);
        }
        if ((features & REDSTONE) == REDSTONE) {
            aOut.writeByte(mRedstone);
        }
        if ((features & MODES) == MODES) {
            aOut.writeByte(mMode);
            aOut.writeInt(mAllowedModes);
        }
        if ((features & CONTROLLER) == CONTROLLER) {
            aOut.writeInt(mTargetPos.posX);
            aOut.writeShort(mTargetPos.posY);
            aOut.writeInt(mTargetPos.posZ);
        }

        if (false) {
            aOut.writeByte(mTexturePage);
            aOut.writeByte(mUpdate);
            aOut.writeByte(mColor);
        }
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        final int packetFeatures = aData.readInt();

        final GT_Packet_MultiTileEntity packet = new GT_Packet_MultiTileEntity(
                packetFeatures,
                // Coords
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),
                // Registry & ID
                aData.readShort(),
                aData.readShort(),
                // Common Data
                aData.readByte(),
                aData.readByte());
        if ((packetFeatures & COVERS) == COVERS) {
            packet.setCoverData(
                    aData.readInt(),
                    aData.readInt(),
                    aData.readInt(),
                    aData.readInt(),
                    aData.readInt(),
                    aData.readInt());
        }
        if ((packetFeatures & REDSTONE) == REDSTONE) {
            packet.setRedstoneData(aData.readByte());
        }
        if ((packetFeatures & MODES) == MODES) {
            packet.setModes(aData.readByte(), aData.readInt());
        }
        if ((packetFeatures & CONTROLLER) == CONTROLLER) {
            packet.setTargetPos(aData.readInt(), aData.readShort(), aData.readInt());
        }

        return packet;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null) return;
        final TileEntity tTileEntity = aWorld.getTileEntity(mX, mY, mZ);
        try {
            final Block tBlock = aWorld.getBlock(mX, mY, mZ);
            if (tBlock instanceof MultiTileEntityBlock) {
                final MultiTileEntityBlock mteBlock = (MultiTileEntityBlock) tBlock;
                final IMultiTileEntity mte = mteBlock.receiveMultiTileEntityData(aWorld, mX, mY, mZ, mRID, mID);
                if (mte == null) return;
                mte.receiveClientEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, mCommonData);
                mte.receiveClientEvent(GregTechTileClientEvents.CHANGE_COLOR, mColor);

                if ((features & COVERS) == COVERS) {
                    mteBlock.receiveCoverData(mte, mC0, mC1, mC2, mC3, mC4, mC5);
                }
                if ((features & REDSTONE) == REDSTONE) {
                    mte.receiveClientEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, mRedstone);
                }

                if ((features & MODES) == MODES && mte instanceof IMultiTileEntity.IMTE_HasModes) {
                    final IMultiTileEntity.IMTE_HasModes mteModes = (IMultiTileEntity.IMTE_HasModes) mte;
                    mteModes.setMode(mMode);
                    mteModes.setAllowedModes(mAllowedModes);
                }

                if ((features & CONTROLLER) == CONTROLLER && mte instanceof IMultiBlockPart) {
                    final IMultiBlockPart mtePart = (IMultiBlockPart) mte;
                    mtePart.setTargetPos(mTargetPos);
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                    "Exception setting tile entity data for tile entity {} at ({}, {}, {})", tTileEntity, mX, mY, mZ);
        }
    }

    @Override
    public byte getPacketID() {
        return 18;
    }
}
