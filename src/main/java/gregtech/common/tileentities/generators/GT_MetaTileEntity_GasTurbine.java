package gregtech.common.tileentities.generators;

import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BACK;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BACK_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BACK_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BACK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BOTTOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BOTTOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_BOTTOM_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_SIDE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_SIDE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_SIDE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_TOP;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_TOP_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_TOP_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.GAS_TURBINE_TOP_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_GasTurbine extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public GT_MetaTileEntity_GasTurbine(int aID, String aName, String aNameRegional, int aTier, int mEfficiency) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Requires flammable Gasses",
                "Causes "
                    + (int) (GT_Mod.gregtechproxy.mPollutionBaseGasTurbinePerSecond
                        * GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier[aTier])
                    + " Pollution per second" });
        this.mEfficiency = mEfficiency;
    }

    public GT_MetaTileEntity_GasTurbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        int mEfficiency) {
        super(aName, aTier, aDescription, aTextures);
        this.mEfficiency = mEfficiency;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_GasTurbine(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mEfficiency);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.gasTurbineFuels;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_FRONT),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_FRONT_GLOW)
                    .glow()
                    .build()),
            OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_BACK),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_BACK_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_BOTTOM),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_BOTTOM_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_TOP),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_TOP_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_SIDE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_SIDE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build()),
            OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_BACK_ACTIVE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_BACK_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_BOTTOM_ACTIVE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_BOTTOM_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_TOP_ACTIVE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_TOP_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(GAS_TURBINE_SIDE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(GAS_TURBINE_SIDE_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public int getPollution() {
        return (int) (GT_Mod.gregtechproxy.mPollutionBaseGasTurbinePerSecond
            * GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier[mTier]);
    }
}
