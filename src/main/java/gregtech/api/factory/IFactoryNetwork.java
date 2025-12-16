package gregtech.api.factory;

import java.util.Collection;

import gregtech.api.factory.standard.StandardFactoryNetwork;

/**
 * A factory network is a logical group of factory elements. You usually want to extend {@link StandardFactoryNetwork},
 * not this.
 */
public interface IFactoryNetwork<TSelf extends IFactoryNetwork<TSelf, TElement, TGrid>, TElement extends IFactoryElement<TElement, TSelf, TGrid>, TGrid extends IFactoryGrid<TGrid, TElement, TSelf>> {

    public void addElement(TElement element);

    public void removeElement(TElement element);

    public default void onNetworkRemoved() {

    }

    public default void onNetworkSubsumedPre(TSelf subsumer) {

    }

    public default void onNetworkSubsumedPost(TSelf subsumer) {

    }

    public Collection<TElement> getElements();
}
