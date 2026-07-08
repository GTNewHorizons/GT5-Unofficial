package tectech.mechanics.boseEinsteinCondensate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appeng.api.storage.data.IAEFluidStack;
import gregtech.api.factory.RoutedNode;
import it.unimi.dsi.fastutil.Pair;

class BECFactoryNetworkTest {

    // -------------------------------------------------------------------------
    // Minimal stub types
    // -------------------------------------------------------------------------

    /** Base stub: BECFactoryElement + NotableBECFactoryElement, but NOT a BECInventory. */
    static class StubGenerator implements BECFactoryElement, NotableBECFactoryElement {

        BECFactoryNetwork network;
        List<? extends NotableBECFactoryElement> routedNeighbors = Collections.emptyList();

        @Override
        public void getNeighbours(Collection<BECFactoryElement> out) {}

        @Override
        public BECFactoryNetwork getNetwork() {
            return network;
        }

        @Override
        public void setNetwork(BECFactoryNetwork n) {
            network = n;
        }

        @Override
        public BECFactoryElement.ConnectionType getConnectionOnSide(ForgeDirection side) {
            return BECFactoryElement.ConnectionType.NONE;
        }

        @Override
        public List<RoutedNode<NotableBECFactoryElement, BECRouteInfo>> getRoutedNeighbours() {
            return routedNeighbors.stream()
                .map(e -> new RoutedNode<NotableBECFactoryElement, BECRouteInfo>(e, new BECRouteInfo(1)))
                .toList();
        }
    }

    /** Storage stub: BECFactoryElement + NotableBECFactoryElement + BECInventory. */
    static class StubStorage implements BECFactoryElement, NotableBECFactoryElement, BECInventory {

        BECFactoryNetwork network;
        long amountAdded = 0;
        int removeCondensateCalls = 0;
        final double capacity;
        CondensateList contents = new CondensateList();
        List<? extends NotableBECFactoryElement> routedNeighbors = Collections.emptyList();

        StubStorage() {
            this(1_000_000);
        }

        StubStorage(double capacity) {
            this.capacity = capacity;
        }

        @Override
        public void getNeighbours(Collection<BECFactoryElement> out) {}

        @Override
        public BECFactoryNetwork getNetwork() {
            return network;
        }

        @Override
        public void setNetwork(BECFactoryNetwork n) {
            network = n;
        }

        @Override
        public BECFactoryElement.ConnectionType getConnectionOnSide(ForgeDirection side) {
            return BECFactoryElement.ConnectionType.NONE;
        }

        @Override
        public List<RoutedNode<NotableBECFactoryElement, BECRouteInfo>> getRoutedNeighbours() {
            return routedNeighbors.stream()
                .map(e -> new RoutedNode<NotableBECFactoryElement, BECRouteInfo>(e, new BECRouteInfo(1)))
                .toList();
        }

        @Override
        public List<Pair<Class<?>, Object>> getComponents() {
            return Collections.singletonList(Pair.of(BECInventory.class, (Object) this));
        }

        @Override
        public CondensateList getContents() {
            return contents;
        }

        @Override
        public double getCondensateCapacity() {
            return capacity;
        }

        @Override
        public void addCondensate(IAEFluidStack stack) {
            amountAdded += stack.getStackSize();
            stack.setStackSize(0);
        }

        @Override
        public boolean removeCondensate(IAEFluidStack stack) {
            removeCondensateCalls++;
            return false;
        }
    }

    /** Blocks all condensate passage — used to verify SkipNode behaviour. */
    static class StubBlockingStorage extends StubStorage {

        @Override
        public boolean allowsCondensateThrough(Fluid condensate) {
            return false;
        }
    }

    /**
     * Storage that actually drains condensate from the request stack, enabling
     * early-termination (Break) tests for drainCondensate.
     */
    static class StubDrainingStorage extends StubStorage {

        long stored;

        StubDrainingStorage(long stored) {
            this.stored = stored;
        }

