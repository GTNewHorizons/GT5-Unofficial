package gregtech.api.ec.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.IComponent;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.ec.component.IActivitySoundLoopComponent;
import gregtech.api.ec.component.IMachineModeComponent;
import gregtech.api.ec.component.IPollutionLogicComponent;
import gregtech.api.ec.component.IProcessingLogicRecipeComponent;
import gregtech.api.ec.component.IRecipeCheckHandlerComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IRecipePollingComponent;
import gregtech.api.ec.component.multiblock.CustomHatchKey;
import gregtech.api.ec.component.multiblock.IBatchConfigComponent;
import gregtech.api.ec.component.multiblock.ICoilLogicComponent;
import gregtech.api.ec.component.multiblock.IDualInputLogicComponent;
import gregtech.api.ec.component.multiblock.IInputSeparationComponent;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IParallelLogicComponent;
import gregtech.api.ec.component.multiblock.IPowerSetupComponent;
import gregtech.api.ec.component.multiblock.IRecipeEfficiencyLogicComponent;
import gregtech.api.ec.component.multiblock.IRecipeLockingComponent;
import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.enums.SoundResource;
import gregtech.api.logic.ProcessingLogic;
import gregtech.common.ec.multiblock.ActivitySoundLoopComponent;
import gregtech.common.ec.multiblock.BatchConfigComponent;
import gregtech.common.ec.multiblock.CoilLogicComponent;
import gregtech.common.ec.multiblock.ColorRecipeCheckComponent;
import gregtech.common.ec.multiblock.ControllerToolWearComponent;
import gregtech.common.ec.multiblock.CustomHatchContainerComponent;
import gregtech.common.ec.multiblock.DroneDownLinkLogicComponent;
import gregtech.common.ec.multiblock.DualInputLogicComponent;
import gregtech.common.ec.multiblock.EnergyConsumeComponent;
import gregtech.common.ec.multiblock.EnergyInputLogicComponent;
import gregtech.common.ec.multiblock.EnergyOutputLogicComponent;
import gregtech.common.ec.multiblock.FluidInputLogicComponent;
import gregtech.common.ec.multiblock.FluidOutputLogicComponent;
import gregtech.common.ec.multiblock.InputSeparationComponent;
import gregtech.common.ec.multiblock.ItemInputLogicComponent;
import gregtech.common.ec.multiblock.ItemOutputLogicComponent;
import gregtech.common.ec.multiblock.MachineModeComponent;
import gregtech.common.ec.multiblock.MaintenanceLogicComponent;
import gregtech.common.ec.multiblock.ParallelLogicComponent;
import gregtech.common.ec.multiblock.PollutionEmitterComponent;
import gregtech.common.ec.multiblock.PollutionLogicComponent;
import gregtech.common.ec.multiblock.PowerSetupComponent;
import gregtech.common.ec.multiblock.RecipeEfficiencyLogicComponent;
import gregtech.common.ec.multiblock.RecipeLockingComponent;
import gregtech.common.ec.multiblock.RecipeLogicComponent;
import gregtech.common.ec.multiblock.RecipePollingComponent;
import gregtech.common.ec.multiblock.RecipeSoundComponent;
import gregtech.common.ec.multiblock.VoidingConfigComponent;

public class MultiblockComponentBuilder {

    private final ComponentSlot<IDualInputLogicComponent, DualInputLogicComponent> dualInputSlot = new ComponentSlot<>(
        true,
        DualInputLogicComponent::new);
    private final ComponentSlot<IMultiblockItemInputLogicComponent, ItemInputLogicComponent> itemInputSlot = new ComponentSlot<>(
        true,
        ItemInputLogicComponent::new);
    private final ComponentSlot<IMultiblockFluidInputLogicComponent, FluidInputLogicComponent> fluidInputSlot = new ComponentSlot<>(
        true,
        FluidInputLogicComponent::new);
    private final ComponentSlot<IMultiblockItemOutputLogicComponent, ItemOutputLogicComponent> itemOutputSlot = new ComponentSlot<>(
        true,
        ItemOutputLogicComponent::new);
    private final ComponentSlot<IMultiblockFluidOutputLogicComponent, FluidOutputLogicComponent> fluidOutputSlot = new ComponentSlot<>(
        true,
        FluidOutputLogicComponent::new);

