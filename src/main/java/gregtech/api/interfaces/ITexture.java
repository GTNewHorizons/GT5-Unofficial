package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.common.util.ForgeDirection;

public interface ITexture {

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
    void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
    void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
    void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
    void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
    void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    /**
     * Renders the {@link Block}'s face at the given axis sign
     *
     * @param aRenderer the Minecraft {@link RenderBlocks} renderer
     * @param aBlock    the {@link Block} to render
     * @param aX        the x coordinate of the {@link Block}
     * @param aY        y
     * @param aZ        z
     *
     * @deprecated use {@link #render(RenderBlocks, Block, int, int, int, ForgeDirection)} instead
     */
    @Deprecated
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

    /**
     * Renders the {@link Block}'s face at the given {@link ForgeDirection}
     *
     * @param renderer  the Minecraft {@link RenderBlocks} renderer
     * @param block     the {@link Block} to render
     * @param x         the x coordinate of the {@link Block}
     * @param y         y
     * @param z         z
     * @param direction the {@link ForgeDirection} of the face
     */
    @SuppressWarnings("MethodWithTooManyParameters")
    default void render(RenderBlocks renderer, Block block, int x, int y, int z, ForgeDirection direction) {
        switch (direction) {
            case DOWN -> renderYNeg(renderer, block, x, y, z);
            case UP -> renderYPos(renderer, block, x, y, z);
            case NORTH -> renderZNeg(renderer, block, x, y, z);
            case SOUTH -> renderZPos(renderer, block, x, y, z);
            case WEST -> renderXNeg(renderer, block, x, y, z);
            case EAST -> renderXPos(renderer, block, x, y, z);
        }
    }
}
