package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.api.util.GTUtility.validMTEList;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.CircularGaugeDrawable;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.CustomItemList;

public abstract class MTESteamMultiBase<T extends MTESteamMultiBase<T>> extends GTPPMultiBlockBase<T> {

    public ArrayList<MTEHatchSteamBusInput> mSteamInputs = new ArrayList<>();
    public ArrayList<MTEHatchSteamBusOutput> mSteamOutputs = new ArrayList<>();
    public ArrayList<MTEHatchCustomFluidBase> mSteamInputFluids = new ArrayList<>();

    protected static final String TT_steaminputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamInputBus");
    protected static final String TT_steamoutputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamOutputBus");
    protected static final String TT_steamhatch = StatCollector.translateToLocal("GTPP.MBTT.SteamHatch");
    protected static final String HIGH_PRESSURE_TOOLTIP_NOTICE = "Processing Speed & Steam Consumption is doubled under High Pressure";

    public MTESteamMultiBase(String aName) {
        super(aName);
    }

    public MTESteamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected boolean explodesImmediately() {
        return false;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    protected abstract ITexture getFrontOverlay();

    protected abstract ITexture getFrontOverlayActive();

    public abstract int getTierRecipes();

    private int getCasingTextureIndex() {
        return 10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    protected SteamTypes getSteamType() {
        return SteamTypes.STEAM;
    }

    public ArrayList<FluidStack> getAllSteamStacks() {
        ArrayList<FluidStack> aFluids = new ArrayList<>();

        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid.getFluid() == getSteamType().fluid) {
                aFluids.add(aFluid);
            }
        }
        return aFluids;
    }

