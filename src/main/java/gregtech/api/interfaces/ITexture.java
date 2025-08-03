package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import gregtech.api.util.LightingHelper;

public interface ITexture {

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderXPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderXNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderYPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderYNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderZPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    @SuppressWarnings("MethodWithTooManyParameters")
    void renderZNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass);

    boolean isValidTexture();

    /**
     * @return {@code true} if this texture is from the old package
     */
    default boolean isOldTexture() {
        return true;
    }

    /**
     * Will initialize the {@link Tessellator} if rendering off-world (Inventory)
     *
     * @param aRenderer The {@link RenderBlocks} Renderer
     * @param aNormalX  The X Normal for current Quad Face
     * @param aNormalY  The Y Normal for current Quad Face
     * @param aNormalZ  The Z Normal for current Quad Face
     */
    default void startDrawingQuads(RenderBlocks aRenderer, float aNormalX, float aNormalY, float aNormalZ) {
        if (aRenderer.useInventoryTint && !isOldTexture()) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            tess.setNormal(aNormalX, aNormalY, aNormalZ);
        }
    }

    /**
     * Will run the {@link Tessellator} to draw Quads if rendering off-world (Inventory)
     *
     * @param aRenderer The {@link RenderBlocks} Renderer
     */
    default void draw(RenderBlocks aRenderer) {
        if (aRenderer.useInventoryTint && !isOldTexture()) {
            Tessellator.instance.draw();
        }
    }
}
