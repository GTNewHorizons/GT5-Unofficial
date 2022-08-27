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
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BW_Container_RadioHatch extends Container {

    public byte coverage, dcoverage;
    long timer;
    private final GT_MetaTileEntity_RadioHatch iGregTechTileEntity;

    public BW_Container_RadioHatch(InventoryPlayer inventory, IMetaTileEntity iGregTechTileEntity) {
        this.iGregTechTileEntity = (GT_MetaTileEntity_RadioHatch) iGregTechTileEntity;

        IInventory inv = new IInventory() {
            @Override
            public int getSizeInventory() {
                return 0;
            }

            @Override
            public ItemStack getStackInSlot(int p_70301_1_) {
                return null;
            }

            @Override
            public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
                return null;
            }

            @Override
            public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
                return null;
            }

            @Override
            public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {}

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
                return 0;
            }

            @Override
            public void markDirty() {}

            @Override
            public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
                return false;
            }

            @Override
            public void openInventory() {}

            @Override
            public void closeInventory() {}

            @Override
            public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
                return false;
            }
        };

        for (int i = 0; i < 12; i++) {
            this.addSlotToContainer(new GT_Slot_Holo(inv, i, -64 + i * 18, 22, false, false, 1));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void detectAndSendChanges() {
        if (!this.iGregTechTileEntity.getBaseMetaTileEntity().isClientSide()
                && this.iGregTechTileEntity.getBaseMetaTileEntity().getMetaTileEntity() != null) {
            this.coverage = this.iGregTechTileEntity.getCoverage();
            ++this.timer;
            Iterator var2 = this.crafters.iterator();
            if (this.timer >= Long.MAX_VALUE - 1) this.timer = 0;
            while (true) {
                do {
                    if (!var2.hasNext()) {
                        this.dcoverage = this.coverage;
                        return;
                    }
                    ICrafting var1 = (ICrafting) var2.next();
                    if (this.timer % 500 == 10 || this.dcoverage != this.coverage)
                        var1.sendProgressBarUpdate(this, 0, this.coverage);
                } while (this.timer % 500 != 10 && this.dcoverage != this.coverage);
            }
        }
    }

    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ == 0) this.coverage = (byte) p_75137_2_;
    }

    @Override
    public ItemStack slotClick(int slot, int button, int aShifthold, EntityPlayer entityPlayer) {
        if (slot >= 0 && slot < 12) {
            byte setto;
            switch (slot) {
                case 0:
                    setto = -100;
                    break;
                case 1:
                    setto = -75;
                    break;
                case 2:
                    setto = -50;
                    break;
                case 3:
                    setto = -25;
                    break;
                case 4:
                    setto = -10;
                    break;
                case 5:
                    setto = -1;
                    break;
                case 6:
                    setto = 1;
                    break;
                case 7:
                    setto = 10;
                    break;
                case 8:
                    setto = 25;
                    break;
                case 9:
                    setto = 50;
                    break;
                case 10:
                    setto = 75;
                    break;
                case 11:
                    setto = 100;
                    break;
                default:
                    setto = 0;
            }
            this.iGregTechTileEntity.setCoverage((short) (this.iGregTechTileEntity.getCoverage() + setto));
            this.detectAndSendChanges();
            return null;
        }
        return super.slotClick(slot, button, aShifthold, entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int SlotNR) {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
