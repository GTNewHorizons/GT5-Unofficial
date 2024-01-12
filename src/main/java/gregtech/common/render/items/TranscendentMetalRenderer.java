package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.TextureUtils;
import gregtech.GT_Mod;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GT_Util;

public class TranscendentMetalRenderer extends GT_GeneratedMaterial_Renderer {

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        if (type == ItemRenderType.ENTITY) {
            // Pretend fancy graphics is enabled
            if (!Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                if (RenderItem.renderInFrame) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }
                // Magic numbers calculated from vanilla code
                GL11.glTranslatef(-0.5F, -0.25F, 0.0421875F);
            }
        }
        super.renderItem(type, aStack, data);
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack itemStack, IIcon icon,
        boolean shouldModulateColor) {

        if (!(itemStack.getItem() instanceof final IGT_ItemWithMaterialRenderer itemRenderer)) return;

        GL11.glPushMatrix();
        applyEffect(type, itemRenderer.getRGBa(itemStack), shouldModulateColor);

        if (shouldModulateColor) {
            short[] tModulation = itemRenderer.getRGBa(itemStack);
            GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
        }

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glScalef(16, 16, 32);
        }
        ItemRenderer.renderItemIn2D(
            Tessellator.instance,
            icon.getMaxU(),
            icon.getMinV(),
            icon.getMinU(),
            icon.getMaxV(),
            icon.getIconWidth(),
            icon.getIconHeight(),
            0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    protected void renderContainedFluid(ItemRenderType type, FluidStack fluidStack, IIcon fluidIcon) {
        GL11.glPushMatrix();

        Fluid fluid = fluidStack.getFluid();
        applyEffect(type, GT_Util.getRGBaArray(fluid.getColor()), true);

        TextureUtils.bindAtlas(fluid.getSpriteNumber());
        GL11.glDepthFunc(GL11.GL_EQUAL);
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glScalef(16, 16, 32);
        }

        ItemRenderer.renderItemIn2D(
            Tessellator.instance,
            fluidIcon.getMaxU(),
            fluidIcon.getMinV(),
            fluidIcon.getMinU(),
            fluidIcon.getMaxV(),
            fluidIcon.getIconWidth(),
            fluidIcon.getIconHeight(),
            0.0625F);

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPopMatrix();
    }

    @Override
    protected void renderItemOverlay(ItemRenderType type, IIcon overlay) {
        GL11.glPushMatrix();
        applyEffect(type, null, false);

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glScalef(16, 16, 32);
        }
        ItemRenderer.renderItemIn2D(
            Tessellator.instance,
            overlay.getMaxU(),
            overlay.getMinV(),
            overlay.getMinU(),
            overlay.getMaxV(),
            overlay.getIconWidth(),
            overlay.getIconHeight(),
            0.0625F);

        GL11.glPopMatrix();
    }

    private void applyEffect(ItemRenderType type, short[] modulation, boolean shouldModulateColor) {
        if (RenderItem.renderInFrame) {
            // Float in front of item frame
            GL11.glTranslatef(0.0f, 0.0f, -0.5f);
        }

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glTranslatef(8f, 8f, 0f);
        } else {
            GL11.glTranslatef(0.5f, 0.5f, 0.0f);
        }

        GL11.glRotatef((GT_Mod.gregtechproxy.getAnimationTicks() * 3.5f) % 360, 0.3f, 0.5f, 0.2f);
        GL11.glRotatef(180, 0.5f, 0.0f, 0.0f);

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glTranslatef(-8f, -8f, 0f);
        } else {
            GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        }

        // Center on point of rotation
        GL11.glTranslatef(0.0f, 0.0f, 0.03125F);

        if (shouldModulateColor) {
            GL11.glColor4f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F, 255);
        } else {
            GL11.glColor4f(1f, 1f, 1f, 255);
        }
    }
}
