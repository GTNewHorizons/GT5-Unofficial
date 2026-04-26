package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collection;
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
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.StructureError;
import gregtech.api.gui.modularui.CircularGaugeDrawable;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchVoidBus;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.objects.overclockdescriber.SteamOverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTESteamMultiBlockBaseGui;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTESteamMultiBlockBase<T extends MTESteamMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> implements IOverclockDescriptionProvider {

    private final OverclockDescriber overclockDescriber;

    public ArrayList<MTEHatchSteamBusInput> mSteamInputs = new ArrayList<>();
    public ArrayList<MTEHatchSteamBusOutput> mSteamOutputs = new ArrayList<>();
    public ArrayList<MTEHatchCustomFluidBase> mSteamInputFluids = new ArrayList<>();

    public static final Casings bronzeCasing = Casings.BronzePlatedBricks;
    public static final Casings steelCasing = Casings.SolidSteelMachineCasing;

    protected static final String HIGH_PRESSURE_TOOLTIP_NOTICE = "High Pressure Doubles " + EnumChatFormatting.GREEN
        + "Speed"
        + EnumChatFormatting.GRAY
        + " and "
        + EnumChatFormatting.AQUA
        + "Steam Usage";

    public MTESteamMultiBlockBase(String aName) {
        super(aName);
        this.overclockDescriber = createOverclockDescriber();
    }

    public MTESteamMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.overclockDescriber = createOverclockDescriber();
    }

    public abstract String getMachineType();

    public abstract int getTierRecipes();

    /**
     * Returns true when the structure is fully tier-2 (High Pressure / Steel).
     * Subclasses implement this based on their own tier fields, e.g.:
     * return tierMachineCasing == 2;
     * return tierGearBoxCasing == 2 && tierPipeCasing == 2 && ...;
     */
    protected abstract boolean isHighPressure();

    protected Casings getCurrentCasing() {
        return isHighPressure() ? steelCasing : bronzeCasing;
    }

    protected int getCasingTextureId() {
        return getCurrentCasing().textureId;
    }

    /**
     * Front-face active overlay icon. Must be implemented by every steam multi.
     */
    protected abstract IIconContainer getActiveOverlay();

    /**
     * Front-face inactive overlay icon. Must be implemented by every steam multi.
     */
    protected abstract IIconContainer getInactiveOverlay();

    /**
     * Optional glow layer shown only when active. Returns null by default (no glow).
     */
    @Nullable
    protected IIconContainer getActiveGlowOverlay() {
        return null;
    }

    /**
     * Optional glow layer shown only when disabled. Returns null by default (no glow).
     */
    @Nullable
    protected IIconContainer getInactiveGlowOverlay() {
        return null;
    }

    /**
     * Builds the texture array from the overlay hooks above, using the correct tier
     * casing automatically. Subclasses no longer need to override getTexture().
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        Casings casing = getCurrentCasing();
        if (side != aFacing) {
            return new ITexture[] { casing.getCasingTexture() };
        }
        IIconContainer overlayIcon = aActive ? getActiveOverlay() : getInactiveOverlay();
        IIconContainer glowIcon = aActive ? getActiveGlowOverlay() : getInactiveGlowOverlay();

        if (glowIcon != null) {
            return new ITexture[] { casing.getCasingTexture(), TextureFactory.builder()
                .addIcon(overlayIcon)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(glowIcon)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casing.getCasingTexture(), TextureFactory.builder()
            .addIcon(overlayIcon)
            .extFacing()
            .build() };
    }

    /**
     * Updates all steam hatch textures to match the current casing tier.
     * Call at the end of checkMachine() once the tier has been determined.
     */
    protected void updateHatchTexture() {
        int id = getCasingTextureId();
        for (MTEHatch h : mSteamInputs) h.updateTexture(id);
        for (MTEHatch h : mSteamOutputs) h.updateTexture(id);
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(id);
        for (MTEHatch h : mOutputHatches) h.updateTexture(id);
    }

    // tierMachine isn't synced to client - getThemeTier() it is instead of a syncHandler
    public abstract int getThemeTier();

    @Override
    protected GTGuiTheme getGuiTheme() {
        return isHighPressure() ? GTGuiThemes.STEEL : GTGuiThemes.BRONZE;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[getTierRecipes()]);
        // We need to trick the GT_ParallelHelper we have enough amps for all recipe parallels.
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
        logic.setMaxTierSkips(0);
    }

    public ArrayList<FluidStack> getAllSteamStacks() {
        ArrayList<FluidStack> aFluids = new ArrayList<>();
        FluidStack aSteam = Materials.Steam.getGas(1);
        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid.isFluidEqual(aSteam)) {
                aFluids.add(aFluid);
            }
        }
        return aFluids;
    }

    public int getTotalSteamStored() {
        int aSteam = 0;
        for (FluidStack aFluid : getAllSteamStacks()) {
            aSteam += aFluid.amount;
        }
        return aSteam;
    }

    public int getTotalSteamCapacity() {
        int aSteam = 0;
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            aSteam += tHatch.getRealCapacity();
        }
        return aSteam;
    }

    public boolean tryConsumeSteam(int aAmount) {
        if (getTotalSteamStored() <= 0) {
            return false;
        } else {
            return this.depleteInput(Materials.Steam.getGas(aAmount));
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack arg0) {
        return 0;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mSteamInputs.clear();
                this.mSteamOutputs.clear();
                this.mInputHatches.clear();
                this.mSteamInputFluids.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    /**
     * Called every tick the Machine runs
     */
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (lEUt < 0) {
            long aSteamVal = ((-lEUt * 10000) / Math.max(1000, mEfficiency));
            if (!tryConsumeSteam((int) aSteamVal)) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IMetaTileEntity aTileEntity,
        final int aBaseCasingIndex) {
        if (aTileEntity == null) return false;

        if (aTileEntity instanceof MTEHatch mteHatch) {
            mteHatch.updateTexture(aBaseCasingIndex);
            mteHatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }

        // Set recipe map for input hatches.
        if (aTileEntity instanceof MTEHatchInput hatch) hatch.mRecipeMap = getRecipeMap();
        if (aTileEntity instanceof MTEHatchInputBus hatch) hatch.mRecipeMap = getRecipeMap();

        if (aList.contains(aTileEntity)) return false;

        // noinspection unchecked
        return aList.add((E) aTileEntity);
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        if (aMetaTileEntity instanceof MTEHatchCustomFluidBase) {
            return addToMachineListInternal(mSteamInputFluids, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchSteamBusInput) {
            this.resetRecipeMapForHatch(aTileEntity, getRecipeMap());
            return addToMachineListInternal(mSteamInputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchSteamBusOutput || aMetaTileEntity instanceof MTEHatchVoidBus) {
            return addToMachineListInternal(mSteamOutputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchInput) {
            return addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        }

        return false;
    }

    public boolean resetRecipeMapForHatch(IGregTechTileEntity aTileEntity, RecipeMap<?> aMap) {
        if (aTileEntity == null) return false;
        IMetaTileEntity meta = aTileEntity.getMetaTileEntity();
        if (meta instanceof MTEHatch hatch) return resetRecipeMapForHatch(hatch, aMap);
        return false;
    }

    public boolean resetRecipeMapForHatch(MTEHatch aTileEntity, RecipeMap<?> aMap) {
        if (aTileEntity == null) return false;
        if (aTileEntity instanceof MTEHatchInput hatch) {
            hatch.mRecipeMap = aMap;
            return true;
        }
        if (aTileEntity instanceof MTEHatchInputBus hatch) {
            hatch.mRecipeMap = aMap;
            return true;
        }
        return false;
    }

    @Override
    public boolean depleteInput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            FluidStack tLiquid = tHatch.getFluid();
            if (tLiquid != null && tLiquid.isFluidEqual(aLiquid)) {
                tLiquid = tHatch.drain(aLiquid.amount, false);
                if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                    tLiquid = tHatch.drain(aLiquid.amount, true);
                    return tLiquid != null && tLiquid.amount >= aLiquid.amount;
                }
            }
        }
        return false;
    }

    @Override
    public boolean depleteInput(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GTUtility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            if (GTUtility.areStacksEqual(
                aStack,
                tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(0))) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(0).stackSize >= aStack.stackSize) {
                    tHatch.getBaseMetaTileEntity()
                        .decrStackSize(0, aStack.stackSize);
                    return true;
                }
            }
        }
        for (MTEHatchSteamBusInput tHatch : validMTEList(mSteamInputs)) {
            for (int i = tHatch.getBaseMetaTileEntity()
                .getSizeInventory() - 1; i >= 0; i--) {
                if (GTUtility.areStacksEqual(
                    aStack,
                    tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i))) {
                    if (tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(0).stackSize >= aStack.stackSize) {
                        tHatch.getBaseMetaTileEntity()
                            .decrStackSize(0, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            if (tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        for (MTEHatchInput hatch : this.mInputHatches) {
            if (hatch.getFillableStack() != null) {
                byte hatchColor = hatch.getBaseMetaTileEntity()
                    .getColorization();
                if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
                rList.add(hatch.getFillableStack());
            }
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchSteamBusInput tHatch : validMTEList(mSteamInputs)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
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
    public List<IOutputBus> getOutputBusses() {
        List<IOutputBus> output = new ArrayList<>(mSteamOutputs.size());
        for (MTEHatchSteamBusOutput outputBus : mSteamOutputs) {
            if (outputBus.isValid()) output.add(outputBus);
        }
        return output;
    }

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) tHatch.updateSlots();
        for (MTEHatchSteamBusInput tHatch : validMTEList(mSteamInputs)) tHatch.updateSlots();
    }

    /*
     * With batch mode enabled (true by default from config), HP steam multi processing times get rounded and look weird
     * Setting to false makes it look normal. Steam multis can't process every tick anyway
     */
    @Override
    public boolean getDefaultBatchMode() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mInputHatches.clear();
        mSteamInputFluids.clear();
        mSteamInputs.clear();
        mSteamOutputs.clear();
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        if (mSteamInputFluids.isEmpty()) {
            errors.add(StructureError.MISSING_STEAM_HATCH);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        if (errors.contains(StructureError.MISSING_STEAM_HATCH)) {
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.missing_hatch",
                    GregtechItemList.Hatch_Input_Steam.get(1)
                        .getDisplayName()));
        }
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTESteamMultiBlockBaseGui(this);
    }

    private int uiSteamStored = 0;
    private int uiSteamCapacity = 0;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getTotalSteamCapacity, val -> uiSteamCapacity = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getTotalSteamStored, val -> uiSteamStored = val));

        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.STEAM_GAUGE_BG_STEEL)
                .dynamicTooltip(
                    () -> Collections.singletonList(
                        translateToLocalFormatted(
                            MTEBasicMachine.STEAM_AMOUNT_LANGKEY,
                            numberFormat.format(uiSteamStored),
                            numberFormat.format(uiSteamCapacity))))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setUpdateTooltipEveryTick(true)
                .setSize(48, 42)
                .setPos(-48, -8));

        builder.widget(
            new DrawableWidget().setDrawable(new CircularGaugeDrawable(() -> (float) uiSteamStored / uiSteamCapacity))
                .setPos(-48 + 21, -8 + 21)
                .setSize(18, 4));
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
                        .translateToLocalFormatted("GTPP.waila.steam.use", formatNumber(actualEnergyUsage * 20)));
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
                    .translateToLocalFormatted("GT5U.waila.multiblock.status.cpu_load", formatNumber(tAverageTime)));
        }
        super.getMTEWailaBody(itemStack, currentTip, accessor, config);
    }

    protected static String getSteamTierTextForWaila(NBTTagCompound tag) {
        int tierMachine = tag.getInteger("tierMachine");
        if (tierMachine == 1) return "Basic";
        if (tierMachine == 2) return "High Pressure";
        return String.valueOf(tierMachine);
    }

    protected static <T extends MTESteamMultiBlockBase<T>> HatchElementBuilder<T> buildSteamInput(Class<T> typeToken) {
        return buildHatchAdder(typeToken).adder(MTESteamMultiBlockBase::addToMachineList)
            .hatchIds(31040)
            .shouldReject(t -> !t.mSteamInputFluids.isEmpty());
    }

    protected static OverclockDescriber createOverclockDescriber() {
        return new SteamOverclockDescriber(SteamVariant.BRONZE, 1, 2);
    }

    @Override
    public @Nullable OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    protected enum SteamHatchElement implements IHatchElement<MTESteamMultiBlockBase<?>> {

        InputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(MTEHatchSteamBusInput.class);
            }

            @Override
            public long count(MTESteamMultiBlockBase<?> t) {
                return t.mSteamInputs.size();
            }
        },
        OutputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(MTEHatchSteamBusOutput.class);
            }

            @Override
            public long count(MTESteamMultiBlockBase<?> t) {
                return t.mSteamOutputs.size();
            }
        };

        @Override
        public IGTHatchAdder<? super MTESteamMultiBlockBase<?>> adder() {
            return MTESteamMultiBlockBase::addToMachineList;
        }
    }
}
