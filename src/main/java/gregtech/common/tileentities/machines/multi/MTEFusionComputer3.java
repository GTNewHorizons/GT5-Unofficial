package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;

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
        tt.addMachineType("gt.recipe.fusionreactor")
            .addInfo("gt.fusion.tips.b")
            .addInfo("gt.fusion.tips", "32,768", "40M")
            .beginStructureBlock(15, 3, 15, false)
            .addController("gt.fusion.info.1")
            .addCasingInfoRange(
                ItemList.Casing_Fusion2.get(1)
                    .getDisplayName(),
                79,
                123,
                false)
            .addStructureInfo("gt.fusion.info.2")
            .addStructurePart(
                ItemList.Casing_Coil_Superconductor.get(1)
                    .getDisplayName(),
                "gt.fusion.info.3")
            .addEnergyHatch("gt.fusion.info.4", 2)
            .addInputHatch("gt.fusion.info.5", 1)
            .addOutputHatch("gt.fusion.info.4", 3)
            .addStructureInfo("gt.fusion.info.6", TooltipHelper.voltageText(VoltageIndex.UV))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
