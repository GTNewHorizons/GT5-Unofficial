package tectech.rendering.EOH;

import static tectech.rendering.EOH.EOHRenderingUtils.renderEOHStar;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

public class EOHItemRenderer implements IItemRenderer {

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
        // Hack
        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;
        float time = world.getTotalWorldTime() * 0.2f;

        renderEOHStar(type, time, 0.82);
    }

}
