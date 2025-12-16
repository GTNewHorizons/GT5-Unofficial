package gregtech.common.render.items;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.Textures;
import gregtech.api.items.MetaGeneratedItem;

public class CosmicNeutroniumMetaItemRenderer implements IItemRenderer {

    public Random rand = new Random();

    // spotless:off
    private static final Pos2d point0 = new Pos2d(0  - 10, 0  - 10);
    private static final Pos2d point1 = new Pos2d(17 + 10, 0  - 10);
    private static final Pos2d point2 = new Pos2d(17 + 10, 17 + 10);
    private static final Pos2d point3 = new Pos2d(0  - 10, 17 + 10);
    // spotless:on

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
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    renderHalo(type);
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

    private void renderHalo(ItemRenderType type) {
        final IIcon haloFuzzy = Textures.ItemIcons.HALO_FUZZY.getIcon();

        if (haloFuzzy == null) {
            return;
        }

        GL11.glPushMatrix();

        int haloColour = 0x4DFFFFFF;
        float ca = (float) (haloColour >> 24 & 255) / 255.0F;
        float cr = (float) (0x99FFFFFF >> 16 & 255) / 255.0F;
        float cg = (float) (0x99FFFFFF >> 8 & 255) / 255.0F;
        float cb = (float) (0x99FFFFFF & 255) / 255.0F;
        GL11.glColor4f(cr, cg, cb, ca);

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

}
