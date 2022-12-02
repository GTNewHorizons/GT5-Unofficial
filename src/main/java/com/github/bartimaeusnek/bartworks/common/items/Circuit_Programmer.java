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
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.IItemWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class Circuit_Programmer extends GT_Generic_Item implements IElectricItem, IItemWithModularUI {

    private static final int COST_PER_USE = 100;

    public Circuit_Programmer() {
        super("BWCircuitProgrammer", "Circuit Programmer", "Programs Integrated Circuits");
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setHasSubtypes(false);
        this.setCreativeTab(MainMod.BWT);
        GregTech_API.registerCircuitProgrammer(
                s -> s.getItem() instanceof Circuit_Programmer && ElectricItem.manager.canUse(s, COST_PER_USE),
                (s, p) -> {
                    ElectricItem.manager.use(s, COST_PER_USE, p);
                    return s;
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        if (aStack != null && aStack.getTagCompound() != null)
            aList.add(StatCollector.translateToLocal("tooltip.cp.0.name") + " "
                    + (aStack.getTagCompound().getBoolean("HasChip")
                            ? StatCollector.translateToLocal("tooltip.bw.yes.name")
                            : StatCollector.translateToLocal("tooltip.bw.no.name")));
        aList.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (ElectricItem.manager.use(aStack, COST_PER_USE, aPlayer)) {
            GT_UIInfos.openPlayerHeldItemUI(aPlayer);
        }
        return aStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (this.getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
        if (this.getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        this.mIcon = aIconRegister.registerIcon("bartworks:CircuitProgrammer");
    }

    @Override
    public int getTier(ItemStack var1) {
        return 1;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return GT_Values.V[1];
    }

    private static final String NBT_KEY_HAS_CHIP = "HasChip";
    private static final String NBT_KEY_CHIP_CONFIG = "ChipConfig";

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext, ItemStack heldStack) {
        ModularWindow.Builder builder = ModularWindow.builder(256, 166);
        builder.setBackground(BW_UITextures.BACKGROUND_CIRCUIT_PROGRAMMER);
        builder.bindPlayerInventory(buildContext.getPlayer(), new Pos2d(86, 83), ModularUITextures.ITEM_SLOT);

        ItemStackHandler inventoryHandler = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        SlotWidget circuitSlotWidget = new SlotWidget(new BaseSlot(inventoryHandler, 0) {
            @Override
            public void putStack(ItemStack stack) {
                if (isLVCircuit(stack)) {
                    stack = createRealCircuit(0);
                }
                ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(getSlotIndex(), stack);
                this.onSlotChanged();
            }
        });

        ItemStack initialStack = null;
        NBTTagCompound tag = heldStack.getTagCompound();
        if (tag != null && tag.getBoolean(NBT_KEY_HAS_CHIP)) {
            initialStack = createRealCircuit(tag.getByte(NBT_KEY_CHIP_CONFIG));
        }
        circuitSlotWidget.getMcSlot().putStack(initialStack);

        builder.widget(circuitSlotWidget
                .setChangeListener(widget -> {
                    ItemStack stack = widget.getMcSlot().getStack();
                    ItemStack heldItem = widget.getContext().getPlayer().getHeldItem();
                    NBTTagCompound tag2 = heldItem.getTagCompound();
                    if (tag2 == null) {
                        tag2 = new NBTTagCompound();
                    }

                    if (stack != null) {
                        tag2.setBoolean(NBT_KEY_HAS_CHIP, true);
                        tag2.setByte(NBT_KEY_CHIP_CONFIG, (byte) stack.getItemDamage());
                    } else {
                        tag2.setBoolean(NBT_KEY_HAS_CHIP, false);
                    }
                    heldItem.setTagCompound(tag2);
                })
                .setFilter(stack -> isProgrammedCircuit(stack) || isLVCircuit(stack))
                .setBackground(ModularUITextures.ITEM_SLOT, GT_UITextures.OVERLAY_SLOT_INT_CIRCUIT)
                .setPos(122, 60));

        for (int i = 0; i < 24; i++) {
            final int index = i;
            builder.widget(new ButtonWidget()
                    .setOnClick((clickData, widget) -> {
                        if (circuitSlotWidget.getMcSlot().getHasStack()
                                && isProgrammedCircuit(
                                        circuitSlotWidget.getMcSlot().getStack())) {
                            circuitSlotWidget.getMcSlot().putStack(createRealCircuit(index + 1));
                        }
                    })
                    .setPos(32 + (i % 12) * 18, 21 + (i / 12) * 18)
                    .setSize(18, 18));
        }

        return builder.build();
    }

    private ItemStack createRealCircuit(int config) {
        return ItemList.Circuit_Integrated.getWithDamage(1, config);
    }

    private boolean isProgrammedCircuit(ItemStack stack) {
        return stack.getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem());
    }

    private boolean isLVCircuit(ItemStack stack) {
        return BW_Util.checkStackAndPrefix(stack)
                && GT_OreDictUnificator.getAssociation(stack).mPrefix.equals(OrePrefixes.circuit)
                && GT_OreDictUnificator.getAssociation(stack)
                        .mMaterial
                        .mMaterial
                        .equals(Materials.Basic);
    }
}
