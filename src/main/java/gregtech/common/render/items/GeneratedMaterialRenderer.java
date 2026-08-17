package gregtech.common.render.items;

import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;
import static gregtech.api.enums.Textures.InvisibleIcon.INVISIBLE_ICON;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import codechicken.lib.render.TextureUtils;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTUtility;

public class GeneratedMaterialRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

    /**
     * Handle special fluid display rendering. Return false if does not need such kind of handling. Note: annotations
     * should not be rendered here. Only render the fluid texture. Parameters are values passed from
     * {@link IItemRenderer#renderItem(ItemRenderType, ItemStack, Object...)} verbatim. Do not modify the argument.
     * <p>
     * While this is called, BLEND and ALPHA_TEST is on. It is expected that these remain enabled while exit.
     *
     * @return true if did special fluid display rendering. false otherwise.
     */
    public boolean renderFluidDisplayItem(ItemRenderType type, ItemStack aStack, Object... data) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        short aMetaData = (short) aStack.getItemDamage();
        IGT_ItemWithMaterialRenderer aItem = IGT_ItemWithMaterialRenderer.resolve(aStack);
        if (aItem == null) return;

        IIconContainer container = aItem.getIconContainer(aMetaData);

        int passes = 1;
        if (aItem.requiresMultipleRenderPasses()) {
            passes = aItem.getRenderPasses(aMetaData);
        }

        for (int pass = 0; pass < passes; pass++) {
            FluidStack aFluid = GTUtility.getFluidForFilledItem(aStack, true);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (container != null) {
                IIcon base = container.getLayerIcon(0);
                if (base != null && base != INVISIBLE_ICON) {
                    renderRegularItem(type, aStack, base, aFluid == null, pass, data);
                }

                if (container.getIconPasses() > 1 && aFluid != null && aFluid.getFluid() != null) {
                    renderContainedFluid(type, aFluid);
                }

                renderUpperLayers(type, aStack, aItem, container);
            } else {
                IIcon tIcon = aItem.getIcon(aMetaData, pass);
                IIcon tOverlay = aItem.getOverlayIcon(aMetaData, pass);

                if (tIcon != null) {
                    renderRegularItem(type, aStack, tIcon, aFluid == null, pass, data);
                }

                if (tOverlay != null && aFluid != null && aFluid.getFluid() != null) {
                    renderContainedFluid(type, aFluid);
                }

                if (tOverlay != null && tOverlay != INVISIBLE_ICON) {
                    GL11.glColor3f(1.0F, 1.0F, 1.0F);
                    TextureUtils.bindAtlas(aItem.getSpriteNumber());
                    renderItemOverlay(type, tOverlay);
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    /**
     * Draws every icon layer of {@code container} above layer 0, each in its own color, through
     * {@link #renderItemOverlay}. Binds the item atlas ahead of them.
     */
    protected void renderUpperLayers(ItemRenderType type, ItemStack aStack, IGT_ItemWithMaterialRenderer aItem,
        IIconContainer container) {
        int passes = container.getIconPasses();
        if (passes > 1) TextureUtils.bindAtlas(aItem.getSpriteNumber());

        for (int layer = 1; layer < passes; layer++) {
            IIcon icon = container.getLayerIcon(layer);
            if (icon == null || icon == INVISIBLE_ICON) continue;
            short[] color = container.hasOverrideIcon() ? UNCOLORED_RGBA
                : container.isUsingColorModulation(layer) ? aItem.getRGBa(aStack) : container.getIconColor(layer);
            GL11.glColor3f(color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F);
            renderItemOverlay(type, icon);
        }
    }

    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor,
        int pass, Object... data) {
        renderRegularItem(type, aStack, icon, shouldModulateColor);
    }

    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        IGT_ItemWithMaterialRenderer aItem = IGT_ItemWithMaterialRenderer.resolve(aStack);
        if (aItem == null) return;

        if (shouldModulateColor) {
            short[] tModulation = aItem.getRGBa(aStack);
            GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
        }

        ItemRenderUtil.renderItem(type, icon);
    }

    /**
     * @return Whether {@code stack} renders from a resource pack override icon, which carries its own colors. See
     *         {@link IGT_ItemWithMaterialRenderer#hasOverrideIcon}.
     */
    protected static boolean hasOverrideIcon(ItemStack stack) {
        IGT_ItemWithMaterialRenderer item = IGT_ItemWithMaterialRenderer.resolve(stack);
        return item != null && item.hasOverrideIcon(stack);
    }

    /**
     * Draws the fluid {@code aFluidStack} holds, clipped to the art already drawn, when that fluid has an icon.
     */
    protected void renderContainedFluid(ItemRenderType type, FluidStack aFluidStack) {
        IIcon fluidIcon = aFluidStack.getFluid()
            .getIcon(aFluidStack);
        if (fluidIcon != null) renderContainedFluid(type, aFluidStack, fluidIcon);
    }

    protected void renderContainedFluid(ItemRenderType type, FluidStack aFluidStack, IIcon fluidIcon) {
        Fluid aFluid = aFluidStack.getFluid();
        int tColor = aFluid.getColor(aFluidStack);
        GL11.glColor3f((tColor >> 16 & 0xFF) / 255.0F, (tColor >> 8 & 0xFF) / 255.0F, (tColor & 0xFF) / 255.0F);
        TextureUtils.bindAtlas(aFluid.getSpriteNumber());

        GL11.glDepthFunc(GL11.GL_EQUAL);
        ItemRenderUtil.renderItem(type, fluidIcon);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    protected void renderItemOverlay(ItemRenderType type, IIcon overlay) {
        ItemRenderUtil.renderItem(type, overlay);
    }
}
