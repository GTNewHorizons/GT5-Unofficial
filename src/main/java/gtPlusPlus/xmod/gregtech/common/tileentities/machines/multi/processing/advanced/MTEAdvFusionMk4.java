package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.block.Block;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
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
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;
import gtPlusPlus.core.block.ModBlocks;
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
        tt.addMachineType("Fusion Reactor")
            .addInfo("HARNESSING THE POWER OF A BLUE GIANT")
            .addInfo("§b131,072§7 EU/t and §b320M§7 EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .addInfo("Performs 4/4 overclocks")
            .beginStructureBlock(15, 3, 15, false)
            .addController("See diagram when placed")
            .addCasingInfoMin("Fusion Machine Casings MK III", 79, false)
            .addStructureInfo("Cover the coils with casing")
            .addOtherStructurePart("Advanced Fusion Coils", "Center part of the ring")
            .addEnergyHatch("1-16, Specified casings", 2)
            .addInputHatch("2-16, Specified casings", 1)
            .addOutputHatch("1-16, Specified casings", 3)
            .addStructureInfo("ALL Hatches must be UHV or better")
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
}
