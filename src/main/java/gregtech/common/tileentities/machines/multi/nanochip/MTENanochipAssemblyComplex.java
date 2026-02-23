package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Z;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyor;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitBatch;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitCalibration;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.ItemStackWithSourceBus;
import gregtech.common.tileentities.machines.multi.nanochip.util.NanochipTooltipValues;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;

public class MTENanochipAssemblyComplex extends MTEExtendedPowerMultiBlockBase<MTENanochipAssemblyComplex>
    implements ISurvivalConstructable, NanochipTooltipValues {

    public static final String STRUCTURE_PIECE_MAIN = "main";

    public static final int CASING_INDEX_WHITE = Casings.NanochipMeshInterfaceCasing.textureId;

    public static final int BATCH_SIZE = 1000;
    public static final int HISTORY_BLOCKS = 100;
    public static final int CALIBRATION_MAX = BATCH_SIZE * HISTORY_BLOCKS;
    public final Queue<CircuitBatch> circuitHistory = new ArrayDeque<>();
    private CircuitBatch currentBlock;

    public CircuitCalibration.CalibrationThreshold currentThreshold;

    public static final IStructureDefinition<MTENanochipAssemblyComplex> STRUCTURE_DEFINITION = StructureDefinition
        .<MTENanochipAssemblyComplex>builder()
        .addShape(STRUCTURE_PIECE_MAIN, AssemblyComplexStructureString.MAIN_STRUCTURE)
        // Exterior Nanochip Casing
        .addElement('A', Casings.NanochipFirewallProjectionCasing.asElement())
        // Primary Nanochip Casing
        .addElement('B', Casings.NanochipMeshInterfaceCasing.asElement())
        // Secondary Nanochip Casing
        .addElement('C', Casings.NanochipReinforcementCasing.asElement())
        // Computational Matrix Casing
        .addElement('J', Casings.NanochipComputationalMatrixCasing.asElement())
        .addElement('D', ofFrame(Materials.Naquadah))
        // Nanochip Glass
        .addElement('E', Casings.NanochipComplexGlass.asElement())
        // Either a module or an ignored hatch (since this hatch would be on the module)
        .addElement(
            'F',
            HatchElementBuilder.<MTENanochipAssemblyComplex>builder()
                .atLeast(ImmutableMap.of(AssemblyHatchElement.AssemblyModule, 0, AssemblyHatchElement.IgnoredHatch, 0))
                .casingIndex(CASING_INDEX_WHITE)
                .hint(1)
                // Base casing or assembly module
                .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
        // Vacuum conveyor hatches that the main controller cares about go in specific slots & Energy Hatches
        .addElement(
            'H',
            HatchElementBuilder.<MTENanochipAssemblyComplex>builder()
                .atLeast(
                    ImmutableMap.of(
                        AssemblyHatchElement.VacuumConveyorHatch,
                        1,
                        InputBus,
                        1,
                        OutputBus,
                        1,
                        ExoticEnergy,
                        1,
                        Energy,
                        0))
                .casingIndex(CASING_INDEX_WHITE)
                .hint(2)
                .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
        // Either a white casing block or an ignored hatch (this hatch is on the module)
        .addElement(
            'I',
            HatchElementBuilder.<MTENanochipAssemblyComplex>builder()
                .atLeast(ImmutableMap.of(AssemblyHatchElement.IgnoredHatch, 0))
                .casingIndex(CASING_INDEX_WHITE)
                .hint(3)
                .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
        .build();

    public static final int MODULE_CONNECT_INTERVAL = 20;

    private final ArrayList<MTENanochipAssemblyModuleBase<?>> modules = new ArrayList<>();

    private final VacuumConveyorHatchMap<MTEHatchVacuumConveyor> vacuumConveyors = new VacuumConveyorHatchMap<>();

    public MTENanochipAssemblyComplex(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTENanochipAssemblyComplex(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, MAIN_OFFSET_X, MAIN_OFFSET_Y, MAIN_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            MAIN_OFFSET_X,
            MAIN_OFFSET_Y,
            MAIN_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    private MTEHatch getEnergyHatch() {
        if (this.mExoticEnergyHatches.isEmpty()) {
            if (this.mEnergyHatches.isEmpty()) return null;
            return this.mEnergyHatches.get(0);
        }
        return this.mExoticEnergyHatches.get(0);
    }

    public long getEnergyHatchTier() {
        MTEHatch energyHatch = this.getEnergyHatch();
        return energyHatch != null ? energyHatch.getInputTier() : 0;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        modules.clear();
        vacuumConveyors.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, MAIN_OFFSET_X, MAIN_OFFSET_Y, MAIN_OFFSET_Z)) return false;
        // At least most one energy hatch is accepted
        boolean validEnergy = false;
        if (this.mEnergyHatches.isEmpty()) {
            validEnergy = this.mExoticEnergyHatches.size() == 1;
        } else {
            validEnergy = this.mEnergyHatches.size() == 1;
        }
        if (!validEnergy) return false;

        modules.sort((module1, module2) -> module2.getPriority() - module1.getPriority());

        for (MTENanochipAssemblyModuleBase<?> module : modules) {
            final int maxDurationOfModuleRecipe = module.getMaxRecipeDuration();
            // multiplty by 1.5 so there is no stuttering in between fully saturated recipes
            BigInteger bufferSize = BigInteger.valueOf(this.getMaxInputEu());
            bufferSize = bufferSize.multiply(BigInteger.valueOf(maxDurationOfModuleRecipe * 2L));
            module.setBufferSize(bufferSize);
            module.setAvailableEUt(this.getMaxInputEu());
        }
        return true;
    }

    @Override
    public IStructureDefinition<MTENanochipAssemblyComplex> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(translateToLocal("GT5U.tooltip.nac.main.machine_type"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.action", TOOLTIP_CCs))
            .addInfo(translateToLocal("GT5U.tooltip.nac.main.logo"))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.1", TOOLTIP_CCs, VC_HATCHES))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.2", TOOLTIP_CCs, TOOLTIP_COLORED))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.3", TOOLTIP_COLORED))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.4", TOOLTIP_VCOs, TOOLTIP_COLOR))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.5", TOOLTIP_CCs, TOOLTIP_VCI))
            .addInfo(
                translateToLocalFormatted(
                    "GT5U.tooltip.nac.main.body.6",
                    TOOLTIP_CCs,
                    TOOLTIP_COLORED,
                    TOOLTIP_VCI,
                    TOOLTIP_COLOR))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.7", TOOLTIP_CCs))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.8", TOOLTIP_COLORED))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.main.body.9", TOOLTIP_COLORED))
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.main.flavor.1")))
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.main.flavor.2")))
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.main.flavor.3")))
            .beginStructureBlock(63, 49, 63, false)
            .addOtherStructurePart(
                translateToLocal("GT5U.tooltip.nac.interface.nac_module"),
                translateToLocal("GT5U.tooltip.nac.interface.structure_outer_ring_base_casing"))
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 3956, false)
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 2226, false)
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 1720, false)
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.3.name"), 721, false)
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.324.name").replace("%material", Materials.Naquadah.getLocalizedName()),
                53,
                false)
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.4.name"), 32, false)
            .addStructureInfo(TOOLTIP_VCI_LONG + ": " + TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING)
            .addStructureInfo(TOOLTIP_VCO_LONG + ": " + TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING)
            .addInputBus(TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING)
            .addOutputBus(TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING)
            .addEnergyHatch(TOOLTIP_STRUCTURE_CONTROL_ROOM_BASE_CASING)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENanochipAssemblyComplex(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_NANOCHIP_ASSEMBLY_COMPLEX_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public boolean addModuleToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTENanochipAssemblyModuleBase<?>module) {
            module.connect(this);
            return modules.add(module);
        }
        return false;
    }

    public boolean addConveyorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchVacuumConveyor hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.setMainController(this);
            return vacuumConveyors.addHatch(hatch);
        }
        return false;
    }

    public boolean ignoreAndAcceptHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            return true;
        }
        return false;
    }

    /**
     * Callback that will be invoked when the controller is removed
     */
    @Override
    public void onRemoval() {
        // On destroying the controller block, all modules should be disconnected
        if (!GTMod.GT.isClientSide()) disconnectAll();
        super.onRemoval();
    }

    private void disconnectAll() {
        for (MTENanochipAssemblyModuleBase<?> module : modules) {
            module.disconnect();
            module.clearBaseMulti();
        }
    }

    private ArrayList<ItemStackWithSourceBus> getStoredInputsWithBus() {
        // We need to replicate some behaviour of getStoredInputs() here to avoid duplicating items with stocking
        // buses, but we cannot call getStoredInputs() directly because the specific hatch the ItemStack is coming
        // from matters for routing the created circuit components

        ArrayList<ItemStackWithSourceBus> inputs = new ArrayList<>();
        Map<GTUtility.ItemId, ItemStackWithSourceBus> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus bus : filterValidMTEs(this.mInputBusses)) {
            // Ignore crafting input buses
            if (bus instanceof MTEHatchCraftingInputME) {
                continue;
            }

            // Same as the original implementation of getStoredInputs(), but keep track of the bus we found the input
            // in.
            IGregTechTileEntity te = bus.getBaseMetaTileEntity();
            boolean isMEBus = bus instanceof MTEHatchInputBusME;
            for (int i = te.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack stack = te.getStackInSlot(i);
                if (stack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(stack), new ItemStackWithSourceBus(stack, bus));
                    } else {
                        inputs.add(new ItemStackWithSourceBus(stack, bus));
                    }
                }
            }
        }
        // Now add all values from the ME input map
        inputs.addAll(inputsFromME.values());
        return inputs;
    }

    // Route circuit components to a set of hatches. Returns true if the components were routed successfully and the
    // stack
    // should be consumed
    private boolean routeToHatches(List<MTEHatchVacuumConveyor> hatches, byte color, CircuitComponent component,
        int amount) {
        // If no hatches were passed, we can't route
        if (hatches == null) return false;
        // Find the first hatch that can be used for routing
        for (MTEHatchVacuumConveyor hatch : filterValidMTEs(hatches)) {
            // Hatch must be an output
            if (hatch instanceof MTEHatchVacuumConveyorOutput outputHatch) {
                // Ensure that the color matches the expected color, since hatches can be recolored in between rebuilds
                // of the hatch map
                if (outputHatch.getBaseMetaTileEntity()
                    .getColorization() != color) {
                    // If the color did not match, we found an inconsistency in the hatch map, so fix it instead
                    // of waiting for the next structure check
                    vacuumConveyors.fixConsistency();
                    continue;
                }
                // Now we can route our components to this hatch
                CircuitComponentPacket packet = new CircuitComponentPacket(component, amount);
                // Merge with the already existing hatch contents
                outputHatch.unifyPacket(packet);
                return true;
            }
        }
        return false;
    }

    private void processRealItemInputs() {
        this.startRecipeProcessing();

        ArrayList<ItemStackWithSourceBus> inputs = getStoredInputsWithBus();
        // Say the magic incantation to prevent duping
        this.startRecipeProcessing();
        // For each stack in the input, try to find a matching circuit component and if so send it to the correct hatch
        for (ItemStackWithSourceBus stack : inputs) {
            // Find a conversion recipe
            GTRecipe recipe = RecipeMaps.nanochipConversionRecipes.findRecipeQuery()
                .items(stack.stack)
                .find();
            if (recipe == null) continue;
            // If one existed, we have the component now
            CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(recipe.mOutputs[0]);
            // Find destination hatch. Note that we already know that this bus is a valid MTE, see
            // getStoredInputsWithBus
            byte busColor = stack.bus.getBaseMetaTileEntity()
                .getColorization();
            ArrayList<MTEHatchVacuumConveyor> destinationHatches = vacuumConveyors.findColoredHatches(busColor);
            // Try to route to the set of destination hatches
            boolean routed = routeToHatches(destinationHatches, busColor, component, stack.stack.stackSize);
            // If successful, consume the input
            if (routed) {
                stack.bus.removeAllResource(stack.stack);
            }
        }
    }

    private void processComponentInputs() {
        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this.getOutputBusses(), true);

        // Convert finished circuit CCs to real circuit items and add them as output.
        for (ArrayList<MTEHatchVacuumConveyor> hatchList : this.vacuumConveyors.allHatches()) {
            for (MTEHatchVacuumConveyor hatch : filterValidMTEs(hatchList)) {
                // For each vacuum conveyor input, loop over its contents and try to find a circuit component
                if (hatch instanceof MTEHatchVacuumConveyorInput) {
                    // Skip empty hatches
                    if (hatch.contents == null) continue;
                    Map<CircuitComponent, Long> contents = hatch.contents.getComponents();
                    // Use Iterator to protect against ConcurrentModificationException
                    Iterator<Map.Entry<CircuitComponent, Long>> iterator = contents.entrySet()
                        .iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<CircuitComponent, Long> entry = iterator.next();
                        CircuitComponent component = entry.getKey();
                        long amount = entry.getValue();
                        if (component.realComponent != null) {
                            long ejected = ejectCircuitComponent(ejectionHelper, component, amount);
                            if (ejected > 0) {
                                long originalAmount = contents.get(component);
                                long newAmount = originalAmount - ejected;
                                if (newAmount == 0) {
                                    iterator.remove();
                                } else {
                                    entry.setValue(newAmount);
                                }
                            }
                        }
                    }
                }
            }
        }

        ejectionHelper.commit();
    }

    // Outputs a CC to the output bus as a real item, attempting to fit as much as possible in one operation
    private long ejectCircuitComponent(ItemEjectionHelper helper, CircuitComponent component, long amount) {
        long ejected = 0;
        while (amount > 0) {
            int maxEject = (int) Math.min(Integer.MAX_VALUE, amount);
            ItemStack toOutput = GTUtility.copyAmountUnsafe(maxEject, component.realComponent.get());
            if (toOutput == null) break;

            int amountEjected = helper.ejectStack(toOutput);
            ejected += amountEjected;
            if (amountEjected != maxEject) break;
            amount -= maxEject;
        }
        return ejected;
    }

    public void addToHistory(CircuitCalibration circuitType, int amount) {
        amount = Math.min(amount, CALIBRATION_MAX);
        if (currentBlock == null) currentBlock = new CircuitBatch();

        int leftover = currentBlock.add(circuitType, amount);

        // If the current block is full, store to history. Push the oldest block if needed.
        if (currentBlock.isFull()) {
            circuitHistory.add(currentBlock);
            if (circuitHistory.size() > HISTORY_BLOCKS) {
                circuitHistory.poll();
            }
            currentBlock = new CircuitBatch();
            // Update calibration stats
            setCurrentThreshold(CircuitCalibration.getCurrentCalibration(this));
        }

        // Recursively call addToHistory to clear leftovers evenly
        if (leftover > 0) addToHistory(circuitType, leftover);
    }

    // values of calibration steps
    public float globalEUMultiplier = 1;
    // duration only gets applied if the CircuitCalibration Metadata key is present on the recipe and is active on the
    // NAC
    public float globalDurationMultiplier = 1;
    public boolean crystalT3Active = false;
    public boolean wetwareT3Active = false;
    public boolean bioT3Active = false;
    public boolean opticalT3Active = false;

    public void setCurrentThreshold(CircuitCalibration.CalibrationThreshold currentThreshold) {
        this.currentThreshold = currentThreshold;
        this.resetCalibrationValues();
        if (currentThreshold == null) return;
        currentThreshold.function.accept(this);
    }

    public void resetCalibrationValues() {
        globalEUMultiplier = 1;
        globalDurationMultiplier = 1;
        crystalT3Active = false;
        wetwareT3Active = false;
        bioT3Active = false;
        opticalT3Active = false;
    }

    public String getCalibrationTitle() {
        if (currentThreshold == null) return "Standard";
        return currentThreshold.title;
    }

    public int getCurrentBlockSize() {
        return currentBlock == null ? 0 : currentBlock.total;
    }

    public int getTotalCircuit(CircuitCalibration circuitType) {
        int total = 0;
        for (CircuitBatch batch : circuitHistory) {
            switch (circuitType) {
                case PRIMITIVE -> total += batch.primitives;
                case CRYSTAL -> total += batch.crystals;
                case WETWARE -> total += batch.wetwares;
                case BIO -> total += batch.bios;
                case OPTICAL -> total += batch.opticals;
                case EXOTIC -> total += batch.exotics;
                case COSMIC -> total += batch.cosmics;
                case TEMPORAL -> total += batch.temporals;
                case SPECIAL -> total += batch.specials;
            }
        }
        return total;
    }

    public long getHatchVar() {
        MTEHatch hatch = this.getEnergyHatch();
        if (hatch == null || hatch.getBaseMetaTileEntity() == null) return 0;

        return hatch.getBaseMetaTileEntity()
            .getStoredEU();
    }

    public void setHatchVar(long energy) {
        MTEHatch hatch = this.getEnergyHatch();
        if (hatch == null || hatch.getBaseMetaTileEntity() == null) return;
        hatch.setEUVar(energy);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isAllowedToWork()) {
                // If the complex is turned on, periodically reconnect modules.
                if (aTick % MODULE_CONNECT_INTERVAL == 0) {
                    if (!modules.isEmpty()) {
                        // Calculate the max power to be shared to the modules
                        BigInteger availableEnergy = BigInteger
                            .valueOf(Math.min(this.getHatchVar(), this.getMaxInputEu() * MODULE_CONNECT_INTERVAL));
                        if (availableEnergy.compareTo(BigInteger.ZERO) <= 0) return;
                        BigInteger drainedEnergy = BigInteger.ZERO;
                        // iterate over the modules, sending EU to fill their internal buffers
                        for (MTENanochipAssemblyModuleBase<?> module : modules) {
                            module.connect(this);

                            BigInteger moduleCapacity = module.getBufferSize();
                            BigInteger moduleStored = module.getCurrentEUStored();
                            if (moduleCapacity.compareTo(moduleStored) <= 0) continue;

                            BigInteger euToSend = moduleCapacity.subtract(moduleStored)
                                .min(availableEnergy);
                            BigInteger sentEnergy = module.increaseStoredEU(euToSend);
                            availableEnergy = availableEnergy.subtract(sentEnergy);
                            drainedEnergy = drainedEnergy.add(sentEnergy);
                            if (availableEnergy.compareTo(BigInteger.ZERO) <= 0) break;
                        }
                        setHatchVar(getHatchVar() - drainedEnergy.longValue());
                    }
                }
            } else {
                // If the complex is turned off or unformed, disconnect all modules
                disconnectAll();
            }
        }
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // Always keep the machine running, it doesn't run recipes directly.
        if (isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 5 * SECONDS;
            // Inside checkProcessing we can safely consume inputs from hatches
            processRealItemInputs();
            processComponentInputs();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipConversionRecipes;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagList history = new NBTTagList();
        for (CircuitBatch batch : circuitHistory) {
            history.appendTag(new NBTTagIntArray(batch.writeToIntArray()));
        }
        aNBT.setTag("history", history);
        if (currentBlock != null) {
            aNBT.setIntArray("currentBlock", currentBlock.writeToIntArray());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTTagList history = aNBT.getTagList("history", 11);
        for (Object rawTag : history.tagList) {
            if (rawTag instanceof NBTTagIntArray batch) {
                circuitHistory.add(new CircuitBatch(batch.func_150302_c()));
            }
        }
        setCurrentThreshold(CircuitCalibration.getCurrentCalibration(this));
        if (aNBT.hasKey("currentBlock")) currentBlock = new CircuitBatch(aNBT.getIntArray("currentBlock"));
    }

    public List<MTENanochipAssemblyModuleBase<?>> getModules() {
        return modules;
    }

    public void setModules(List<MTENanochipAssemblyModuleBase<?>> incomingList) {
        this.modules.clear();
        this.modules.addAll(incomingList);
    }

    @Override
    public boolean supportsMaintenanceIssueHoverable() {
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTENanochipAssemblyComplexGui(this);
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.NANOCHIP;
    }

    // holds a minute of eu/t reserves.
    @Override
    public long maxEUStore() {
        return 60 * this.getMaxInputEu() * MODULE_CONNECT_INTERVAL;
    }

    // Hatch adder for modules
    public enum AssemblyHatchElement implements IHatchElement<MTENanochipAssemblyComplex> {

        AssemblyModule(MTENanochipAssemblyComplex::addModuleToMachineList, MTENanochipAssemblyModuleBase.class) {

            @Override
            public long count(MTENanochipAssemblyComplex tileEntity) {
                return tileEntity.modules.size();
            }
        },
        VacuumConveyorHatch(MTENanochipAssemblyComplex::addConveyorToMachineList, MTEHatchVacuumConveyor.class) {

            @Override
            public long count(MTENanochipAssemblyComplex tileEntity) {
                return tileEntity.vacuumConveyors.size();
            }
        },
        // Hatches are allowed in the module base slots, but the assembly complex ignores these for its base operation,
        // so we need a custom adder to not add them to our hatch lists
        IgnoredHatch(MTENanochipAssemblyComplex::ignoreAndAcceptHatch, MTEHatch.class) {

            @Override
            public long count(MTENanochipAssemblyComplex tileEntity) {
                return 0;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTENanochipAssemblyComplex> adder;

        @SafeVarargs
        AssemblyHatchElement(IGTHatchAdder<MTENanochipAssemblyComplex> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTENanochipAssemblyComplex> adder() {
            return adder;
        }
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // Does not get have maintenance issues
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
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
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_NANOCHIP;
    }
}