    private final ComponentSlot<IMultiblockEnergyInputLogicComponent, EnergyInputLogicComponent> energyInputSlot = new ComponentSlot<>(
        true,
        EnergyInputLogicComponent::new);
    private final ComponentSlot<IMultiblockEnergyOutputLogicComponent, EnergyOutputLogicComponent> energyOutputSlot = new ComponentSlot<>(
        false,
        EnergyOutputLogicComponent::new);
    private final ComponentSlot<IComponent, EnergyConsumeComponent> energyConsumeSlot = new ComponentSlot<>(
        true,
        EnergyConsumeComponent::new);
    private final ComponentSlot<IPowerSetupComponent, PowerSetupComponent> powerSetupSlot = new ComponentSlot<>(
        true,
        PowerSetupComponent::new);

    private final ComponentSlot<IPollutionLogicComponent, PollutionLogicComponent> pollutionLogicSlot = new ComponentSlot<>(
        false,
        PollutionLogicComponent::new);
    private final ComponentSlot<IComponent, PollutionEmitterComponent> pollutionEmitterSlot = new ComponentSlot<>();

    private final ComponentSlot<IRecipeLogicComponent, RecipeLogicComponent> recipeLogicSlot = new ComponentSlot<>(
        true,
        ctx -> new RecipeLogicComponent(
            ctx.processingLogicSupplier()
                .get(),
            ctx.recipeMapProvider()));
    private final ComponentSlot<IRecipeEfficiencyLogicComponent, RecipeEfficiencyLogicComponent> efficiencySlot = new ComponentSlot<>(
        true,
        RecipeEfficiencyLogicComponent::new);
    private final ComponentSlot<IRecipeCheckHandlerComponent, ColorRecipeCheckComponent> recipeCheckSlot = new ComponentSlot<>(
        true,
        ColorRecipeCheckComponent::new);
    private final ComponentSlot<IBatchConfigComponent, BatchConfigComponent> batchModeSlot = new ComponentSlot<>(
        false,
        BatchConfigComponent::new);
    private final ComponentSlot<IInputSeparationComponent, InputSeparationComponent> inputSeparationSlot = new ComponentSlot<>(
        false,
        InputSeparationComponent::new);
    private final ComponentSlot<IVoidingConfigComponent, VoidingConfigComponent> voidProtectionSlot = new ComponentSlot<>(
        false,
        VoidingConfigComponent::new);
    private final ComponentSlot<IRecipeLockingComponent, RecipeLockingComponent> recipeLockingSlot = new ComponentSlot<>(
        false,
        RecipeLockingComponent::new);
    private final ComponentSlot<ICoilLogicComponent, CoilLogicComponent> coilSlot = new ComponentSlot<>(
        false,
        CoilLogicComponent::new);
    private final ComponentSlot<IRecipePollingComponent, RecipePollingComponent> recipePollingSlot = new ComponentSlot<>(
        true,
        RecipePollingComponent::new);

    private final ComponentSlot<IComponent, ControllerToolWearComponent> toolWearSlot = new ComponentSlot<>();
    private final ComponentSlot<IActivitySoundLoopComponent, ActivitySoundLoopComponent> activitySoundLoopSlot = new ComponentSlot<>();
    private final ComponentSlot<IParallelLogicComponent, ParallelLogicComponent> parallelLogicSlot = new ComponentSlot<>();
    private final ComponentSlot<IComponent, RecipeSoundComponent> recipeSoundSlot = new ComponentSlot<>();
    private final ComponentSlot<IMachineModeComponent, MachineModeComponent> machineModeSlot = new ComponentSlot<>();
    private final ComponentSlot<IMaintenanceLogicComponent, MaintenanceLogicComponent> maintenanceSlot = new ComponentSlot<>(
        true,
        () -> new MaintenanceLogicComponent());
    private final ComponentSlot<IComponent, DroneDownLinkLogicComponent> droneDownLinkSlot = new ComponentSlot<>(
        true,
        DroneDownLinkLogicComponent::new);
    private final Set<CustomHatchKey<?>> customHatchKeys = new HashSet<>();

    public MultiblockComponentBuilder apply(@NotNull Consumer<MultiblockComponentBuilder> preset) {
        preset.accept(this);
        return this;
    }

