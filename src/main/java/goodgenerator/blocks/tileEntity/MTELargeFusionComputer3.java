package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3_GLOW;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableMap;
import com.ruling_0.materiallib.api.Material;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.material.MaterialUtils;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeFusionComputer3 extends MTELargeFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION3)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION3_GLOW)
            .extFacing()
            .glow()
            .build());

    public MTELargeFusionComputer3(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer3(String name) {
        super(name);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.fusion_reactor"))
            .addMarkdown(
                new ResourceLocation("gregtech", "large-fusion-computer-mk3"),
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
        return 8;
    }

    @Override
    public long capableStartupCanonical() {
        return 640_000_000;
    }

    @Override
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 8;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 2;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 5;
    }

    @Override
    public int energyHatchTier() {
        return 8;
    }

    @Override
    public Material getFrameBox() {
        return Materials.Neutronium;
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(long startEnergy) {
        return (startEnergy < 160000000L ? 3 : (startEnergy < 320000000L ? 2 : 1));
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer3(mName);
    }
}
