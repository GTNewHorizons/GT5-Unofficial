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
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MegaUtils;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.bartimaeusnek.crossmod.tectech.helper.TecTechUtils;
import com.github.bartimaeusnek.crossmod.tectech.tileentites.tiered.LowPowerLaser;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaBlastFurnace extends GT_MetaTileEntity_ElectricBlastFurnace implements TecTechEnabledMulti {

    private static final int CASING_INDEX = 11;
    private static final IStructureDefinition<GT_TileEntity_MegaBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MegaBlastFurnace>builder()
            .addShape("main", createShape())
            .addElement('t', ofHatchAdderOptional(GT_TileEntity_MegaBlastFurnace::addOutputHatchToTopList, CASING_INDEX, 1, GregTech_API.sBlockCasings1, CASING_INDEX))
            .addElement('m', ofHatchAdder(GT_TileEntity_MegaBlastFurnace::addMufflerToMachineList, CASING_INDEX, 2))
            .addElement('C', ofCoil(GT_TileEntity_MegaBlastFurnace::setCoilLevel, GT_MetaTileEntity_ElectricBlastFurnace::getCoilLevel))
            .addElement('g', ofBlockAdder(GT_TileEntity_MegaBlastFurnace::addGlas, ItemRegistry.bw_glasses[0], 1))
            .addElement('b', ofHatchAdderOptional(GT_TileEntity_MegaBlastFurnace::addBottomHatch, CASING_INDEX, 3, GregTech_API.sBlockCasings1, CASING_INDEX))
            .build();

    private static String[][] createShape() {
        String[][] raw = new String[20][];

        raw[0] = new String[15];
        String topCasing   = "ttttttttttttttt";
        String mufflerLine = "tmmmmmmmmmmmmmt";
        raw[0][0] = topCasing;
        for (int i = 1; i < 14; i++) {
            raw[0][i] = mufflerLine;
        }
        raw[0][14] = topCasing;

        raw[1] = new String[15];
        String allGlass   = "ggggggggggggggg";
        String allCoil    = "gCCCCCCCCCCCCCg";
        String middleLine = "gC-----------Cg";
        raw[1][0] = allGlass;
        raw[1][1] = allCoil;
        raw[1][13] = allCoil;
        raw[1][14] = allGlass;
        for (int i = 2; i < 13; i++) {
            raw[1][i] = middleLine;
        }
        for (int i = 2; i < 19; i++) {
            raw[i] = raw[1];
        }
        String bottomCasing = "bbbbbbbbbbbbbbb";
        raw[19] = new String[15];
        for (int i = 0; i < 15; i++) {
            raw[19][i] = bottomCasing;
        }

        raw[17] = Arrays.copyOf(raw[17], raw[17].length);
        raw[17][0] = "ggggggg~ggggggg";

        return transpose(raw);
    }

    private int mHeatingCapacity;
    private byte glasTier;
    private int polPtick = super.getPollutionPerTick(null) * ConfigHandler.megaMachinesMax;

    public GT_TileEntity_MegaBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
                .addInfo("Controller block for the Mega Blast Furnace")
                .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
                .addInfo("Each 900K over the min. Heat required multiplies EU/t by 0.95")
                .addInfo("Each 1800K over the min. Heat required allows for one upgraded overclock instead of normal")
                .addInfo("Upgraded overclocks reduce recipe time to 25% (instead of 50%) and increase EU/t to 400%")
                .addInfo("Additionally gives +100K for every tier past MV")
                .addPollutionAmount(20 * getPollutionPerTick(null))
                .addSeparator()
                .beginStructureBlock(15, 20, 15, true)
                .addController("3rd layer center")
                .addCasingInfo("Heat Proof Machine Casing", 0)
                .addOtherStructurePart("Heating Coils", "Inner 13x18x13 (Hollow)")
                .addOtherStructurePart("Borosilicate Glass", "Outer 15x18x15")
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addEnergyHatch("Any bottom layer casing")
                .addMaintenanceHatch("Any bottom layer casing")
                .addMufflerHatch("Top middle 13x13")
                .addInputBus("Any bottom layer casing")
                .addInputHatch("Any bottom layer casing")
                .addOutputBus("Any bottom layer casing")
                .addOutputHatch("Gasses, Any top layer casing")
                .addStructureInfo("Recovery amount scales with Muffler Hatch tier")
                .addOutputHatch("Platline fluids, Any bottom layer casing")
                .addStructureHint("This Mega Multiblock is too big to have its structure hologram displayed fully.")
                .toolTipFinisher(BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get());
        return tt;
    }

    @SuppressWarnings("rawtypes")
    public ArrayList<Object> TTTunnels = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList<Object> TTMultiAmp = new ArrayList<>();

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

    @Override
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

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " +
                        EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(this.mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " +
                        EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " +
                        EnumChatFormatting.RED + GT_Utility.formatNumbers(-this.lEUt) + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(this.getMaxInputVoltage()) + EnumChatFormatting.RESET + " EU/t(*2A) " +
                        StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                        EnumChatFormatting.YELLOW + GT_Values.VN[GT_Utility.getTier(this.getMaxInputVoltage())] + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (this.getIdealStatus() - this.getRepairStatus()) + EnumChatFormatting.RESET + " " +
                        StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + (float) this.mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " +
                        EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"};
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

        byte tTier = (byte) Math.max(1, Math.min(GT_Utility.getTier(nominalV), V.length - 1));
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

        long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / precutRecipeVoltage);

        if (this.mHeatingCapacity >= tRecipe.mSpecialValue) {
            int tCurrentPara = handleParallelRecipe(tRecipe, tFluids, tInputs, (int) tMaxPara);
            processed = tCurrentPara;
            found_Recipe = true;
            Object[] Outputs = getMultiOutput(tRecipe, tCurrentPara);
            outputFluids = (ArrayList<FluidStack>) Outputs[0];
            outputItems = (ArrayList<ItemStack>) Outputs[1];
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
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public IStructureDefinition<GT_MetaTileEntity_ElectricBlastFurnace> getStructureDefinition() {
        // hack to get around generic
        return (IStructureDefinition) STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 7, 17, 0);
    }

    private boolean addGlas(Block block, int meta) {
        if (block != ItemRegistry.bw_glasses[0])
            return false;
        byte tier = BW_Util.getTierFromGlasMeta(meta);
        if (glasTier > 0)
            return tier == glasTier;
        glasTier = tier;
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }

        this.mHeatingCapacity = 0;
        glasTier = 0;

        setCoilLevel(HeatingCoilLevel.None);

        this.mPollutionOutputHatches.clear();

        if (!checkPiece("main", 7, 17, 0))
            return false;

        if (getCoilLevel() == HeatingCoilLevel.None)
            return false;

        if (mMaintenanceHatches.size() != 1)
            return false;

        if (LoaderReference.tectech && this.glasTier != 8)
            if (!areLazorsLowPowa() || areThingsNotProperlyTiered(this.getTecTechEnergyTunnels()) || areThingsNotProperlyTiered(this.getTecTechEnergyMultis()))
                return false;

        if (this.glasTier != 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier)
                    return false;

        this.mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Optional.Method(modid = "tectech")
    private boolean areThingsNotProperlyTiered(Collection collection) {
        if (!collection.isEmpty())
            for (Object tecTechEnergyMulti : collection)
                if (((GT_MetaTileEntity_TieredMachineBlock) tecTechEnergyMulti).mTier > this.glasTier)
                    return true;
        return false;
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
