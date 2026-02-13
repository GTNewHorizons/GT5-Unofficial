package gregtech.common.render.items;

import java.util.Random;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.TextureUtils;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;

// TODO: Render effects outside inventory.

public class InfinityRenderer extends GeneratedMaterialRenderer {

    private static final Random rand = new Random();

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        if (!Client.render.renderInfinityFancy) {
            super.renderItem(type, aStack, data);
            return;
        }

        short aMetaData = (short) aStack.getItemDamage();
        if (!(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem)) return;

        int passes = 1;
        if (aItem.requiresMultipleRenderPasses()) {
            passes = aItem.getRenderPasses(aMetaData);
        }

        for (int pass = 0; pass < passes; pass++) {
            IIcon tIcon = aItem.getIcon(aMetaData, pass);
            IIcon tOverlay = aItem.getOverlayIcon(aMetaData, pass);
            FluidStack aFluid = GTUtility.getFluidForFilledItem(aStack, true);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (type == ItemRenderType.INVENTORY) {
                if (pass == 0) {
                    renderHalo();
                }
                renderPulse(tOverlay, tIcon);
            }

            // Workaround for cell and comb:
            // 1. BW capsule needs `renderContainedFluid` call as it doesn't have
            // `materialicons/CUSTOM/infinity/capsuleMolten`
            // 2. Without these 2 GL calls fluid texture leaks out of the cell / capsule
            // 3. Comb texture doesn't like depth enabled
            if (passes == 1) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (tIcon != null) {
                renderRegularItem(type, aStack, tIcon, aFluid == null);
            }

            if (tOverlay != null && aFluid != null && aFluid.getFluid() != null) {
                IIcon fluidIcon = aFluid.getFluid()
                    .getIcon(aFluid);
                if (fluidIcon != null) {
                    // Adds colour to a cells fluid. Does not colour full fluid icons as shown in NEI etc.
                    renderContainedFluid(type, aFluid, fluidIcon);
                }
            }

            if (tOverlay != null) {
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                TextureUtils.bindAtlas(aItem.getSpriteNumber());
                renderItemOverlay(type, tOverlay);
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static void renderHalo() {
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
    }

    public static void renderPulse(IIcon... icons) {
        if (icons.length == 0) return;
        Tessellator t = Tessellator.instance;
        float random = (float) rand.nextGaussian();
        float scale = (random * 0.15f) + 0.95f;
        float offset = (1.0f - scale) / 2.0f;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glTranslatef(offset * 16.0f, offset * 16.0f, 1.0f);
        GL11.glScalef(scale, scale, 1.0f);

        for (IIcon icon : icons) {
            if (icon == null) continue;
            t.startDrawingQuads();
            t.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.6f);
            t.addVertexWithUV(0 - offset, 0 - offset, 0, icon.getMinU(), icon.getMinV());
            t.addVertexWithUV(0 - offset, 16 + offset, 0, icon.getMinU(), icon.getMaxV());
            t.addVertexWithUV(16 + offset, 16 + offset, 0, icon.getMaxU(), icon.getMaxV());
            t.addVertexWithUV(16 + offset, 0 - offset, 0, icon.getMaxU(), icon.getMinV());
            t.draw();
        }
        GL11.glPopMatrix();
    }
}
