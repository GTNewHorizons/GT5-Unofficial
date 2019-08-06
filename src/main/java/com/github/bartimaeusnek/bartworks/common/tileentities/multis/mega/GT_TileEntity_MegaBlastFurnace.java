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
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_MegaBlastFurnace extends GT_MetaTileEntity_ElectricBlastFurnace {

    private int mHeatingCapacity;
    private byte glasTier;
    private int polPtick = super.getPollutionPerTick(null) * ConfigHandler.megaMachinesMax;

    public GT_TileEntity_MegaBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaBlastFurnace(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        String[] dsc = StatCollector.translateToLocal("tooltip.tile.mbf.0.name").split(";");
        String tmp = dsc[dsc.length - 1];
        dsc[dsc.length - 1] = tmp + " " + 20 * this.getPollutionPerTick(null) + " " + StatCollector.translateToLocal("tooltip.tile.mbf.1.name");
        String[] fdsc = new String[dsc.length + 1];
        for (int i = 0; i < dsc.length; i++) {
            fdsc[i] = dsc[i];
            fdsc[dsc.length] = StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks";
        }
        return fdsc;
    }

    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0)
            return true;
        long allTheEu = 0;
        int hatches = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches)
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                allTheEu += tHatch.getEUVar();
                hatches++;
            }
        if (allTheEu < aEU)
            return false;
        long euperhatch = aEU / hatches;
        HashSet<Boolean> returnset = new HashSet<>();
        for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches)
            if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euperhatch, false))
                returnset.add(true);
            else
                returnset.add(false);
        return returnset.size() > 0 && !returnset.contains(false);
    }

