package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableMap;
import com.ruling_0.materiallib.api.Material;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputerPP;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.material.MaterialUtils;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeFusionComputer4 extends MTELargeFusionComputerPP {

    public MTELargeFusionComputer4(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer4(String name) {
        super(name);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.fusion_reactor"))
            .addMarkdown(
                new ResourceLocation("gregtech", "large-fusion-computer-mk4"),
                ImmutableMap.of(
                    "power", formatNumber(getSingleHatchPower()),
                    "capacity", formatNumber(capableStartupCanonical() / 32 / M),
                    "tier", GTUtility.getColoredTierNameFromTier((byte) tier())))
            .addSupportAny()
            .beginStructureBlock(47, 7, 47, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.middle_center_4th_layer"))
            .addCasing("1662-1695", new ItemStack(getCasingBlock(), 1, getCasingMeta()).getDisplayName(), false)
            .addCasing("560", new ItemStack(getCoilBlock(), 1, getCoilMeta()).getDisplayName(), false)
            .addCasing("128", OrePrefixes.frameGt.getLocalizedNameForItem(MaterialUtils.internalName(getFrameBox())), false)
            .addCasing("63-93", new ItemStack(getGlassBlock(), 1, getGlassMeta()).getDisplayName(), false)
            .addEnergyHatch("1-32", StatCollector.translateToLocalFormatted("gt.mbtt.structure.specific_casings_on_each_curve", GTValues.VN[energyHatchTier()]), 2)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.specific_glass_on_each_side"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.specific_glass_on_each_side"), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("gt.mbtt.structure.supports_crafting_input_buffers"))
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public int tier() {
        return 9;
    }

    @Override
    public long capableStartupCanonical() {
        return 5_120_000_000L;
    }

    @Override
    public Block getCasingBlock() {
        return ModBlocks.blockCasings3Misc;
    }

    @Override
    public int getCasingMeta() {
        return 12;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 3;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 6;
    }

    @Override
    public Block getGlassBlock2() {
        return ItemRegistry.bw_realglas2;
    }

    @Override
    public int getGlassMeta2() {
        return 1;
    }

    @Override
    public int energyHatchTier() {
        return 9;
    }

    @Override
    public Material getFrameBox() {
        return Materials.InfinityCatalyst;
    }

    @Override
    public ITexture getTextureOverlay() {
        if (this.mMaxProgresstime > 0) return TextureFactory.of(
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Casing_Machine_Screen_3)
                .extFacing()
                .build());
        else return TextureFactory.of(
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Casing_Machine_Screen_1)
                .extFacing()
                .build());
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(long startEnergy) {
        if (startEnergy < 160000000L) return 4;
        if (startEnergy < 320000000L) return 3;
        if (startEnergy < 640000000L) return 2;
        return 1;
    }

    @Override
    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.eEnergyMulti != null) {
            for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build(), getTextureOverlay() };
        if (!aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA)
            .extFacing()
            .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer4(mName);
    }
}
