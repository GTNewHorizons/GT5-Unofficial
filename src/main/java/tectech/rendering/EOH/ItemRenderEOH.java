package tectech.rendering.EOH;

import static tectech.rendering.EOH.EOHRenderingUtils.renderEOHStar;

import gregtech.common.GTClient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderEOH implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        renderEOHStar(type, GTClient.getAnimationRenderTicks(), 0.82);
    }

}
