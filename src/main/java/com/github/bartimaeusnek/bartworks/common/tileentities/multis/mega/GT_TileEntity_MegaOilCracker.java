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

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.*;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.tectech.helper.TecTechUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@Optional.Interface(
        iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti",
        modid = "tectech",
        striprefs = true)
public class GT_TileEntity_MegaOilCracker extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaOilCracker>
        implements ISurvivalConstructable {
    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaOilCracker> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_MegaOilCracker>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {
                            " p         p ",
                            "ppgggggggggpp",
                            " pgggggggggp ",
                            " pgggpppgggp ",
                            " pgggpMpgggp ",
                            " pgggpppgggp ",
                            " pgggggggggp ",
                            "ppgggggggggpp",
                            " p         p "
                        },
                        {
                            " p         p ",
                            "pgggggggggggp",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            "pgggggggggggp",
                            " p         p "
                        },
                        {
                            " p         p ",
                            "pgggggggggggp",
                            " g c c c c g ",
                            " p   c   c p ",
                            " p c c c c p ",
                            " p   c   c p ",
                            " g c c c c g ",
                            "pgggggggggggp",
                            " p         p "
                        },
                        {
                            " p         p ",
                            "pgggggggggggp",
                            " g c c c c g ",
                            " p c c c c p ",
                            " l c c c c r ",
                            " p c c c c p ",
                            " g c c c c g ",
                            "pgggggggggggp",
                            " p         p "
                        },
                        {
                            " p         p ",
                            "pgggggggggggp",
                            " g c c c c g ",
                            " p   c   c p ",
                            " p c c c c p ",
                            " p   c   c p ",
                            " g c c c c g ",
                            "pgggggggggggp",
                            " p         p "
                        },
                        {
                            " p         p ",
                            "pgggggggggggp",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            " g c c c c g ",
                            "pgggggggggggp",
                            " p         p "
                        },
                        {
                            "ppmmmm~mmmmpp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppppppppppppp",
                            "ppmmmmmmmmmpp"
                        },
                    }))
                    .addElement(
                            'c',
                            ofCoil(
                                    GT_TileEntity_MegaOilCracker::setCoilLevel,
                                    GT_TileEntity_MegaOilCracker::getCoilLevel))
                    .addElement('p', ofBlock(GregTech_API.sBlockCasings4, 1))
                    .addElement(
                            'l',
                            InputHatch.withAdder(GT_TileEntity_MegaOilCracker::addLeftHatchToMachineList)
                                    .newAny(CASING_INDEX, 2))
                    .addElement(
                            'r',
                            OutputHatch.withAdder(GT_TileEntity_MegaOilCracker::addRightHatchToMachineList)
                                    .newAny(CASING_INDEX, 3))
                    .addElement(
                            'm',
                            buildHatchAdder(GT_TileEntity_MegaOilCracker.class)
                                    .atLeast(TTEnabledEnergyHatchElement.INSTANCE, Maintenance)
                                    .casingIndex(CASING_INDEX)
                                    .dot(1)
                                    .buildAndChain(GregTech_API.sBlockCasings4, 1))
                    .addElement(
                            'M',
                            InputHatch.withAdder(GT_TileEntity_MegaOilCracker::addMiddleInputToMachineList)
                                    .newAny(CASING_INDEX, 4))
                    .addElement(
                            'g',
                            BorosilicateGlass.ofBoroGlass(
                                    (byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glasTier = t, te -> te.glasTier))
                    .build();
    private byte glasTier;
    private HeatingCoilLevel heatLevel;
    protected final List<GT_MetaTileEntity_Hatch_Input> mMiddleInputHatches = new ArrayList<>();
    protected int mInputOnSide = -1;
    protected int mOutputOnSide = -1;

    public GT_TileEntity_MegaOilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaOilCracker(String aName) {
        super(aName);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Cracker")
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
                .addEnergyHatch("Hint block", 1)
                .addMaintenanceHatch("Hint block", 1)
                .addInputHatch("Hint block", 2, 3)
                .addOutputHatch("Hint block", 2, 3)
                .addInputHatch("Steam/Hydrogen ONLY, Hint block", 4)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaOilCracker(this.mName);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                casingTexturePages[0][CASING_INDEX],
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {casingTexturePages[0][CASING_INDEX]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(
                aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "OilCrackingUnit.png");
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

        long nominalV =
                LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);

        byte tTier = (byte) Math.max(1, Math.min(GT_Utility.getTier(nominalV), V.length - 1));

        GT_Recipe tRecipe = getRecipeMap()
                .findRecipe(
                        getBaseMetaTileEntity(),
                        false,
                        gregtech.api.enums.GT_Values.V[tTier],
                        tInputFluids,
                        mInventory[1]);

        boolean found_Recipe = false;
        int processed = 0;

        if (tRecipe != null) {
            found_Recipe = true;
            long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / tRecipe.mEUt);
            int tCurrentPara = handleParallelRecipe(tRecipe, tInputFluids, tInputs, (int) tMaxPara);
            this.updateSlots();
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
            calculateOverclockedNessMulti((int) actualEUT, tRecipe.mDuration, nominalV);
            // In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.lEUt == Integer.MAX_VALUE - 1) {
                return false;
            }

            if (this.getCoilLevel().getTier() < 5) {
                this.lEUt *= 1 - (0.1D * (this.getCoilLevel().getTier() + 1));
            } else {
                this.lEUt *= 0.5;
            }

            if (this.lEUt > 0) {
                this.lEUt = (-this.lEUt);
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputItems = new ItemStack[outputItems.size()];
            this.mOutputItems = outputItems.toArray(this.mOutputItems);
            this.mOutputFluids = new FluidStack[outputFluids.size()];
            this.mOutputFluids = outputFluids.toArray(this.mOutputFluids);
            return true;
        }
        return false;
    }

    public HeatingCoilLevel getCoilLevel() {
        return heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        heatLevel = aCoilLevel;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 6, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 6, 6, 0, realBudget, source, actor, false, true);
    }
    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glasTier = 0;
        mInputOnSide = -1;
        mOutputOnSide = -1;
        mMiddleInputHatches.clear();

        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 6, 6, 0)) return false;

        if (mMaintenanceHatches.size() != 1) return false;

        if (LoaderReference.tectech && this.glasTier < 8)
            if (!areLazorsLowPowa()
                    || areThingsNotProperlyTiered(this.getTecTechEnergyTunnels())
                    || areThingsNotProperlyTiered(this.getTecTechEnergyMultis())) return false;

        if (this.glasTier < 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier) return false;

        return true;
    }

    private boolean addLeftHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            if (mInputOnSide == 1) {
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
            if (mOutputOnSide == 1) {
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
            if (mInputOnSide == 0) {
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
            if (mOutputOnSide == 0) {
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
    public IStructureDefinition<GT_TileEntity_MegaOilCracker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @SuppressWarnings("rawtypes")
    @Optional.Method(modid = "tectech")
    private boolean areThingsNotProperlyTiered(Collection collection) {
        if (!collection.isEmpty())
            for (Object tecTechEnergyMulti : collection)
                if (((GT_MetaTileEntity_TieredMachineBlock) tecTechEnergyMulti).mTier > this.glasTier) return true;
        return false;
    }
}
