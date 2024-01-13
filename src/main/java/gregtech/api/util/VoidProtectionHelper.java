package gregtech.api.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizons.modularui.api.fluids.IFluidTankLong;

import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;

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
     * The item output inventory
     */
    private ItemInventoryLogic itemOutputInventory;
    /**
     * The fluid output inventory
     */
    private FluidInventoryLogic fluidOutputInventory;
    /**
     * Has this helper been built?
     */
    private boolean built;
    /**
     * Is this helper working for a MuTE?
     */
    private boolean muteMode;
    /**
     * Multiplier by which the output will be multiplied
     */
    private int outputMultiplier = 1;
    /**
     * Multiplier that is applied on the output chances
     */
    private double chanceMultiplier = 1;

    private Function<Integer, Integer> chanceGetter;

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

    public VoidProtectionHelper setItemOutputInventory(ItemInventoryLogic itemOutputInventory) {
        this.itemOutputInventory = itemOutputInventory;
        return this;
    }

    public VoidProtectionHelper setFluidOutputInventory(FluidInventoryLogic fluidOutputInventory) {
        this.fluidOutputInventory = fluidOutputInventory;
        return this;
    }

    public VoidProtectionHelper setMuTEMode(boolean muteMode) {
        this.muteMode = muteMode;
        return this;
    }

    public VoidProtectionHelper setOutputMultiplier(int outputMultiplier) {
        this.outputMultiplier = outputMultiplier;
        return this;
    }

    public VoidProtectionHelper setChanceMultiplier(double chanceMultiplier) {
        this.chanceMultiplier = chanceMultiplier;
        return this;
    }

    public VoidProtectionHelper setChangeGetter(Function<Integer, Integer> getter) {
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
            itemOutputs = new ItemStack[0];
        }
        if (fluidOutputs == null) {
            fluidOutputs = new FluidStack[0];
        }

        // Don't check IVoidable#protectsExcessItem nor #protectsExcessFluid here,
        // to allow more involved setting for void protections (see ComplexParallelProcessingLogic)
        if (protectExcessItem && itemOutputs.length > 0 && !machine.canDumpItemToME()) {
            maxParallel = Math.min(calculateMaxItemParallels(), maxParallel);
            if (maxParallel <= 0) {
                isItemFull = true;
                return;
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
        if (hatches.size() < fluidOutputs.length) {
            return 0;
        }

        // A map to hold the items we will be 'inputting' into the output hatches. These fluidstacks are actually
        // the recipe outputs.
        Map<FluidStack, Integer> tFluidOutputMap = new HashMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each fluid output.
        // In the pair, we keep track of number of full crafts plus mb of fluid in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<FluidStack, ParallelData> tParallels = new HashMap<>();

        // Iterate over the outputs, calculating require stack spacing they will require.
        for (FluidStack aY : fluidOutputs) {
            if (aY == null || aY.amount <= 0) {
                continue;
            }
            tFluidOutputMap.merge(aY, aY.amount, Integer::sum);
            tParallels.put(aY, new ParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            // nothing to output, bail early
            return maxParallel;
        }

        for (IFluidStore tHatch : hatches) {
            int tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();

            // check if hatch filled
            if (tSpaceLeft <= 0) continue;

            // check if hatch is empty and unrestricted
            if (tHatch.isEmptyAndAcceptsAnyFluid()) continue;

            for (Map.Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (!tHatch.canStoreFluid(tFluidOutput)) continue;
                // this fluid is not prevented by restrictions on output hatch
                ParallelData tParallel = entry.getValue();
                Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            }
        }
        // now that all partial/restricted hatches have been counted, create a priority queue for our outputs
        // the lowest priority fluid is the number of complete parallel crafts we can support
        PriorityQueue<ParallelStackInfo<FluidStack>> aParallelQueue = new PriorityQueue<>(
            Comparator.comparing(i -> i.batch));
        for (Map.Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
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
            int tSpaceLeft = tHatch.getCapacity();
            tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
            tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            aParallelQueue.add(tParallel);
        }
        return aParallelQueue.element().batch;
    }

    private int calculateMaxFluidParallelsMuTE() {
        if (fluidOutputs.length > fluidOutputInventory.getInventory()
            .getTanks()) {
            return 0;
        }

        // A map to hold the items we will be 'inputting' into the output hatches. These fluidstacks are actually
        // the recipe outputs.
        Map<FluidStack, Integer> tFluidOutputMap = new HashMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each fluid output.
        // In the pair, we keep track of number of full crafts plus mb of fluid in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<FluidStack, ParallelData> tParallels = new HashMap<>();

        // Iterate over the outputs, calculating require stack spacing they will require.
        for (FluidStack aY : fluidOutputs) {
            if (aY == null) continue;
            int fluidAmount = aY.amount * outputMultiplier;
            if (fluidAmount <= 0) continue;
            tFluidOutputMap.merge(aY, fluidAmount, Integer::sum);
            tParallels.put(aY, new ParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            // nothing to output, bail early
            return maxParallel;
        }

        for (int i = 0; i < fluidOutputInventory.getInventory()
            .getTanks(); i++) {
            IFluidTankLong tank = fluidOutputInventory.getInventory()
                .getFluidTank(i);
            long tSpaceLeft = tank.getCapacityLong() - tank.getFluidAmountLong();
            // check if hatch filled
            if (tSpaceLeft <= 0) continue;
            // check if hatch is empty and unrestricted
            if (tank.getStoredFluid() == null) continue;

            for (Map.Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (tank.fill(tFluidOutput.getFluid(), tFluidOutput.amount, false) == tFluidOutput.amount) continue;
                // this fluid is not prevented by restrictions on output hatch
                ParallelData tParallel = entry.getValue();
                Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            }
        }
        // now that all partial/restricted hatches have been counted, create a priority queue for our outputs
        // the lowest priority fluid is the number of complete parallel crafts we can support
        PriorityQueue<ParallelStackInfo<FluidStack>> aParallelQueue = new PriorityQueue<>(
            Comparator.comparing(i -> i.batch));
        for (Map.Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
            aParallelQueue
                .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
        }
        // add extra parallels for open slots as well
        for (int i = 0; i < fluidOutputInventory.getInventory()
            .getTanks(); i++) {
            IFluidTankLong tank = fluidOutputInventory.getInventory()
                .getFluidTank(i);
            // partially filled or restricted hatch. done in the last pass
            if (tank.getStoredFluid() != null) continue;

            ParallelStackInfo<FluidStack> tParallel = aParallelQueue.poll();
            assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
            Integer tCraftSize = tFluidOutputMap.get(tParallel.stack);
            long tSpaceLeft = tank.getCapacityLong();
            tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
            tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            aParallelQueue.add(tParallel);
        }

        return aParallelQueue.element().batch;
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxItemParallels() {
        List<ItemStack> busStacks;

        if (muteMode) {
            busStacks = itemOutputInventory.getInventory()
                .getStacks();
        } else {
            busStacks = machine.getItemOutputSlots(itemOutputs);
        }
        // A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the
        // recipe outputs.
        Map<ItemStack, Integer> tItemOutputMap = new ItemStackMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each item output.
        // In the pair, we keep track of number of full crafts plus number of items in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<ItemStack, ParallelData> tParallels = new ItemStackMap<>();
        int tSlotsFree = 0;
        int index = 0;
        for (ItemStack tItem : itemOutputs) {
            // GT_RecipeBuilder doesn't handle null item output
            if (tItem == null) continue;
            int itemStackSize = (int) (tItem.stackSize * outputMultiplier
                * Math.ceil(chanceMultiplier * chanceGetter.apply(index++) / 10000));
            if (itemStackSize <= 0) continue;
            tItemOutputMap.merge(tItem, itemStackSize, Integer::sum);
            tParallels.put(tItem, new ParallelData(0, 0));
        }

        if (tItemOutputMap.isEmpty()) {
            // nothing to output, bail early
            return maxParallel;
        }

        if (itemOutputs.length > 0) {
            for (ItemStack tBusStack : busStacks) {
                if (tBusStack == null) {
                    tSlotsFree++;
                } else {
                    // get the real stack size
                    // we ignore the bus inventory stack limit here as no one set it to anything other than 64
                    int tMaxBusStackSize = tBusStack.getMaxStackSize();
                    if (tBusStack.stackSize >= tMaxBusStackSize)
                        // this bus stack is full. no checking
                        continue;
                    int tSpaceLeft = tMaxBusStackSize - tBusStack.stackSize;
                    Integer tCraftSize = tItemOutputMap.get(tBusStack);
                    if (tCraftSize == null) {
                        // we don't have a matching stack to output, ignore this bus stack
                        continue;
                    }
                    ParallelData tParallel = tParallels.get(tBusStack);
                    tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                    tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
                }

            }
            // now that all partial stacks have been counted, create a priority queue for our outputs
            // the lowest priority item is the number of complete parallel crafts we can support
            PriorityQueue<ParallelStackInfo<ItemStack>> aParallelQueue = new PriorityQueue<>(
                Comparator.comparing(i -> i.batch));
            for (Map.Entry<ItemStack, ParallelData> entry : tParallels.entrySet()) {
                aParallelQueue
                    .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
            }

            while (tSlotsFree > 0) {
                ParallelStackInfo<ItemStack> tParallel = aParallelQueue.poll();
                assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
                Integer tCraftSize = tItemOutputMap.get(tParallel.stack);
                int tStackSize = tParallel.stack.getMaxStackSize();
                tParallel.batch += (tParallel.partial + tStackSize) / tCraftSize;
                tParallel.partial = (tParallel.partial + tStackSize) % tCraftSize;
                aParallelQueue.add(tParallel);
                --tSlotsFree;
            }

            return aParallelQueue.element().batch;
        }
        return 0;
    }

    private static class ParallelData {

        private int batch;
        private long partial;

        private ParallelData(int batch, long partial) {
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
