package gregtech.common.render.items;

import gregtech.api.enums.Textures;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class InfinityRenderer extends GT_GeneratedMaterial_Renderer {
    public Random rand = new Random();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor) {

        IIcon halo = Textures.ItemIcons.HALO.getIcon();

        int spread = 10;
        int haloAlpha = 0xFF000000;

        if (halo == null) {
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

        GL11.glColor4f(0, 0, 0, (float) (haloAlpha >> 24 & 255) / 255.0F);

        t.startDrawingQuads();
        t.addVertexWithUV(-spread, -spread, 0, halo.getMinU(), halo.getMinV());
        t.addVertexWithUV(-spread, 16 + spread, 0, halo.getMinU(), halo.getMaxV());
        t.addVertexWithUV(16 + spread, 16 + spread, 0, halo.getMaxU(), halo.getMaxV());
        t.addVertexWithUV(16 + spread, -spread, 0, halo.getMaxU(), halo.getMinV());
        t.draw();

        GL11.glPushMatrix();
        double scale = (rand.nextGaussian() * 0.15) + 0.95;
        double offset = (1.0 - scale) / 2.0;
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

        r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        r.renderWithColor = true;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
