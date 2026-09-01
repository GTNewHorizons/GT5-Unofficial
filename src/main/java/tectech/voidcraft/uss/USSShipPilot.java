package tectech.voidcraft.uss;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * The per-ship PILOT (programming framework, Phase C) — the brain of a Voidcraft fleet entity: the in-flight
 * ship and the anchored Voidbase run the SAME pilot (a non-null anchor on the ship marks the base).
 *
 * <p>
 * The pilot owns one entity's program: it runs the {@link USSProgramExecutor} (Phase B) against the entity, and
 * it implements the executor's {@link USSExecutionContext} game seam by bridging to the {@link USSPilotWorld}
 * (the MTE: USS variable space, ripple field, fleet, cargo, logging). Bare-JVM throughout (NBT + primitives +
 * these seams), so the full program-driven mission loop is unit-testable with a fake world
 * (see {@code USSShipPilotTest}).
 *
 * <p>
 * Tick model (one {@link #tick()} per game tick, called by the MTE's fleet loop):
 * <ol>
 * <li>the energy buffer recharges, and the active leg counts down ONLY while the buffer covers the leg's energy
 * draw (the stall model — an exhausted buffer pauses the leg, not the program; the executor keeps polling);</li>
 * <li>a leg that just completed is consumed EXACTLY ONCE (its side-effect — a WORK leg's cargo/reveal, or a
 * HOME leg's delivery — fires here, then the completion is handed to the executor as
 * {@link #legComplete()} on the same tick);</li>
 * <li>the executor steps (it only transitions a node on its 20-tick boundary);</li>
 * <li>when the program ends (a STOP ran — or the program was empty) the ship HOLDS
 * ({@link USSShipState#HOVERING}) — no implicit return (user decision); a finished program wraps and runs
 * again (the executor's invisible while), so only STOP ends it.</li>
 * </ol>
 *
 * <p>
 * Persistence: the pilot's state (the executor's cursor + the in-flight leg's bookkeeping + the last resolved
 * target) serializes into the SHIP's NBT under {@code vc_pilot} (the MTE composes it — see
 * {@link MTEUnstableSolarSystem}), so a ship saved mid-leg / mid-loop resumes EXACTLY where it stopped: the
 * side-effect fires exactly once across a reload (the leg-complete latch is persisted with the leg).
 */
public final class USSShipPilot implements USSExecutionContext {

    /** The tag the MTE nests the pilot under in the ship's NBT. */
    public static final String TAG_PILOT = "vc_pilot";
    private static final String TAG_EXEC = "vc_exec";
    private static final String TAG_LEG_WORK_KIND = "vc_leg_work_kind";
    private static final String TAG_LEG_HOME = "vc_leg_home";
    private static final String TAG_LAST_KIND = "vc_last_kind";
    private static final String TAG_LAST_INDEX = "vc_last_index";
    private static final String TAG_ORIGIN = "vc_origin";

    private final VoidcraftActiveShip ship;
    private final USSProgram program;
    private final USSPilotWorld world;
    /** The ship's deterministic RNG (re-seeded on attach from the ship's per-launch seed). */
    private Random rng;
    private USSProgramExecutor executor;

    /** The ship's launch origin (fleet-anchor coordinates) — the HOME destination. */
    private USSPosition origin;

    // region leg bookkeeping (persisted — the leg's side-effect must fire exactly once across a reload)

    /** The leg in flight (or the last one)'s work kind (see {@link USSWorkKind}; TRAVEL = a travel leg). */
    private int legWorkKind = USSWorkKind.TRAVEL;
    /** The leg in flight (or the last one) was the HOME leg (MOVE HOME — delivery on completion). */
    private boolean legHome;
    /**
     * A {@code MOVE HOME} was resolved but its leg not started yet (consumed by the next {@link #startLeg}).
     * Runtime only (never persists in a meaningful state).
     */
    private boolean pendingHome;
    /**
     * A leg completed and the executor has not observed it yet (runtime only — set when the leg completes, read
     * by the executor's {@link #legComplete()} on the same tick, reset by the next leg start / hold).
     */
    private boolean legDoneReported;

    // endregion

    /** The last resolved MOVE target kind (the MOVE {@code target} string) — the WORK leg's side-effect key. */
    private String lastKind = "";
    /** The last resolved MOVE target's body index (planet i / ripple i / -1 star / -2 ship). */
    private int lastIndex = -1;

    private USSShipPilot(VoidcraftActiveShip ship, USSProgram program, USSProgramExecutor executor,
        USSPilotWorld world) {
        this.ship = ship;
        this.program = program;
        this.executor = executor;
        this.world = world;
        this.rng = new Random();
    }

    // region lifecycle

    /**
     * Create a fresh pilot for a launched ship.
     *
     * @param ship    the ship (its position at launch — the HOME origin)
     * @param program the program (null → empty program → the ship HOLDS at the origin)
     * @param world   the game seam (the MTE)
     * @param seed    the ship's per-launch seed (the pilot's target-picking RNG — deterministic per launch)
     */
    public static USSShipPilot create(VoidcraftActiveShip ship, USSProgram program, USSPilotWorld world, int seed) {
        USSProgram p = (program == null) ? USSProgram.empty() : program;
        USSShipPilot pilot = new USSShipPilot(ship, p, USSProgramExecutor.start(p), world);
        pilot.rng = (seed == 0) ? new Random() : new Random(seed);
        pilot.origin = ship.getPosition();
        if (pilot.executor.isCompleted()) {
            ship.hold(); // nothing to run — the ship holds at the origin
        }
        return pilot;
    }

    /**
     * Re-attach a pilot to a RESTORED ship (chunk reload): the program comes from the ship's payload
     * ({@code vc_program}), the executor's cursor + the leg bookkeeping from the pilot NBT the MTE nested under
     * {@link #TAG_PILOT}. A missing/corrupt pilot NBT degrades to a FRESH pilot (a corrupt cursor fails safe to
     * COMPLETED — the ship holds, never a half-run).
     *
     * @param ship    the restored ship
     * @param world   the game seam (the MTE)
     * @param shipNbt the ship's NBT tag (may lack {@link #TAG_PILOT} — fresh pilot then)
     */
    public static USSShipPilot attach(VoidcraftActiveShip ship, USSPilotWorld world, NBTTagCompound shipNbt) {
        NBTTagCompound payload = ship.getPayload();
        NBTTagList list = (payload != null && payload.hasKey(VoidcraftNbt.TAG_PROGRAM))
            ? payload.getTagList(VoidcraftNbt.TAG_PROGRAM, 10)
            : null;
        USSProgram program = USSProgram.readFromNBT(list);
        USSShipPilot pilot = new USSShipPilot(ship, program, USSProgramExecutor.start(program), world);
        pilot.rng = new Random(ship.getSeed());
        pilot.origin = ship.getPosition();
        NBTTagCompound p = null;
        if (shipNbt != null) {
            Object pilotRaw = shipNbt.getTag(TAG_PILOT);
            if (pilotRaw instanceof NBTTagCompound) {
                p = (NBTTagCompound) pilotRaw;
            }
        }
        if (p != null) {
            try {
                Object execRaw = p.getTag(TAG_EXEC);
                pilot.executor = (execRaw instanceof NBTTagCompound)
                    ? USSProgramExecutor.readFromNBT((NBTTagCompound) execRaw)
                    : USSProgramExecutor.readFromNBT(null); // fail-safe COMPLETED (a missing cursor = the program is
                                                            // over)
                if (p.hasKey(TAG_LEG_WORK_KIND)) {
                    pilot.legWorkKind = p.getInteger(TAG_LEG_WORK_KIND);
                }
                if (p.hasKey(TAG_LEG_HOME)) {
                    pilot.legHome = p.getBoolean(TAG_LEG_HOME);
                }
                if (p.hasKey(TAG_LAST_KIND)) {
                    pilot.lastKind = p.getString(TAG_LAST_KIND);
                }
                if (p.hasKey(TAG_LAST_INDEX)) {
                    pilot.lastIndex = p.getInteger(TAG_LAST_INDEX);
                }
                Object originRaw = p.getTag(TAG_ORIGIN);
                if (originRaw instanceof NBTTagCompound) {
                    pilot.origin = USSPosition.readFromNBT((NBTTagCompound) originRaw);
                }
            } catch (RuntimeException ex) {
                // A corrupt pilot NBT (wrongly-typed fields, a nested class-cast) must never crash the chunk load:
                // fail SAFE to a finished program — the ship HOLDS (never a half-run, never a double-run).
                pilot.executor = USSProgramExecutor.readFromNBT(null);
                pilot.legWorkKind = USSWorkKind.TRAVEL;
                pilot.legHome = false;
                pilot.lastKind = "";
                pilot.lastIndex = -1;
            }
        }
        if (pilot.executor.isCompleted()) {
            ship.hold();
        }
        return pilot;
    }

    // endregion

    // region tick

    /**
     * Advance the ship one game tick (call every tick while the ship exists).
     *
     * @return true when this tick's leg was the HOME leg and just completed — the ship's mission is OVER (the
     *         caller delivers it and removes it); false otherwise
     */
    public boolean tick() {
        // 1) the leg counts down in REAL time, on the entity's energy buffer AND (travel legs on a fuel-burning
        // engine) its fuel tank: it ticks only while both cover the leg's draw (the stall model — the executor
        // keeps polling, the leg just does not advance).
        ship.tickEnergy();
        if (ship.isLegActive()) {
            long energyDraw = USSConstants.legEnergyDraw(ship.getLegWorkKind(), ship);
            long fuelDraw = USSConstants.travelFuelDraw(ship.getLegWorkKind(), ship);
            if (ship.spendEnergy(energyDraw) && ship.spendFuel(fuelDraw)) {
                ship.tickLeg();
            }
        }

        // 2) consume a just-completed leg EXACTLY ONCE (its side-effect, then the executor observes it).
        if (ship.isLegComplete()) {
            int kind = legWorkKind;
            boolean home = legHome;
            ship.clearLegComplete(); // position = the leg's endpoint; the latch is cleared
            if (executor.isCompleted()) {
                // The program is already over (corrupt-cursor edge): the leg is abandoned — the ship holds.
                ship.hold();
                world.log(ship, "leg finished after program end — holding in place");
                legDoneReported = false;
                return false;
            }
            if (home) {
                // MOVE HOME completed — the mission is over (delivery + re-emission is the MTE's job).
                world.log(ship, "HOME leg complete — returning to the gateway");
                legDoneReported = false;
                return true;
            }
            if (kind != USSWorkKind.TRAVEL) {
                // The work leg's yield — keyed by the leg's WORK KIND (owned by the work command) — EXACTLY ONCE
                // (the latch was just cleared). A base never flew (MOVE is force-refused on it), so its work legs
                // complete with the ANCHOR descriptor, not a last-MOVE target.
                if (ship.isBase()) {
                    USSBaseAnchor anchor = ship.getAnchor();
                    world.onWorkComplete(ship, kind, anchor.targetKind(), anchor.index());
                } else {
                    world.onWorkComplete(ship, kind, lastKind, lastIndex);
                }
            }
            legDoneReported = true; // the active MOVE/WORK command observes it on the same tick
        }

        // 3) the executor steps (one node transition per 20 ticks; active commands polled every tick).
        executor.tick(this);

        // 4) program over (a STOP ran — or the program was empty) → the ship HOLDS (no implicit return).
        if (executor.isCompleted()) {
            legDoneReported = false;
            ship.hold();
            world.log(ship, "program over — holding in place");
        }
        return false;
    }

    // endregion

    // region USSExecutionContext (the executor's game seam — bridged to the ship + world)

    @Override
    public String resolve(USSValue value) {
        if (value == null) {
            return "";
        }
        switch (value.kind()) {
            case LITERAL:
                return value.literal();
            case VAR:
                return world.readVar(value.slot());
            case STAT:
                return stat(USSShipStat.byId(value.statId()));
            case LOCATION:
                return position().coordString();
            default:
                return "";
        }
    }

    @Override
    public String readVar(int slot) {
        return world.readVar(slot);
    }

    @Override
    public void writeVar(int slot, String value) {
        world.writeVar(slot, value);
    }

    @Override
    public String stat(USSShipStat stat) {
        if (stat == null) {
            return "";
        }
        switch (stat) {
            case CARGO_USED: {
                CargoHold hold = ship.getHold();
                return String.valueOf(hold == null ? 0 : hold.usedUnits());
            }
            case CARGO_FREE: {
                CargoHold hold = ship.getHold();
                return String.valueOf(hold == null ? 0 : hold.remainingUnits());
            }
            case CARGO_FULL: {
                CargoHold hold = ship.getHold();
                return (hold != null && hold.isFull()) ? "1" : "0";
            }
            case STATE:
                return ship.isBase() ? "BASE"
                    : ship.getState()
                        .name();
            case TARGET:
                if (ship.isBase()) {
                    USSBaseAnchor anchor = ship.getAnchor();
                    return anchor.isStar() ? USSProgramDefaults.TARGET_STAR
                        : anchor.targetKind() + ":" + anchor.index();
                }
                if (lastKind == null || lastKind.isEmpty()) {
                    return "";
                }
                return lastIndex >= 0 ? lastKind + ":" + lastIndex : lastKind;
            case POSITION_X:
                return String.valueOf(
                    ship.getPosition()
                        .x());
            case POSITION_Y:
                return String.valueOf(
                    ship.getPosition()
                        .y());
            case POSITION_Z:
                return String.valueOf(
                    ship.getPosition()
                        .z());
            case DIST_TO_TARGET: {
                USSPosition dest = ship.getDestination();
                return (dest == null) ? "0"
                    : String.valueOf(
                        ship.getPosition()
                            .distanceTo(dest));
            }
            case SPEED:
                return String.valueOf(ship.getSpeed());
            case TICKS_IN_LEG:
                return String.valueOf(ship.getTicksInLeg());
            case INTEGRITY:
                return String.valueOf(ship.getIntegrity());
            case RIPPLES_UNSCANNED:
                return String.valueOf(world.unscannedRipples());
            case LOGISTICS_POWER:
                return String.valueOf(ship.getLogisticsPower());
            default:
                return "";
        }
    }

    @Override
    public USSPosition position() {
        return ship.getPosition();
    }

    @Override
    public double distanceTo(USSPosition dest) {
        return (dest == null) ? 0.0
            : ship.getPosition()
                .distanceTo(dest);
    }

    @Override
    public USSPosition resolveMoveTarget(String target, int index) {
        // The anchor forces the refusal (see the entity contract): a base's MOVE legs SKIP and the program
        // continues.
        if (ship.getAnchor() != null) {
            log("MOVE: anchored at " + ship.getAnchor() + " — cannot move");
            return null;
        }
        // HOME resolves to the launch origin — the ship itself knows it (the world seam never sees it).
        if (USSProgramDefaults.TARGET_HOME.equals(target)) {
            pendingHome = true;
            world.log(ship, "MOVE HOME — origin " + origin);
            return origin;
        }
        USSTargetResult result = world.resolveTarget(target, index, ship);
        if (result == null || result.getPosition() == null) {
            return null;
        }
        pendingHome = false;
        // Remember the target (the WORK leg's side-effect key) + the client's body (hover target).
        lastKind = (target == null) ? "" : target;
        lastIndex = result.getIndex();
        ship.setTargetPlanet(result.getIndex() >= 0 ? result.getIndex() : -1);
        ship.setBodyStatic(result.isStaticBody());
        return result.getPosition();
    }

    @Override
    public boolean startLeg(USSPosition dest, double dist, int workKind) {
        // The power gates (the capability system, runtime truth): a leg the craft cannot do is refused here, so
        // the command SKIPs and the program continues (a MOVE without thrusters, a MINE without mining power, …).
        if (!USSWorkKind.isWork(workKind)) {
            if (ship.getSpeed() <= 0.0) {
                log("MOVE: no thrusters — skipping");
                return false;
            }
        } else if (workKind == USSWorkKind.MINE && ship.getMiningPower() <= 0) {
            log("MINE: no mining power — skipping");
            return false;
        } else if (workKind == USSWorkKind.SCAN && ship.getScanPower() <= 0) {
            log("SCAN: no scan power — skipping");
            return false;
        } else if (workKind == USSWorkKind.SIPHON && ship.getStarlifterPower() <= 0) {
            log("SIPHON: no siphon power — skipping");
            return false;
        }
        long ticks = world.legTicks(workKind, ship, dist);
        if (ticks <= 0 || dest == null) {
            return false;
        }
        USSShipState state = USSWorkKind.isWork(workKind) ? USSShipState.MINING
            : (pendingHome ? USSShipState.RETURNING : USSShipState.OUTBOUND);
        USSPosition from = ship.getPosition();
        ship.startLeg(state, from, dest, (int) ticks, Math.max(0.0, dist), workKind);
        legWorkKind = workKind;
        legHome = !USSWorkKind.isWork(workKind) && pendingHome;
        pendingHome = false;
        legDoneReported = false;
        return true;
    }

    @Override
    public boolean legComplete() {
        return legDoneReported;
    }

    @Override
    public int nextInt(int bound) {
        return (bound <= 0) ? 0 : rng.nextInt(bound);
    }

    @Override
    public boolean constructStart() {
        return world.constructStart(ship, lastKind, lastIndex);
    }

    @Override
    public boolean constructTick() {
        return world.constructTick(ship, lastKind, lastIndex);
    }

    @Override
    public boolean repairStart(String target) {
        if (ship.getAnchor() == null) {
            // REPAIR is a station command: only an anchored Voidbase runs it (in its own program).
            log("REPAIR: not a station — skipping");
            return false;
        }
        return world.repairStart(ship, target);
    }

    @Override
    public boolean repairTick() {
        return world.repairTick(ship);
    }

    @Override
    public boolean transferStart(int commandId, String target, long amount, String filter) {
        return world.cargoTransferStart(ship, commandId, target, amount, filter);
    }

    @Override
    public boolean transferTick(int commandId) {
        return world.cargoTransferTick(ship, commandId);
    }

    @Override
    public boolean stabilizeStart(long ticks) {
        if (ship.getAnchor() == null) {
            // STABILIZE is a station command: only an anchored Voidbase runs it (in its own program).
            log("STABILIZE: not a station - skipping");
            return false;
        }
        return world.stabilizeStart(ship, ticks);
    }

    @Override
    public boolean stabilizeTick() {
        return world.stabilizeTick(ship);
    }

    @Override
    public void log(String message) {
        world.log(ship, message);
    }

    // endregion

    // region accessors (tests / MTE)

    public VoidcraftActiveShip getShip() {
        return ship;
    }

    public USSProgram getProgram() {
        return program;
    }

    /**
     * @return true when the program has finished (the ship is holding — or was just delivered via {@code MOVE HOME}).
     */
    public boolean isCompleted() {
        return executor.isCompleted();
    }

    public USSProgramExecutor getExecutor() {
        return executor;
    }

    public USSPosition getOrigin() {
        return origin;
    }

    public String getLastKind() {
        return lastKind;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    /**
     * The id of the entity's in-flight MINE work leg (the base's mining-beam identity for the fleet render
     * anchor) — 0 when no mining leg is active.
     */
    public int miningLegId() {
        return (ship.isLegActive() && legWorkKind == USSWorkKind.MINE) ? ship.getLegId() : 0;
    }

    /**
     * @return the pilot's NBT (the MTE nests it under {@link #TAG_PILOT} in the ship's NBT)
     */
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(TAG_EXEC, executor.writeToNBT());
        nbt.setInteger(TAG_LEG_WORK_KIND, legWorkKind);
        nbt.setBoolean(TAG_LEG_HOME, legHome);
        nbt.setString(TAG_LAST_KIND, lastKind);
        nbt.setInteger(TAG_LAST_INDEX, lastIndex);
        if (origin != null) {
            NBTTagCompound o = new NBTTagCompound();
            origin.writeToNBT(o);
            nbt.setTag(TAG_ORIGIN, o);
        }
        return nbt;
    }

    // endregion

    @Override
    public String toString() {
        return "USSShipPilot[ship=" + ship
            .getName() + ", state=" + ship.getState() + ", executor=" + executor.state() + "]";
    }
}
