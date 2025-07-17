package gregtech.api.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.util.extensions.IteratorExt;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * A helper class that contains the logic for ejecting items from multiblock processing into output busses. This is used
 * for item void protection calculates, but it can be used for generic item ejecting as well. This stores output bus
 * transactions and the original busses are only modified if {@link #commit()} is called, beyond which point this helper
 * is 'spent' and cannot be modified further.
 */
public class ItemEjectionHelper {

    private final boolean itemProtectionEnabled;
    private final List<IOutputBusTransaction> discreteTransactions, nonDiscreteTransactions;

    private boolean active = true;

    public ItemEjectionHelper(IVoidable voidable) {
        itemProtectionEnabled = voidable.protectsExcessItem();

        discreteTransactions = new ArrayList<>();
        nonDiscreteTransactions = new ArrayList<>();

        for (IOutputBus bus : voidable.getOutputBusses()) {
            if (bus.hasDiscreteSlots()) {
                discreteTransactions.add(bus.createTransaction());
            } else {
                nonDiscreteTransactions.add(bus.createTransaction());
            }
        }
    }

    public ItemEjectionHelper(List<IOutputBus> busses, boolean protectItems) {
        itemProtectionEnabled = protectItems;

        discreteTransactions = new ArrayList<>();
        nonDiscreteTransactions = new ArrayList<>();

        for (IOutputBus bus : busses) {
            if (bus.hasDiscreteSlots()) {
                discreteTransactions.add(bus.createTransaction());
            } else {
                nonDiscreteTransactions.add(bus.createTransaction());
            }
        }
    }

    /**
     * Ejects items into the contained output bus transactions, and calculates the number of parallels that were
     * successfully ran.
     * 
     * @param outputs           The items to eject per parallels. Total amount of items ejected are multiplied by the
     *                          number of starting parallels. Not modified.
     * @param startingParallels The number of parallels to calculate. This param is O(1) so you can set it to any
     *                          positive integer.
     * @return The number of parallels that can be safely ran without voiding items.
     */
    public int ejectItems(List<ItemStack> outputs, int startingParallels) {
        if (outputs == null || outputs.isEmpty()) return 0;
        if (!active)
            throw new IllegalStateException("Cannot eject additional items after committing an ItemEjectionHelper");

        Object2LongOpenHashMap<GTUtility.ItemId> outputMap = GTUtility.getItemStackHistogram(outputs);

        Object2ObjectOpenHashMap<GTUtility.ItemId, ItemParallelData> outputParallels = new Object2ObjectOpenHashMap<>();

        for (var e : Object2LongMaps.fastIterable(outputMap)) {
            GTUtility.ItemId id = e.getKey();
            int amount = GTUtility.longToInt(e.getLongValue());

            outputParallels
                .put(id, new ItemParallelData(id, GTUtility.longToInt(amount * (long) startingParallels), amount));
        }

        // Try to eject stacks into ME output busses.

        // First pass: try filtered busses.
        for (IOutputBusTransaction transaction : nonDiscreteTransactions) {
            if (!transaction.isFiltered()) continue;

            outputParallels.forEach((id, parallelData) -> {
                if (parallelData.remaining.stackSize == 0) return;

                if (!transaction.isFilteredToItem(id)) return;

                // If this item can be stored in an ME bus, it should accept the whole stack.
                // storePartial will decrement the stack size itself, so we won't mis-assign any items here by accident
                // in case something changes in ME busses.
                if (transaction.canStoreItem(id)) {
                    transaction.storePartial(id, parallelData.remaining);
                }
            });
        }

        // Second pass: try unfiltered busses.
        for (IOutputBusTransaction transaction : nonDiscreteTransactions) {
            if (transaction.isFiltered()) continue;

            outputParallels.forEach((id, parallelData) -> {
                if (parallelData.remaining.stackSize == 0) return;

                if (transaction.canStoreItem(id)) {
                    transaction.storePartial(id, parallelData.remaining);
                }
            });
        }

        // We don't have to check ME outputs by this point, because they've already been 'filled' with everything they
        // can hold
        List<IOutputBusTransaction> unfilteredTransactions = GTDataUtils
            .filterList(discreteTransactions, b -> !b.isFiltered());

        PriorityQueue<ItemParallelData> pendingOutputs = new PriorityQueue<>(
            Comparator.comparingInt(output -> output.remaining.stackSize));

        outputParallels.forEach((id, parallelData) -> {
            // If this item has no remaining stackSize, it's either some weird NC output or it's been assigned to an ME
            // output, in which case we can ignore it for the below output bin packing algorithm entirely.
            if (parallelData.remaining.stackSize <= 0) return;

            List<IOutputBusTransaction> filteredBusses = GTDataUtils
                .filterList(discreteTransactions, t -> t.isFilteredToItem(parallelData.id));

            parallelData.outputs = Iterators
                .peekingIterator(IteratorExt.merge(filteredBusses.iterator(), unfilteredTransactions.iterator()));

            pendingOutputs.add(parallelData);
        });

        // This grabs the smallest stack in the priority queue and tries to fill one slot in a bus with it, until there
        // are no more items remaining or until all busses cannot accept more items.
        while (!pendingOutputs.isEmpty()) {
            // Grab the smallest stack
            ItemParallelData output = pendingOutputs.poll();

            PeekingIterator<IOutputBusTransaction> outputBusses = output.outputs;

            // Loop until there are no more output busses for this item, or until all of its stacks have been ejected.
            while (outputBusses.hasNext() && output.remaining.stackSize > 0) {
                IOutputBusTransaction transaction = outputBusses.peek();

                // If this bus is completely full, don't bother checking it.
                if (!transaction.hasAvailableSpace()) {
                    transaction.completeItem(output.id);
                    outputBusses.next();
                    continue;
                }

                // Fill at most one slot with the remaining items
                if (transaction.storePartial(output.id, output.remaining)) {
                    break;
                } else {
                    // If we couldn't insert anything into the bus, we won't ever try to put this item in again so we
                    // can tell the transaction to ignore this item's slots from now on.
                    transaction.completeItem(output.id);
                    outputBusses.next();
                }
            }

            // If there are still busses available and there are still items to eject, insert this item back into the
            // queue. It will be put into its proper spot in the priority queue automatically.
            if (outputBusses.hasNext() && output.remaining.stackSize > 0) {
                pendingOutputs.add(output);
            }
        }

        if (itemProtectionEnabled) {
            // If we care about protecting the items, reduce the starting parallels by however many batches of items
            // were ejected.
            // Otherwise, we can just tell the multi to run the full amount and it'll void everything that doesn't fit.

            for (ItemParallelData parallelData : outputParallels.values()) {
                int ejected = parallelData.initialAmount - parallelData.remaining.stackSize;

                startingParallels = Math.min(startingParallels, ejected / parallelData.perParallel);
            }
        }

        return startingParallels;
    }

    public void commit() {
        discreteTransactions.forEach(IOutputBusTransaction::commit);
        nonDiscreteTransactions.forEach(IOutputBusTransaction::commit);

        active = false;
    }

    private static class ItemParallelData {

        public final GTUtility.ItemId id;
        public final ItemStack remaining;
        public int perParallel, initialAmount;
        public PeekingIterator<IOutputBusTransaction> outputs;

        private ItemParallelData(GTUtility.ItemId id, int amount, int perParallel) {
            this.id = id;
            this.remaining = id.getItemStack(amount);
            this.perParallel = perParallel;
            this.initialAmount = amount;
        }
    }
}
