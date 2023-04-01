package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.power.FusionPower;

public class GT_MetaTileEntity_FusionComputer2 extends GT_MetaTileEntity_FusionComputer {

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

    public GT_MetaTileEntity_FusionComputer2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 7);
        power = new FusionPower((byte) 7, 320_000_000);
    }

    public GT_MetaTileEntity_FusionComputer2(String aName) {
        super(aName);
        power = new FusionPower((byte) 7, 320_000_000);
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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_FusionComputer2(mName);
    }

    @Override
    public Block getCasing() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 6;
    }

    @Override
    public Block getFusionCoil() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public int getFusionCoilMeta() {
        return 7;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor")
          .addInfo("It's over 9000!!!")
          .addInfo("Controller block for the Fusion Reactor Mk II")
          .addInfo("8192EU/t and 20M EU capacity per Energy Hatch")
          .addInfo("If the recipe has a startup cost greater than the")
          .addInfo("number of energy hatches * cap, you can't do it")
          .addSeparator()
          .beginStructureBlock(15, 3, 15, false)
          .addController("See diagram when placed")
          .addCasingInfoRange("Fusion Machine Casing", 79, 123, false)
          .addStructureInfo("Cover the coils with casing")
          .addOtherStructurePart("Fusion Coil Block", "Center part of the ring")
          .addEnergyHatch("1-16, Specified casings", 2)
          .addInputHatch("2-16, Specified casings", 1)
          .addOutputHatch("1-16, Specified casings", 3)
          .addStructureInfo("ALL Hatches must be ZPM or better")
          .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public int tierOverclock() {
        return 2;
    }
}
