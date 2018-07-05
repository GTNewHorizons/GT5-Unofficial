package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import net.minecraft.block.Block;

public class GregtechMetaTileEntity_Adv_Fusion_MK4 extends GT_MetaTileEntity_FusionComputer {

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
		return new String[]{
				"HARNESSING THE POWER OF A NEUTRON STAR", 
				"Fusion Machine Casings MK III around Advanced Fusion Coils", 
				"2-16 Input Hatches", 
				"1-16 Output Hatches", 
				"1-16 Energy Hatches", 
				"All Hatches must be UV or better", 
				"32768 EU/t and 80mio EU Cap per Energy Hatch",
				"Produces "+this.getPollutionPerTick(null)+" pollution per tick",
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
		if (this.mEnergyHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
				hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
			}
		}
		if (this.mOutputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Output hatch2 : this.mOutputHatches) {
				hatch2.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
			}
		}
		if (this.mInputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Input hatch3 : this.mInputHatches) {
				hatch3.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
			}
		}
		return true;
	}

}
