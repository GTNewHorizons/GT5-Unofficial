package gregtech.common.render.items;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.GTMod;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.common.GTClient;
import gregtech.common.render.GTRenderUtil;

public class RainbowOverlayMetaItemRenderer implements IItemRenderer {

    private float baseR = 1;
    private float baseG = 1;
    private float baseB = 1;

    public RainbowOverlayMetaItemRenderer(short[] rgba) {
        baseR = rgba[0] / 255.0F;
        baseG = rgba[1] / 255.0F;
        baseB = rgba[2] / 255.0F;
    }

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;

    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);

    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        GL11.glPushMatrix();

        if (item.getItem() instanceof MetaGeneratedItem mgItem) {
            IIcon[] icons = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
            if (icons != null && icons.length > 0 && icons[0] != null) {

                final GTClient clientProxy = GTMod.clientProxy();
                long animationTicks = clientProxy.getAnimationTicks();
                float partialTicks = clientProxy.getPartialRenderTicks();

                Color color = Color.getHSBColor((animationTicks % 180 + partialTicks) % 90 / 90f, 0.4f, 0.9f);

                float modR = color.getRed() / 255.0F;
                float modG = color.getGreen() / 255.0F;
                float modB = color.getBlue() / 255.0F;

                // Multiply base color with modifier
                GL11.glColor3f(baseR * modR, baseG * modG, baseB * modB);

                GTRenderUtil.renderItem(type, icons[0]);

            }
        }

        GL11.glPopMatrix();
    }

}
