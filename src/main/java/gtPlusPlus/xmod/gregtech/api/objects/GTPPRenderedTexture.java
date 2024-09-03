package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GTRenderedTexture;

/**
 * Made this to get rid of deprecation warnings <i>everywhere</i>.
 *
 * @author Alkalus
 *
 */
@SuppressWarnings("deprecation")
public class GTPPRenderedTexture extends GTRenderedTexture {

    public GTPPRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon, aRGBa, aAllowAlpha);
    }

    public GTPPRenderedTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public GTPPRenderedTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }
}
