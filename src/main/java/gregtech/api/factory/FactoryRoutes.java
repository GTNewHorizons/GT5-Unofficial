package gregtech.api.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FactoryRoutes<TNotable, TRouteInfo extends IRouteInfo<TRouteInfo>>
    implements Iterable<FactoryPath<TNotable, TRouteInfo>> {

    public final TNotable start;
    public final Map<TNotable, GraphRouteTracker.From<TNotable, TRouteInfo>> dijkstra;

    private List<FactoryPath<TNotable, TRouteInfo>> byDistance;

    public FactoryRoutes(TNotable start, Map<TNotable, GraphRouteTracker.From<TNotable, TRouteInfo>> dijkstra) {
        this.start = start;
        this.dijkstra = dijkstra;
    }

    public @Nullable FactoryPath<TNotable, TRouteInfo> getPath(TNotable end) {
        List<TNotable> path = new ArrayList<>();

        path.add(end);

        TNotable curr = end;

        while (curr != start && curr != null) {
            var from = dijkstra.get(curr);

            if (from == null) return null;

            path.add(from.previousNode);
            curr = from.previousNode;
        }

        Collections.reverse(path);

        return new FactoryPath<>(start, end, dijkstra.get(end).routeInfo, path);
    }

    private void populateDistances() {
        if (byDistance == null) byDistance = new ArrayList<>();

        if (!byDistance.isEmpty()) return;

        for (var e : dijkstra.entrySet()) {
            byDistance.add(new FactoryPath<>(start, e.getKey(), e.getValue().routeInfo) {

                boolean loaded = false;

                List<TNotable> path2;

                @Override
                public List<TNotable> getPath() {
                    if (!loaded) {
                        loaded = true;

                        var p = FactoryRoutes.this.getPath(e.getKey());

                        path2 = p == null ? null : p.getPath();
                    }

                    return path2;
                }
            });
        }

        byDistance.sort(FactoryPath::compareTo);
    }

    @Override
    public @NotNull Iterator<FactoryPath<TNotable, TRouteInfo>> iterator() {
        populateDistances();

        return byDistance.iterator();
    }
}
