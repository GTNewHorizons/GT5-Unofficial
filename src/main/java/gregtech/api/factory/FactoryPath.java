package gregtech.api.factory;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

public class FactoryPath<TNotable, TRouteInfo extends IRouteInfo<TRouteInfo>>
    implements Comparable<FactoryPath<TNotable, TRouteInfo>> {

    public final TNotable start, end;
    public final TRouteInfo routeInfo;
    private List<TNotable> path;

    public FactoryPath(TNotable start, TNotable end, TRouteInfo routeInfo) {
        this.start = start;
        this.end = end;
        this.routeInfo = routeInfo;
    }

    public FactoryPath(TNotable start, TNotable end, TRouteInfo routeInfo, List<TNotable> path) {
        this.start = start;
        this.end = end;
        this.routeInfo = routeInfo;
        this.path = path;
    }

    public List<TNotable> getPath() {
        return path;
    }

    @Override
    public int compareTo(@NotNull FactoryPath<TNotable, TRouteInfo> o) {
        return routeInfo.compareTo(o.routeInfo);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof FactoryPath<?, ?>that)) return false;

        return Objects.equals(start, that.start) && Objects.equals(end, that.end)
            && Objects.equals(routeInfo, that.routeInfo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(start);
        result = 31 * result + Objects.hashCode(end);
        result = 31 * result + Objects.hashCode(routeInfo);
        return result;
    }

    @Override
    public String toString() {
        return "FactoryPath{" + "start=" + start + ", end=" + end + ", routeInfo=" + routeInfo + '}';
    }
}
