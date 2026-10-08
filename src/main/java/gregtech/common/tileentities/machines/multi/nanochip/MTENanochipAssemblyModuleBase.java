package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
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
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchNanochipRedstone;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyor;
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

public abstract class MTENanochipAssemblyModuleBase<T extends MTEExtendedPowerMultiBlockBase<T>> extends
    MTEExtendedPowerMultiBlockBase<T> implements ISurvivalConstructable, NanochipTooltipValues, ICasingTextureProvider {

    protected static final String STRUCTURE_PIECE_BASE = "base";
    // spotless:off
    protected static final String[][] base_structure = new String[][] {
        { " VV~VV ", "       ", " VVVVV " },
        { "VPPPPPV", " ZZZZZ ", "VVVVVVV" },
        { "VPPPPPV", " ZZZZZ ", "VVVVVVV" },
        { "VPPPPPV", " ZZZZZ ", "VVVVVVV" },
        { "VPPPPPV", " ZZZZZ ", "VVVVVVV" },
        { "VPPPPPV", " ZZZZZ ", "VVVVVVV" },
        { " VVVVV ", "       ", " VVVVV " } };
    // spotless:on

    protected static final int BASE_STRUCTURE_OFFSET_X = 3;
    protected static final int BASE_STRUCTURE_OFFSET_Y = 0;
    protected static final int BASE_STRUCTURE_OFFSET_Z = 0;

    private boolean isConnected = false;

    private long availableEUt = 0;
    private int runningCooldown = 0;
    public final ArrayList<MTEHatchNanochipRedstone> redstoneHatches = new ArrayList<>();

    protected FluidStack[] fluidInputs = null;
    private byte outputColor = -1;
    public static final XSTR random = XSTR.XSTR_INSTANCE;

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

    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> vacuumConveyorInputs = new VacuumConveyorHatchMap<>();
    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorOutput> vacuumConveyorOutputs = new VacuumConveyorHatchMap<>();

    public static <B extends MTENanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> addBaseStructure(
        StructureDefinition.Builder<B> structure) {
        return structure.addShape(STRUCTURE_PIECE_BASE, base_structure)
            .addElement(
                'V',
                HatchElementBuilder.<B>builder()
                    .atLeast(
                        ModuleHatchElement.VacuumConveyorHatch,
                        InputBus,
                        InputHatch,
                        OutputHatch,
                        ModuleHatchElement.RedstoneHatch)
                    .casingIndex(CASING_INDEX_WHITE)
                    .hint(3)
                    .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
            .addElement('P', Casings.NanochipMeshInterfaceCasing.asElement())
            .addElement('Z', Casings.NanochipReinforcementCasing.asElement());
    }

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    public abstract int structureOffsetX();

    public abstract int structureOffsetY();

    public abstract int structureOffsetZ();

    public enum ModuleHatchElement implements IHatchElement<MTENanochipAssemblyModuleBase<?>> {

        VacuumConveyorHatch("GT5U.MBTT.VacuumConveyorHatch", MTENanochipAssemblyModuleBase::addConveyorToMachineList,
            MTEHatchVacuumConveyor.class) {

            @Override
            public long count(MTENanochipAssemblyModuleBase<?> tileEntity) {
                return tileEntity.vacuumConveyorInputs.size() + tileEntity.vacuumConveyorOutputs.size();
            }
        },

        RedstoneHatch("GT5U.MBTT.SplitterRedstoneHatch", MTENanochipAssemblyModuleBase::addRedstoneHatchToMachineList,
            MTEHatchNanochipRedstone.class) {

            @Override
            public long count(MTENanochipAssemblyModuleBase<?> module) {
                return module.redstoneHatches.size();
            }
        };

        private final String name;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTENanochipAssemblyModuleBase<?>> adder;

        @SafeVarargs
        ModuleHatchElement(String name, IGTHatchAdder<MTENanochipAssemblyModuleBase<?>> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.name = name;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTENanochipAssemblyModuleBase<?>> adder() {
            return adder;
        }

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public String getDescriptionLangKey() {
            return name;
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        for (ArrayList<MTEHatchVacuumConveyorInput> conveyorList : this.vacuumConveyorInputs.allHatches()) {
            for (MTEHatchVacuumConveyorInput conveyor : conveyorList) {
                conveyor.removeWatcher(this);
            }
        }
        this.vacuumConveyorInputs.clear();
        this.vacuumConveyorOutputs.clear();
        this.redstoneHatches.clear();
        fixAllIssues();
        // Base structure
        if (!checkPiece(
            STRUCTURE_PIECE_BASE,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z,
            errors)) return;
        // Module structure
        checkPiece(STRUCTURE_PIECE_MAIN, structureOffsetX(), structureOffsetY(), structureOffsetZ(), errors);
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
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_BASE,
            trigger,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z,
            realBudget,
            env,
            false,
            true);
        if (built >= 0) return built;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            structureOffsetX(),
            structureOffsetY(),
            structureOffsetZ(),
            realBudget,
            env,
            false,
            true);
    }

    public boolean addConveyorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        switch (aMetaTileEntity) {
            case null -> {
                return false;
            }
            case MTEHatchVacuumConveyorInput hatch -> {
                hatch.updateTexture(aBaseCasingIndex);
                hatch.setMainController(this.getBaseMulti());
                hatch.setModule(this);
                // Components arrive as fake items in the hatch's own storage (not mInventory), so register for the
                // hatch's push instead of relying on the inventory-dirty flag.
                hatch.addWatcher(this);
                return vacuumConveyorInputs.addHatch(hatch);
            }
            case MTEHatchVacuumConveyorOutput hatch -> {
                hatch.updateTexture(aBaseCasingIndex);
                hatch.setMainController(this.getBaseMulti());
                hatch.setModule(this);
                return vacuumConveyorOutputs.addHatch(hatch);
            }
            default -> {
            }
        }

        return false;
    }

    private boolean addRedstoneHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchNanochipRedstone redstoneHatch) {
            redstoneHatch.updateTexture(aBaseCasingIndex);
            return this.redstoneHatches.add(redstoneHatch);
        }
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.NANOCHIP;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTENanochipAssemblyModuleBaseGui<>(this);
    }

    /**
     * Find all inputs stored in the vacuum conveyor inputs.
     * If input separation is disabled, will merge all items into one list for combined lookup, while also tracking
     * 'marker items' to determine VCO color based on the recipe's first input slot.
     * If input separation is enabled, will separate items into different lists depending on their VCI color.
     */
    private ItemInputInformation getInputItemsByColor() {
        if (!isInputSeparationEnabled()) {
            List<ItemStack> inputs = new ArrayList<>();
            Map<GTUtility.ItemId, Byte> markerItems = new HashMap<>();
            for (ArrayList<MTEHatchVacuumConveyorInput> conveyorList : this.vacuumConveyorInputs.allHatches()) {
                for (MTEHatchVacuumConveyorInput conveyor : conveyorList) {
                    // Add all inputs into one list. Also save marker items for color lookup for outputting.
                    if (conveyor.contents == null) continue;
                    for (ItemStack stack : conveyor.contents.getItemRepresentations()) {
                        inputs.add(stack);
                        markerItems.put(GTUtility.ItemId.createWithoutNBT(stack), conveyor.getColorization());
                    }
                }
            }
            return new ItemInputInformation(inputs, markerItems);
        }

        Map<Byte, List<ItemStack>> inputs = new HashMap<>();
        for (ArrayList<MTEHatchVacuumConveyorInput> conveyorList : this.vacuumConveyorInputs.allHatches()) {
            for (MTEHatchVacuumConveyorInput conveyor : conveyorList) {
                // Add all inputs into separate lists, separated by color.
                if (conveyor.contents == null) continue;
                List<ItemStack> colorList = inputs.computeIfAbsent(conveyor.getColorization(), _ -> new ArrayList<>());
                colorList.addAll(conveyor.contents.getItemRepresentations());
            }
        }

        return new ItemInputInformation(inputs);
    }

    /**
     * Try to find a recipe in the recipe map using the given stored inputs
     *
     * @return A recipe if one was found, null otherwise
     */
    protected GTRecipe findRecipe(List<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        this.fluidInputs = getStoredFluids().toArray(new FluidStack[0]);
        return recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[0]))
            .fluids(fluidInputs)
            .find();
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
        return 2;
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
        this.lEUt = 0;

        if (!isConnected || baseMulti == null || runningCooldown > 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // First step in recipe checking is finding all inputs we have to deal with.
        // As a result of this process, we also get the colors of the hatch each item is found in, which
        // we will use for routing the outputs
        ItemInputInformation allInputs = getInputItemsByColor();

        // Now find a recipe with the fake inputs, checking over each color until one is found
        GTRecipe recipe = null;
        List<ItemStack> inputs = null;
        var itr = allInputs.inputs.entrySet()
            .iterator();
        while (itr.hasNext() && recipe == null) {
            var entry = itr.next();
            this.outputColor = entry.getKey();
            inputs = entry.getValue();
            recipe = findRecipe(inputs);
        }

        if (recipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
        // Refresh the output color if needed
        this.outputColor = allInputs.getPrimaryColor(recipe, this.outputColor);

        // Validate it with custom logic, by default does nothing but can be overridden
        // by the module
        CheckRecipeResult validationResult = validateRecipe(recipe);
        if (!validationResult.wasSuccessful()) return validationResult;

        // Try to find a valid output hatch to see if we have output space available, and error if we don't.
        MTEHatchVacuumConveyorOutput outputHatch = this.vacuumConveyorOutputs.findAnyColoredHatch(this.outputColor);
        if (outputHatch == null) {
            return CheckRecipeResultRegistry.noValidOutputColor(this.outputColor);
        }

        GTRecipe properRecipe = this.transformRecipe(recipe);

        // Do an initial calculation for parallels without consuming items, to determine power needed.
        ParallelHelper simulatedParallelHelper = new ParallelHelper().setItemInputs(inputs.toArray(new ItemStack[0]))
            .setFluidInputs(fluidInputs)
            .setAvailableEUt(this.availableEUt)
            .enableBatchMode(0)
            .setRecipe(properRecipe)
            .setMachine(this, false, false)
            .setMaxParallel(this.getMaximumParallel())
            .setOutputCalculation(true)
            .setCalculator(OverclockCalculator.ofNoOverclock(properRecipe))
            .setConsumption(false)
            .build();

        CheckRecipeResult result = simulatedParallelHelper.getResult();
        if (result.wasSuccessful()) {

            CCInputConsumer inputConsumer;
            if (isInputSeparationEnabled()) {
                // Ensure we only consume from the allowed color when separation is enabled
                inputConsumer = new CCInputConsumer(this.vacuumConveyorInputs, this.outputColor);
            } else {
                // Otherwise, try to consume from any VCI
                inputConsumer = new CCInputConsumer(this.vacuumConveyorInputs);
            }
            inputConsumer.consume(properRecipe, simulatedParallelHelper.getCurrentParallel(), this.fluidInputs, null);

            // Set item outputs and parallel count. Note that while these outputs are fake, we override the method to
            // not output to normal buses
            // Then use addVCOutput to convert these back into CCs in the right hatch
            int currentParallel = simulatedParallelHelper.getCurrentParallel();

            // Check for any XOR outputs to determine what to output
            ItemStack[] originalOutputs = simulatedParallelHelper.getItemOutputs();
            if (supportsXOROutput()) {
                ItemStack[] newOutputs = new ItemStack[originalOutputs.length];
                for (int i = 0; i < originalOutputs.length; i++) {
                    ItemStack output = originalOutputs[i];
                    CircuitComponent cc = CircuitComponent.tryGetFromFakeStack(output);
                    if (cc != null && cc.xorResult != null && XSTR.XSTR_INSTANCE.nextInt(10000) > cc.xorSuccessChance) {
                        // XOR result exists, failed the chance check, output the failure CC instead
                        newOutputs[i] = cc.xorResult.getFakeStack(output.stackSize);
                    } else {
                        // No CC found or no XOR result, continue as normal
                        newOutputs[i] = output;
                    }
                }
                this.mOutputItems = newOutputs;
            } else {
                this.mOutputItems = originalOutputs;
            }

            // apply 2/4 overclock with any excess power
            // this still keeps the >= 5 seconds rule so we don't have to think about sub-ticking
            int recipeDuration = properRecipe.mDuration;
            long recipeEUT = (long) properRecipe.mEUt * currentParallel;
            while (recipeDuration / 2 >= 5 * SECONDS && recipeEUT * 4 <= this.availableEUt) {
                recipeDuration /= 2;
                recipeEUT *= 4;
            }

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = recipeDuration;
            // Needs to be negative obviously to display correctly
            this.lEUt = -recipeEUT;
        }

        return result;
    }

    protected boolean supportsXOROutput() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean getDefaultInputSeparationMode() {
        return true;
    }

    /**
     * Takes in the original recipe from lookup and applies any changes to it.
     * Standard logic is overclocking and applying calibration multipliers
     * Modules can override to further transform the recipe
     *
     * @param recipe - initial recipe that is copied and modified
     * @return modified recipe
     */
    public GTRecipe transformRecipe(GTRecipe recipe) {
        double recipeDuration = recipe.mDuration * this.getModuleDurationModifier();
        double recipeEUT = recipe.mEUt * this.getEUDiscountModifier(recipe) * baseMulti.globalEUMultiplier;

        CircuitCalibration recipeCalibration = recipe
            .getMetadataOrDefault(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, null);
        if (recipeCalibration != null && baseMulti.currentThreshold != null
            && baseMulti.currentThreshold.calibrationType == recipeCalibration) {
            recipeDuration *= baseMulti.globalDurationMultiplier;
            if (recipeCalibration == CircuitCalibration.SPECIAL) {
                // restore the EU/t so people aren't getting -50% eu cost per circuit.
                recipeEUT *= 1 / Math.max(0.1, (1 - baseMulti.globalDurationMultiplier));
            }
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

        GTRecipe copiedRecipe = recipe.copy();
        copiedRecipe.setEUt((int) recipeEUT);
        copiedRecipe.setDuration((int) recipeDuration);
        return copiedRecipe;
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
        aNBT.setLong("availableEUt", this.availableEUt);

        aNBT.setBoolean("connected", this.isConnected);
        aNBT.setByte("outputColor", this.outputColor);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.euBufferSize = new BigInteger(aNBT.getByteArray("bufferSize"));
        this.currentEU = new BigInteger(aNBT.getByteArray("currentEU"));
        this.availableEUt = aNBT.getLong("availableEUt");
        this.isConnected = aNBT.getBoolean("connected");
        // Default to -1 (unset) if missing from old saves
        this.outputColor = aNBT.hasKey("outputColor") ? aNBT.getByte("outputColor") : -1;
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
            if (runningCooldown > 0) runningCooldown--;
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
                "GT5U.tooltip.nac.module.scanner.available_eut",
                GTUtility.scientificFormat(availableEUt)),
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
    protected float getEUDiscountModifier(GTRecipe recipe) {
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

    @Override
    public boolean addOutputAtomic(ItemStack aStack) {
        MTEHatchVacuumConveyorOutput hatch = findOutputHatch(this.outputColor);
        addVCOutput(aStack, hatch);
        return true;
    }

    // Modules may Override this depending on a specific mechanic
    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        for (var stack : outputItems) {
            if (!addOutputAtomic(stack)) return false;
        }
        return true;
    }

    public void addVCOutput(ItemStack aStack, MTEHatchVacuumConveyorOutput hatch) {
        if (GTUtility.isStackInvalid(aStack)) return;
        if (hatch == null) {
            stopMachine(SimpleShutDownReason.ofCritical("nac_output_hatch_missing"));
            return;
        }
        // Look up component from this output fake stack and unify it with the packet inside the output hatch
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(aStack);
        String customName = GTUtility.getStackCustomName(aStack);
        CircuitComponentPacket outputPacket = new CircuitComponentPacket(component, aStack.stackSize, customName);
        hatch.unifyPacket(outputPacket);
    }

    public void setAvailableEUt(long eut) {
        // If the available EU/t increases, add a brief running cooldown to avoid a potential power-fail
        // by allowing enough time for the module to get a new batch of EU to its buffer with updated values.
        if (this.availableEUt < eut) {
            this.runningCooldown = 20;
        }
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
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE);
    }

    /**
     * This includes the normal overlay icon even when the module is active
     */
    protected ITexture[] createNanochipModuleTextures(ForgeDirection side, ForgeDirection aFacing, boolean aActive,
        IIconContainer overlay, IIconContainer overlayGlow, IIconContainer overlayActive,
        IIconContainer overlayActiveGlow) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(overlay)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(overlayActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(overlayActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(overlay)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(overlayGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("connected", isConnected());

    }

    @Override
    public void getExtraWailaBody(ItemStack itemStack, List<String> list, NBTTagCompound tag,
        IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (tag.hasKey("connected")) {
            if (tag.getBoolean("connected")) {
                list.add(translateToLocal("GT5U.tooltip.nac.interface.connected"));
            } else {
                list.add(translateToLocal("GT5U.tooltip.nac.interface.disconnected"));
            }

        }
    }

    class ItemInputInformation {

        public final Map<Byte, List<ItemStack>> inputs;
        private final Map<GTUtility.ItemId, Byte> markerItems;

        ItemInputInformation(Map<Byte, List<ItemStack>> separatedInputs) {
            this.inputs = separatedInputs;
            this.markerItems = null;
        }

        ItemInputInformation(List<ItemStack> items, Map<GTUtility.ItemId, Byte> markerItems) {
            this.inputs = ImmutableMap.of((byte) -1, items);
            this.markerItems = markerItems;
        }

        // Set the output color to the recipe's first input's color if input separation is disabled.
        // Fallback just in case here if markerItems is null, but this shouldn't happen
        public byte getPrimaryColor(GTRecipe recipe, byte matchedColor) {
            if (MTENanochipAssemblyModuleBase.this.isInputSeparationEnabled() || markerItems == null) {
                return matchedColor;
            }
            GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(recipe.mInputs[0]);
            return markerItems.get(id);
        }
    }
}
