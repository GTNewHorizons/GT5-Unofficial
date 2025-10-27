package gregtech.common.tileentities.generators;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;

public class MTEPlasmaGenerator extends MTEBasicGenerator {

    public MTEPlasmaGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Plasma into energy");
    }

    public MTEPlasmaGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS),
            OVERLAYS_ENERGY_OUT[mTier + 1] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                .glow()
                .build(),
            OVERLAYS_ENERGY_OUT[mTier + 1] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                .glow()
                .build() };
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.plasmaFuels;
    }

    @Override
    public int getEfficiency() {
        return Math.max(10, 10 + Math.min(90, this.mTier * 10));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPlasmaGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getPollution() {
        return 0;
    }
}
