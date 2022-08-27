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

package com.github.bartimaeusnek.crossmod.thaumcraft.tile;

import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.util.GT_ModHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

public class GT_WandBuffer extends GT_MetaTileEntity_BasicBatteryBuffer {
    public GT_WandBuffer(int aID, String aName, String aNameRegional, int aTier, String aDescription, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    public GT_WandBuffer(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    public GT_WandBuffer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mBatteryCount = 0;
            this.mChargeableCount = 0;

            for (ItemStack tStack : this.mInventory) {
                if (ThaumcraftHandler.isWand(tStack)) {
                    ++this.mBatteryCount;
                    ++this.mChargeableCount;
                }
            }
        }
    }

    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return ThaumcraftHandler.isWand(aStack);
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return ThaumcraftHandler.isWand(aStack);
    }

    public int getInventoryStackLimit() {
        return 1;
    }

    public long[] getStoredEnergy() {
        boolean scaleOverflow = false;
        boolean storedOverflow = false;
        long tScale = this.getBaseMetaTileEntity().getEUCapacity();
        long tStored = this.getBaseMetaTileEntity().getStoredEU();
        long tStep;
        if (this.mInventory != null) {

            for (ItemStack aStack : this.mInventory) {
                if (GT_ModHandler.isElectricItem(aStack)) {
                    if (aStack.getItem() instanceof GT_MetaBase_Item) {
                        Long[] stats = ((GT_MetaBase_Item) aStack.getItem()).getElectricStats(aStack);
                        if (stats != null) {
                            if (stats[0] > 4611686018427387903L) {
                                scaleOverflow = true;
                            }

                            tScale += stats[0];
                            tStep = ((GT_MetaBase_Item) aStack.getItem()).getRealCharge(aStack);
                            if (tStep > 4611686018427387903L) {
                                storedOverflow = true;
                            }

                            tStored += tStep;
                        }
                    } else if (aStack.getItem() instanceof IElectricItem) {
                        tStored += (long) ElectricItem.manager.getCharge(aStack);
                        tScale += (long) ((IElectricItem) aStack.getItem()).getMaxCharge(aStack);
                    }
                }
            }
        }

        if (scaleOverflow) {
            tScale = 9223372036854775807L;
        }

        if (storedOverflow) {
            tStored = 9223372036854775807L;
        }

        return new long[] {tStored, tScale};
    }
}
