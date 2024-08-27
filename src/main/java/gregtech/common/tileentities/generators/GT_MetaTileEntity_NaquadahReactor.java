package gregtech.common.tileentities.generators;

import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_GLOW;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_NaquadahReactor extends GT_MetaTileEntity_BasicGenerator {

    private int mEfficiency;

    public GT_MetaTileEntity_NaquadahReactor(int aID, String aName, String[] aDescription, String aNameRegional,
        int aTier) {
        super(aID, aName, aNameRegional, aTier, aDescription);
        if (aTier > 8 || aTier < 4) {
            new Exception("Tier without Recipe Map!").printStackTrace();
        }
        mEfficiency = getBaseEff();
    }

    public GT_MetaTileEntity_NaquadahReactor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        if (aTier > 8 || aTier < 4) {
            new Exception("Tier without Recipe Map!").printStackTrace();
        }
        mEfficiency = getBaseEff();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return false;
        return ((side.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0)
            && (side != getBaseMetaTileEntity().getFrontFacing())
            && (side != getBaseMetaTileEntity().getBackFacing());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_NaquadahReactor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        RecipeMap<?> ret;
        switch (mTier) {
            case 4 -> ret = RecipeMaps.smallNaquadahReactorFuels;
            case 5 -> ret = RecipeMaps.largeNaquadahReactorFuels;
            case 6 -> ret = RecipeMaps.hugeNaquadahReactorFuels;
            case 7 -> ret = RecipeMaps.extremeNaquadahReactorFuels;
            case 8 -> ret = RecipeMaps.ultraHugeNaquadahReactorFuels;
            default -> ret = null;
        }
        return ret;
    }

    @Override
    public int getCapacity() {
        return getRecipeMap() != null ? getRecipeMap().getBackend()
            .getProperties().minFluidInputs > 0 ? 8000 * (mTier + 1) : 0 : 0;
    }

    @Override
    public int getEfficiency() {
        return mEfficiency;
    }

    private int getBaseEff() {
        return mTier == 4 ? 80 : 100 + (50 * (mTier - 5));
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(NAQUADAH_REACTOR_SOLID_FRONT),
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(NAQUADAH_REACTOR_SOLID_BACK),
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_BACK_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(NAQUADAH_REACTOR_SOLID_BOTTOM),
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_BOTTOM_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(NAQUADAH_REACTOR_SOLID_TOP),
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_TOP_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(NAQUADAH_REACTOR_SOLID_SIDE),
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_SIDE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0], TextureFactory.of(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE),
            TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0], TextureFactory.of(NAQUADAH_REACTOR_SOLID_BACK_ACTIVE),
            TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_BACK_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE), TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0], TextureFactory.of(NAQUADAH_REACTOR_SOLID_TOP_ACTIVE),
            TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_TOP_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0], TextureFactory.of(NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE),
            TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public int getPollution() {
        return 0;
    }
}
