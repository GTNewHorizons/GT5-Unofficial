package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidcraft in flight inside an Unstable Solar System: a PASSIVE leg driver (no mission state machine — the
 * ship's PILOT decides its legs, programming framework Phase C).
 *
 * <p>
 * The ship knows ONE thing: its current leg. A leg has a state ({@link USSShipState#OUTBOUND} a travel leg to a
 * body, {@link USSShipState#MINING} a work leg, {@link USSShipState#RETURNING} the travel leg home), a start
 * point, an endpoint, a duration, and a countdown. {@link #startLeg} arms it, {@link #tickLeg} counts it down,
 * {@link #isLegComplete()} latches completion (consumed exactly once via {@link #clearLegComplete()} by the
 * pilot — its side-effect fires exactly once, even across a save/reload), and {@link #hold()} parks the ship
 * ({@link USSShipState#HOVERING} — the program finished without a return; user decision: no implicit MOVE HOME).
 *
 * <p>
 * Bare-JVM (NBT + primitives only) so the ship + pilot stay unit-testable without Forge. Cargo is built externally
 * (see {@link USSShipCargo}) and handed in via {@link #setCargo(NBTTagCompound)} on the WORK leg's completion —
 * this class stays free of {@code Materials} so tests run without a live ore dictionary.
 *
 * <p>
 * On completion the ship is delivered: {@link #isRecoverable()} ships are re-emitted as items (the gateway puts
 * them back in its ship slot); expendable ships are consumed (only the cargo is kept).
 */
public final class VoidcraftActiveShip {

    private static final String TAG_UUID = "vc_uuid";
    private static final String TAG_NAME = "vc_name";
    private static final String TAG_SPEED = "vc_speed";
    private static final String TAG_MINING = "vc_mining";
    private static final String TAG_RECOVERABLE = "vc_recoverable";
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
    private static final String TAG_LEG_ACTIVE = "vc_leg_active";
    private static final String TAG_LEG_DONE = "vc_leg_done";
    private static final String TAG_BODY_STATIC = "vc_body_static";

    /**
     * Pass 27 (user: "the cargo hold size should be increased by a factor of 100 — as currently the mining is
     * basically instant"): the hold capacity is the blueprint's {@code cargoSlots} times this. 100× makes a trip
     * carry a real haul instead of filling (and thus completing) near-instantly. Applied at the single capacity
     * source ({@link #cargoCapacity()}) so the hold, its NBT, and the derived delivery cargo all scale together.
     */
    public static final long CARGO_UNIT_MULTIPLIER = 100L;

    private final String uuid;
    private final String name;
    private final double speed;
    private final long miningPower;
    private final boolean recoverable;
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

    private VoidcraftActiveShip(String uuid, String name, double speed, long miningPower, boolean recoverable,
        NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed) {
        this.uuid = uuid;
        this.name = name;
        this.speed = speed;
        this.miningPower = miningPower;
        this.recoverable = recoverable;
        this.payload = payload;
        this.gatewayPos = gatewayPos;
        this.bayPos = bayPos;
        this.seed = seed;
        this.state = USSShipState.HOVERING;
        // The internal cargo hold (the cargo-capacity pass): empty, capacity from cargoCapacity() (the payload's
        // vc_cargo × CARGO_UNIT_MULTIPLIER — the pass-27 100× hold).
        if (payload != null) {
            this.hold = CargoHold.of(cargoCapacity());
        }
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
     * @param recoverable true if the ship returns as an item when the mission ends
     * @param payload     the ship payload — the item's tag compound (vc_* keys at its top level), kept so the ship can
     *                    be re-emitted as an item on return and the client can render the ship model
     * @param gatewayPos  launching gateway world position (the RETURNING endpoint + where a recoverable ship is
     *                    re-emitted); may be null (drop-at-USS fallback)
     * @param bayPos      storage-bay world position (the cargo delivery target); may be null (drop-at-USS fallback)
     * @param seed        the per-launch identity seed (the client's per-ship key)
     * @param origin      the launch origin in fleet-anchor coordinates (null → {@code (0,0,0)})
     */
    public static VoidcraftActiveShip launch(String uuid, String name, double speed, long miningPower,
        boolean recoverable, NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed, USSPosition origin) {
        VoidcraftActiveShip ship = new VoidcraftActiveShip(
            uuid,
            name,
            speed,
            miningPower,
            recoverable,
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

    /** @return the hover body descriptor: a system planet index, a ripple-point index, or {@code -1} (star / none). */
    public int getTargetPlanet() {
        return targetPlanet;
    }

    /** Set the hover body descriptor (the pilot does this when a MOVE target resolves). */
    public void setTargetPlanet(int targetPlanet) {
        this.targetPlanet = targetPlanet;
    }

    /** @return true when the client hovers the ship at the fixed resolved destination (ripple / ship rendezvous). */
    public boolean isBodyStatic() {
        return bodyStatic;
    }

    /** Set the static-body flag (the pilot sets it when a MOVE target resolves). */
    public void setBodyStatic(boolean bodyStatic) {
        this.bodyStatic = bodyStatic;
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
     * The ship's role bitmask (from the payload's {@code vc_roles} — see {@code VoidcraftRole}). 0 when the
     * payload lacks the tag (a pre-role ship acts as a pure miner).
     *
     * @return the active role mask.
     */
    public int getRoles() {
        if (payload == null) {
            return 0;
        }
        return VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES);
    }

    public boolean isRecoverable() {
        return recoverable;
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

    /** @return the monotonic leg counter (the client's leg-identity for progress-phase resets). */
    public int getLegId() {
        return legId;
    }

    // region leg driver (the pilot arms, ticks, and consumes)

    /**
     * Arm a new leg (replaces any previous one).
     *
     * @param state    the leg's state (OUTBOUND a travel leg to a body / MINING a work leg / RETURNING the leg home)
     * @param from     the leg's start point (the ship's current position)
     * @param to       the leg's endpoint
     * @param ticks    the leg's duration in ticks (&gt; 0; &le; 0 clamps to 0 — a zero-length leg completes on the
     *                 next consumption)
     * @param distance the leg's distance in blocks (&le; 0 clamps to 0)
     */
    public void startLeg(USSShipState state, USSPosition from, USSPosition to, int ticks, double distance) {
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
        if (payload == null) {
            return 0L;
        }
        // Pass 27: the hold is 100× the blueprint's cargoSlots (see CARGO_UNIT_MULTIPLIER).
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
        nbt.setBoolean(TAG_RECOVERABLE, recoverable);
        nbt.setInteger(TAG_STATE, state.getId());
        nbt.setInteger(TAG_TICKS, ticksRemaining);
        nbt.setInteger(TAG_LEG, legTotal);
        // The leg (Phase C passive driver): start point, endpoint, distance, identity, and the completion latch
        // (a leg that finished but whose side-effect has not fired yet must resume exactly once after a reload).
        nbt.setBoolean(TAG_LEG_ACTIVE, legActive);
        nbt.setBoolean(TAG_LEG_DONE, legDone);
        nbt.setInteger(TAG_LEG_ID, legId);
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
        if (payload != null) {
            nbt.setTag(TAG_PAYLOAD, payload);
        }
        if (gatewayPos != null) {
            nbt.setIntArray(TAG_GATEWAY, gatewayPos);
        }
        if (bayPos != null) {
            nbt.setIntArray(TAG_BAY, bayPos);
        }
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
            // In-flight ships only: docked ships live in the gateway's inventory, not here.
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
            nbt.getBoolean(TAG_RECOVERABLE),
            payload,
            gatewayPos,
            bayPos,
            seed);
        ship.state = state;
        ship.targetPlanet = targetPlanet;
        ship.ticksRemaining = nbt.getInteger(TAG_TICKS);
        ship.legTotal = nbt.hasKey(TAG_LEG) ? nbt.getInteger(TAG_LEG) : ship.ticksRemaining;
        ship.legActive = nbt.hasKey(TAG_LEG_ACTIVE) && nbt.getBoolean(TAG_LEG_ACTIVE);
        ship.legDone = nbt.hasKey(TAG_LEG_DONE) && nbt.getBoolean(TAG_LEG_DONE);
        ship.legId = nbt.hasKey(TAG_LEG_ID) ? nbt.getInteger(TAG_LEG_ID) : 0;
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
        return ship;
    }

    // endregion

    @Override
    public String toString() {
        return "VoidcraftActiveShip[" + name
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
            + " cargo="
            + (cargo == null ? "none" : "yes")
            + "]";
    }
}
