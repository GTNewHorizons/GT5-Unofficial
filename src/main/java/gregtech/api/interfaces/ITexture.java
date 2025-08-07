package gregtech.api.interfaces;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import gregtech.api.render.SBRContext;

public interface ITexture {

    void renderXPos(SBRContext ctx);

    void renderXNeg(SBRContext ctx);

    void renderYPos(SBRContext ctx);

    void renderYNeg(SBRContext ctx);

    void renderZPos(SBRContext ctx);

    void renderZNeg(SBRContext ctx);

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
