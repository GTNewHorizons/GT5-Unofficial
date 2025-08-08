package gregtech.common.tileentities.machines.steam;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_COMPRESSOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_COMPRESSOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_COMPRESSOR_GLOW;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineSteel;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTESteamCompressorSteel extends MTEBasicMachineSteel {

    public MTESteamCompressorSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Compressing Items", 1, 1, true);
    }

    public MTESteamCompressorSteel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamCompressorSteel(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_COMPRESSOR_OP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_SIDE_STEAM_COMPRESSOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_COMPRESSOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_SIDE_STEAM_COMPRESSOR), TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_COMPRESSOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_COMPRESSOR), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_COMPRESSOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_TOP_STEAM_COMPRESSOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_COMPRESSOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_TOP_STEAM_COMPRESSOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_COMPRESSOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_COMPRESSOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_COMPRESSOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_COMPRESSOR), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_COMPRESSOR_GLOW)
                .glow()
                .build() };
    }
}
