/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.server.container;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;

public class GT_Container_CircuitProgrammer extends Container {

    public GT_Container_CircuitProgrammer(InventoryPlayer inventory) {

        IInventory inv = new pinv(inventory.player);

        addSlotToContainer(new Slot(inv, 0, 44, 61));//-45, 84));

        for (int i = 1; i < 13; i++) {
            addSlotToContainer(new GT_Slot_Holo(inv, i, -64 + i * 18, 22, false, false, 1));
        }
        for (int i = 0; i < 12; i++) {
            addSlotToContainer(new GT_Slot_Holo(inv, i + 12, -46 + i * 18, 40, false, false, 1));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }

    }

    @Override
    public ItemStack slotClick(int slot, int button, int aShifthold, EntityPlayer entityPlayer) {
        if (slot > 0 && slot < 25 && ((Slot) this.inventorySlots.get(0)).getStack() != null) {
            ((Slot) this.inventorySlots.get(0)).getStack().setItemDamage(slot);
            detectAndSendChanges();
            return ((Slot) this.inventorySlots.get(0)).getStack();
        }
        return super.slotClick(slot, button, aShifthold, entityPlayer);//( (Slot) this.inventorySlots.get(slot)).getStack();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int SlotNR) {
        Slot chipslot = (Slot) this.inventorySlots.get(0);
        if (SlotNR > 24) {
            Slot slot = (Slot) this.inventorySlots.get(SlotNR);
            if (slot != null && slot.getStack().getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem())) {
                if (chipslot.getStack() == null) {
                    chipslot.putStack(slot.getStack());
                    slot.decrStackSize(1);
                }
            }
        } else if (SlotNR == 0 && chipslot.getStack() != null) {
            for (int i = 25; i < this.inventorySlots.size(); i++) {
                if (((Slot) this.inventorySlots.get(i)).getStack() == null) {
                    Slot empty = ((Slot) this.inventorySlots.get(i));
                    empty.putStack(chipslot.getStack());
                    chipslot.decrStackSize(1);
                    break;
                }
            }
        }
        return null;
    }

    class pinv implements IInventory {

        ItemStack toBind;
        EntityPlayer Player;
        ItemStack Slot;
        NBTTagCompound tag;

        public pinv(EntityPlayer Player) {
            super();
            this.Player = Player;
            this.toBind = Player.inventory.getCurrentItem();
            tag = this.toBind.getTagCompound();
            if (tag.getBoolean("HasChip"))
                Slot = GT_Utility.getIntegratedCircuit(tag.getByte("ChipConfig"));
        }

        @Override
        public int getSizeInventory() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot == 0 ? Slot : null;
        }

        @Override
        public ItemStack decrStackSize(int slotNR, int count) {
            ItemStack ret = Slot.copy();
            Slot = null;
            tag = toBind.getTagCompound();
            tag.setBoolean("HasChip", false);
            toBind.setTagCompound(tag);
            Player.inventory.setInventorySlotContents(Player.inventory.currentItem, toBind);
            return ret;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
            return Slot;
        }

        @Override
        public void setInventorySlotContents(int slotNR, ItemStack itemStack) {
            if (itemStack != null && itemStack.getItem() != null && itemStack.getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem())) {
                Slot = itemStack.copy().splitStack(1);
                itemStack.stackSize--;
                tag = toBind.getTagCompound();
                tag.setBoolean("HasChip", true);
                tag.setByte("ChipConfig", (byte) itemStack.getItemDamage());
                toBind.setTagCompound(tag);
                Player.inventory.setInventorySlotContents(Player.inventory.currentItem, toBind);
            } else if (BW_Util.checkStackAndPrefix(itemStack) && GT_OreDictUnificator.getAssociation(itemStack).mPrefix.equals(OrePrefixes.circuit) && GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial.equals(Materials.Basic)) {
                Slot = GT_Utility.getIntegratedCircuit(0);
                itemStack.stackSize--;
                tag = toBind.getTagCompound();
                tag.setBoolean("HasChip", true);
                tag.setByte("ChipConfig", (byte) itemStack.getItemDamage());
                toBind.setTagCompound(tag);
                Player.inventory.setInventorySlotContents(Player.inventory.currentItem, toBind);
            } else {
                ForgeHooks.onPlayerTossEvent(Player, itemStack, false);
                tag = toBind.getTagCompound();
                tag.setBoolean("HasChip", false);
                toBind.setTagCompound(tag);
            }
        }

        @Override
        public String getInventoryName() {
            return null;
        }

        @Override
        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
            return true;
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int p_94041_1_, ItemStack itemStack) {
            if (itemStack != null && itemStack.getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem()))
                return true;
            return false;
        }
    }

}
