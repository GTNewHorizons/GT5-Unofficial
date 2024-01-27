package gregtech.common.tileentities.generators;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BACK;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BACK_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BACK_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BACK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BOTTOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BOTTOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_BOTTOM_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_SIDE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_TOP;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_TOP_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_TOP_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.STEAM_TURBINE_TOP_GLOW;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;

public class GT_MetaTileEntity_SteamTurbine extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public GT_MetaTileEntity_SteamTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Converts Steam into EU", "Base rate: 2L of Steam -> 1 EU" });
        onConfigLoad();
    }

    public GT_MetaTileEntity_SteamTurbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_SteamTurbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SteamTurbine(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 2];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        desc[mDescriptionArray.length] = "Fuel Efficiency: " + (600 / getEfficiency()) + "%";
        desc[mDescriptionArray.length + 1] = String.format(
            "Consumes up to %sL of Steam per second",
            (int) (4000 * (8 * Math.pow(4, mTier) + Math.pow(2, Math.max(mTier - 1, 0))) / (600 / getEfficiency())));
        return desc;
    }

    @Override
    public int getCapacity() {
        return 24000 * this.mTier;
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "SteamTurbine.efficiency.tier." + this.mTier, 6 + this.mTier);
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public long getFuelValue(FluidStack aLiquid, boolean aLong) {
        return getFuelValue(aLiquid);
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        return GT_ModHandler.isAnySteam(aLiquid) ? 3 : 0;
    }

    @Override
    public int consumedFluidPerOperation(FluidStack aLiquid) {
        return this.mEfficiency;
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_FRONT),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_FRONT_GLOW)
                    .glow()
                    .build()),
            OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_BACK),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_BACK_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_BOTTOM),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_BOTTOM_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_TOP),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_TOP_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_SIDE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_SIDE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build()),
            OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_BACK_ACTIVE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_BACK_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_BOTTOM_ACTIVE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_BOTTOM_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_TOP_ACTIVE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_TOP_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
            TextureFactory.of(
                TextureFactory.of(STEAM_TURBINE_SIDE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(STEAM_TURBINE_SIDE_ACTIVE_GLOW)
                    .glow()
                    .build()) };
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        if (GT_ModHandler.isSuperHeatedSteam(aFluid)) {
            aFluid.amount = 0;
            aFluid = null;
            return false;
        }
        return super.isFluidInputAllowed(aFluid);
    }
}
