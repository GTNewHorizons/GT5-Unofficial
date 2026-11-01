package gregtech.api.factory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.factory.standard.StandardFactoryGrid;
import gregtech.api.factory.standard.StandardFactoryNetwork;
import it.unimi.dsi.fastutil.Pair;

/**
 * Unit tests for {@link StandardFactoryGrid} topology logic.
 * All stubs are pure Java — no Minecraft or Forge dependency.
 *
 * Covers:
 * - Basic registration / network formation
 * - Split path registering new networks in the grid (bug #1)
 * - onEdgeAdded called on both sides when an edge is added (bug #4)
 * - Component registration surviving splits and merges
 */
class StandardFactoryGridTest {

    // -------------------------------------------------------------------------
    // Stub types
    // -------------------------------------------------------------------------

    /**
     * Marker interface for components. StubElement itself implements it so that
     * {@code this} can be used as the component value — this ensures the same
     * object identity is used for both addComponentImpl and removeComponentImpl,
     * which is required for HashSet removal to work correctly.
     */
    interface ComponentMarker {
    }

    static class StubElement implements IFactoryElement<StubElement, StubNetwork, StubGrid>, ComponentMarker {

        final HashSet<StubElement> neighbors = new HashSet<>();
        StubNetwork network;

        int edgeAddedCalls = 0;
        int edgeRemovedCalls = 0;

        /** Whether this element provides a {@link ComponentMarker} component. */
        final boolean providesComponent;

        StubElement() {
            this(false);
        }

        StubElement(boolean providesComponent) {
            this.providesComponent = providesComponent;
        }

        void connect(StubElement other) {
            neighbors.add(other);
            other.neighbors.add(this);
        }

        void disconnect(StubElement other) {
            neighbors.remove(other);
            other.neighbors.remove(this);
        }

        @Override
        public void getNeighbours(Collection<StubElement> out) {
            out.addAll(neighbors);
        }

        @Override
        public StubNetwork getNetwork() {
            return network;
        }

        @Override
        public void setNetwork(StubNetwork n) {
            network = n;
        }

        @Override
        public void onEdgeAdded(StubElement adj) {
            edgeAddedCalls++;
        }

        @Override
        public void onEdgeRemoved(StubElement adj) {
            edgeRemovedCalls++;
        }

        @Override
        public List<Pair<Class<?>, Object>> getComponents() {
            // Use `this` so HashSet.remove() finds the right object by identity.
            if (providesComponent) {
                return Collections.singletonList(Pair.of(ComponentMarker.class, this));
            }
            return Collections.emptyList();
        }
    }

    static class StubNetwork extends StandardFactoryNetwork<StubNetwork, StubElement, StubGrid> {
    }

    static class StubGrid extends StandardFactoryGrid<StubGrid, StubElement, StubNetwork> {

        @Override
        protected StubNetwork createNetwork() {
            return new StubNetwork();
        }
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    StubGrid grid;

    @BeforeEach
    void setUp() {
        grid = new StubGrid();
    }

    StubElement reg(StubElement e) {
        grid.updateElement(e);
        return e;
    }

    // -------------------------------------------------------------------------
    // Basic formation
    // -------------------------------------------------------------------------

    @Test
    void isolatedElementAssignedNetwork() {
        StubElement a = reg(new StubElement());
        assertNotNull(a.getNetwork());
        assertEquals(1, grid.networks.size());
    }

    @Test
    void connectedPairSharesOneNetwork() {
        StubElement a = new StubElement(), b = new StubElement();
        a.connect(b);
        reg(a);
        reg(b);
        assertSame(a.getNetwork(), b.getNetwork());
        assertEquals(1, grid.networks.size());
    }

    @Test
    void chainOfThreeSharesOneNetwork() {
        StubElement a = new StubElement(), b = new StubElement(), c = new StubElement();
        a.connect(b);
        b.connect(c);
        reg(a);
        reg(b);
        reg(c);
        assertSame(a.getNetwork(), c.getNetwork());
        assertEquals(1, grid.networks.size());
    }

    @Test
    void splitProducesTwoTrackedNetworks() {
        StubElement a = new StubElement(), b = new StubElement(), c = new StubElement();
        a.connect(b);
        b.connect(c);
        reg(a);
        reg(b);
        reg(c);

        // Break the chain at b
        b.disconnect(a);
        b.disconnect(c);
        grid.removeElement(b);

        assertNotSame(a.getNetwork(), c.getNetwork(), "a and c should be on different networks");
        assertEquals(2, grid.networks.size(), "both split networks must be tracked in the grid");
        assertTrue(grid.networks.contains(a.getNetwork()), "a's network not tracked");
        assertTrue(grid.networks.contains(c.getNetwork()), "c's network not tracked");
        assertNull(b.getNetwork(), "removed element should have no network assigned");
        assertFalse(
            a.getNetwork()
                .getElements()
                .contains(b),
            "removed element b must not be in a's network");
        assertFalse(
            c.getNetwork()
                .getElements()
                .contains(b),
            "removed element b must not be in c's network");
    }

    @Test
    void splitNetworksContainCorrectElements() {
        StubElement a = new StubElement(), b = new StubElement(), c = new StubElement();
        a.connect(b);
        b.connect(c);
        reg(a);
        reg(b);
        reg(c);

        b.disconnect(a);
        b.disconnect(c);
        grid.removeElement(b);

        assertTrue(
            a.getNetwork()
                .getElements()
                .contains(a));
        assertFalse(
            a.getNetwork()
                .getElements()
                .contains(c));
        assertTrue(
            c.getNetwork()
                .getElements()
                .contains(c));
        assertFalse(
            c.getNetwork()
                .getElements()
                .contains(a));
    }

