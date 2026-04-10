package tectech.thing.metaTileEntity.multi.base.render;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GTRenderedTexture;

public class TTRenderedExtendedFacingTexture extends GTRenderedTexture {

    // called from hydroenergy
    public TTRenderedExtendedFacingTexture(IIconContainer aIcon) {
        super(aIcon, Dyes._NULL.getRGBA(), false, true);
    }
}
