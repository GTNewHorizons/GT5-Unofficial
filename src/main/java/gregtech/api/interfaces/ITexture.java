package gregtech.api.interfaces;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import gregtech.api.render.SBRContextBase;

public interface ITexture {

    void renderXPos(SBRContextBase<? extends SBRContextBase<?>> ctx);

    void renderXNeg(SBRContextBase<? extends SBRContextBase<?>> ctx);

    void renderYPos(SBRContextBase<? extends SBRContextBase<?>> ctx);

    void renderYNeg(SBRContextBase<? extends SBRContextBase<?>> ctx);

    void renderZPos(SBRContextBase<? extends SBRContextBase<?>> ctx);

    void renderZNeg(SBRContextBase<? extends SBRContextBase<?>> ctx);

    boolean isValidTexture();

    /**
     * Will initialize the {@link Tessellator} if rendering off-world (Inventory)
     *
     * @param aRenderer The {@link RenderBlocks} Renderer
     * @param aNormalX  The X Normal for current Quad Face
     * @param aNormalY  The Y Normal for current Quad Face
     * @param aNormalZ  The Z Normal for current Quad Face
     */
    default void startDrawingQuads(RenderBlocks aRenderer, float aNormalX, float aNormalY, float aNormalZ) {
        if (aRenderer.useInventoryTint) {
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
        if (aRenderer.useInventoryTint) {
            Tessellator.instance.draw();
        }
    }
}
