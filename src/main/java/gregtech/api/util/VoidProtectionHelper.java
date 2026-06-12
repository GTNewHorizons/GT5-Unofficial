package gregtech.api.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

/**
 * Helper class to calculate how many parallels of items / fluids can fit in the output buses / hatches.
 */
public class VoidProtectionHelper {

    private static final long PSEUDO_INFINITE_FLUID_SPACE = Long.MAX_VALUE / 1024;

    /**
     * Machine used for calculation
     */
    private IVoidable machine;
    /**
     * Does void protection enabled for items
     */
    private boolean protectExcessItem;
    /**
     * Does void protection enabled for fluids
     */
    private boolean protectExcessFluid;
    /**
     * The maximum possible parallel possible for the multiblock
     */
    private int maxParallel = 1;
    /**
     * If item output is full.
     */
    private boolean isItemFull;
    /**
     * If fluid output is full.
     */
    private boolean isFluidFull;
    /**
     * The item outputs to check
     */
    private ItemStack[] itemOutputs;
    /**
     * The fluid outputs to check
     */
    private FluidStack[] fluidOutputs;
    /**
     * Has this helper been built?
     */
    private boolean built;
    /**
     * Multiplier that is applied on the output chances
     */
    private double outputChanceMultiplier = 1;

    private Int2IntFunction outputChanceGetter = i -> 10000;
    private Int2IntFunction fluidOutputChanceGetter = i -> 10000;

    public VoidProtectionHelper() {}

    /**
     * Sets machine, with current configuration for void protection mode.
     */
    public VoidProtectionHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    public VoidProtectionHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    public VoidProtectionHelper setItemOutputs(ItemStack[] itemOutputs) {
        this.itemOutputs = itemOutputs;
        return this;
    }

