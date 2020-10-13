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
import com.github.bartimaeusnek.crossmod.tectech.helper.IHasCoils;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Set;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;

public class TT_ElectronicBlastFurnace extends GT_MetaTileEntity_MultiblockBase_EM implements IHasCoils, IConstructable {

    public TT_ElectronicBlastFurnace(Object unused) {
        super(32765, "multimachine.blastfurnace", "Electric Blast Furnace");
        GregTech_API.METATILEENTITIES[32765] = null;
        GregTech_API.METATILEENTITIES[1000] = this;
    }

    private TT_ElectronicBlastFurnace(String aName) {
        super(aName);
    }

    private short coilMeta = -1;
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
        this.setCoilMeta((short) -1);
        boolean ret = this.structureCheck_EM("main", 1, 3, 0) && this.getHeatingCapacity() > -1;
        if (this.mMufflerHatches.stream()
                .map(MetaTileEntity::getBaseMetaTileEntity)
                .mapToInt(ITurnable::getFrontFacing)
                .noneMatch(x -> x == this.getExtendedFacing().getRelativeUpInWorld().ordinal()))
            return false;
        return ret;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,3,0, b, itemStack);
    }

    @Override
    protected boolean cyclicUpdate_EM() {
        return false;
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
        }

        for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
            if (
                    (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid)
                            ? !tHatch.outputsSteam()
                            : !tHatch.outputsLiquids())
                            || tHatch.getBaseMetaTileEntity().getYCoord() <= this.getBaseMetaTileEntity().getYCoord())
                continue;
            int tAmount = tHatch.fill(tLiquid, false);
            if (tAmount >= tLiquid.amount)
                return tHatch.fill(tLiquid, true) >= tLiquid.amount;
            else if (tAmount > 0)
                tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
        }
        return false;
    }

    public final boolean addEBFInputsBottom(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        else
            return false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
            return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        return false;
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Blast Furnace",
                "Size(WxHxD): 3x4x3 (Hollow), Controller (Front middle bottom)",
                "16x Heating Coils (Two middle Layers, hollow)",
                "1x Input Hatch/Bus (Any bottom layer casing)",
                "1x Output Hatch/Bus (Any bottom layer casing)",
                "1x Energy Hatch (Any bottom layer casing)",
                "1x Maintenance Hatch (Any bottom layer casing)",
                "1x Muffler Hatch (Top middle)",
                "1x Output Hatch to recover CO2/CO/SO2 (optional, any top layer casing),",
                "    Recovery scales with Muffler Hatch tier",
                "Heat Proof Machine Casings for the rest",
                "Each 900K over the min. Heat Capacity multiplies eu/t by 0.95",
                "Each 1800K over the min. Heat Capacity allows for one upgraded overclock",
                "Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second",
                ADV_STR_CHECK
        };
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricBlastFurnace.png");
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void setCoilMeta(short coilMeta) {
        this.coilMeta = coilMeta;
    }

    @Override
    public short getCoilMeta() {
        return coilMeta;
    }

    private int getHeatingCapacity() {
        switch (coilMeta) {
            case 0:
                return 1801;
            case 1:
                return 2701;
            case 2:
                return 3601;
            case 3:
                return 4501;
            case 4:
                return 5401;
            case 5:
                return 7201;
            case 6:
                return 9001;
            case 7:
                return 9901;
            case 8:
                return 10801;
            default:
                return -1;
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_ElectronicBlastFurnace(this.mName);
    }

    public boolean checkRecipe_EM(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS = tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                    if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                        tInputList.remove(j--);
                        tInputList_sS = tInputList.size();
                    } else {
                        tInputList.remove(i--);
                        tInputList_sS = tInputList.size();
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);

        ArrayList<FluidStack> tFluidList = getStoredFluids();
        int tFluidList_sS = tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                    if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                        tFluidList.remove(j--);
                        tFluidList_sS = tFluidList.size();
                    } else {
                        tFluidList.remove(i--);
                        tFluidList_sS = tFluidList.size();
                        break;
                    }
                }
            }
        }
        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
        if (tInputList.size() > 0) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
            if (tRecipe == null
                    || (this.getHeatingCapacity() < tRecipe.mSpecialValue)
                    || (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
                return false;
            }
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            int tHeatCapacityDivTiers = (this.getHeatingCapacity() - tRecipe.mSpecialValue) / 900;
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
        return false;
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
    public boolean isMachineBlockUpdateRecursive() {
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 20;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
