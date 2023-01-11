package gregtech.common.render.items;

import codechicken.lib.render.TextureUtils;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_RenderUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GT_GeneratedMaterial_Renderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED
                || type == ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == ItemRenderType.INVENTORY
                || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    // Do not use this method. Only for GT_FluidDisplayItem.
    public void renderFluidSpecial(ItemRenderType type, ItemStack aStack, IIcon icon, Object... data) {
        renderRegularItem(type, aStack, icon, false);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        short aMetaData = (short) aStack.getItemDamage();
        GT_MetaGenerated_Item aItem = (GT_MetaGenerated_Item) aStack.getItem();

        IIconContainer aIconContainer = aItem.getIconContainer(aMetaData);

        if (aIconContainer == null) {
            return;
        }

        IIcon tIcon = aIconContainer.getIcon();
        IIcon tOverlay = aIconContainer.getOverlayIcon();
        FluidStack aFluid = GT_Utility.getFluidForFilledItem(aStack, true);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (tIcon != null) {
            renderRegularItem(type, aStack, tIcon, aFluid == null);
        }

        if (tOverlay != null && aFluid != null && aFluid.getFluid() != null) {
            IIcon fluidIcon = aFluid.getFluid().getIcon(aFluid);
            if (fluidIcon != null) {
                // Adds colour to a cells fluid. Does not colour full fluid icons as shown in NEI etc.
                renderContainedFluid(type, aFluid, fluidIcon);
            }
        }

        if (tOverlay != null) {
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            TextureUtils.bindAtlas(aItem.getSpriteNumber());
            if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                GT_RenderUtil.renderItemIcon(tOverlay, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            } else {
                ItemRenderer.renderItemIn2D(
                        Tessellator.instance,
                        tOverlay.getMaxU(),
                        tOverlay.getMinV(),
                        tOverlay.getMinU(),
                        tOverlay.getMaxV(),
                        tOverlay.getIconWidth(),
                        tOverlay.getIconHeight(),
                        0.0625F);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        GT_MetaGenerated_Item aItem = (GT_MetaGenerated_Item) aStack.getItem();

        if (shouldModulateColor) {
            short[] tModulation = aItem.getRGBa(aStack);
            GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
        }

        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    icon.getMaxU(),
                    icon.getMinV(),
                    icon.getMinU(),
                    icon.getMaxV(),
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    0.0625F);
        }
    }

    public void renderContainedFluid(ItemRenderType type, FluidStack aFluidStack, IIcon fluidIcon) {
        Fluid aFluid = aFluidStack.getFluid();
        int tColor = aFluid.getColor(aFluidStack);
        GL11.glColor3f((tColor >> 16 & 0xFF) / 255.0F, (tColor >> 8 & 0xFF) / 255.0F, (tColor & 0xFF) / 255.0F);
        TextureUtils.bindAtlas(aFluid.getSpriteNumber());

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
    }
}
