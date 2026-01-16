package gregtech.api.factory.itempipes;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.value.IntValue;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.factory.standard.StandardFactoryNetwork;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayPriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class ItemPipeFactoryNetwork
    extends StandardFactoryNetwork<ItemPipeFactoryNetwork, ItemPipeFactoryElement, ItemPipeFactoryGrid> {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public final int id = COUNTER.getAndIncrement();

    @Desugar
    public record TransferInfo(int stacksToTransfer, int stackMultiplier) {}

    @Desugar
    public record PipeJumpTransferState(ItemPipeFactoryElement master, IntValue transfers, IntValue lastTransferred,
        IntValue stepCounter) {

        public int getTransferredStacks() {
            return transfers.getIntValue();
        }

        public void resetTransfers() {
            lastTransferred.setIntValue(transfers.getIntValue());
            transfers.setIntValue(0);
        }

        public int getStepCounter() {
            return stepCounter.getIntValue();
        }

        public void setStepCounter(int step) {
            stepCounter.setIntValue(step);
        }
    }

    @Desugar
    public record PipeJump(ItemPipeFactoryElement to, long totalDistance, TransferInfo transferInfo,
        Set<ItemPipeFactoryElement> pipes, PipeJumpTransferState transferState) {

        public void incrementTransferCounter(int stacks) {
            transferState.transfers.setIntValue(transferState.transfers.getIntValue() + stacks);
        }

        public int getRemainingTransferrableStacks() {
            return transferInfo.stacksToTransfer - transferState.transfers.getIntValue();
        }
    }

    private final Map<ItemPipeFactoryElement, PipeJump> jumps = new Object2ObjectOpenHashMap<>();
    private final Set<ItemPipeFactoryElement> pipesWithInventories = new ObjectOpenHashSet<>();
    private boolean calculatedJumps = false;

    @Override
    public void addElement(ItemPipeFactoryElement element) {
        super.addElement(element);

        calculatedJumps = false;
    }

    @Override
    public void removeElement(ItemPipeFactoryElement element) {
        super.removeElement(element);

        calculatedJumps = false;
    }

    @Override
    public void onElementUpdated(ItemPipeFactoryElement element, boolean topologyChanged) {
        if (!topologyChanged) {
            if (element.isConnectedToInventory()) {
                pipesWithInventories.add(element);
            } else {
                pipesWithInventories.remove(element);
            }
        } else {
            calculatedJumps = false;
        }
    }

    private void calculateJumps() {
        if (calculatedJumps) return;

        jumps.clear();
        pipesWithInventories.clear();
        calculatedJumps = true;

        for (ItemPipeFactoryElement element : elements) {
            element.setJumpTransferState(null);

            if (element.isConnectedToInventory()) pipesWithInventories.add(element);
        }

        HashSet<ItemPipeFactoryElement> visited = new HashSet<>(elements.size());

        for (ItemPipeFactoryElement element : elements) {
            if (!visited.add(element)) continue;

            Set<ItemPipeFactoryElement> edges = ItemPipeFactoryGrid.INSTANCE.edges.get(element);

            if (edges.size() != 2 || pipesWithInventories.contains(element)) continue;

            HashSet<ItemPipeFactoryElement> path = new HashSet<>();
            path.add(element);

            Iterator<ItemPipeFactoryElement> edgeIter = edges.iterator();

            ItemPipeFactoryElement a = walkPath(element, edgeIter.next(), path);
            ItemPipeFactoryElement b = walkPath(element, edgeIter.next(), path);

            if (path.size() == 1) continue;

            visited.addAll(path);

            int maxStacksToTransfer = Integer.MAX_VALUE;
            int stackMultiplier = Integer.MAX_VALUE;
            long distance = 0;

            PipeJumpTransferState transferState = new PipeJumpTransferState(
                element,
                new IntValue(0),
                new IntValue(0),
                new IntValue(0));

            for (ItemPipeFactoryElement step : path) {
                distance += step.getStepSize();
                maxStacksToTransfer = Math.min(maxStacksToTransfer, step.getMaxTransferableStacks());
                stackMultiplier = Math.min(stackMultiplier, step.getStackMultiplier());
                step.setJumpTransferState(transferState);
            }

            jumps.put(
                a,
                new PipeJump(b, distance, new TransferInfo(maxStacksToTransfer, stackMultiplier), path, transferState));
            jumps.put(
                b,
                new PipeJump(a, distance, new TransferInfo(maxStacksToTransfer, stackMultiplier), path, transferState));
        }
    }

    private ItemPipeFactoryElement walkPath(ItemPipeFactoryElement start, ItemPipeFactoryElement curr,
        HashSet<ItemPipeFactoryElement> path) {
        if (ItemPipeFactoryGrid.INSTANCE.edges.get(curr)
            .size() != 2) return start;

        path.add(curr);

        ItemPipeFactoryElement prev = start;
        ItemPipeFactoryElement next;

        while ((next = getNext(curr, prev)) != null) {
            path.add(next);
            prev = curr;
            curr = next;
        }

        return curr;
    }

    private ItemPipeFactoryElement getNext(ItemPipeFactoryElement node, ItemPipeFactoryElement from) {
        Set<ItemPipeFactoryElement> edges = ItemPipeFactoryGrid.INSTANCE.edges.get(node);

        if (edges.size() != 2) return null;

        for (ItemPipeFactoryElement element : edges) {
            if (element == from) continue;

            if (pipesWithInventories.contains(element)) return null;

            if (ItemPipeFactoryGrid.INSTANCE.edges.get(element)
                .size() != 2) return null;

            return element;
        }

        return null;
    }

    @Desugar
    public record Step(Step prev, ItemPipeFactoryElement pipe, long totalDistance, PipeJump jump) {

        public TransferInfo getTransferInfo() {
            Step curr = this;

            int remainingStackTransfers = pipe.getRemainingTransferrableStacks();
            int stackMultiplier = pipe.getStackMultiplier();

            while (curr != null) {
                if (curr.jump != null) {
                    remainingStackTransfers = Math
                        .min(remainingStackTransfers, curr.jump.getRemainingTransferrableStacks());
                    stackMultiplier = Math.min(stackMultiplier, curr.jump.transferInfo.stackMultiplier);
                } else {
                    remainingStackTransfers = Math
                        .min(remainingStackTransfers, curr.pipe.getRemainingTransferrableStacks());
                    stackMultiplier = Math.min(stackMultiplier, curr.pipe.getStackMultiplier());
                }

                if (remainingStackTransfers <= 0) break;

                curr = curr.prev;
            }

            return new TransferInfo(remainingStackTransfers, stackMultiplier);
        }

        public void markChainUsed(int stacks) {
            Step curr = this;

            while (curr != null) {
                if (curr.jump != null) {
                    curr.jump.incrementTransferCounter(stacks);
                } else {
                    curr.pipe.incrementTransferCounter(stacks);
                }

                curr = curr.prev;
            }
        }
    }

    public interface PipeWalker {

        boolean accept(Step step);
    }

    public void walkPipes(ItemPipeFactoryElement start, PipeWalker walker) {
        calculateJumps();

        Set<ItemPipeFactoryElement> visited = new ObjectOpenHashSet<>();

        ObjectArrayPriorityQueue<Step> queue = new ObjectArrayPriorityQueue<>(
            4,
            Comparator.comparingLong(Step::totalDistance));

        queue.enqueue(new Step(null, start, start.getStepSize(), null));

        int counter = 0;

        while (!queue.isEmpty()) {
            Step curr = queue.dequeue();

            if (!visited.add(curr.pipe)) continue;

            curr.pipe.setStepCounter(counter);
            if (curr.jump != null) curr.jump.transferState.setStepCounter(counter);

            counter++;

            if (!curr.pipe.canTransferMoreItemsThisTick()) continue;

            if (pipesWithInventories.contains(curr.pipe)) {
                if (!walker.accept(curr)) return;
            }

            PipeJump jump = jumps.get(curr.pipe);

            if (jump != null) {
                queue.enqueue(new Step(curr, jump.to, curr.totalDistance + jump.totalDistance, jump));
            } else {
                Set<ItemPipeFactoryElement> edges = ItemPipeFactoryGrid.INSTANCE.edges.get(curr.pipe);

                for (ItemPipeFactoryElement next : edges) {
                    queue.enqueue(new Step(curr, next, curr.totalDistance + next.getStepSize(), null));
                }
            }
        }
    }

    public Object2LongOpenHashMap<GTUtility.ItemId> plungerNetwork() {
        Object2LongOpenHashMap<GTUtility.ItemId> items = new Object2LongOpenHashMap<>();

        for (ItemPipeFactoryElement element : elements) {
            for (ItemStack stack : element.removeAllItems()) {
                items.addTo(GTUtility.ItemId.create(stack), stack.stackSize);
            }
        }

        return items;
    }
}
