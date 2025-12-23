package gregtech.common.tileentities.machines.multi.steam;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.api.util.GTUtility.validMTEList;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTEHatchVoidBus;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.objects.overclockdescriber.SteamOverclockDescriber;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTESteamMultiBlockBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTESteamMultiBlockBase<T extends MTESteamMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> implements IOverclockDescriptionProvider {

    private final OverclockDescriber overclockDescriber;
    protected static final FluidStack STEAM_STACK = Materials.Steam.getGas(1);
    public ArrayList<MTEHatchSteamBusInput> steamBusInputs = new ArrayList<>();
    public ArrayList<MTEHatchOutputBus> steamBusOutputs = new ArrayList<>();
    public ArrayList<MTEHatchCustomFluidBase> steamHatchInputs = new ArrayList<>();

    protected static final String HIGH_PRESSURE_TOOLTIP_NOTICE = "High Pressure Doubles " + EnumChatFormatting.GREEN
        + "Speed"
        + EnumChatFormatting.GRAY
        + " and "
        + EnumChatFormatting.AQUA
        + "Steam Usage";

    protected MTESteamMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        overclockDescriber = createOverclockDescriber();
    }

    protected MTESteamMultiBlockBase(String aName) {
        super(aName);
        overclockDescriber = createOverclockDescriber();
    }

    protected static OverclockDescriber createOverclockDescriber() {
        return new SteamOverclockDescriber(SteamVariant.BRONZE, 1, 2);
    }

    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean getDefaultBatchMode() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public VoidingMode getDefaultVoidingMode() {
        return VoidingMode.VOID_NONE;
    }

    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(getCasingTextureID());
    }

    protected int getCasingTextureID() {
        return 10;
    }

    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
    }

    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
    }

    protected IIconContainer getInactiveGlowOverlay() {
        return null;
    }

    protected IIconContainer getActiveGlowOverlay() {
        return null;
    }

    /**
     * Handles the overlays and casing textures. Does not need overriding except for **very** custom classes.
     * The overriding is done in {@link #getCasingTextureID() }
     * and the various overlay methods like {@link #getInactiveOverlay()}
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture casingTexture = getCasingTexture();
        if (side != facing) {
            return new ITexture[] { casingTexture };
        }

        int textures = 1;
        IIconContainer container = aActive ? getActiveOverlay() : getInactiveOverlay();
        ITexture overlay = null;
        if (container != null) {
            textures++;
            overlay = TextureFactory.builder()
                .addIcon(container)
                .extFacing()
                .build();
        }

        IIconContainer glowContainer = aActive ? getActiveGlowOverlay() : getInactiveGlowOverlay();
        ITexture glowOverlay = null;
        if (glowContainer != null) {
            textures++;
            glowOverlay = TextureFactory.builder()
                .addIcon(glowContainer)
                .extFacing()
                .glow()
                .build();
        }

        ITexture[] retVal = new ITexture[textures];
        retVal[0] = getCasingTexture();
        if (overlay != null) retVal[1] = overlay;
        if (glowOverlay != null) retVal[2] = glowOverlay;
        return retVal;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTESteamMultiBlockBaseGui(this);
    }

    public int getThemeTier() {
        return 2;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return getThemeTier() != 2 ? GTGuiThemes.BRONZE : GTGuiThemes.STEEL;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip
                .add(RED + StatCollector.translateToLocalFormatted("GT5U.waila.multiblock.status.incomplete") + RESET);
        }
        String efficiency = RESET + StatCollector
            .translateToLocalFormatted("GT5U.waila.multiblock.status.efficiency", tag.getFloat("efficiency"));
        if (tag.getBoolean("hasProblems")) {
            currentTip
                .add(RED + StatCollector.translateToLocal("GT5U.waila.multiblock.status.has_problem") + efficiency);
        } else if (!tag.getBoolean("incompleteStructure")) {
            currentTip
                .add(GREEN + StatCollector.translateToLocal("GT5U.waila.multiblock.status.running_fine") + efficiency);
        }

        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (actualEnergyUsage > 0) {
                currentTip.add(
                    StatCollector
                        .translateToLocalFormatted("GTPP.waila.steam.use", formatNumbers(actualEnergyUsage * 20)));
            }
        }
        currentTip.add(
            GTWaila.getMachineProgressString(
                isActive,
                tag.getBoolean("isAllowedToWork"),
                tag.getInteger("maxProgress"),
                tag.getInteger("progress")));
        // Show ns on the tooltip
        if (GTMod.proxy.wailaAverageNS && tag.hasKey("averageNS")) {
            int tAverageTime = tag.getInteger("averageNS");
            currentTip.add(
                StatCollector
                    .translateToLocalFormatted("GT5U.waila.multiblock.status.cpu_load", formatNumbers(tAverageTime)));
        }
        super.getMTEWailaBody(itemStack, currentTip, accessor, config);
    }

    protected static String getSteamTierTextForWaila(NBTTagCompound tag) {
        int tierMachine = tag.getInteger("tierMachine");
        String tierMachineText;
        if (tierMachine == 1) {
            tierMachineText = "Basic";
        } else if (tierMachine == 2) {
            tierMachineText = "High Pressure";
        } else {
            tierMachineText = String.valueOf(tierMachine);
        }
        return tierMachineText;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (lEUt < 0) {
            long steamToDrain = ((-lEUt * 10000) / Math.max(1000, mEfficiency));
            if (!tryConsumeSteam((int) steamToDrain)) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack arg0) {
        return 0;
    }

    public boolean tryConsumeSteam(int amount) {
        if (getTotalSteamStored() <= 0) return false;
        return this.depleteInput(Materials.Steam.getGas(amount));
    }

    public int getTotalSteamStored() {
        int storedSteam = 0;
        for (FluidStack fluid : this.getStoredFluids()) {
            if (fluid.isFluidEqual(STEAM_STACK)) storedSteam += fluid.amount;
        }
        return storedSteam;
    }

    public int getTotalSteamCapacity() {
        int capacity = 0;
        for (MTEHatchCustomFluidBase tHatch : validMTEList(steamHatchInputs)) {
            capacity += tHatch.getRealCapacity();
        }
        return capacity;
    }

    // iterates over the hatches that hold steam
    @Override
    public boolean depleteInput(FluidStack stack, boolean simulate) {
        if (stack == null) return false;
        for (MTEHatchCustomFluidBase hatch : validMTEList(steamHatchInputs)) {
            FluidStack drainedStack = hatch.drain(ForgeDirection.UNKNOWN, stack, false);
            if (drainedStack != null && drainedStack.amount >= stack.amount) {
                if (simulate) return true;
                drainedStack = hatch.drain(ForgeDirection.UNKNOWN, stack, true);
                return drainedStack != null && drainedStack.amount >= stack.amount;
            }

        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(steamHatchInputs)) tHatch.updateSlots();
        for (MTEHatchSteamBusInput tHatch : validMTEList(steamBusInputs)) tHatch.updateSlots();
    }

    @Override
    public List<IOutputBus> getOutputBusses() {
        List<IOutputBus> output = new ArrayList<>(steamBusOutputs.size());

        for (MTEHatchOutputBus steamBusOutput : steamBusOutputs) {
            if (steamBusOutput instanceof MTEHatchSteamBusOutput steamOutput) {
                if (steamOutput.isValid()) output.add(steamOutput);
            }
        }

        return output;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (MTEHatchCustomFluidBase tHatch : validMTEList(steamHatchInputs)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            if (tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        for (MTEHatchInput hatch : this.mInputHatches) if (hatch.getFillableStack() != null) {
            byte hatchColor = hatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            rList.add(hatch.getFillableStack());
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchSteamBusInput tHatch : validMTEList(steamBusInputs)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            for (int i = tHatch.getBaseMetaTileEntity()
                .getSizeInventory() - 1; i >= 0; i--) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(i) != null) {
                    rList.add(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[1]); // <=LV recipes allowed.
        // We need to trick the GT_ParallelHelper we have enough amps for all recipe parallels.
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
        logic.setMaxTierSkips(0);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        steamHatchInputs.clear();
        steamBusInputs.clear();
        steamBusOutputs.clear();
    }

    public boolean addSteamInputBusToMachineList(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;
        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;
        if (mte instanceof MTEHatchSteamBusInput hatch) {
            hatch.updateTexture(casingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return steamBusInputs.add(hatch);
        }
        return false;
    }

    public boolean addSteamOutputBusToMachineList(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;
        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;
        if (mte instanceof MTEHatchSteamBusOutput hatch) {
            hatch.updateTexture(casingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return steamBusOutputs.add(hatch);
        } else if (mte instanceof MTEHatchVoidBus voidBus) {
            voidBus.updateTexture(casingIndex);
            voidBus.updateCraftingIcon(this.getMachineCraftingIcon());
            return steamBusOutputs.add(voidBus);
        }
        return false;
    }

    public boolean addSteamInputHatchToMachineList(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;
        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;
        if (mte instanceof MTEHatchCustomFluidBase hatch) {
            if (!hatch.mLockedFluid.equals(STEAM_STACK.getFluid())) return false;
            hatch.updateTexture(casingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return steamHatchInputs.add(hatch);
        }
        return false;
    }

    // needed to whitelist specifically steam input hatches in NEI preview
    protected static <T extends MTESteamMultiBlockBase<T>> HatchElementBuilder<T> buildSteamInput(Class<T> typeToken) {
        return buildHatchAdder(typeToken).adder(MTESteamMultiBlockBase::addSteamInputHatchToMachineList)
            .hatchIds(31040)
            .shouldReject(t -> !t.steamHatchInputs.isEmpty());
    }

    protected enum SteamHatchElement implements IHatchElement<MTESteamMultiBlockBase<?>> {

        SteamInputBus("GT5U.MBTT.SteamInputBus", MTESteamMultiBlockBase::addSteamInputBusToMachineList,
            MTEHatchSteamBusInput.class) {

            @Override
            public long count(MTESteamMultiBlockBase<?> mteSteamMultiBlockBase) {
                return mteSteamMultiBlockBase.steamBusInputs.size();
            }
        },
        SteamOutputBus("GT5U.MBTT.SteamOutputBus", MTESteamMultiBlockBase::addSteamOutputBusToMachineList,
            MTEHatchSteamBusOutput.class) {

            @Override
            public long count(MTESteamMultiBlockBase<?> mteSteamMultiBlockBase) {
                return mteSteamMultiBlockBase.steamBusOutputs.size();
            }
        };

        private final String name;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTESteamMultiBlockBase<?>> adder;

        @SafeVarargs
        SteamHatchElement(String name, IGTHatchAdder<MTESteamMultiBlockBase<?>> adder,
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
        public String getDisplayName() {
            return GTUtility.translate(name);
        }

        public IGTHatchAdder<? super MTESteamMultiBlockBase<?>> adder() {
            return adder;
        }
    }

}
