package gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.common.render.GTSidedTextureRender;

/**
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GTSidedTexture extends GTSidedTextureRender implements ITexture, IColorModulationContainer {

    @Deprecated
    public short[] mRGBa;

    public GTSidedTexture(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3,
        IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon0, aIcon1, aIcon2, aIcon3, aIcon4, aIcon5, aRGBa, aAllowAlpha);

        // Backwards Compat
        GTSidedTexture.this.mRGBa = aRGBa;
    }

    public GTSidedTexture(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3,
        IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa) {
        this(aIcon0, aIcon1, aIcon2, aIcon3, aIcon4, aIcon5, aRGBa, true);
    }

    public GTSidedTexture(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3,
        IIconContainer aIcon4, IIconContainer aIcon5) {
        this(aIcon0, aIcon1, aIcon2, aIcon3, aIcon4, aIcon5, Dyes._NULL.mRGBa);
    }

    public GTSidedTexture(IIconContainer aBottom, IIconContainer aTop, IIconContainer aSides, short[] aRGBa) {
        this(aBottom, aTop, aSides, aSides, aSides, aSides, aRGBa);
    }

    public GTSidedTexture(IIconContainer aBottom, IIconContainer aTop, IIconContainer aSides) {
        this(aBottom, aTop, aSides, Dyes._NULL.mRGBa);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
