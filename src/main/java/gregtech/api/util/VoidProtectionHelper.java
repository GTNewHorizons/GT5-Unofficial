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

/**
 * Helper class to calculate how many parallels of items / fluids can fit in the output buses / hatches.
 */
public class VoidProtectionHelper {

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
    private double chanceMultiplier = 1;

    private Int2IntFunction chanceGetter = i -> 10000;

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

    public VoidProtectionHelper setChanceMultiplier(double chanceMultiplier) {
        this.chanceMultiplier = chanceMultiplier;
        return this;
    }

    public VoidProtectionHelper setChanceGetter(Int2IntFunction getter) {
        this.chanceGetter = getter;
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
                        * Math.ceil(chanceMultiplier * chanceGetter.apply(i) / 10000d));

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
        // TODO: Temporary Fix, need to refactor it.
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

        // A map to hold the items we will be 'inputting' into the output hatches. These fluidstacks are actually
        // the recipe outputs.
        Map<FluidStack, Integer> tFluidOutputMap = new HashMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each fluid output.
        // In the pair, we keep track of number of full crafts plus mb of fluid in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<FluidStack, FluidParallelData> tParallels = new HashMap<>();

        // Iterate over the outputs, calculating require stack spacing they will require.
        for (FluidStack aY : fluidOutputs) {
            if (aY == null || aY.amount <= 0) {
                continue;
            }
            tFluidOutputMap.merge(aY, aY.amount, Integer::sum);
            tParallels.put(aY, new FluidParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            // nothing to output, bail early
            return maxParallel;
        }

        for (IFluidStore tHatch : hatches) {
            int tSpaceLeft;
            if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? Integer.MAX_VALUE : 0;
            } else if (tHatch instanceof OutputHatchWrapper w && w.unwrap() instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? Integer.MAX_VALUE : 0;
            } else {
                tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();
            }

            // check if hatch filled
            if (tSpaceLeft <= 0) continue;

            // check if hatch is empty and unrestricted
            if (tHatch.isEmptyAndAcceptsAnyFluid()) continue;

            for (Map.Entry<FluidStack, FluidParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (!tHatch.canStoreFluid(tFluidOutput)) continue;
                // this fluid is not prevented by restrictions on output hatch
                FluidParallelData tParallel = entry.getValue();
                Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            }
        }
        // now that all partial/restricted hatches have been counted, create a priority queue for our outputs
        // the lowest priority fluid is the number of complete parallel crafts we can support
        PriorityQueue<ParallelStackInfo<FluidStack>> aParallelQueue = new PriorityQueue<>(
            Comparator.comparing(i -> i.batch));
        for (Map.Entry<FluidStack, FluidParallelData> entry : tParallels.entrySet()) {
            aParallelQueue
                .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
        }
        // add extra parallels for open slots as well
        for (IFluidStore tHatch : hatches) {
            // partially filled or restricted hatch. done in the last pass
            if (!tHatch.isEmptyAndAcceptsAnyFluid()) continue;

            ParallelStackInfo<FluidStack> tParallel = aParallelQueue.poll();
            assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
            Integer tCraftSize = tFluidOutputMap.get(tParallel.stack);

            int tSpaceLeft;
            if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? Integer.MAX_VALUE : 0;
            } else if (tHatch instanceof OutputHatchWrapper w && w.unwrap() instanceof MTEHatchOutputME tMEHatch) {
                tSpaceLeft = tMEHatch.canAcceptFluid() ? Integer.MAX_VALUE : 0;
            } else {
                tSpaceLeft = tHatch.getCapacity();
            }

            tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
            tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            aParallelQueue.add(tParallel);
        }
        return aParallelQueue.element().batch;
    }

    private static class FluidParallelData {

        private int batch;
        private long partial;

        private FluidParallelData(int batch, long partial) {
            this.batch = batch;
            this.partial = partial;
        }
    }

    private static class ParallelStackInfo<T> {

        private int batch;
        private long partial;
        private final T stack;

        private ParallelStackInfo(int batch, long partial, T stack) {
            this.batch = batch;
            this.partial = partial;
            this.stack = stack;
        }
    }
}
