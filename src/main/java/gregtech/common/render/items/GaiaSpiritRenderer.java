package gregtech.common.render.items;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import gregtech.GT_Mod;
import gregtech.common.render.GT_RenderUtil;

public class GaiaSpiritRenderer extends GT_GeneratedMaterial_Renderer {

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        long animationTicks = GT_Mod.gregtechproxy.getAnimationTicks();
        float partialTicks = GT_Mod.gregtechproxy.getPartialRenderTicks();

        if (shouldModulateColor) {
            Color color = Color.getHSBColor((animationTicks % 360 + partialTicks) % 180 / 180f, 0.4f, 0.9f);
            GL11.glColor3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        }

        GT_RenderUtil.renderItem(type, icon);
    }
}
