package gregtech.common.render.items;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.enums.Textures;
import gregtech.common.render.GT_RenderUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import static gregtech.common.render.GT_RenderUtil.colourGTItem;

public class CosmicNeutroniumRenderer extends GT_GeneratedMaterial_Renderer {

    // spotless:off
    final Pos2d point0 = new Pos2d(0  - 10, 0  - 10);
    final Pos2d point1 = new Pos2d(17 + 10, 0  - 10);
    final Pos2d point2 = new Pos2d(17 + 10, 17 + 10);
    final Pos2d point3 = new Pos2d(0  - 10, 17 + 10);
    // spotless:on

    @Override
    public void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor) {

        IIcon haloFuzzy = Textures.ItemIcons.HALO_FUZZY.getIcon();


        float spread;
        if (type.equals(ItemRenderType.INVENTORY)) {
            spread = 10.0f;
        } else {
            spread = 1.2f;
        }

        if (haloFuzzy == null) {
            return;
        }

        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        // Ideally this magic haloColour number should scale depending on the # of transparent pixels,
        // but I'm not sure how to determine this with OpenGL.
        int haloColour = 1_090_519_039;
        float ca = (float) (haloColour >> 24 & 255) / 255.0F;
        float cr = (float) (haloColour >> 16 & 255) / 255.0F;
        float cg = (float) (haloColour >> 8 & 255) / 255.0F;
        float cb = (float) (haloColour & 255) / 255.0F;
        GL11.glColor4f(cr, cg, cb, ca);

//        // Draw
//        t.startDrawingQuads();
//        t.addVertexWithUV(-spread, -spread, 0, haloFuzzy.getMinU(), haloFuzzy.getMinV());
//        t.addVertexWithUV(-spread, 16 + spread, 0, haloFuzzy.getMinU(), haloFuzzy.getMaxV());
//        t.addVertexWithUV(16 + spread, 16 + spread, 0, haloFuzzy.getMaxU(), haloFuzzy.getMaxV());
//        t.addVertexWithUV(16 + spread, -spread, 0, haloFuzzy.getMaxU(), haloFuzzy.getMinV());
//        t.draw();


        // spotless:off
        // For those following in my footsteps, this may be of use - Colen 25th dec 2022.
        // http://greyminecraftcoder.blogspot.com/2013/08/the-tessellator.html
        // 0 - 1
        // | I | // I = Item.
        // 3 - 2

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            t.startDrawingQuads();
            t.addVertexWithUV(point0.x, point0.y, 0, haloFuzzy.getMinU(), haloFuzzy.getMinV());
            t.addVertexWithUV(point3.x, point3.y, 0, haloFuzzy.getMinU(), haloFuzzy.getMaxV()); // Lower left?
            t.addVertexWithUV(point2.x, point2.y, 0, haloFuzzy.getMaxU(), haloFuzzy.getMaxV()); // Lower right?
            t.addVertexWithUV(point1.x, point1.y, 0, haloFuzzy.getMaxU(), haloFuzzy.getMinV());
            t.draw();
        }
        //spotless:on

//        else {
//            ItemRenderer.renderItemIn2D(
//                Tessellator.instance,
//                haloFuzzy.getMaxU(),
//                haloFuzzy.getMinV(),
//                haloFuzzy.getMinU(),
//                haloFuzzy.getMaxV(),
//                haloFuzzy.getIconWidth()*2,
//                haloFuzzy.getIconHeight()*2,
//                0.0625F); // Depth of item held in hand.
//        }

        {
            // Draw actual cosmic Nt item.
            GL11.glPushMatrix();

            colourGTItem(item);

            if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                GT_RenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            } else {
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

        GL11.glPopMatrix();
    }
}
