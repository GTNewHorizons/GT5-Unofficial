package gregtech.api.factory;

import gregtech.api.factory.standard.StandardFactoryGrid;

/**
 * A factory grid is the global coordinator for your factory pipe system. Grids will create and destroy networks as
 * elements join or leave the world. You usually want to extend {@link StandardFactoryGrid}, not this.
 */
public interface IFactoryGrid<TSelf extends IFactoryGrid<TSelf, TElement, TNetwork>, TElement extends IFactoryElement<TElement, TNetwork, TSelf>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TSelf>> {

    /**
     * Adds an element and its edges to the grid. Removes any connections the element previously had but no longer has.
     * The element will always have a network after this returns.
     */
    void updateElement(TElement element);

    /**
     * Removes an element and its edges from the grid. The element will not have a network after this returns.
     */
    void removeElement(TElement element);
}
