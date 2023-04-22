package gregtech.common.render.items;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.TextureUtils;
import gregtech.GT_Mod;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GT_Util;
import gregtech.common.render.GT_RenderUtil;

public class TranscendentMetalRenderer extends GT_GeneratedMaterial_Renderer {

    // frameIndex should not have 3 or more frames with same value (unless it's meant to be still)
    // otherwise the primitive linear interpolation will render the animation in a pretty sluggish way
    private final int[] frameIndex = new int[] { 0, 0, 0, 0, 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 12, 13, 14, 16, 17, 19, 21,
        22, 24, 25, 27, 29, 30, 32, 33, 35, 36, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 50, 49, 48, 47, 46, 45,
        44, 43, 42, 40, 39, 38, 36, 35, 33, 32, 30, 29, 27, 25, 24, 22, 21, 19, 17, 16, 14, 13, 12, 10, 9, 8, 7, 5, 4,
        3, 2, 1, 1, 0, 0, 0, 0 };
    private final float[] frameRotation = new float[] { 0, 7, 14, 21, 28, 36, 43, 50, 57, 64, 72, 79, 86, 93, 100, 108,
        115, 122, 129, 136, 144, 151, 158, 165, 172, 180, 187, 194, 201, 208, 216, 223, 230, 237, 244, 252, 259, 266,
        273, 280, 288, 295, 302, 309, 316, 324, 331, 338, 345, 352, 360 };
    private final float[] frameAlpha = new float[] { 100 / 255f, 106 / 255f, 112 / 255f, 118 / 255f, 124 / 255f,
        131 / 255f, 137 / 255f, 143 / 255f, 149 / 255f, 155 / 255f, 162 / 255f, 168 / 255f, 174 / 255f, 180 / 255f,
        186 / 255f, 193 / 255f, 199 / 255f, 205 / 255f, 211 / 255f, 217 / 255f, 224 / 255f, 230 / 255f, 236 / 255f,
        242 / 255f, 248 / 255f, 255 / 255f, 248 / 255f, 242 / 255f, 236 / 255f, 230 / 255f, 224 / 255f, 217 / 255f,
        211 / 255f, 205 / 255f, 199 / 255f, 193 / 255f, 186 / 255f, 180 / 255f, 174 / 255f, 168 / 255f, 162 / 255f,
        155 / 255f, 149 / 255f, 143 / 255f, 137 / 255f, 131 / 255f, 124 / 255f, 118 / 255f, 112 / 255f, 106 / 255f,
        100 / 255f };

    private static float linearInterpolation(float[] controlPoints, int frameA, int frameB, float partial) {
        float a = controlPoints[frameA], b = controlPoints[frameB];
        return a * (1 - partial) + b * partial;
    }

    @Override
    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        GL11.glPushMatrix();
        applyEffect(type, ((IGT_ItemWithMaterialRenderer) aStack.getItem()).getRGBa(aStack), shouldModulateColor);
        super.renderRegularItem(type, aStack, icon, false);
        GL11.glPopMatrix();
    }

    @Override
    protected void renderContainedFluid(ItemRenderType type, FluidStack aFluidStack, IIcon fluidIcon) {
        GL11.glPushMatrix();

        Fluid fluid = aFluidStack.getFluid();
        applyEffect(type, GT_Util.getRGBaArray(fluid.getColor()), true);

        TextureUtils.bindAtlas(fluid.getSpriteNumber());
        GL11.glDepthFunc(GL11.GL_EQUAL);
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(fluidIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                fluidIcon.getMaxU(),
                fluidIcon.getMinV(),
                fluidIcon.getMinU(),
                fluidIcon.getMaxV(),
                fluidIcon.getIconWidth(),
                fluidIcon.getIconHeight(),
                0.0625F);
        }
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPopMatrix();
    }

    @Override
    protected void renderItemOverlay(ItemRenderType type, IIcon overlay) {
        GL11.glPushMatrix();
        applyEffect(type, null, false);
        super.renderItemOverlay(type, overlay);
        GL11.glPopMatrix();
    }

    private void applyEffect(ItemRenderType type, short[] modulation, boolean shouldModulateColor) {
        long animationTicks = GT_Mod.gregtechproxy.getAnimationTicks();
        int frameCurrent = frameIndex[(int) (animationTicks / 2 % frameIndex.length)];
        int frameNext = frameIndex[(int) ((animationTicks + 2) / 2 % frameIndex.length)];
        float partialTicks = GT_Mod.gregtechproxy.getPartialRenderTicks();
        if ((animationTicks & 1) == 1) partialTicks = (partialTicks + 1) / 2;
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glTranslatef(8f, 8f, 0f);
        } else {
            GL11.glTranslatef(0.5f, 0.5f, 0.0f);
        }
        GL11.glRotatef(linearInterpolation(frameRotation, frameCurrent, frameNext, partialTicks), 0, 0, 1);
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GL11.glTranslatef(-8f, -8f, 0f);
        } else {
            GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        }

        if (shouldModulateColor) {
            GL11.glColor4f(
                modulation[0] / 255.0F,
                modulation[1] / 255.0F,
                modulation[2] / 255.0F,
                linearInterpolation(frameAlpha, frameCurrent, frameNext, partialTicks));
        } else {
            GL11.glColor4f(1f, 1f, 1f, linearInterpolation(frameAlpha, frameCurrent, frameNext, partialTicks));
        }
    }
}