//    @Override
//    public long getMaxInputVoltage() {
//        long rVoltage = 0L;
//        Iterator var3 = this.mEnergyHatches.iterator();
//
//        while(var3.hasNext()) {
//            GT_MetaTileEntity_Hatch_Energy tHatch = (GT_MetaTileEntity_Hatch_Energy)var3.next();
//            if (isValidMetaTileEntity(tHatch)) {
//                rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
//            }
//        }
//        //glass tier needs NBT persistance if this is enabled
//        return glasTier != 8 && rVoltage > BW_Util.getTierVoltage(glasTier) ? BW_Util.getTierVoltage(glasTier) : rVoltage ;
//    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
        FluidStack[] tFluids = this.getStoredFluids().toArray(new FluidStack[0]);
        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
        if (tRecipe == null)
            return false;

        ArrayList<ItemStack> outputItems = new ArrayList<ItemStack>();
        ArrayList<FluidStack> outputFluids = new ArrayList<FluidStack>();

        boolean found_Recipe = false;
        int processed = 0;

        long nominalV = BW_Util.getnominalVoltage(this);
        int tHeatCapacityDivTiers = (this.mHeatingCapacity - tRecipe.mSpecialValue) / 900;
        long precutRecipeVoltage = (long) (tRecipe.mEUt * Math.pow(0.95, tHeatCapacityDivTiers));

        while (this.getStoredInputs().size() > 0 && processed < ConfigHandler.megaMachinesMax) {
            if (this.mHeatingCapacity >= tRecipe.mSpecialValue && (precutRecipeVoltage * (processed + 1)) < nominalV && tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
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
            this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            byte overclockCount = 0;
            long actualEUT = (long) (tRecipe.mEUt) * processed;
            if (actualEUT > Integer.MAX_VALUE) {
                byte divider = 0;
                while (actualEUT > Integer.MAX_VALUE) {
                    actualEUT = actualEUT / 2;
                    divider++;
                }
                overclockCount = this.calculateOverclockednessEBF((int) (actualEUT / (divider * 2)), tRecipe.mDuration * (divider * 2), nominalV);
            } else
                overclockCount = this.calculateOverclockednessEBF(actualEUT, tRecipe.mDuration, nominalV);
            //In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
                return false;
            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            if (tHeatCapacityDivTiers > 0) {
                this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
                this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount); //extra free overclocking if possible
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
    protected byte calculateOverclockednessEBF(long aEUt, int aDuration, long maxInputVoltage) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage)), timesOverclocked = 0;
        if (mTier == 0) {
            //Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                //make impossible if too long
                this.mEUt = Integer.MAX_VALUE - 1;
                this.mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                this.mEUt = (int) (aEUt >> 2);
                this.mMaxProgresstime = (int) xMaxProgresstime;
            }
            //return 0;
        } else {
            //Long EUt calculation
            long xEUt = aEUt;
            //Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            this.mMaxProgresstime = aDuration;
            while (tempEUt <= V[mTier - 1]) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                this.mMaxProgresstime >>= 1;//this is effect of overclocking
                xEUt = this.mMaxProgresstime <= 0 ? xEUt >> 1 : xEUt << 2;//U know, if the time is less than 1 tick make the machine use less power
                timesOverclocked++;
            }
            if (xEUt > maxInputVoltage){
                //downclock one notch, we have overshot.
                xEUt >>=2;
                this.mMaxProgresstime <<= 1;
                timesOverclocked--;
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                this.mEUt = Integer.MAX_VALUE - 1;
                this.mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                this.mEUt = (int) xEUt;
                if (this.mEUt == 0)
                    this.mEUt = 1;
                if (this.mMaxProgresstime <= 0)
                    this.mMaxProgresstime = 1;//set time to 1 tick
            }
        }
        return timesOverclocked;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mHeatingCapacity = 0;
        HashSet<Boolean> ret = new HashSet<Boolean>();
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, -2, -1, GregTech_API.sBlockCasings1, 11, 7, false,false,true,GregTech_API.sBlockCasings1,11,true,11));
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, 17, 18, GregTech_API.sBlockCasings1, 11, 7, false, null, -1, 11));
        ret.add(BW_Util.check_layer(iGregTechTileEntity, 6, -1, 17, GregTech_API.sBlockCasings5, -1, 7, false, false, true, Blocks.air, -1, false, 11));
        for (int y = -1; y < 17; y++) {
            ret.add(BW_Util.check_layer(iGregTechTileEntity, 7, y, y + 1, ItemRegistry.bw_glasses[0], -1, 7, y == 0, false, false, null, -1, false, 11));
            if (!this.getCoilHeat(iGregTechTileEntity, 7, y, 6))
                return false;
            List<Byte> metasFromLayer = BW_Util.getMetasFromLayer(iGregTechTileEntity, 7, y, y + 1, 7, y == 0, false, false);
            for (Byte meta : metasFromLayer) {
                byte inttier = BW_Util.getTierFromGlasMeta(meta);
                if (this.glasTier > 0 && inttier != this.glasTier)
                    return false;
                else if (this.glasTier == 0)
                    this.glasTier = inttier;
            }
        }
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 7;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 7;
        for (int z = -6; z <= 6; z++)
            for (int x = -6; x <= 6; x++) {
                if (!this.addMufflerToMachineList(iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + x, 17, zDir + z), 11)) {
                    return false;
                }
            }
        if (!this.mOutputHatches.isEmpty()) {
            for (GT_MetaTileEntity_Hatch_Output hatchOutput : this.mOutputHatches)
                if (hatchOutput.getBaseMetaTileEntity().getYCoord() < iGregTechTileEntity.getYCoord())
                    return false;
        }

        if (this.glasTier != 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches) {
                if (this.glasTier < hatchEnergy.mTier)
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
                        internalH = 9901;
                        break;
                    case 8:
                        internalH = 10801;
                        break;
                    default:
                        break;
                }
                if (this.mHeatingCapacity > 0 && internalH != this.mHeatingCapacity)
                    return false;
                else if (this.mHeatingCapacity == 0)
                    this.mHeatingCapacity = internalH;
            }
        }
        return true;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return this.polPtick;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_MegaBlastFurnace(this.mName);
    }
}
