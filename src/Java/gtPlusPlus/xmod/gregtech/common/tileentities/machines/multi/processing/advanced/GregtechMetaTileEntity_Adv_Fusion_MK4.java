package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import java.lang.reflect.Method;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_Adv_Fusion_MK4 extends GT_MetaTileEntity_FusionComputer {

	public static final Method mUpdateHatchTexture;
	
	static {
		mUpdateHatchTexture = ReflectionUtils.getMethod(GT_MetaTileEntity_Hatch.class, "updateTexture", int.class);
	}
	
	public GregtechMetaTileEntity_Adv_Fusion_MK4(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional, 6);
	}

	public GregtechMetaTileEntity_Adv_Fusion_MK4(String aName) {
		super(aName);
	}

	@Override
	public int tier() {
		return 8;
	}

	@Override
	public long maxEUStore() {
		return (640010000L*4) * (Math.min(16, this.mEnergyHatches.size())) / 8L;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_Fusion_MK4(mName);
	}

	@Override
	public Block getCasing() {
		return getFusionCoil();
	}

	@Override
	public int getCasingMeta() {
		return 12;
	}

	@Override
	public Block getFusionCoil() {
		return ModBlocks.blockCasings3Misc;
	}

	@Override
	public int getFusionCoilMeta() {
		return 13;
	}

	public String[] getDescription() {
		String aTierName = GT_Values.VN[9];
		return new String[]{
				"HARNESSING THE POWER OF A NEUTRON STAR", 
				"Fusion Machine Casings MK III around Advanced Fusion Coils", 
				"2-16 Input Hatches", 
				"1-16 Output Hatches", 
				"1-16 Energy Hatches", 
				"All Hatches must be "+aTierName+" or better", 
				"32768 EU/t and 80mio EU Cap per Energy Hatch",
				CORE.GT_Tooltip};
	}

	@Override
	public int tierOverclock() {
		return 8;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		ITexture[] sTexture;
		if (aSide == aFacing) {
			sTexture = new ITexture[]{
					new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
							Dyes.getModulation(-1, Dyes._NULL.mRGBa)),
					new GT_RenderedTexture(this.getIconOverlay())};
		} else if (!aActive) {
			sTexture = new ITexture[]{
					new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
							Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		} else {
			sTexture = new ITexture[]{
					new GT_RenderedTexture((IIconContainer) TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA,
							Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		}
		return sTexture;
	}

	@Override
	public IIconContainer getIconOverlay() {
		return this.mMaxProgresstime > 0 ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1;
	}

	@Override
	public int overclock(final int mStartEnergy) {
		if (this.tierOverclock() == 1) {
			return 1;
		}
		if (this.tierOverclock() == 2) {
			return (mStartEnergy < 160000000) ? 2 : 1;
		}
		if (this.tierOverclock() == 4) {
			return (mStartEnergy < 160000000 ? 4 : (mStartEnergy < 320000000 ? 2 : 1));
		}
		return (mStartEnergy < 160000000) ? 8 : ((mStartEnergy < 320000000) ? 4 : (mStartEnergy < 640000000) ? 2 : 1);
	}

	public boolean turnCasingActive(final boolean status) {
		try {
		if (this.mEnergyHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
				mUpdateHatchTexture.invoke(hatch, (status ? TAE.getIndexFromPage(2, 14) : 53));
			}
		}
		if (this.mOutputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Output hatch2 : this.mOutputHatches) {
				mUpdateHatchTexture.invoke(hatch2, (status ? TAE.getIndexFromPage(2, 14) : 53));
			}
		}
		if (this.mInputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Input hatch3 : this.mInputHatches) {
				mUpdateHatchTexture.invoke(hatch3, (status ? TAE.getIndexFromPage(2, 14) : 53));
			}
		}
		}
		catch (Throwable t) {
			return false;
		}
		return true;
	}

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xCenter = getBaseMetaTileEntity().getXCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetX * 5;
        int yCenter = getBaseMetaTileEntity().getYCoord();
        int zCenter = getBaseMetaTileEntity().getZCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetZ * 5;
        if (((isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter)) || (xCenter + 5 == getBaseMetaTileEntity().getXCoord()))
                && ((isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter)) || (xCenter - 5 == getBaseMetaTileEntity().getXCoord()))
                && ((isAdvancedMachineCasing(xCenter, yCenter, zCenter + 5)) || (zCenter + 5 == getBaseMetaTileEntity().getZCoord()))
                && ((isAdvancedMachineCasing(xCenter, yCenter, zCenter - 5)) || (zCenter - 5 == getBaseMetaTileEntity().getZCoord())) && (checkCoils(xCenter, yCenter, zCenter))
                && (checkHulls(xCenter, yCenter, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter + 1, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter - 1, zCenter))
                && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter + 3, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter - 3, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter + 5, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter - 5, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter + 3, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter - 3, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter + 5, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter - 5, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 3, yCenter, zCenter + 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 3, yCenter, zCenter + 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 5, yCenter, zCenter + 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 5, yCenter, zCenter + 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 3, yCenter, zCenter - 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 3, yCenter, zCenter - 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 5, yCenter, zCenter - 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 5, yCenter, zCenter - 4, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 1, yCenter, zCenter - 5, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 1, yCenter, zCenter + 5, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 1, yCenter, zCenter - 5, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 1, yCenter, zCenter + 5, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 1, yCenter, zCenter - 7, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 1, yCenter, zCenter + 7, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 1, yCenter, zCenter - 7, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 1, yCenter, zCenter + 7, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 5, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 5, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 5, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 5, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 7, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 7, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 7, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 7, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter + 1, yCenter + 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 1, yCenter + 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 1, yCenter + 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter - 1, yCenter + 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter + 1, zCenter + 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter + 1, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter + 1, zCenter - 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter + 1, zCenter - 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter + 1, yCenter - 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 1, yCenter - 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 1, yCenter - 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter - 1, yCenter - 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter - 1, zCenter + 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter - 1, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter - 1, zCenter - 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter - 1, zCenter - 1, aBaseMetaTileEntity))
                && (this.mEnergyHatches.size() >= 1) && (this.mOutputHatches.size() >= 1) && (this.mInputHatches.size() >= 2)) {
            int mEnergyHatches_sS = this.mEnergyHatches.size();
            for (int i = 0; i < mEnergyHatches_sS; i++) {
                if (this.mEnergyHatches.get(i).mTier < 9)
                    return false;
            }
            int mOutputHatches_sS = this.mOutputHatches.size();
            for (int i = 0; i < mOutputHatches_sS; i++) {
                if (this.mOutputHatches.get(i).mTier < 9)
                    return false;
            }
            int mInputHatches_sS = this.mInputHatches.size();
            for (int i = 0; i < mInputHatches_sS; i++) {
                if (this.mInputHatches.get(i).mTier < 9)
                    return false;
            }
            mWrench = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mHardHammer = true;
            mSolderingTool = true;
            mCrowbar = true;
            return true;
        }
        return false;
    }

    private boolean checkCoils(int aX, int aY, int aZ) {
        return (isFusionCoil(aX + 6, aY, aZ - 1)) && (isFusionCoil(aX + 6, aY, aZ)) && (isFusionCoil(aX + 6, aY, aZ + 1)) && (isFusionCoil(aX + 5, aY, aZ - 3)) && (isFusionCoil(aX + 5, aY, aZ - 2))
                && (isFusionCoil(aX + 5, aY, aZ + 2)) && (isFusionCoil(aX + 5, aY, aZ + 3)) && (isFusionCoil(aX + 4, aY, aZ - 4)) && (isFusionCoil(aX + 4, aY, aZ + 4))
                && (isFusionCoil(aX + 3, aY, aZ - 5)) && (isFusionCoil(aX + 3, aY, aZ + 5)) && (isFusionCoil(aX + 2, aY, aZ - 5)) && (isFusionCoil(aX + 2, aY, aZ + 5))
                && (isFusionCoil(aX + 1, aY, aZ - 6)) && (isFusionCoil(aX + 1, aY, aZ + 6)) && (isFusionCoil(aX, aY, aZ - 6)) && (isFusionCoil(aX, aY, aZ + 6)) && (isFusionCoil(aX - 1, aY, aZ - 6))
                && (isFusionCoil(aX - 1, aY, aZ + 6)) && (isFusionCoil(aX - 2, aY, aZ - 5)) && (isFusionCoil(aX - 2, aY, aZ + 5)) && (isFusionCoil(aX - 3, aY, aZ - 5))
                && (isFusionCoil(aX - 3, aY, aZ + 5)) && (isFusionCoil(aX - 4, aY, aZ - 4)) && (isFusionCoil(aX - 4, aY, aZ + 4)) && (isFusionCoil(aX - 5, aY, aZ - 3))
                && (isFusionCoil(aX - 5, aY, aZ - 2)) && (isFusionCoil(aX - 5, aY, aZ + 2)) && (isFusionCoil(aX - 5, aY, aZ + 3)) && (isFusionCoil(aX - 6, aY, aZ - 1))
                && (isFusionCoil(aX - 6, aY, aZ)) && (isFusionCoil(aX - 6, aY, aZ + 1));
    }

    private boolean checkUpperOrLowerHulls(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX + 6, aY, aZ)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 2))
                && (isAdvancedMachineCasing(aX + 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX + 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 4, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX + 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 5))
                && (isAdvancedMachineCasing(aX + 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 5)) && (isAdvancedMachineCasing(aX, aY, aZ - 6))
                && (isAdvancedMachineCasing(aX, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 2, aY, aZ + 5))
                && (isAdvancedMachineCasing(aX - 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 5)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX - 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 2))
                && (isAdvancedMachineCasing(aX - 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ));
    }

    private boolean checkHulls(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX + 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 6, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX + 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 6))
                && (isAdvancedMachineCasing(aX + 2, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 6))
                && (isAdvancedMachineCasing(aX - 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 6))
                && (isAdvancedMachineCasing(aX - 7, aY, aZ)) && (isAdvancedMachineCasing(aX + 7, aY, aZ)) && (isAdvancedMachineCasing(aX, aY, aZ - 7)) && (isAdvancedMachineCasing(aX, aY, aZ + 7))
                && (isAdvancedMachineCasing(aX - 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 6, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX - 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 4, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX + 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 4, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX - 2, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ - 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 4));
    }
    
    private boolean addIfEnergyInjector(int aX, int aY, int aZ, IGregTechTileEntity aBaseMetaTileEntity) {
        if (addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean addIfInjector(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
        if (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean addIfExtractor(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
        if (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean isAdvancedMachineCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasing()) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta());
    }
    
    private boolean isFusionCoil(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getFusionCoil() && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getFusionCoilMeta()));
    }
	
}
