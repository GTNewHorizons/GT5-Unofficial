package com.detrav.tools;

import com.detrav.utils.DetravMetaGeneratedItem;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravRenderItemMetaGenerated implements IItemRenderer {

    public DetravRenderItemMetaGenerated()
    {
        Iterator tIterator = DetravMetaGeneratedItem01.sInstances.values().iterator();
        while(tIterator.hasNext())
        {
            DetravMetaGeneratedItem dItem = (DetravMetaGeneratedItem) tIterator.next();
            if(dItem != null)
                MinecraftForgeClient.registerItemRenderer(dItem, this);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, IItemRenderer.ItemRenderType aType) {
        if ((GT_Utility.isStackInvalid(aStack)) || (aStack.getItemDamage() < 0)) {
            return false;
        }
        return (aType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) || (aType == IItemRenderer.ItemRenderType.INVENTORY) || (aType == IItemRenderer.ItemRenderType.EQUIPPED) || (aType == IItemRenderer.ItemRenderType.ENTITY);
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType aType, ItemStack aStack, IItemRenderer.ItemRendererHelper aHelper) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        }
        return aType == IItemRenderer.ItemRenderType.ENTITY;
    }
    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack aStack, Object... data) {

        if (GT_Utility.isStackInvalid(aStack)) {
            return;
        }
        short aMetaData = (short) aStack.getItemDamage();
        if (aMetaData < 0) {
            return;
        }

        DetravMetaGeneratedItem aItem = (DetravMetaGeneratedItem) aStack.getItem();

        GL11.glEnable(3042);
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                GL11.glScalef(0.85F, 0.85F, 0.85F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            } else {
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            }
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            if (aMetaData < 0) {
                IIconContainer aIcon = aItem.getIconContainer(aMetaData);
                IIcon tOverlay = null;
                IIcon tFluidIcon = null;
                IIcon tIcon;
                if (aIcon == null) {
                    tIcon = aStack.getIconIndex();
                } else {
                    tIcon = aIcon.getIcon();
                    tOverlay = aIcon.getOverlayIcon();
                }
                if (tIcon == null) {
                    return;
                }
                /*FluidStack tFluid = GT_Utility.getFluidForFilledItem(aStack, true);
                if ((tOverlay != null) && (tFluid != null) && (tFluid.getFluid() != null)) {
                    tFluidIcon = tFluid.getFluid().getIcon(tFluid);
                }*/
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                GL11.glBlendFunc(770, 771);
                if (tFluidIcon == null) {
                    short[] tModulation = aItem.getRGBa(aStack);
                    GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
                }
                if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                    GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                } else {
                    ItemRenderer.renderItemIn2D(Tessellator.instance, tIcon.getMaxU(), tIcon.getMinV(), tIcon.getMinU(), tIcon.getMaxV(), tIcon.getIconWidth(), tIcon.getIconHeight(), 0.0625F);
                }
                /*if (tFluidIcon != null) {
                    assert (tFluid != null);
                    int tColor = tFluid.getFluid().getColor(tFluid);
                    GL11.glColor3f((tColor >> 16 & 0xFF) / 255.0F, (tColor >> 8 & 0xFF) / 255.0F, (tColor & 0xFF) / 255.0F);
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    GL11.glBlendFunc(770, 771);
                    GL11.glDepthFunc(514);
                    if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                        GT_RenderUtil.renderItemIcon(tFluidIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                    } else {
                        ItemRenderer.renderItemIn2D(Tessellator.instance, tFluidIcon.getMaxU(), tFluidIcon.getMinV(), tFluidIcon.getMinU(), tFluidIcon.getMaxV(), tFluidIcon.getIconWidth(), tFluidIcon.getIconHeight(), 0.0625F);
                    }
                    GL11.glDepthFunc(515);
                }*/
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                if (tOverlay != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                    GL11.glBlendFunc(770, 771);
                    if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                        GT_RenderUtil.renderItemIcon(tOverlay, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                    } else {
                        ItemRenderer.renderItemIn2D(Tessellator.instance, tOverlay.getMaxU(), tOverlay.getMinV(), tOverlay.getMinU(), tOverlay.getMaxV(), tOverlay.getIconWidth(), tOverlay.getIconHeight(), 0.0625F);
                    }
                }
            } else {
                IIcon tIcon;
                if (aItem.mIconList[(aMetaData)].length > 1) {
                    Long[] tStats = null;
                    //Long[] tStats = (Long[]) aItem.mElectricStats.get(Short.valueOf(aMetaData));

                    if ((tStats != null) && (tStats[3].longValue() < 0L)) {
                        long tCharge = aItem.getRealCharge(aStack);

                        if (tCharge <= 0L) {
                            tIcon = aItem.mIconList[(aMetaData)][1];
                        } else {

                            if (tCharge >= tStats[0].longValue()) {
                                tIcon = aItem.mIconList[(aMetaData)][8];
                            } else {
                                tIcon = aItem.mIconList[(aMetaData)][(7 - (int) java.lang.Math.max(0L, java.lang.Math.min(5L, (tStats[0].longValue() - tCharge) * 6L / tStats[0].longValue())))];
                            }
                        }
                    } else {
                        tIcon = aItem.mIconList[(aMetaData)][0];
                    }
                } else {
                    tIcon = aItem.mIconList[(aMetaData)][0];
                }
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                GL11.glBlendFunc(770, 771);
                if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                    GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                } else {
                    ItemRenderer.renderItemIn2D(Tessellator.instance, tIcon.getMaxU(), tIcon.getMinV(), tIcon.getMinU(), tIcon.getMaxV(), tIcon.getIconWidth(), tIcon.getIconHeight(), 0.0625F);
                }
            }
            GL11.glDisable(3042);
        }
    }
}