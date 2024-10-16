package gregtech.common.tileentities.machines.steam;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineSteel;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTESteamExtractorSteel extends MTEBasicMachineSteel {

    public MTESteamExtractorSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Extracting your first Rubber", 1, 1, true);
    }

    public MTESteamExtractorSteel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamExtractorSteel(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.extractorRecipes;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_EXTRACTOR_OP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_SIDE_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_SIDE_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_SIDE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_SIDE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_FRONT_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_FRONT_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_FRONT")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_FRONT_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_TOP_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_TOP_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_TOP")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_TOP_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_BOTTOM_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_BOTTOM")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/extractor/OVERLAY_BOTTOM_GLOW"))
                .glow()
                .build() };
    }
}