    public VoidProtectionHelper setFluidOutputs(FluidStack[] fluidOutputs) {
        this.fluidOutputs = fluidOutputs;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    public VoidProtectionHelper setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    public VoidProtectionHelper setOutputChanceMultiplier(double chanceMultiplier) {
        this.outputChanceMultiplier = chanceMultiplier;
        return this;
    }

    public VoidProtectionHelper setOutputChanceGetter(Int2IntFunction getter) {
        this.outputChanceGetter = getter;
        return this;
    }

    public VoidProtectionHelper setFluidOutputChanceGetter(Int2IntFunction getter) {
        this.fluidOutputChanceGetter = getter;
        return this;
    }

    /**
     * Finishes the VoidProtectionHelper. Anything changed after this will not affect anything
     */
    public VoidProtectionHelper build() {
        if (built) {
            throw new IllegalStateException("Tried to build twice");
        }
        if (machine == null) {
            throw new IllegalStateException("Machine is not set");
        }
        built = true;
        determineParallel();
        return this;
    }

    /**
     * @return The current parallels possible by the multiblock
     */
    public int getMaxParallel() {
        if (!built) {
            throw new IllegalStateException("Tried to get parallels before building");
        }
        return maxParallel;
    }

    /**
     * @return If the calculation resulted in item output being full.
     */
    public boolean isItemFull() {
        if (!built) {
            throw new IllegalStateException("Tried to get isItemFull before building");
        }
        return isItemFull;
    }

    /**
     * @return If the calculation resulted in fluid output being full.
     */
    public boolean isFluidFull() {
        if (!built) {
            throw new IllegalStateException("Tried to get isFluidFull before building");
        }
        return isFluidFull;
    }

    /**
     * Called by {@link #build()}. Determines the parallels and everything else that needs to be done at build time
     */
    private void determineParallel() {
        if (itemOutputs == null) {
            itemOutputs = GTValues.emptyItemStackArray;
        }
        if (fluidOutputs == null) {
            fluidOutputs = GTValues.emptyFluidStackArray;
        }

        // Don't check IVoidable#protectsExcessItem nor #protectsExcessFluid here,
        // to allow more involved setting for void protections (see ComplexParallelProcessingLogic)
        if (protectExcessItem && itemOutputs.length > 0) {
            List<GTUtility.ItemId> outputIds = GTDataUtils.mapToList(itemOutputs, GTUtility.ItemId::create);

            if (!machine.canDumpItemToME(outputIds)) {
                List<ItemStack> maxItemOutputs = new ArrayList<>(itemOutputs.length);

                for (int i = 0, itemOutputsLength = itemOutputs.length; i < itemOutputsLength; i++) {
                    ItemStack stack = itemOutputs[i];

                    if (stack == null || stack.stackSize == 0) continue;

                    // Find the max possible output for this stack (note the .ceil)
                    // We can't know how many items per parallel will be eventually ejected, so we just check the
                    // worst-case scenario
                    int stackSize = (int) (stack.stackSize
                        * Math.ceil(outputChanceMultiplier * outputChanceGetter.apply(i) / 10000d));

                    maxItemOutputs.add(GTUtility.copyAmount(stackSize, stack));
                }

                // Pass the VP helper's protectExcessItem flag to the ejection helper instead of using the machine's
                // flag
                ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(
                    machine.getOutputBusses(),
                    protectExcessItem);

                maxParallel = ejectionHelper.ejectItems(maxItemOutputs, maxParallel);

                if (maxParallel <= 0) {
                    isItemFull = true;
                    return;
                }
            }
        }

        if (protectExcessFluid && fluidOutputs.length > 0 && !machine.canDumpFluidToME()) {
            maxParallel = Math.min(calculateMaxFluidParallels(), maxParallel);
            if (maxParallel <= 0) {
                isFluidFull = true;
            }
        }
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private int calculateMaxFluidParallels() {
        List<? extends IFluidStore> hatches = machine.getFluidOutputSlots(fluidOutputs);
        int size = hatches.size();
        if (size < fluidOutputs.length) {
            boolean hasMe = false;
            for (IFluidStore hatch : hatches) {
                if (hatch instanceof MTEHatchOutputME) {
                    hasMe = true;
                    break;
                }
            }
            if (!hasMe) return 0;
        }

        Object2LongMap<FluidStack> tFluidOutputMap = new Object2LongOpenHashMap<>();

        // Tracks full crafts + partial mb per fluid output to avoid floating point inaccuracies.
        Map<FluidStack, FluidParallelData> tParallels = new HashMap<>();

        for (int i = 0; i < fluidOutputs.length; i++) {
            FluidStack aY = fluidOutputs[i];
            long fluidAmount = GTUtility.getFluidAmountLong(aY);
            if (aY == null || fluidAmount <= 0) {
                continue;
            }

            long estimatedAmount = (long) (fluidAmount * Math.ceil(fluidOutputChanceGetter.apply(i) / 10000d));

            tFluidOutputMap.mergeLong(aY, estimatedAmount, Long::sum);
            tParallels.put(aY, new FluidParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            return maxParallel;
        }

        for (IFluidStore tHatch : hatches) {
            long tSpaceLeft;
            if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? PSEUDO_INFINITE_FLUID_SPACE : 0;
            } else if (tHatch instanceof OutputHatchWrapper w && w.unwrap() instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? PSEUDO_INFINITE_FLUID_SPACE : 0;
            } else {
                tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();
            }

            if (tSpaceLeft <= 0) continue;

            if (tHatch.isEmptyAndAcceptsAnyFluid()) continue;

            for (Map.Entry<FluidStack, FluidParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (!tHatch.canStoreFluid(tFluidOutput)) continue;
                FluidParallelData tParallel = entry.getValue();
                long tCraftSize = tFluidOutputMap.getLong(tFluidOutput);
                tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            }
        }
        PriorityQueue<ParallelStackInfo<FluidStack>> aParallelQueue = new PriorityQueue<>(
            Comparator.comparing(i -> i.batch));
        for (Map.Entry<FluidStack, FluidParallelData> entry : tParallels.entrySet()) {
            aParallelQueue
                .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
        }
        for (IFluidStore tHatch : hatches) {
            if (!tHatch.isEmptyAndAcceptsAnyFluid()) continue;

            ParallelStackInfo<FluidStack> tParallel = aParallelQueue.poll();
            assert tParallel != null;
            long tCraftSize = tFluidOutputMap.getLong(tParallel.stack);

            long tSpaceLeft;
            if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? PSEUDO_INFINITE_FLUID_SPACE : 0;
            } else if (tHatch instanceof OutputHatchWrapper w && w.unwrap() instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? PSEUDO_INFINITE_FLUID_SPACE : 0;
            } else {
                tSpaceLeft = tHatch.getCapacity();
            }

            tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
            tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            aParallelQueue.add(tParallel);
        }
        return GTUtility.longToInt(aParallelQueue.element().batch);
    }

    private static class FluidParallelData {

        private long batch;
        private long partial;

        private FluidParallelData(long batch, long partial) {
            this.batch = batch;
            this.partial = partial;
        }
    }

    private static class ParallelStackInfo<T> {

        private long batch;
        private long partial;
        private final T stack;

        private ParallelStackInfo(long batch, long partial, T stack) {
            this.batch = batch;
            this.partial = partial;
            this.stack = stack;
        }
    }
}
