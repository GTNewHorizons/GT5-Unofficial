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
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.colorString;
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.coloredString;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Z;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
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
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyor;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.ItemStackWithSourceBus;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;

public class MTENanochipAssemblyComplex extends MTEExtendedPowerMultiBlockBase<MTENanochipAssemblyComplex>
    implements ISurvivalConstructable {

    public static final String STRUCTURE_PIECE_MAIN = "main";
    public static final String TOOLTIP_CC = EnumChatFormatting.YELLOW + "CC" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_CCs = EnumChatFormatting.YELLOW + "CCs" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCI = EnumChatFormatting.YELLOW + "VCI" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCIs = EnumChatFormatting.YELLOW + "VCIs" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCO = EnumChatFormatting.YELLOW + "VCO" + EnumChatFormatting.GRAY;
    public static final String TOOLTIP_VCOs = EnumChatFormatting.YELLOW + "VCOs" + EnumChatFormatting.GRAY;
    public static final String NAC_MODULE = "Module of the " + EnumChatFormatting.GREEN
        + "Nanochip Assembly Complex"
        + EnumChatFormatting.GRAY;

    public static final int CASING_INDEX_WHITE = Casings.NanochipPrimaryCasing.textureId;

    // For usage in the GUI
    public boolean isTalkModeActive = false;

    public static final IStructureDefinition<MTENanochipAssemblyComplex> STRUCTURE_DEFINITION = StructureDefinition
        .<MTENanochipAssemblyComplex>builder()
        .addShape(STRUCTURE_PIECE_MAIN, AssemblyComplexStructureString.MAIN_STRUCTURE)
        // Exterior Nanochip Casing
        .addElement('A', Casings.NanochipExteriorCasing.asElement())
        // Primary Nanochip Casing
        .addElement('B', Casings.NanochipPrimaryCasing.asElement())
        // Secondary Nanochip Casing
        .addElement('C', Casings.NanochipSecondaryCasing.asElement())
        // Computational Matrix Casing
        .addElement('J', Casings.NanochipBrainCasing.asElement())
        .addElement('D', ofFrame(Materials.Naquadah))
        // Nanochip Glass
        .addElement('E', Casings.NanochipGlass.asElement())
        // Module
        .addElement(
            'F',
            HatchElementBuilder.<MTENanochipAssemblyComplex>builder()
                .atLeast(ImmutableMap.of(AssemblyHatchElement.AssemblyModule, 0))
                .casingIndex(CASING_INDEX_WHITE)
                .hint(1)
                // Base casing or assembly module
                .buildAndChain(Casings.NanochipPrimaryCasing.asElement()))

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
                .buildAndChain(Casings.NanochipPrimaryCasing.asElement()))
        // Either a white casing block or an ignored hatch (this hatch is on the module)
        .addElement(
            'I',
            HatchElementBuilder.<MTENanochipAssemblyComplex>builder()
                .atLeast(ImmutableMap.of(AssemblyHatchElement.IgnoredHatch, 0))
                .casingIndex(CASING_INDEX_WHITE)
                .hint(3)
                .buildAndChain(Casings.NanochipPrimaryCasing.asElement()))
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
            final int maxDurationOfModuleRecipe = module.getBackend()
                .getMaxDuration();
            module.setBufferSize(this.getMaxInputEu() * maxDurationOfModuleRecipe * MODULE_CONNECT_INTERVAL);
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
        return new MultiblockTooltipBuilder().addMachineType("NAC, Nanochip-Assembly-Complex")
            .addSeparator()
            .addInfo("Uses Circuit Components (" + TOOLTIP_CCs + ") to create circuits at unimaginable speeds")
            .addInfo(
                TOOLTIP_CCs + " can only exist in the perfect environment present in"
                    + EnumChatFormatting.WHITE
                    + " Vacuum Conveyor Hatches")
            .addInfo("Convert items to " + TOOLTIP_CCs + " in the control room by placing them in a colored input bus")
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "5 seconds"
                    + EnumChatFormatting.GRAY
                    + ", all items in "
                    + coloredString()
                    + " input buses are placed")
            .addInfo("in Vacuum Conveyor Outputs (" + TOOLTIP_VCOs + ") of the same " + colorString())
            .addInfo(
                "Finished circuit " + TOOLTIP_CCs
                    + " are converted back into circuits by routing them to a "
                    + TOOLTIP_VCI
                    + " in the control room")
            .addInfo(
                TOOLTIP_CCs + " in a "
                    + coloredString()
                    + " "
                    + TOOLTIP_VCI
                    + " are placed in an output bus of the same "
                    + colorString())
            .addSeparator()
            .addInfo(
                TOOLTIP_CCs + " must be processed into a refined version by "
                    + EnumChatFormatting.WHITE
                    + EnumChatFormatting.UNDERLINE
                    + "modules"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " before they")
            .addInfo("can be assembled into a circuit in the " + EnumChatFormatting.GREEN + "Nanochip Assembly Matrix")
            .addInfo("All power from the energy hatch is distributed to the modules as they need it")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "Home of the Gestalt Rapidly Evolving Genesis Optimization System (GREGOS),")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "a pseudo-sentient program overseeing circuit production in the NAC")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "It seems to be eager to learn more, maybe you can become friends?")
            .addStructureInfo("Any control room base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any control room base casing - Input bus")
            .addStructureInfo("Any control room base casing - Vacuum Conveyor Output")
            .addStructureInfo("Any control room base casing - Output bus")
            .addStructureInfo("Any control room base casing - Energy Hatch")
            .toolTipFinisher("GregTech");
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
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTENanochipAssemblyModuleBase<?>module) {
            module.setBaseMulti(this);
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
        disconnectAll();
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
        ArrayList<ItemStackWithSourceBus> inputs = getStoredInputsWithBus();
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
                final IGregTechTileEntity baseMetaTileEntity = stack.bus.getBaseMetaTileEntity();
                for (int i = baseMetaTileEntity.getSizeInventory() - 1; i >= 0; i--) {
                    ItemStack stackInSlot = baseMetaTileEntity.getStackInSlot(i);
                    if (GTUtility.areStacksEqual(stack.stack, stackInSlot)) {
                        if (stackInSlot.stackSize >= stack.stack.stackSize) {
                            lEUt -= (stack.stack.stackSize * EU_MULTIPLIER);
                            baseMetaTileEntity.decrStackSize(i, stack.stack.stackSize);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void processComponentInputs() {
        // Convert finished circuit CCs to real circuit items and add them as output.
        for (ArrayList<MTEHatchVacuumConveyor> hatchList : this.vacuumConveyors.allHatches()) {
            for (MTEHatchVacuumConveyor hatch : filterValidMTEs(hatchList)) {
                // For each vacuum conveyor input, loop over its contents and try to find a circuit component
                if (hatch instanceof MTEHatchVacuumConveyorInput) {
                    // Skip empty hatches
                    if (hatch.contents == null) continue;
                    Map<CircuitComponent, Long> contents = hatch.contents.getComponents();
                    for (Map.Entry<CircuitComponent, Long> entry : contents.entrySet()) {
                        CircuitComponent component = entry.getKey();
                        Long amount = entry.getValue();
                        // If this entry has a real circuit, we have produced a circuit using the NAC!
                        if (component.realComponent != null) {
                            lEUt -= (amount * EU_MULTIPLIER);
                            ItemStack toOutput = GTUtility.copyAmountUnsafe(
                                (int) Math.min(Integer.MAX_VALUE, amount),
                                component.realComponent.get());
                            // Add output and deplete from hatch
                            // todo check that this is correct after item logistics changes
                            addOutputAtomic(toOutput);
                            contents.remove(component);
                        }
                    }
                }
            }
        }
    }

    private void tryChargeInternalBuffer() {
        MTEHatch hatch = this.getEnergyHatch();
        if (hatch == null) return;

        long eut = this.getMaxInputEu();
        long euToAdd = Math.min(
            eut,
            hatch.getBaseMetaTileEntity()
                .getStoredEU());
        if (hatch.getBaseMetaTileEntity()
            .decreaseStoredEnergyUnits(euToAdd, false)) {
            setEUVar(getBaseMetaTileEntity().getStoredEU() + euToAdd);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isAllowedToWork()) {
                // Every running tick, try to charge the buffer in the controller.
                tryChargeInternalBuffer();
                // If the complex is turned on, periodically reconnect modules.
                if (aTick % MODULE_CONNECT_INTERVAL == 0) {
                    if (!modules.isEmpty()) {
                        // Calculate the max power to be shared to the modules
                        long availableEnergy = Math
                            .min(this.getEUVar(), this.getMaxInputEu() * MODULE_CONNECT_INTERVAL);
                        if (availableEnergy == 0) return;
                        // iterate over the modules, sending EU to fill their internal buffers
                        for (MTENanochipAssemblyModuleBase<?> module : modules) {
                            module.connect();
                            long moduleSize = module.getBufferSize();
                            long moduleCurrentEU = module.getEUVar();
                            long euToSend = moduleSize - moduleCurrentEU;
                            if (euToSend <= 0) continue;
                            long sentEnergy = module.increaseStoredEU(Math.min(euToSend, availableEnergy));
                            availableEnergy -= sentEnergy;
                            if (availableEnergy <= 0) break;
                        }
                        setEUVar(getEUVar() - availableEnergy);
                    }
                }
            } else {
                // If the complex is turned off or unformed, disconnect all modules
                disconnectAll();
            }
        }
    }

    private static final long EU_MULTIPLIER = TierEU.UV;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // Always keep the machine running, it doesn't run recipes directly.
        if (isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 5 * SECONDS;
            lEUt = 0;
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

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("talkMode", this.isTalkModeActive);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isTalkModeActive = aNBT.getBoolean("talkMode");
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

        AssemblyModule(MTENanochipAssemblyComplex::addModuleToMachineList, MTENanochipAssemblyComplex.class) {

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
        IgnoredHatch(MTENanochipAssemblyComplex::ignoreAndAcceptHatch, MTENanochipAssemblyComplex.class) {

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
}
