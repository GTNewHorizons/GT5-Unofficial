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

package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.bartimaeusnek.crossmod.tectech.helper.CoilAdder;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;

public class TT_ElectronicBlastFurnace extends TT_Abstract_GT_Replacement_Coils {

    public TT_ElectronicBlastFurnace(Object unused, Object unused2) {
        super(1000, "multimachine.blastfurnace", "Electric Blast Furnace");
    }

    private TT_ElectronicBlastFurnace(String aName) {
        super(aName);
    }

    private int mHeatingCapacity = 0;

    private static final byte TEXTURE_INDEX = 11;
    private static final IStructureDefinition<TT_ElectronicBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
            .<TT_ElectronicBlastFurnace>builder()
            .addShape("main",
                    transpose(new String[][]{
                            {"AAA", "AMA", "AAA"},
                            {"CCC", "C-C", "CCC"},
                            {"CCC", "C-C", "CCC"},
                            {"B~B", "BBB", "BBB"}
                    })
            ).addElement(
                    'C',
                    CoilAdder.getINSTANCE()
            ).addElement(
                    'M',
                    ofHatchAdder(
                            GT_MetaTileEntity_MultiblockBase_EM::addClassicMufflerToMachineList, TEXTURE_INDEX,
                            1
                    )
            ).addElement(
                    'A',
                    ofHatchAdderOptional(
                            TT_ElectronicBlastFurnace::addEBFInputsTop, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX
                    )
            ).addElement(
                    'B',
                    ofHatchAdderOptional(
                            TT_ElectronicBlastFurnace::addEBFInputsBottom, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX
                    )
            )
            .build();

    @Override
    public IStructureDefinition<TT_ElectronicBlastFurnace> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mHeatingCapacity = 0;
        this.setCoilHeat(HeatingCoilLevel.None);
        boolean ret = this.structureCheck_EM("main", 1, 3, 0) && this.getCoilHeat() != HeatingCoilLevel.None;
        this.mMufflerHatches.forEach(x -> x.setInValidFacings(this.getExtendedFacing().getRelativeUpInWorld().getOpposite()));

        if (this.mMufflerHatches.stream()
                .map(MetaTileEntity::getBaseMetaTileEntity)
                .mapToInt(ITurnable::getFrontFacing)
                .noneMatch(x -> x == this.getExtendedFacing().getRelativeUpInWorld().ordinal()))
            return false;

        this.mHeatingCapacity = (int) this.getCoilHeat().getHeat();
        this.mHeatingCapacity += 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);
        setInputFilters();
        return ret;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,3,0, b, itemStack);
    }

    public final boolean addEBFInputsTop(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        else
            return false;

        return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
    }

    private static final Set<FluidStack> POLLUTION_FLUID_STACKS = ImmutableSet.of(
            Materials.CarbonDioxide.getGas(1000),
            Materials.CarbonMonoxide.getGas(1000),
            Materials.SulfurDioxide.getGas(1000)
    );

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null)
            return false;
        FluidStack tLiquid = aLiquid.copy();

        if (POLLUTION_FLUID_STACKS.stream().anyMatch(tLiquid::isFluidEqual)) {
            tLiquid.amount = tLiquid.amount * (mMufflerHatches.stream()
                    .filter(GT_MetaTileEntity_MultiBlockBase::isValidMetaTileEntity)
                    .findFirst()
                    .map(tHatch -> 100 - tHatch.calculatePollutionReduction(100))
                    .orElse(0) + 5) / 100;
            for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
                if ((isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid)
                        ? !tHatch.outputsSteam()
                        : !tHatch.outputsLiquids())
                        || tHatch.getBaseMetaTileEntity().getYCoord() <= this.getBaseMetaTileEntity().getYCoord()
                        || canNotFillOutput(tHatch, tLiquid))
                    continue;
                return true;
            }
        } else {
            for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
                if ((isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid)
                        ? !tHatch.outputsSteam()
                        : !tHatch.outputsLiquids())
                        || tHatch.getBaseMetaTileEntity().getYCoord() > this.getBaseMetaTileEntity().getYCoord()
                        || canNotFillOutput(tHatch, tLiquid))
                    continue;
                return true;
            }
        }
        return false;
    }

    private boolean canNotFillOutput(GT_MetaTileEntity_Hatch_Output tHatch, FluidStack tLiquid){
            int tAmount = tHatch.fill(tLiquid, false);
            if (tAmount >= tLiquid.amount)
                return tHatch.fill(tLiquid, true) < tLiquid.amount;
            else if (tAmount > 0)
                tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
            return true;
    }

    private static final int pollutionPerTick = 20;

    private final static String[] desc = new String[]{
            "Blast Furnace",
            "Controller block for the Electric Blast Furnace",
            "You can use some fluids to reduce recipe time. Place the circuit in the Input Bus",
            "Each 900K over the min. Heat required multiplies EU/t by 0.95",
            "Each 1800K over the min. Heat required allows for one upgraded overclock instead of normal",
            "Upgraded overclocks reduce recipe time to 25% (instead of 50%) and increase EU/t to 400%",
            "Additionally gives +100K for every tier past MV",
            "Creates up to: " + 20 * pollutionPerTick + " Pollution per Second",
            ADV_STR_CHECK,
            TT_BLUEPRINT
    };

    @Override
    public String[] getDescription() {
        return desc;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11]};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_ElectronicBlastFurnace(this.mName);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ItemStack[] tInputs = this.getCompactedInputs();
        FluidStack[] tFluids = this.getCompactedFluids();

        if (tInputs.length <= 0) {
            return false;
        }
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
        if (tRecipe == null
                || (this.mHeatingCapacity < tRecipe.mSpecialValue)
                || (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
            return false;
        }
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        int tHeatCapacityDivTiers = ((this.mHeatingCapacity - tRecipe.mSpecialValue) / 900);
        byte overclockCount = calculateOverclockednessEBF(tRecipe.mEUt, tRecipe.mDuration, tVoltage);
        //In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        if (this.mEUt > 0)
            this.mEUt = (-this.mEUt);

        if (tHeatCapacityDivTiers > 0) {
            this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
            this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount);//extra free overclocking if possible
            if (this.mMaxProgresstime < 1)
                this.mMaxProgresstime = 1;//no eu efficiency correction
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0), tRecipe.getOutput(1)};
        this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
        updateSlots();
        return true;
    }

    /**
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
            long tempEUt = Math.max(xEUt, V[1]);

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
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
            }
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN + mProgresstime / 20 + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN + storedEnergy + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " + EnumChatFormatting.RED + -mEUt + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " + EnumChatFormatting.YELLOW + getMaxInputVoltage() + EnumChatFormatting.RESET + " EU/t(*2A) " + StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                        EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.EBF.heat") + ": " +
                        EnumChatFormatting.GREEN + mHeatingCapacity + EnumChatFormatting.RESET + " K",
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " + EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"
        };
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return pollutionPerTick;
    }

    private static final String[] sfStructureDescription = new String[] {
            "0 - Air",
            "1 - Muffler",
            "Required: Output Bus, Input Bus, Energy Hatch, Maintenance Hatch",
            "Optional: Input Hatch, Output Hatch at Bottom, Output Hatch at top to regain CO/CO2/SO2"
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }

}
