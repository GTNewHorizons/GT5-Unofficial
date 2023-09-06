package gregtech.api.logic;

import java.util.stream.LongStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.enums.InventoryType;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class ComplexParallelProcessingLogic<P extends ComplexParallelProcessingLogic<P>> extends ProcessingLogic<P> {

    protected boolean hasPerfectOverclock;
    protected int maxComplexParallels;
    protected ItemStack[][] outputItems;
    protected FluidStack[][] outputFluids;
    protected long[] calculatedEutValues;
    protected int[] durations;
    protected int[] progresses;
    protected boolean isCleanroom;

    @Override
    @Nonnull
    protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
        if (map == null) return FindRecipeResult.NOT_FOUND;
        return map.findRecipeWithResult(lastRecipe, availableVoltage, machineHost.getItemLogic(InventoryType.Input, null), machineHost.getFluidLogic(InventoryType.Input, null));
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

    @Override
    public boolean canWork() {
        boolean allBusy = true;
        for (int i = 0; i < maxComplexParallels; i++) {
            allBusy &= progresses[i] < durations[i];
        }
        return !allBusy;
    }

    @Override
    public P clear() {
        // TODO Auto-generated method stub
        return super.clear();
    }

    @Override
    public long getCalculatedEut() {
        return LongStream.of(this.calculatedEutValues)
            .sum();
    }

    @Override
    public void progress() {
        for (int i = 0; i < maxComplexParallels; i++) {
            if (progresses[i] == durations[i]) {
                progresses[i] = 0;
                durations[i] = 0;
                output(i);
            }
            progresses[i] = progresses[i] + 1;
        }
    }

    @Override
    public void startCheck() {
        for (int i = 0; i < maxComplexParallels; i++) {
            if (durations[i] > 0) continue;
            recipeResult = process();
            calculatedEutValues[i] = calculatedEut;
            durations[i] = duration;
            progresses[i] = 0;
            outputItems[i] = getOutputItems();
            outputFluids[i] = getOutputFluids();
        }
    }

    protected void output(int index) {
        setOutputItems(getOutputItems(index));
        setOutputFluids(getOutputFluids(index));
        output();
    }
}
