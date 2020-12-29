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
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
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
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_MultiSmelter extends GT_MetaTileEntity_MultiblockBase_EM implements IHasCoils, IConstructable {

    private int mLevel;
    private int mCostDiscount;

    public TT_MultiSmelter(Object unused, Object unused2) {
        super(32765, "multimachine.multifurnace", "Multi Smelter");
        GregTech_API.METATILEENTITIES[32765] = null;
        GregTech_API.METATILEENTITIES[1003] = this;
    }

    private TT_MultiSmelter(String aName) {
        super(aName);
    }

    private HeatingCoilLevel coilMeta = HeatingCoilLevel.None;
    private static final byte TEXTURE_INDEX = 11;
    private static final IStructureDefinition<TT_MultiSmelter> STRUCTURE_DEFINITION = StructureDefinition
            .<TT_MultiSmelter>builder()
            .addShape("main",
                    transpose(new String[][]{
                            {"AAA", "AMA", "AAA"},
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
                    ofBlock(
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX
                    )
            ).addElement(
                    'B',
                    ofHatchAdderOptional(
                            TT_MultiSmelter::addHatchesBottom, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX,
                            GregTech_API.sBlockCasings1, TEXTURE_INDEX
                    )
            )
            .build();

    public final boolean addHatchesBottom(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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
        if (this.mMufflerHatches.stream()
                .map(MetaTileEntity::getBaseMetaTileEntity)
                .mapToInt(ITurnable::getFrontFacing)
                .noneMatch(x -> x == this.getExtendedFacing().getRelativeUpInWorld().ordinal()))
            return false;

        this.mLevel = this.getCoilHeat().getLevel();
        this.mCostDiscount = this.getCoilHeat().getCostDiscount();
        return ret;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,2,0, b, itemStack);
    }

    @Override
    protected boolean cyclicUpdate_EM() {
        return false;
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Furnace")
                .addInfo("Controller Block for the Multi Smelter")
                .addInfo("Smelts up to 8-128 items at once")
                .addInfo("Items smelted increases with coil tier")
                .addPollutionAmount(20 * getPollutionPerTick(null))
                .addInfo(ADV_STR_CHECK)
                .addSeparator()
                .beginStructureBlock(3, 3, 3, true)
                .addController("Front bottom")
                .addCasingInfo("Heat Proof Machine Casing", 8)
                .addOtherStructurePart("Heating Coils", "Middle layer")
                .addEnergyHatch("Any bottom casing")
                .addMaintenanceHatch("Any bottom casing")
                .addMufflerHatch("Top Middle")
                .addInputBus("Any bottom casing")
                .addOutputBus("Any bottom casing")
                .toolTipFinisher("Gregtech");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            return tt.getInformation();
        return tt.getStructureInformation();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][11]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MultiFurnace.png");
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void setCoilHeat(HeatingCoilLevel coilMeta) {
        this.coilMeta = coilMeta;
    }

    @Override
    public HeatingCoilLevel getCoilHeat() {
        return coilMeta;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
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
        for (ItemStack item : tInputList)
            if (tSmeltStack.isItemEqual(item))
                if (item.stackSize < (tMaxParrallel - tCurrenParrallel)) {
                    tCurrenParrallel += item.stackSize;
                    item.stackSize = 0;
                } else {
                    item.stackSize = (tCurrenParrallel + item.stackSize) - tMaxParrallel;
                    tCurrenParrallel = tMaxParrallel;
                    break;
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