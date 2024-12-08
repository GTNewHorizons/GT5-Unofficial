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
                return machineHost.isAllowedToWork();
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
        System.arraycopy(oldOutputItems, 0, outputItems, 0, oldOutputItems.length);
        System.arraycopy(oldOutputFluids, 0, outputFluids, 0, oldOutputFluids.length);
        System.arraycopy(oldCalculatedEutValues, 0, calculatedEutValues, 0, oldCalculatedEutValues.length);
        for (int i = 0; i < oldDurations[i]; i++) {
            durations[i] = oldDurations[i];
        }
        System.arraycopy(oldProgresses, 0, progresses, 0, oldProgresses.length);
    }
}
