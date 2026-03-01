package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.block.Block;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AdvancedFusionOverclockDescriber;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAdvFusionMk4 extends MTEFusionComputer {

    public MTEAdvFusionMk4(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvFusionMk4(String aName) {
        super(aName);
    }

    @Override
    protected OverclockDescriber createOverclockDescriber() {
        return new AdvancedFusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("gt.recipe.fusionreactor")
            .addInfo("gt.fusion.tips.c")
            .addInfo("gt.fusion.tips", "131,072", "320M")
            .addInfo("gt.fusion.tips.overclock")
            .beginStructureBlock(15, 3, 15, false)
            .addController("gt.fusion.info.1")
            .addCasingInfoMin(
                GregtechItemList.Casing_Fusion_External.get(1)
                    .getDisplayName(),
                79)
            .addStructureInfo("gt.fusion.info.2")
            .addStructurePart(
                GregtechItemList.Casing_Fusion_Internal.get(1)
                    .getDisplayName(),
                "gt.fusion.info.3")
            .addEnergyHatch("gt.fusion.info.4", 2)
            .addInputHatch("gt.fusion.info.5", 1)
            .addOutputHatch("gt.fusion.info.4", 3)
            .addStructureInfo("gt.fusion.info.6", TooltipHelper.voltageText(VoltageIndex.UHV))
            .toolTipFinisher();
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
    public long capableStartupCanonical() {
        return 5_120_000_000L;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvFusionMk4(mName);
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
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic().enablePerfectOverclock();
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1)),
                TextureFactory.builder()
                    .addIcon(this.getIconOverlay())
                    .extFacing()
                    .build() };
        } else if (!aActive) {
            return new ITexture[] {
                TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1)) };
        } else {
            return new ITexture[] {
                TextureFactory.of(TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA, Dyes.getModulation(-1)) };
        }
    }

    @Override
    public ITexture getTextureOverlay() {
        return TextureFactory.of(
            this.getBaseMetaTileEntity()
                .isActive() ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1);
    }

    public IIconContainer getIconOverlay() {
        return this.getBaseMetaTileEntity()
            .isActive() ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1;
    }

    @Override
    public boolean turnCasingActive(final boolean status) {
        if (this.mEnergyHatches != null) {
            for (final MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture((status ? TAE.getIndexFromPage(2, 14) : 53));
            }
        }
        if (this.mOutputHatches != null) {
            for (final MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (final MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String tier = "IV";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration;
            }
        }

        return new String[] { StatCollector.translateToLocalFormatted("gtpp.infodata.adv_fusion.name", tier),
            StatCollector.translateToLocalFormatted("gtpp.infodata.adv_fusion.eu_required", powerRequired),
            StatCollector.translateToLocalFormatted("gtpp.infodata.adv_fusion.stored_eu", mEUStore, maxEUStore()),
            StatCollector.translateToLocalFormatted("gtpp.infodata.adv_fusion.plasma_output", plasmaOut),
            StatCollector.translateToLocalFormatted("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }
}
