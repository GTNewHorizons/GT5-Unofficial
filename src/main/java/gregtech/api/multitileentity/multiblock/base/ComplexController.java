package gregtech.api.multitileentity.multiblock.base;

import java.util.stream.LongStream;

import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.logic.interfaces.PollutionLogicHost;

public abstract class ComplexController<T extends ComplexController<T>> extends PowerController<T> {

    protected ComplexParallelProcessingLogic processingLogic;
    protected int maxComplexParallels = 0;
    protected int currentComplexParallels = 0;
    protected long[] maxProgressTimes = new long[0];
    protected long[] progressTimes = new long[0];

    protected void setMaxComplexParallels(int parallel) {
        if (parallel != maxComplexParallels) {
            stopMachine(false);
        }
        maxComplexParallels = parallel;
        maxProgressTimes = new long[parallel];
        progressTimes = new long[parallel];
    }

    @Override
    protected void runMachine(long tick) {
        if (acceptsFuel() && isActive()) {
            if (!consumeFuel()) {
                stopMachine(true);
                return;
            }
        }

        if (hasThingsToDo()) {
            markDirty();
            runningTick(tick);
        }
        if ((tick % TICKS_BETWEEN_RECIPE_CHECKS == 0 || hasWorkJustBeenEnabled() || hasInventoryBeenModified())
            && maxComplexParallels != currentComplexParallels) {
            if (isAllowedToWork() && maxComplexParallels > currentComplexParallels) {
                wasEnabled = false;
                boolean started = false;
                for (int i = 0; i < maxComplexParallels; i++) {
                    if (checkRecipe(i)) {
                        currentComplexParallels++;
                        started = true;
                    }
                }
                if (started) {
                    setActive(true);
                    updateSlots();
                    markDirty();
                    issueClientUpdate();
                }
            }
        }
    }

    @Override
    protected void runningTick(long tick) {
        // consumeEnergy();
        boolean allStopped = true;
        for (int i = 0; i < maxComplexParallels; i++) {
            if (maxProgressTimes[i] > 0 && ++progressTimes[i] >= maxProgressTimes[i]) {
                progressTimes[i] = 0;
                maxProgressTimes[i] = 0;
                outputItems(i);
                outputFluids(i);
                if (isAllowedToWork()) {
                    if (checkRecipe(i)) {
                        allStopped = false;
                    } else {
                        currentComplexParallels--;
                    }
                }
                updateSlots();
            }
        }
        if (allStopped) {
            setActive(false);
            issueClientUpdate();
        }

        if (this instanceof PollutionLogicHost && tick % POLLUTION_TICK == 0) {
            doPollution();
        }
        emitEnergy();
    }

    protected boolean checkRecipe(int index) {
        ComplexParallelProcessingLogic processingLogic = getComplexProcessingLogic();
        if (processingLogic == null || index < 0 || index >= maxComplexParallels) {
            return false;
        }
        processingLogic.clear(index);
        boolean result = processingLogic.setInputItems(index, getInputItems())
            .setInputFluids(index, getInputFluids())
            .setTileEntity(this)
            .setVoidProtection(index, isVoidProtectionEnabled(index))
            // .setEut(index, getEutForComplexParallel(index))
            .setEut(index, 1000000000)
            .process(index);
        setDuration(index, processingLogic.getDuration(index));
        setEut(processingLogic.getTotalEU());
        return result;
    }

    protected void outputItems(int index) {
        ComplexParallelProcessingLogic processingLogic = getComplexProcessingLogic();
        if (processingLogic != null && index >= 0 && index < maxComplexParallels) {
            outputItems(processingLogic.getOutputItems(index));
        }
    }

    protected void outputFluids(int index) {
        ComplexParallelProcessingLogic processingLogic = getComplexProcessingLogic();
        if (processingLogic != null && index >= 0 && index < maxComplexParallels) {
            outputFluids(processingLogic.getOutputFluids(index));
        }
    }

    protected ComplexParallelProcessingLogic getComplexProcessingLogic() {
        return processingLogic;
    }

    @Override
    public boolean hasThingsToDo() {
        return LongStream.of(maxProgressTimes)
            .sum() > 0;
    }

    protected void setDuration(int index, long duration) {
        if (duration < 0) {
            duration = -duration;
        }
        if (index >= 0 && index < maxComplexParallels) {
            maxProgressTimes[index] = duration;
        }
    }

    protected boolean isVoidProtectionEnabled(int index) {
        return !voidExcess;
    }

    protected long getEutForComplexParallel(int index) {
        return eut / maxComplexParallels;
    }
}
