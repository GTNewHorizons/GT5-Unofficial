package gregtech.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.util.extensions.IteratorExt;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;

/**
 * A helper class that contains the logic for ejecting items from multiblock processing into output busses. This is used
 * for item void protection calculations, but it can be used for generic item ejecting as well. This operates on output
 * bus transactions. The original busses are only modified if {@link #commit()} is called, beyond which point this
 * helper is 'spent' and cannot be modified further.
 */
public class ItemEjectionHelper {

    private final boolean itemProtectionEnabled;
    private final List<IOutputBusTransaction> discreteTransactions, nonDiscreteTransactions, unfilteredStandard,
        unfilteredME;

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

        unfilteredStandard = GTDataUtils.filterList(discreteTransactions, t -> !t.isFiltered());
        unfilteredME = GTDataUtils.filterList(nonDiscreteTransactions, t -> !t.isFiltered());
    }

    public ItemEjectionHelper(List<IOutputBus> busses, boolean protectItems) {
        itemProtectionEnabled = protectItems;

        discreteTransactions = new ArrayList<>();
        nonDiscreteTransactions = new ArrayList<>();

        for (int i = 0, bussesSize = busses.size(); i < bussesSize; i++) {
            IOutputBus bus = busses.get(i);

            if (bus.hasDiscreteSlots()) {
                discreteTransactions.add(bus.createTransaction());
            } else {
                nonDiscreteTransactions.add(bus.createTransaction());
            }
        }

        unfilteredStandard = GTDataUtils.filterList(discreteTransactions, t -> !t.isFiltered());
        unfilteredME = GTDataUtils.filterList(nonDiscreteTransactions, t -> !t.isFiltered());
    }

    /**
     * Ejects a single stack, removes the ejected items from the stack, and returns the amount of items that were
     * removed.
     *
     * @param stack The stack to eject. Ejected items are subtracted from this stack.
     * @return The number of items that were ejected.
     */
    public int ejectStack(ItemStack stack) {
        List<ItemStack> list = Collections.singletonList(GTUtility.copyAmount(1, stack));

        int ejected = ejectItems(list, stack.stackSize);

        stack.stackSize -= ejected;
        return ejected;
    }

    /**
     * Ejects items into the contained output bus transactions, and calculates the number of parallels that were
     * successfully ran.
     *
     * @param outputs           The items to eject per parallel. Each stack is multiplied by startingParallels to
     *                          determine the total number of items to eject. This param is not modified.
     * @param startingParallels The number of parallels to calculate. Must be an integer >= 0.
     * @return The number of parallels that can be safely ran without voiding items.
     */
    public int ejectItems(List<ItemStack> outputs, int startingParallels) {
        if (outputs == null || outputs.isEmpty()) return 0;
        if (!active)
            throw new IllegalStateException("Cannot eject additional items after committing an ItemEjectionHelper");

        List<ItemParallelData> outputParallels = new ArrayList<>(outputs.size());

        PriorityQueue<ItemParallelData> pendingOutputs = new PriorityQueue<>(
            Comparator.comparingInt(output -> output.remaining.stackSize));

        for (var e : Object2LongMaps.fastIterable(GTUtility.getItemStackHistogram(outputs))) {
            GTUtility.ItemId id = e.getKey();
            int amount = GTUtility.longToInt(e.getLongValue());

            ItemParallelData parallelData = new ItemParallelData(
                id,
                GTUtility.longToInt(amount * (long) startingParallels),
                amount);

            outputParallels.add(parallelData);

            if (parallelData.remaining.stackSize <= 0) continue;

            List<IOutputBusTransaction> filteredStandard = GTDataUtils
                .filterList(discreteTransactions, t -> t.isFilteredToItem(parallelData.id));
            List<IOutputBusTransaction> filteredME = GTDataUtils
                .filterList(nonDiscreteTransactions, t -> t.isFilteredToItem(parallelData.id));

            List<Iterator<IOutputBusTransaction>> iters = new ArrayList<>(4);

            // Busses are checked in this order
            if (!filteredStandard.isEmpty()) iters.add(filteredStandard.iterator());
            if (!filteredME.isEmpty()) iters.add(filteredME.iterator());
            if (!unfilteredStandard.isEmpty()) iters.add(unfilteredStandard.iterator());
            if (!unfilteredME.isEmpty()) iters.add(unfilteredME.iterator());

            parallelData.outputs = Iterators
                .peekingIterator(IteratorExt.merge(iters.toArray(GTValues.emptyIteratorArray())));

            pendingOutputs.add(parallelData);
        }

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
                    // If we couldn't insert anything into the bus, go to the next one
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

            for (ItemParallelData parallelData : outputParallels) {
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
