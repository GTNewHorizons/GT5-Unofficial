package gregtech.api.factory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import gregtech.api.factory.routing.NetworkStep;
import gregtech.api.factory.routing.NetworkVisitor;
import gregtech.api.factory.routing.StepQueue;
import gregtech.api.factory.routing.VisitorResult;

public class GraphRouteTracker<TElement extends IFactoryElement<TElement, TNetwork, TGrid>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TGrid>, TGrid extends IFactoryGrid<TGrid, TElement, TNetwork>, TNotable extends INotableFactoryElement<TNotable, TRouteInfo>, TRouteInfo extends IRouteInfo<TRouteInfo>> {

    @SuppressWarnings("rawtypes")
    private static final RoutedNode[] EMPTY_NODE_ARRAY = new RoutedNode[0];

    private final HashMap<TNotable, RoutedNode<TNotable, TRouteInfo>[]> edges = new HashMap<>();
    private final Class<TNotable> notableType;
    private final Set<TNotable> notableElements = new HashSet<>();

    private final TRouteInfo zero;

    private boolean networkChanged = false;

    public GraphRouteTracker(Class<TNotable> notableType, TRouteInfo zero) {
        this.notableType = notableType;
        this.zero = zero;
    }

    public void invalidateRoutes() {
        networkChanged = true;
    }

    public void onElementAdded(TElement element) {
        if (!notableType.isAssignableFrom(element.getClass())) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.add(notableElement);

        invalidateRoutes();
    }

    public void onElementRemoved(TElement element) {
        if (!notableType.isAssignableFrom(element.getClass())) return;

        TNotable notableElement = notableType.cast(element);

        notableElements.remove(notableElement);

        invalidateRoutes();
    }

    public void updateEdgesIfNeeded() {
        if (networkChanged) {
            networkChanged = false;

            updateEdges();
        }
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

    public RoutedNode<TNotable, TRouteInfo>[] getEdges(TNotable from) {
        updateEdgesIfNeeded();

        return edges.get(from);
    }

    public static class From<TNotable, TRouteInfo> {

        public TNotable previousNode;
        public TRouteInfo routeInfo;

        public From(TNotable previousNode, TRouteInfo routeInfo) {
            this.previousNode = previousNode;
            this.routeInfo = routeInfo;
        }
    }

    private static <TNotable, TRouteInfo> RoutedNode<TNotable, TRouteInfo>[] emptyNodeArray() {
        // noinspection unchecked
        return EMPTY_NODE_ARRAY;
    }

    public void iterateNetworkBFS(TNotable start, NetworkVisitor<TNotable, TRouteInfo> visitor) {
        updateEdgesIfNeeded();

        StepQueue<TNotable, TRouteInfo> queue = new StepQueue<>();

        queue.add(new NetworkStep<>(start, zero, null));

        NetworkStep<TNotable, TRouteInfo> step;

        while ((step = queue.takeFront()) != null) {
            VisitorResult result = visitor.visit(step);

            if (result == VisitorResult.Break) break;
            if (result == VisitorResult.SkipNode) continue;

            for (var edge : edges.getOrDefault(step.node(), emptyNodeArray())) {
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
}
