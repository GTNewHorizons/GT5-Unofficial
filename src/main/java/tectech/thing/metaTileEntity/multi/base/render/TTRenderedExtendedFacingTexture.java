package tectech.thing.metaTileEntity.multi.base.render;

import net.minecraft.block.Block;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GTRenderedTexture;

public class TTRenderedExtendedFacingTexture extends GTRenderedTexture {

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean allowAlpha, boolean glow,
        boolean stdOrient, boolean extFacing, Block matBlock, int matMeta) {
        super(aIcon, aRGBa, allowAlpha, glow, stdOrient, extFacing, matBlock, matMeta);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        this(aIcon, aRGBa, aAllowAlpha, false, false, true, null, 0);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public TTRenderedExtendedFacingTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }
}
