package gregtech.api.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.Item;

import org.jetbrains.annotations.NotNull;

import gregtech.common.render.SBRInventoryContext;
import gregtech.common.render.SBRWorldContext;

/**
 * Holds and manages single instances of {@link SBRInventoryContext} and {@link SBRWorldContext},
 * intended to be reused and reconfigured for each rendering operation.
 * <p>
 * This holder is expected to be used in a thread-confined context (e.g. via
 * ThreadLocal management upstream). No internal synchronization is performed.
 * </p>
 */
public final class SBRContextHolder {

    private final SBRInventoryContext inventoryContext = new SBRInventoryContext();
    private final SBRWorldContext worldContext = new SBRWorldContext();

    /**
     * Returns this holder's {@link SBRInventoryContext} instance configured to
     * render a single {@link Block} in an inventory, using the provided parameters.
     *
     * @param block        the block to render
     * @param meta         the block's metadata value (corresponds to the {@link Item} damage value)
     * @param modelId      the model ID for the block
     * @param renderBlocks the {@link RenderBlocks} renderer to use
     * @return the configured {@link SBRInventoryContext} instance unique to this holder
     */
    public ISBRInventoryContext getSBRInventoryContext(@NotNull Block block, int meta, int modelId,
        @NotNull RenderBlocks renderBlocks) {
        return inventoryContext.setup(block, meta, modelId, renderBlocks);
    }

    /**
     * Returns this holder's {@link SBRWorldContext} instance configured to
     * render a single {@link Block} in world, using the provided parameters.
     *
     * @param x        world X coordinate
     * @param y        world Y coordinate
     * @param z        world Z coordinate
     * @param block    the block to render
     * @param modelId  the Model ID for the block
     * @param renderer the {@link RenderBlocks} renderer to use
     * @return the configured {@link SBRWorldContext} instance unique to this holder
     */
    @SuppressWarnings("MethodWithTooManyParameters") // Blame ISimpleBlockRenderingHandler.renderWorldBlock
    public ISBRWorldContext getSBRWorldContext(int x, int y, int z, @NotNull Block block, int modelId,
        @NotNull RenderBlocks renderer) {
        return worldContext.setup(x, y, z, block, modelId, renderer);
    }
}
