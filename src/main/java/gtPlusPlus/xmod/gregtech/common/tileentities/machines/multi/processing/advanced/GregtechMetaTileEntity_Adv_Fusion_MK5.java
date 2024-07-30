package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AdvancedFusionOverclockDescriber;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_Adv_Fusion_MK5 extends GT_MetaTileEntity_FusionComputer {

    public static final Method mUpdateHatchTexture;

    static {
        mUpdateHatchTexture = ReflectionUtils.getMethod(GT_MetaTileEntity_Hatch.class, "updateTexture", int.class);
    }

    public GregtechMetaTileEntity_Adv_Fusion_MK5(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Adv_Fusion_MK5(String aName) {
        super(aName);
    }

    @Override
    protected OverclockDescriber createOverclockDescriber() {
        return new AdvancedFusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor")
            .addInfo("HARNESSING THE POWER OF A NEUTRON STAR")
            .addInfo("Controller block for the Fusion Reactor Mk V")
            .addInfo("524,288EU/t and 1.28B EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .addInfo("Performs 4/4 overclocks")
            .addSeparator()
            .beginStructureBlock(15, 3, 15, false)
            .addController("See diagram when placed")
            .addCasingInfoMin("Fusion Machine Casings MK IV", 79, false)
            .addStructureInfo("Cover the coils with casing")
            .addOtherStructurePart("Advanced Fusion Coils II", "Center part of the ring")
            .addEnergyHatch("1-16, Specified casings", 2)
            .addInputHatch("2-16, Specified casings", 1)
            .addOutputHatch("1-16, Specified casings", 3)
            .addStructureInfo("ALL Hatches must be UEV or better")
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public int tier() {
        return 10;
    }

    @Override
    public long maxEUStore() {
        return (640010000L * 16) * (Math.min(16, this.mEnergyHatches.size())) / 8L;
    }

    @Override
    public long capableStartupCanonical() {
        return 20_480_000_000L;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_Fusion_MK5(mName);
    }

    @Override
    public Block getCasing() {
        return getFusionCoil();
    }

    @Override
    public int getCasingMeta() {
        return 0;
    }

    @Override
    public Block getFusionCoil() {
        return ModBlocks.blockCasings6Misc;
    }

    @Override
    public int getFusionCoilMeta() {
        return 1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic().enablePerfectOverclock();
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                new GT_RenderedTexture(
                    Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
                    Dyes.getModulation(-1, Dyes._NULL.mRGBa)),
                TextureFactory.builder()
                    .addIcon(this.getIconOverlay())
                    .extFacing()
                    .build() };
        } else if (!aActive) {
            return new ITexture[] { new GT_RenderedTexture(
                Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS,
                Dyes.getModulation(-1, Dyes._NULL.mRGBa)) };
        } else {
            return new ITexture[] { new GT_RenderedTexture(
                TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_HYPER,
                Dyes.getModulation(-1, Dyes._NULL.mRGBa)) };
        }
    }

    @Override
    public ITexture getTextureOverlay() {
        return new GT_RenderedTexture(
            this.getBaseMetaTileEntity()
                .isActive() ? TexturesGtBlock.Casing_Machine_Screen_Rainbow : TexturesGtBlock.Casing_Machine_Screen_1);
    }

    public IIconContainer getIconOverlay() {
        return this.getBaseMetaTileEntity()
            .isActive() ? TexturesGtBlock.Casing_Machine_Screen_Rainbow : TexturesGtBlock.Casing_Machine_Screen_1;
    }

    @Override
    public boolean turnCasingActive(final boolean status) {
        try {
            if (this.mEnergyHatches != null) {
                for (final GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
                    mUpdateHatchTexture.invoke(hatch, (status ? TAE.getIndexFromPage(3, 6) : 53));
                }
            }
            if (this.mOutputHatches != null) {
                for (final GT_MetaTileEntity_Hatch_Output hatch2 : this.mOutputHatches) {
                    mUpdateHatchTexture.invoke(hatch2, (status ? TAE.getIndexFromPage(3, 6) : 53));
                }
            }
            if (this.mInputHatches != null) {
                for (final GT_MetaTileEntity_Hatch_Input hatch3 : this.mInputHatches) {
                    mUpdateHatchTexture.invoke(hatch3, (status ? TAE.getIndexFromPage(3, 6) : 53));
                }
            }
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String tier = "V";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration;
            }
        }

        return new String[] { "Fusion Reactor MK " + tier, "EU Required: " + powerRequired + "EU/t",
            "Stored EU: " + mEUStore + " / " + maxEUStore(), "Plasma Output: " + plasmaOut + "L/t" };
    }
}
