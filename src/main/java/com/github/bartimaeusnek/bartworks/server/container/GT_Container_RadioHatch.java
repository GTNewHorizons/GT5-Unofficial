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

import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import java.nio.ByteBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_RadioHatch extends GT_Container_1by1 {

    public byte mass;
    public short sv, sievert, r, g, b;
    public long teTimer, decayTime;
    GT_MetaTileEntity_RadioHatch TE;
    private long timer;
    private static final int packetSize = Byte.BYTES + Short.BYTES * 5 + Long.BYTES * 2;

    private ByteBuffer buffer;

    public GT_Container_RadioHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new RadioSlot(this.mTileEntity, 0, 80, 35));
    }

    @SuppressWarnings("rawtypes")
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.buffer == null) {
            this.buffer = ByteBuffer.allocate(128);
        }
        if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {
            this.TE = (GT_MetaTileEntity_RadioHatch) this.mTileEntity.getMetaTileEntity();
            this.mass = this.TE.getMass();
            this.sievert = (short) this.TE.sievert;
            short[] rgb = this.TE.getColorForGUI();
            this.r = rgb[0];
            this.g = rgb[1];
            this.b = rgb[2];
            this.sv = (short) this.TE.getSievert();
            this.teTimer = this.TE.getTimer();
            this.decayTime = this.TE.getDecayTime();
            ++this.timer;
            if (this.timer >= Long.MAX_VALUE - 1) this.timer = 0;
            this.buffer.put(0, mass);
            this.buffer.putShort(Byte.BYTES, sv);
            this.buffer.putShort(Byte.BYTES + Short.BYTES, sievert);
            this.buffer.putShort(Byte.BYTES + Short.BYTES * 2, r);
            this.buffer.putShort(Byte.BYTES + Short.BYTES * 3, g);
            this.buffer.putShort(Byte.BYTES + Short.BYTES * 4, b);
            this.buffer.putLong(Byte.BYTES + Short.BYTES * 5, teTimer);
            this.buffer.putLong(Byte.BYTES + Short.BYTES * 5 + Long.BYTES, decayTime);
            for (Object clientHandle : this.crafters) {
                sendStateUpdate((ICrafting) clientHandle);
            }
        }
    }

    private void sendStateUpdate(ICrafting clientHandle) {
        for (int i = 0; i < packetSize; i++) {
            clientHandle.sendProgressBarUpdate(this, i + 300, buffer.get(i));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting clientHandle) {
        super.addCraftingToCrafters(clientHandle);
        this.buffer.put(0, mass);
        this.buffer.putShort(Byte.BYTES, sv);
        this.buffer.putShort(Byte.BYTES + Short.BYTES, sievert);
        this.buffer.putShort(Byte.BYTES + Short.BYTES * 2, r);
        this.buffer.putShort(Byte.BYTES + Short.BYTES * 3, g);
        this.buffer.putShort(Byte.BYTES + Short.BYTES * 4, b);
        this.buffer.putLong(Byte.BYTES + Short.BYTES * 5, teTimer);
        this.buffer.putLong(Byte.BYTES + Short.BYTES * 5 + Long.BYTES, decayTime);
        sendStateUpdate(clientHandle);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        super.updateProgressBar(index, value);
        index = index - 300;
        if (index >= 0 && index < buffer.capacity()) {
            this.buffer.put(index, (byte) value);
        }
        if (index >= packetSize - 1) {
            this.mass = this.buffer.get(0);
            this.sv = this.buffer.getShort(Byte.BYTES);
            this.sievert = this.buffer.getShort(Byte.BYTES + Short.BYTES);
            this.r = this.buffer.getShort(Byte.BYTES + Short.BYTES * 2);
            this.g = this.buffer.getShort(Byte.BYTES + Short.BYTES * 3);
            this.b = this.buffer.getShort(Byte.BYTES + Short.BYTES * 4);
            this.teTimer = this.buffer.getLong(Byte.BYTES + Short.BYTES * 5);
            this.decayTime = this.buffer.getLong(Byte.BYTES + Short.BYTES * 5 + Long.BYTES);
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    // Handle shift-clicking
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int id) {
        Slot slot = (Slot) this.inventorySlots.get(id);
        ItemStack stack = slot.getStack();
        if (stack == null) return null;
        if (slot instanceof RadioSlot) return super.transferStackInSlot(player, id);
        else if (BWRecipes.instance.getMappingsFor(BWRecipes.RADHATCH).containsInput(stack))
            return super.transferStackInSlot(player, id);
        else return null;
    }

    private static class RadioSlot extends Slot {
        public RadioSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return BWRecipes.instance.getMappingsFor(BWRecipes.RADHATCH).containsInput(stack);
        }
    }
}
