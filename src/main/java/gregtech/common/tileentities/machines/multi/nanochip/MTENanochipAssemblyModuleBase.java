package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.NACRecipeMapBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyModuleBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.CCInputConsumer;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitCalibration;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gregtech.common.tileentities.machines.multi.nanochip.util.NanochipTooltipValues;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTENanochipAssemblyModuleBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> implements ISurvivalConstructable, NanochipTooltipValues {

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String[][] base_structure = new String[][] { { " VV~VV ", "       ", " VVVVV " },
        { "VVVVVVV", " ZZZZZ ", "VVVVVVV" }, { "VVVVVVV", " ZZZZZ ", "VVVVVVV" }, { "VVVVVVV", " ZZZZZ ", "VVVVVVV" },
        { "VVVVVVV", " ZZZZZ ", "VVVVVVV" }, { "VVVVVVV", " ZZZZZ ", "VVVVVVV" }, { " VVVVV ", "       ", " VVVVV " } };

    protected static final int BASE_STRUCTURE_OFFSET_X = 3;
    protected static final int BASE_STRUCTURE_OFFSET_Y = 0;
    protected static final int BASE_STRUCTURE_OFFSET_Z = 0;

    private boolean isConnected = false;

    private long availableEUt = 0;

    private final ArrayList<ItemStack> inputFakeItems = new ArrayList<>();
    private FluidStack[] fluidInputs = null;
    private byte outputColor = -1;
    private int currentParallel;

    protected MTENanochipAssemblyComplex baseMulti;

    @Nullable
    public MTENanochipAssemblyComplex getBaseMulti() {
        return baseMulti;
    }

    public MTENanochipAssemblyComplex setBaseMulti(MTENanochipAssemblyComplex baseMulti) {
        this.baseMulti = baseMulti;
        for (var hatchList : this.vacuumConveyorInputs.allHatches()) {
            for (var hatch : hatchList) {
                hatch.setMainController(baseMulti);
            }
        }
        for (var hatchList : this.vacuumConveyorOutputs.allHatches()) {
            for (var hatch : hatchList) {
                hatch.setMainController(baseMulti);
            }
        }
        return baseMulti;
    }

    public void clearBaseMulti() {
        this.baseMulti = null;
        disconnect();
    }

    public int getMaxRecipeDuration() {
        return ((NACRecipeMapBackend) (this.getRecipeMap()
            .getBackend())).getMaxDuration();
    }

    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> vacuumConveyorInputs = new VacuumConveyorHatchMap<>();
    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorOutput> vacuumConveyorOutputs = new VacuumConveyorHatchMap<>();

    public static <B extends MTENanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> addBaseStructure(
        StructureDefinition.Builder<B> structure) {
        return structure.addShape(STRUCTURE_PIECE_BASE, base_structure)
            .addElement(
                'V',
                HatchElementBuilder.<B>builder()
                    .atLeast(ModuleHatchElement.VacuumConveyorHatch, InputBus, InputHatch, OutputHatch)
                    .casingIndex(CASING_INDEX_WHITE)
                    .hint(2)
                    .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
            .addElement('Z', Casings.NanochipReinforcementCasing.asElement());
    }

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    public abstract int structureOffsetX();

    public abstract int structureOffsetY();

    public abstract int structureOffsetZ();

    public enum ModuleHatchElement implements IHatchElement<MTENanochipAssemblyModuleBase<?>> {

        VacuumConveyorHatch(MTENanochipAssemblyModuleBase::addConveyorToMachineList,
            MTENanochipAssemblyModuleBase.class) {

            @Override
            public long count(MTENanochipAssemblyModuleBase<?> tileEntity) {
                return tileEntity.vacuumConveyorInputs.size() + tileEntity.vacuumConveyorOutputs.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTENanochipAssemblyModuleBase<?>> adder;

        @SafeVarargs
        ModuleHatchElement(IGTHatchAdder<MTENanochipAssemblyModuleBase<?>> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTENanochipAssemblyModuleBase<?>> adder() {
            return adder;
        }
    }

    /**
     * Create new nanochip assembly module
     *
     * @param aID           ID of this module
     * @param aName         Name of this module
     * @param aNameRegional Localized name of this module
     */
    protected MTENanochipAssemblyModuleBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTENanochipAssemblyModuleBase(String aName) {
        super(aName);
    }

    public abstract ModuleTypes getModuleType();

    // Only checks the base structure piece
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.vacuumConveyorInputs.clear();
        this.vacuumConveyorOutputs.clear();
        fixAllIssues();
        // Base structure
        if (!checkPiece(
            STRUCTURE_PIECE_BASE,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z)) return false;
        // Module structure
        return checkPiece(STRUCTURE_PIECE_MAIN, structureOffsetX(), structureOffsetY(), structureOffsetZ());
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_BASE,
            trigger,
            hintsOnly,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z);
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            structureOffsetX(),
            structureOffsetY(),
            structureOffsetZ());
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_BASE,
            trigger,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            structureOffsetX(),
            structureOffsetY(),
            structureOffsetZ(),
            elementBudget,
            env,
            false,
            true);
        return built;
    }

    public boolean addConveyorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }

        if (aMetaTileEntity instanceof MTEHatchVacuumConveyorInput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.setMainController(this.getBaseMulti());
            return vacuumConveyorInputs.addHatch(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchVacuumConveyorOutput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.setMainController(this.getBaseMulti());
            return vacuumConveyorOutputs.addHatch(hatch);
        }
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.NANOCHIP;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTENanochipAssemblyModuleBaseGui(this);
    }

    protected static class ItemInputInformation {

        /**
         * A map containing one entry per unique item, with in each entry the color of the last hatch it was seen in.
         * This can be used to determine the output color
         */
        public final Map<GTUtility.ItemId, Byte> colors;
        public final Map<GTUtility.ItemId, ItemStack> inputs;

        public ItemInputInformation(Map<GTUtility.ItemId, Byte> colors, Map<GTUtility.ItemId, ItemStack> inputs) {
            this.colors = colors;
            this.inputs = inputs;
        }
    }

    /**
     * Find all inputs stored in the vacuum conveyor inputs.
     * Clears inputFakeItems and then adds all fake items to this hatch. Note that different stacks with the same id
     * are merged into one entry in this list, which makes lookup and parallel calculation a bit easier.
     *
     * @return Info about which hatches contained the items, and a full list of item inputs indexed by id to make
     *         parallel
     *         calculation easier
     */
    protected ItemInputInformation refreshInputItems() {
        Map<GTUtility.ItemId, Byte> itemColorMap = new HashMap<>();
        Map<GTUtility.ItemId, ItemStack> inputs = new HashMap<>();
        // Clear input items before processing
        this.inputFakeItems.clear();
        // Refresh fake stacks represented by items in the conveyor hatches.
        // Note that we only take the first hatch with items and process it
        for (ArrayList<MTEHatchVacuumConveyorInput> conveyorList : this.vacuumConveyorInputs.allHatches()) {
            for (MTEHatchVacuumConveyorInput conveyor : conveyorList) {
                // Get the contents of this hatch as fake items.
                if (conveyor.contents == null) continue;
                List<ItemStack> itemsInHatch = conveyor.contents.getItemRepresentations();
                // Store the color of this hatch for each ItemStack
                byte conveyorColor = conveyor.getColorization();
                for (ItemStack stack : itemsInHatch) {
                    GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(stack);
                    // Merge stack into the input map, so we have a list of entries that are all unique.
                    inputs.merge(
                        id,
                        stack,
                        (a, b) -> new ItemStack(a.getItem(), a.stackSize + b.stackSize, a.getItemDamage()));
                    // Also register its color
                    itemColorMap.put(id, conveyorColor);
                    // Also add the item to the list of individual input items for recipe checking
                    this.inputFakeItems.add(stack);
                }
            }
        }
        return new ItemInputInformation(itemColorMap, inputs);
    }

    /**
     * Find the color hatch that we want to use for output of the given recipe.
     *
     * @param recipe     The recipe that we are going to run
     * @param itemColors The colors the hatch each ItemStack in the recipe input can be found in
     * @return The color that the output needs to end up in. If no hatch with this color exists, the module will report
     *         that no output space is available.
     */
    protected byte findOutputColor(GTRecipe recipe, Map<GTUtility.ItemId, Byte> itemColors) {
        ItemStack firstInput = recipe.mInputs[0];
        GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(firstInput);
        // If this recipe was valid and found, this should never not exist, or we have a bug
        return itemColors.get(id);
    }

    /**
     * Try to find a recipe in the recipe map using the given stored inputs
     *
     * @return A recipe if one was found, null otherwise
     */
    protected GTRecipe findRecipe(ArrayList<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        this.fluidInputs = getStoredFluids().toArray(new FluidStack[] {});
        return recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[] {}))
            .fluids(fluidInputs)
            .find();
    }

    /**
     * Return a parallel helper, but this should not yet be built, since we will append the item consumer to it first
     */
    protected ParallelHelper createParallelHelper(GTRecipe recipe, ItemInputInformation info) {
        return new ParallelHelper().setItemInputs(this.inputFakeItems.toArray(new ItemStack[] {}))
            .setFluidInputs(fluidInputs)
            .setAvailableEUt(this.availableEUt)
            .enableBatchMode(0)
            .setRecipe(recipe)
            .setMachine(this, false, false)
            .setMaxParallel(this.getMaximumParallel())
            .setOutputCalculation(true)
            .setCalculator(OverclockCalculator.ofNoOverclock(recipe));
    }

    /**
     * Validate if a recipe can be run by this module. By default, always succeeds.
     * Override this logic if you want to do recipe validation such as tiering of the module.
     * This is called before finding output hatch space or checking parallels.
     *
     * @param recipe The recipe the module is trying to run
     * @return A successful CheckRecipeResult if the recipe should be accepted.
     */
    @NotNull
    public CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // overridable for the matrix to use recipe metadata instead.
    public int getRecipeTier(GTRecipe recipe) {
        return GTUtility.getTier(recipe.mEUt);
    }

    public int getOCFactorReduction() {
        return 4;
    }

    @Override
    public boolean drainEnergyInput(long eu) {
        BigInteger euOut = BigInteger.valueOf(eu);
        if (euOut.compareTo(currentEU) > 0) {
            currentEU = BigInteger.ZERO;
            return false;
        }
        currentEU = currentEU.subtract(euOut);
        return true;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // Reset output color
        outputColor = -1;
        currentParallel = 0;
        this.lEUt = 0;

        if (!isConnected || baseMulti == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // First step in recipe checking is finding all inputs we have to deal with.
        // As a result of this process, we also get the colors of the hatch each item is found in, which
        // we will use for routing the outputs
        ItemInputInformation inputInfo = refreshInputItems();

        // Now find a recipe with the fake inputs
        GTRecipe recipe = findRecipe(this.inputFakeItems);
        if (recipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
        // Validate it with custom logic, by default does nothing but can be overridden
        // by the module
        CheckRecipeResult validationResult = validateRecipe(recipe);
        if (!validationResult.wasSuccessful()) return validationResult;

        // Now that we know the recipe, we can figure out the color the output hatch should have
        outputColor = findOutputColor(recipe, inputInfo.colors);
        // Try to find a valid output hatch to see if we have output space available, and error if we don't.
        MTEHatchVacuumConveyorOutput outputHatch = this.vacuumConveyorOutputs.findAnyColoredHatch(this.outputColor);
        if (outputHatch == null) {
            return CheckRecipeResultRegistry.noValidOutputColor(this.outputColor);
        }
        double recipeDuration = recipe.mDuration * this.getModuleDurationModifier();
        double recipeEUT = recipe.mEUt * this.getEUDiscountModifier() * baseMulti.globalEUMultiplier;

        CircuitCalibration recipeCalibration = recipe
            .getMetadataOrDefault(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, null);
        if (recipeCalibration != null && baseMulti.currentThreshold != null
            && baseMulti.currentThreshold.calibrationType == recipeCalibration) {
            recipeDuration *= baseMulti.globalDurationMultiplier;
        }

        int remainingOverclocks = (int) Math.max(0, this.baseMulti.getEnergyHatchTier() - this.getRecipeTier(recipe));
        // max overclocks is ehatch tier - recipe tier
        // can only overclock if machine has a remaining overclock,
        // duration when overclocked won't go below 5 seconds
        // and recipe eu/t after overclock is less than available eu/t
        final int ocFactor = getOCFactorReduction();
        while (remainingOverclocks > 0 && (recipeDuration / ocFactor) >= 5 * SECONDS
            && recipeEUT * ocFactor <= this.availableEUt) {
            recipeDuration /= ocFactor;
            recipeEUT *= ocFactor;
            remainingOverclocks -= 1;
        }

        GTRecipe properRecipe = recipe.copy();
        properRecipe.setEUt((int) recipeEUT);
        properRecipe.setDuration((int) recipeDuration);

        // Create parallel helper to calculate parallel and consume inputs
        ParallelHelper parallelHelper = createParallelHelper(properRecipe, inputInfo);
        // Add item consumer to parallel helper and make it consume the input items while it builds
        parallelHelper.setConsumption(true)
            .setInputConsumer(new CCInputConsumer(this.vacuumConveyorInputs, this))
            .build();
        CheckRecipeResult result = parallelHelper.getResult();
        if (result.wasSuccessful()) {
            BigInteger euToConsume = BigInteger.valueOf(parallelHelper.getCurrentParallel())
                .multiply(BigInteger.valueOf(properRecipe.mDuration))
                .multiply(BigInteger.valueOf(properRecipe.mEUt));
            if (euToConsume.compareTo(this.currentEU) > 0) {
                return CheckRecipeResultRegistry.NAC_WAITING_FOR_POWER;
            }

            // Set item outputs and parallel count. Note that while these outputs are fake, we override the method to
            // not output to normal busses
            // Then use addVCOutput to convert these back into CCs in the right hatch
            this.currentParallel = parallelHelper.getCurrentParallel();
            this.mOutputItems = parallelHelper.getItemOutputs();

            addVCOutput(mOutputItems[0], outputHatch);
            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = properRecipe.mDuration;
            // Needs to be negative obviously to display correctly
            this.lEUt = -(long) properRecipe.mEUt * (long) this.currentParallel;
        }

        return result;
    }

    public int getPriority() {
        return 1;
    }

    protected BigInteger euBufferSize = BigInteger.ZERO;
    protected BigInteger currentEU = BigInteger.ZERO;

    public void setBufferSize(BigInteger buffer) {
        this.euBufferSize = buffer;
    }

    public BigInteger getBufferSize() {
        return this.euBufferSize;
    }

    public BigInteger getCurrentEUStored() {
        return this.currentEU;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByteArray("bufferSize", this.euBufferSize.toByteArray());
        aNBT.setByteArray("currentEU", this.currentEU.toByteArray());

        aNBT.setBoolean("connected", this.isConnected);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.euBufferSize = new BigInteger(aNBT.getByteArray("bufferSize"));
        this.currentEU = new BigInteger(aNBT.getByteArray("currentEU"));
        this.isConnected = aNBT.getBoolean("connected");
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        ProcessingLogic aProcessingLogic = new ProcessingLogic();
        aProcessingLogic.setMachine(this);
        aProcessingLogic.setAmperageOC(false);
        return aProcessingLogic;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (mEfficiency < 0) mEfficiency = 0;
            if (currentEU.compareTo(BigInteger.ZERO) <= 0 && mMaxProgresstime > 0) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            }
        }
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            translateToLocalFormatted(
                "GT5U.tooltip.nac.module.scanner.current_eu",
                GTUtility.scientificFormat(currentEU)),
            translateToLocalFormatted(
                "GT5U.tooltip.nac.module.scanner.total_buffer",
                GTUtility.scientificFormat(euBufferSize)) };
    }

    /**
     * Determines the maximum parallel for use in {@see createParallelHelper}
     * In case any specific module wants to control this value.
     */
    protected int getMaximumParallel() {
        return Integer.MAX_VALUE;
    }

    /**
     * Further applies a modifier to speed
     * In case any specific module wants to control this value.
     */
    protected float getModuleDurationModifier() {
        return 1f;
    }

    /**
     * Applies an EU Discount
     * In case any specific module wants to control this value
     */
    protected float getEUDiscountModifier() {
        return 1;
    }

    /**
     * Increase the EU stored in the controller buffer
     *
     * @param maximumIncrease EU that should be added to the buffer
     * @return Actually used amount
     */
    public BigInteger increaseStoredEU(BigInteger maximumIncrease) {
        if (getBaseMetaTileEntity() == null) {
            return BigInteger.ZERO;
        }
        isConnected = true;
        BigInteger euToFull = euBufferSize.subtract(currentEU);
        BigInteger increasedEU = euToFull.min(maximumIncrease);
        currentEU = currentEU.add(increasedEU);
        return increasedEU;
    }

    protected MTEHatchVacuumConveyorOutput findOutputHatch(byte color) {
        return vacuumConveyorOutputs.findAnyColoredHatch(color);
    }

    protected boolean removeItemFromInputByColor(ItemStack stack, byte color) {
        int totalToConsome = stack.stackSize;
        List<MTEHatchVacuumConveyorInput> hatches = vacuumConveyorInputs.findColoredHatches(color);
        for (MTEHatchVacuumConveyorInput inputHatch : hatches) {
            int amountConsumed = inputHatch.tryConsume(stack);
            totalToConsome -= amountConsumed;
            if (totalToConsome <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addOutputAtomic(ItemStack aStack) {
        // We need to override this because outputs are produced in vacuum conveyor outputs, not as real items
        if (GTUtility.isStackInvalid(aStack)) return false;
        MTEHatchVacuumConveyorOutput hatch = findOutputHatch(this.outputColor);
        if (hatch == null) {
            stopMachine(SimpleShutDownReason.ofCritical("Colored output hatch disappeared mid-recipe."));
            return false;
        }
        // Look up component from this output fake stack and unify it with the packet inside the output hatch
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(aStack);
        CircuitComponentPacket outputPacket = new CircuitComponentPacket(component, aStack.stackSize);
        hatch.unifyPacket(outputPacket);
        return true;
    }

    // Modules may Override this depending on a specific mechanic
    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        return true;
    }

    public void addVCOutput(ItemStack aStack, MTEHatchVacuumConveyorOutput hatch) {
        if (GTUtility.isStackInvalid(aStack)) return;
        if (hatch == null) {
            stopMachine(SimpleShutDownReason.ofCritical("Colored output hatch disappeared mid-recipe."));
            return;
        }
        // Look up component from this output fake stack and unify it with the packet inside the output hatch
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(aStack);
        CircuitComponentPacket outputPacket = new CircuitComponentPacket(component, aStack.stackSize);
        hatch.unifyPacket(outputPacket);
    }

    public void setAvailableEUt(long eut) {
        this.availableEUt = eut;
    }

    public void connect(MTENanochipAssemblyComplex baseMulti) {
        isConnected = true;
        this.setBaseMulti(baseMulti);
    }

    public void disconnect() {
        isConnected = false;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // Does not get have maintenance issues
        return true;
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("connected", isConnected());

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("connected")) {
            if (tag.getBoolean("connected")) {
                currentTip.add(EnumChatFormatting.GREEN + "Connected To NAC");
            } else {
                currentTip.add(EnumChatFormatting.RED + "Disconnected from NAC");
            }

        }
    }
}
