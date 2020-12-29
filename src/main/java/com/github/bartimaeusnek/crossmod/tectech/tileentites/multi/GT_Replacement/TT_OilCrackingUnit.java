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
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_OilCrackingUnit extends GT_MetaTileEntity_MultiblockBase_EM implements IHasCoils, IConstructable {

    public TT_OilCrackingUnit(Object unused, Object unused2) {
        super(32765, "multimachine.cracker", "Oil Cracking Unit");
        GregTech_API.METATILEENTITIES[32765] = null;
        GregTech_API.METATILEENTITIES[1160] = this;
    }

    private TT_OilCrackingUnit(String aName) {
        super(aName);
    }

    private HeatingCoilLevel coilMeta = HeatingCoilLevel.None;

    @Override
    public void setCoilHeat(HeatingCoilLevel coilMeta) {
        this.coilMeta = coilMeta;
    }

    @Override
    public HeatingCoilLevel getCoilHeat() {
        return coilMeta;
    }

    private byte blocks = 0;

    private static final byte TEXTURE_INDEX = 49;

    @Override
    protected boolean cyclicUpdate_EM() {
        return false;
    }

    private static final IStructureDefinition<TT_OilCrackingUnit> STRUCTURE_DEFINITION = StructureDefinition.<TT_OilCrackingUnit>builder().addShape("main",
            transpose(new String[][]{
                    {"ABABA","ABGBA","ABABA"},
                    {"AB~BA","E---F","ABABA"},
                    {"ABCBA","ABABA","ABABA"}
            })
    ).addElement(
            'A',
            onElementPass(
                    x -> ++x.blocks,ofBlock(
                        GregTech_API.sBlockCasings4,
                    1
                    )
            )
    ).addElement(
            'B',
            CoilAdder.getINSTANCE()
    ).addElement(
            'C',
            ofHatchAdder(
                    GT_MetaTileEntity_MultiblockBase_EM::addClassicMaintenanceToMachineList,
                    TEXTURE_INDEX,
                    1
            )
    ).addElement(
            'E',
            ofHatchAdder(
                    TT_OilCrackingUnit::addInputFluidHatch,
                    TEXTURE_INDEX,
                    2
            )
    ).addElement(
            'F',
            ofHatchAdder(
                    TT_OilCrackingUnit::addOutputFluidHatch,
                    TEXTURE_INDEX,
                    3
            )
    ).addElement(
            'G',
            ofHatchAdder(
                    TT_OilCrackingUnit::addMiddleFluidHatch,
                    TEXTURE_INDEX,
                    4
            )
    ).build();

    GT_MetaTileEntity_Hatch_Input middleFluidHatch;

    public final boolean addMiddleFluidHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                this.middleFluidHatch = (GT_MetaTileEntity_Hatch_Input)aMetaTileEntity;
                return true;
            } else
                return false;
        }
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.coilMeta = HeatingCoilLevel.None;
        this.blocks = 0;
        return this.structureCheck_EM("main", 2,1,0)
                && this.blocks >= 18;
    }

    public final boolean addOutputFluidHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output)aMetaTileEntity);
            } else
                return false;
        }
    }

    public final boolean addInputFluidHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            } else
                return false;
        }
    }

    @Override
    public IStructureDefinition<TT_OilCrackingUnit> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_OilCrackingUnit(this.mName);
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Cracker")
                .addInfo("Controller block for the Oil Cracking Unit")
                .addInfo("Thermally cracks heavy hydrocarbons into lighter fractions")
                .addInfo("More efficient than the Chemical Reactor")
                .addInfo("Place the appropriate circuit in the controller")
                .addSeparator()
                .beginStructureBlock(5, 3, 3, true)
                .addController("Front center")
                .addCasingInfo("Clean Stainless Steel Machine Casing", 18)
                .addOtherStructurePart("2 Rings of 8 Coils", "Each side of the controller")
                .addInfo("Gets 5% energy cost reduction per coil tier")
                .addInfo(ADV_STR_CHECK)
                .addEnergyHatch("Any casing")
                .addMaintenanceHatch("Any casing")
                .addInputHatch("Steam/Hydrogen, Any middle ring casing")
                .addInputHatch("Any left/right side casing")
                .addOutputHatch("Any left/right side casing")
                .addStructureInfo("Input/Output Hatches must be on opposite sides!")
                .toolTipFinisher("Gregtech");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            return tt.getInformation();
        else
            return tt.getStructureInformation();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX],
                    new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "OilCrackingUnit.png");
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ArrayList<FluidStack> tInputList = getStoredFluids();
        FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[0]);
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sCrakingRecipes.findRecipe(
                getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs , mInventory[1]);
        if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluidInputs, mInventory[1])) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, getMaxInputVoltage());
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return false;
            this.mEUt *= Math.pow(0.95D, this.coilMeta.getTier());

            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }

            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
            return true;
        }
        return false;
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
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();

        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                FluidStack tStack = tHatch.getFillableStack();
                if (!tStack.isFluidEqual(GT_ModHandler.getSteam(1000)) && !tStack.isFluidEqual(Materials.Hydrogen.getGas(1000)))
                    rList.add(tStack);
            }
        }

        if (this.middleFluidHatch != null && isValidMetaTileEntity(this.middleFluidHatch) && this.middleFluidHatch.getFillableStack() != null) {
            this.middleFluidHatch.mRecipeMap = getRecipeMap();
            FluidStack tStack = this.middleFluidHatch.getFillableStack();
            if (tStack.isFluidEqual(GT_ModHandler.getSteam(1000)) || tStack.isFluidEqual(Materials.Hydrogen.getGas(1000)))
                rList.add(tStack);
        }

        return rList;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 2,1,0, b, itemStack);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }
}
