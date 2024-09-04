package gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;

@Deprecated
public class GTRenderedTexture extends gregtech.common.render.GTRenderedTexture
    implements ITexture, IColorModulationContainer {

    @Deprecated
    public short[] mRGBa;

    public GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon, aRGBa, aAllowAlpha, false, true, false);
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
        mRGBa = aRGBa;
    }

    public GTRenderedTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public GTRenderedTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
