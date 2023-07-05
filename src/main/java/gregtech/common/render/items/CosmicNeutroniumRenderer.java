package gregtech.common.render.items;

import static gregtech.common.render.GT_RenderUtil.colorGTItem;

import java.awt.*;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import gregtech.api.enums.Textures;
import gregtech.common.render.GT_RenderUtil;

public class CosmicNeutroniumRenderer extends GT_GeneratedMaterial_Renderer {

    // spotless:off
    private static final Point point0 = new Point(0  - 10, 0  - 10);
    private static final Point point1 = new Point(17 + 10, 0  - 10);
    private static final Point point2 = new Point(17 + 10, 17 + 10);
    private static final Point point3 = new Point(0  - 10, 17 + 10);
    // spotless:on

    private void drawHalo(ItemRenderType type) {
        // Because when this class is instantiated, making this a static field will cause it to set to null.
        final IIcon haloFuzzy = Textures.ItemIcons.HALO_FUZZY.getIcon();

        if (haloFuzzy == null) {
            return;
        }

        markNeedsAnimationUpdate(haloFuzzy);
        GL11.glPushMatrix();

        // Ideally this magic haloColour number should scale depending on the # of transparent pixels,
        // but I'm not sure how to determine this with OpenGL.
        // This is from Avaritia code, but modified to untangle the interfaces.
        int haloColour = 0x4DFFFFFF;
        float ca = (float) (haloColour >> 24 & 255) / 255.0F;
        float cr = (float) (0x99FFFFFF >> 16 & 255) / 255.0F;
        float cg = (float) (0x99FFFFFF >> 8 & 255) / 255.0F;
        float cb = (float) (0x99FFFFFF & 255) / 255.0F;
        GL11.glColor4f(cr, cg, cb, ca);

        // spotless:off
        // For those following in my footsteps, this may be of use - Colen 25th dec 2022.
        // http://greyminecraftcoder.blogspot.com/2013/08/the-tessellator.html

        Tessellator t = Tessellator.instance;

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            t.startDrawingQuads();
            t.addVertexWithUV(point0.x, point0.y, 0, haloFuzzy.getMinU(), haloFuzzy.getMinV());
            t.addVertexWithUV(point3.x, point3.y, 0, haloFuzzy.getMinU(), haloFuzzy.getMaxV());
            t.addVertexWithUV(point2.x, point2.y, 0, haloFuzzy.getMaxU(), haloFuzzy.getMaxV());
            t.addVertexWithUV(point1.x, point1.y, 0, haloFuzzy.getMaxU(), haloFuzzy.getMinV());
            t.draw();
        }

        GL11.glPopMatrix();
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor, int pass, Object... data) {
        RenderItem r = RenderItem.getInstance();

        GL11.glPushMatrix();

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        if (pass == 0) {
            drawHalo(type);
        }

        //spotless:on
        {
            // Draw actual cosmic Nt item.
            GL11.glPushMatrix();

            colorGTItem(item);

            if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                GT_RenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            } else {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    icon.getMaxU(),
                    icon.getMinV(),
                    icon.getMinU(),
                    icon.getMaxV(),
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    0.0625F);
            }
            GL11.glPopMatrix();
        }

        // GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        r.renderWithColor = true;

        GL11.glPopMatrix();
    }
}
