package gregtech.api.logic;

import java.util.stream.LongStream;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

public class ComplexParallelProcessingLogic {

    protected Controller<?> tileEntity;
    protected GT_Recipe.GT_Recipe_Map recipeMap;
    protected boolean hasPerfectOverclock;
    protected final int maxComplexParallels;
    protected final ItemStack[][] outputItems;
    protected final ItemStack[][] inputItems;
    protected final FluidStack[][] inputFluids;
    protected final FluidStack[][] outputFluids;
    protected final long[] availableEut;
    protected final long[] eut;
    protected final long[] durations;
    protected final boolean[] isItemVoidProtected;
    protected final boolean[] isFluidVoidProtected;

    public ComplexParallelProcessingLogic(int maxComplexParallels) {
        this(null, maxComplexParallels);
    }

    public ComplexParallelProcessingLogic(GT_Recipe.GT_Recipe_Map recipeMap, int maxComplexParallels) {
        this.maxComplexParallels = maxComplexParallels;
        this.recipeMap = recipeMap;
        inputItems = new ItemStack[maxComplexParallels][];
        outputItems = new ItemStack[maxComplexParallels][];
        inputFluids = new FluidStack[maxComplexParallels][];
        outputFluids = new FluidStack[maxComplexParallels][];
        eut = new long[maxComplexParallels];
        availableEut = new long[maxComplexParallels];
        durations = new long[maxComplexParallels];
        isItemVoidProtected = new boolean[maxComplexParallels];
        isFluidVoidProtected = new boolean[maxComplexParallels];
    }

    public ComplexParallelProcessingLogic setRecipeMap(GT_Recipe.GT_Recipe_Map recipeMap) {
        this.recipeMap = recipeMap;
        return this;
    }

    public ComplexParallelProcessingLogic setInputItems(int index, ItemStack... itemInputs) {
        if (index >= 0 && index < maxComplexParallels) {
            inputItems[index] = itemInputs;
        }
        return this;
    }

    public ComplexParallelProcessingLogic setInputFluids(int index, FluidStack... inputFluids) {
        if (index >= 0 && index < maxComplexParallels) {
            this.inputFluids[index] = inputFluids;
        }
        return this;
    }

    public ComplexParallelProcessingLogic setTileEntity(Controller<?> tileEntity) {
        this.tileEntity = tileEntity;
        return this;
    }

    public ComplexParallelProcessingLogic setEut(int index, long eut) {
        if (index >= 0 && index < maxComplexParallels) {
            availableEut[index] = eut;
        }
        return this;
    }

    public ComplexParallelProcessingLogic setVoidProtection(int index, boolean protectItem, boolean protectFluid) {
        if (index >= 0 && index < maxComplexParallels) {
            isItemVoidProtected[index] = protectItem;
            isFluidVoidProtected[index] = protectFluid;
        }
        return this;
    }

    public ComplexParallelProcessingLogic setPerfectOverclock(boolean shouldOverclockPerfectly) {
        this.hasPerfectOverclock = shouldOverclockPerfectly;
        return this;
    }

    public ComplexParallelProcessingLogic clear() {
        for (int i = 0; i < maxComplexParallels; i++) {
            outputItems[i] = null;
            outputFluids[i] = null;
            durations[i] = 0;
            eut[i] = 0;
        }
        return this;
    }

    public ComplexParallelProcessingLogic clear(int index) {
        if (index >= 0 && index < maxComplexParallels) {
            inputItems[index] = null;
            inputFluids[index] = null;
            outputItems[index] = null;
            outputFluids[index] = null;
            durations[index] = 0;
            availableEut[index] = 0;
            eut[index] = 0;
        }
        return this;
    }

    public boolean process(int index) {
        if (recipeMap == null) {
            return false;
        }
        GT_Recipe recipe = recipeMap
            .findRecipe(tileEntity, false, false, availableEut[index], inputFluids[index], inputItems[index]);
        if (recipe == null) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems[index])
            .setFluidInputs(inputFluids[index])
            .setAvailableEUt(availableEut[index])
            .setController(tileEntity, isItemVoidProtected[index], isFluidVoidProtected[index])
            .enableConsumption()
            .enableOutputCalculation();

        helper.build();

        if (helper.getCurrentParallel() <= 0) {
            return false;
        }

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setDuration(recipe.mDuration)
            .setEUt(availableEut[index]);

        if (hasPerfectOverclock) {
            calculator.enablePerfectOC();
        }

        if (calculator.getConsumption() == Long.MAX_VALUE - 1 || calculator.getDuration() == Integer.MAX_VALUE - 1) {
            return false;
        }

        durations[index] = calculator.getDuration();
        eut[index] = calculator.getConsumption();
        outputItems[index] = helper.getItemOutputs();
        outputFluids[index] = helper.getFluidOutputs();

        return true;
    }

    public long getDuration(int index) {
        if (index >= 0 && index < maxComplexParallels) {
            return durations[index];
        }
        return 0;
    }

    public long getTotalEU() {
        return LongStream.of(eut)
            .sum();
    }

    public ItemStack[] getOutputItems(int index) {
        if (index >= 0 && index < maxComplexParallels) {
            return outputItems[index];
        }
        return null;
    }

    public FluidStack[] getOutputFluids(int index) {
        if (index >= 0 && index < maxComplexParallels) {
            return outputFluids[index];
        }
        return null;
    }
}
