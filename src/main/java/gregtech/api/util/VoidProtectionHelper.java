package gregtech.api.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IVoidable;
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
                    protectExcessItem,
                    true);

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
        List<FluidStack> maxFluidOutputs = new ArrayList<>(fluidOutputs.length);

        for (int i = 0, fluidOutputsLength = fluidOutputs.length; i < fluidOutputsLength; i++) {
            FluidStack stack = fluidOutputs[i];

            if (stack == null || stack.amount == 0) continue;

            // Find the max possible output for this stack (note the .ceil)
            // We can't know how many fluids per parallel will be eventually ejected, so we just check the
            // worst-case scenario
            int stackSize = (int) (stack.amount
                * Math.ceil(outputChanceMultiplier * fluidOutputChanceGetter.apply(i) / 10000d));

            maxFluidOutputs.add(GTUtility.copyAmount(stackSize, stack));
        }

        // Pass the VP helper's protectExcessFluid flag to the ejection helper instead of using the machine's
        // flag
        FluidEjectionHelper ejectionHelper = new FluidEjectionHelper(
            machine.getOutputHatches(),
            protectExcessFluid,
            true);

        return ejectionHelper.ejectFluids(maxFluidOutputs, maxParallel);
    }
}
