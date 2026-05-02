package gregtech.api.factory;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import gregtech.api.factory.routing.NetworkStep;
import gregtech.api.factory.routing.NetworkVisitor;
import gregtech.api.factory.routing.StepQueue;
import gregtech.api.factory.routing.VisitorResult;
import it.unimi.dsi.fastutil.Pair;

public class GraphRouteTracker<TElement extends IFactoryElement<TElement, TNetwork, TGrid>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TGrid>, TGrid extends IFactoryGrid<TGrid, TElement, TNetwork>, TNotable extends INotableFactoryElement<TNotable, TRouteInfo>, TRouteInfo extends IRouteInfo<TRouteInfo>> {

    private static final RoutedNode[] EMPTY_NODE_ARRAY = new RoutedNode[0];

    public final HashMap<TNotable, RoutedNode<TNotable, TRouteInfo>[]> edges = new HashMap<>();

    private final Class<TNotable> notableType;
    private final Set<TNotable> notableElements = new HashSet<>();

    private final TRouteInfo zero;

    public GraphRouteTracker(Class<TNotable> notableType, TRouteInfo zero) {
        this.notableType = notableType;
        this.zero = zero;
    }

    public void onElementAdded(TElement element) {
        if (!notableType.isAssignableFrom(element.getClass())) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.add(notableElement);
    }

    public void onElementRemoved(TElement element) {
        if (!notableType.isAssignableFrom(element.getClass())) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.remove(notableElement);
    }

    public void updateEdges() {
        edges.clear();

        for (TNotable notableElement : notableElements) {
            // noinspection unchecked
            edges.put(
                notableElement,
                notableElement.getRoutedNeighbours()
                    .toArray(EMPTY_NODE_ARRAY));
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

    public void iterateNetworkBFS(TNotable start, NetworkVisitor<TNotable, TRouteInfo> visitor) {
        StepQueue<TNotable, TRouteInfo> queue = new StepQueue<>();

        queue.add(new NetworkStep<>(start, zero, null));

        NetworkStep<TNotable, TRouteInfo> step;

        while ((step = queue.takeFront()) != null) {
            VisitorResult result = visitor.visit(step);

            if (result == VisitorResult.Break) break;
            if (result == VisitorResult.SkipNode) continue;

            for (var edge : edges.get(step.node())) {
                if (!step.contains(edge.element())) {
                    queue.add(
                        new NetworkStep<>(
                            edge.element(),
                            step.route()
                                .copy()
                                .merge(edge.routeInfo()),
                            step));
                }
            }
        }
    }

    public FactoryRoutes<TNotable, TRouteInfo> dijkstra(TNotable start) {
        return dijkstra(start, null);
    }

    public FactoryRoutes<TNotable, TRouteInfo> dijkstra(TNotable start,
        Predicate<RoutedNode<TNotable, TRouteInfo>> filter) {
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
                if (filter != null && !filter.test(edge)) continue;

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
