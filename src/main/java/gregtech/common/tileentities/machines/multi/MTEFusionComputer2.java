package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;

public class MTEFusionComputer2 extends MTEFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION2)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION2_GLOW)
            .extFacing()
            .glow()
            .build());

    public MTEFusionComputer2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFusionComputer2(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 7;
    }

    @Override
    public long maxEUStore() {
        return 320006000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public long capableStartupCanonical() {
        return 320_000_000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFusionComputer2(mName);
    }

    @Override
    public Block getCasing() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 6;
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
        tt.addMachineType("gt.recipe.fusionreactor")
            .addInfo("gt.fusion.tips.a")
            .addInfo("gt.fusion.tips", "8,192", "20M")
            .beginStructureBlock(15, 15, 3, false)
            .addController("Middle center, 2nd layer")
            .addCasing("79-123", "Fusion Machine Casing", false)
            .addCasing("32", "Fusion Coil Block", false)
            .addStructureInfo("gt.fusion.info.2")
            .addStructureInfo("gt.fusion.info.3")
            .addEnergyHatch("1-16", "Specific middle casings on each curve (ZPM+)", 2)
            .addInputHatch("1+", "Specific top or bottom casings on each side", 1)
            .addOutputHatch("1+", "Specific middle casings on each side", 3)
            .addStructureInfo("gt.fusion.info.6", TooltipHelper.voltageText(VoltageIndex.ZPM))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
