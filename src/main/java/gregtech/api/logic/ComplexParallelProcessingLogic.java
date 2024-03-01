package gregtech.api.logic;

import java.util.stream.LongStream;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ComplexParallelProcessingLogic<P extends ComplexParallelProcessingLogic<P>>
    extends MuTEProcessingLogic<P> {

    protected int maxComplexParallels;
    protected ItemStack[][] outputItems;
    protected FluidStack[][] outputFluids;
    protected long[] calculatedEutValues;
    protected int[] durations;
    protected int[] progresses;

    public P setMaxComplexParallel(int maxComplexParallels) {
        this.maxComplexParallels = maxComplexParallels;
        reinitializeProcessingArrays();
        return getThis();
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
        for (int i = 0; i < maxComplexParallels; i++) {
            if (progresses[i] >= durations[i]) {
                // return machineHost.isAllowedToWork();
            }
        }
        return false;
    }

    @Override
    public long getCalculatedEut() {
        return LongStream.of(this.calculatedEutValues)
            .sum();
    }

    public int getDuration(int index) {
        return durations[index];
    }

    public int getProgress(int index) {
        return progresses[index];
    }

    @Override
    public void progress() {
        for (int i = 0; i < maxComplexParallels; i++) {
            if (progresses[i] == durations[i]) {
                progresses[i] = 0;
                durations[i] = 0;
                output(i);
                continue;
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

    protected void reinitializeProcessingArrays() {
        ItemStack[][] oldOutputItems = outputItems;
        FluidStack[][] oldOutputFluids = outputFluids;
        long[] oldCalculatedEutValues = calculatedEutValues;
        int[] oldDurations = durations;
        int[] oldProgresses = progresses;
        outputItems = new ItemStack[maxComplexParallels][];
        outputFluids = new FluidStack[maxComplexParallels][];
        calculatedEutValues = new long[maxComplexParallels];
        durations = new int[maxComplexParallels];
        progresses = new int[maxComplexParallels];
        for (int i = 0; i < oldOutputItems.length; i++) {
            outputItems[i] = oldOutputItems[i];
        }
        for (int i = 0; i < oldOutputFluids.length; i++) {
            outputFluids[i] = oldOutputFluids[i];
        }
        for (int i = 0; i < oldCalculatedEutValues.length; i++) {
            calculatedEutValues[i] = oldCalculatedEutValues[i];
        }
        for (int i = 0; i < oldDurations[i]; i++) {
            durations[i] = oldDurations[i];
        }
        for (int i = 0; i < oldProgresses.length; i++) {
            progresses[i] = oldProgresses[i];
        }
    }
}
