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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ProcessingArray;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine.isValidForLowGravity;

public class GT_TileEntity_MegaProcessingArray extends GT_MetaTileEntity_ProcessingArray {
    private GT_Recipe mLastRecipe;
    private int tTier;
    private int mMult;
    private String mMachine = "";
    private GT_MetaTileEntity_Hatch_InputBus machineBus;
    public GT_TileEntity_MegaProcessingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaProcessingArray(String aName) {
        super(aName);
    }

    public boolean checkRecipe(ItemStack aStack) {
        if (!this.isCorrectMachinePart(this.machineBus.mInventory[0])) {
            return false;
        }

        GT_Recipe.GT_Recipe_Map map = this.getRecipeMap();
        if (map == null) return false;
        ArrayList<ItemStack> tInputList = this.getStoredInputs();

        if (this.mInventory[1].getUnlocalizedName().endsWith("10")) {
            this.tTier = 9;
            this.mMult = 2;//u need 4x less machines and they will use 4x less power
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("11")) {
            this.tTier = 9;
            this.mMult = 4;//u need 16x less machines and they will use 16x less power
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("12") ||
                this.mInventory[1].getUnlocalizedName().endsWith("13") ||
                this.mInventory[1].getUnlocalizedName().endsWith("14") ||
                this.mInventory[1].getUnlocalizedName().endsWith("15")) {
            this.tTier = 9;
            this.mMult = 6;//u need 64x less machines and they will use 64x less power
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("1")) {
            this.tTier = 1;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("2")) {
            this.tTier = 2;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("3")) {
            this.tTier = 3;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("4")) {
            this.tTier = 4;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("5")) {
            this.tTier = 5;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("6")) {
            this.tTier = 6;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("7")) {
            this.tTier = 7;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("8")) {
            this.tTier = 8;
            this.mMult = 0;//*1
        } else if (this.mInventory[1].getUnlocalizedName().endsWith("9")) {
            this.tTier = 9;
            this.mMult = 0;//*1
        } else {
            this.tTier = 0;
            this.mMult = 0;//*1
        }

        if (!this.mMachine.equals(this.mInventory[1].getUnlocalizedName())) this.mLastRecipe = null;
        this.mMachine = this.mInventory[1].getUnlocalizedName();
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[tInputList.size()]);

        ArrayList<FluidStack> tFluidList = this.getStoredFluids();

        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);
        if (tInputList.size() > 0 || tFluids.length > 0) {
            GT_Recipe tRecipe = map.findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false, gregtech.api.enums.GT_Values.V[this.tTier], tFluids, tInputs);
            if (tRecipe != null) {
                if (GT_Mod.gregtechproxy.mLowGravProcessing && tRecipe.mSpecialValue == -100 &&
                        !isValidForLowGravity(tRecipe, this.getBaseMetaTileEntity().getWorld().provider.dimensionId))
                    return false;

                this.mLastRecipe = tRecipe;
                this.mEUt = 0;
                this.mOutputItems = null;
                this.mOutputFluids = null;
                int machines = Math.min(64, this.mInventory[1].stackSize << this.mMult); //Upped max Cap to 64
                int i = 0;
                for (; i < machines; i++) {
                    if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                        if (i == 0) {
                            return false;
                        }
                        break;
                    }
                }
                this.mMaxProgresstime = tRecipe.mDuration;
                this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                BW_Util.calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, map.mAmperage, GT_Values.V[this.tTier], this);
                //In case recipe is too OP for that machine
                if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
                    return false;
                this.mEUt = GT_Utility.safeInt(((long) this.mEUt * i) >> this.mMult, 1);
                if (this.mEUt == Integer.MAX_VALUE - 1)
                    return false;

                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
                for (int h = 0; h < tRecipe.mOutputs.length; h++) {
                    if (tRecipe.getOutput(h) != null) {
                        tOut[h] = tRecipe.getOutput(h).copy();
                        tOut[h].stackSize = 0;
                    }
                }
                FluidStack tFOut = null;
                if (tRecipe.getFluidOutput(0) != null) tFOut = tRecipe.getFluidOutput(0).copy();
                for (int f = 0; f < tOut.length; f++) {
                    if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
                        for (int g = 0; g < i; g++) {
                            if (this.getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
                                tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
                        }
                    }
                }
                if (tFOut != null) {
                    int tSize = tFOut.amount;
                    tFOut.amount = tSize * i;
                }
                tOut = GT_MetaTileEntity_ProcessingArray.clean(tOut);
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                List<ItemStack> overStacks = new ArrayList<ItemStack>();
                for (int f = 0; f < tOut.length; f++) {
                    while (tOut[f].getMaxStackSize() < tOut[f].stackSize) {
                        if (tOut[f] != null) {
                            ItemStack tmp = tOut[f].copy();
                            tmp.stackSize = tmp.getMaxStackSize();
                            tOut[f].stackSize = tOut[f].stackSize - tOut[f].getMaxStackSize();
                            overStacks.add(tmp);
                        }
                    }
                }
                if (overStacks.size() > 0) {
                    ItemStack[] tmp = new ItemStack[overStacks.size()];
                    tmp = overStacks.toArray(tmp);
                    tOut = ArrayUtils.addAll(tOut, tmp);
                }
                List<ItemStack> tSList = new ArrayList<ItemStack>();
                for (ItemStack tS : tOut) {
                    if (tS.stackSize > 0) tSList.add(tS);
                }
                tOut = tSList.toArray(new ItemStack[tSList.size()]);
                this.mOutputItems = tOut;
                this.mOutputFluids = new FluidStack[]{tFOut};
                this.updateSlots();
                return true;
            }/* else{
                ...remoteRecipeCheck()
            }*/
        }
        return false;
    }

}
