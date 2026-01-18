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

package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.*;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.maps.OilCrackerBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;

public class MTEMegaOilCracker extends MegaMultiBlockBase<MTEMegaOilCracker> implements ISurvivalConstructable {

    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMegaOilCracker> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaOilCracker>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { " p         p ", "ppgggggggggpp", " pgggggggggp ", " pgggMMMgggp ", " pgggMMMgggp ",
                        " pgggMMMgggp ", " pgggggggggp ", "ppgggggggggpp", " p         p " },
                    { " p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ", " g c c c c g ",
                        " g c c c c g ", " g c c c c g ", "pgggggggggggp", " p         p " },
                    { " p         p ", "pgggggggggggp", " g c c c c g ", " l   c   c r ", " l c c c c r ",
                        " l   c   c r ", " g c c c c g ", "pgggggggggggp", " p         p " },
                    { " p         p ", "pgggggggggggp", " g c c c c g ", " l c c c c r ", " l c c c c r ",
                        " l c c c c r ", " g c c c c g ", "pgggggggggggp", " p         p " },
                    { " p         p ", "pgggggggggggp", " g c c c c g ", " l   c   c r ", " l c c c c r ",
                        " l   c   c r ", " g c c c c g ", "pgggggggggggp", " p         p " },
                    { " p         p ", "pgggggggggggp", " g c c c c g ", " g c c c c g ", " g c c c c g ",
                        " g c c c c g ", " g c c c c g ", "pgggggggggggp", " p         p " },
                    { "ppmmmm~mmmmpp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppppppppppppp",
                        "ppppppppppppp", "ppppppppppppp", "ppppppppppppp", "ppmmmmmmmmmpp" }, }))
        .addElement(
            'c',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEMegaOilCracker::setCoilLevel, MTEMegaOilCracker::getCoilLevel))))

        .addElement('p', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'l',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(InputHatch.withAdder(MTEMegaOilCracker::addLeftHatchToMachineList))
                .hint(2)
                .casingIndex(CASING_INDEX)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'r',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(OutputHatch.withAdder(MTEMegaOilCracker::addRightHatchToMachineList))
                .hint(3)
                .casingIndex(CASING_INDEX)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'm',
            buildHatchAdder(MTEMegaOilCracker.class).atLeast(Energy.or(ExoticEnergy), Maintenance, InputBus)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'M',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(InputHatch.withAdder(MTEMegaOilCracker::addMiddleInputToMachineList))
                .hint(4)
                .casingIndex(CASING_INDEX)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .build();
    private int glassTier = -1;
    private HeatingCoilLevel heatLevel;
    protected final List<MTEHatchInput> mMiddleInputHatches = new ArrayList<>();
    protected int mInputOnSide = -1;
    protected int mOutputOnSide = -1;

    public MTEMegaOilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaOilCracker(String aName) {
        super(aName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Cracker, MOC")
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"Thermally cracks heavy hydrocarbons into lighter fractions\""),
                    EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addInfo(
                TooltipHelper.effText("-10%") + " EU Usage per " + TooltipHelper.tierText(TooltipTier.COIL) + " Tier")
            .addInfo("up to a maximum of " + TooltipHelper.effText("-50%") + " EU Usage")
            .addSeparator()
            .addInfo("Gives different benefits whether it hydro or steam-cracks:")
            .addInfo(
                "Hydro - Consumes " + TooltipHelper.coloredText("20%", EnumChatFormatting.DARK_AQUA)
                    + " less Hydrogen and outputs "
                    + TooltipHelper.coloredText("25%", EnumChatFormatting.DARK_AQUA)
                    + " more cracked fluid")
            .addInfo(
                "Steam - Outputs " + TooltipHelper.coloredText("50%", EnumChatFormatting.DARK_AQUA)
                    + " more cracked fluid")
            .addInfo(TooltipHelper.italicText("In comparison to a chemical reactor"))
            .addSeparator()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(13, 7, 9, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 197, false)
            .addCasingInfoExactly("Coil", 92, true)
            .addCasingInfoExactly("Any Tiered Glass", 196, true)
            .addEnergyHatch("Hint block", 1)
            .addMaintenanceHatch("Hint block", 1)
            .addInputHatch("Hint block", 2, 3)
            .addOutputHatch("Hint block", 2, 3)
            .addInputHatch("Steam/Hydrogen ONLY, Hint block", 4)
            .addInputBus("Optional, for programmed circuit automation. Hint block", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaOilCracker(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    public RecipeMap<OilCrackerBackend> getRecipeMap() {
        return RecipeMaps.crackingRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                this.setEuModifier(1.0F - Math.min(0.1F * (MTEMegaOilCracker.this.heatLevel.getTier() + 1), 0.5F));
                return super.process();
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 6, 6, 0, realBudget, env, false, true);
    }
    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = -1;
        this.mInputOnSide = -1;
        this.mOutputOnSide = -1;
        this.mMiddleInputHatches.clear();

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 6, 6, 0) || this.mMaintenanceHatches.size() != 1) return false;

        if (this.glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
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
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            if (this.mInputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            if (this.mOutputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
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
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            if (this.mInputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            if (this.mOutputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
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
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mMiddleInputHatches.add(tHatch);
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        final ArrayList<FluidStack> rList = new ArrayList<>();
        Map<Fluid, FluidStack> inputsFromME = new HashMap<>();
        for (final MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack tFluid : meHatch.getStoredFluids()) {
                    if (tFluid != null && !getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        inputsFromME.put(tFluid.getFluid(), tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchMultiInput) {
                for (final FluidStack tFluid : ((MTEHatchMultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && !getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    if (!getRecipeMap().getBackend()
                        .isValidCatalystFluid(tHatch.getFillableStack())) rList.add(tHatch.getFillableStack());
                }
            }
        }
        for (final MTEHatchInput tHatch : validMTEList(mMiddleInputHatches)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack tFluid : meHatch.getStoredFluids()) {
                    if (tFluid != null && getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        inputsFromME.put(tFluid.getFluid(), tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchMultiInput) {
                for (final FluidStack tFluid : ((MTEHatchMultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    final FluidStack tStack = tHatch.getFillableStack();
                    if (getRecipeMap().getBackend()
                        .isValidCatalystFluid(tStack)) {
                        rList.add(tStack);
                    }
                }
            }
        }
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    @Override
    public IStructureDefinition<MTEMegaOilCracker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public void startRecipeProcessing() {
        for (MTEHatchInput hatch : validMTEList(mMiddleInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        super.startRecipeProcessing();
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        for (MTEHatchInput hatch : validMTEList(mMiddleInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }
}
