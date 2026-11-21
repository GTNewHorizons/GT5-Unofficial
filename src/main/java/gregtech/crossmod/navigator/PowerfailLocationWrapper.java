package gregtech.crossmod.navigator;

import java.util.Locale;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizons.navigator.api.model.locations.IWaypointAndLocationProvider;
import com.gtnewhorizons.navigator.api.model.waypoints.Waypoint;

import gregtech.common.data.GTPowerfailTracker;

public class PowerfailLocationWrapper implements IWaypointAndLocationProvider {

    public GTPowerfailTracker.Powerfail powerfail;

    private boolean activeAsWaypoint = false;

    public String mteName;
    public boolean highlighted = true;

    public PowerfailLocationWrapper(GTPowerfailTracker.Powerfail powerfail) {
        update(powerfail);
    }

    public void update(GTPowerfailTracker.Powerfail powerfail) {
        this.powerfail = powerfail;
        this.mteName = powerfail.getMTEName()
            .toLowerCase(Locale.ROOT);
    }

    @Override
    public Waypoint toWaypoint() {
        return new Waypoint(
            powerfail.x,
            powerfail.y,
            powerfail.z,
            powerfail.dim,
            powerfail.toSummary()
                .toString(),
            0xFF3333);
    }

    @Override
    public boolean isActiveAsWaypoint() {
        return activeAsWaypoint;
    }

    @Override
    public void onWaypointCleared() {
        activeAsWaypoint = false;
    }

    @Override
    public void onWaypointUpdated(Waypoint waypoint) {
        activeAsWaypoint = waypoint.dimensionId == powerfail.dim && waypoint.blockX == powerfail.x
            && waypoint.blockY == powerfail.y
            && waypoint.blockZ == powerfail.z;
    }

    @Override
    public int getDimensionId() {
        return powerfail.dim;
    }

    @Override
    public double getBlockX() {
        return powerfail.x;
    }

    @Override
    public double getBlockZ() {
        return powerfail.z;
    }

    @Override
    public long toLong() {
        return CoordinatePacker.pack(powerfail.x, powerfail.y, powerfail.z);
    }
}
