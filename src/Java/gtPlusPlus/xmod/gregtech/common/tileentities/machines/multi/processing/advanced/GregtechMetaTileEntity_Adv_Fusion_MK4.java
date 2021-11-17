package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.enums.Dyes;
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
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;

import java.lang.reflect.Method;

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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor")
                .addInfo("HARNESSING THE POWER OF A NEUTRON STAR")
                .addSeparator()
                .beginStructureBlock(15, 3, 15, false)
                .addController("See diagram when placed")
                .addCasingInfo("Fusion Machine Casings MK III", 79)
                .addStructureInfo("Cover the coils with casing")
                .addOtherStructurePart("Advanced Fusion Coils", "Center part of the ring")
                .addEnergyHatch("1-16, Specified casings", 2)
                .addInputHatch("2-16, Specified casings", 1)
                .addOutputHatch("1-16, Specified casings", 3)
                .addStructureInfo("ALL Hatches must be UHV or better")
                .toolTipFinisher("GT++");
        return tt;
	}

    @Override
    public int tier() {
        return 9;
    }

    @Override
    public long maxEUStore() {
        return (640010000L * 4) * (Math.min(16, this.mEnergyHatches.size())) / 8L;
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
                    new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
                            Dyes.getModulation(-1, Dyes._NULL.mRGBa)),
                    new GT_RenderedTexture(this.getIconOverlay())};
		} else if (!aActive) {
			sTexture = new ITexture[]{
                    new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
                            Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		} else {
			sTexture = new ITexture[]{
                    new GT_RenderedTexture(TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA,
                            Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		}
		return sTexture;
	}

    @Override
    public ITexture getTextureOverlay() {
        return new GT_RenderedTexture(this.mMaxProgresstime > 0 ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1);
    }

	public IIconContainer getIconOverlay() {
		return this.mMaxProgresstime > 0 ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1;
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
}
