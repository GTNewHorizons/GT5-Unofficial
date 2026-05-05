package gregtech.api.factory;

import java.util.List;

public interface INotableFactoryElement<TSelf extends INotableFactoryElement<TSelf, TRouteInfo>, TRouteInfo extends IRouteInfo<TRouteInfo>> {

    /** Exactly the same as getConnections, but with some routing metadata. */
    List<RoutedNode<TSelf, TRouteInfo>> getRoutedNeighbours();
}
