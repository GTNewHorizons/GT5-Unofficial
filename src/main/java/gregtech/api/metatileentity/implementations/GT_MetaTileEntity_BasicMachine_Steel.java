package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEEL_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.common.power.Power;
import gregtech.common.power.SteamPower;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public abstract class GT_MetaTileEntity_BasicMachine_Steel extends GT_MetaTileEntity_BasicMachine_Bronze {
    public GT_MetaTileEntity_BasicMachine_Steel(
            int aID,
            String aName,
            String aNameRegional,
            String aDescription,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aHighPressure) {
        super(aID, aName, aNameRegional, aDescription, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(
            String aName,
            String aDescription,
            ITexture[][][] aTextures,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aHighPressure) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(
            String aName,
            String[] aDescription,
            ITexture[][][] aTextures,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aHighPressure) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aHighPressure);
    }

    @Override
    public Power buildPower() {
        return new SteamPower(mTier, 2, 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa))
        };
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            TextureFactory.of(
                    isBricked() ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE,
                    Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
            TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    protected IDrawable getGregTechLogo() {
        return GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM.get(SteamTexture.Variant.STEEL);
    }

    @Override
    protected UITexture getBackground() {
        return GT_UITextures.BACKGROUND_STEAM.get(SteamTexture.Variant.STEEL);
    }

    @Override
    protected IDrawable getSlotBackground() {
        return GT_UITextures.SLOT_ITEM_STEAM.get(SteamTexture.Variant.STEEL);
    }

    @Override
    protected GT_GuiTabIconSet getTabIconSet() {
        return new GT_GuiTabIconSet(
                GT_UITextures.TAB_COVER_STEAM_NORMAL.get(SteamTexture.Variant.STEEL),
                GT_UITextures.TAB_COVER_STEAM_HIGHLIGHT.get(SteamTexture.Variant.STEEL),
                GT_UITextures.TAB_COVER_STEAM_DISABLED.get(SteamTexture.Variant.STEEL));
    }
}
