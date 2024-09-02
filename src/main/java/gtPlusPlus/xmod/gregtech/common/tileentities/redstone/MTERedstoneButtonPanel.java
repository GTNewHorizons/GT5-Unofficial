package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class MTERedstoneButtonPanel extends MTERedstoneBase {

    public byte mRedstoneStrength = 0, mType = 0, mUpdate = 0;

    public static TexturesGtBlock.CustomIcon[] sIconList = new TexturesGtBlock.CustomIcon[64];

    static {
        for (int i = 0; i < 64; i++) {
            sIconList[i] = new CustomIcon("TileEntities/gt4/redstone/ButtonPanel/" + i);
        }
    }

    public MTERedstoneButtonPanel(int aID) {
        super(
            aID,
            "redstone.button.panel",
            "Button Panel",
            5,
            0,
            "Right-click with Screwdriver to change Button Design");
    }

    public MTERedstoneButtonPanel(final String aName, String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, 5, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERedstoneButtonPanel(this.mName, mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mRedstoneStrength", mRedstoneStrength);
        aNBT.setByte("mType", mType);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mRedstoneStrength = aNBT.getByte("mRedstoneStrength");
        mType = aNBT.getByte("mType");
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mRedstoneStrength = (byte) (aValue & 15);
        mType = (byte) (aValue >>> 4);
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((mRedstoneStrength & 15) | (mType << 4));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            if (getBaseMetaTileEntity().isServerSide()) {
                mUpdate = 2;
                switch (mType) {
                    case 0:
                    default:
                        switch (side) {
                            case DOWN, UP -> mRedstoneStrength = (byte) ((byte) (aX * 4) + 4 * (byte) (aZ * 4));
                            case NORTH -> mRedstoneStrength = (byte) ((byte) (4 - aX * 4) + 4 * (byte) (4 - aY * 4));
                            case SOUTH -> mRedstoneStrength = (byte) ((byte) (aX * 4) + 4 * (byte) (4 - aY * 4));
                            case WEST -> mRedstoneStrength = (byte) ((byte) (aZ * 4) + 4 * (byte) (4 - aY * 4));
                            case EAST -> mRedstoneStrength = (byte) ((byte) (4 - aZ * 4) + 4 * (byte) (4 - aY * 4));
                        }
                        break;
                    case 1:
                        switch (side) {
                            case DOWN, UP -> mRedstoneStrength = (byte) (mRedstoneStrength
                                ^ (1 << (((byte) (aX * 2) + 2 * (byte) (aZ * 2)))));
                            case NORTH -> mRedstoneStrength = (byte) (mRedstoneStrength
                                ^ (1 << (((byte) (2 - aX * 2) + 2 * (byte) (2 - aY * 2)))));
                            case SOUTH -> mRedstoneStrength = (byte) (mRedstoneStrength
                                ^ (1 << (((byte) (aX * 2) + 2 * (byte) (2 - aY * 2)))));
                            case WEST -> mRedstoneStrength = (byte) (mRedstoneStrength
                                ^ (1 << (((byte) (aZ * 2) + 2 * (byte) (2 - aY * 2)))));
                            case EAST -> mRedstoneStrength = (byte) (mRedstoneStrength
                                ^ (1 << (((byte) (2 - aZ * 2) + 2 * (byte) (2 - aY * 2)))));
                        }
                        break;
                    case 2:
                        switch (side) {
                            case DOWN, UP -> mRedstoneStrength = (byte) (mRedstoneStrength ^ (1 << ((byte) (aZ * 4))));
                            case NORTH -> mRedstoneStrength = (byte) (mRedstoneStrength ^ (1 << ((byte) (4 - aY * 4))));
                            case SOUTH -> mRedstoneStrength = (byte) (mRedstoneStrength ^ (1 << ((byte) (4 - aY * 4))));
                            case WEST -> mRedstoneStrength = (byte) (mRedstoneStrength ^ (1 << ((byte) (4 - aY * 4))));
                            case EAST -> mRedstoneStrength = (byte) (mRedstoneStrength ^ (1 << ((byte) (4 - aY * 4))));
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (getBaseMetaTileEntity().isServerSide()) {
            getBaseMetaTileEntity().setGenericRedstoneOutput(true);
            if (mUpdate > 0) {
                mUpdate--;
            } else if (getBaseMetaTileEntity().isAllowedToWork()) {
                mRedstoneStrength = 0;
            }
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                getBaseMetaTileEntity().setStrongOutputRedstoneSignal(
                    side,
                    side == getBaseMetaTileEntity().getFrontFacing() ? (byte) 0 : mRedstoneStrength);
                getBaseMetaTileEntity().setInternalOutputRedstoneSignal(
                    side,
                    side == getBaseMetaTileEntity().getFrontFacing() ? (byte) 0 : mRedstoneStrength);
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) mType = (byte) ((mType + 1) % 3);
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getSides(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFront(i);
            rTextures[6][i + 1] = this.getSidesActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1],
                new GTRenderedTexture(sIconList[mType * 16 + mRedstoneStrength]) };
        }
        return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Top_Main_Off) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Top_Main_On) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_Main_Off) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_Main_On) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Main_Off) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Main_On) };
    }
}
