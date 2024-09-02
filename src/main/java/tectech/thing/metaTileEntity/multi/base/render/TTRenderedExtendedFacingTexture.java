package tectech.thing.metaTileEntity.multi.base.render;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GTRenderedTexture;

public class TTRenderedExtendedFacingTexture extends GTRenderedTexture {

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean allowAlpha, boolean glow,
        boolean stdOrient, boolean extFacing) {
        super(aIcon, aRGBa, allowAlpha, glow, stdOrient, extFacing);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        this(aIcon, aRGBa, aAllowAlpha, false, false, true);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }
}
