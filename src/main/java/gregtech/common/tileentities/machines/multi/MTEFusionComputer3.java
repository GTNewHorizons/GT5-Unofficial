package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEFusionComputer3 extends MTEFusionComputer {

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
        tt.addMachineType("Fusion Reactor")
            .addInfo("A SUN DOWN ON EARTH")
            .addInfo("§b32,768§7 EU/t and §b40M§7 EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .beginStructureBlock(15, 15, 3, false)
            .addController("See diagram when placed")
            .addCasing("79-123", "Fusion Machine Casing Mk-II", false)
            .addCasing("32", "Fusion Coil Block", false)
            .addEnergyHatch("1-16", "Specified casings (UV+)", 2)
            .addInputHatch("1+", "Specified casings", 1)
            .addOutputHatch("1+", "Specified casings", 3)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