    @Test
    void splitWithFourElementsStillTracksAllNetworks() {
        // a - b - c - d; remove b and c, leaving a and d isolated
        StubElement a = new StubElement(), b = new StubElement(), c = new StubElement(), d = new StubElement();
        a.connect(b);
        b.connect(c);
        c.connect(d);
        reg(a);
        reg(b);
        reg(c);
        reg(d);

        b.disconnect(a);
        b.disconnect(c);
        grid.removeElement(b);
        // After removing b: a alone, c-d together
        assertEquals(2, grid.networks.size());

        c.disconnect(d);
        grid.removeElement(c);
        // After removing c: a alone, d alone
        assertEquals(2, grid.networks.size());
        assertTrue(grid.networks.contains(a.getNetwork()));
        assertTrue(grid.networks.contains(d.getNetwork()));
    }

    @Test
    void splitThenReconnectMergesBackToOneNetwork() {
        StubElement a = new StubElement(), b = new StubElement(), c = new StubElement();
        a.connect(b);
        b.connect(c);
        reg(a);
        reg(b);
        reg(c);

        b.disconnect(a);
        b.disconnect(c);
        grid.removeElement(b);
        assertNotSame(a.getNetwork(), c.getNetwork());

        // Reconnect a and c directly
        a.connect(c);
        grid.updateElement(a);

        assertSame(a.getNetwork(), c.getNetwork());
        assertEquals(1, grid.networks.size());
    }

    @Test
    void newEdgeCallsOnEdgeAddedOnBothSides() {
        StubElement a = new StubElement(), b = new StubElement();
        reg(a);
        reg(b); // isolated so far

        a.connect(b);
        grid.updateElement(a); // triggers addEdge(a, b)

        assertEquals(1, a.edgeAddedCalls, "a.onEdgeAdded not called");
        assertEquals(1, b.edgeAddedCalls, "b.onEdgeAdded not called");
        assertEquals(0, b.edgeRemovedCalls, "b.onEdgeRemoved incorrectly called on new edge");
    }

    @Test
    void removedEdgeCallsOnEdgeRemovedOnBothSides() {
        StubElement a = new StubElement(), b = new StubElement();
        a.connect(b);
        reg(a);
        reg(b);
        // reset counters after initial registration
        a.edgeAddedCalls = 0;
        b.edgeAddedCalls = 0;

        a.disconnect(b);
        grid.updateElement(a);

        assertEquals(1, a.edgeRemovedCalls, "a.onEdgeRemoved not called");
        assertEquals(1, b.edgeRemovedCalls, "b.onEdgeRemoved not called");
        assertEquals(0, a.edgeAddedCalls, "a.onEdgeAdded incorrectly called on removal");
        assertEquals(0, b.edgeAddedCalls, "b.onEdgeAdded incorrectly called on removal");
    }

    // -------------------------------------------------------------------------
    // Component registration
    // -------------------------------------------------------------------------

    @Test
    void componentRegisteredInNetworkAfterJoin() {
        StubElement a = new StubElement(true), b = new StubElement();
        a.connect(b);
        reg(a);
        reg(b);

        assertFalse(
            a.getNetwork()
                .getComponents(ComponentMarker.class)
                .isEmpty(),
            "component not registered after element joined network");
    }

    @Test
    void componentRegisteredInSplitNetworkAfterSplit() {
        StubElement a = new StubElement(true), b = new StubElement(), c = new StubElement(true);
        a.connect(b);
        b.connect(c);
        reg(a);
        reg(b);
        reg(c);

        b.disconnect(a);
        b.disconnect(c);
        grid.removeElement(b);

        // a and c in different networks — both must have their own component
        assertFalse(
            a.getNetwork()
                .getComponents(ComponentMarker.class)
                .isEmpty(),
            "a's component missing after split");
        assertFalse(
            c.getNetwork()
                .getComponents(ComponentMarker.class)
                .isEmpty(),
            "c's component missing after split");
    }

    @Test
    void removeIsolatedElementCleansUpNetworkAndGrid() {
        StubElement a = reg(new StubElement());
        StubNetwork network = a.getNetwork();
        assertEquals(1, grid.networks.size());

        grid.removeElement(a);

        assertNull(a.getNetwork(), "removed element should have no network");
        assertFalse(grid.networks.contains(network), "empty network should be removed from grid");
        assertEquals(0, grid.networks.size());
    }

    @Test
    void subsumePreservesComponentsFromBothNetworks() {
        // Register two isolated elements that both provide components
        StubElement a = reg(new StubElement(true)), b = reg(new StubElement(true));
        assertEquals(2, grid.networks.size(), "two isolated elements → two networks");

        // Connect them — triggers subsume (two adjacent networks merge)
        a.connect(b);
        grid.updateElement(a);

        assertEquals(1, grid.networks.size(), "connected elements must share one network");
        assertSame(a.getNetwork(), b.getNetwork());
        assertEquals(
            2,
            a.getNetwork()
                .getComponents(ComponentMarker.class)
                .size(),
            "both components must survive network subsume");
    }

    @Test
    void componentDeregisteredWhenElementRemoved() {
        StubElement a = new StubElement(true), b = new StubElement();
        a.connect(b);
        reg(a);
        reg(b);
        StubNetwork network = a.getNetwork();
        assertFalse(
            network.getComponents(ComponentMarker.class)
                .isEmpty());

        a.disconnect(b);
        grid.removeElement(a);

        assertNull(a.getNetwork(), "removed element should have no network");
        assertTrue(
            network.getComponents(ComponentMarker.class)
                .isEmpty(),
            "a's component should be removed from the network when a is removed");
    }
}
