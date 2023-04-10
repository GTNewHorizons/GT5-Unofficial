package gregtech.api.net;

import static gregtech.api.enums.GT_Values.B;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GT_Mod;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileMachine;
import gregtech.common.tileentities.casings.upgrade.InventoryUpgrade;
import io.netty.buffer.ByteBuf;

public class GT_Packet_MultiTileEntity extends GT_Packet_New {

    public static final int COVERS = B[0], REDSTONE = B[1], MODES = B[2], CONTROLLER = B[3], INVENTORY_INDEX = B[4],
        INVENTORY_NAME = B[5], BOOLEANS = B[6], SOUND = B[7];

    private int features = 0;

    private int mX, mZ;
    private int mC0 = 0, mC1 = 0, mC2 = 0, mC3 = 0, mC4 = 0, mC5 = 0;
    private short mY, mID, mRID;
    private byte mCommonData, mRedstone, mColor;
    private ChunkCoordinates mTargetPos = null;
    private int mLockedInventoryIndex;
    private String mInventoryName;
    private int mInventoryLength;
    private int booleans;
    private byte soundEvent;
    private int soundEventValue;

    // MultiBlockPart
    private byte mMode;
    private int mAllowedModes;

    public GT_Packet_MultiTileEntity() {
        super(true);
    }

    // For multi tiles
    public GT_Packet_MultiTileEntity(int aFeatures, int aX, short aY, int aZ, short aRID, short aID, byte aCommonData,
        byte aColor) {
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

    public void setInventoryIndex(int aInventoryIndex) {
        features |= INVENTORY_INDEX;
        mLockedInventoryIndex = aInventoryIndex;

    }

    public void setInventoryName(String aInventoryName) {
        features |= INVENTORY_NAME;
        mInventoryName = aInventoryName;
    }

    /**
     *
     * @param boolToSync each bit of the integer will be a boolean.
     */
    public void setBooleans(int boolToSync) {
        features |= BOOLEANS;
        this.booleans = boolToSync;
    }

    public void setSoundEvent(byte soundEvent, int soundEventValue) {
        features |= SOUND;
        this.soundEvent = soundEvent;
        this.soundEventValue = soundEventValue;
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
        if ((features & INVENTORY_INDEX) == INVENTORY_INDEX) {
            aOut.writeInt(mLockedInventoryIndex);
        }
        if ((features & INVENTORY_NAME) == INVENTORY_NAME) {
            if (mInventoryName != null && mInventoryName.length() > 0) {
                mInventoryLength = mInventoryName.length();
                aOut.writeInt(mInventoryLength);
                for (char tChar : mInventoryName.toCharArray()) {
                    aOut.writeChar(tChar);
                }
            } else {
                mInventoryLength = 0;
                aOut.writeInt(mInventoryLength);
            }
        }

        if ((features & BOOLEANS) == BOOLEANS) {
            aOut.writeInt(booleans);
        }

        if ((features & SOUND) == SOUND) {
            aOut.writeByte(soundEvent);
            aOut.writeInt(soundEventValue);
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
        if ((packetFeatures & INVENTORY_INDEX) == INVENTORY_INDEX) {
            packet.setInventoryIndex(aData.readInt());
        }
        if ((packetFeatures & INVENTORY_NAME) == INVENTORY_NAME) {
            int tLength = aData.readInt();
            String tName;
            if (tLength > 0) {
                StringBuilder tNameBuilder = new StringBuilder();
                for (int i = 0; i < tLength; i++) {
                    tNameBuilder.append(aData.readChar());
                }
                tName = tNameBuilder.toString();
            } else {
                tName = null;
            }
            packet.setInventoryName(tName);
        }

        if ((packetFeatures & BOOLEANS) == BOOLEANS) {
            packet.setBooleans(aData.readInt());
        }

        if ((packetFeatures & SOUND) == SOUND) {
            packet.setSoundEvent(aData.readByte(), aData.readInt());
        }

        return packet;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null) return;
        final TileEntity tTileEntity = aWorld.getTileEntity(mX, mY, mZ);
        try {
            final Block tBlock = aWorld.getBlock(mX, mY, mZ);
            if (tBlock instanceof MultiTileEntityBlock mteBlock) {
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

                if ((features & MODES) == MODES && mte instanceof IMultiTileEntity.IMTE_HasModes mteModes) {
                    mteModes.setMode(mMode);
                    mteModes.setAllowedModes(mAllowedModes);
                }

                if ((features & CONTROLLER) == CONTROLLER && mte instanceof IMultiBlockPart) {
                    final IMultiBlockPart mtePart = (IMultiBlockPart) mte;
                    mtePart.setTargetPos(mTargetPos);
                }

                if ((features & INVENTORY_INDEX) == INVENTORY_INDEX && mte instanceof IMultiBlockPart) {
                    final IMultiBlockPart mtePart = (IMultiBlockPart) mte;
                    mtePart.setLockedInventoryIndex(mLockedInventoryIndex);
                }

                if ((features & INVENTORY_NAME) == INVENTORY_NAME && mte instanceof InventoryUpgrade invUpg) {
                    invUpg.setInventoryName(mInventoryName);
                }

                if ((features & BOOLEANS) == BOOLEANS && mte instanceof IMultiTileMachine) {
                    final IMultiTileMachine machine = (IMultiTileMachine) mte;
                    machine.setBooleans(booleans);
                }

                if ((features & SOUND) == SOUND && mte instanceof IMultiTileMachine) {
                    final IMultiTileMachine machine = (IMultiTileMachine) mte;
                    machine.setSound(soundEvent, soundEventValue);
                }

            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                "Exception setting tile entity data for tile entity {} at ({}, {}, {})",
                tTileEntity,
                mX,
                mY,
                mZ);
        }
    }

    @Override
    public byte getPacketID() {
        return 18;
    }
}
