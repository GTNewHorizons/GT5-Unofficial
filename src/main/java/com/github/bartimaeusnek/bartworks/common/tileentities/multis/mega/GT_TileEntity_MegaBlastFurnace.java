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
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_MegaBlastFurnace extends GT_MetaTileEntity_ElectricBlastFurnace {

    private int mHeatingCapacity = 0;
    private byte glasTier = 0;
    private int polPtick = super.getPollutionPerTick(null) * ConfigHandler.megaMachinesMax;

    public GT_TileEntity_MegaBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaBlastFurnace(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{"Controller Block for the Blast Furnace",
                "Size(WxHxD): 15x20x15 (Hollow)",
                "Controller 2nd Layer Middle Center",
                "Inner 14x18x14 Heating Coils (Hollow)",
                "Outer 15x18x15 Boronsilicate Glass",
                "The glass limits the the Energy Input tier",
                "1+ Input Hatch/Bus (Any casing)",
                "1+ Output Hatch/Bus (Any casing)",
                "1+ Energy Hatch (Any casing)",
                "1x Maintenance Hatch (Any casing)",
                "14x14 Muffler Hatches (Top middle)",
                "Heat Proof Machine Casings for the outer 15x15 (Layer 20)",
                "1+ Output Hatch to recover CO2/CO/SO2 (optional, any top layer casing),",
                "    Recovery scales with Muffler Hatch tier",
                "Heat Proof Machine Casings for Base",
                "Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)",
                "Each 1800K over the min. Heat Capacity allows for one upgraded overclock",
                "Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%",
                "Causes maximal" + 20 * this.getPollutionPerTick((ItemStack) null) + " Pollution per second"
        };
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = (ItemStack[]) this.getStoredInputs().toArray(new ItemStack[0]);
        FluidStack[] tFluids = (FluidStack[]) this.getStoredFluids().toArray(new FluidStack[0]);

        ArrayList<ItemStack> outputItems = new ArrayList<ItemStack>();
        ArrayList<FluidStack> outputFluids = new ArrayList<FluidStack>();

        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
        boolean found_Recipe = false;
        int processed = 0;
        while (this.getStoredInputs().size() > 0 && processed < ConfigHandler.megaMachinesMax) {
            if (tRecipe != null && this.mHeatingCapacity >= tRecipe.mSpecialValue && tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                found_Recipe = true;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    outputItems.add(tRecipe.getOutput(i));
                }
                for (int i = 0; i < tRecipe.mFluidOutputs.length; i++) {
                    outputFluids.add(tRecipe.getFluidOutput(i));
                }
                ++processed;
            } else
                break;
        }

        if (found_Recipe) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            int tHeatCapacityDivTiers = (mHeatingCapacity - tRecipe.mSpecialValue) / 900;
            byte overclockCount = 0;
            long actualEUT = (long) (tRecipe.mEUt) * processed;
            if (actualEUT > Integer.MAX_VALUE) {
                byte divider = 0;
                while (actualEUT > Integer.MAX_VALUE) {
                    actualEUT = actualEUT / 2;
                    divider++;
                }
                overclockCount = calculateOverclockednessEBF((int) (actualEUT / (divider * 2)), tRecipe.mDuration * (divider * 2), tVoltage);
            } else
                overclockCount = calculateOverclockednessEBF(tRecipe.mEUt * 64, tRecipe.mDuration, tVoltage);
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return false;
            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            if (tHeatCapacityDivTiers > 0) {
                this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
                this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount);//extra free overclocking if possible
                if (this.mMaxProgresstime < 1)
                    this.mMaxProgresstime = 1;//no eu efficiency correction
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

            this.polPtick = super.getPollutionPerTick(null) * processed;
            this.mOutputItems = new ItemStack[outputItems.size()];
            this.mOutputItems = outputItems.toArray(this.mOutputItems);
            this.mOutputFluids = new FluidStack[outputFluids.size()];
            this.mOutputFluids = outputFluids.toArray(this.mOutputFluids);
            this.updateSlots();
            return true;
        }
        return false;
    }

    /**
     * Taken from the GTNH fork, made originally by Tec
     * Calcualtes overclocked ness using long integers
     *
     * @param aEUt      - recipe EUt
     * @param aDuration - recipe Duration
     */
    protected byte calculateOverclockednessEBF(int aEUt, int aDuration, long maxInputVoltage) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage)), timesOverclocked = 0;
        if (mTier == 0) {
            //Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                //make impossible if too long
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = aEUt >> 2;
                mMaxProgresstime = (int) xMaxProgresstime;
            }
            //return 0;
        } else {
            //Long EUt calculation
            long xEUt = aEUt;
            //Isnt too low EUt check?
            long tempEUt = xEUt < V[1] ? V[1] : xEUt;

            mMaxProgresstime = aDuration;

            while (tempEUt <= V[mTier - 1]) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                mMaxProgresstime >>= 1;//this is effect of overclocking
                xEUt = mMaxProgresstime == 0 ? xEUt >> 1 : xEUt << 2;//U know, if the time is less than 1 tick make the machine use less power
                timesOverclocked++;
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = (int) xEUt;
                if (mEUt == 0)
                    mEUt = 1;
                if (mMaxProgresstime == 0)
                    mMaxProgresstime = 1;//set time to 1 tick
            }
        }
        return timesOverclocked;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        mHeatingCapacity = 0;
        HashSet<Boolean> ret = new HashSet<Boolean>();
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, -2, -1, GregTech_API.sBlockCasings1, 11, 7, 11));
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, 17, 18, GregTech_API.sBlockCasings1, 11, 7, false, null, -1, 11));
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 6, -1, 17, GregTech_API.sBlockCasings5, -1, 7, false, false, true, Blocks.air, -1, false, 11));
        for (int y = -1; y < 17; y++) {
            ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, y, y + 1, ItemRegistry.bw_glasses[0], -1, 7, y == 0, false, false, null, -1, true, 11));
            if (!getCoilHeat(iGregTechTileEntity, 7, y, 6))
                return false;
            List<Byte> metasFromLayer = BW_Util.getMetasFromLayer(iGregTechTileEntity, 7, y, y + 1, 7, y == 0, false, false);
            for (Byte meta : metasFromLayer) {
                byte inttier = BW_Util.getTierFromGlasMeta(meta);
                if (glasTier > 0 && inttier != glasTier)
                    return false;
                else if (glasTier == 0)
                    glasTier = inttier;
            }
        }
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 7;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 7;
        for (int z = -6; z <= 6; z++)
            for (int x = -6; x <= 6; x++) {
                if (!addMufflerToMachineList(iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + x, 17, zDir + z), 11)) {
                    return false;
                }
            }
        if (!this.mOutputHatches.isEmpty()) {
            for (GT_MetaTileEntity_Hatch_Output hatchOutput : mOutputHatches)
                if (hatchOutput.getBaseMetaTileEntity().getYCoord() < iGregTechTileEntity.getYCoord())
                    return false;
        }

        if (glasTier != 8 && !mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : mEnergyHatches) {
                if (glasTier > hatchEnergy.mTier)
                    return false;
            }

        return !ret.contains(Boolean.FALSE) && !this.mMaintenanceHatches.isEmpty() && !this.mOutputBusses.isEmpty() && !this.mInputBusses.isEmpty();
    }

    private boolean getCoilHeat(IGregTechTileEntity iGregTechTileEntity, int offset, int y, int radius) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * offset;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * offset;
        int internalH = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.abs(x) < radius && Math.abs(z) != radius)
                    continue;
                byte tUsedMeta = iGregTechTileEntity.getMetaIDOffset(xDir + x, y, zDir + z);
                switch (tUsedMeta) {
                    case 0:
                        internalH = 1801;
                        break;
                    case 1:
                        internalH = 2701;
                        break;
                    case 2:
                        internalH = 3601;
                        break;
                    case 3:
                        internalH = 4501;
                        break;
                    case 4:
                        internalH = 5401;
                        break;
                    case 5:
                        internalH = 7201;
                        break;
                    case 6:
                        internalH = 9001;
                        break;
                    case 7:
                        internalH = 12001;
                        break;
                    case 8:
                        internalH = 15001;
                        break;
                    default:
                        break;
                }
                if (mHeatingCapacity > 0 && internalH != mHeatingCapacity)
                    return false;
                else if (mHeatingCapacity == 0)
                    mHeatingCapacity = internalH;
            }
        }
        return true;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return polPtick;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_MegaBlastFurnace(this.mName);
    }
}
