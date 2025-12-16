package gregtech.common.render.items;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.GTMod;
import gregtech.common.GTClient;

public class RainbowOverlayRenderer extends GeneratedMaterialRenderer {

    private float baseR = 1;
    private float baseG = 1;
    private float baseB = 1;

    public RainbowOverlayRenderer(short[] rgba) {
        baseR = rgba[0] / 255.0F;
        baseG = rgba[1] / 255.0F;
        baseB = rgba[2] / 255.0F;
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        final GTClient clientProxy = GTMod.clientProxy();
        long animationTicks = clientProxy.getAnimationTicks();
        float partialTicks = clientProxy.getPartialRenderTicks();

        if (shouldModulateColor) {
            Color color = Color.getHSBColor((animationTicks % 180 + partialTicks) % 90 / 90f, 0.4f, 0.9f);

            float modR = color.getRed() / 255.0F;
            float modG = color.getGreen() / 255.0F;
            float modB = color.getBlue() / 255.0F;

            // Multiply base color with modifier
            GL11.glColor3f(baseR * modR, baseG * modG, baseB * modB);
        }

        ItemRenderUtil.renderItem(type, icon);
    }
}
