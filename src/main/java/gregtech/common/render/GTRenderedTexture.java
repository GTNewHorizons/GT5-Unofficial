package gregtech.common.render;

import static gregtech.api.util.LightingHelper.MAX_BRIGHTNESS;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector2f;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.GTMod;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.LightingHelper;

public class GTRenderedTexture extends GTTextureBase implements ITexture, IColorModulationContainer {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean stdOrient;
    private final boolean useExtFacing;

    protected GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean allowAlpha, boolean glow,
        boolean stdOrient, boolean extFacing) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTRenderedTexture");
        mIconContainer = aIcon;
        mRGBa = aRGBa;
        this.glow = glow;
        this.stdOrient = stdOrient;
        this.useExtFacing = extFacing;
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 1.0f, 0.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            if (enableAO) lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingXPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.EAST, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.EAST, 0xffffff);
            renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, -1.0f, 0.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingXNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.WEST, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.WEST, 0xffffff);
            renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 1.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingYPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.UP, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.UP, 0xffffff);
            renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, -1.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingYNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.DOWN, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(255, 255, 255, 255);
            renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, 1.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingZPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.SOUTH, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.SOUTH, 0xffffff);
            renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, -1.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GTMod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingZNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.NORTH, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.NORTH, 0xffffff);
            renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public boolean isValidTexture() {
        return mIconContainer != null;
    }

    private static class TexCoords {

        public float u1, u2, v1, v2;

        public TexCoords(IIcon icon) {
            u1 = icon.getMinU();
            u2 = icon.getMaxU();
            v1 = icon.getMinV();
            v2 = icon.getMaxV();
        }

        public void flipU() {
            float temp = u1;
            u1 = u2;
            u2 = temp;
        }

        public void flipV() {
            float temp = v1;
            v1 = v2;
            v2 = temp;
        }

        public Vector2f[] toUVs() {
            // spotless:off
            return new Vector2f[] {
                new Vector2f(u2, v1),
                new Vector2f(u1, v1),
                new Vector2f(u1, v2),
                new Vector2f(u2, v2)
            };
            // spotless:on
        }
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex() + 3;

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        double x0 = x + 0;
        double x1 = x + 1;
        double y0 = y + 0;
        double z0 = z + 0;
        double z1 = z + 1;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z1, uv.x, uv.y);
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex() + 3;

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        double x0 = x + 0;
        double x1 = x + 1;
        double y1 = y + 1;
        double z0 = z + 0;
        double z1 = z + 1;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z1, uv.x, uv.y);
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex();

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        double x0 = x + 0;
        double x1 = x + 1;
        double y0 = y + 0;
        double y1 = y + 1;
        double z0 = z + 0;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z0, uv.x, uv.y);
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex() + 1;

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        double x0 = x + 0;
        double x1 = x + 1;
        double y0 = y + 0;
        double y1 = y + 1;
        double z1 = z + 1;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z1, uv.x, uv.y);
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex();

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        double x0 = x + 0;
        double y0 = y + 0;
        double y1 = y + 1;
        double z0 = z + 0;
        double z1 = z + 1;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x0, y0, z1, uv.x, uv.y);
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        TexCoords texCoords = new TexCoords(icon);

        int rotation = extendedFacing.getRotation()
            .getIndex() + 2;

        if (extendedFacing.getFlip()
            .isHorizontallyFlipped()) {
            if (rotation % 2 == 1) {
                texCoords.flipV();
            } else {
                texCoords.flipU();
            }
        }

        if (extendedFacing.getFlip()
            .isVerticallyFliped()) {
            if (rotation % 2 == 1) {
                texCoords.flipU();
            } else {
                texCoords.flipV();
            }
        }

        double x1 = x + 1;
        double y0 = y + 0;
        double y1 = y + 1;
        double z0 = z + 0;
        double z1 = z + 1;

        Tessellator tessellator = Tessellator.instance;

        Vector2f[] uvs = texCoords.toUVs();
        Vector2f uv;

        if (aRenderer.enableAO) {
            tessellator
                .setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
        }

        uv = uvs[(0 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z1, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomLeft,
                aRenderer.colorGreenBottomLeft,
                aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
        }

        uv = uvs[(1 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y0, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedBottomRight,
                aRenderer.colorGreenBottomRight,
                aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
        }

        uv = uvs[(2 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z0, uv.x, uv.y);

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(
                aRenderer.colorRedTopRight,
                aRenderer.colorGreenTopRight,
                aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
        }

        uv = uvs[(3 + rotation) % 4];
        tessellator.addVertexWithUV(x1, y1, z1, uv.x, uv.y);
    }

    private ExtendedFacing getExtendedFacing(int x, int y, int z) {
        if (stdOrient || !useExtFacing) return ExtendedFacing.DEFAULT;

        final EntityPlayer player = GTMod.gregtechproxy.getThePlayer();
        if (player == null) return ExtendedFacing.DEFAULT;

        final World w = player.getEntityWorld();
        if (w == null) return ExtendedFacing.DEFAULT;

        final TileEntity te = w.getTileEntity(x, y, z);

        IAlignment alignment = null;

        if (te instanceof IGregTechTileEntity) {
            final IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();

            if (meta instanceof IAlignmentProvider) {
                alignment = ((IAlignmentProvider) meta).getAlignment();
            } else if (meta != null) {
                return ExtendedFacing.of(
                    meta.getBaseMetaTileEntity()
                        .getFrontFacing());
            }
        } else if (te instanceof IAlignmentProvider) {
            alignment = ((IAlignmentProvider) te).getAlignment();
        }

        if (alignment != null) return alignment.getExtendedFacing();

        return ExtendedFacing.DEFAULT;
    }
}
