package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_AssemblyLineUtils;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;

public class GT_MetaTileEntity_AssemblyLine extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_AssemblyLine> implements ISurvivalConstructable {

    public ArrayList<GT_MetaTileEntity_Hatch_DataAccess> mDataAccessHatches = new ArrayList<>();
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    private static final IStructureDefinition<GT_MetaTileEntity_AssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_AssemblyLine>builder()
        .addShape(
            STRUCTURE_PIECE_FIRST,
            transpose(new String[][] { { " ", "e", " " }, { "~", "l", "G" }, { "g", "m", "g" }, { "b", "i", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_LATER,
            transpose(new String[][] { { " ", "e", " " }, { "d", "l", "d" }, { "g", "m", "g" }, { "b", "I", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_LAST,
            transpose(new String[][] { { " ", "e", " " }, { "d", "l", "d" }, { "g", "m", "g" }, { "o", "i", "b" }, }))
        .addElement('G', ofBlock(GregTech_API.sBlockCasings3, 10)) // grate machine casing
        .addElement('l', ofBlock(GregTech_API.sBlockCasings2, 9)) // assembler machine casing
        .addElement('m', ofBlock(GregTech_API.sBlockCasings2, 5)) // assembling line casing
        .addElement('g', Glasses.chainAllGlasses())
        .addElement(
            'e',
            ofChain(
                Energy.newAny(16, 1, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH),
                ofBlock(GregTech_API.sBlockCasings2, 0)))
        .addElement(
            'd',
            buildHatchAdder(GT_MetaTileEntity_AssemblyLine.class).atLeast(DataHatchElement.DataAccess)
                .dot(2)
                .casingIndex(42)
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(GregTech_API.sBlockCasings3, 10))
        .addElement(
            'b',
            buildHatchAdder(GT_MetaTileEntity_AssemblyLine.class)
                .atLeast(InputHatch, InputHatch, InputHatch, InputHatch, Maintenance)
                .casingIndex(16)
                .dot(3)
                .allowOnly(ForgeDirection.DOWN)
                .buildAndChain(
                    ofBlock(GregTech_API.sBlockCasings2, 0),
                    ofHatchAdder(GT_MetaTileEntity_AssemblyLine::addOutputToMachineList, 16, 4)))
        .addElement(
            'I',
            ofChain(
                // all blocks nearby use solid steel casing, so let's use the texture of that
                InputBus.newAny(16, 5, ForgeDirection.DOWN),
                ofHatchAdder(GT_MetaTileEntity_AssemblyLine::addOutputToMachineList, 16, 4)))
        .addElement('i', InputBus.newAny(16, 5, ForgeDirection.DOWN))
        .addElement('o', OutputBus.newAny(16, 4, ForgeDirection.DOWN))
        .build();

    public GT_MetaTileEntity_AssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_AssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_AssemblyLine(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Assembling Line")
            .addInfo("Controller block for the Assembling Line")
            .addInfo("Used to make complex machine parts (LuV+)")
            .addInfo("Does not make Assembler items")
            .addInfo("Recipe tier is at most Energy Hatch tier + 1.")
            .addSeparator()
            .beginVariableStructureBlock(5, 16, 4, 4, 3, 3, false) // ?
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo(
                "Layer 1 - Solid Steel Machine Casing, Input Bus (last can be Output Bus), Solid Steel Machine Casing")
            .addStructureInfo(
                "Layer 2 - Borosilicate Glass(any)/Warded Glass/Reinforced Glass, Assembling Line Casing, Reinforced Glass")
            .addStructureInfo("Layer 3 - Grate Machine Casing, Assembler Machine Casing, Grate Machine Casing")
            .addStructureInfo("Layer 4 - Empty, Solid Steel Machine Casing, Empty")
            .addStructureInfo("Up to 16 repeating slices, each one allows for 1 more item in recipes")
            .addController("Either Grate on layer 3 of the first slice")
            .addEnergyHatch("Any layer 4 casing", 1)
            .addMaintenanceHatch("Any layer 1 casing", 3)
            .addInputBus("As specified on layer 1", 4, 5)
            .addInputHatch("Any layer 1 casing", 3)
            .addOutputBus("Replaces Input Bus on final slice or on any solid steel casing on layer 1", 4)
            .addOtherStructurePart("Data Access Hatch", "Optional, next to controller", 2)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16] };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblylineVisualRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Start ALine recipe check");
        }
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_DATA_STICKS;

        ArrayList<ItemStack> tDataStickList = getDataItems(2);
        if (tDataStickList.isEmpty()) {
            return result;
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Stick accepted, " + tDataStickList.size() + " Data Sticks found");
        }

        int[] tStacks = new int[0];
        FluidStack[] tFluids = new FluidStack[0];
        long averageVoltage = getAverageInputVoltage();
        long maxAmp = mEnergyHatches.size() <= 1 ? 1 : getMaxInputAmps();
        int maxParallel = 1;
        Map<GT_Utility.ItemId, ItemStack> inputsFromME = getStoredInputsFromME();
        Map<Fluid, FluidStack> fluidsFromME = getStoredFluidsFromME();

        for (ItemStack tDataStick : tDataStickList) {
            GT_AssemblyLineUtils.LookupResult tLookupResult = GT_AssemblyLineUtils
                .findAssemblyLineRecipeFromDataStick(tDataStick, false);

            if (tLookupResult.getType() == GT_AssemblyLineUtils.LookupResultType.INVALID_STICK) {
                result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            GT_Recipe_AssemblyLine tRecipe = tLookupResult.getRecipe();
            // Check if the recipe on the data stick is the current recipe for it's given output, if not we update it
            // and continue to next.
            if (tLookupResult.getType() != GT_AssemblyLineUtils.LookupResultType.VALID_STACK_AND_VALID_HASH) {
                tRecipe = GT_AssemblyLineUtils.processDataStick(tDataStick);
                if (tRecipe == null) {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                    continue;
                }
            }

            // Void protection check.
            if (!canOutputAll(new ItemStack[] { tRecipe.mOutput })) {
                result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                continue;
            }

            // So here we check against the recipe found on the data stick.
            // If we run into missing buses/hatches or bad inputs, we go to the next data stick.
            // This check only happens if we have a valid up-to-date data stick.

            // first validate we have enough input busses and input hatches for this recipe
            if (mInputBusses.size() < tRecipe.mInputs.length || mInputHatches.size() < tRecipe.mFluidInputs.length) {
                if (GT_Values.D1) {
                    GT_FML_LOGGER.info(
                        "Not enough sources: Need ({}, {}), has ({}, {})",
                        mInputBusses.size(),
                        tRecipe.mInputs.length,
                        mInputHatches.size(),
                        tRecipe.mFluidInputs.length);
                }
                result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            // Check Inputs allign
            int[] itemConsumptions = GT_Recipe_AssemblyLine.getItemConsumptionAmountArray(mInputBusses, tRecipe);
            if (itemConsumptions == null || itemConsumptions.length == 0) {
                result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }
            maxParallel = (int) GT_Recipe_AssemblyLine
                .maxParallelCalculatedByInputItems(mInputBusses, maxParallel, itemConsumptions, inputsFromME);
            if (maxParallel <= 0) {
                result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }
            tStacks = itemConsumptions;

            if (GT_Values.D1) {
                GT_FML_LOGGER.info("All Items accepted");
            }

            // Check Fluid Inputs allign
            if (tRecipe.mFluidInputs.length > 0) {
                maxParallel = (int) GT_Recipe_AssemblyLine
                    .maxParallelCalculatedByInputFluids(mInputHatches, maxParallel, tRecipe.mFluidInputs, fluidsFromME);
                if (maxParallel <= 0) {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                    continue;
                }
                tFluids = tRecipe.mFluidInputs;
            }

            if (GT_Values.D1) {
                GT_FML_LOGGER.info("All fluids accepted");
            }

            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Check overclock");
            }

            // Recipe tier is limited to hatch tier + 1.
            if (tRecipe.mEUt > averageVoltage * 4) {
                result = CheckRecipeResultRegistry.insufficientPower(tRecipe.mEUt);
                continue;
            }

            // Insufficient power check.
            if (tRecipe.mEUt > maxAmp * averageVoltage) {
                result = CheckRecipeResultRegistry.insufficientPower(tRecipe.mEUt);
                continue;
            }

            calculateOverclockedNessMultiInternal(tRecipe.mEUt, tRecipe.mDuration, (int) maxAmp, averageVoltage, false);
            // In case recipe is too OP for that machine
            if (lEUt == Long.MAX_VALUE) {
                if (GT_Values.D1) {
                    GT_FML_LOGGER.info("Recipe too OP");
                }
                result = CheckRecipeResultRegistry.POWER_OVERFLOW;
                continue;
            }

            if (mMaxProgresstime == Integer.MAX_VALUE) {
                if (GT_Values.D1) {
                    GT_FML_LOGGER.info("Recipe too OP");
                }
                result = CheckRecipeResultRegistry.DURATION_OVERFLOW;
                continue;
            }

            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Find available recipe");
            }
            result = CheckRecipeResultRegistry.SUCCESSFUL;
            mOutputItems = new ItemStack[] { tRecipe.mOutput };
            break;
        }

        if (!result.wasSuccessful()) {
            return result;
        }

        // Must be something wrong here...
        if (tStacks.length == 0 || maxParallel <= 0) {
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("All checked start consuming inputs");
        }

        GT_Recipe_AssemblyLine.consumeInputItems(mInputBusses, maxParallel, tStacks, inputsFromME);
        GT_Recipe_AssemblyLine.consumeInputFluids(mInputHatches, maxParallel, tFluids, fluidsFromME);

        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        updateSlots();
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Recipe successful");
        }
        return result;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        for (GT_MetaTileEntity_Hatch_DataAccess hatch_dataAccess : mDataAccessHatches) {
            hatch_dataAccess.setActive(true);
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_AssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mDataAccessHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_FIRST, 0, 1, 0)) return false;
        return checkMachine(true) || checkMachine(false);
    }

    private boolean checkMachine(boolean leftToRight) {
        for (int i = 1; i < 16; i++) {
            if (!checkPiece(STRUCTURE_PIECE_LATER, leftToRight ? -i : i, 1, 0)) return false;
            if (!mOutputBusses.isEmpty())
                return !mEnergyHatches.isEmpty() && mMaintenanceHatches.size() == 1 && mDataAccessHatches.size() <= 1;
        }
        return false;
    }

    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    private boolean isCorrectDataItem(ItemStack aStack, int state) {
        if ((state & 1) != 0 && ItemList.Circuit_Integrated.isStackEqual(aStack, true, true)) return true;
        if ((state & 2) != 0 && ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) return true;
        return (state & 4) != 0 && ItemList.Tool_DataOrb.isStackEqual(aStack, false, true);
    }

    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    public ArrayList<ItemStack> getDataItems(int state) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (GT_Utility.isStackValid(mInventory[1]) && isCorrectDataItem(mInventory[1], state)) {
            rList.add(mInventory[1]);
        }
        for (GT_MetaTileEntity_Hatch_DataAccess tHatch : filterValidMTEs(mDataAccessHatches)) {
            for (int i = 0; i < tHatch.getBaseMetaTileEntity()
                .getSizeInventory(); i++) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(i) != null && isCorrectDataItem(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i),
                        state))
                    rList.add(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i));
            }
        }
        return rList;
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, 0, 1, 0);
        int tLength = Math.min(stackSize.stackSize + 1, 16);
        for (int i = 1; i < tLength; i++) {
            buildPiece(STRUCTURE_PIECE_LATER, stackSize, hintsOnly, -i, 1, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 1, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        int tLength = Math.min(stackSize.stackSize + 1, 16);
        for (int i = 1; i < tLength - 1; i++) {
            build = survivialBuildPiece(STRUCTURE_PIECE_LATER, stackSize, -i, 1, 0, elementBudget, env, false, true);
            if (build >= 0) return build;
        }
        return survivialBuildPiece(STRUCTURE_PIECE_LAST, stackSize, 1 - tLength, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return VoidingMode.ITEM_ONLY_MODES;
    }

    private enum DataHatchElement implements IHatchElement<GT_MetaTileEntity_AssemblyLine> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_DataAccess.class);
        }

        @Override
        public IGT_HatchAdder<GT_MetaTileEntity_AssemblyLine> adder() {
            return GT_MetaTileEntity_AssemblyLine::addDataAccessToMachineList;
        }

        @Override
        public long count(GT_MetaTileEntity_AssemblyLine t) {
            return t.mDataAccessHatches.size();
        }
    }
}
