package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Made this to get rid of deprecation warnings <i>everywhere</i>.
 * 
 * @author Alkalus
 *
 */
@SuppressWarnings("deprecation")
public class GTPP_RenderedTexture extends GT_RenderedTexture {

    public GTPP_RenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon, aRGBa, aAllowAlpha);
    }

    public GTPP_RenderedTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public GTPP_RenderedTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }
}
