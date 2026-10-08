package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3_GLOW;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.ImmutableMap;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;

@IMetaTileEntity.SkipGenerateDescription
public class MTEFusionComputer3 extends MTEFusionComputer {

    private static final int MAX_ENERGY_HATCHES = 16;
    private static final int HATCH_POWER_DIVISOR = 16;

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

    public MTEFusionComputer3(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFusionComputer3(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 8;
    }

    @Override
    public long maxEUStore() {
        return 640010000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public long capableStartupCanonical() {
        return 640_000_000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFusionComputer3(mName);
    }

    @Override
    public Block getCasing() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 8;
    }

    @Override
    public Block getFusionCoil() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public int getFusionCoilMeta() {
        return 7;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType("Fusion Reactor")
            .addMarkdown(
                new ResourceLocation("gregtech", "fusion-computer"),
                ImmutableMap.<String, Object>builder()
                    .put("power", formatNumber(GTValues.V[tier()] / HATCH_POWER_DIVISOR))
                    .put("capacity", formatNumber(capableStartupCanonical() / MAX_ENERGY_HATCHES))
                    .put("tier", GTValues.TIER_COLORS[tier()] + GTValues.VN[tier()])
                    .build())
            .addSupportAny()
            .beginStructureBlock(15, 3, 15, false)
            .addController("Middle center, 2nd layer")
            .addCasing("79-123", "Fusion Machine Casing Mk-II", false)
            .addCasing("32", "Fusion Coil Block", false)
            .addEnergyHatch("1-16", "Specific middle casings on each curve (UV+)", 2)
            .addInputHatch("1+", "Specific top or bottom casings on each side", 1)
            .addOutputHatch("1+", "Specific middle casings on each side", 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
