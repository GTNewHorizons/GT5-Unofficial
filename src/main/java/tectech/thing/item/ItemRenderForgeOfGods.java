package tectech.thing.item;

import static tectech.rendering.EOH.EOHRenderingUtils.renderGORGEStar;

import gregtech.common.GTClient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderForgeOfGods implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item,
        IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        renderGORGEStar(type, GTClient.getAnimationRenderTicks(), 0.82);
    }
}
