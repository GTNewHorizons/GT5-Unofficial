package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

import net.minecraft.block.Block;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;

@IMetaTileEntity.SkipGenerateDescription
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
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }
}
