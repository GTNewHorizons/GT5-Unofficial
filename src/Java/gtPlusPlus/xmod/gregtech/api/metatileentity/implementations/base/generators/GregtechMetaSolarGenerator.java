package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import static gregtech.api.enums.GT_Values.V;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;

public abstract class GregtechMetaSolarGenerator extends GT_MetaTileEntity_BasicTank {
	
	public int mEfficiency;
	public int mProcessingEnergy = 0;
	public int mSolarCharge = 20;
	public int mLossTimer = 0;
	public static int sEnergyPerTick = 16;
	
    public GregtechMetaSolarGenerator(int aID, String aName, String aNameRegional, int aTier, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public GregtechMetaSolarGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = getFront(i);
            rTextures[1][i + 1] = getBack(i);
            rTextures[2][i + 1] = getBottom(i);
            rTextures[3][i + 1] = getTop(i);
            rTextures[4][i + 1] = getSides(i);
            rTextures[5][i + 1] = getFrontActive(i);
            rTextures[6][i + 1] = getBackActive(i);
            rTextures[7][i + 1] = getBottomActive(i);
            rTextures[8][i + 1] = getTopActive(i);
            rTextures[9][i + 1] = getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
    }

    @Override
    public String[] getDescription() {
        return new String[]{mDescription, "Efficiency: " + getEfficiency() + "%"};
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    public ITexture[] getFront(byte aColor) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
    }

    public ITexture[] getBack(byte aColor) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
    }

    public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
    }

    public ITexture[] getTop(byte aColor) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
    }

    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1]};
    }

    public ITexture[] getFrontActive(byte aColor) {
        return getFront(aColor);
    }

    public ITexture[] getBackActive(byte aColor) {
        return getBack(aColor);
    }

    public ITexture[] getBottomActive(byte aColor) {
        return getBottom(aColor);
    }

    public ITexture[] getTopActive(byte aColor) {
        return getTop(aColor);
    }

    public ITexture[] getSidesActive(byte aColor) {
        return getSides(aColor);
    }
    
    @Override
    public boolean isFacingValid(byte aSide) {
        return aSide > 1;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 2;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public long maxEUOutput() {
        return getBaseMetaTileEntity().isAllowedToWork() ? V[mTier] : 0;
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[mTier] * 40 + getMinimumStoredEU());
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && aTick > 20L
				&& aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() + aBaseMetaTileEntity.getEUCapacity()) {

			if (this.mSolarCharge <= 20) {
				this.mSolarCharge = 20;
				this.mLossTimer = 0;
			}
			if (++this.mLossTimer > 45) {
				this.mSolarCharge -= 1;
				this.mLossTimer = 0;
			}

			if (aTick % 25L == 0L) {
				if (this.mSolarCharge > 100) {
					if ((this.mProcessingEnergy > 0) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 256L == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering() && aBaseMetaTileEntity.getUniversalEnergyStored() < (maxEUOutput() * 20 + getMinimumStoredEU()))) {
						getBaseMetaTileEntity().increaseStoredEnergyUnits(sEnergyPerTick * getEfficiency() / 10, false);
					}
				}
			}			 

			if ((this.mSolarCharge < 500) && (this.mProcessingEnergy > 0) && (aTick % 12L == 0L)) {
				this.mProcessingEnergy -= 1;
				this.mSolarCharge += 1;
			}

			if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 256L == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering())) {
				boolean bRain = aBaseMetaTileEntity.getWorld().isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F;
				mProcessingEnergy += bRain && aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4 || !aBaseMetaTileEntity.getSkyAtSide((byte) 1) ? 0 : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 : 1;
			}

			if (aBaseMetaTileEntity.isServerSide()){
				aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU());
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
