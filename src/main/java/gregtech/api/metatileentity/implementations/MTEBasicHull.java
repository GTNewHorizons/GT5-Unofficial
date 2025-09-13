package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEBasicHull extends MTEBasicTank {

    public MTEBasicHull(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 1, aDescription, aTextures);
    }

    public MTEBasicHull(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBasicHull(mName, mTier, mInventory.length, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return !isOutputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier] * 50;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aConnected, boolean redstoneLevel) {
        return mTextures[Math.min(2, side.ordinal()) + (side == aFacing ? 3 : 0)][colorIndex + 1];
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[6][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1] };
            rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1] };
            rTextures[2][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1] };
            rTextures[3][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 1] };
            rTextures[4][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 1] };
            rTextures[5][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 1] };
        }
        return rTextures;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public int getCapacity() {
        return (mTier + 1) * 1000;
    }
}
