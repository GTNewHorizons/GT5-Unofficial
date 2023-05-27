package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizons.modularui.api.forge.IItemHandler;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

/**
 * Helper class to calculate how many parallels of items / fluids can fit in the output buses / hatches.
 */
public class VoidProtectionHelper {

    /**
     * A MetaTileEntity Controller
     */
    private GT_MetaTileEntity_MultiBlockBase machineMeta;
    /**
     * A MultiTileEntity Controller
     */
    private Controller<?> machineMulti;
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

    public VoidProtectionHelper() {}

    /**
     * Sets MetaTE controller, with current configuration for void protection mode.
     */
    public VoidProtectionHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta) {
        return setController(machineMeta, machineMeta.protectsExcessItem(), machineMeta.protectsExcessFluid());
    }

    /**
     * Sets MetaTE controller, with void protection mode forcibly.
     */
    public VoidProtectionHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta, boolean protectExcessItem,
        boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machineMeta = machineMeta;
        return this;
    }

    /**
     * Sets MuTE controller, with current configuration for void protection mode.
     */
    public VoidProtectionHelper setController(Controller<?> machineMulti) {
        return setController(machineMulti, machineMulti.protectsExcessItem(), machineMulti.protectsExcessFluid());
    }

    /**
     * Sets MuTE controller, with void protection mode forcibly.
     */
    public VoidProtectionHelper setController(Controller<?> machineMulti, boolean protectExcessItem,
        boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machineMulti = machineMulti;
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

    /**
     * Finishes the VoidProtectionHelper. Anything changed after this will not affect anything
     */
    public VoidProtectionHelper build() {
        if (built) {
            throw new IllegalStateException("Tried to build twice");
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
     * Called by {@link #build()}. Determines the parallels and everything else that needs to be done at build time
     */
    private void determineParallel() {
        if (itemOutputs == null) {
            itemOutputs = new ItemStack[0];
        }
        if (fluidOutputs == null) {
            fluidOutputs = new FluidStack[0];
        }

        // Don't check ControllerWithButtons#protectsExcessItem nor #protectsExcessFluid here,
        // to allow more involved setting for void protections (see ComplexParallelProcessingLogic)
        if (machineMeta != null) {
            boolean tMEOutputBus = false;
            boolean tMEOutputHatch = false;
            for (GT_MetaTileEntity_Hatch tHatch : machineMeta.mOutputBusses) {
                if (tHatch instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
                    tMEOutputBus = true;
                    break;
                }
            }

            for (GT_MetaTileEntity_Hatch tHatch : machineMeta.mOutputHatches) {
                if (tHatch instanceof GT_MetaTileEntity_Hatch_Output_ME) {
                    tMEOutputHatch = true;
                    break;
                }
            }

            if (protectExcessItem && itemOutputs.length > 0 && !tMEOutputBus) {
                maxParallel = Math.min(calculateMaxItemParallelsForMetaTEs(), maxParallel);
            }
            if (protectExcessFluid && fluidOutputs.length > 0 && !tMEOutputHatch) {
                maxParallel = Math.min(calculateMaxFluidParallelsForMetaTEs(), maxParallel);
            }
        } else if (machineMulti != null) {
            if (protectExcessItem && itemOutputs.length > 0) {
                maxParallel = Math.min(calculateMaxItemParallelsForMuTEs(), maxParallel);
            }
            if (protectExcessFluid && fluidOutputs.length > 0) {
                maxParallel = Math.min(calculateMaxFluidParallelsForMuTEs(), maxParallel);
            }
        }
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private int calculateMaxFluidParallelsForMuTEs() {
        if (machineMulti == null || machineMulti.getOutputTanks() == null) {
            return 0;
        }
        return calculateMaxFluidParallels(
            Arrays.asList(machineMulti.getOutputTanks()),
            tHatch -> tHatch.getFluidAmount() == 0,
            (tHatch, fluidStack) -> true);
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private int calculateMaxFluidParallelsForMetaTEs() {
        if (machineMeta == null) {
            return 0;
        }
        return calculateMaxFluidParallels(
            machineMeta.mOutputHatches,
            tHatch -> tHatch.mMode == 0 && tHatch.getFluidAmount() == 0,
            (tHatch, fluidStack) -> {
                if (GT_ModHandler.isSteam(fluidStack)) {
                    return tHatch.outputsSteam();
                } else {
                    if (!tHatch.outputsLiquids()) {
                        return false;
                    }
                    String tLockedFluidName = tHatch.getLockedFluidName();
                    return !tHatch.isFluidLocked() || tLockedFluidName == null
                        || tLockedFluidName.equals(
                            fluidStack.getFluid()
                                .getName());
                }
            });
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private <T extends IFluidTank> int calculateMaxFluidParallels(List<T> hatches, Function<T, Boolean> isEmpty,
        BiFunction<T, FluidStack, Boolean> acceptsFluid) {
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
            if (aY == null) {
                continue;
            }
            tFluidOutputMap.merge(aY, aY.amount, Integer::sum);
            tParallels.put(aY, new ParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            // nothing to output, bail early
            return maxParallel;
        }

        for (T tHatch : hatches) {
            int tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();

            // check if hatch filled
            if (tSpaceLeft <= 0) continue;

            // check if hatch is empty and unrestricted
            if (isEmpty.apply(tHatch)) continue;

            for (Map.Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (!acceptsFluid.apply(tHatch, tFluidOutput)) continue;
                // this fluid is not prevented by restrictions on output hatch
                if (tHatch.getFluidAmount() == 0 || GT_Utility.areFluidsEqual(tHatch.getFluid(), tFluidOutput)) {
                    ParallelData tParallel = entry.getValue();
                    Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                    tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                    tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
                }
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
        for (T tHatch : hatches) {
            // partially filled or restricted hatch. done in last pass
            if (!isEmpty.apply(tHatch)) continue;

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

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxItemParallelsForMuTEs() {
        List<ItemStack> busStacks = new ArrayList<>();
        if (machineMulti != null) {
            IItemHandler inv = machineMulti.getOutputInventory();
            if (inv != null && inv.getSlots() > 0) {
                for (int i = 0; i < inv.getSlots(); i++) {
                    busStacks.add(inv.getStackInSlot(i));
                }
            }
        }
        return calculateMaxItemParallels(busStacks);
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxItemParallelsForMetaTEs() {
        List<ItemStack> busStacks = new ArrayList<>();
        if (machineMeta != null) {
            for (final GT_MetaTileEntity_Hatch tBus : machineMeta.mOutputBusses) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tBus)) {
                    continue;
                }
                final IInventory tBusInv = tBus.getBaseMetaTileEntity();
                for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                    busStacks.add(tBus.getStackInSlot(i));
                }
            }
        }
        return calculateMaxItemParallels(busStacks);
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     *
     * @param busStacks List of itemstacks that are already stored in buses
     */
    private int calculateMaxItemParallels(List<ItemStack> busStacks) {
        // A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the
        // recipe outputs.
        Map<ItemStack, Integer> tItemOutputMap = new ItemStackMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each item output.
        // In the pair, we keep track of number of full crafts plus number of items in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<ItemStack, ParallelData> tParallels = new ItemStackMap<>();
        int tSlotsFree = 0;
        for (ItemStack tItem : itemOutputs) {
            tItemOutputMap.merge(tItem, tItem.stackSize, Integer::sum);
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
        private int partial;

        private ParallelData(int batch, int partial) {
            this.batch = batch;
            this.partial = partial;
        }
    }

    private static class ParallelStackInfo<T> {

        private int batch;
        private int partial;
        private final T stack;

        private ParallelStackInfo(int batch, int partial, T stack) {
            this.batch = batch;
            this.partial = partial;
            this.stack = stack;
        }
    }
}
