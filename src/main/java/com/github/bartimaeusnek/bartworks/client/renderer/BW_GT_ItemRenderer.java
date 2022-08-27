/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.renderer;

import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

@SideOnly(Side.CLIENT)
public class BW_GT_ItemRenderer implements IItemRenderer {
    public BW_GT_ItemRenderer() {
        for (BW_Meta_Items.BW_GT_MetaGen_Item_Hook tItem : BW_Meta_Items.BW_GT_MetaGen_Item_Hook.sInstances) {
            MinecraftForgeClient.registerItemRenderer(tItem, this);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, IItemRenderer.ItemRenderType aType) {
        if (!GT_Utility.isStackInvalid(aStack) && aStack.getItemDamage() >= 0) {
            return aType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                    || aType == IItemRenderer.ItemRenderType.INVENTORY
                    || aType == IItemRenderer.ItemRenderType.EQUIPPED
                    || aType == IItemRenderer.ItemRenderType.ENTITY;
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(
            IItemRenderer.ItemRenderType aType, ItemStack aStack, IItemRenderer.ItemRendererHelper aHelper) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        } else {
            return aType == IItemRenderer.ItemRenderType.ENTITY;
        }
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack aStack, Object... data) {
        if (!GT_Utility.isStackInvalid(aStack)) {
            short aMetaData = (short) aStack.getItemDamage();
            if (aMetaData >= 0) {
                BW_Meta_Items.BW_GT_MetaGen_Item_Hook aItem = (BW_Meta_Items.BW_GT_MetaGen_Item_Hook) aStack.getItem();
                GL11.glEnable(3042);
                if (type == IItemRenderer.ItemRenderType.ENTITY) {
                    if (RenderItem.renderInFrame) {
                        GL11.glScalef(0.85F, 0.85F, 0.85F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }
                    GL11.glTranslated(-0.5D, -0.42D, 0.0D);
                }

                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                IIcon tIcon = (IIcon) BW_Util.get2DCoordFrom1DArray(aMetaData, 0, 2, aItem.mIconList);

                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                GL11.glBlendFunc(770, 771);
                if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                    if (aMetaData < CircuitImprintLoader.reverseIDs)
                        GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
                    else {
                        for (int i = 0; i < 4; i++) {
                            GT_RenderUtil.renderItemIcon(
                                    tIcon,
                                    0.0D + i * 2D,
                                    0.0D + i * 2D,
                                    10.0D + i * 2D,
                                    10.0D + i * 2D,
                                    0.001D,
                                    0.0F,
                                    0.0F,
                                    -1.0F);
                        }
                    }
                } else {
                    if (aMetaData < CircuitImprintLoader.reverseIDs)
                        ItemRenderer.renderItemIn2D(
                                Tessellator.instance,
                                tIcon.getMaxU(),
                                tIcon.getMinV(),
                                tIcon.getMinU(),
                                tIcon.getMaxV(),
                                tIcon.getIconWidth(),
                                tIcon.getIconHeight(),
                                0.0625F);
                    //                    else {
                    //                        for (int i = 0; i < 4; i++) {
                    //                            ItemRenderer.renderItemIn2D(Tessellator.instance,
                    // 0.0F+i*2F,0.0F+i*2F,10.0F+i*2F,10.0F+i*2F, tIcon.getIconWidth(), tIcon.getIconHeight(),0.0625F);
                    //                        }
                    //                    }
                    //                    ItemRenderer.renderItemIn2D(Tessellator.instance, tIcon.getMaxU(),
                    // tIcon.getMinV(), tIcon.getMinU(), tIcon.getMaxV(), tIcon.getIconWidth(), tIcon.getIconHeight(),
                    // 0.0625F);
                }

                IIcon tOverlay = (IIcon) BW_Util.get2DCoordFrom1DArray(aMetaData, 1, 2, aItem.mIconList);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                if (tOverlay != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                    GL11.glBlendFunc(770, 771);
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
            }

            GL11.glDisable(3042);
        }
    }
}