    private int getTotalSteamStoredOfAnyType() {
        int aSteam = 0;
        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid == null) continue;
            for (SteamTypes type : SteamTypes.VALUES) {
                if (aFluid.getFluid() == type.fluid) {
                    aSteam += aFluid.amount;
                }
            }
        }
        return aSteam;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[getTierRecipes()]);
        // We need to trick the GT_ParallelHelper we have enough amps for all recipe parallels.
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
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
            return this.depleteInput(new FluidStack(getSteamType().fluid, aAmount));
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
            // Logger.INFO("Trying to drain "+aSteamVal+" steam per tick.");
            if (!tryConsumeSteam((int) aSteamVal)) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            log("Invalid IGregTechTileEntity");
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            log("Invalid IMetaTileEntity");
            return false;
        }

        // Use this to determine the correct value, then update the hatch texture after.
        boolean aDidAdd = false;

        if (aMetaTileEntity instanceof MTEHatchCustomFluidBase) {
            log("Adding Steam Input Hatch");
            aDidAdd = addToMachineListInternal(mSteamInputFluids, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchSteamBusInput) {
            log(
                "Trying to set recipe map. Type: "
                    + (getRecipeMap() != null ? getRecipeMap().unlocalizedName : "Null"));
            this.resetRecipeMapForHatch(aTileEntity, getRecipeMap());
            log("Adding Steam Input Bus");
            aDidAdd = addToMachineListInternal(mSteamInputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchSteamBusOutput) {
            log("Adding Steam Output Bus");
            aDidAdd = addToMachineListInternal(mSteamOutputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof MTEHatchInput)
            aDidAdd = addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof MTEHatchOutput);

        return aDidAdd;
    }

    /*
     * Handle I/O with custom hatches
     */

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
            tHatch.mRecipeMap = getRecipeMap();
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
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            if (tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        for (MTEHatchInput hatch : this.mInputHatches) if (hatch.getFillableStack() != null) {
            rList.add(hatch.getFillableStack());
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchSteamBusInput tHatch : validMTEList(mSteamInputs)) {
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
    public boolean addOutput(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        aStack = GTUtility.copy(aStack);
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (MTEHatchSteamBusOutput tHatch : validMTEList(mSteamOutputs)) {
                if (!outputSuccess) {
                    for (int i = tHatch.getSizeInventory() - 1; i >= 0 && !outputSuccess; i--) {
                        if (tHatch.getBaseMetaTileEntity()
                            .addStackToSlot(i, single)) outputSuccess = true;
                    }
                }
            }
            for (MTEHatchOutput tHatch : validMTEList(mOutputHatches)) {
                if (!outputSuccess && tHatch.outputsItems()) {
                    if (tHatch.getBaseMetaTileEntity()
                        .addStackToSlot(1, single)) outputSuccess = true;
                }
            }
        }
        return outputSuccess;
    }

    @Override
    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchSteamBusOutput tHatch : validMTEList(mSteamOutputs)) {
            for (int i = tHatch.getBaseMetaTileEntity()
                .getSizeInventory() - 1; i >= 0; i--) {
                rList.add(
                    tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i));
            }
        }
        return rList;
    }

    @Override
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        List<ItemStack> ret = new ArrayList<>();
        for (final MTEHatch tBus : validMTEList(mSteamOutputs)) {
            final IInventory tBusInv = tBus.getBaseMetaTileEntity();
            for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                ret.add(tBus.getStackInSlot(i));
            }
        }
        return ret;
    }

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) tHatch.updateSlots();
        for (MTEHatchSteamBusInput tHatch : validMTEList(mSteamInputs)) tHatch.updateSlots();
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
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
    public boolean resetRecipeMapForAllInputHatches(RecipeMap<?> aMap) {
        boolean ret = super.resetRecipeMapForAllInputHatches(aMap);
        for (MTEHatchSteamBusInput hatch : mSteamInputs) {
            if (resetRecipeMapForHatch(hatch, aMap)) {
                ret = true;
            }
        }
        for (MTEHatchInput g : this.mInputHatches) {
            if (resetRecipeMapForHatch(g, aMap)) {
                ret = true;
            }
        }

        return ret;
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
        if (GTMod.gregtechproxy.wailaAverageNS && tag.hasKey("averageNS")) {
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

    protected static <T extends MTESteamMultiBase<T>> HatchElementBuilder<T> buildSteamInput(Class<T> typeToken) {
        return buildHatchAdder(typeToken).adder(MTESteamMultiBase::addToMachineList)
            .hatchIds(31040, 15511)
            .shouldReject(t -> !t.mSteamInputFluids.isEmpty());
    }

    protected enum SteamHatchElement implements IHatchElement<MTESteamMultiBase<?>> {

        InputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(MTEHatchSteamBusInput.class);
            }

            @Override
            public long count(MTESteamMultiBase<?> t) {
                return t.mSteamInputs.size();
            }
        },
        OutputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(MTEHatchSteamBusOutput.class);
            }

            @Override
            public long count(MTESteamMultiBase<?> t) {
                return t.mSteamOutputs.size();
            }
        },;

        @Override
        public IGTHatchAdder<? super MTESteamMultiBase<?>> adder() {
            return MTESteamMultiBase::addToMachineList;
        }
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    private int uiSteamStored = 0;
    private int uiSteamCapacity = 0;
    private int uiSteamStoredOfAllTypes = 0;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getTotalSteamCapacity, val -> uiSteamCapacity = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getTotalSteamStored, val -> uiSteamStored = val));
        builder.widget(
            new FakeSyncWidget.IntegerSyncer(this::getTotalSteamStoredOfAnyType, val -> uiSteamStoredOfAllTypes = val));

        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.STEAM_GAUGE_BG)
                .dynamicTooltip(() -> {
                    List<String> ret = new ArrayList<>();
                    ret.add(getSteamType().name + ": " + uiSteamStored + "/" + uiSteamCapacity + "L");
                    if (uiSteamStored == 0 && uiSteamStoredOfAllTypes != 0) {
                        ret.add(EnumChatFormatting.RED + "Found steam of wrong type!");
                    }
                    return ret;
                })
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setUpdateTooltipEveryTick(true)
                .setSize(64, 42)
                .setPos(-64, 100));

        builder.widget(
            new DrawableWidget().setDrawable(new CircularGaugeDrawable(() -> (float) uiSteamStored / uiSteamCapacity))
                .setPos(-64 + 21, 100 + 21)
                .setSize(18, 4));

        builder.widget(
            new ItemDrawable(CustomItemList.Fools_FakeItemSiren.get(1)).asWidget()
                .setPos(-64 + 21 - 7, 100 - 20)
                .setEnabled(w -> uiSteamStored == 0 && uiSteamStoredOfAllTypes != 0));
    }

    protected enum SteamTypes {

        STEAM("Steam", FluidUtils.getSteam(1)
            .getFluid()),
        SH_STEAM("Superheated Steam", FluidUtils.getSuperHeatedSteam(1)
            .getFluid()),
        SC_STEAM("Supercritical Steam", FluidRegistry.getFluidStack("supercriticalsteam", 1)
            .getFluid());

        static final SteamTypes[] VALUES = values();

        final String name;
        final Fluid fluid;

        SteamTypes(String name, Fluid fluid) {
            this.name = name;
            this.fluid = fluid;
        }
    }
}
