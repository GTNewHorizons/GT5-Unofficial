/*
 * Copyright (c) 2022 SKYCATV587
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
import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.*;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MegaUtils;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.bartimaeusnek.crossmod.tectech.helper.TecTechUtils;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilCracker;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.*;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaOilCracker extends GT_MetaTileEntity_OilCracker implements TecTechEnabledMulti {

    public GT_TileEntity_MegaOilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaOilCracker(String aName) {
        super(aName);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Oil Cracking Unit")
                .addInfo("Controller block for the Mega Oil Cracking")
                .addInfo("Thermally cracks heavy hydrocarbons into lighter fractions")
                .addInfo("More efficient than the Chemical Reactor")
                .addInfo("Gives different benefits whether it hydro or steam-cracks:")
                .addInfo("Hydro - Consumes 20% less Hydrogen and outputs 25% more cracked fluid")
                .addInfo("Steam - Outputs 50% more cracked fluid")
                .addInfo("(Values compared to cracking in the Chemical Reactor)")
                .addInfo("Place the appropriate circuit in the controller")
                .addSeparator()
                .beginStructureBlock(13, 7, 9, true)
                .addController("Front bottom")
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addInfo("Gets 10% EU/t reduction per coil tier, up to a maximum of 50%")
                .addEnergyHatch("Hint block",1)
                .addMaintenanceHatch("Hint block",1)
                .addInputHatch("Hint block",2,3)
                .addOutputHatch("Hint block",2,3)
                .addInputHatch("Steam/Hydrogen ONLY, Hint block",4)
                .toolTipFinisher(BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get());
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaOilCracker(this.mName);
    }

    @SuppressWarnings("rawtypes")
    public ArrayList TTTunnels = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList TTMultiAmp = new ArrayList<>();

    @Override
    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (LoaderReference.tectech) {
            return TecTechUtils.addEnergyInputToMachineList(this, aTileEntity, aBaseCasingIndex);
        }
        return super.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        if (LoaderReference.tectech) {
            return TecTechUtils.drainEnergyMEBFTecTech(this, aEU);
        }
        return MegaUtils.drainEnergyMegaVanilla(this, aEU);
    }

    @Override
    public long getMaxInputVoltage() {
        if (LoaderReference.tectech) {
            return TecTechUtils.getMaxInputVoltage(this);
        }
        return super.getMaxInputVoltage();
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sCrackingRecipes;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        ItemStack[] tInputs = this.getStoredInputs().toArray(new ItemStack[0]);
        FluidStack[] tInputFluids = this.getStoredFluids().toArray(new FluidStack[0]);
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        long nominalV = LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);

        byte tTier = (byte) Math.max(1, Math.min(GT_Utility.getTier(nominalV), V.length - 1));

        GT_Recipe tRecipe = getRecipeMap().findRecipe(
                getBaseMetaTileEntity(),
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                tInputFluids,
                mInventory[1]
        );

        boolean found_Recipe = false;
        int processed = 0;

        if (tRecipe != null) {
            found_Recipe = true;
            long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / tRecipe.mEUt);
            int tCurrentPara = handleParallelRecipe(tRecipe, tInputFluids, tInputs, (int) tMaxPara);
            if (tCurrentPara <= 0) {
                return false;
            }
            processed = tCurrentPara;
            Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs = getMultiOutput(tRecipe, tCurrentPara);
            outputFluids = Outputs.getKey();
            outputItems = Outputs.getValue();
        }

        if (found_Recipe) {
            this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            long actualEUT = (long) (tRecipe.mEUt) * processed;
            if (actualEUT > Integer.MAX_VALUE) {
                byte divider = 0;
                while (actualEUT > Integer.MAX_VALUE) {
                    actualEUT = actualEUT / 2;
                    divider++;
                }
                calculatePerfectOverclockedNessMulti((int) actualEUT, tRecipe.mDuration * (divider * 2), 1, nominalV);
            } else {
                calculatePerfectOverclockedNessMulti((int) actualEUT, tRecipe.mDuration, 1, nominalV);
            }
            //In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1) {
                return false;
            }

            if (this.heatLevel.getTier() < 5) {
                this.mEUt *= 1 - (0.1D * (this.heatLevel.getTier() + 1));
            }
            else {
                this.mEUt *= 0.5;
            }

            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
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
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN,aStack,aHintsOnly,6,6,0);
    }

    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glasTier = 0;

        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }

        if(checkPiece(STRUCTURE_PIECE_MAIN,6,6,0)&&(mMaintenanceHatches.size()==1)&&(mEnergyHatches.size()<=2)){
            return false;
        }


        if (this.glasTier != 8 && !this.mEnergyHatches.isEmpty()) {
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches) {
                if (this.glasTier < hatchEnergy.mTier) {
                    return false;
                }
            }
        }

        return  true;
    }

    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaOilCracker> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MegaOilCracker>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                    {" p         p ", "ppgggggggggpp", " pgggggggggp ", " pgggpppgggp ", " pgggpMpgggp ", " pgggpppgggp ", " pgggggggggp ", "ppgggggggggpp", " p         p "},
                    {" p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ", " g c c c c g ", " g c c c c g ", " g c c c c g ", "pgggggggggggp", " p         p "},
                    {" p         p ", "pgggggggggggp", " g c c c c g ", " p   c   c p ", " p c c c c p ", " p   c   c p ", " g c c c c g ", "pgggggggggggp", " p         p "},
                    {" p         p ", "pgggggggggggp", " g c c c c g ", " p c c c c p ", " l c c c c r ", " p c c c c p ", " g c c c c g ", "pgggggggggggp", " p         p "},
                    {" p         p ", "pgggggggggggp", " g c c c c g ", " p   c   c p ", " p c c c c p ", " p   c   c p ", " g c c c c g ", "pgggggggggggp", " p         p "},
                    {" p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ", " g c c c c g ", " g c c c c g ", " g c c c c g ", "pgggggggggggp", " p         p "},
                    {"ppmmmm~mmmmpp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppmmmmmmmmmpp"},

            }))
            .addElement('c', ofCoil(GT_TileEntity_MegaOilCracker::setCoilLevel, GT_TileEntity_MegaOilCracker::getCoilLevel))
            .addElement('p', ofBlock(GregTech_API.sBlockCasings4, 1))
//            .addElement('s', addTileCasing(BW_GT_MaterialReference.StainlessSteel))
            .addElement('l', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaOilCracker::addLeftHatchToMachineList, CASING_INDEX, 2)
            ))
            .addElement('r', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaOilCracker::addRightHatchToMachineList, CASING_INDEX, 3)
            ))
            .addElement('m', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaOilCracker::addEnergyInputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_TileEntity_MegaOilCracker::addMaintenanceToMachineList, CASING_INDEX, 1),
                    onElementPass(GT_TileEntity_MegaOilCracker::onCasingAdded, ofBlock(GregTech_API.sBlockCasings4, 1))
            ))
            .addElement('M', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaOilCracker::addMiddleInputToMachineList, CASING_INDEX, 4)
            ))
            .addElement('g', ofChain(
                    ofBlockAdder(GT_TileEntity_MegaOilCracker::addGlas, ItemRegistry.bw_glasses[0], 1)
            ))
            .build();

    private HeatingCoilLevel heatLevel;

    private int mCoilAmount;

    private boolean addLeftHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            if (mInputOnSide == 1){
                return false;
            }
            mInputOnSide = 0;
            mOutputOnSide = 1;
            GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            if (mOutputOnSide == 1){
                return false;
            }
            mInputOnSide = 1;
            mOutputOnSide = 0;
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            return mOutputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addRightHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            if (mInputOnSide == 0){
                return false;
            }
            mInputOnSide = 1;
            mOutputOnSide = 0;
            GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            if (mOutputOnSide == 0){
                return false;
            }
            mInputOnSide = 0;
            mOutputOnSide = 1;
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            return mOutputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addMiddleInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mMiddleInputHatches.add(tHatch);
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                if (!GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tHatch.getFillableStack())) {
                    rList.add(tHatch.getFillableStack());
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_Input tHatch : mMiddleInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                FluidStack tStack = tHatch.getFillableStack();
                if (GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tStack)) {
                    rList.add(tStack);
                }
            }
        }
        return rList;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_OilCracker> getStructureDefinition() {
        return (IStructureDefinition) STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private byte glasTier;

    private boolean addGlas(Block block, int meta) {
        if (block != ItemRegistry.bw_glasses[0]) {
            return false;
        }
        byte tier = BW_Util.getTierFromGlasMeta(meta);
        if (glasTier > 0) {
            return tier == glasTier;
        }
        glasTier = tier;
        return true;
    }

    private void onCoilAdded() {
        mCoilAmount++;
    }



    @Override
    public String[] getInfoData() {
        return LoaderReference.tectech ? this.getInfoDataArray(this) : super.getInfoData();
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
