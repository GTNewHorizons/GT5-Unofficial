package gregtech.common.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;

public class GT_MetaGenerated_Tool_Renderer implements IItemRenderer {

    public GT_MetaGenerated_Tool_Renderer() {
        for (GT_MetaGenerated_Tool tItem : GT_MetaGenerated_Tool.sInstances.values()) {
            if (tItem != null) {
                MinecraftForgeClient.registerItemRenderer(tItem, this);
            }
        }
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return (type == ItemRenderType.EQUIPPED_FIRST_PERSON) || (type == ItemRenderType.INVENTORY)
            || (type == ItemRenderType.EQUIPPED)
            || (type == ItemRenderType.ENTITY);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GT_MetaGenerated_Tool item = (GT_MetaGenerated_Tool) stack.getItem();
        GL11.glEnable(GL11.GL_BLEND);
        GT_RenderUtil.applyStandardItemTransform(type);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        IToolStats toolStats = item != null ? item.getToolStats(stack) : null;
        if (toolStats != null) {
            renderToolPart(type, stack, toolStats, false);
            renderToolPart(type, stack, toolStats, true);

            if ((type == ItemRenderType.INVENTORY)
                && (GT_MetaGenerated_Tool.getPrimaryMaterial(stack) != Materials._NULL)) {
                if (GT_Mod.gregtechproxy.mRenderItemDurabilityBar) {
                    IIconContainer iconContainer;
                    long damage = GT_MetaGenerated_Tool.getToolDamage(stack);
                    long maxDamage = GT_MetaGenerated_Tool.getToolMaxDamage(stack);
                    if (damage <= 0L) {
                        iconContainer = Textures.ItemIcons.DURABILITY_BAR[8];
                    } else if (damage >= maxDamage) {
                        iconContainer = Textures.ItemIcons.DURABILITY_BAR[0];
                    } else {
                        iconContainer = Textures.ItemIcons.DURABILITY_BAR[((int) Math
                            .max(0L, Math.min(7L, (maxDamage - damage) * 8L / maxDamage)))];
                    }
                    renderIcon(iconContainer);
                }

                if (GT_Mod.gregtechproxy.mRenderItemChargeBar) {
                    IIconContainer iconContainer;
                    Long[] stats = item.getElectricStats(stack);
                    if ((stats != null) && (stats[3] < 0L)) {
                        long tCharge = item.getRealCharge(stack);
                        if (tCharge <= 0L) {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[0];
                        } else if (tCharge >= stats[0]) {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[8];
                        } else {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[(7
                                - (int) Math.max(0L, Math.min(6L, (stats[0] - tCharge) * 7L / stats[0])))];
                        }
                    } else {
                        iconContainer = null;
                    }
                    renderIcon(iconContainer);
                }
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderIcon(IIconContainer iconContainer) {
        if (iconContainer != null) {
            IIcon icon = iconContainer.getIcon();
            IIcon overlay = iconContainer.getOverlayIcon();
            if (icon != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GT_RenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            }
            if (overlay != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GT_RenderUtil.renderItemIcon(overlay, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            }
        }
    }

    private static void renderToolPart(ItemRenderType type, ItemStack stack, IToolStats toolStats, boolean isToolHead) {
        IIconContainer iconContainer = toolStats.getIcon(isToolHead, stack);
        if (iconContainer != null) {
            IIcon icon = iconContainer.getIcon();
            IIcon overlay = iconContainer.getOverlayIcon();
            if (icon != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                short[] modulation = toolStats.getRGBa(isToolHead, stack);
                GL11.glColor3f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F);
                GT_RenderUtil.renderItem(type, icon);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
            if (overlay != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GT_RenderUtil.renderItem(type, overlay);
            }
        }
    }
}
