package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The pilot's fake game world (programming framework, Phase C tests): records every side-effect the pilot
 * dispatches (so the "exactly once" invariants are assertable) and serves scripted target resolutions + leg
 * durations.
 */
public final class FakePilotWorld implements USSPilotWorld {

    /** The USS variable space (the pilot's WRITE/READ channel). */
    public final Map<Integer, String> variables = new HashMap<>();
    /** The still-unscanned ripple count (the RIPPLES_UNSCANNED stat). */
    public int unscannedRipples = 7;
    /**
     * Scripted resolutions: key {@code target + ":" + index} → result (absent key → unresolvable, like the real world).
     */
    public final Map<String, USSTargetResult> targets = new HashMap<>();
    /** Leg durations (work / travel). */
    public long workTicks = 40L;
    public long travelTicks = 80L;
    /** A leg start that should be refused (the executor then SKIPS the instruction). */
    public boolean refuseLegStarts = false;

    // region effect records

    public int workCalls = 0;
    /** The WORK KIND of each completed work leg (the command that ran it). */
    public final List<Integer> workLegKinds = new ArrayList<>();
    /** The TARGET kind of each completed work leg (PLANET / STAR / ...). */
    public final List<String> workKinds = new ArrayList<>();
    public final List<Integer> workIndices = new ArrayList<>();
    public final List<String> logs = new ArrayList<>();
    public int legStarts = 0;

    /** Recorded CONSTRUCT leg dispatches. */
    public int constructStartCalls = 0;
    public final List<String> constructStartKinds = new ArrayList<>();
    public final List<Integer> constructStartIndices = new ArrayList<>();
    public int constructTickCalls = 0;
    public final List<String> constructTickKinds = new ArrayList<>();
    public final List<Integer> constructTickIndices = new ArrayList<>();
    /** Recorded base REPAIR dispatches. */
    public int baseRepairStarts = 0;
    public int baseRepairTicks = 0;
    public final List<String> baseLogs = new ArrayList<>();

    // endregion

    // region scripted results (base-mode seam)

    /** The CONSTRUCT leg start result (false = nothing to construct - the command SKIPs). */
    public boolean constructStartResult = false;
    /** The CONSTRUCT leg tick result (true = still running). */
    public boolean constructTickResult = false;
    /** The base REPAIR start result. */
    public boolean repairStartResult = true;
    /** The base REPAIR tick result (true = still below max). */
    public boolean repairTickResult = true;
    /**
     * When &gt;= 0, the base REPAIR tick returns true exactly this many more times and then false (a bounded
     * repair run); -1 uses {@link #repairTickResult} forever.
     */
    public int repairTickTrueLeft = -1;

    // endregion

    @Override
    public String readVar(int slot) {
        String v = variables.get(slot);
        return v == null ? "" : v;
    }

    @Override
    public void writeVar(int slot, String value) {
        variables.put(slot, value == null ? "" : value);
    }

    @Override
    public int unscannedRipples() {
        return unscannedRipples;
    }

    @Override
    public USSTargetResult resolveTarget(String target, int index, VoidcraftActiveShip ship) {
        return targets.get((target == null ? "" : target) + ":" + index);
    }

    @Override
    public long legTicks(int workKind, VoidcraftActiveShip ship, double distance) {
        if (refuseLegStarts) {
            return 0L;
        }
        return USSWorkKind.isWork(workKind) ? workTicks : travelTicks;
    }

    @Override
    public void onWorkComplete(VoidcraftActiveShip ship, int workKind, String targetKind, int targetIndex) {
        workCalls++;
        workLegKinds.add(workKind);
        workKinds.add(targetKind == null ? "" : targetKind);
        workIndices.add(targetIndex);
    }

    @Override
    public void log(VoidcraftActiveShip ship, String message) {
        logs.add(message == null ? "" : message);
    }

    @Override
    public boolean constructStart(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        constructStartCalls++;
        constructStartKinds.add(targetKind == null ? "" : targetKind);
        constructStartIndices.add(targetIndex);
        return constructStartResult;
    }

    @Override
    public boolean constructTick(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        constructTickCalls++;
        constructTickKinds.add(targetKind == null ? "" : targetKind);
        constructTickIndices.add(targetIndex);
        return constructTickResult;
    }

    @Override
    public boolean baseRepairStart(VoidcraftActiveBase base) {
        baseRepairStarts++;
        return repairStartResult;
    }

    @Override
    public boolean baseRepairTick(VoidcraftActiveBase base) {
        baseRepairTicks++;
        if (repairTickTrueLeft >= 0) {
            if (repairTickTrueLeft == 0) {
                return false;
            }
            repairTickTrueLeft--;
            return true;
        }
        return repairTickResult;
    }

    @Override
    public void logBase(VoidcraftActiveBase base, String message) {
        baseLogs.add(message == null ? "" : message);
    }

    /** Resolve key helper (test readability). */
    public void resolve(String target, int index, USSPosition position, int bodyIndex, boolean staticBody) {
        targets.put(target + ":" + index, new USSTargetResult(position, bodyIndex, staticBody));
    }
}
