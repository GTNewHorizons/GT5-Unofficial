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

package com.github.bartimaeusnek.bartworks.server.container;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.items.Circuit_Programmer;
import com.github.bartimaeusnek.bartworks.common.net.CircuitProgrammerPacket;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Render;
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

    EntityPlayer player;

    public GT_Container_CircuitProgrammer(InventoryPlayer inventory) {

        this.player = inventory.player;

        IInventory inv = new pinv(this.player);

        this.addSlotToContainer(new Slot(inv, 0, 44, 61)); // -45, 84));

        for (int i = 1; i < 13; i++) {
            this.addSlotToContainer(new GT_Slot_Holo(inv, i, -64 + i * 18, 22, false, false, 1));
        }
        for (int i = 0; i < 12; i++) {
            this.addSlotToContainer(new GT_Slot_Holo(inv, i + 12, -46 + i * 18, 40, false, false, 1));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            if (GT_Utility.isStackValid(inventory.getStackInSlot(i))
                    && inventory.getStackInSlot(i).getItem() instanceof Circuit_Programmer)
                this.addSlotToContainer(new GT_Slot_Render(inventory, i, 8 + i * 18, 142));
            else this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack slotClick(int slot, int button, int aShifthold, EntityPlayer entityPlayer) {
        if (slot > 0 && slot < 25 && ((Slot) this.inventorySlots.get(0)).getStack() != null) {
            ItemStack iCircuit = GT_Utility.getIntegratedCircuit(slot);
            iCircuit.stackSize = 1;
            ((Slot) this.inventorySlots.get(0)).putStack(iCircuit);
            this.detectAndSendChanges();
            return ((Slot) this.inventorySlots.get(0)).getStack();
        }
        this.detectAndSendChanges();
        return super.slotClick(slot, button, aShifthold, entityPlayer);
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
            if (slot != null
                    && slot.getStack() != null
                    && slot.getStack()
                            .getItem()
                            .equals(GT_Utility.getIntegratedCircuit(0).getItem())) {
                if (chipslot.getStack() == null) {
                    chipslot.putStack(slot.getStack().copy());
                    slot.decrStackSize(1);
                }
            }
        } else if (SlotNR == 0 && chipslot.getStack() != null) {
            for (int i = 25; i < this.inventorySlots.size(); i++) {
                if (((Slot) this.inventorySlots.get(i)).getStack() == null) {
                    Slot empty = ((Slot) this.inventorySlots.get(i));
                    empty.putStack(chipslot.getStack().copy());
                    chipslot.decrStackSize(1);
                    break;
                }
            }
        }
        this.detectAndSendChanges();
        return null;
    }

    class pinv implements IInventory {

        ItemStack toBind;
        EntityPlayer Player;
        ItemStack Slot;
        NBTTagCompound tag;

        public pinv(EntityPlayer Player) {
            this.Player = Player;
            this.toBind = Player.inventory.getCurrentItem();
            this.tag = this.toBind.getTagCompound();
            if (this.tag.getBoolean("HasChip")) {
                this.Slot = GT_Utility.getIntegratedCircuit(this.tag.getByte("ChipConfig"));
                this.Slot.stackSize = 1;
            }
        }

        @Override
        public int getSizeInventory() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot == 0 ? this.Slot : null;
        }

        @Override
        public ItemStack decrStackSize(int slotNR, int count) {
            ItemStack ret = this.Slot.copy();
            this.Slot = null;
            this.tag = this.toBind.getTagCompound();
            this.tag.setBoolean("HasChip", false);
            this.toBind.setTagCompound(this.tag);
            this.Player.inventory.setInventorySlotContents(this.Player.inventory.currentItem, this.toBind);
            return ret;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
            return this.Slot;
        }

        @Override
        public void setInventorySlotContents(int slotNR, ItemStack itemStack) {
            if (itemStack != null
                    && itemStack.getItem() != null
                    && itemStack
                            .getItem()
                            .equals(GT_Utility.getIntegratedCircuit(0).getItem())) {
                this.Slot = BW_Util.setStackSize(itemStack.copy(), 1);
                itemStack.stackSize--;
                this.tag = this.toBind.getTagCompound();
                this.tag.setBoolean("HasChip", true);
                this.tag.setByte("ChipConfig", (byte) itemStack.getItemDamage());
                this.toBind.setTagCompound(this.tag);
                this.Player.inventory.setInventorySlotContents(this.Player.inventory.currentItem, this.toBind);
                if (!this.Player.isClientWorld())
                    MainMod.BW_Network_instance.sendToServer(new CircuitProgrammerPacket(
                            this.Player.worldObj.provider.dimensionId, this.Player.getEntityId(), true, (byte)
                                    itemStack.getItemDamage()));
            } else if (BW_Util.checkStackAndPrefix(itemStack)
                    && GT_OreDictUnificator.getAssociation(itemStack).mPrefix.equals(OrePrefixes.circuit)
                    && GT_OreDictUnificator.getAssociation(itemStack)
                            .mMaterial
                            .mMaterial
                            .equals(Materials.Basic)) {
                this.Slot = GT_Utility.getIntegratedCircuit(0);
                this.Slot.stackSize = 1;
                itemStack.stackSize--;
                this.tag = this.toBind.getTagCompound();
                this.tag.setBoolean("HasChip", true);
                this.tag.setByte("ChipConfig", (byte) 0);
                this.toBind.setTagCompound(this.tag);
                this.Player.inventory.setInventorySlotContents(this.Player.inventory.currentItem, this.toBind);
                if (!this.Player.isClientWorld())
                    MainMod.BW_Network_instance.sendToServer(new CircuitProgrammerPacket(
                            this.Player.worldObj.provider.dimensionId, this.Player.getEntityId(), true, (byte) 0));
            } /* else if (GT_Utility.isStackValid(itemStack) && itemStack.getItem() instanceof Circuit_Programmer) {
                  ForgeHooks.onPlayerTossEvent(Player, itemStack, false);
                  this.closeInventory();
                  Player.closeScreen();
              }*/ else {
                ForgeHooks.onPlayerTossEvent(this.Player, itemStack, false);
                this.tag = this.toBind.getTagCompound();
                this.tag.setBoolean("HasChip", false);
                this.toBind.setTagCompound(this.tag);
                this.Player.inventory.setInventorySlotContents(this.Player.inventory.currentItem, this.toBind);
                if (!this.Player.isClientWorld())
                    MainMod.BW_Network_instance.sendToServer(new CircuitProgrammerPacket(
                            this.Player.worldObj.provider.dimensionId, this.Player.getEntityId(), false, (byte) 0));
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
        public void markDirty() {}

        @Override
        public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
            return true;
        }

        @Override
        public void openInventory() {}

        @Override
        public void closeInventory() {}

        @Override
        public boolean isItemValidForSlot(int p_94041_1_, ItemStack itemStack) {
            return itemStack != null
                    && itemStack
                            .getItem()
                            .equals(GT_Utility.getIntegratedCircuit(0).getItem());
        }
    }
}
