package gregtech.common.render;

import static gregtech.api.enums.Textures.InvisibleIcon.INVISIBLE_ICON;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGTTool;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.render.items.GeneratedMaterialRenderer;

public class MetaGeneratedToolRenderer implements IItemRenderer {

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
        IGTTool item = stack.getItem() instanceof IGTTool tool ? tool : null;
        GL11.glEnable(GL11.GL_BLEND);
        ItemRenderUtil.applyStandardItemTransform(type);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        IToolStats toolStats = item != null ? item.getToolStats(stack) : null;
        if (toolStats != null) {
            GeneratedMaterialRenderer materialRenderer = getMaterialRenderer(stack);
            if (materialRenderer != null) {
                materialRenderer.renderItem(type, stack, data);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            } else {
                renderToolPart(type, stack, toolStats, false);
                renderToolPart(type, stack, toolStats, true);
            }

            if ((type == ItemRenderType.INVENTORY)
                && (MetaGeneratedTool.getPrimaryMaterial(stack) != Materials._NULL)) {
                long maxDamage = item.getMaxStoredDamage(stack);
                if (GTMod.proxy.mRenderItemDurabilityBar && maxDamage > 0L) {
                    IIconContainer iconContainer;
                    long damage = item.getStoredDamage(stack);
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

                if (GTMod.proxy.mRenderItemChargeBar) {
                    IIconContainer iconContainer;
                    long maxCharge = item.getMaxStoredCharge(stack);
                    if (maxCharge > 0L) {
                        long charge = item.getStoredCharge(stack);
                        if (charge <= 0L) {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[0];
                        } else if (charge >= maxCharge) {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[8];
                        } else {
                            iconContainer = Textures.ItemIcons.ENERGY_BAR[(7
                                - (int) Math.max(0L, Math.min(6L, (maxCharge - charge) * 7L / maxCharge)))];
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
                ItemRenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
            }
            if (overlay != null && overlay != INVISIBLE_ICON) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                ItemRenderUtil.renderItemIcon(overlay, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
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
                ItemRenderUtil.renderItem(type, icon);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
            if (overlay != null && overlay != INVISIBLE_ICON) {
                Minecraft.getMinecraft().renderEngine.bindTexture(iconContainer.getTextureFile());
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                ItemRenderUtil.renderItem(type, overlay);
            }
        }
    }

    /**
     * The tool inherits its crafting material's special renderer -- the same {@code Materials.<name>.renderer} field
     * {@link gregtech.common.items.ItemComb} and {@link gregtech.api.items.MetaGeneratedItem} read for their own
     * per-material visuals -- rather than the plain textured-and-tinted icon draw below. Only tools whose metadata
     * <em>is</em> their material ({@link gregtech.common.items.tools.ToolItemBase} and its subclasses) implement
     * {@link IGT_ItemWithMaterialRenderer}; the old NBT-material {@link MetaGeneratedTool} family does not, so this
     * returns null for them and they keep rendering exactly as before.
     */
    private static GeneratedMaterialRenderer getMaterialRenderer(ItemStack stack) {
        if (!(stack.getItem() instanceof IGT_ItemWithMaterialRenderer materialItem)) return null;
        int meta = stack.getItemDamage();
        if (!materialItem.shouldUseCustomRenderer(meta) || !materialItem.allowMaterialRenderer(meta)) return null;
        return materialItem.getMaterialRenderer(meta);
    }
}
