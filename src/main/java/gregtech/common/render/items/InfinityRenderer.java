package gregtech.common.render.items;

import static gregtech.api.enums.Textures.InvisibleIcon.INVISIBLE_ICON;

import java.util.Random;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.TextureUtils;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.interfaces.IIconContainer;
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
        IGT_ItemWithMaterialRenderer aItem = IGT_ItemWithMaterialRenderer.resolve(aStack);
        if (aItem == null) return;

        IIconContainer container = aItem.getIconContainer(aMetaData);

        int passes = 1;
        if (aItem.requiresMultipleRenderPasses()) {
            passes = aItem.getRenderPasses(aMetaData);
        }

        for (int pass = 0; pass < passes; pass++) {
            final IIcon base;
            final IIcon top;
            final boolean hasArtOverFluid;
            if (container != null) {
                int layers = container.getIconPasses();
                base = drawable(container.getLayerIcon(0));
                top = layers > 1 ? drawable(container.getLayerIcon(layers - 1)) : null;
                hasArtOverFluid = layers > 1;
            } else {
                base = aItem.getIcon(aMetaData, pass);
                top = aItem.getOverlayIcon(aMetaData, pass);
                hasArtOverFluid = top != null;
            }
            FluidStack aFluid = GTUtility.getFluidForFilledItem(aStack, true);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (type == ItemRenderType.INVENTORY) {
                if (pass == 0) {
                    renderHalo();
                }
                renderPulse(top, base);
            }

            // Workaround for cell and comb:
            // 1. BW capsule needs `renderContainedFluid` call as it has no capsule art of its own
            // 2. Without these 2 GL calls fluid texture leaks out of the cell / capsule
            // 3. Comb texture doesn't like depth enabled
            if (passes == 1) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (base != null) {
                renderRegularItem(type, aStack, base, aFluid == null);
            }

            if (hasArtOverFluid && aFluid != null && aFluid.getFluid() != null) {
                renderContainedFluid(type, aFluid);
            }

            if (container != null) {
                renderUpperLayers(type, aStack, aItem, container);
            } else if (top != null) {
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                TextureUtils.bindAtlas(aItem.getSpriteNumber());
                renderItemOverlay(type, top);
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private static IIcon drawable(IIcon icon) {
        return icon == INVISIBLE_ICON ? null : icon;
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
