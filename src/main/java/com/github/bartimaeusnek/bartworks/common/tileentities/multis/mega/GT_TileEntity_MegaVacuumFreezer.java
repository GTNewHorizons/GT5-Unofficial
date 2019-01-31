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

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_VacuumFreezer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_MegaVacuumFreezer extends GT_MetaTileEntity_VacuumFreezer {

    public GT_TileEntity_MegaVacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaVacuumFreezer(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Mega Vacuum Freezer",
                "Super cools hot ingots and cells",
                "Size(WxHxD): 15x15x15 (Hollow), Controller (Front centered)",
                "1x Input Bus (Any casing)",
                "1x Output Bus (Any casing)",
                "1x Maintenance Hatch (Any casing)",
                "1x Energy Hatch (Any casing)",
                "Frost Proof Machine Casings for the rest"
        };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaVacuumFreezer(this.mName);
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = (ItemStack[]) this.getStoredInputs().toArray(new ItemStack[0]);
        ArrayList<ItemStack> outputItems = new ArrayList<ItemStack>();

        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], null, tInputs);
        boolean found_Recipe = false;
        int processed = 0;
        while (this.getStoredInputs().size() > 0 && processed < ConfigHandler.megaMachinesMax) {
            if (tRecipe != null && tRecipe.isRecipeInputEqual(true, null, tInputs)) {
                found_Recipe = true;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    outputItems.add(tRecipe.getOutput(i));
                }
                ++processed;
            } else
                break;
        }

        if (found_Recipe) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            long actualEUT = (long) (tRecipe.mEUt) * processed;
            if (actualEUT > Integer.MAX_VALUE) {
                byte divider = 0;
                while (actualEUT > Integer.MAX_VALUE) {
                    actualEUT = actualEUT / 2;
                    divider++;
                }
                BW_Util.calculateOverclockedNessMulti((int) (actualEUT / (divider * 2)), tRecipe.mDuration * (divider * 2), 1, this.getMaxInputVoltage(), this);
            } else
                BW_Util.calculateOverclockedNessMulti((int) actualEUT, tRecipe.mDuration, 1, this.getMaxInputVoltage(), this);
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return false;
            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputItems = new ItemStack[outputItems.size()];
            this.mOutputItems = outputItems.toArray(this.mOutputItems);
            this.updateSlots();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        HashSet<Boolean> ret = new HashSet<Boolean>();
        ret.add(BW_Util.check_layer(aBaseMetaTileEntity, 7, -7, -6, GregTech_API.sBlockCasings2, 1, 7, false, 17));
        ret.add(BW_Util.check_layer(aBaseMetaTileEntity, 7, -6, 0, GregTech_API.sBlockCasings2, 1, 7, false, false, true, Blocks.air, -1, true, 17));
        ret.add(BW_Util.check_layer(aBaseMetaTileEntity, 7, 0, 1, GregTech_API.sBlockCasings2, 1, 7, true, false, true, Blocks.air, -1, true, 17));
        ret.add(BW_Util.check_layer(aBaseMetaTileEntity, 7, 1, 7, GregTech_API.sBlockCasings2, 1, 7, false, false, true, Blocks.air, -1, true, 17));
        ret.add(BW_Util.check_layer(aBaseMetaTileEntity, 7, 7, 8, GregTech_API.sBlockCasings2, 1, 7, false, 17));
        return !(ret.contains(Boolean.FALSE) || this.mInputBusses.isEmpty() || this.mOutputBusses.isEmpty() || this.mEnergyHatches.isEmpty() || this.mMaintenanceHatches.isEmpty());
    }


}
