package tectech.voidcraft.uss;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidbase PILOT (Voidbase construction framework): runs the station controller program (the one digitized
 * into the blueprint) in BASE mode - the base does not fly. It mirrors the in-flight {@link USSShipPilot}: the
 * same executor, the same command handlers, the same failure-SKIP contract (and the same invisible-while
 * program wrap), but the legs are
 * ZERO-LENGTH (a base sits at its anchor; a leg completes on the next tick):
 * <ul>
 * <li>MOVE resolves only to the base OWN anchor (an instant leg in place); any other target - and HOME / SHIP -
 * is unresolvable (SKIP).</li>
 * <li>MINE with mining power runs a REAL mining leg (the ship's duration table: {@code mineTicks(mining power)});
 * MINE without mining power is an instant no-op leg (v1: a station produces no cargo). SCAN and SIPHON are
 * instant no-op legs on a base (a station cannot scan or siphon - those yields belong to the ships).</li>
 * <li>REPAIR runs at the station (energy from its own buffer).</li>
 * <li>CONSTRUCT SKIPs (a base carries no parts - a build loadout belongs to the Constructor in flight).</li>
 * <li>a non-empty program runs forever (the executor's invisible while) - it ends only on a STOP (or an empty
 * program); at the end the base HOLDS its course (it stays; only integrity decay decommissions it).</li>
 * </ul>
 *
 * <p>
 * Bare-JVM except the world seam (like the ship pilot).
 */
public final class USSBasePilot implements USSExecutionContext {

    /** The pilot NBT key nested inside the base NBT (same pattern as the ship pilot). */
    public static final String TAG_PILOT = "vc_pilot";

    private static final String TAG_EXEC = "vc_exec";
    private static final String TAG_LEG_ACTIVE = "vc_leg_active";
    private static final String TAG_LEG_DONE = "vc_leg_done";
    private static final String TAG_LEG_WORK_KIND = "vc_leg_work_kind";
    private static final String TAG_MINE_TICKS = "vc_mine_ticks";
    private static final String TAG_MINE_TOTAL = "vc_mine_total";
    private static final String TAG_MINE_ID = "vc_mine_id";

    private final VoidcraftActiveBase base;
    private final USSProgram program;
    private USSProgramExecutor executor;
    private final USSPilotWorld world;
    private Random rng;

    // The leg (zero-length on a base - completes on the next tick; the latch is consumed exactly once).
    private boolean legActive;
    private boolean legDone;
    /** The leg in flight (or the last one)'s work kind (see {@link USSWorkKind}; TRAVEL = a travel leg). */
    private int legWorkKind = USSWorkKind.TRAVEL;
    private boolean legDoneReported;

    // The active mining leg (a WORK on a base with mining power): ticks left, the leg's total, and the leg id
    // (monotonic; the client animates the mining beam from it). 0 = no active mining leg.
    private int miningTicks;
    private int miningTotal;
    private int miningLegId;

    private USSBasePilot(VoidcraftActiveBase base, USSProgram program, USSProgramExecutor executor,
        USSPilotWorld world) {
        this.base = base;
        this.program = program;
        this.executor = executor;
        this.world = world;
    }

    /**
     * Create a fresh pilot for a spawned base.
     *
     * @param base    the base (its seed drives the target-picking RNG)
     * @param program the station program (null - empty program - the base holds its course; a non-empty program
     *                repeats forever until a STOP)
     * @param world   the game seam (the MTE)
     */
    public static USSBasePilot create(VoidcraftActiveBase base, USSProgram program, USSPilotWorld world) {
        USSProgram p = (program == null) ? USSProgram.empty() : program;
        USSBasePilot pilot = new USSBasePilot(base, p, USSProgramExecutor.start(p), world);
        pilot.rng = (base.seed() == 0) ? new Random() : new Random(base.seed());
        return pilot;
    }

    /**
     * Re-attach a pilot to a RESTORED base (chunk reload): the program comes from the base payload
     * ({@code vc_program}), the executor cursor + the leg bookkeeping from the pilot NBT nested under
     * {@link #TAG_PILOT}. A missing/corrupt pilot NBT degrades to a FRESH pilot (a corrupt cursor fails safe to
     * COMPLETED - the base holds, never a half-run).
     *
     * @param base    the restored base
     * @param world   the game seam
     * @param baseNbt the base NBT tag (may lack {@link #TAG_PILOT} - fresh pilot then)
     */
    public static USSBasePilot attach(VoidcraftActiveBase base, USSPilotWorld world, NBTTagCompound baseNbt) {
        NBTTagCompound payload = base.payload();
        NBTTagList list = (payload != null && payload.hasKey(VoidcraftNbt.TAG_PROGRAM))
            ? payload.getTagList(VoidcraftNbt.TAG_PROGRAM, 10)
            : null;
        USSProgram program = USSProgram.readFromNBT(list);
        USSBasePilot pilot = new USSBasePilot(base, program, USSProgramExecutor.start(program), world);
        pilot.rng = new Random(base.seed());
        if (baseNbt != null) {
            Object pilotRaw = baseNbt.getTag(TAG_PILOT);
            if (pilotRaw instanceof NBTTagCompound) {
                NBTTagCompound p = (NBTTagCompound) pilotRaw;
                try {
                    Object execRaw = p.getTag(TAG_EXEC);
                    pilot.executor = (execRaw instanceof NBTTagCompound)
                        ? USSProgramExecutor.readFromNBT((NBTTagCompound) execRaw)
                        : USSProgramExecutor.readFromNBT(null);
                    pilot.legActive = p.getBoolean(TAG_LEG_ACTIVE);
                    pilot.legDone = p.getBoolean(TAG_LEG_DONE);
                    pilot.legWorkKind = p.getInteger(TAG_LEG_WORK_KIND);
                    pilot.miningTicks = p.getInteger(TAG_MINE_TICKS);
                    pilot.miningTotal = p.getInteger(TAG_MINE_TOTAL);
                    pilot.miningLegId = p.getInteger(TAG_MINE_ID);
                } catch (RuntimeException ex) {
                    // A corrupt pilot NBT must never crash the chunk load: fail SAFE to a finished program -
                    // the base HOLDS (never a half-run, never a double-run).
                    pilot.executor = USSProgramExecutor.readFromNBT(null);
                    pilot.legActive = false;
                    pilot.legDone = false;
                    pilot.legWorkKind = USSWorkKind.TRAVEL;
                    pilot.miningTicks = 0;
                    pilot.miningTotal = 0;
                    pilot.miningLegId = 0;
                }
            }
        }
        return pilot;
    }

    // region tick

    /**
     * Advance the base one game tick (call every tick while the base exists).
     */
    public void tick() {
        // 1) the leg counts down in REAL time (a base leg is zero-length - it completes on this tick; a mining
        // leg counts down over its real duration).
        if (miningTicks > 0) {
            miningTicks--;
        }
        if (legActive && !legDone) {
            legDone = miningTicks <= 0;
        }

        // 2) consume a just-completed leg EXACTLY ONCE.
        if (legActive && legDone) {
            legActive = false;
            legDone = false;
            int kind = legWorkKind;
            legWorkKind = USSWorkKind.TRAVEL;
            if (executor.isCompleted()) {
                world.logBase(base, "leg finished after program end - holding course");
                legDoneReported = false;
                miningTicks = 0; // an abandoned leg never keeps the beam burning
                miningTotal = 0;
                return;
            }
            if (kind != USSWorkKind.TRAVEL) {
                if (kind == USSWorkKind.MINE) {
                    if (miningTotal > 0) {
                        // The mining leg just ran out (v1: a station produces no cargo - the beam is the yield).
                        world.logBase(base, "MINE mining leg complete (the station produced no cargo)");
                        miningTotal = 0;
                    } else {
                        // MINE without mining power: an instant no-op (v1 - a station produces no cargo).
                        world.logBase(base, "MINE: no mining power - the base idles at its anchor");
                    }
                } else {
                    // SCAN / SIPHON on a base: instant no-op (a station cannot scan or siphon - those yields
                    // belong to the ships).
                    world.logBase(
                        base,
                        USSWorkKind.name(kind) + ": a station cannot "
                            + USSWorkKind.name(kind)
                                .toLowerCase()
                            + " - the base idles at its anchor");
                }
            }
            legDoneReported = true; // the active MOVE/WORK command observes it on the same tick
        }

        // 3) the executor steps (one node transition per 20 ticks; active commands polled every tick).
        executor.tick(this);

        // 4) program over (a STOP ran - or the program was empty) - the base HOLDS its course (it stays; only
        // integrity decay decommissions it). An in-flight mining leg is abandoned (the beam stops).
        if (executor.isCompleted()) {
            legDoneReported = false;
            miningTicks = 0;
            miningTotal = 0;
            world.logBase(base, "base program over - holding course");
        }
    }

    // endregion

    // region USSExecutionContext (base mode - the executor game seam)

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
            case CARGO_USED:
            case CARGO_FREE:
            case CARGO_FULL:
                return "0"; // a station carries no cargo (v1)
            case STATE:
                return "BASE";
            case TARGET:
                return anchorTarget();
            case POSITION_X:
            case POSITION_Y:
            case POSITION_Z: {
                USSPosition pos = base.position();
                if (pos == null) {
                    return "0";
                }
                switch (stat) {
                    case POSITION_X:
                        return String.valueOf(pos.x());
                    case POSITION_Y:
                        return String.valueOf(pos.y());
                    default:
                        return String.valueOf(pos.z());
                }
            }
            case DIST_TO_TARGET:
                return "0"; // a base sits at its anchor
            case SPEED:
                return "0"; // a base does not fly
            case TICKS_IN_LEG:
                return String.valueOf(miningTicks); // a zero-length leg has no remaining ticks
            case RIPPLES_UNSCANNED:
                return String.valueOf(world.unscannedRipples());
            default:
                return "";
        }
    }

    /** The base anchor as a target descriptor (the TARGET stat: STAR / PLANET:i / RIPPLE:j). */
    private String anchorTarget() {
        USSBaseAnchor anchor = base.anchor();
        if (anchor.isStar()) {
            return USSProgramDefaults.TARGET_STAR;
        }
        if (anchor.isPlanet()) {
            return USSProgramDefaults.TARGET_PLANET + ":" + anchor.index();
        }
        return USSProgramDefaults.TARGET_RIPPLE + ":" + anchor.index();
    }

    @Override
    public USSPosition position() {
        return base.position();
    }

    @Override
    public double distanceTo(USSPosition dest) {
        return (dest == null) ? 0.0
            : base.position()
                .distanceTo(dest);
    }

    @Override
    public USSPosition resolveMoveTarget(String target, int index) {
        // A base is immobile: only a target that resolves to its OWN anchor is reachable (a zero-length leg
        // in place). Anything else - other bodies, HOME, SHIP - is unresolvable (the MOVE SKIPs).
        USSBaseAnchor anchor = USSBaseAnchor.fromMoveTarget(target, index);
        if (anchor == null || !anchor.equals(base.anchor())) {
            return null;
        }
        return base.position();
    }

    @Override
    public boolean startLeg(USSPosition dest, double dist, int workKind) {
        if (dest == null) {
            return false;
        }
        // Zero-length leg: it completes on the next tick (the base does not move).
        legActive = true;
        legDone = false;
        legWorkKind = workKind;
        legDoneReported = false;
        if (workKind == USSWorkKind.MINE) {
            // MINE with mining power: a REAL mining leg, the same duration table the ship mines with
            // (mineTicks: MINE_TICKS_MAX / min(power, saturation), clamped to the 90..600 tick window).
            long power = VoidcraftNbt.readLong(base.payload(), VoidcraftNbt.TAG_MINING);
            if (power > 0) {
                miningTotal = (int) USSConstants.mineTicks(power);
                miningTicks = miningTotal;
                miningLegId++;
                world.logBase(base, "MINE - mining leg (mining power " + power + ", ~" + (miningTotal / 20) + "s)");
            }
        }
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
        // A base carries no parts (a build loadout belongs to the Constructor in flight) - the command SKIPs.
        return false;
    }

    @Override
    public boolean constructTick() {
        return false; // constructStart never RUNNING on a base
    }

    @Override
    public boolean repairStart() {
        return world.baseRepairStart(base);
    }

    @Override
    public boolean repairTick() {
        return world.baseRepairTick(base);
    }

    @Override
    public void log(String message) {
        world.logBase(base, message);
    }

    // endregion

    // region accessors (tests / MTE)

    public VoidcraftActiveBase getBase() {
        return base;
    }

    public USSProgram getProgram() {
        return program;
    }

    /** Ticks left in the active mining leg (0 = none). */
    public int miningTicks() {
        return miningTicks;
    }

    /** The active mining leg's total duration (0 = none). */
    public int miningTotal() {
        return miningTotal;
    }

    /** The monotonic mining-leg id (the client's beam animation key; 0 = never mined). */
    public int miningLegId() {
        return miningLegId;
    }

    /**
     * @return true when the program has finished (the base is holding its course)
     */
    public boolean isCompleted() {
        return executor.isCompleted();
    }

    /**
     * @return the pilot NBT (the MTE nests it under {@link #TAG_PILOT} in the base NBT)
     */
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(TAG_EXEC, executor.writeToNBT());
        nbt.setBoolean(TAG_LEG_ACTIVE, legActive);
        nbt.setBoolean(TAG_LEG_DONE, legDone);
        nbt.setInteger(TAG_LEG_WORK_KIND, legWorkKind);
        nbt.setInteger(TAG_MINE_TICKS, miningTicks);
        nbt.setInteger(TAG_MINE_TOTAL, miningTotal);
        nbt.setInteger(TAG_MINE_ID, miningLegId);
        return nbt;
    }

    // endregion

    @Override
    public String toString() {
        return "USSBasePilot[base=" + base.name() + ", executor=" + executor.state() + "]";
    }
}
