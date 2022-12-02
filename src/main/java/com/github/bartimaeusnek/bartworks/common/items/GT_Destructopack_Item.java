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

package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.API.modularUI.BW_UITextures;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.IItemWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.items.GT_Generic_Item;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GT_Destructopack_Item extends GT_Generic_Item implements IItemWithModularUI {

    public GT_Destructopack_Item() {
        super("GT2Destructopack", "Destructopack", "Mobile Trash Bin");
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setHasSubtypes(false);
        this.setCreativeTab(MainMod.GT2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        aList.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        GT_UIInfos.openPlayerHeldItemUI(aPlayer);
        return aStack;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        this.mIcon = aIconRegister.registerIcon("bartworks:gt.GT2Destructopack");
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext, ItemStack heldStack) {
        ModularWindow.Builder builder = ModularWindow.builder(176, 166);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.bindPlayerInventory(buildContext.getPlayer());

        builder.widget(new SlotWidget(new BaseSlot(new ItemStackHandler(), 0) {
                            @Override
                            public void putStack(ItemStack stack) {
                                onSlotChanged();
                            }
                        })
                        .setBackground(ModularUITextures.ITEM_SLOT, BW_UITextures.OVERLAY_SLOT_CROSS)
                        .setPos(79, 16))
                .widget(new DrawableWidget()
                        .setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT)
                        .setSize(17, 17)
                        .setPos(152, 63));

        return builder.build();
    }
}
