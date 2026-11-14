package tectech.mechanics.boseEinsteinCondensate;

import org.jetbrains.annotations.NotNull;

import gregtech.api.factory.IRouteInfo;

public class BECRouteInfo implements IRouteInfo<BECRouteInfo> {

    public int distance;

    public BECRouteInfo(int distance) {
        this.distance = distance;
    }

    @Override
    public BECRouteInfo merge(BECRouteInfo other) {
        return new BECRouteInfo(distance + other.distance);
    }

    @Override
    public BECRouteInfo copy() {
        return new BECRouteInfo(distance);
    }

    @Override
    public int compareTo(@NotNull BECRouteInfo o) {
        return Integer.compare(distance, o.distance);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof BECRouteInfo that)) return false;

        return distance == that.distance;
    }

    @Override
    public int hashCode() {
        return distance;
    }

    @Override
    public String toString() {
        return "BECRouteInfo{" + "distance=" + distance + '}';
    }
}
