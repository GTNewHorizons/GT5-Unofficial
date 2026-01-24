package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.GTMod;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.common.config.Client;

public class TranscendentalMetaItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return Client.render.renderTransMetalFancy && type != ItemRenderType.FIRST_PERSON_MAP;
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

        if (item.getItem() instanceof MetaGeneratedItem mgItem) {
            IIcon[] icons = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
            if (icons != null && icons.length > 0 && icons[0] != null) {

                GL11.glEnable(GL11.GL_ALPHA_TEST);

                IIcon icon = icons[0];

                GL11.glPushMatrix();
                applyEffect(type);

                GL11.glColor4f(1f, 1f, 1f, 255);

                boolean flip = false;

                if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                    GL11.glScalef(16, 16, 32);
                    flip = true;
                }

                ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    flip ? icon.getMinU() : icon.getMaxU(),
                    flip ? icon.getMinV() : icon.getMaxV(),
                    flip ? icon.getMaxU() : icon.getMinU(),
                    flip ? icon.getMaxV() : icon.getMinV(),
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    0.0625F);

                GL11.glPopMatrix();
            }
        }

        GL11.glPopMatrix();
    }

    private void applyEffect(ItemRenderType type) {
        if (RenderItem.renderInFrame) {
            // Float in front of item frame
            GL11.glTranslatef(0.0f, 0.0f, -0.5f);
        }

        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glTranslatef(8f, 8f, 0f);
        } else {
            GL11.glTranslatef(0.5f, 0.5f, 0.0f);
        }

        GL11.glRotatef(
            (GTMod.clientProxy()
                .getAnimationRenderTicks() * 3.5f) % 360,
            0.3f,
            0.5f,
            0.2f);

        GL11.glRotatef(180, 0.5f, 0.0f, 0.0f);

        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glTranslatef(-8f, -8f, 0f);
        } else {
            GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        }

        // Center on point of rotation
        GL11.glTranslatef(0.0f, 0.0f, 0.03125F);

        GL11.glColor4f(1f, 1f, 1f, 255);

    }

}
