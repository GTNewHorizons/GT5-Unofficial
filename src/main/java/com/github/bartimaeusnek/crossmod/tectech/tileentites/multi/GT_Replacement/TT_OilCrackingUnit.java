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
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_OilCrackingUnit extends TT_Abstract_GT_Replacement_Coils {

    public TT_OilCrackingUnit(Object unused, Object unused2) {
        super(1160, "multimachine.cracker", "Oil Cracking Unit");
    }

    private TT_OilCrackingUnit(String aName) {
        super(aName);
    }

    private byte blocks = 0;

    private static final byte TEXTURE_INDEX = 49;

    private static final IStructureDefinition<TT_OilCrackingUnit> STRUCTURE_DEFINITION = StructureDefinition.<TT_OilCrackingUnit>builder().addShape("main",
            transpose(new String[][]{
                    {"EBGBF","EBGBF","EBGBF"},
                    {"EB~BF","E---F","EBGBF"},
                    {"EBGBF","EBGBF","EBGBF"}
            })
    ).addElement(
            'A',
            ofChain(
                    onElementPass(
                            x -> ++x.blocks,
                            ofBlock(
                                    GregTech_API.sBlockCasings4,
                                    1
                            )
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addClassicMaintenanceToMachineList,
                            TEXTURE_INDEX,
                            1
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addEnergyIOToMachineList,
                            TEXTURE_INDEX,
                            1
                    )
            )
    ).addElement(
            'B',
            CoilAdder.getINSTANCE()
    ).addElement(
            'E',
            ofChain(
                    ofHatchAdder(
                            TT_OilCrackingUnit::addInputFluidHatch,
                            TEXTURE_INDEX,
                            1),
                    onElementPass(
                            x -> ++x.blocks,
                            ofBlock(
                                    GregTech_API.sBlockCasings4,
                                    1
                            )
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addClassicMaintenanceToMachineList,
                            TEXTURE_INDEX,
                            1
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addEnergyIOToMachineList,
                            TEXTURE_INDEX,
                            1
                    )
            )
    ).addElement(
            'F',
            ofChain(
                    ofHatchAdder(
                            TT_OilCrackingUnit::addOutputFluidHatch,
                            TEXTURE_INDEX,
                            2),
                    onElementPass(
                            x -> ++x.blocks,
                            ofBlock(
                                    GregTech_API.sBlockCasings4,
                                    1
                            )
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addClassicMaintenanceToMachineList,
                            TEXTURE_INDEX,
                            1
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addEnergyIOToMachineList,
                            TEXTURE_INDEX,
                            1
                    )

            )
    ).addElement(
            'G',
            ofChain(
                    ofHatchAdder(
                            TT_OilCrackingUnit::addMiddleFluidHatch,
                            TEXTURE_INDEX,
                            3),
                    onElementPass(
                            x -> ++x.blocks,
                            ofBlock(
                                    GregTech_API.sBlockCasings4,
                                    1
                            )
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addClassicMaintenanceToMachineList,
                            TEXTURE_INDEX,
                            1
                    ),
                    ofHatchAdder(
                            TT_OilCrackingUnit::addEnergyIOToMachineList,
                            TEXTURE_INDEX,
                            1
                    )

            )
    ).build();

    private static final String[] sfStructureDescription = new String[] {
            "0 - Air",
            "1 - Input Hatch",
            "2 - Output Hatch",
            "3 - 1x Input Hatch, rest Casings, Maintenance Hatch or Energy Hatch",
            "Required: Maintenance Hatch, Energy Hatch, at Position 3 or instead of Casings",
            "18 Casings at least!"
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }

    private GT_MetaTileEntity_Hatch_Input middleFluidHatch;

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
        this.setCoilHeat(HeatingCoilLevel.None);
        this.blocks = 0;
        boolean ret = this.structureCheck_EM("main", 2,1,0);
        setInputFilters();
        return ret && this.blocks >= 18;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sCrakingRecipes;
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

    private final static String[] desc = new String[]{
            "Cracker",
            "Controller block for the Oil Cracking Unit",
            "Thermally cracks heavy hydrocarbons into lighter fractions",
            "More efficient than the Chemical Reactor",
            "Place the appropriate circuit in the controller",
            "Gets 5% energy cost reduction per coil tier",
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX],
                    new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][TEXTURE_INDEX]};
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ArrayList<FluidStack> tInputList = getStoredFluids();
        FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[0]);
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sCrakingRecipes.findRecipe(
                getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs , mInventory[1]);
        if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, tFluidInputs, mInventory[1])) {
            return false;
        }
        setEfficiencyAndOc(tRecipe);
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        this.mEUt *= Math.pow(0.95D, this.getCoilHeat().getTier());

        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }

        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
        return true;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();

        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (!isValidMetaTileEntity(tHatch) || tHatch.getFillableStack() == null) {
                continue;
            }
            FluidStack tStack = tHatch.getFillableStack();
            if (!tStack.isFluidEqual(GT_ModHandler.getSteam(1000)) && !tStack.isFluidEqual(Materials.Hydrogen.getGas(1000)))
                rList.add(tStack);
        }

        if (this.middleFluidHatch == null || !isValidMetaTileEntity(this.middleFluidHatch) || this.middleFluidHatch.getFillableStack() == null) {
            return rList;
        }
        this.middleFluidHatch.mRecipeMap = getRecipeMap();
        FluidStack tStack = this.middleFluidHatch.getFillableStack();
        if (tStack.isFluidEqual(GT_ModHandler.getSteam(1000)) || tStack.isFluidEqual(Materials.Hydrogen.getGas(1000)))
            rList.add(tStack);

        return rList;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 2,1,0, b, itemStack);
    }
}
