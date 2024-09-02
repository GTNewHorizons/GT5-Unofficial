package gregtech.api.objects;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.LightingHelper;

/**
 * This ITexture implementation extends the GT_RenderedTexture class to render with bottom side flipped as with dumb
 * blocks rendering. It is used in Ore blocks rendering so they better blends with dumb block ores from vanilla or other
 * mods, when seen from bottom.
 *
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GTStdRenderedTexture extends GTRenderedTexture {

    @SuppressWarnings("unused")
    public GTStdRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon, aRGBa, aAllowAlpha);
    }

    public GTStdRenderedTexture(IIconContainer aIcon, short[] aRGBa) {
        super(aIcon, aRGBa, true);
    }

    @SuppressWarnings("unused")
    public GTStdRenderedTexture(IIconContainer aIcon) {
        super(aIcon, Dyes._NULL.mRGBa);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        LightingHelper lighting = new LightingHelper(aRenderer);
        lighting.setupLightingYNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.DOWN, mRGBa);
        aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, mIconContainer.getIcon());
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.DOWN, 0xffffff);
            aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, mIconContainer.getOverlayIcon());
        }
    }
}
