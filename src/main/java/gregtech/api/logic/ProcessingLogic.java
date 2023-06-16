package gregtech.api.logic;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.CheckRecipeResult;
import gregtech.api.enums.CheckRecipeResults;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingLogic {

    protected IVoidable controller;
    protected IHasWorldObjectAndCoords tileEntity;
    protected GT_Recipe_Map recipeMap;
    protected Supplier<GT_Recipe_Map> mapSupplier;
    protected GT_Recipe lastRecipe;
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected ItemStack[] currentOutputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected FluidStack[] currentOutputFluids;
    protected long calculatedEut;
    protected long duration;
    protected long availableVoltage;
    protected long availableAmperage;
    protected int overClockTimeReduction = 2;
    protected int overClockPowerIncrease = 4;
    protected boolean protectItems;
    protected boolean protectFluids;
    protected int parallels = 1;
    protected Supplier<Integer> parallelSupplier;
    protected int batchSize = 1;

    public ProcessingLogic() {}

    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return this;
    }

    public ProcessingLogic setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return this;
    }

    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return this;
    }

    public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return this;
    }

    public ProcessingLogic setOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return this;
    }

    public ProcessingLogic setOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return this;
    }

    public ProcessingLogic setCurrentOutputItems(ItemStack... currentOutputItems) {
        this.currentOutputItems = currentOutputItems;
        return this;
    }

    public ProcessingLogic setCurrentOutputFluids(FluidStack... currentOutputFluids) {
        this.currentOutputFluids = currentOutputFluids;
        return this;
    }

    public ProcessingLogic setParallels(int parallels) {
        this.parallels = parallels;
        return this;
    }

    public ProcessingLogic setParallelSupplier(Supplier<Integer> supplier) {
        this.parallelSupplier = supplier;
        return this;
    }

    public ProcessingLogic setBatchSize(int size) {
        this.batchSize = size;
        return this;
    }

    public ProcessingLogic setRecipeMap(GT_Recipe_Map recipeMap) {
        this.recipeMap = recipeMap;
        return this;
    }

    public ProcessingLogic setRecipeMapSupplier(Supplier<GT_Recipe_Map> supplier) {
        this.mapSupplier = supplier;
        return this;
    }

    public ProcessingLogic setController(IVoidable controller) {
        this.controller = controller;
        return this;
    }

    public ProcessingLogic setTileEntity(IHasWorldObjectAndCoords tileEntity) {
        this.tileEntity = tileEntity;
        return this;
    }

    public ProcessingLogic setMetaTEController(GT_MetaTileEntity_MultiBlockBase metaTEController) {
        return setController(metaTEController).setTileEntity(metaTEController.getBaseMetaTileEntity());
    }

    public ProcessingLogic setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public ProcessingLogic setCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return this;
    }

    public ProcessingLogic setAvailableVoltage(long voltage) {
        availableVoltage = voltage;
        return this;
    }

    public ProcessingLogic setAvailableAmperage(long amperage) {
        availableAmperage = amperage;
        return this;
    }

    public ProcessingLogic setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return this;
    }

    public ProcessingLogic setOverclock(int timeReduction, int powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return this;
    }

    public ProcessingLogic enablePerfectOverclock() {
        return this.setOverclock(4, 4);
    }

    /**
     * Clears calculated outputs, and provided machine inputs
     */
    public ProcessingLogic clear() {
        this.inputItems = null;
        this.inputFluids = null;
        this.outputItems = null;
        this.outputFluids = null;
        this.calculatedEut = 0;
        this.duration = 0;
        return this;
    }

    public CheckRecipeResult process() {
        if (recipeMap == null && mapSupplier == null) return CheckRecipeResults.NO_RECIPE;

        if (mapSupplier != null) {
            recipeMap = mapSupplier.get();
        }
        if (recipeMap == null) return CheckRecipeResults.NO_RECIPE;

        GT_Recipe recipe = recipeMap
            .findRecipe(tileEntity, lastRecipe, false, availableVoltage, inputFluids, inputItems);

        if (recipe != null) {
            CheckRecipeResult result = checkRecipe(recipe);
            if (!result.wasSuccessful()) {
                return result;
            } else {
                lastRecipe = recipe;
            }
        } else {
            return CheckRecipeResults.NO_RECIPE;
        }

        GT_ParallelHelper helper = createParallelHelper(recipe);

        if (helper == null || helper.getCurrentParallel() <= 0) return CheckRecipeResults.OUTPUT_FULL;

        GT_OverclockCalculator calculator = createOverclockCalculator(recipe, helper);

        if (calculator == null || calculator.getConsumption() == Long.MAX_VALUE - 1
            || calculator.getDuration() == Integer.MAX_VALUE - 1) {
            return CheckRecipeResults.NO_RECIPE;
        }

        calculatedEut = calculator.getConsumption();
        duration = calculator.getDuration();
        outputItems = helper.getItemOutputs();
        outputFluids = helper.getFluidOutputs();

        return CheckRecipeResults.SUCCESSFUL;
    }

    protected GT_ParallelHelper createParallelHelper(GT_Recipe recipe) {
        if (parallelSupplier != null) {
            parallels = parallelSupplier.get();
        }
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMachine(controller, protectItems, protectFluids)
            .setMaxParallel(parallels)
            .enableBatchMode(batchSize)
            .enableConsumption()
            .enableOutputCalculation()
            .build();
    }

    protected CheckRecipeResult checkRecipe(GT_Recipe recipe) {
        return CheckRecipeResults.SUCCESSFUL;
    }

    protected GT_OverclockCalculator createOverclockCalculator(GT_Recipe recipe, GT_ParallelHelper helper) {
        return new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setParallel(helper.getCurrentParallel())
            .setDuration(recipe.mDuration)
            .setAmperage(availableAmperage)
            .setEUt(availableVoltage)
            .setDurationDecreasePerOC(overClockTimeReduction)
            .setEUtIncreasePerOC(overClockPowerIncrease)
            .calculate();
    }

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public long getDuration() {
        return duration;
    }

    public long getCalculatedEut() {
        return calculatedEut;
    }
}
