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

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class MTESteamTurbine extends MTEBasicGenerator {

    public MTESteamTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, (String) null);
    }

    public MTESteamTurbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamTurbine(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Converts Steam into EU", "Base rate: 2L of Steam -> 1 EU",
            "Fuel Efficiency: " + addFormattedString(String.valueOf(600 / getEfficiency())) + "%%",
            "Consumes up to " + addFormattedString(
                String.valueOf(
                    (int) (4000 * (8 * GTUtility.powInt(4, mTier) + GTUtility.powInt(2, Math.max(mTier - 1, 0)))
                        / (600 / getEfficiency()))))
                + "L of Steam per second" };
    }

    @Override
    public int getCapacity() {
        return 24000 * this.mTier;
    }

    @Override
    public int getEfficiency() {
        return 6 + this.mTier;
    }

    @Override
    public long getFuelValue(FluidStack aLiquid, boolean aLong) {
        return getFuelValue(aLiquid);
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        return GTModHandler.isAnySteam(aLiquid) ? 3 : 0;
    }

    @Override
    public int consumedFluidPerOperation(FluidStack aLiquid) {
        return this.getEfficiency();
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
            OVERLAYS_ENERGY_OUT[this.mTier + 1] };
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
            OVERLAYS_ENERGY_OUT[this.mTier + 1] };
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
        if (GTModHandler.isSuperHeatedSteam(aFluid)) {
            aFluid.amount = 0;
            return false;
        }
        return super.isFluidInputAllowed(aFluid);
    }
}
