package gregtech.api.factory;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import gregtech.api.objects.TimedLRUCache;
import it.unimi.dsi.fastutil.Pair;

public class GraphRouteTracker<TElement extends IFactoryElement<TElement, TNetwork, TGrid>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TGrid>, TGrid extends IFactoryGrid<TGrid, TElement, TNetwork>, TNotable extends INotableFactoryElement<TNotable, TRouteInfo>, TRouteInfo extends IRouteInfo<TRouteInfo>> {

    public final SetMultimap<TNotable, RoutedNode<TNotable, TRouteInfo>> edges = MultimapBuilder.hashKeys()
        .hashSetValues()
        .build();

    private final Class<TNotable> notableType;
    private final Set<TNotable> notableElements = new HashSet<>();

    private final TRouteInfo zero;

    private final TimedLRUCache<TNotable, FactoryRoutes<TNotable, TRouteInfo>> routeCache = new TimedLRUCache<>(
        this::dijkstraUncached,
        20 * 60,
        1024);

    public GraphRouteTracker(Class<TNotable> notableType, TRouteInfo zero) {
        this.notableType = notableType;
        this.zero = zero;
    }

    public void onElementAdded(TElement element) {
        if (!(notableType.isAssignableFrom(element.getClass()))) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.add(notableElement);

        invalidateRoutes();
    }

    public void onElementRemoved(TElement element) {
        if (!(notableType.isAssignableFrom(element.getClass()))) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.remove(notableElement);

        invalidateRoutes();
    }

    public void invalidateRoutes() {
        routeCache.clear();
    }

    public void updateEdges() {
        edges.clear();

        for (TNotable notableElement : notableElements) {
            for (RoutedNode<TNotable, TRouteInfo> edge : notableElement.getRoutedNeighbours()) {
                edges.put(notableElement, edge);
            }
        }
    }

    public static class From<TNotable, TRouteInfo> {

        public TNotable previousNode;
        public TRouteInfo routeInfo;

        public From(TNotable previousNode, TRouteInfo routeInfo) {
            this.previousNode = previousNode;
            this.routeInfo = routeInfo;
        }
    }

    public FactoryRoutes<TNotable, TRouteInfo> dijkstra(TNotable start) {
        return dijkstraUncached(start);
        // return routeCache.get(start);
    }

    private FactoryRoutes<TNotable, TRouteInfo> dijkstraUncached(TNotable start) {
        ArrayDeque<Pair<TNotable, TRouteInfo>> queue = new ArrayDeque<>();
        HashSet<TNotable> visited = new HashSet<>();
        HashMap<TNotable, From<TNotable, TRouteInfo>> result = new HashMap<>();

        queue.add(Pair.of(start, null));
        result.put(start, new From<>(start, zero.copy()));

        while (!queue.isEmpty()) {
            var c = queue.pop();
            TNotable current = c.left();
            TRouteInfo route = c.right();

            if (!visited.add(current)) continue;

            for (var edge : edges.get(current)) {
                var totalRoute = route == null ? edge.routeInfo() : route.merge(edge.routeInfo());

                From<TNotable, TRouteInfo> existing = result.get(edge.element());

                if (existing != null) {
                    if (existing.routeInfo.compareTo(totalRoute) <= 0) continue;
                }

                result.put(edge.element(), new From<>(current, totalRoute));
                queue.add(Pair.of(edge.element(), totalRoute));
            }
        }

        return new FactoryRoutes<>(start, result);
    }
}
