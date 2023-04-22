package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.render.TextureFactory;
import gregtech.common.power.Power;
import gregtech.common.power.SteamPower;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public abstract class GT_MetaTileEntity_BasicMachine_Steel extends GT_MetaTileEntity_BasicMachine_Bronze
    implements IGetTitleColor {

    public GT_MetaTileEntity_BasicMachine_Steel(int aID, String aName, String aNameRegional, String aDescription,
        int aInputSlotCount, int aOutputSlotCount, boolean aHighPressure) {
        super(aID, aName, aNameRegional, aDescription, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(String aName, String aDescription, ITexture[][][] aTextures,
        int aInputSlotCount, int aOutputSlotCount, boolean aHighPressure) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(String aName, String[] aDescription, ITexture[][][] aTextures,
        int aInputSlotCount, int aOutputSlotCount, boolean aHighPressure) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    @Override
    public Power buildPower() {
        return new SteamPower(mTier, 2, 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.STEEL;
    }

    @Override
    public int getTitleColor() {
        return COLOR_TITLE_WHITE.get();
    }
}
