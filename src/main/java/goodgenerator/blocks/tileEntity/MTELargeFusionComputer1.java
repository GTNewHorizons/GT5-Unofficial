package goodgenerator.blocks.tileEntity;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

import net.minecraft.block.Block;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeFusionComputer1 extends MTELargeFusionComputer {

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

    public MTELargeFusionComputer1(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer1(String name) {
        super(name);
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
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings1;
    }

    @Override
    public int getCasingMeta() {
        return 6;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 0;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 3;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.NaquadahAlloy;
    }

    @Override
    public int extraPara(long startEnergy) {
        return 1;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer1(mName);
    }
}
