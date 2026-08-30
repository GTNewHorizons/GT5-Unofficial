package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fake {@link USSExecutionContext} — the bare-JVM stand-in for the game side.
 * Deterministic; counts the calls the tests assert pacing/failure semantics against.
 */
public final class FakeUSSContext implements USSExecutionContext {

    /** The USS variable space (immutable — writeVar replaces it). */
    public USSVariableSpace vars = USSVariableSpace.fresh();
    /** Stat overrides (anything not listed reads as {@code ""}). */
    public final Map<USSShipStat, String> stats = new EnumMap<USSShipStat, String>(USSShipStat.class);
    /**
     * Test hook: when true, {@code stat(CARGO_FREE)} returns the current value and DECREMENTS it — simulates the
     * world changing (cargo filling up) as the program polls. Lets a WHILE-terminated-by-stat test be
     * deterministic.
     */
    public int cargoFree = 0;
    public boolean autoDecrementCargoFree = false;
    /** The ship's position (for distanceTo + WORK). */
    public USSPosition position = USSPosition.zero();
    /** MOVE targets: key {@code target + "#" + index} (or {@code target + "#-1"} for any index). */
    public final Map<String, USSPosition> targets = new HashMap<String, USSPosition>();
    public int resolveCalls = 0;
    public int writeVarCalls = 0;
    public boolean legStarted = false;
    public boolean legComplete = false;
    /** Test hook: refuse leg starts (the executor must SKIP the instruction). */
    public boolean startRefused = false;
    public int travelLegs = 0;
    public int workLegs = 0;
    public USSPosition lastLegDest;
    public double lastLegDist;
    public int lastLegWorkKind;
    public final List<String> log = new ArrayList<String>();
    /** nextInt(bound) = bound ≤ 0 ? 0 : randomBase % bound (deterministic). */
    public int randomBase = 7;
    /** CONSTRUCT / REPAIR seam results (the Voidbase construction framework). */
    public boolean constructStartResult = true;
    public int constructStartCalls = 0;
    public boolean constructTickResult = true;
    public int constructTickCalls = 0;
    public boolean repairStartResult = true;
    public int repairStartCalls = 0;
    /** The target param of the last REPAIR start (empty / SELF = the executor itself). */
    public String repairStartTarget = "";
    public boolean repairTickResult = true;
    public int repairTickCalls = 0;
    /** SEND / TAKE seam results + the args of the last start (the cargo transfer framework). */
    public boolean transferStartResult = true;
    public int transferStartCalls = 0;
    public int transferStartCommandId;
    public String transferStartTarget;
    public long transferStartAmount = -1L;
    public String transferStartFilter;
    public boolean transferTickResult = true;
    public int transferTickCalls = 0;

    public void setTarget(String target, int index, USSPosition dest) {
        targets.put(target + "#" + index, dest);
    }

    public boolean loggedContains(String fragment) {
        for (String line : log) {
            if (line.contains(fragment)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String resolve(USSValue value) {
        resolveCalls++;
        if (value == null) {
            return "";
        }
        switch (value.kind()) {
            case VAR:
                return readVar(value.slot());
            case STAT:
                return stat(USSShipStat.byId(value.statId()));
            case LOCATION:
                return position().coordString();
            case LITERAL:
            default:
                return value.literal();
        }
    }

    @Override
    public String readVar(int slot) {
        return vars.get(slot);
    }

    @Override
    public void writeVar(int slot, String value) {
        writeVarCalls++;
        vars = vars.set(slot, value);
    }

    @Override
    public String stat(USSShipStat stat) {
        if (stat == USSShipStat.CARGO_FREE && autoDecrementCargoFree) {
            int current = cargoFree;
            if (current > 0) {
                cargoFree = current - 1;
            }
            return String.valueOf(current);
        }
        if (stat == null) {
            return "";
        }
        String value = stats.get(stat);
        return value == null ? "" : value;
    }

    @Override
    public USSPosition position() {
        return position;
    }

    @Override
    public double distanceTo(USSPosition dest) {
        return position.distanceTo(dest == null ? USSPosition.zero() : dest);
    }

    @Override
    public USSPosition resolveMoveTarget(String target, int index) {
        if (target == null) {
            return null;
        }
        USSPosition dest = targets.get(target + "#" + index);
        if (dest != null) {
            return dest;
        }
        return targets.get(target + "#-1");
    }

    @Override
    public boolean startLeg(USSPosition dest, double dist, int workKind) {
        if (startRefused) {
            return false;
        }
        legStarted = true;
        lastLegDest = dest;
        lastLegDist = dist;
        lastLegWorkKind = workKind;
        if (USSWorkKind.isWork(workKind)) {
            workLegs++;
        } else {
            travelLegs++;
        }
        return true;
    }

    @Override
    public boolean legComplete() {
        return legComplete;
    }

    @Override
    public int nextInt(int bound) {
        return bound <= 0 ? 0 : randomBase % bound;
    }

    @Override
    public boolean constructStart() {
        constructStartCalls++;
        return constructStartResult;
    }

    @Override
    public boolean constructTick() {
        constructTickCalls++;
        return constructTickResult;
    }

    @Override
    public boolean repairStart(String target) {
        repairStartCalls++;
        repairStartTarget = target == null ? "" : target;
        return repairStartResult;
    }

    @Override
    public boolean repairTick() {
        repairTickCalls++;
        return repairTickResult;
    }

    @Override
    public boolean transferStart(int commandId, String target, long amount, String filter) {
        transferStartCalls++;
        transferStartCommandId = commandId;
        transferStartTarget = target;
        transferStartAmount = amount;
        transferStartFilter = filter;
        return transferStartResult;
    }

    @Override
    public boolean transferTick(int commandId) {
        transferTickCalls++;
        return transferTickResult;
    }

    @Override
    public void log(String message) {
        log.add(message);
    }
}
