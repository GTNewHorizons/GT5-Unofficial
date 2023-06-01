package gregtech.common.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Utility;

public class GT_MetaGenerated_Tool_Renderer implements IItemRenderer {

    public GT_MetaGenerated_Tool_Renderer() {
        for (GT_MetaGenerated_Tool tItem : GT_MetaGenerated_Tool.sInstances.values()) {
            if (tItem != null) {
                MinecraftForgeClient.registerItemRenderer(tItem, this);
            }
        }
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, IItemRenderer.ItemRenderType aType) {
        if ((GT_Utility.isStackInvalid(aStack)) || (aStack.getItemDamage() < 0)) {
            return false;
        }
        return (aType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            || (aType == IItemRenderer.ItemRenderType.INVENTORY)
            || (aType == IItemRenderer.ItemRenderType.EQUIPPED)
            || (aType == IItemRenderer.ItemRenderType.ENTITY);
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType aType, ItemStack aStack,
        IItemRenderer.ItemRendererHelper aHelper) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        }
        return aType == IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType aType, ItemStack aStack, Object... data) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return;
        }
        GT_MetaGenerated_Tool aItem = (GT_MetaGenerated_Tool) aStack.getItem();
        GL11.glEnable(GL11.GL_BLEND);
        if (aType == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                GL11.glScalef(0.85F, 0.85F, 0.85F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            } else {
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            }
        }
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        IToolStats tToolStats = aItem.getToolStats(aStack);
        if (tToolStats != null) {
            IIconContainer aIcon = tToolStats.getIcon(false, aStack);
            if (aIcon != null) {
                IIcon tIcon = aIcon.getIcon();
                IIcon tOverlay = aIcon.getOverlayIcon();
                if (tIcon != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    short[] tModulation = tToolStats.getRGBa(false, aStack);
                    GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
                    if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                        GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                    } else {
                        ItemRenderer.renderItemIn2D(
                            Tessellator.instance,
                            tIcon.getMaxU(),
                            tIcon.getMinV(),
                            tIcon.getMinU(),
                            tIcon.getMaxV(),
                            tIcon.getIconWidth(),
                            tIcon.getIconHeight(),
                            0.0625F);
                    }
                    GL11.glColor3f(1.0F, 1.0F, 1.0F);
                }
                if (tOverlay != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
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
            }
            aIcon = tToolStats.getIcon(true, aStack);
            if (aIcon != null) {
                IIcon tIcon = aIcon.getIcon();
                IIcon tOverlay = aIcon.getOverlayIcon();
                if (tIcon != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    short[] tModulation = tToolStats.getRGBa(true, aStack);
                    GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
                    if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                        GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                    } else {
                        ItemRenderer.renderItemIn2D(
                            Tessellator.instance,
                            tIcon.getMaxU(),
                            tIcon.getMinV(),
                            tIcon.getMinU(),
                            tIcon.getMaxV(),
                            tIcon.getIconWidth(),
                            tIcon.getIconHeight(),
                            0.0625F);
                    }
                    GL11.glColor3f(1.0F, 1.0F, 1.0F);
                }
                if (tOverlay != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
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
            }
            if ((aType == IItemRenderer.ItemRenderType.INVENTORY)
                && (GT_MetaGenerated_Tool.getPrimaryMaterial(aStack) != Materials._NULL)) {
                if (GT_Mod.gregtechproxy.mRenderItemDurabilityBar) {
                    long tDamage = GT_MetaGenerated_Tool.getToolDamage(aStack);
                    long tMaxDamage = GT_MetaGenerated_Tool.getToolMaxDamage(aStack);
                    if (tDamage <= 0L) {
                        aIcon = gregtech.api.enums.Textures.ItemIcons.DURABILITY_BAR[8];
                    } else if (tDamage >= tMaxDamage) {
                        aIcon = gregtech.api.enums.Textures.ItemIcons.DURABILITY_BAR[0];
                    } else {
                        aIcon = gregtech.api.enums.Textures.ItemIcons.DURABILITY_BAR[((int) java.lang.Math
                            .max(0L, java.lang.Math.min(7L, (tMaxDamage - tDamage) * 8L / tMaxDamage)))];
                    }
                    if (aIcon != null) {
                        IIcon tIcon = aIcon.getIcon();
                        IIcon tOverlay = aIcon.getOverlayIcon();
                        if (tIcon != null) {
                            Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                                GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                            } else {
                                ItemRenderer.renderItemIn2D(
                                    Tessellator.instance,
                                    tIcon.getMaxU(),
                                    tIcon.getMinV(),
                                    tIcon.getMinU(),
                                    tIcon.getMaxV(),
                                    tIcon.getIconWidth(),
                                    tIcon.getIconHeight(),
                                    0.0625F);
                            }
                        }
                        if (tOverlay != null) {
                            Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
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
                    }
                }

                if (GT_Mod.gregtechproxy.mRenderItemChargeBar) {
                    Long[] tStats = aItem.getElectricStats(aStack);
                    if ((tStats != null) && (tStats[3] < 0L)) {
                        long tCharge = aItem.getRealCharge(aStack);
                        if (tCharge <= 0L) {
                            aIcon = gregtech.api.enums.Textures.ItemIcons.ENERGY_BAR[0];
                        } else if (tCharge >= tStats[0]) {
                            aIcon = gregtech.api.enums.Textures.ItemIcons.ENERGY_BAR[8];
                        } else {
                            aIcon = gregtech.api.enums.Textures.ItemIcons.ENERGY_BAR[(7 - (int) java.lang.Math
                                .max(0L, java.lang.Math.min(6L, (tStats[0] - tCharge) * 7L / tStats[0])))];
                        }
                    } else {
                        aIcon = null;
                    }
                    if (aIcon != null) {
                        IIcon tIcon = aIcon.getIcon();
                        IIcon tOverlay = aIcon.getOverlayIcon();
                        if (tIcon != null) {
                            Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                                GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                            } else {
                                ItemRenderer.renderItemIn2D(
                                    Tessellator.instance,
                                    tIcon.getMaxU(),
                                    tIcon.getMinV(),
                                    tIcon.getMinU(),
                                    tIcon.getMaxV(),
                                    tIcon.getIconWidth(),
                                    tIcon.getIconHeight(),
                                    0.0625F);
                            }
                        }
                        if (tOverlay != null) {
                            Minecraft.getMinecraft().renderEngine.bindTexture(aIcon.getTextureFile());
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            if (aType.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
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
                    }
                }
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
