package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEFusionComputer1 extends MTEFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1_GLOW)
            .extFacing()
            .glow()
            .build());

    public MTEFusionComputer1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFusionComputer1(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 6;
    }

    @Override
    public long maxEUStore() {
        return 160003000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public long capableStartupCanonical() {
        return 160_000_000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFusionComputer1(mName);
    }

    @Override
    public Block getCasing() {
        return GregTechAPI.sBlockCasings1;
    }

    @Override
    public int getCasingMeta() {
        return 6;
    }

    @Override
    public Block getFusionCoil() {
        return GregTechAPI.sBlockCasings1;
    }

    @Override
    public int getFusionCoilMeta() {
        return 15;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fusion Reactor")
            .addInfo("It's over 9000!!!")
            .addInfo("Controller block for the Fusion Reactor Mk I")
            .addInfo("2048EU/t and 10M EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .addSeparator()
            .beginStructureBlock(15, 3, 15, false)
            .addController("See diagram when placed")
            .addCasingInfoRange("LuV Machine Casing", 79, 123, false)
            .addStructureInfo("Cover the coils with casing")
            .addOtherStructurePart("Superconducting Coil Block", "Center part of the ring")
            .addEnergyHatch("1-16, Specified casings", 2)
            .addInputHatch("2-16, Specified casings", 1)
            .addOutputHatch("1-16, Specified casings", 3)
            .addStructureInfo("ALL Hatches must be LuV or better")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
