package gregtech.common.render.items;

import gregtech.api.enums.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CosmicNeutroniumRenderer extends GT_GeneratedMaterial_Renderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor) {

        IIcon haloFuzzy = Textures.ItemIcons.HALO_FUZZY.getIcon();

        int spread = 10;

        if (haloFuzzy == null) {
            return;
        }

        RenderItem r = RenderItem.getInstance();
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Ideally this magic haloColour number should scale depending on the # of transparent pixels,
        // but I'm not sure how to determine this with OpenGL.
        int haloColour = 1308622847;
        float ca = (float)(haloColour >> 24 & 255) / 255.0F;
        float cr = (float)(haloColour >> 16 & 255) / 255.0F;
        float cg = (float)(haloColour >> 8 & 255) / 255.0F;
        float cb = (float)(haloColour & 255) / 255.0F;
        GL11.glColor4f(cr, cg, cb, ca);

        t.startDrawingQuads();
        t.addVertexWithUV(-spread, -spread, 0, haloFuzzy.getMinU(), haloFuzzy.getMinV());
        t.addVertexWithUV(-spread, 16+spread, 0, haloFuzzy.getMinU(), haloFuzzy.getMaxV());
        t.addVertexWithUV(16+spread, 16+spread, 0, haloFuzzy.getMaxU(), haloFuzzy.getMaxV());
        t.addVertexWithUV(16+spread, -spread, 0, haloFuzzy.getMaxU(), haloFuzzy.getMinV());
        t.draw();

        r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
