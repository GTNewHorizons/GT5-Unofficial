package gregtech.common.tileentities.machines.multi.nanochip;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;

import java.util.*;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.CCInputConsumer;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;

public abstract class MTENanochipAssemblyModuleBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> implements ISurvivalConstructable {

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
    private final ArrayList<ItemStack> outputFakeItems = new ArrayList<>();
    private byte outputColor = -1;
    private int currentParallel;

    // Something, needs to be tested further what this should really be (probably MUCH higher and scale with hatch tier)
    protected static long EU_BUFFER_BASE_SIZE = 160008000L * 16384;
    protected final long euBufferSize = EU_BUFFER_BASE_SIZE;

    protected final Map<CircuitComponent, Long> processedItemCounts = new HashMap<>();

    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> vacuumConveyorInputs = new VacuumConveyorHatchMap<>();
    protected final VacuumConveyorHatchMap<MTEHatchVacuumConveyorOutput> vacuumConveyorOutputs = new VacuumConveyorHatchMap<>();

    public static <B extends MTENanochipAssemblyModuleBase<B>> StructureDefinition.Builder<B> addBaseStructure(
        StructureDefinition.Builder<B> structure) {
        return structure.addShape(STRUCTURE_PIECE_BASE, base_structure)
            .addElement(
                'V',
                HatchElementBuilder.<B>builder()
                    .atLeast(ModuleHatchElement.VacuumConveyorHatch, InputBus, InputHatch)
                    .casingIndex(CASING_INDEX_WHITE)
                    .dot(2)
                    .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 5)))
            .addElement('Z', ofBlock(GregTechAPI.sBlockCasings8, 10));
    }

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

    // Only checks the base structure piece
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.vacuumConveyorInputs.clear();
        this.vacuumConveyorOutputs.clear();
        fixAllIssues();
        return checkPiece(
            STRUCTURE_PIECE_BASE,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z);
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
            return vacuumConveyorInputs.addHatch(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchVacuumConveyorOutput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
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
    private ItemInputInformation refreshInputItems() {
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
                        (a, b) -> new ItemStack(a.getItem(), a.getItemDamage(), a.stackSize + b.stackSize));
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
            .setMaxParallel(Integer.MAX_VALUE)
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

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // Reset output color
        outputColor = -1;
        currentParallel = 0;
        this.lEUt = 0;

        if (!isConnected) {
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
            // TODO: Maybe add a custom result for this
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        // Create parallel helper to calculate parallel and consume inputs
        ParallelHelper parallelHelper = createParallelHelper(recipe, inputInfo);
        // Add item consumer to parallel helper and make it consume the input items while it builds
        parallelHelper.setConsumption(true)
            .setInputConsumer(new CCInputConsumer(this.vacuumConveyorInputs, this))
            .build();
        CheckRecipeResult result = parallelHelper.getResult();
        if (result.wasSuccessful()) {
            // Set item outputs and parallel count. Note that while these outputs are fake, we override
            // addOutput to convert these back into CCs in the right hatch
            this.currentParallel = parallelHelper.getCurrentParallel();
            this.mOutputItems = parallelHelper.getItemOutputs();

            CircuitComponent fakeItem = CircuitComponent.tryGetFromFakeStack(mOutputItems[0]);
            incrementProcessedItemCounts(fakeItem, mOutputItems[0].stackSize);
            this.processingLogic.setSpeedBonus(1F / Math.min(10, Math.max(1, 1 + getSpeedModifierForOutput(fakeItem))));

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = recipe.mDuration;
            // Needs to be negative obviously to display correctly
            this.lEUt = -(long) recipe.mEUt * (long) this.currentParallel;
        }

        return result;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        ProcessingLogic aProcessingLogic = new ProcessingLogic();
        aProcessingLogic.setMachine(this);
        aProcessingLogic.setAmperageOC(false);
        return aProcessingLogic;
    }

    private void incrementProcessedItemCounts(CircuitComponent type, long amount) {
        if (!this.processedItemCounts.containsKey(type)) this.processedItemCounts.put(type, 0L);
        this.processedItemCounts.put(type, this.processedItemCounts.get(type) + amount);
    }

    private double getSpeedModifierForOutput(CircuitComponent output) {
        if (!this.processedItemCounts.containsKey(output)) {
            throw new IllegalArgumentException("This item isn't in the item counts for the multi!");
        }
        double loss = 0;
        for (Map.Entry<CircuitComponent, Long> c : this.processedItemCounts.entrySet()) {
            if (c.getKey()
                .equals(output)) continue;
            double thisLoss = Math.log(Math.log(c.getValue()) / Math.log(5000)) / Math.log(1.1);
            if (thisLoss > 0) loss += 2 * thisLoss;
        }
        long count = this.processedItemCounts.get(output);
        return Math.log(Math.log(count) / Math.log(5000)) / Math.log(1.1) - loss;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (mEfficiency < 0) mEfficiency = 0;
            if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            }
        }
    }

    @Override
    public long maxEUStore() {
        return euBufferSize;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        // Drain EU from internal buffer in controller. We will need to charge this buffer from the
        // Assembly Complex
        if (aEU <= this.getEUVar()) {
            this.setEUVar(this.getEUVar() - aEU);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Increase the EU stored in the controller buffer
     *
     * @param maximumIncrease EU that should be added to the buffer
     * @return Actually used amount
     */
    public long increaseStoredEU(long maximumIncrease) {
        if (getBaseMetaTileEntity() == null) {
            return 0;
        }
        connect();
        long increasedEU = Math
            .min(getBaseMetaTileEntity().getEUCapacity() - getBaseMetaTileEntity().getStoredEU(), maximumIncrease);
        return getBaseMetaTileEntity().increaseStoredEnergyUnits(increasedEU, false) ? increasedEU : 0;
    }

    protected MTEHatchVacuumConveyorOutput findOutputHatch(byte color) {
        return vacuumConveyorOutputs.findAnyColoredHatch(color);
    }

    @Override
    public boolean addOutput(ItemStack aStack) {
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

    public void setAvailableEUt(long eut) {
        this.availableEUt = eut;
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
        this.availableEUt = 0;
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
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        Map.Entry<CircuitComponent, Long> optimizedEntry = processedItemCounts.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        if (optimizedEntry != null) {
            tag.setString("optimizedItem", optimizedEntry.getKey().getLocalizedName());
            tag.setDouble("speedBoost", getSpeedModifierForOutput(optimizedEntry.getKey()));
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        int insertIdx = 0;
        for(; insertIdx < currentTip.size() && !currentTip.get(insertIdx).startsWith("Producing"); insertIdx++) {}
        if(tag.hasKey("optimizedItem"))
            currentTip.add(insertIdx++, "Optimized for: " + tag.getString("optimizedItem"));
        if(tag.hasKey("speedBoost"))
            currentTip.add(insertIdx, "Speed boost: §b" + GTUtility.formatNumbers((100 * Math.min(10, tag.getDouble("speedBoost")))) + "%");
    }
}
