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

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

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
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

@Optional.Interface(
        iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti",
        modid = "tectech",
        striprefs = true)
public class GT_TileEntity_MegaVacuumFreezer extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaVacuumFreezer>
        implements ISurvivalConstructable {

    public GT_TileEntity_MegaVacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaVacuumFreezer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaVacuumFreezer(this.mName);
    }

    private int mCasing = 0;

    private static final int CASING_INDEX = 17;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaVacuumFreezer> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_MegaVacuumFreezer>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccc~ccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "c=============c",
                            "ccccccccccccccc"
                        },
                        {
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc",
                            "ccccccccccccccc"
                        }
                    }))
                    .addElement('=', StructureElementAirNoHint.getInstance())
                    .addElement(
                            'c',
                            buildHatchAdder(GT_TileEntity_MegaVacuumFreezer.class)
                                    .atLeast(
                                            TTEnabledEnergyHatchElement.INSTANCE,
                                            InputHatch,
                                            InputBus,
                                            OutputHatch,
                                            OutputBus,
                                            Maintenance)
                                    .casingIndex(CASING_INDEX)
                                    .dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> x.mCasing++, ofBlock(GregTech_API.sBlockCasings2, 1))))
                    .build();

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Vacuum Freezer")
                .addInfo("Controller Block for the Mega Vacuum Freezer")
                .addInfo("Cools hot ingots and cells")
                .addSeparator()
                .beginStructureBlock(15, 15, 15, true)
                .addController("Front center")
                .addCasingInfo("Frost Proof Machine Casing", 900)
                .addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1)
                .addInputHatch("Any casing", 1)
                .addOutputHatch("Any casing", 1)
                .addInputBus("Any casing", 1)
                .addOutputBus("Any casing", 1)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaVacuumFreezer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 7, 7, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 7, 0, realBudget, source, actor, false, true);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sVacuumRecipes;
    }

    private boolean mUseMultiparallelMode = false;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mUseMultiparallelMode", mUseMultiparallelMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mUseMultiparallelMode = aNBT.getBoolean("mUseMultiparallelMode");
    }

    @Override
    public boolean onWireCutterRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            mUseMultiparallelMode = !mUseMultiparallelMode;
            if (mUseMultiparallelMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
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

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.findRecipe(
                this.getBaseMetaTileEntity(), false, V[tTier], tInputFluids, tInputs);
        boolean found_Recipe = false;
        int processed = 0;
        float tBatchMultiplier = 1.0f;

        if (tRecipe != null) {
            found_Recipe = true;
            long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / tRecipe.mEUt);

            if (mUseMultiparallelMode && tMaxPara == ConfigHandler.megaMachinesMax) {
                tMaxPara *= 128;
            }

            int tCurrentPara = handleParallelRecipe(tRecipe, tInputFluids, tInputs, (int) tMaxPara);
            tBatchMultiplier =
                    mUseMultiparallelMode ? (float) Math.max(tCurrentPara / ConfigHandler.megaMachinesMax, 1.0f) : 1.0f;

            this.updateSlots();
            if (tCurrentPara <= 0) return false;
            processed = Math.min(tCurrentPara, ConfigHandler.megaMachinesMax);
            Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs = getMultiOutput(tRecipe, tCurrentPara);
            outputFluids = Outputs.getKey();
            outputItems = Outputs.getValue();
        }

        if (found_Recipe) {
            this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                    .setRecipeEUt(tRecipe.mEUt)
                    .setParallel(processed)
                    .setDuration(tRecipe.mDuration)
                    .setEUt(nominalV)
                    .calculate();

            this.mMaxProgresstime = calculator.getDuration();
            this.lEUt = calculator.getConsumption();

            // In case recipe is too OP for that machine
            if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.lEUt == Integer.MAX_VALUE - 1) return false;
            if (this.lEUt > 0) {
                this.lEUt = (-this.lEUt);
            }

            if (mUseMultiparallelMode) {
                this.mMaxProgresstime = (int) Math.ceil(this.mMaxProgresstime * tBatchMultiplier);
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

    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }
        this.mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 7, 0)) return false;
        return this.mMaintenanceHatches.size() == 1
                && (LoaderReference.tectech
                        ? (!this.getTecTechEnergyMultis().isEmpty()
                                || !this.getTecTechEnergyTunnels().isEmpty()
                                || !this.mEnergyHatches.isEmpty())
                        : !this.mEnergyHatches.isEmpty())
                && this.mCasing >= 900;
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        ITexture[] rTexture;
        if (aSide == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    casingTexturePages[0][17],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            } else {
                rTexture = new ITexture[] {
                    casingTexturePages[0][17],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_VACUUM_FREEZER)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            }
        } else {
            rTexture = new ITexture[] {casingTexturePages[0][17]};
        }
        return rTexture;
    }
}
