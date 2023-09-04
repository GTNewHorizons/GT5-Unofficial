package gregtech.api.logic;

import java.util.stream.LongStream;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

public class ComplexParallelProcessingLogic<P extends ComplexParallelProcessingLogic<P>> extends ProcessingLogic<P> {

    protected GT_Recipe.GT_Recipe_Map recipeMap;
    protected boolean hasPerfectOverclock;
    protected int maxComplexParallels;
    protected ItemStack[][] outputItems;
    protected ItemStack[][] inputItems;
    protected FluidStack[][] inputFluids;
    protected FluidStack[][] outputFluids;
    protected long[] availableEut;
    protected long[] eut;
    protected int[] durations;
    protected boolean[] isItemVoidProtected;
    protected boolean[] isFluidVoidProtected;
    protected boolean isCleanroom;

    public ComplexParallelProcessingLogic() {
        this(null, 0);
    }

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
        durations = new int[maxComplexParallels];
        isItemVoidProtected = new boolean[maxComplexParallels];
        isFluidVoidProtected = new boolean[maxComplexParallels];
    }

    public boolean process(int index) {
        if (recipeMap == null) {
            return false;
        }
        GT_Recipe recipe = recipeMap
            .findRecipe(null, false, false, availableEut[index], inputFluids[index], inputItems[index]);
        if (recipe == null) {
            return false;
        }

        if (recipe.mSpecialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom && !isCleanroom) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems[index])
            .setFluidInputs(inputFluids[index])
            .setAvailableEUt(availableEut[index])
            .setMachine(null, isItemVoidProtected[index], isFluidVoidProtected[index])
            .setConsumption(true)
            .setOutputCalculation(true);

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

    public int getDuration(int index) {
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