        @Override
        public boolean removeCondensate(IAEFluidStack stack) {
            removeCondensateCalls++;
            long removed = Math.min(stack.getStackSize(), stored);
            stored -= removed;
            stack.decStackSize(removed);
            return removed > 0;
        }
    }

    // -------------------------------------------------------------------------
    // IAEFluidStack mock helpers
    // -------------------------------------------------------------------------

    /**
     * Creates a Mockito mock for IAEFluidStack that behaves like a simple mutable
     * counter. {@code empty()} returns a fresh sibling mock.
     */
    static IAEFluidStack mockFluidStack(long initialSize) {
        IAEFluidStack stack = mock(IAEFluidStack.class);
        long[] size = { initialSize };

        when(stack.getStackSize()).thenAnswer(inv -> size[0]);
        doAnswer(inv -> {
            size[0] = inv.getArgument(0);
            return null;
        }).when(stack)
            .setStackSize(anyLong());
        doAnswer(inv -> {
            size[0] -= (long) inv.getArgument(0);
            return null;
        }).when(stack)
            .decStackSize(anyLong());
        doAnswer(inv -> {
            size[0] += (long) inv.getArgument(0);
            return null;
        }).when(stack)
            .incStackSize(anyLong());

        // empty() creates a fresh sibling with size 0 that the network uses for distribution
        when(stack.empty()).thenAnswer(inv -> mockFluidStack(0));

        return stack;
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    BECFactoryNetwork network;
    StubGenerator generator;
    StubStorage storage;

    @BeforeEach
    void setUp() {
        network = new BECFactoryNetwork();

        generator = new StubGenerator();
        storage = new StubStorage();

        // generator.getRoutedNeighbours() → [storage]
        generator.routedNeighbors = List.of(storage);
    }

    /**
     * Verifies that condensate is injected into storage in the same "tick"
     * that both elements are added to the network, without requiring onPostTick
     * to run first.
     *
     * Before the fix, the route tracker's edge map was only updated by onPostTick
     * (called at ServerTickEvent.END). If a recipe finished processing before END
     * in the same tick the network was formed, injectCondensate would BFS over
     * an empty edge map and lose the condensate.
     *
     * After the fix, ensureRoutesUpToDate() rebuilds the edge map lazily before
     * any BFS operation.
     */
    @Test
    void injectCondensateFindsStorageWithoutOnPostTick() {
        network.addElement(generator);
        network.addElement(storage);

        // Do NOT call network.onPostTick() — simulates intra-tick injection

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        assertTrue(storage.amountAdded > 0, "storage should have received condensate without onPostTick");
        assertEquals(100L, storage.amountAdded, "all condensate should reach the single storage");
        assertEquals(0L, fluidStack.getStackSize(), "input stack should be fully consumed");
    }

    @Test
    void noStorageFoundWhenOnlyGeneratorPresent() {
        network.addElement(generator);
        // storage is NOT added, and generator routes to nowhere
        generator.routedNeighbors = Collections.emptyList();

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        // No BECInventory reachable → injectCondensate returns early, stack unchanged
        assertEquals(100L, fluidStack.getStackSize(), "condensate should be unaffected when no storage reachable");
    }

    @Test
    void drainCondensateFindsStorageWithoutOnPostTick() {
        network.addElement(generator);
        network.addElement(storage);

        // Pre-populate storage with some condensate
        storage.amountAdded = 500L; // cheat: set field directly for the drain check

        // No onPostTick — routes must still be built lazily
        IAEFluidStack request = mockFluidStack(50L);
        // drainCondensate decrements request as it finds condensate; we just verify
        // it doesn't throw and calls through the BFS
        assertDoesNotThrow(() -> network.drainCondensate(generator, request));
    }

    // -------------------------------------------------------------------------
    // Capacity-weighted distribution
    // -------------------------------------------------------------------------

    @Test
    void injectCondensateDistributesEquallyToTwoEqualCapacityStorages() {
        StubStorage storage2 = new StubStorage();
        generator.routedNeighbors = List.of(storage, storage2);
        network.addElement(generator);
        network.addElement(storage);
        network.addElement(storage2);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        assertEquals(100L, storage.amountAdded + storage2.amountAdded, "total condensate must be conserved");
        assertEquals(0L, fluidStack.getStackSize(), "input stack should be fully consumed");
        assertTrue(storage.amountAdded > 0, "first storage should receive some condensate");
        assertTrue(storage2.amountAdded > 0, "second storage should receive some condensate");
    }

    @Test
    void injectCondensateDistributesProportionallyByCapacity() {
        // 3:1 capacity ratio → 75:25 split for input of 100
        StubStorage large = new StubStorage(3_000_000);
        StubStorage small = new StubStorage(1_000_000);
        generator.routedNeighbors = List.of(large, small);
        network.addElement(generator);
        network.addElement(large);
        network.addElement(small);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        // GTUtility.ceil(100 * 0.75) = 75 for large; remainder 25 for small
        assertEquals(75L, large.amountAdded, "large storage (3/4 of capacity) should receive 75");
        assertEquals(25L, small.amountAdded, "small storage (1/4 of capacity) should receive 25");
        assertEquals(0L, fluidStack.getStackSize(), "input stack should be fully consumed");
    }

    // -------------------------------------------------------------------------
    // allowsCondensateThrough
    // -------------------------------------------------------------------------

    @Test
    void injectCondensateSkipsBlockedNode() {
        StubBlockingStorage blocker = new StubBlockingStorage();
        generator.routedNeighbors = List.of(blocker);
        network.addElement(generator);
        network.addElement(blocker);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        assertEquals(0L, blocker.amountAdded, "blocked node should not receive condensate");
        assertEquals(100L, fluidStack.getStackSize(), "stack should be unchanged when all nodes are blocked");
    }

    // -------------------------------------------------------------------------
    // Drain
    // -------------------------------------------------------------------------

    @Test
    void drainCondensateCallsRemoveCondensateOnReachableStorage() {
        network.addElement(generator);
        network.addElement(storage);

        IAEFluidStack request = mockFluidStack(50L);
        network.drainCondensate(generator, request);

        assertEquals(
            1,
            storage.removeCondensateCalls,
            "removeCondensate should be called exactly once on the reachable storage");
    }

    // -------------------------------------------------------------------------
    // Graph topology edge cases
    // -------------------------------------------------------------------------

    @Test
    void cycleInRouteGraphDoesNotInfiniteLoop() {
        StubGenerator a = new StubGenerator();
        StubGenerator b = new StubGenerator();
        // Mutual cycle: a ↔ b, no BECInventory anywhere
        a.routedNeighbors = List.of(b);
        b.routedNeighbors = List.of(a);
        generator.routedNeighbors = List.of(a);
        network.addElement(generator);
        network.addElement(a);
        network.addElement(b);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        assertDoesNotThrow(() -> network.injectCondensate(generator, fluidStack));
        // No BECInventory reachable → inventories empty → early return → stack unchanged
        assertEquals(100L, fluidStack.getStackSize(), "no storage in cycle → condensate should be unchanged");
    }

    @Test
    void transitiveRoutingThroughIntermediateNotable() {
        // generator → intermediate → storage (generator does NOT route directly to storage)
        StubGenerator intermediate = new StubGenerator();
        intermediate.routedNeighbors = List.of(storage);
        generator.routedNeighbors = List.of(intermediate);
        network.addElement(generator);
        network.addElement(intermediate);
        network.addElement(storage);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        assertEquals(100L, storage.amountAdded, "storage should receive condensate via intermediate notable");
        assertEquals(0L, fluidStack.getStackSize());
    }

    @Test
    void duplicateEdgeToSameStorageCountsOnce() {
        // Same storage appears twice in the routed-neighbor list
        generator.routedNeighbors = List.of(storage, storage);
        network.addElement(generator);
        network.addElement(storage);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        // inventories is a HashSet → storage counted once → all 100 goes there
        assertEquals(100L, storage.amountAdded, "duplicate edge should not double-count the storage");
        assertEquals(0L, fluidStack.getStackSize());
    }

    // -------------------------------------------------------------------------
    // getStoredCondensate
    // -------------------------------------------------------------------------

    @Test
    void getStoredCondensateSumsAcrossReachableStorages() {
        Fluid mockFluid = mock(Fluid.class);
        StubStorage storage2 = new StubStorage();
        storage.contents.put(mockFluid, 300L);
        storage2.contents.put(mockFluid, 300L);
        generator.routedNeighbors = List.of(storage, storage2);
        network.addElement(generator);
        network.addElement(storage);
        network.addElement(storage2);

        assertEquals(600L, network.getStoredCondensate(generator, mockFluid));
    }

    @Test
    void getStoredCondensateAllFluidsReturnsAllReachableFluids() {
        Fluid fluidA = mock(Fluid.class);
        Fluid fluidB = mock(Fluid.class);
        storage.contents.put(fluidA, 300L);
        storage.contents.put(fluidB, 500L);
        network.addElement(generator);
        network.addElement(storage);

        CondensateList result = network.getStoredCondensate(generator);

        assertEquals(300L, result.getLong(fluidA));
        assertEquals(500L, result.getLong(fluidB));
    }

    @Test
    void getStoredCondensateIgnoresBlockedStorage() {
        Fluid mockFluid = mock(Fluid.class);
        StubBlockingStorage blocker = new StubBlockingStorage();
        blocker.routedNeighbors = List.of(storage);
        storage.contents.put(mockFluid, 500L);
        generator.routedNeighbors = List.of(blocker);
        network.addElement(generator);
        network.addElement(blocker);
        network.addElement(storage);

        // Blocker is SkipNode'd — downstream storage is never visited
        assertEquals(0L, network.getStoredCondensate(generator, mockFluid));
    }

    // -------------------------------------------------------------------------
    // getSlowdowns
    // -------------------------------------------------------------------------

    @Test
    void getSlowdownsReturnsZeroWhenNoContaminants() {
        Fluid valid = mock(Fluid.class);
        storage.contents.put(valid, 100L);
        network.addElement(generator);
        network.addElement(storage);

        assertEquals(0, network.getSlowdowns(generator, List.of(valid)));
    }

    @Test
    void getSlowdownsCountsReachableContaminant() {
        Fluid contaminant = mock(Fluid.class);
        storage.contents.put(contaminant, 50L);
        network.addElement(generator);
        network.addElement(storage);

        assertEquals(1, network.getSlowdowns(generator, List.of()));
    }

    @Test
    void getSlowdownsTwoStoragesWithSameContaminantCountOnce() {
        Fluid contaminant = mock(Fluid.class);
        StubStorage storage2 = new StubStorage();
        storage.contents.put(contaminant, 50L);
        storage2.contents.put(contaminant, 50L);
        generator.routedNeighbors = List.of(storage, storage2);
        network.addElement(generator);
        network.addElement(storage);
        network.addElement(storage2);

        // Same contaminant in two storages → still just 1 unique contaminant
        assertEquals(1, network.getSlowdowns(generator, List.of()));
    }

    @Test
    void getSlowdownsDoesNotCountContaminantBehindBlocker() {
        Fluid contaminant = mock(Fluid.class);
        StubBlockingStorage blocker = new StubBlockingStorage();
        blocker.routedNeighbors = List.of(storage);
        storage.contents.put(contaminant, 50L);
        generator.routedNeighbors = List.of(blocker);
        network.addElement(generator);
        network.addElement(blocker);
        network.addElement(storage);

        // Phase 1 (getComponents scan) finds the contaminant in storage.
        // Phase 2 (BFS) cannot reach storage because blocker is SkipNode'd.
        // → contaminant not counted.
        assertEquals(0, network.getSlowdowns(generator, List.of()));
    }

    // -------------------------------------------------------------------------
    // Drain early termination
    // -------------------------------------------------------------------------

    @Test
    void drainCondensateStopsWhenRequestFullySatisfied() {
        StubDrainingStorage draining = new StubDrainingStorage(200L);
        StubStorage overflow = new StubStorage();
        generator.routedNeighbors = List.of(draining, overflow);
        network.addElement(generator);
        network.addElement(draining);
        network.addElement(overflow);

        IAEFluidStack request = mockFluidStack(200L);
        network.drainCondensate(generator, request);

        assertEquals(0L, request.getStackSize(), "request should be fully satisfied");
        assertEquals(1, draining.removeCondensateCalls, "draining storage visited once");
        assertEquals(0, overflow.removeCondensateCalls, "overflow should not be visited after early exit");
    }

    // -------------------------------------------------------------------------
    // Transitive blocking
    // -------------------------------------------------------------------------

    @Test
    void allowsCondensateThroughBlocksTransitivePath() {
        StubBlockingStorage blocker = new StubBlockingStorage();
        StubStorage downstream = new StubStorage();
        blocker.routedNeighbors = List.of(downstream);
        generator.routedNeighbors = List.of(blocker);
        network.addElement(generator);
        network.addElement(blocker);
        network.addElement(downstream);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        network.injectCondensate(generator, fluidStack);

        assertEquals(0L, downstream.amountAdded, "storage behind blocker should not receive condensate");
        assertEquals(100L, fluidStack.getStackSize(), "stack unchanged when blocker prevents reaching storage");
    }

    // -------------------------------------------------------------------------
    // Zero capacity distribution
    // -------------------------------------------------------------------------

    @Test
    void injectWithZeroTotalCapacityDistributesEqually() {
        StubStorage s1 = new StubStorage(0);
        StubStorage s2 = new StubStorage(0);
        generator.routedNeighbors = List.of(s1, s2);
        network.addElement(generator);
        network.addElement(s1);
        network.addElement(s2);

        IAEFluidStack fluidStack = mockFluidStack(100L);
        assertDoesNotThrow(() -> network.injectCondensate(generator, fluidStack));
        assertEquals(100L, s1.amountAdded + s2.amountAdded, "all condensate must be distributed");
        assertEquals(0L, fluidStack.getStackSize());
    }

    @Test
    void routesRebuildAfterStorageRemovedFromNetwork() {
        network.addElement(generator);
        network.addElement(storage);

        IAEFluidStack first = mockFluidStack(100L);
        network.injectCondensate(generator, first);
        assertEquals(100L, storage.amountAdded, "first inject should reach storage");

        // Remove storage — invalidateRoutes() fires, edges will be rebuilt on next BFS
        network.removeElement(storage);
        generator.routedNeighbors = Collections.emptyList();

        IAEFluidStack second = mockFluidStack(100L);
        network.injectCondensate(generator, second);
        assertEquals(100L, second.getStackSize(), "no storage reachable after removal → stack unchanged");
    }

    @Test
    void routesRebuildAfterElementAddedMidSession() {
        // Start with only the generator — no route to any storage
        network.addElement(generator);
        generator.routedNeighbors = Collections.emptyList();

        IAEFluidStack first = mockFluidStack(50L);
        network.injectCondensate(generator, first);
        assertEquals(50L, first.getStackSize(), "no storage routed yet — condensate should be unchanged");

        // Add storage and wire the route — no onPostTick between these calls
        network.addElement(storage);
        generator.routedNeighbors = List.of(storage);

        IAEFluidStack second = mockFluidStack(50L);
        network.injectCondensate(generator, second);
        assertTrue(storage.amountAdded > 0, "storage should receive condensate after route is wired");
    }
}
