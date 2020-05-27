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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.MegaUtils;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.bartimaeusnek.crossmod.tectech.TecTechUtils;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.LowPowerLaser;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static gregtech.api.enums.GT_Values.V;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaBlastFurnace extends GT_MetaTileEntity_ElectricBlastFurnace implements TecTechEnabledMulti {

    private int mHeatingCapacity;
    private byte glasTier;
    private int polPtick = super.getPollutionPerTick(null) * ConfigHandler.megaMachinesMax;
    static Field controllerY;

    static {
        try {
            controllerY = GT_MetaTileEntity_ElectricBlastFurnace.class.getDeclaredField("controllerY");
        } catch (NoSuchFieldException e) {
            MainMod.LOGGER.catching(e);
        }
        controllerY.setAccessible(true);
    }

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

    @SuppressWarnings("rawtypes")
    public ArrayList TTTunnels = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList TTMultiAmp = new ArrayList<>();

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.circuitMode = aNBT.getByte("circuitMode");
        this.glasTier = aNBT.getByte("glasTier");
        this.lEUt = aNBT.getLong("lEUt");
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            this.addEnergyOutput(this.lEUt * (long)this.mEfficiency / 10000L);
            return true;
        } else if (this.lEUt < 0 && !this.drainEnergyInput((-this.lEUt) * 10000L / (long)Math.max(1000, this.mEfficiency))) {
            this.stopMachine();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void stopMachine() {
        this.mOutputItems = null;
        this.mEUt = 0;
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.mProgresstime = 0;
        this.mMaxProgresstime = 0;
        this.mEfficiencyIncrease = 0;
        this.getBaseMetaTileEntity().disableWorking();
    }

    private byte circuitMode = 0;
    private long lEUt = 0;

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            --circuitMode;
            if (circuitMode < 0)
                circuitMode = 24;
        } else {
            ++circuitMode;
            if (circuitMode > 24)
                circuitMode = 0;
        }

        GT_Utility.sendChatToPlayer(aPlayer, circuitMode > 0 ? "MEBF will prioritise circuit: " + circuitMode : "Circuit prioritisation disabled.");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glasTier", glasTier);
        aNBT.setByte("circuitMode", circuitMode);
        aNBT.setLong("lEUt", lEUt);
    }

    @Override
    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (LoaderReference.tectech)
            return TecTechUtils.addEnergyInputToMachineList(this, aTileEntity, aBaseCasingIndex);
        return super.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
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
                this.lEUt = Integer.MAX_VALUE - 1;
                this.mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                this.lEUt = (int) (aEUt >> 2);
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
            if (xEUt > maxInputVoltage) {
                //downclock one notch, we have overshot.
                xEUt >>=2;
                this.mMaxProgresstime <<= 1;
                timesOverclocked--;
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                this.lEUt = Integer.MAX_VALUE - 1;
                this.mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                this.lEUt = (int) xEUt;
                if (this.lEUt == 0)
                    this.lEUt = 1;
                if (this.mMaxProgresstime <= 0)
                    this.mMaxProgresstime = 1;//set time to 1 tick
            }
        }
        return timesOverclocked;
    }

    public String[] getInfoData() {
        int mPollutionReduction = 0;

        for (GT_MetaTileEntity_Hatch_Muffler e : this.mMufflerHatches)
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(e))
                mPollutionReduction = Math.max(e.calculatePollutionReduction(this.mPollution), mPollutionReduction);

        long storedEnergy = 0L;
        long maxEnergy = 0L;

        if (LoaderReference.tectech) {
            long[] info = getCurrentInfoData();
            storedEnergy = info[0];
            maxEnergy = info[1];
        }

        for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN + this.mProgresstime / 20 + EnumChatFormatting.RESET + " s / " + EnumChatFormatting.YELLOW + this.mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s", StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN + storedEnergy + EnumChatFormatting.RESET + " EU / " + EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU", StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " + EnumChatFormatting.RED + -this.lEUt + EnumChatFormatting.RESET + " EU/t", StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " + EnumChatFormatting.YELLOW + this.getMaxInputVoltage() + EnumChatFormatting.RESET + " EU/t(*2A) " + StatCollector.translateToLocal("GT5U.machines.tier") + ": " + EnumChatFormatting.YELLOW + GT_Values.VN[GT_Utility.getTier(this.getMaxInputVoltage())] + EnumChatFormatting.RESET, StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " + EnumChatFormatting.RED + (this.getIdealStatus() - this.getRepairStatus()) + EnumChatFormatting.RESET + " " + StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " + EnumChatFormatting.YELLOW + (float) this.mEfficiency / 100.0F + EnumChatFormatting.RESET + " %", StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " + EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"};
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        if (LoaderReference.tectech)
            return TecTechUtils.drainEnergyMEBFTecTech(this, aEU);
        return MegaUtils.drainEnergyMegaVanilla(this, aEU);
    }

    @Override
    public long getMaxInputVoltage() {
        if (LoaderReference.tectech)
            return TecTechUtils.getMaxInputVoltage(this);
        return super.getMaxInputVoltage();
    }

    private boolean getCoilHeat(IGregTechTileEntity iGregTechTileEntity, int y) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 7;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 7;
        int internalH = 0;
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                if (Math.abs(x) < 6 && Math.abs(z) != 6)
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
                    case 9:
                        internalH = 21601;
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

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        ItemStack[] tInputs = this.getStoredInputs().toArray(new ItemStack[0]);

        FluidStack[] tFluids = this.getStoredFluids().toArray(new FluidStack[0]);
        long nominalV = LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);

        byte tTier = (byte) Math.max(1, GT_Utility.getTier(nominalV));
        GT_Recipe tRecipe;
        if (circuitMode > 0 && Arrays.stream(tInputs).anyMatch(e -> GT_Utility.areStacksEqual(e, GT_Utility.getIntegratedCircuit(circuitMode), true))) {
            List<ItemStack> modInputs = Arrays.stream(tInputs).filter(Objects::nonNull).filter(e -> !e.getItem().equals(GT_Utility.getIntegratedCircuit(circuitMode).getItem())).collect(Collectors.toList());
            modInputs.add(GT_Utility.getIntegratedCircuit(circuitMode));
            tInputs = modInputs.toArray(new ItemStack[0]);
        }
        tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
        if (tRecipe == null) {
            if (circuitMode == 0)
                return false;
            tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
            tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], tFluids, tInputs);
            if (tRecipe == null)
                return false;
        }

        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        boolean found_Recipe = false;
        int processed = 0;

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

            long actualEUT = precutRecipeVoltage * processed;
            byte overclockCount = this.calculateOverclockednessEBF(actualEUT, tRecipe.mDuration, nominalV);

            //In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.lEUt == Integer.MAX_VALUE - 1)
                return false;

            if (this.lEUt > 0)
                this.lEUt = (-this.lEUt);

            if (tHeatCapacityDivTiers > 0) {
                this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount); //extra free overclocking if possible
                if (this.mMaxProgresstime < 1)
                    this.mMaxProgresstime = 1; //no eu efficiency correction
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

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        try {
            controllerY.set(this, iGregTechTileEntity.getYCoord() - 2);
        } catch (IllegalAccessException e) {
            MainMod.LOGGER.catching(e);
        }

        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }

        this.mHeatingCapacity = 0;
        boolean ret;
        ret = BW_Util.check_layer(iGregTechTileEntity, 7, -2, -1, GregTech_API.sBlockCasings1, 11, 7, false, false, true, GregTech_API.sBlockCasings1, 11, true, 11);
        ret &= BW_Util.check_layer(iGregTechTileEntity, 7, 17, 18, GregTech_API.sBlockCasings1, 11, 7, false, null, -1, 11);
        ret &= BW_Util.check_layer(iGregTechTileEntity, 6, -1, 17, GregTech_API.sBlockCasings5, -1, 7, false, false, true, Blocks.air, -1, false, 11);

        for (int y = -1; y < 17; y++) {
            ret &= BW_Util.check_layer(iGregTechTileEntity, 7, y, y + 1, ItemRegistry.bw_glasses[0], -1, 7, y == 0, false, false, null, -1, false, 11);
            if (!this.getCoilHeat(iGregTechTileEntity, y))
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
            for (int x = -6; x <= 6; x++)
                if (!this.addMufflerToMachineList(iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + x, 17, zDir + z), 11))
                    return false;

        if (LoaderReference.tectech && this.glasTier != 8)
            if (!areLazorsLowPowa() || !areThingsProperlyTiered(this.getTecTechEnergyTunnels()) || !areThingsProperlyTiered(this.getTecTechEnergyMultis()))
                return false;

        if (this.glasTier != 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier)
                    return false;

        return ret && !this.mMaintenanceHatches.isEmpty() && !this.mOutputBusses.isEmpty() && !this.mInputBusses.isEmpty();
    }

    @SuppressWarnings("rawtypes")
    @Optional.Method(modid = "tectech")
    private boolean areThingsProperlyTiered(Collection collection) {
        if (!collection.isEmpty())
            for (Object tecTechEnergyMulti : collection)
                if (((GT_MetaTileEntity_TieredMachineBlock) tecTechEnergyMulti).mTier > this.glasTier)
                    return false;
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Optional.Method(modid = "tectech")
    private boolean areLazorsLowPowa() {
        Collection collection = this.getTecTechEnergyTunnels();
        if (!collection.isEmpty())
            for (Object tecTechEnergyMulti : collection)
                if (!(tecTechEnergyMulti instanceof LowPowerLaser))
                    return false;
        return true;
    }

    @Override
    @Optional.Method(modid = "tectech")
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Optional.Method(modid = "tectech")
    public List getTecTechEnergyTunnels() {
        return TTTunnels;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Optional.Method(modid = "tectech")
    public List getTecTechEnergyMultis() {
        return TTMultiAmp;
    }
}
