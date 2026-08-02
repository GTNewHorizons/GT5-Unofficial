package gregtech.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import gregtech.api.enums.OutputHatchType;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.IOutputHatchTransaction;
import gregtech.api.interfaces.tileentity.IVoidable;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;

public class FluidEjectionHelper {

    private final boolean fluidProtectionEnabled;

    private final EnumMap<OutputHatchType, List<IOutputHatchTransaction>> transactionsByType = new EnumMap<>(
        OutputHatchType.class);

    private boolean active = true;

    public FluidEjectionHelper(IVoidable voidable) {
        this(voidable.getOutputHatches(), voidable.protectsExcessFluid());
    }

    public FluidEjectionHelper(List<? extends IOutputHatch> hatches, boolean protectFluids) {
        this(hatches, protectFluids, false);
    }

    public FluidEjectionHelper(List<? extends IOutputHatch> hatches, boolean protectFluids, boolean isRecipeCheck) {
        fluidProtectionEnabled = protectFluids;

        for (int i = 0, hatchesSize = hatches.size(); i < hatchesSize; i++) {
            IOutputHatch hatch = hatches.get(i);
            IOutputHatchTransaction transaction = hatch.createTransaction();
            if (transaction instanceof IOutputHatchTransaction.IRecipeCheckAware tran) {
                tran.setRecipeCheck(isRecipeCheck);
            }
            if (transaction instanceof IOutputHatchTransaction.IProtectOutputAware tran) {
                tran.setProtectOutput(protectFluids);
            }
            transactionsByType.computeIfAbsent(hatch.getHatchType(), x -> new ArrayList<>())
                .add(transaction);
        }
    }

    /**
     * Ejects a single stack, removes the ejected fluids from the stack, and returns the amount of fluids that were
     * removed.
     *
     * @param stack The stack to eject. Ejected fluids are subtracted from this stack.
     * @return The number of fluids that were ejected.
     */
    public long ejectStack(FluidStack stack) {
        List<FluidStack> list = Collections.singletonList(GTUtility.copyAmount(1, stack));

        long ejected = ejectFluids(list, GTUtility.getFluidAmount(stack));

        GTUtility.decFluidAmount(stack, ejected);
        return ejected;
    }

    /**
     * Ejects fluids into the contained output hatch transactions, and calculates the number of parallels that were
     * successfully ran.
     *
     * @param outputs           The fluids to eject per parallel. Each stack is multiplied by startingParallels to
     *                          determine the total number of fluids to eject. This param is not modified.
     * @param startingParallels The number of parallels to calculate. Must be an integer >= 0.
     * @return The number of parallels that can be safely ran without voiding fluids.
     */
    public long ejectFluids(List<FluidStack> outputs, long startingParallels) {
        if (outputs == null || outputs.isEmpty()) return 0;
        if (!active)
            throw new IllegalStateException("Cannot eject additional fluids after committing an FluidEjectionHelper");

        List<FluidParallelData> outputParallels = new ArrayList<>(outputs.size());

        PriorityQueue<FluidParallelData> pendingOutputs = new PriorityQueue<>(
            Comparator.comparingDouble(output -> -output.remainingAmount / (double) output.perParallel));

        for (var e : Object2LongMaps.fastIterable(GTUtility.getFluidStackHistogram(outputs))) {
            if (e.getLongValue() <= 0) continue;

            GTUtility.FluidId id = e.getKey();
            long amount = e.getLongValue();

            FluidParallelData parallelData = new FluidParallelData(id, amount * startingParallels, amount);

            outputParallels.add(parallelData);

            if (parallelData.remainingAmount <= 0) continue;

            List<IOutputHatchTransaction> transactions = new ArrayList<>(8);

            for (OutputHatchType hatchType : OutputHatchType.VALUES) {
                List<IOutputHatchTransaction> ofType = transactionsByType.get(hatchType);

                if (ofType == null) continue;

                GTDataUtils
                    .addAllFiltered(ofType, transactions, t -> !t.isFiltered() || t.isFilteredTo(parallelData.id));
            }

            parallelData.outputs = Iterators.peekingIterator(transactions.iterator());

            pendingOutputs.add(parallelData);
        }

        // This grabs the largest stack in the priority queue and tries to fill one slot in a hatch with it, until there
        // are no more fluids remaining or until all hatches cannot accept more fluids.
        while (!pendingOutputs.isEmpty()) {
            // Grab the largest stack
            FluidParallelData output = pendingOutputs.poll();

            PeekingIterator<IOutputHatchTransaction> outputHatches = output.outputs;

            // Loop until there are no more output hatches for this fluid, or until all of its stacks have been ejected.
            while (outputHatches.hasNext() && output.remainingAmount > 0) {
                IOutputHatchTransaction transaction = outputHatches.peek();

                // If this hatch is completely full, don't bother checking it.
                if (!transaction.hasAvailableSpace()) {
                    transaction.complete(output.id);
                    outputHatches.next();
                    continue;
                }

                // Fill at most one slot with the remaining fluids
                if (output.storePartial(transaction)) {
                    break;
                } else {
                    // If we couldn't insert anything into the hatch, go to the next one
                    outputHatches.next();
                }
            }

            // If there are still hatches available and there are still fluids to eject, insert this fluid back into the
            // queue. It will be put into its proper spot in the priority queue automatically.
            if (outputHatches.hasNext() && output.remainingAmount > 0) {
                pendingOutputs.add(output);
            }
        }

        if (fluidProtectionEnabled) {
            // If we care about protecting the fluids, reduce the starting parallels by however many batches of fluids
            // were ejected.
            // Otherwise, we can just tell the multi to run the full amount and it'll void everything that doesn't fit.

            for (FluidParallelData parallelData : outputParallels) {
                long ejected = parallelData.initialAmount - parallelData.remainingAmount;

                startingParallels = Math.min(startingParallels, ejected / parallelData.perParallel);
            }
        }

        return startingParallels;
    }

    public int ejectFluids(List<FluidStack> outputs, int startingParallels) {
        return GTUtility.longToInt(ejectFluids(outputs, (long) startingParallels));
    }

    public void commit() {
        transactionsByType.forEach((hatchType, list) -> list.forEach(IOutputHatchTransaction::commit));

        active = false;
    }

    private static class FluidParallelData {

        public final GTUtility.FluidId id;
        public long initialAmount, remainingAmount;
        public long perParallel;
        public PeekingIterator<IOutputHatchTransaction> outputs;
        private final FluidStack tmpStack;

        private FluidParallelData(GTUtility.FluidId id, long amount, long perParallel) {
            this.id = id;
            this.remainingAmount = amount;
            this.perParallel = perParallel;
            this.initialAmount = amount;
            this.tmpStack = id.getFluidStack();
        }

        public boolean storePartial(IOutputHatchTransaction transaction) {
            boolean isSharedOutput = transaction instanceof IOutputHatchTransaction.IDynamicCapacityOutputAware sharedOutput
                && sharedOutput.isDynamicCapacity();
            long targetAmount = remainingAmount;
            if (isSharedOutput) {
                targetAmount = Math.min(remainingAmount, perParallel);
            }
            GTUtility.setFluidAmount(tmpStack, targetAmount);
            transaction.storePartial(id, tmpStack);
            long actuallyInsert = targetAmount - GTUtility.getFluidAmount(tmpStack);
            remainingAmount -= actuallyInsert;
            return actuallyInsert > 0;
        }
    }
}
