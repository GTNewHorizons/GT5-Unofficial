package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidcraft in the Unstable Solar System: the single active fleet entity — an in-flight ship and an anchored
 * Voidbase are the same object (a non-null {@link USSBaseAnchor} marks the base).
 *
 * <p>
 * The entity knows ONE thing: its current leg. A leg has a state ({@link USSShipState#OUTBOUND} a travel leg to a
 * body, {@link USSShipState#MINING} a work leg, {@link USSShipState#RETURNING} the travel leg home), a start
 * point, an endpoint, a duration, and a countdown. {@link #startLeg} arms it, {@link #tickLeg} counts it down,
 * {@link #isLegComplete()} latches completion (consumed exactly once via {@link #clearLegComplete()} by the
 * pilot — its side-effect fires exactly once, even across a save/reload), and {@link #hold()} parks the ship
 * ({@link USSShipState#HOVERING} — the program finished without a return; user decision: no implicit MOVE HOME).
 *
 * <p>
 * A base (anchor != null) never flies: the pilot refuses its MOVE legs, and the USS re-computes its position
 * from the live anchor each tick (a planet anchor orbits). Its location (the shared-location rule) derives from
 * the anchor's body: planet → the planet orbit zone, star → the star, ripple → the fixed ripple point.
 *
 * <p>
 * Bare-JVM (NBT + primitives only) so the ship + pilot stay unit-testable without Forge. Cargo is built externally
 * (see {@link USSShipCargo}) and handed in via {@link #setCargo(NBTTagCompound)} on the WORK leg's completion —
 * this class stays free of {@code Materials} so tests run without a live ore dictionary.
 *
 * <p>
 * The INTEGRITY is the time limit: it is set to the maximum (the blueprint's integrity) when the entity enters
 * the USS, drops by 1 per second while in the USS ({@link #tickIntegrity()}), and at 0 the entity is LOST
 * (removed from the USS, its cargo discarded — no delivery, no re-emission; a base is decommissioned the same
 * way). A SHIP that finishes its program before the limit expires survives: its item is re-emitted into the
 * gateway's output bus (dropped at the USS when the bus cannot absorb it), with its integrity back at maximum for
 * the next flight. A base never completes a mission (it cannot MOVE) — it stands until its integrity runs out.
 *
 * <p>
 * ENERGY: every action runs on the entity's energy buffer (capacity from the blueprint's power cells, generation
 * from its solar covers — the payload's denormalized stats). The buffer starts full; {@link #tickEnergy()}
 * recharges it each tick; an action spends from it via {@link #spendEnergy(long)} and STALLS (no progress) while
 * the buffer cannot cover the draw.
 */
public final class VoidcraftActiveShip {

    private static final String TAG_UUID = "vc_uuid";
    private static final String TAG_NAME = "vc_name";
    private static final String TAG_SPEED = "vc_speed";
    private static final String TAG_MINING = "vc_mining";
    private static final String TAG_INTEGRITY = "vc_integrity";
    private static final String TAG_INTEG_TICKS = "vc_integ_ticks";
    private static final String TAG_STATE = "vc_state";
    private static final String TAG_TICKS = "vc_ticks";
    private static final String TAG_LEG = "vc_leg";
    private static final String TAG_CARGO = "vc_cargo";
    private static final String TAG_HOLD = "vc_hold";
    private static final String TAG_PAYLOAD = "vc_payload";
    private static final String TAG_GATEWAY = "vc_gateway";
    private static final String TAG_BAY = "vc_bay";
    private static final String TAG_SEED = "vc_seed";
    private static final String TAG_TARGET = "vc_target";
    private static final String TAG_DEST = "vc_dest";
    private static final String TAG_TDIST = "vc_tdist";
    private static final String TAG_POS = "vc_pos";
    private static final String TAG_LEG_FROM = "vc_leg_from";
    private static final String TAG_LEG_TOTAL = "vc_leg_total";
    private static final String TAG_LEG_ID = "vc_leg_id";
    private static final String TAG_LEG_WORK_KIND = "vc_leg_work_kind";
    private static final String TAG_LEG_ACTIVE = "vc_leg_active";
    private static final String TAG_LEG_DONE = "vc_leg_done";
    private static final String TAG_BODY_STATIC = "vc_body_static";
    private static final String TAG_BUILD_LOADOUT = "vc_build_parts";
    private static final String TAG_ANCHOR = "vc_anchor";
    private static final String TAG_ENERGY = "vc_energy";

    /**
     * Pass 27 (user: "the cargo hold size should be increased by a factor of 100 — as currently the mining is
     * basically instant"): the hold capacity is the blueprint's {@code cargoSlots} times this. 100× makes a trip
     * carry a real haul instead of filling (and thus completing) near-instantly. Applied at the single capacity
     * source ({@link #cargoCapacity()}) so the hold, its NBT, and the derived delivery cargo all scale together.
     */
    public static final long CARGO_UNIT_MULTIPLIER = 100L;

    /**
     * The integrity time limit drops by 1 every {@code TICKS_PER_INTEGRITY} ticks — 20 = ONE game second, so the
     * ship's integrity is exactly its survival budget in SECONDS while it is in the USS.
     */
    public static final int TICKS_PER_INTEGRITY = 20;

    /**
     * The integrity a spawned base falls back to when its payload carries no usable integrity stat (a stale or
     * corrupt blueprint item): a base never spawns at 0 (it would decommission on its first tick).
     */
    public static final long DEFAULT_INTEGRITY = 60L;

    private final String uuid;
    private final String name;
    private final double speed;
    private final long miningPower;
    private final NBTTagCompound payload;
    private final int[] gatewayPos;
    private final int[] bayPos;
    /**
     * Per-launch identity (pass 5.1): assigned by the USS at launch, unique per flight even for DUPLICATED ship
     * items (creative copies share the item's {@code vc_uuid}). The client keys each ship's animation phase and
     * its swarm-spread spot ({@code USSFleetOrbit}) on this — duplicated items must not share one spot or one
     * leg-progress counter. 0 = unset (legacy save; the client falls back to the item UUID).
     */
    private final int seed;

    /**
     * The body descriptor the CLIENT hovers at (programming framework, Phase C): a system planet index, a
     * ripple-point index, or {@code -1} for the star (no body yet — a fresh ship at the origin). Set by the pilot
     * when a MOVE target resolves (the ship's hover body for the leg and the work leg that follows).
     */
    /** The hover body descriptor (the pilot sets it when a MOVE target resolves). -1 = star / none. */
    private int targetPlanet = -1;

    /**
     * True when the hover body is a FIXED point (ripple / ship rendezvous) — the client hovers at the resolved
     * destination.
     */
    private boolean bodyStatic;

    private USSShipState state;

    /**
     * The ship's INTEGRITY (the time-limit pass): its survival budget while in the USS. Set to the ship's maximum
     * (the blueprint's integrity, from the payload) when the ship enters the USS, then drops by 1 per second
     * ({@link #tickIntegrity()}). At 0 the ship is lost (removed, cargo discarded).
     */
    private long integrity;

    /** Ticks until the next integrity drop (re-armed to {@link #TICKS_PER_INTEGRITY} after each drop). */
    private int integrityTimer = TICKS_PER_INTEGRITY;

    // region the base anchor (non-null = an anchored Voidbase, not a flying ship)

    /**
     * The Voidbase anchor (null = a flying ship): the body the station is built around. The station's position is
     * re-computed from the live anchor each tick, and its location (the shared-location rule) derives from the
     * anchor's body (see {@link #getTargetPlanet()} / {@link #isBodyStatic()}).
     */
    private USSBaseAnchor anchor;

    // endregion

    // region the energy buffer (every action runs on it)

    /** The energy buffer content in EU (starts full — a freshly built ship or station). */
    private long energy;
    /** The buffer capacity in EU (the blueprint's power cells, from the payload's {@code vc_energy_buffer}). */
    private final long energyCapacity;
    /** The buffer generation in EU/tick (the blueprint's solar covers, from the payload's {@code vc_energy_gen}). */
    private final long energyGen;

    // endregion

    // region the current leg (a passive countdown — the pilot arms + consumes it)

    private int ticksRemaining;
    private int legTotal;
    private boolean legActive;
    /** Latched the moment the countdown hits zero — consumed exactly once via {@link #clearLegComplete()}. */
    private boolean legDone;
    /**
     * The current leg's start point (the ship's position when the leg began — the client lerps from it on a
     * travel leg).
     */
    private USSPosition legFrom;
    /** The current leg's endpoint (the point the ship flies to / works at). */
    private USSPosition destination;
    /** The current leg's distance in blocks (the client derives the same leg duration the server ticks). */
    private double travelDistance;
    /**
     * The ship's CURRENT position in the solar system (fleet-anchor coordinates): its launch origin at launch,
     * then the last leg's endpoint. The client renders the ship here while it HOLDS
     * ({@link USSShipState#HOVERING}).
     */
    private USSPosition position;
    /**
     * Monotonic leg counter (per ship, persisted) — the leg ID. The client resets its leg-progress phase when
     * this changes, so consecutive legs of the SAME state (MOVE → MOVE) animate from their own start instead of
     * continuing the previous leg's progress.
     */
    private int legId;

    // endregion

    private NBTTagCompound cargo;

    /**
     * The ship's internal cargo hold (the cargo-capacity pass): a bounded hold (capacity in cargo units, where
     * {@code 1 unit = 1 item = 100 mB}) that mining / starlifting / construction fill. The hold is the ship's
     * STATE — it is empty at launch, filled by the mission (clamped by capacity), and delivered on return. A full
     * hold means the ship "cannot mine if it is full" (the yield is 0).
     *
     * <p>
     * The {@link #cargo} NBT (the abstract items + fluids for delivery) is DERIVED from this hold — see
     * {@link #getCargo()} / {@link #setCargo(NBTTagCompound)}.
     */
    private CargoHold hold;

    /**
     * The parts loadout a CONSTRUCTOR carries (parts-list key → count on board): filled by the gateway at launch
     * (from the payload's {@code vc_build_loadout}), consumed PART-BY-PART by the CONSTRUCT legs — a site takes
     * only what it needs, and whatever is left stays on board (for the next station, or the return to the gateway
     * when the mission completes).
     */
    private final java.util.LinkedHashMap<String, Long> buildLoadout = new java.util.LinkedHashMap<>();

    private VoidcraftActiveShip(String uuid, String name, double speed, long miningPower, NBTTagCompound payload,
        int[] gatewayPos, int[] bayPos, int seed) {
        this.uuid = uuid;
        this.name = name;
        this.speed = speed;
        this.miningPower = miningPower;
        this.payload = payload;
        this.gatewayPos = gatewayPos;
        this.bayPos = bayPos;
        this.seed = seed;
        this.state = USSShipState.HOVERING;
        // The internal cargo hold (the cargo-capacity pass): empty, capacity from cargoCapacity() (the payload's
        // vc_cargo × CARGO_UNIT_MULTIPLIER — the pass-27 100× hold).
        if (payload != null) {
            this.hold = CargoHold.of(cargoCapacity());
            readBuildLoadoutFromPayload(payload);
        }
        // The integrity time limit: the ship enters the USS at its MAXIMUM (the blueprint's total) and counts
        // down from there (tickIntegrity()).
        this.integrity = maxIntegrity();
        // The energy buffer: capacity + generation from the payload's denormalized stats, content starts FULL.
        this.energyCapacity = (payload != null)
            ? Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_ENERGY_BUFFER))
            : 0L;
        this.energyGen = (payload != null) ? Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_ENERGY_GEN))
            : 0L;
        this.energy = this.energyCapacity;
    }

    /**
     * Spawn a completed Voidbase at its anchor (a base is ONLY constructed in-USS — never launched from a
     * gateway): HOLDING at the anchor's hover point, legs unarmed, integrity at maximum (a payload without a
     * usable integrity stat falls back to {@link #DEFAULT_INTEGRITY} — a base never spawns at 0), energy buffer
     * full.
     *
     * @param uuid     base identity (the blueprint item's {@code vc_uuid})
     * @param name     base display name
     * @param payload  the digitized base payload (blueprint grid + derived stats + the controller program)
     * @param anchor   the body the station stands around
     * @param seed     the identity seed (the client's per-base key: the mining-beam phase, the rendezvous nudge)
     * @param position the anchor's hover point (the base's starting position)
     */
    public static VoidcraftActiveShip spawnBase(String uuid, String name, NBTTagCompound payload, USSBaseAnchor anchor,
        int seed, USSPosition position) {
        long mining = (payload != null) ? VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING) : 0L;
        VoidcraftActiveShip ship = new VoidcraftActiveShip(uuid, name, 0.0, mining, payload, null, null, seed);
        ship.anchor = (anchor == null) ? USSBaseAnchor.star() : anchor;
        USSPosition p = (position == null) ? USSPosition.zero() : position;
        ship.position = p;
        ship.legFrom = p;
        long max = ship.maxIntegrity();
        ship.integrity = (max > 0L) ? max : DEFAULT_INTEGRITY;
        return ship;
    }

    /**
     * Create a newly launched ship: HOLDING at its launch origin (fleet-anchor coordinates — the gateway's
     * anchor-relative position), legs unarmed. The pilot (programming framework, Phase C) runs the ship's program
     * and arms its legs; a ship with no program (or an empty one) simply holds.
     *
     * @param uuid        ship identity (the item's {@code vc_uuid})
     * @param name        ship display name
     * @param speed       ship speed in [0, 1] (denormalized from the item NBT)
     * @param miningPower mining power (denormalized from the item NBT)
     * @param payload     the ship payload — the item's tag compound (vc_* keys at its top level), kept so the ship can
     *                    be re-emitted as an item on return and the client can render the ship model
     * @param gatewayPos  launching gateway world position (the RETURNING endpoint + where a surviving ship is
     *                    re-emitted); may be null (drop-at-USS fallback)
     * @param bayPos      storage-bay world position (the cargo delivery target); may be null (drop-at-USS fallback)
     * @param seed        the per-launch identity seed (the client's per-ship key)
     * @param origin      the launch origin in fleet-anchor coordinates (null → {@code (0,0,0)})
     */
    public static VoidcraftActiveShip launch(String uuid, String name, double speed, long miningPower,
        NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed, USSPosition origin) {
        VoidcraftActiveShip ship = new VoidcraftActiveShip(
            uuid,
            name,
            speed,
            miningPower,
            payload,
            gatewayPos == null ? null : gatewayPos.clone(),
            bayPos == null ? null : bayPos.clone(),
            seed);
        USSPosition o = (origin == null) ? USSPosition.zero() : origin;
        ship.position = o;
        ship.legFrom = o;
        return ship;
    }

    public String getUuid() {
        return uuid;
    }

    /** @return the per-launch identity seed (0 = unset/legacy — the client uses the item UUID instead). */
    public int getSeed() {
        return seed;
    }

    /**
     * @return the hover body descriptor: a system planet index, a ripple-point index, or {@code -1} (star / none).
     *         An anchored base always reads its anchor's body (a star anchor reads -1).
     */
    public int getTargetPlanet() {
        if (anchor != null) {
            return anchor.isStar() ? -1 : anchor.index();
        }
        return targetPlanet;
    }

    /** Set the hover body descriptor (the pilot does this when a MOVE target resolves). */
    public void setTargetPlanet(int targetPlanet) {
        this.targetPlanet = targetPlanet;
    }

    /**
     * @return true when the client hovers the ship at the fixed resolved destination (ripple / ship rendezvous).
     *         An anchored base is static exactly when its anchor is a ripple point.
     */
    public boolean isBodyStatic() {
        if (anchor != null) {
            return anchor.isRipple();
        }
        return bodyStatic;
    }

    /** Set the static-body flag (the pilot sets it when a MOVE target resolves). */
    public void setBodyStatic(boolean bodyStatic) {
        this.bodyStatic = bodyStatic;
    }

    /** @return the base anchor (null = a flying ship). */
    public USSBaseAnchor getAnchor() {
        return anchor;
    }

    /** @return true when this entity is an anchored Voidbase (not a flying ship). */
    public boolean isBase() {
        return anchor != null;
    }

    /** @return the energy buffer content in EU. */
    public long getEnergy() {
        return energy;
    }

    /** @return the energy buffer capacity in EU. */
    public long getEnergyCapacity() {
        return energyCapacity;
    }

    /** @return the energy generation in EU/tick. */
    public long getEnergyGen() {
        return energyGen;
    }

    /**
     * Advance the energy buffer one world tick: generation is added, clamped at capacity (a zero-capacity buffer
     * stays at 0). Call once per tick while the entity is in the USS.
     */
    public void tickEnergy() {
        if (energyGen > 0L && energy < energyCapacity) {
            energy = Math.min(energyCapacity, energy + energyGen);
        }
    }

    /**
     * Spend energy from the buffer (the stall model's gate): the draw is paid only when the buffer covers it.
     *
     * @param amount the draw in EU (&lt;= 0 is a free no-op)
     * @return true when the draw was paid (false = the action must STALL: no progress this tick)
     */
    public boolean spendEnergy(long amount) {
        if (amount <= 0L) {
            return true;
        }
        if (energy < amount) {
            return false;
        }
        energy -= amount;
        return true;
    }

    /**
     * Restore integrity (the REPAIR work): clamped at the maximum.
     *
     * @param amount the integrity restored (&gt; 0)
     * @return true when integrity was actually restored (false = already at maximum)
     */
    public boolean repair(int amount) {
        if (amount <= 0) {
            return false;
        }
        long max = maxIntegrity();
        if (integrity >= max) {
            return false;
        }
        integrity = Math.min(max, integrity + amount);
        return true;
    }

    public String getName() {
        return name;
    }

    public double getSpeed() {
        return speed;
    }

    public long getMiningPower() {
        return miningPower;
    }

    /**
     * The ship's scan power (the Explorer pass; from the payload's {@code vc_scan}). 0 when the payload lacks the
     * tag (a pre-scan ship has no scanning capability).
     *
     * @return the total scan power (0 = not an Explorer).
     */
    public long getScanPower() {
        if (payload == null) {
            return 0L;
        }
        return VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_SCAN);
    }

    /**
     * The ship's construction power (the CONSTRUCT pacing: one part per second per 100 power, see
     * {@link USSConstants#constructTicksPerItem}): from the payload's {@code vc_construction} (denormalized at
     * digitization). 0 when the payload lacks the tag (the pacing then uses the base rate).
     *
     * @return the total construction power (0 = no constructor components)
     */
    public long getConstructionPower() {
        if (payload == null) {
            return 0L;
        }
        return VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_CONSTRUCTION);
    }

    /**
     * The ship's siphon (starlifter) power (the SIPHON work leg's pacing + yield): from the payload's
     * {@code vc_starlifter} (denormalized at digitization). 0 when the payload lacks the tag (a pre-starlifter
     * ship has no siphoning capability).
     *
     * @return the total siphon power (0 = no Star Siphon components)
     */
    public long getStarlifterPower() {
        if (payload == null) {
            return 0L;
        }
        return VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_STARLIFTER);
    }

    /**
     * The ship's logistics power (the SEND / TAKE cargo-transfer rate: 1 power = 1 cargo unit per second, see
     * {@link USSConstants#transferTicksPerUnit}): from the payload's {@code vc_logistics} (denormalized at
     * digitization). 0 when the payload lacks the tag (no Cargo Drone Bay covers — the ship cannot transfer
     * cargo).
     *
     * @return the total logistics power (0 = no Cargo Drone Bay components)
     */
    public long getLogisticsPower() {
        if (payload == null) {
            return 0L;
        }
        return VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_LOGISTICS);
    }

    /**
     * The ship's remaining INTEGRITY (the time limit, in seconds — it drops 1 per second while the ship is in the
     * USS). 0 = the ship is lost (the caller removes it and discards its cargo).
     */
    public long getIntegrity() {
        return integrity;
    }

    /**
     * The ship's MAXIMUM integrity (the blueprint's total, from the payload's {@code vc_integrity}) — the value
     * the integrity time limit starts from when the ship enters the USS.
     */
    public long maxIntegrity() {
        if (payload == null) {
            return 0L;
        }
        return Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_INTEGRITY));
    }

    /**
     * Advance the integrity time limit by ONE world tick (call once per tick while the ship is in the USS — it
     * counts down even while the ship HOLDS). Integrity drops by 1 every {@link #TICKS_PER_INTEGRITY} ticks
     * (one game second).
     *
     * @return true when the integrity has reached 0 — the ship is LOST (removed from the USS, cargo discarded)
     */
    public boolean tickIntegrity() {
        if (integrity <= 0L) {
            return true;
        }
        if (integrityTimer > 0) {
            integrityTimer--;
        }
        if (integrityTimer <= 0) {
            integrityTimer = TICKS_PER_INTEGRITY;
            integrity--;
        }
        return integrity <= 0L;
    }

    public USSShipState getState() {
        return state;
    }

    /** Ticks left in the current leg (0 when no leg is armed). */
    public int getTicksRemaining() {
        return ticksRemaining;
    }

    /** The current leg's total duration in ticks (the client derives the same duration from the synced state). */
    public int getLegTotal() {
        return legTotal;
    }

    /** @return ticks elapsed in the current leg (the {@code TICKS_IN_LEG} stat; 0 when no leg is armed). */
    public int getTicksInLeg() {
        return legTotal - ticksRemaining;
    }

    /** @return true while a leg is armed (counting down or latched-complete-but-unconsumed). */
    public boolean isLegActive() {
        return legActive;
    }

    /** @return the current leg's start point (the ship's position when the leg began). */
    public USSPosition getLegFrom() {
        return legFrom;
    }

    /** @return the current leg's endpoint (the point the ship flies to / works at) — null when no leg has run. */
    public USSPosition getDestination() {
        return destination;
    }

    /** The current leg's distance in blocks. */
    public double getTravelDistance() {
        return travelDistance;
    }

    /**
     * @return the ship's CURRENT position in fleet-anchor coordinates (its launch origin, then the last leg's
     *         endpoint).
     */
    public USSPosition getPosition() {
        return position;
    }

    /**
     * Set the position in fleet-anchor coordinates (the USS re-derives an anchored entity's position from its
     * live anchor each tick).
     */
    public void setPosition(USSPosition position) {
        this.position = (position == null) ? USSPosition.zero() : position;
    }

    /** @return the monotonic leg counter (the client's leg-identity for progress-phase resets). */
    public int getLegId() {
        return legId;
    }

    // region leg driver (the pilot arms, ticks, and consumes)

    /**
     * The current leg's work kind (see {@link USSWorkKind}): TRAVEL for a travel leg, or the work kind the
     * work command started (the pilot arms it with the leg). The client reads this (fleet-entry tag) to derive
     * the SAME leg duration the server ticks — a ship's leg duration depends on the kind.
     */
    private int legWorkKind = USSWorkKind.TRAVEL;

    /** @return the current leg's work kind ({@link USSWorkKind}; TRAVEL when no work leg is armed). */
    public int getLegWorkKind() {
        return legWorkKind;
    }

    /**
     * @return true while a MOVE (travel) leg is armed — the ship is en route and not properly settled at any
     *         location (a work leg does not qualify: the ship hovers at its work point for the whole leg). A
     *         latched-complete leg still counts: the ship has only just arrived until the leg is consumed.
     */
    public boolean isTraveling() {
        return legActive && !USSWorkKind.isWork(legWorkKind);
    }

    /**
     * Arm a new leg (replaces any previous one).
     *
     * @param state    the leg's state (OUTBOUND a travel leg to a body / MINING a work leg / RETURNING the leg home)
     * @param from     the leg's start point (the ship's current position)
     * @param to       the leg's endpoint
     * @param ticks    the leg's duration in ticks (&gt; 0; &le; 0 clamps to 0 — a zero-length leg completes on the
     *                 next consumption)
     * @param distance the leg's distance in blocks (&le; 0 clamps to 0)
     * @param workKind the leg's work kind (see {@link USSWorkKind})
     */
    public void startLeg(USSShipState state, USSPosition from, USSPosition to, int ticks, double distance,
        int workKind) {
        this.state = state;
        USSPosition f = (from == null) ? USSPosition.zero() : from;
        this.legFrom = f;
        this.position = f;
        this.destination = to;
        this.travelDistance = Math.max(0.0, distance);
        this.ticksRemaining = Math.max(0, ticks);
        this.legTotal = Math.max(0, ticks);
        this.legActive = true;
        this.legDone = false;
        this.legId++;
        this.legWorkKind = workKind;
    }

    /**
     * Count the armed leg down by one tick (call every tick — legs tick in REAL time; the pilot's executor pacing
     * never distorts them). A finished leg latches ({@link #isLegComplete()}) until consumed.
     */
    public void tickLeg() {
        if (!legActive || legDone) {
            return;
        }
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
        if (ticksRemaining <= 0) {
            legDone = true;
        }
    }

    /**
     * @return true while a leg is armed AND its countdown has finished (latched; consumed by
     *         {@link #clearLegComplete()})
     */
    public boolean isLegComplete() {
        return legActive && legDone;
    }

    /**
     * Consume the completed leg: the latch is cleared, the leg deactivated, and the ship's position becomes the
     * leg's endpoint (arrival). The pilot calls this EXACTLY ONCE per leg (its side-effect fires there).
     */
    public void clearLegComplete() {
        legDone = false;
        legActive = false;
        if (destination != null) {
            position = destination;
        }
    }

    /**
     * Park the ship (the program finished — user decision: HOLD, no implicit return). Abandons any in-flight leg
     * (the countdown stops); the ship stays where it is.
     */
    public void hold() {
        state = USSShipState.HOVERING;
        legActive = false;
        legDone = false;
    }

    // endregion

    /** Cargo (null until a WORK leg's completion sets it). */
    public NBTTagCompound getCargo() {
        return cargo;
    }

    /**
     * Hand the cargo in (the WORK leg's completion sets it; also used when restoring a ship from NBT).
     */
    public void setCargo(NBTTagCompound cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the ship's internal cargo hold (the cargo-capacity pass) — null until the hold is initialized (at
     *         launch or NBT restore)
     */
    public CargoHold getHold() {
        return hold;
    }

    /**
     * Set the ship's internal cargo hold.
     *
     * @param hold the hold (null clears it)
     */
    public void setHold(CargoHold hold) {
        this.hold = hold;
    }

    /**
     * The ship's cargo CAPACITY in cargo units (the blueprint's {@code cargoSlots}, from the payload's
     * {@code vc_cargo} tag). This is the bound the internal hold is created with at launch.
     *
     * @return the capacity (0 when the payload lacks the tag)
     */
    public long cargoCapacity() {
        return holdCapacityFor(payload);
    }

    /**
     * The cargo hold capacity (in cargo units) implied by a ship payload: the payload's {@code vc_cargo} (the
     * blueprint's cargoSlots) times {@link #CARGO_UNIT_MULTIPLIER}. Shared with the gateway, which caps the
     * constructor parts loadout at the same cargo space the ship's hold gets at launch.
     *
     * @param payload a ship item NBT (null → 0)
     * @return the capacity (0 when the payload lacks the tag)
     */
    public static long holdCapacityFor(NBTTagCompound payload) {
        if (payload == null) {
            return 0L;
        }
        return VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_CARGO) * CARGO_UNIT_MULTIPLIER;
    }

    /**
     * Initialize the ship's internal cargo hold (empty, capacity from the payload's {@code vc_cargo}). Called at
     * launch and NBT restore so the ship's hold is always present.
     */
    public void initializeHold() {
        if (hold == null) {
            hold = CargoHold.of(cargoCapacity());
        }
    }

    /**
     * The parts loadout a CONSTRUCTOR carries (parts-list key to count on board) - an unmodifiable view. Empty
     * for ships without a constructor mission.
     */
    public java.util.Map<String, Long> getBuildLoadout() {
        return java.util.Collections.unmodifiableMap(buildLoadout);
    }

    /**
     * @return the total parts on board over all keys (0 when the ship carries no loadout)
     */
    public long buildLoadoutTotal() {
        long total = 0L;
        for (Long count : buildLoadout.values()) {
            total += count;
        }
        return total;
    }

    /**
     * Consume parts from the loadout: take up to {@code amount} of the given key (clamped to what is on board)
     * - the key is dropped once it reaches zero.
     *
     * @param key    a parts-list key (unknown key to 0)
     * @param amount the requested count
     * @return the count actually consumed
     */
    public long consumeBuildParts(String key, long amount) {
        if (key == null || amount <= 0L) {
            return 0L;
        }
        Long onBoard = buildLoadout.get(key);
        if (onBoard == null) {
            return 0L;
        }
        long take = Math.min(amount, onBoard);
        if (take <= 0L) {
            return 0L;
        }
        long left = onBoard - take;
        if (left <= 0L) {
            buildLoadout.remove(key);
        } else {
            buildLoadout.put(key, left);
        }
        return take;
    }

    /**
     * Load the constructor loadout from the launch payload tag {@code vc_build_loadout} (entries
     * {@code {key, amount}}) - called by the constructor; zero or negative amounts are skipped.
     */
    private void readBuildLoadoutFromPayload(NBTTagCompound payload) {
        if (!payload.hasKey(VoidcraftNbt.TAG_BUILD_LOADOUT)) {
            return;
        }
        loadBuildLoadoutList(payload.getTagList(VoidcraftNbt.TAG_BUILD_LOADOUT, 10));
    }

    /**
     * Replace the loadout with the persisted remainder (a CONSTRUCT leg may have consumed parts in flight) -
     * the launch-payload copy the constructor made is discarded.
     *
     * @param loadout the persisted list (entries {@code {key, amount}})
     */
    public void restoreBuildLoadout(NBTTagList loadout) {
        buildLoadout.clear();
        loadBuildLoadoutList(loadout);
    }

    /**
     * Merge a loadout list (entries {@code {key, amount}}) into the loadout - zero or negative amounts are
     * skipped, equal keys add up.
     */
    private void loadBuildLoadoutList(NBTTagList loadout) {
        if (loadout == null) {
            return;
        }
        for (int i = 0; i < loadout.tagCount(); i++) {
            NBTTagCompound part = loadout.getCompoundTagAt(i);
            if (part == null) {
                continue;
            }
            String key = part.getString("key");
            long amount = part.hasKey("amount") ? part.getInteger("amount") : 0;
            if (key.isEmpty() || amount <= 0L) {
                continue;
            }
            Long prev = buildLoadout.get(key);
            buildLoadout.put(key, (prev == null ? 0L : prev) + amount);
        }
    }

    /** The full ship item NBT captured at launch. */
    public NBTTagCompound getPayload() {
        return payload;
    }

    /** Launching gateway world position (return endpoint + ship re-emission target), or null. */
    public int[] getGatewayPos() {
        return gatewayPos;
    }

    /** Storage-bay world position (cargo delivery target), or null. */
    public int[] getBayPos() {
        return bayPos;
    }

    // region NBT

    /**
     * Serialize the full ship state (identity, the leg, the cargo, the payload). The pilot's state is nested by
     * the MTE under {@code vc_pilot} (see {@link USSShipPilot#writeToNBT()}).
     */
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(TAG_UUID, uuid);
        nbt.setString(TAG_NAME, name);
        nbt.setDouble(TAG_SPEED, speed);
        nbt.setLong(TAG_MINING, miningPower);
        nbt.setLong(TAG_INTEGRITY, integrity);
        nbt.setInteger(TAG_INTEG_TICKS, integrityTimer);
        nbt.setInteger(TAG_STATE, state.getId());
        nbt.setInteger(TAG_TICKS, ticksRemaining);
        nbt.setInteger(TAG_LEG, legTotal);
        // The leg (Phase C passive driver): start point, endpoint, distance, identity, and the completion latch
        // (a leg that finished but whose side-effect has not fired yet must resume exactly once after a reload).
        nbt.setBoolean(TAG_LEG_ACTIVE, legActive);
        nbt.setBoolean(TAG_LEG_DONE, legDone);
        nbt.setInteger(TAG_LEG_ID, legId);
        nbt.setInteger(TAG_LEG_WORK_KIND, legWorkKind);
        nbt.setBoolean(TAG_BODY_STATIC, bodyStatic);
        if (legFrom != null) {
            NBTTagCompound fromTag = new NBTTagCompound();
            legFrom.writeToNBT(fromTag);
            nbt.setTag(TAG_LEG_FROM, fromTag);
        }
        nbt.setDouble(TAG_TDIST, travelDistance);
        if (destination != null) {
            NBTTagCompound destTag = new NBTTagCompound();
            destination.writeToNBT(destTag);
            nbt.setTag(TAG_DEST, destTag);
        }
        if (position != null) {
            NBTTagCompound posTag = new NBTTagCompound();
            position.writeToNBT(posTag);
            nbt.setTag(TAG_POS, posTag);
        }
        if (cargo != null) {
            nbt.setTag(TAG_CARGO, cargo);
        }
        // The internal cargo hold (the cargo-capacity pass) — the ship's stateful cargo.
        if (hold != null) {
            NBTTagCompound holdTag = new NBTTagCompound();
            hold.writeToNBT(holdTag);
            nbt.setTag(TAG_HOLD, holdTag);
        }
        // The constructor's remaining parts loadout (the CONSTRUCT legs consume it part by part).
        if (!buildLoadout.isEmpty()) {
            NBTTagList loadoutTag = new NBTTagList();
            for (java.util.Map.Entry<String, Long> entry : buildLoadout.entrySet()) {
                NBTTagCompound part = new NBTTagCompound();
                part.setString("key", entry.getKey());
                part.setInteger(
                    "amount",
                    entry.getValue()
                        .intValue());
                loadoutTag.appendTag(part);
            }
            nbt.setTag(TAG_BUILD_LOADOUT, loadoutTag);
        }
        if (payload != null) {
            nbt.setTag(TAG_PAYLOAD, payload);
        }
        if (gatewayPos != null) {
            nbt.setIntArray(TAG_GATEWAY, gatewayPos);
        }
        if (bayPos != null) {
            nbt.setIntArray(TAG_BAY, bayPos);
        }
        if (anchor != null) {
            NBTTagCompound anchorTag = new NBTTagCompound();
            anchor.writeToNBT(anchorTag);
            nbt.setTag(TAG_ANCHOR, anchorTag);
        }
        nbt.setLong(TAG_ENERGY, energy);
        nbt.setInteger(TAG_SEED, seed);
        nbt.setInteger(TAG_TARGET, targetPlanet);
        return nbt;
    }

    /**
     * Restore a mission from NBT.
     *
     * @param nbt the tag written by {@link #writeToNBT()}
     * @return the restored ship, or null if the tag is missing required keys
     */
    public static VoidcraftActiveShip readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_UUID) || !nbt.hasKey(TAG_STATE)) {
            return null;
        }
        USSShipState state = USSShipState.byId(nbt.getInteger(TAG_STATE));
        if (state == null || state == USSShipState.DOCKED) {
            // In-flight ships only: docked ships wait in the gateway's input buses, not here.
            return null;
        }
        NBTTagCompound cargo = nbt.hasKey(TAG_CARGO) ? nbt.getCompoundTag(TAG_CARGO) : null;
        NBTTagCompound payload = nbt.hasKey(TAG_PAYLOAD) ? nbt.getCompoundTag(TAG_PAYLOAD) : null;
        int[] gatewayPos = nbt.hasKey(TAG_GATEWAY) && nbt.getIntArray(TAG_GATEWAY).length == 3
            ? nbt.getIntArray(TAG_GATEWAY)
            : null;
        int[] bayPos = nbt.hasKey(TAG_BAY) && nbt.getIntArray(TAG_BAY).length == 3 ? nbt.getIntArray(TAG_BAY) : null;
        int seed = nbt.hasKey(TAG_SEED) ? nbt.getInteger(TAG_SEED) : 0;
        int targetPlanet = nbt.hasKey(TAG_TARGET) ? nbt.getInteger(TAG_TARGET) : -1;
        VoidcraftActiveShip ship = new VoidcraftActiveShip(
            nbt.getString(TAG_UUID),
            nbt.getString(TAG_NAME),
            nbt.getDouble(TAG_SPEED),
            nbt.getLong(TAG_MINING),
            payload,
            gatewayPos,
            bayPos,
            seed);
        ship.state = state;
        ship.targetPlanet = targetPlanet;
        // The integrity time limit: restore the persisted countdown (missing tag → the full maximum, as if the
        // ship had just entered the USS).
        ship.integrity = nbt.hasKey(TAG_INTEGRITY) ? Math.max(0L, nbt.getLong(TAG_INTEGRITY)) : ship.maxIntegrity();
        ship.integrityTimer = nbt.hasKey(TAG_INTEG_TICKS) ? Math.max(1, nbt.getInteger(TAG_INTEG_TICKS))
            : TICKS_PER_INTEGRITY;
        ship.ticksRemaining = nbt.getInteger(TAG_TICKS);
        ship.legTotal = nbt.hasKey(TAG_LEG) ? nbt.getInteger(TAG_LEG) : ship.ticksRemaining;
        ship.legActive = nbt.hasKey(TAG_LEG_ACTIVE) && nbt.getBoolean(TAG_LEG_ACTIVE);
        ship.legDone = nbt.hasKey(TAG_LEG_DONE) && nbt.getBoolean(TAG_LEG_DONE);
        ship.legId = nbt.hasKey(TAG_LEG_ID) ? nbt.getInteger(TAG_LEG_ID) : 0;
        ship.legWorkKind = nbt.hasKey(TAG_LEG_WORK_KIND) ? nbt.getInteger(TAG_LEG_WORK_KIND) : USSWorkKind.TRAVEL;
        ship.bodyStatic = nbt.hasKey(TAG_BODY_STATIC) && nbt.getBoolean(TAG_BODY_STATIC);
        ship.travelDistance = nbt.hasKey(TAG_TDIST) ? nbt.getDouble(TAG_TDIST) : 0.0;
        if (nbt.hasKey(TAG_LEG_FROM)) {
            ship.legFrom = USSPosition.readFromNBT(nbt.getCompoundTag(TAG_LEG_FROM));
        }
        if (nbt.hasKey(TAG_DEST)) {
            ship.destination = USSPosition.readFromNBT(nbt.getCompoundTag(TAG_DEST));
        }
        if (nbt.hasKey(TAG_POS)) {
            ship.position = USSPosition.readFromNBT(nbt.getCompoundTag(TAG_POS));
        }
        if (ship.position == null) {
            ship.position = (ship.legFrom != null) ? ship.legFrom : USSPosition.zero();
        }
        ship.setCargo(cargo);
        // The internal cargo hold (the cargo-capacity pass). The constructor created a default hold; replace it
        // with the persisted one when present (a ship mid-mission may have a partially filled hold).
        if (nbt.hasKey(TAG_HOLD)) {
            ship.setHold(CargoHold.readFromNBT(nbt.getCompoundTag(TAG_HOLD)));
        }
        // The constructor's remaining parts loadout: the constructor initialized a full copy from the payload;
        // the persisted remainder (partially consumed in flight) replaces it when present.
        if (nbt.hasKey(TAG_BUILD_LOADOUT)) {
            ship.restoreBuildLoadout(nbt.getTagList(TAG_BUILD_LOADOUT, 10));
        }
        // The base anchor (present = an anchored Voidbase; absent = a flying ship) + the energy buffer content
        // (capacity/generation re-derive from the payload on restore).
        if (nbt.hasKey(TAG_ANCHOR)) {
            ship.anchor = USSBaseAnchor.readFromNBT(nbt.getCompoundTag(TAG_ANCHOR));
        }
        if (nbt.hasKey(TAG_ENERGY)) {
            ship.energy = Math.max(0L, Math.min(ship.energyCapacity, nbt.getLong(TAG_ENERGY)));
        }
        return ship;
    }

    // endregion

    @Override
    public String toString() {
        return "VoidcraftActiveShip[" + name
            + (anchor != null ? " @ " + anchor : "")
            + " "
            + state
            + " ticks="
            + ticksRemaining
            + "/"
            + legTotal
            + " leg#"
            + legId
            + " pos="
            + position
            + " integrity="
            + integrity
            + " energy="
            + energy
            + "/"
            + energyCapacity
            + " cargo="
            + (cargo == null ? "none" : "yes")
            + "]";
    }
}
