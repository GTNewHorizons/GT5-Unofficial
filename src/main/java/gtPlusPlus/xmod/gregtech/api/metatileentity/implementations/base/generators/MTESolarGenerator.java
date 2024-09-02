package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;

public abstract class MTESolarGenerator extends MTEBasicTank {

    public int mEfficiency;
    public int mProcessingEnergy = 0;
    public int mSolarCharge = 20;
    public int mLossTimer = 0;
    public static int sEnergyPerTick = 16;

    public MTESolarGenerator(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public MTESolarGenerator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, "Efficiency: " + this.getEfficiency() + "%");
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return this.getFront(aColor);
    }

    public ITexture[] getBackActive(final byte aColor) {
        return this.getBack(aColor);
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return this.getBottom(aColor);
    }

    public ITexture[] getTopActive(final byte aColor) {
        return this.getTop(aColor);
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return this.getSides(aColor);
    }

    @Override
    public boolean isFacingValid(final ForgeDirection side) {
        return side.offsetY == 0;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return aIndex < 2;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public long maxEUOutput() {
        return this.getBaseMetaTileEntity()
            .isAllowedToWork() ? V[this.mTier] : 0;
    }

    @Override
    public long maxEUStore() {
        return Math.max(this.getEUVar(), (V[this.mTier] * 40) + this.getMinimumStoredEU());
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
            && (aTick > 20L)
            && (aBaseMetaTileEntity.getUniversalEnergyStored()
                < (this.maxEUOutput() + aBaseMetaTileEntity.getEUCapacity()))) {

            if (this.mSolarCharge <= 20) {
                this.mSolarCharge = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > 45) {
                this.mSolarCharge -= 1;
                this.mLossTimer = 0;
            }

            if ((aTick % 25L) == 0L) {
                if (this.mSolarCharge > 100) {
                    if ((this.mProcessingEnergy > 0) && (aBaseMetaTileEntity.isAllowedToWork())
                        && ((aTick % 256L) == 0L)
                        && (!aBaseMetaTileEntity.getWorld()
                            .isThundering()
                            && (aBaseMetaTileEntity.getUniversalEnergyStored()
                                < ((this.maxEUOutput() * 20) + this.getMinimumStoredEU())))) {
                        this.getBaseMetaTileEntity()
                            .increaseStoredEnergyUnits((sEnergyPerTick * this.getEfficiency()) / 10, false);
                    }
                }
            }

            if ((this.mSolarCharge < 500) && (this.mProcessingEnergy > 0) && ((aTick % 12L) == 0L)) {
                this.mProcessingEnergy -= 1;
                this.mSolarCharge += 1;
            }

            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork())
                && ((aTick % 256L) == 0L)
                && (!aBaseMetaTileEntity.getWorld()
                    .isThundering())) {
                final boolean bRain = aBaseMetaTileEntity.getWorld()
                    .isRaining() && (aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
                this.mProcessingEnergy += (bRain && (aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4))
                    || !aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP) ? 0
                        : !bRain && aBaseMetaTileEntity.getWorld()
                            .isDaytime() ? 8 : 1;
            }

            if (aBaseMetaTileEntity.isServerSide()) {
                aBaseMetaTileEntity.setActive(
                    aBaseMetaTileEntity.isAllowedToWork() && (aBaseMetaTileEntity.getUniversalEnergyStored()
                        >= (this.maxEUOutput() + this.getMinimumStoredEU())));
            }
        }
    }

    public abstract int getEfficiency();

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
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }
}
