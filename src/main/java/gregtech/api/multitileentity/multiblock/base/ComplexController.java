package gregtech.api.multitileentity.multiblock.base;

import static mcp.mobius.waila.api.SpecialChars.*;

import java.util.List;
import java.util.stream.LongStream;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.api.enums.GT_Values;
import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.logic.interfaces.PollutionLogicHost;
import gregtech.api.util.GT_Waila;

public abstract class ComplexController<T extends ComplexController<T>> extends PowerController<T> {

    protected ComplexParallelProcessingLogic processingLogic;
    protected int maxComplexParallels = 0;
    protected int currentComplexParallels = 0;
    protected long[] maxProgressTimes = new long[0];
    protected long[] progressTimes = new long[0];

    public ComplexController() {
        isSimpleMachine = false;
    }

    protected void setMaxComplexParallels(int parallel) {
        if (parallel != maxComplexParallels && maxComplexParallels != 0) {
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
                    if (maxProgressTimes[i] <= 0 && checkRecipe(i)) {
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
        consumeEnergy();
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
            } else if (maxProgressTimes[i] > 0) {
                allStopped = false;
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
            .setEut(index, getEutForComplexParallel(index))
            .setPerfectOverclock(hasPerfectOverclock())
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

    @Override
    protected void stopMachine(boolean powerShutDown) {
        super.stopMachine(powerShutDown);
        for (int i = 0; i < maxComplexParallels; i++) {
            maxProgressTimes[i] = 0;
        }
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

    protected boolean hasPerfectOverclock() {
        return false;
    }

    protected long getEutForComplexParallel(int index) {
        // As default behavior we'll give the parallel all remaining EU we have
        return GT_Values.V[tier] - eut;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxComplexParallels", maxComplexParallels);
        for (int i = 0; i < maxComplexParallels; i++) {
            tag.setLong("maxProgress" + i, maxProgressTimes[i]);
            tag.setLong("progress" + i, progressTimes[i]);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        maxComplexParallels = tag.getInteger("maxComplexParallels");
        for (int i = 0; i < maxComplexParallels; i++) {
            long maxProgress = tag.getLong("maxProgress" + i);
            long progress = tag.getLong("progress" + i);
            currentTip.add(
                "Process " + (i + 1)
                    + ": "
                    + GT_Waila
                        .getMachineProgressString(maxProgress > 0 && maxProgress >= progress, maxProgress, progress));
        }
    }
}
