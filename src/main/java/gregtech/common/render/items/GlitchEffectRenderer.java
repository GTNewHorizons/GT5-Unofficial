package gregtech.common.render.items;

import java.util.Random;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.TextureUtils;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GTUtility;

public class GlitchEffectRenderer extends GeneratedMaterialRenderer {

    public Random rand = new Random();
    int[] red = new int[] { 255, 50, 50, 192 };
    int[] cyan = new int[] { 0, 220, 220, 160 };
    int counter = 0;
    double offsetRed = 0;
    double offsetCyan = 0;

    private void applyGlitchEffect(ItemRenderType type, boolean shouldModulateColor, int[] color, IIcon... icons) {
        double offset;
        for (IIcon icon : icons) {
            if (icon == null) continue;
            if (counter % 2 == 0) {
                offset = offsetRed;
            } else {
                offset = offsetCyan;
            }
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);

            Tessellator t = Tessellator.instance;

            if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                t.startDrawingQuads();
                if (shouldModulateColor) {
                    t.setColorRGBA_F(color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F, color[3] / 255.0F);
                } else {
                    t.setColorRGBA_F(1f, 1f, 1f, 0.75f);
                }
                t.addVertexWithUV(0 + offset, 0 + offset, 0, icon.getMinU(), icon.getMinV());
                t.addVertexWithUV(0 + offset, 16 + offset, 0, icon.getMinU(), icon.getMaxV());
                t.addVertexWithUV(16 + offset, 16 + offset, 0, icon.getMaxU(), icon.getMaxV());
                t.addVertexWithUV(16 + offset, 0 + offset, 0, icon.getMaxU(), icon.getMinV());
                t.draw();
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
        counter++;
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor,
        int pass, Object... data) {
        short metaData = (short) item.getItemDamage();
        if (!(item.getItem() instanceof IGT_ItemWithMaterialRenderer itemRenderer)) return;

        int currentFrame = (int) ((System.nanoTime() % 4_000_000_000L) / 20_000_000L);
        boolean timing = currentFrame <= 20;

        if (timing && currentFrame % 5 == 0) {
            offsetRed = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
            offsetCyan = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
        }

        IIcon itemIcon = itemRenderer.getIcon(metaData, pass);
        IIcon overlay = itemRenderer.getOverlayIcon(metaData, pass);
        FluidStack aFluid = GTUtility.getFluidForFilledItem(item, true);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (shouldModulateColor) {
            short[] modulation = itemRenderer.getRGBa(item);
            GL11.glColor4f(
                modulation[0] / 255.0F,
                modulation[1] / 255.0F,
                modulation[2] / 255.0F,
                modulation[3] / 255.0F);
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (itemIcon != null) {
            markNeedsAnimationUpdate(itemIcon);
            renderRegularItem(type, item, itemIcon, aFluid == null);
        }

        if (overlay != null && aFluid != null && aFluid.getFluid() != null) {
            IIcon fluidIcon = aFluid.getFluid()
                .getIcon(aFluid);
            if (fluidIcon != null) {
                markNeedsAnimationUpdate(fluidIcon);
                // Adds colour to a cells fluid. Does not colour full fluid icons as shown in NEI etc.
                renderContainedFluid(type, aFluid, fluidIcon);
            }
        }

        if (overlay != null) {
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            TextureUtils.bindAtlas(itemRenderer.getSpriteNumber());
            markNeedsAnimationUpdate(overlay);
            renderItemOverlay(type, overlay);
        }

        if (type == ItemRenderType.INVENTORY && timing) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            applyGlitchEffect(type, shouldModulateColor, cyan, itemIcon, overlay);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            applyGlitchEffect(type, shouldModulateColor, red, itemIcon, overlay);

        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
