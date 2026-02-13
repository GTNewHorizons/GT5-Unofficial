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
import gregtech.common.config.Client;

public class GlitchEffectRenderer extends GeneratedMaterialRenderer {

    public Random rand = new Random();
    private static final int[] red = new int[] { 255, 50, 50, 192 };
    private static final int[] cyan = new int[] { 0, 220, 220, 160 };

    final long frameTimeNanos = 10_000_000L;
    final int loopFrameCount = 200;
    final int glitchedDurationCount = 40;
    final int glitchMoveFrameCount = 5;

    double offsetRed = 0;
    double offsetCyan = 0;

    public static void applyRedGlitchEffect(ItemRenderType type, boolean shouldModulateColor, double offset,
        IIcon... icons) {
        applyGlitchEffect(type, shouldModulateColor, offset, red, icons);
    }

    public static void applyCyanGlitchEffect(ItemRenderType type, boolean shouldModulateColor, double offset,
        IIcon... icons) {
        applyGlitchEffect(type, shouldModulateColor, offset, cyan, icons);
    }

    public static void applyRedGlitchEffect(ItemRenderType type, double offset, IIcon... icons) {
        applyGlitchEffect(type, true, offset, red, icons);
    }

    public static void applyCyanGlitchEffect(ItemRenderType type, double offset, IIcon... icons) {
        applyGlitchEffect(type, true, offset, cyan, icons);
    }

    private static void applyGlitchEffect(ItemRenderType type, boolean shouldModulateColor, double offset, int[] color,
        IIcon... icons) {
        if (!type.equals(IItemRenderer.ItemRenderType.INVENTORY)) return;
        for (IIcon icon : icons) {
            if (icon == null) continue;
            Tessellator t = Tessellator.instance;

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
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor,
        int pass, Object... data) {

        if (!Client.render.renderGlitchFancy) {
            super.renderRegularItem(type, item, icon, shouldModulateColor);
            return;
        }

        short metaData = (short) item.getItemDamage();
        if (!(item.getItem() instanceof IGT_ItemWithMaterialRenderer itemRenderer)) return;

        int currentFrame = (int) ((System.nanoTime() % (frameTimeNanos * loopFrameCount)) / frameTimeNanos);
        boolean timing = currentFrame <= glitchedDurationCount;

        if (timing && currentFrame % glitchMoveFrameCount == 0) {
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

        if (itemIcon != null) {
            renderRegularItem(type, item, itemIcon, aFluid == null);
        }

        if (overlay != null && aFluid != null && aFluid.getFluid() != null) {
            IIcon fluidIcon = aFluid.getFluid()
                .getIcon(aFluid);
            if (fluidIcon != null) {
                // Adds colour to a cells fluid. Does not colour full fluid icons as shown in NEI etc.
                renderContainedFluid(type, aFluid, fluidIcon);
            }
        }

        if (overlay != null) {
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            TextureUtils.bindAtlas(itemRenderer.getSpriteNumber());
            renderItemOverlay(type, overlay);
        }

        if (type == ItemRenderType.INVENTORY && timing) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            applyGlitchEffect(type, shouldModulateColor, offsetCyan, cyan, itemIcon, overlay);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            applyGlitchEffect(type, shouldModulateColor, offsetRed, red, itemIcon, overlay);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
