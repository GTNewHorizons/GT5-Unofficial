package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.misc.GTStructureChannels;

public class MTEAssemblyLine extends MTEExtendedPowerMultiBlockBase<MTEAssemblyLine> implements ISurvivalConstructable {

    public ArrayList<MTEHatchDataAccess> mDataAccessHatches = new ArrayList<>();
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    private static final IStructureDefinition<MTEAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAssemblyLine>builder()
        .addShape(
            STRUCTURE_PIECE_FIRST,
            transpose(new String[][] { { " ", "e", " " }, { "~", "l", "G" }, { "g", "m", "g" }, { "b", "i", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_LATER,
            transpose(new String[][] { { " ", "e", " " }, { "d", "l", "d" }, { "g", "m", "g" }, { "b", "I", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_LAST,
            transpose(new String[][] { { " ", "e", " " }, { "d", "l", "d" }, { "g", "m", "g" }, { "o", "i", "b" }, }))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings3, 10)) // grate machine casing
        .addElement('l', ofBlock(GregTechAPI.sBlockCasings2, 9)) // assembler machine casing
        .addElement('m', ofBlock(GregTechAPI.sBlockCasings2, 5)) // assembling line casing
        .addElement('g', chainAllGlasses())
        .addElement(
            'e',
            ofChain(
                Energy.newAny(16, 1, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH),
                ofBlock(GregTechAPI.sBlockCasings2, 0)))
        .addElement(
            'd',
            buildHatchAdder(MTEAssemblyLine.class).atLeast(DataHatchElement.DataAccess)
                .hint(2)
                .casingIndex(42)
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(GregTechAPI.sBlockCasings3, 10))
        .addElement(
            'b',
            buildHatchAdder(MTEAssemblyLine.class).atLeast(InputHatch, InputHatch, InputHatch, InputHatch, Maintenance)
                .casingIndex(16)
                .hint(3)
                .allowOnly(ForgeDirection.DOWN)
                .buildAndChain(
                    ofBlock(GregTechAPI.sBlockCasings2, 0),
                    ofHatchAdder(MTEAssemblyLine::addOutputToMachineList, 16, 4)))
        .addElement(
            'I',
            ofChain(
                // all blocks nearby use solid steel casing, so let's use the texture of that
                InputBus.newAny(16, 4, ForgeDirection.DOWN),
                ofHatchAdder(MTEAssemblyLine::addOutputToMachineList, 16, 3)))
        .addElement('i', InputBus.newAny(16, 4, ForgeDirection.DOWN))
        .addElement('o', OutputBus.newAny(16, 3, ForgeDirection.DOWN))
        .build();

    public MTEAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAssemblyLine(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Assembly Line, Assline, AL")
            .addInfo("Used to craft complex machine parts (LuV+)")
            .addInfo("Items & Fluids are inserted in NEI order, one per slice")
            .addInfo("Does not run Assembler recipes")
            .addMaxTierSkips(1)
            .beginVariableStructureBlock(5, 16, 4, 4, 3, 3, false) // ?
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo("Layer 1 - Solid Steel Machine Casing, Input Bus, Solid Steel Machine Casing")
            .addStructureInfo("Layer 2 - Glass, Assembly Line Casing, Glass")
            .addStructureInfo("Layer 3 - Grate Machine Casing, Assembler Machine Casing, Grate Machine Casing")
            .addStructureInfo("Layer 4 - Empty, Solid Steel Machine Casing, Empty")
            .addStructureInfo("Up to 16 repeating slices, each one allows for 1 more item in recipes")
            .addController("Either Grate Machine Casing on the first slice")
            .addEnergyHatch("Any layer 4 casing", 1)
            .addMaintenanceHatch("Any layer 1 casing", 3)
            .addInputBus("As specified on layer 1", 4)
            .addInputHatch("Any layer 1 casing", 3)
            .addOutputBus("Replaces Input Bus or Solid Steel Machine casing on layer 1 of last slice", 3)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.data_access_hatch"),
                "Any Grate Machine Casing NOT on the first slice",
                2)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
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
    public @Nonnull CheckRecipeResult checkProcessing() {
        if (GTValues.D1) {
            GT_FML_LOGGER.info("Start ALine recipe check");
        }
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_DATA_STICKS;

        ArrayList<RecipeAssemblyLine> availableRecipes = new ArrayList<>();

        if (AssemblyLineUtils.isItemDataStick(mInventory[1])) {
            availableRecipes.addAll(AssemblyLineUtils.findALRecipeFromDataStick(mInventory[1]));
        }

        for (MTEHatchDataAccess dataAccess : validMTEList(mDataAccessHatches)) {
            availableRecipes.addAll(dataAccess.getAssemblyLineRecipes());
        }

        if (availableRecipes.isEmpty()) {
            return result;
        }

        if (GTValues.D1) {
            GT_FML_LOGGER.info("Stick accepted, " + availableRecipes.size() + " Data Sticks found");
        }

        int[] tStacks = GTValues.emptyIntArray;
        FluidStack[] tFluids = GTValues.emptyFluidStackArray;
        long averageVoltage = getAverageInputVoltage();
        int maxParallel = 1;
        long maxAmp = getMaxInputAmps();
        Map<GTUtility.ItemId, ItemStack> inputsFromME = getStoredInputsFromME();
        Map<Fluid, FluidStack> fluidsFromME = getStoredFluidsFromME();

        for (RecipeAssemblyLine tRecipe : availableRecipes) {
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

            // So here we check against the recipe found on the data stick.
            // If we run into missing buses/hatches or bad inputs, we go to the next data stick.
            // This check only happens if we have a valid up-to-date data stick.
            // first validate we have enough input busses and input hatches for this recipe
            if (mInputBusses.size() < tRecipe.mInputs.length || mInputHatches.size() < tRecipe.mFluidInputs.length) {
                if (GTValues.D1) {
                    GT_FML_LOGGER.info(
                        "Not enough sources: Need ({}, {}), has ({}, {})",
                        mInputBusses.size(),
                        tRecipe.mInputs.length,
                        mInputHatches.size(),
                        tRecipe.mFluidInputs.length);
                }
                if (result == CheckRecipeResultRegistry.NO_DATA_STICKS) result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            int originalMaxParallel = 1;
            maxParallel = originalMaxParallel;
            OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(tRecipe.mEUt)
                .setEUt(averageVoltage)
                .setAmperage(maxAmp)
                .setAmperageOC(mEnergyHatches.size() != 1)
                .setDuration(tRecipe.mDuration)
                .setParallel(originalMaxParallel);
            maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);
            int maxParallelBeforeBatchMode = maxParallel;
            if (isBatchModeEnabled()) {
                maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
            }

            if (protectsExcessItem()) {
                VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
                voidProtectionHelper.setMachine(this)
                    .setItemOutputs(new ItemStack[] { tRecipe.mOutput })
                    .setMaxParallel(maxParallel)
                    .build();
                maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
                if (voidProtectionHelper.isItemFull()) {
                    result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                    continue;
                }
            }

            // Check Inputs allign
            int[] itemConsumptions = GTRecipe.RecipeAssemblyLine.getItemConsumptionAmountArray(mInputBusses, tRecipe);
            if (itemConsumptions == null || itemConsumptions.length == 0) {
                if (result == CheckRecipeResultRegistry.NO_DATA_STICKS) result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            int currentParallel = maxParallel;

            currentParallel = (int) GTRecipe.RecipeAssemblyLine
                .maxParallelCalculatedByInputItems(mInputBusses, currentParallel, itemConsumptions, inputsFromME);

            if (currentParallel <= 0) {
                if (result == CheckRecipeResultRegistry.NO_DATA_STICKS) result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            tStacks = itemConsumptions;

            if (GTValues.D1) {
                GT_FML_LOGGER.info("All Items accepted");
            }

            // Check Fluid Inputs allign
            if (tRecipe.mFluidInputs.length > 0) {
                currentParallel = (int) RecipeAssemblyLine.maxParallelCalculatedByInputFluids(
                    mInputHatches,
                    currentParallel,
                    tRecipe.mFluidInputs,
                    fluidsFromME);
                if (currentParallel <= 0) {
                    if (result == CheckRecipeResultRegistry.NO_DATA_STICKS)
                        result = CheckRecipeResultRegistry.NO_RECIPE;
                    continue;
                }
                tFluids = tRecipe.mFluidInputs;
            }

            if (GTValues.D1) {
                GT_FML_LOGGER.info("All fluids accepted");
            }

            if (GTValues.D1) {
                GT_FML_LOGGER.info("Check overclock");
            }

            int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);

            calculator.setCurrentParallel(currentParallelBeforeBatchMode)
                .calculate();

            double batchMultiplierMax = 1;
            // In case batch mode enabled
            if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
                batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
                batchMultiplierMax = Math
                    .min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
            }
            int finalParallel = (int) (batchMultiplierMax * currentParallelBeforeBatchMode);

            lEUt = calculator.getConsumption();
            mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
            maxParallel = finalParallel;
            if (GTValues.D1) {
                GT_FML_LOGGER.info("Find available recipe");
            }
            result = CheckRecipeResultRegistry.SUCCESSFUL;
            ArrayList<ItemStack> outputs = new ArrayList<>();
            ParallelHelper.addItemsLong(outputs, tRecipe.mOutput, (long) tRecipe.mOutput.stackSize * maxParallel);
            mOutputItems = outputs.toArray(new ItemStack[0]);
            break;
        }

        if (!result.wasSuccessful()) {
            return result;
        }

        // Must be something wrong here...
        if (tStacks.length == 0 || maxParallel <= 0) {
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        if (GTValues.D1) {
            GT_FML_LOGGER.info("All checked start consuming inputs");
        }

        GTRecipe.RecipeAssemblyLine.consumeInputItems(mInputBusses, maxParallel, tStacks, inputsFromME);
        GTRecipe.RecipeAssemblyLine.consumeInputFluids(mInputHatches, maxParallel, tFluids, fluidsFromME);

        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        updateSlots();
        if (GTValues.D1) {
            GT_FML_LOGGER.info("Recipe successful");
        }
        return result;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        for (MTEHatchDataAccess hatch_dataAccess : mDataAccessHatches) {
            hatch_dataAccess.setActive(true);
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public IStructureDefinition<MTEAssemblyLine> getStructureDefinition() {
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

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDataAccess) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((MTEHatchDataAccess) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, 0, 1, 0);
        int tLength = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 5, 16);
        for (int i = 1; i < tLength; i++) {
            buildPiece(STRUCTURE_PIECE_LATER, stackSize, hintsOnly, -i, 1, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivalBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 1, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        int tLength = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 5, 16);
        for (int i = 1; i < tLength; i++) {
            build = survivalBuildPiece(STRUCTURE_PIECE_LATER, stackSize, -i, 1, 0, elementBudget, env, false, true);
            if (build >= 0) return build;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_LAST, stackSize, 1 - tLength, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return VoidingMode.ITEM_ONLY_MODES;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    private enum DataHatchElement implements IHatchElement<MTEAssemblyLine> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchDataAccess.class);
        }

        @Override
        public IGTHatchAdder<MTEAssemblyLine> adder() {
            return MTEAssemblyLine::addDataAccessToMachineList;
        }

        @Override
        public long count(MTEAssemblyLine t) {
            return t.mDataAccessHatches.size();
        }
    }
}
