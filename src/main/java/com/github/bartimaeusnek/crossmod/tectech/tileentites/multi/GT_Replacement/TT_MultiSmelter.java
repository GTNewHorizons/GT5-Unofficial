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
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.VN;

public class TT_MultiSmelter extends TT_Abstract_GT_Replacement_Coils {

    private int mLevel;
    private int mCostDiscount;

    public TT_MultiSmelter(Object unused, Object unused2) {
        super(1003, "multimachine.multifurnace", "Multi Smelter");
    }

    private TT_MultiSmelter(String aName) {
        super(aName);
    }

    private static final byte TEXTURE_INDEX = 11;
    private static final IStructureDefinition<TT_MultiSmelter> STRUCTURE_DEFINITION = StructureDefinition
            .<TT_MultiSmelter>builder()
            .addShape("main",
                    transpose(new String[][]{
                            {"AAA", "AMA", "AAA"},
                            {"CCC", "C-C", "CCC"},
                            {"A~A", "AAA", "AAA"}
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
                            TT_MultiSmelter::addEBFInputsBottom, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX
                    )
            )
            .build();


    private static final String[] sfStructureDescription = new String[] {
            "0 - Air",
            "1 - Muffler",
            "Required: Output Bus, Input Bus, Energy Hatch, Maintenance Hatch",
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }

    @Override
    public IStructureDefinition<TT_MultiSmelter> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mLevel = 0;
        this.mCostDiscount = 1;
        this.setCoilHeat(HeatingCoilLevel.None);
        boolean ret = this.structureCheck_EM("main", 1, 2, 0) && this.getCoilHeat() != HeatingCoilLevel.None;

        this.mMufflerHatches.forEach(x -> x.setInValidFacings(this.getExtendedFacing().getRelativeUpInWorld().getOpposite()));

        if (this.mMufflerHatches.stream()
                .map(MetaTileEntity::getBaseMetaTileEntity)
                .mapToInt(ITurnable::getFrontFacing)
                .noneMatch(x -> x == this.getExtendedFacing().getRelativeUpInWorld().ordinal()))
            return false;

        this.mLevel = this.getCoilHeat().getLevel();
        this.mCostDiscount = this.getCoilHeat().getCostDiscount();
        setInputFilters();
        return ret;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sFurnaceRecipes;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,2,0, b, itemStack);
    }

    private static final int pollutionPerTick = 20;

    private final static String[] desc = new String[]{
            "Furnace",
            "Controller Block for the Multi Smelter",
            "Smelts up to 8-128 items at once",
            "Items smelted increases with coil tier",
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11]};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_MultiSmelter(this.mName);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty())
            return false;

        int mVolatage = GT_Utility.safeInt(getMaxInputVoltage());
        int tMaxParrallel = 8 * this.mLevel;
        int tCurrenParrallel = 0;
        ItemStack tSmeltStack = tInputList.get(0);
        ItemStack tOutputStack = GT_ModHandler.getSmeltingOutput(tSmeltStack,false,null);
        if (tOutputStack == null)
            return false;
        for (ItemStack item : tInputList) {
            if (!tSmeltStack.isItemEqual(item)) {
                continue;
            }
            if (item.stackSize < (tMaxParrallel - tCurrenParrallel)) {
                tCurrenParrallel += item.stackSize;
                item.stackSize = 0;
            } else {
                item.stackSize = (tCurrenParrallel + item.stackSize) - tMaxParrallel;
                tCurrenParrallel = tMaxParrallel;
                break;
            }
        }
        tCurrenParrallel *= tOutputStack.stackSize;
        this.mOutputItems = new ItemStack[(tCurrenParrallel/64)+1];
        for (int i = 0; i < this.mOutputItems.length; i++) {
            ItemStack tNewStack = tOutputStack.copy();
            int size = Math.min(tCurrenParrallel, 64);
            tNewStack.stackSize = size;
            tCurrenParrallel -= size;
            this.mOutputItems[i] = tNewStack;
        }

        if (this.mOutputItems.length > 0) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            calculateOverclockedNessMulti(4, 512, 1, mVolatage);
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return false;

            this.mEUt = GT_Utility.safeInt(((long)mEUt) * this.mLevel / (long)this.mCostDiscount,1);
            if (mEUt == Integer.MAX_VALUE - 1)
                return false;

            if (this.mEUt > 0)
                this.mEUt = (-this.mEUt);
        }
        updateSlots();
        return true;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return pollutionPerTick;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches)
            if (isValidMetaTileEntity(tHatch))
                mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress")+": "+
                        EnumChatFormatting.GREEN + mProgresstime / 20 + EnumChatFormatting.RESET +" s / "+
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET +" s",
                StatCollector.translateToLocal("GT5U.multiblock.energy")+": "+
                        EnumChatFormatting.GREEN + storedEnergy + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET +" EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage")+": "+
                        EnumChatFormatting.RED + -mEUt + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei")+": "+
                        EnumChatFormatting.YELLOW+ getMaxInputVoltage() +EnumChatFormatting.RESET+" EU/t(*2A) "+StatCollector.translateToLocal("GT5U.machines.tier")+": "+
                        EnumChatFormatting.YELLOW+VN[GT_Utility.getTier(getMaxInputVoltage())]+ EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems")+": "+
                        EnumChatFormatting.RED+ (getIdealStatus() - getRepairStatus())+EnumChatFormatting.RESET+
                        " "+StatCollector.translateToLocal("GT5U.multiblock.efficiency")+": "+
                        EnumChatFormatting.YELLOW+ mEfficiency / 100.0F +EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.MS.multismelting")+": "+
                        EnumChatFormatting.GREEN+mLevel*8+EnumChatFormatting.RESET+" Discount: (EU/t) / "+EnumChatFormatting.GREEN+mCostDiscount+EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.pollution")+": "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %"
        };
    }
}