    public MultiblockComponentBuilder enableStandardQoL() {
        batchModeSlot.enable();
        voidProtectionSlot.enable();
        inputSeparationSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder noFluidIO() {
        return disableFluidInput().disableFluidOutput();
    }

    public MultiblockComponentBuilder noItemIO() {
        return disableItemInput().disableItemOutput();
    }

    public MultiblockComponentBuilder noInputs() {
        return disableDualInput().disableItemInput()
            .disableFluidInput();
    }

    public MultiblockComponentBuilder noOutputs() {
        return disableItemOutput().disableFluidOutput();
    }

    public MultiblockComponentBuilder noIO() {
        return noInputs().noOutputs()
            .disableRecipePolling();
    }

    public MultiblockComponentBuilder enableDualInput() {
        dualInputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableDualInput() {
        dualInputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setDualInput(@Nullable IDualInputLogicComponent dualInput) {
        dualInputSlot.set(dualInput);
        return this;
    }

    public MultiblockComponentBuilder configureDualInput(@NotNull Consumer<DualInputLogicComponent> consumer) {
        dualInputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableItemInput() {
        itemInputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableItemInput() {
        itemInputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setItemInput(@Nullable IMultiblockItemInputLogicComponent itemInput) {
        itemInputSlot.set(itemInput);
        return this;
    }

    public MultiblockComponentBuilder configureItemInput(@NotNull Consumer<ItemInputLogicComponent> consumer) {
        itemInputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableFluidInput() {
        fluidInputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableFluidInput() {
        fluidInputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setFluidInput(@Nullable IMultiblockFluidInputLogicComponent fluidInput) {
        fluidInputSlot.set(fluidInput);
        return this;
    }

    public MultiblockComponentBuilder configureFluidInput(@NotNull Consumer<FluidInputLogicComponent> consumer) {
        fluidInputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableItemOutput() {
        itemOutputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableItemOutput() {
        itemOutputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setItemOutput(@Nullable IMultiblockItemOutputLogicComponent itemOutput) {
        itemOutputSlot.set(itemOutput);
        return this;
    }

    public MultiblockComponentBuilder configureItemOutput(@NotNull Consumer<ItemOutputLogicComponent> consumer) {
        itemOutputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableFluidOutput() {
        fluidOutputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableFluidOutput() {
        fluidOutputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setFluidOutput(@Nullable IMultiblockFluidOutputLogicComponent fluidOutput) {
        fluidOutputSlot.set(fluidOutput);
        return this;
    }

    public MultiblockComponentBuilder configureFluidOutput(@NotNull Consumer<FluidOutputLogicComponent> consumer) {
        fluidOutputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableEnergyInput() {
        energyInputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableEnergyInput() {
        energyInputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setEnergyInput(@Nullable IMultiblockEnergyInputLogicComponent energyInput) {
        energyInputSlot.set(energyInput);
        return this;
    }

    public MultiblockComponentBuilder configureEnergyInput(@NotNull Consumer<EnergyInputLogicComponent> consumer) {
        energyInputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableEnergyOutput() {
        energyOutputSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableEnergyOutput() {
        energyOutputSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setEnergyOutput(@Nullable IMultiblockEnergyOutputLogicComponent energyOutput) {
        energyOutputSlot.set(energyOutput);
        return this;
    }

    public MultiblockComponentBuilder configureEnergyOutput(@NotNull Consumer<EnergyOutputLogicComponent> consumer) {
        energyOutputSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder setPollutionLogic(@Nullable IPollutionLogicComponent pollutionLogic) {
        pollutionLogicSlot.set(pollutionLogic);
        return this;
    }

    public MultiblockComponentBuilder configurePollutionLogic(@NotNull Consumer<PollutionLogicComponent> consumer) {
        pollutionLogicSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enablePowerSetup() {
        powerSetupSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disablePowerSetup() {
        powerSetupSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setPowerSetup(@Nullable IPowerSetupComponent powerSetup) {
        powerSetupSlot.set(powerSetup);
        return this;
    }

    public MultiblockComponentBuilder configurePower(@NotNull Consumer<PowerSetupComponent> consumer) {
        powerSetupSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableExtendedPower() {
        return configurePower(comp -> comp.setExtendedPower(true));
    }

    public MultiblockComponentBuilder disableExtendedPower() {
        return configurePower(comp -> comp.setExtendedPower(false));
    }

    public MultiblockComponentBuilder enableEfficiency() {
        efficiencySlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableEfficiency() {
        efficiencySlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setEfficiency(@Nullable IRecipeEfficiencyLogicComponent efficiency) {
        efficiencySlot.set(efficiency);
        return this;
    }

    public MultiblockComponentBuilder configureEfficiency(@NotNull Consumer<RecipeEfficiencyLogicComponent> consumer) {
        efficiencySlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableRecipeCheck() {
        recipeCheckSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableRecipeCheck() {
        recipeCheckSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setRecipeCheck(@Nullable IRecipeCheckHandlerComponent recipeCheck) {
        recipeCheckSlot.set(recipeCheck);
        return this;
    }

    public MultiblockComponentBuilder configureRecipeCheck(@NotNull Consumer<ColorRecipeCheckComponent> consumer) {
        recipeCheckSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableBatchMode() {
        batchModeSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableBatchMode() {
        batchModeSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setBatchMode(@Nullable IBatchConfigComponent batchConfig) {
        batchModeSlot.set(batchConfig);
        return this;
    }

    public MultiblockComponentBuilder configureBatchMode(@NotNull Consumer<BatchConfigComponent> consumer) {
        batchModeSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableInputSeparation() {
        inputSeparationSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableInputSeparation() {
        inputSeparationSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setInputSeparation(@Nullable IInputSeparationComponent inputSeparation) {
        inputSeparationSlot.set(inputSeparation);
        return this;
    }

    public MultiblockComponentBuilder configureInputSeparation(@NotNull Consumer<InputSeparationComponent> consumer) {
        inputSeparationSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableVoidProtection() {
        voidProtectionSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableVoidProtection() {
        voidProtectionSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setVoidProtection(@Nullable IVoidingConfigComponent voidProtection) {
        voidProtectionSlot.set(voidProtection);
        return this;
    }

    public MultiblockComponentBuilder configureVoidProtection(@NotNull Consumer<VoidingConfigComponent> consumer) {
        voidProtectionSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableRecipeLocking() {
        recipeLockingSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableRecipeLocking() {
        recipeLockingSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setRecipeLocking(@Nullable IRecipeLockingComponent recipeLocking) {
        recipeLockingSlot.set(recipeLocking);
        return this;
    }

    public MultiblockComponentBuilder configureRecipeLocking(@NotNull Consumer<RecipeLockingComponent> consumer) {
        recipeLockingSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableCoils() {
        coilSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableCoils() {
        coilSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setCoil(@Nullable ICoilLogicComponent coilLogic) {
        coilSlot.set(coilLogic);
        return this;
    }

    public MultiblockComponentBuilder configureCoil(@NotNull Consumer<CoilLogicComponent> consumer) {
        coilSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableRecipePolling() {
        recipePollingSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableRecipePolling() {
        recipePollingSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setRecipePolling(@Nullable IRecipePollingComponent recipePolling) {
        recipePollingSlot.set(recipePolling);
        return this;
    }

    public MultiblockComponentBuilder configureRecipePolling(@NotNull Consumer<RecipePollingComponent> consumer) {
        recipePollingSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder configureRecipe(@NotNull Consumer<RecipeLogicComponent> consumer) {
        recipeLogicSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder setRecipeLogic(@NotNull IRecipeLogicComponent customRecipeLogic) {
        recipeLogicSlot.set(customRecipeLogic);
        return this;
    }

    public MultiblockComponentBuilder enableRecipeLogic() {
        recipeLogicSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableRecipeLogic() {
        recipeLogicSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder enableEnergyConsume() {
        energyConsumeSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableEnergyConsume() {
        energyConsumeSlot.disable();
        return this;
    }

    private final @NotNull List<IComponent> extraComponents = new ArrayList<>();

    public MultiblockComponentBuilder enableMaintenance() {
        this.maintenanceSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableMaintenance() {
        this.maintenanceSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setMaintenance(@Nullable IMaintenanceLogicComponent maintenance) {
        this.maintenanceSlot.set(maintenance);
        return this;
    }

    public MultiblockComponentBuilder configureMaintenance(@NotNull Consumer<MaintenanceLogicComponent> consumer) {
        this.maintenanceSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder setMaintenanceIssues(int allowMasks) {
        if (allowMasks == 0) {
            return disableMaintenance();
        }
        return configureMaintenance(comp -> comp.setAllowMasks(allowMasks));
    }

    public MultiblockComponentBuilder enableDroneDownLink() {
        this.droneDownLinkSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disableDroneDownLink() {
        this.droneDownLinkSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder enablePollution(int pollutionPerSecond) {
        return enablePollution(_ -> pollutionPerSecond);
    }

    public MultiblockComponentBuilder enablePollution(
        @NotNull PollutionEmitterComponent.IPollutionCalculator pollutionCalculator) {
        this.pollutionEmitterSlot.enable(() -> new PollutionEmitterComponent(pollutionCalculator));
        this.pollutionLogicSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder disablePollution() {
        this.pollutionEmitterSlot.disable();
        this.pollutionLogicSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder enablePollutionLogicOnly() {
        this.pollutionEmitterSlot.disable();
        this.pollutionLogicSlot.enable();
        return this;
    }

    public MultiblockComponentBuilder enableMachineMode(@NotNull String... modeKeys) {
        machineModeSlot.enable(() -> new MachineModeComponent(modeKeys));
        return this;
    }

    public MultiblockComponentBuilder enableMachineMode(int modeCount, @NotNull IntFunction<String> modeKeyProvider,
        @NotNull IntUnaryOperator nextModeOperator, @NotNull IntUnaryOperator prevModeOperator) {
        machineModeSlot
            .enable(() -> new MachineModeComponent(modeCount, modeKeyProvider, nextModeOperator, prevModeOperator));
        return this;
    }

    public MultiblockComponentBuilder disableMachineMode() {
        machineModeSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder configureMachineMode(@NotNull Consumer<MachineModeComponent> consumer) {
        machineModeSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder enableToolWear(@NotNull ControllerToolWearComponent.IToolValidator toolValidator,
        @NotNull ControllerToolWearComponent.IToolDamageCalculator toolDamageCalculator) {
        toolWearSlot.enable(() -> new ControllerToolWearComponent(toolValidator, toolDamageCalculator));
        return this;
    }

    public MultiblockComponentBuilder disableToolWear() {
        toolWearSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder configureToolWear(@NotNull Consumer<ControllerToolWearComponent> consumer) {
        toolWearSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder setActivitySound(@NotNull SoundResource sound) {
        activitySoundLoopSlot.enable(() -> new ActivitySoundLoopComponent(sound));
        return this;
    }

    public MultiblockComponentBuilder setActivitySound(@NotNull IActivitySoundLoopComponent comp) {
        activitySoundLoopSlot.set(comp);
        return this;
    }

    public MultiblockComponentBuilder configureActivitySound(@NotNull Consumer<ActivitySoundLoopComponent> consumer) {
        activitySoundLoopSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder disableActivitySound() {
        activitySoundLoopSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder enableParallel(int maxParallel) {
        return enableParallel(() -> maxParallel);
    }

    public MultiblockComponentBuilder enableParallel(
        @NotNull ParallelLogicComponent.IMaxParallelProvider maxParallelProvider) {
        parallelLogicSlot.enable(() -> new ParallelLogicComponent(maxParallelProvider));
        return this;
    }

    public MultiblockComponentBuilder setParallel(@NotNull IParallelLogicComponent comp) {
        parallelLogicSlot.set(comp);
        return this;
    }

    public MultiblockComponentBuilder configureParallel(@NotNull Consumer<ParallelLogicComponent> consumer) {
        parallelLogicSlot.configure(consumer);
        return this;
    }

    public MultiblockComponentBuilder disableParallel() {
        parallelLogicSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder setRecipeSound(@NotNull SoundResource sound, int interval) {
        recipeSoundSlot.enable(() -> new RecipeSoundComponent(sound, interval));
        return this;
    }

    public MultiblockComponentBuilder disableRecipeSound() {
        recipeSoundSlot.disable();
        return this;
    }

    public MultiblockComponentBuilder enableHatchSlot(@NotNull CustomHatchKey<?>... keys) {
        Collections.addAll(customHatchKeys, keys);
        return this;
    }

    public MultiblockComponentBuilder disableHatchSlot(@NotNull CustomHatchKey<?> key) {
        customHatchKeys.remove(key);
        return this;
    }

    public MultiblockComponentBuilder addComponent(@NotNull IComponent component) {
        this.extraComponents.add(component);
        return this;
    }

    public void build(@NotNull IECMultiblock multiblock, @NotNull Supplier<@NotNull ProcessingLogic> logicSupplier,
        @NotNull RecipeLogicComponent.IRecipeMapProvider recipeMapProvider) {
        BuildContext ctx = new BuildContext(logicSupplier, recipeMapProvider);
        dualInputSlot.registerTo(multiblock, ctx);
        itemInputSlot.registerTo(multiblock, ctx);
        fluidInputSlot.registerTo(multiblock, ctx);
        itemOutputSlot.registerTo(multiblock, ctx);
        fluidOutputSlot.registerTo(multiblock, ctx);
        energyInputSlot.registerTo(multiblock, ctx);
        energyOutputSlot.registerTo(multiblock, ctx);
        pollutionLogicSlot.registerTo(multiblock, ctx);
        efficiencySlot.registerTo(multiblock, ctx);
        maintenanceSlot.registerTo(multiblock, ctx);
        pollutionEmitterSlot.registerTo(multiblock, ctx);
        toolWearSlot.registerTo(multiblock, ctx);
        parallelLogicSlot.registerTo(multiblock, ctx);
        batchModeSlot.registerTo(multiblock, ctx);
        inputSeparationSlot.registerTo(multiblock, ctx);
        recipeLockingSlot.registerTo(multiblock, ctx);
        voidProtectionSlot.registerTo(multiblock, ctx);
        coilSlot.registerTo(multiblock, ctx);
        recipeSoundSlot.registerTo(multiblock, ctx);
        activitySoundLoopSlot.registerTo(multiblock, ctx);
        machineModeSlot.registerTo(multiblock, ctx);
        droneDownLinkSlot.registerToIf(maintenanceSlot.isEnabled(), multiblock, ctx);
        energyConsumeSlot.registerToIf(energyInputSlot.isEnabled() || energyOutputSlot.isEnabled(), multiblock, ctx);
        if (recipeLogicSlot.isEnabled()) {
            recipePollingSlot.registerTo(multiblock, ctx);
            var recipeLogic = recipeLogicSlot.registerTo(multiblock, ctx);
            boolean usesProcessingLogic = recipeLogic instanceof IProcessingLogicRecipeComponent;
            powerSetupSlot.registerToIf(usesProcessingLogic, multiblock, ctx);
            recipeCheckSlot.registerToIf(usesProcessingLogic, multiblock, ctx);
        }
        if (!customHatchKeys.isEmpty()) {
            multiblock.addComponent(new CustomHatchContainerComponent(customHatchKeys));
        }
        for (var comp : extraComponents) {
            multiblock.addComponent(comp);
        }
    }

    protected record BuildContext(@NotNull Supplier<ProcessingLogic> processingLogicSupplier,
        @NotNull RecipeLogicComponent.IRecipeMapProvider recipeMapProvider) {}

    protected static class ComponentSlot<T extends IComponent, D extends T> {

        private boolean enabled;
        private final boolean needArgs;
        private @Nullable Function<BuildContext, ? extends D> factory = null;
        private @Nullable T customInstance = null;
        private @Nullable Consumer<D> configurator = null;

        public ComponentSlot() {
            enabled = false;
            needArgs = true;
            factory = null;
        }

        public ComponentSlot(boolean defaultEnabled, @NotNull Supplier<? extends D> defaultFactory) {
            this(defaultEnabled, _ -> defaultFactory.get());
        }

        public ComponentSlot(boolean defaultEnabled, @NotNull Function<BuildContext, ? extends D> defaultFactory) {
            this.enabled = defaultEnabled;
            this.factory = defaultFactory;
            this.needArgs = false;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void enable() {
            enabled = true;
        }

        public void enable(@NotNull Supplier<? extends D> factory) {
            enable(_ -> factory.get());
        }

        public void enable(@NotNull Function<BuildContext, ? extends D> factory) {
            this.customInstance = null;
            this.factory = factory;
            this.enabled = true;
        }

        public void disable() {
            enabled = false;
            configurator = null;
            customInstance = null;
            if (needArgs) {
                factory = null;
            }
        }

        public void set(@Nullable T instance) {
            if (instance == null) {
                disable();
                return;
            }
            customInstance = instance;
            configurator = null;
            enabled = true;
        }

        public void configure(@NotNull Consumer<D> consumer) {
            if (customInstance != null) {
                throw new IllegalStateException(
                    "Cannot configure default component after a custom instance was set: " + customInstance.getClass()
                        .getSimpleName());
            }
            if (factory == null) {
                throw new IllegalStateException("Cannot configure component without a factory");
            }
            enable();
            configurator = configurator == null ? consumer : configurator.andThen(consumer);
        }

        public @Nullable T registerTo(@NotNull IECMultiblock multiblock, @NotNull BuildContext ctx) {
            if (!enabled) return null;
            if (customInstance != null) {
                multiblock.addComponent(customInstance);
                return customInstance;
            }
            if (factory == null) return null;
            D comp = factory.apply(ctx);
            if (comp != null) {
                if (configurator != null) {
                    configurator.accept(comp);
                }
                multiblock.addComponent(comp);
                return comp;
            }
            return null;
        }

        public @Nullable T registerToIf(boolean condition, @NotNull IECMultiblock multiblock,
            @NotNull BuildContext ctx) {
            return condition ? registerTo(multiblock, ctx) : null;
        }
    }
}
