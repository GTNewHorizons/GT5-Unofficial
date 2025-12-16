package gregtech.common.render.items;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.Textures;
import gregtech.api.items.MetaGeneratedItem;

public class InfinityMetaItemRenderer implements IItemRenderer {

    public Random rand = new Random();

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

                if (type == ItemRenderType.INVENTORY) {
                    renderHalo();
                    renderPulse(icons[0], icons[0]);
                }

                // Restore state before rendering the actual item
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to avoid blending affecting icon

                ItemRenderUtil.renderItem(type, icons[0]);
            }
        }

        GL11.glPopMatrix();
    }

    private void renderHalo() {
        GL11.glPushMatrix();
        IIcon halo = Textures.ItemIcons.HALO.getIcon();

        int spread = 10;
        int haloAlpha = 0xFF000000;

        if (halo == null) {
            return;
        }

        Tessellator t = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glColor4f(20 / 255.0f, 20 / 255.0f, 20 / 255.0f, (float) (haloAlpha >> 24 & 255) / 255.0F);

        t.startDrawingQuads();
        t.addVertexWithUV(-spread, -spread, 0, halo.getMinU(), halo.getMinV());
        t.addVertexWithUV(-spread, 16 + spread, 0, halo.getMinU(), halo.getMaxV());
        t.addVertexWithUV(16 + spread, 16 + spread, 0, halo.getMaxU(), halo.getMaxV());
        t.addVertexWithUV(16 + spread, -spread, 0, halo.getMaxU(), halo.getMinV());
        t.draw();
        GL11.glPopMatrix();
    }

    private void renderPulse(IIcon... icons) {
        Tessellator t = Tessellator.instance;
        double random = rand.nextGaussian();
        double scale = (random * 0.15) + 0.95;
        double offset = (1.0 - scale) / 2.0;

        for (IIcon icon : icons) {
            if (icon == null) continue;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslated(offset * 16.0, offset * 16.0, 1.0);
            GL11.glScaled(scale, scale, 1.0);

            t.startDrawingQuads();
            t.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.6f);
            t.addVertexWithUV(0 - offset, 0 - offset, 0, icon.getMinU(), icon.getMinV());
            t.addVertexWithUV(0 - offset, 16 + offset, 0, icon.getMinU(), icon.getMaxV());
            t.addVertexWithUV(16 + offset, 16 + offset, 0, icon.getMaxU(), icon.getMaxV());
            t.addVertexWithUV(16 + offset, 0 - offset, 0, icon.getMaxU(), icon.getMinV());
            t.draw();

            GL11.glPopMatrix();
        }
    }

}
