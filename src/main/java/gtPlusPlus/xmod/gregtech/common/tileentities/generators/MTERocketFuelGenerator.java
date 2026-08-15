package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.MTERocketFuelGeneratorBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTERocketFuelGenerator extends MTERocketFuelGeneratorBase {

    public MTERocketFuelGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires GT++ Rocket Fuels");
    }

    public MTERocketFuelGenerator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTERocketFuelGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.rocketFuels;
    }

    @Override
    public int getEfficiency() {
        return 80 - (10 * (this.mTier - 4));
    }

    private ITexture getCasingTexture() {
        if (this.mTier <= 4) {
            return TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Top);
        } else if (this.mTier == 5) {
            return TextureFactory.of(TexturesGtBlock.Casing_Machine_Advanced);
        } else {
            return TextureFactory.of(TexturesGtBlock.Casing_Machine_Ultra);
        }
    }

    @Override
    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0], this.getCasingTexture(),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[this.mTier + 1] };
    }

    @Override
    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0], this.getCasingTexture(),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Vent) };
    }

    @Override
    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Redstone_Off) };
    }

    @Override
    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0], this.getCasingTexture(),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal) };
    }

    @Override
    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0], this.getCasingTexture(),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[this.mTier + 1] };
    }

    @Override
    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0], this.getCasingTexture(),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Vent_Fast) };
    }

    @Override
    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Redstone_On) };
    }

    @Override
    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0], this.getCasingTexture(),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal_Active) };
    }
}
