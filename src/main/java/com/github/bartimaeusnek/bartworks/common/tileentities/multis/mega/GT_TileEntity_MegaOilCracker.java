/*
 * Copyright (c) 2022 SKYCATV587 Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The
 * above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_MegaOilCracker extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaOilCracker>
        implements ISurvivalConstructable {

    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaOilCracker> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_TileEntity_MegaOilCracker>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                            new String[][] {
                                    { " p         p ", "ppgggggggggpp", " pgggggggggp ", " pgggpppgggp ",
                                            " pgggpMpgggp ", " pgggpppgggp ", " pgggggggggp ", "ppgggggggggpp",
                                            " p         p " },
                                    { " p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ",
                                            " g c c c c g ", " g c c c c g ", " g c c c c g ", "pgggggggggggp",
                                            " p         p " },
                                    { " p         p ", "pgggggggggggp", " g c c c c g ", " p   c   c p ",
                                            " p c c c c p ", " p   c   c p ", " g c c c c g ", "pgggggggggggp",
                                            " p         p " },
                                    { " p         p ", "pgggggggggggp", " g c c c c g ", " p c c c c p ",
                                            " l c c c c r ", " p c c c c p ", " g c c c c g ", "pgggggggggggp",
                                            " p         p " },
                                    { " p         p ", "pgggggggggggp", " g c c c c g ", " p   c   c p ",
                                            " p c c c c p ", " p   c   c p ", " g c c c c g ", "pgggggggggggp",
                                            " p         p " },
                                    { " p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ",
                                            " g c c c c g ", " g c c c c g ", " g c c c c g ", "pgggggggggggp",
                                            " p         p " },
                                    { "ppmmmm~mmmmpp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp",
                                            "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp",
                                            "ppmmmmmmmmmpp" }, }))
            .addElement(
                    'c',
                    withChannel(
                            "coil",
                            ofCoil(
                                    GT_TileEntity_MegaOilCracker::setCoilLevel,
                                    GT_TileEntity_MegaOilCracker::getCoilLevel)))

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
                            .atLeast(Energy.or(ExoticEnergy), Maintenance, InputBus).casingIndex(CASING_INDEX).dot(1)
                            .buildAndChain(GregTech_API.sBlockCasings4, 1))
            .addElement(
                    'M',
                    InputHatch.withAdder(GT_TileEntity_MegaOilCracker::addMiddleInputToMachineList)
                            .newAny(CASING_INDEX, 4))
            .addElement(
                    'g',
                    withChannel(
                            "glass",
                            BorosilicateGlass.ofBoroGlass(
                                    (byte) 0,
                                    (byte) 1,
                                    Byte.MAX_VALUE,
                                    (te, t) -> te.glassTier = t,
                                    te -> te.glassTier)))
            .build();
    private byte glassTier;
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
        tt.addMachineType("Cracker").addInfo("Controller block for the Mega Oil Cracking")
                .addInfo("Thermally cracks heavy hydrocarbons into lighter fractions")
                .addInfo("More efficient than the Chemical Reactor")
                .addInfo("Gives different benefits whether it hydro or steam-cracks:")
                .addInfo("Hydro - Consumes 20% less Hydrogen and outputs 25% more cracked fluid")
                .addInfo("Steam - Outputs 50% more cracked fluid")
                .addInfo("(Values compared to cracking in the Chemical Reactor)")
                .addInfo("Place the appropriate circuit in the controller or an input bus").addSeparator()
                .beginStructureBlock(13, 7, 9, true).addController("Front bottom")
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addInfo("Gets 10% EU/t reduction per coil tier, up to a maximum of 50%")
                .addEnergyHatch("Hint block", 1).addMaintenanceHatch("Hint block", 1).addInputHatch("Hint block", 2, 3)
                .addOutputHatch("Hint block", 2, 3).addInputHatch("Steam/Hydrogen ONLY, Hint block", 4)
                .addInputBus("Optional, for programmed circuit automation. Hint block", 1)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaOilCracker(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW).extFacing().glow()
                            .build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW).extFacing().glow().build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.crackingRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                this.setEuModifier(
                        1.0F - Math.min(0.1F * (GT_TileEntity_MegaOilCracker.this.heatLevel.getTier() + 1), 0.5F));
                return super.process();
            }
        }.setMaxParallel(ConfigHandler.megaMachinesMax);
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.heatLevel = aCoilLevel;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 6, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this
                .survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 6, 6, 0, realBudget, source, actor, false, true);
    }
    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = 0;
        this.mInputOnSide = -1;
        this.mOutputOnSide = -1;
        this.mMiddleInputHatches.clear();

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 6, 6, 0) || this.mMaintenanceHatches.size() != 1) return false;

        if (this.glassTier < 8) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == GT_MetaTileEntity_Hatch.ConnectionType.LASER) {
                    return false;
                }
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

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
            if (this.mInputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
            GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            if (this.mOutputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            return this.mOutputHatches.add(tHatch);
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
            if (this.mInputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
            GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            if (this.mOutputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            tHatch.updateTexture(aBaseCasingIndex);
            return this.mOutputHatches.add(tHatch);
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mMiddleInputHatches.add(tHatch);
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        final ArrayList<FluidStack> rList = new ArrayList<>();
        for (final GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                for (final FluidStack tFluid : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && !RecipeMaps.crackingRecipes.getBackend().isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    if (!RecipeMaps.crackingRecipes.getBackend().isValidCatalystFluid(tHatch.getFillableStack()))
                        rList.add(tHatch.getFillableStack());
                }
            }
        }
        for (final GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mMiddleInputHatches)) {
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                for (final FluidStack tFluid : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && RecipeMaps.crackingRecipes.getBackend().isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    final FluidStack tStack = tHatch.getFillableStack();
                    if (RecipeMaps.crackingRecipes.getBackend().isValidCatalystFluid(tStack)) {
                        rList.add(tStack);
                    }
                }
            }
        }
        return rList;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaOilCracker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
            float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
