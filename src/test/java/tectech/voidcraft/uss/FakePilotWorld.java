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
    public final List<String> workKinds = new ArrayList<>();
    public final List<Integer> workIndices = new ArrayList<>();
    public final List<String> logs = new ArrayList<>();
    public int legStarts = 0;

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
    public long legTicks(boolean work, VoidcraftActiveShip ship, double distance) {
        if (refuseLegStarts) {
            return 0L;
        }
        return work ? workTicks : travelTicks;
    }

    @Override
    public void onWorkComplete(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        workCalls++;
        workKinds.add(targetKind == null ? "" : targetKind);
        workIndices.add(targetIndex);
    }

    @Override
    public void log(VoidcraftActiveShip ship, String message) {
        logs.add(message == null ? "" : message);
    }

    /** Resolve key helper (test readability). */
    public void resolve(String target, int index, USSPosition position, int bodyIndex, boolean staticBody) {
        targets.put(target + ":" + index, new USSTargetResult(position, bodyIndex, staticBody));
    }
}
