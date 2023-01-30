package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

public interface ITexture {

    void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

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
            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(aNormalX, aNormalY, aNormalZ);
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
