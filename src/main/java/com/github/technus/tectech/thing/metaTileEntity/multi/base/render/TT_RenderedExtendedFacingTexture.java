package com.github.technus.tectech.thing.metaTileEntity.multi.base.render;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GT_RenderedTexture;

public class TT_RenderedExtendedFacingTexture extends GT_RenderedTexture {

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean allowAlpha, boolean glow,
        boolean stdOrient, boolean extFacing) {
        super(aIcon, aRGBa, allowAlpha, glow, stdOrient, extFacing);
    }

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        this(aIcon, aRGBa, aAllowAlpha, false, false, true);
    }

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }
}
