package gregtech.api.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

public interface ISBRInventoryContext extends ISBRContext {

    ISBRInventoryContext setup(@NotNull Block block, int meta, int modelId, @NotNull RenderBlocks renderer);

    @Override
    ISBRInventoryContext reset();

    @Override
    ISBRInventoryContext setupColor(ForgeDirection side, int hexColor);

    int getMeta();
}
