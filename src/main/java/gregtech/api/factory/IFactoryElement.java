package gregtech.api.factory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gregtech.api.factory.standard.StandardFactoryNetwork;
import it.unimi.dsi.fastutil.Pair;

/**
 * Represents a pipe, hatch, multi, or any other machine in a factory pipe system. You should create a new interface
 * that extends this one, then specify your network and grid in the IFactoryElement generics.
 */
public interface IFactoryElement<TSelf extends IFactoryElement<TSelf, TNetwork, TGrid>, TNetwork extends IFactoryNetwork<TNetwork, TSelf, TGrid>, TGrid extends IFactoryGrid<TGrid, TSelf, TNetwork>> {

    /**
     * Detects all adjacent elements, regardless of what network they're on.
     */
    void getNeighbours(Collection<TSelf> neighbours);

    TNetwork getNetwork();

    void setNetwork(TNetwork network);

    default void onEdgeAdded(TSelf adjacent) {
        onEdgeChanged(adjacent);
    }

    default void onEdgeRemoved(TSelf adjacent) {
        onEdgeChanged(adjacent);
    }

    default void onEdgeChanged(TSelf adjacent) {

    }

    /**
     * A component is an object provided by this element. Generally the component implementation is just {@code this},
     * but it can be anything. In a {@link StandardFactoryNetwork}, components are grouped by their interface and can be
     * queried by the same interface. Components are useful if you want to expose something network-wide so that any
     * element can find it.
     *
     * @return A list of component interfaces and their implementations.
     */
    default List<Pair<Class<?>, Object>> getComponents() {
        return Collections.emptyList();
    }
}
