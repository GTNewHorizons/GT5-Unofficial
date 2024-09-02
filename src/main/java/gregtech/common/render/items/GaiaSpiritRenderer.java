package gregtech.common.render.items;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import gregtech.GTMod;
import gregtech.common.render.GTRenderUtil;

public class GaiaSpiritRenderer extends GeneratedMaterialRenderer {

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        long animationTicks = GTMod.gregtechproxy.getAnimationTicks();
        float partialTicks = GTMod.gregtechproxy.getPartialRenderTicks();

        if (shouldModulateColor) {
            Color color = Color.getHSBColor((animationTicks % 360 + partialTicks) % 180 / 180f, 0.4f, 0.9f);
            GL11.glColor3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        }

        GTRenderUtil.renderItem(type, icon);
    }
}
