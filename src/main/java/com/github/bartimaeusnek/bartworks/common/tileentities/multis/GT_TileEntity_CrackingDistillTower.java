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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DistillationTower;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_TileEntity_CrackingDistillTower extends GT_MetaTileEntity_DistillationTower {

    public GT_TileEntity_CrackingDistillTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_CrackingDistillTower(String aName) {
        super(aName);
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (!GT_Utility.areStacksEqual(itemStack, GT_Utility.getIntegratedCircuit(0), true)) return false;
        else {
            FluidStack[] array = new FluidStack[0];
            ArrayList<FluidStack> fluidInputs = new ArrayList<>();
            for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
                if (hatch != null) {
                    fluidInputs.add(hatch.getFluid());
                }
            }
            array = fluidInputs.toArray(array);
            GT_Recipe.GT_Recipe_Map rMapCracking = GT_Recipe.GT_Recipe_Map.sCrakingRecipes;
            GT_Recipe.GT_Recipe_Map rMapDistillTower = GT_Recipe.GT_Recipe_Map.sDistillationRecipes;
            GT_Recipe recipeCracking = rMapCracking.findRecipe(
                    this.getBaseMetaTileEntity(), false, this.getMaxInputVoltage(), array, itemStack);
            if (recipeCracking == null) return false;
            GT_Recipe recipeDistill = rMapDistillTower.findRecipe(
                    this.getBaseMetaTileEntity(), false, this.getMaxInputVoltage(), recipeCracking.mFluidOutputs);
            if (recipeDistill == null) return false;
            float ratio = (float) recipeCracking.mFluidOutputs[0].amount / (float) recipeDistill.mFluidInputs[0].amount;
            FluidStack[] nuoutputs = new FluidStack[recipeDistill.mFluidOutputs.length];
            for (int i = 0; i < nuoutputs.length; i++) {
                nuoutputs[i] = recipeDistill.mFluidOutputs[i];
                nuoutputs[i].amount = MathUtils.floorInt(recipeDistill.mFluidOutputs[i].amount * ratio);
            }
            BWRecipes.DynamicGTRecipe combined = new BWRecipes.DynamicGTRecipe(
                    true,
                    null,
                    recipeDistill.mOutputs,
                    null,
                    recipeDistill.mChances,
                    recipeCracking.mFluidInputs,
                    nuoutputs,
                    (MathUtils.floorInt(recipeDistill.mDuration * ratio)) + recipeCracking.mDuration,
                    Math.max((MathUtils.floorInt(recipeDistill.mEUt * ratio)), recipeCracking.mEUt),
                    0);
            if (combined.isRecipeInputEqual(true, array)) {
                this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                BW_Util.calculateOverclockedNessMulti(
                        combined.mEUt, combined.mDuration, 1, this.getMaxInputVoltage(), this);
                if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1) return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                this.mOutputFluids = combined.mFluidOutputs.clone();
                this.mOutputItems = combined.mOutputs.clone();
                this.updateSlots();
                return true;
            }
        }
        return false;
    }
}
