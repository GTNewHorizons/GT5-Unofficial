package gregtech.common.render.items;

import static gregtech.common.render.GTRenderUtil.colorGTItem;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.Textures;

public class CosmicNeutroniumRenderer extends GeneratedMaterialRenderer {

    // TODO: Render halo outside of inventory.
    public static void renderHalo(ItemRenderType type) {
        // Because when this class is instantiated, making this a static field will cause it to set to null.
        final IIcon haloFuzzy = Textures.ItemIcons.HALO_FUZZY.getIcon();

        if (haloFuzzy == null) {
            return;
        }

        // Ideally this magic haloColour number should scale depending on the # of transparent pixels,
        // but I'm not sure how to determine this with OpenGL.
        // This is from Avaritia code, but modified to untangle the interfaces.
        // int haloColour = 0x4DFFFFFF;
        final int alpha = 0x4D;
        final float ca = (float) alpha / 255.0F;
        GL11.glColor4f(1, 1, 1, ca);

        // spotless:off
        // For those following in my footsteps, this may be of use - Colen 25th dec 2022.
        // http://greyminecraftcoder.blogspot.com/2013/08/the-tessellator.html

        Tessellator t = Tessellator.instance;

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            t.startDrawingQuads();
            t.addVertexWithUV(0 - 10, 0 - 10, 0, haloFuzzy.getMinU(), haloFuzzy.getMinV());
            t.addVertexWithUV(0 - 10, 17 + 10, 0, haloFuzzy.getMinU(), haloFuzzy.getMaxV());
            t.addVertexWithUV(17 + 10, 17 + 10, 0, haloFuzzy.getMaxU(), haloFuzzy.getMaxV());
            t.addVertexWithUV(17 + 10, 0 - 10, 0, haloFuzzy.getMaxU(), haloFuzzy.getMinV());
            t.draw();
        }
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor, int pass, Object... data) {
        RenderItem r = RenderItem.getInstance();

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        if (pass == 0) {
            renderHalo(type);
        }

        //spotless:on
        // Draw actual cosmic Nt item.

        colorGTItem(item);

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            ItemRenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            ItemRenderUtil.renderItem(type, icon);
        }

        // GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        r.renderWithColor = true;
    }
}
