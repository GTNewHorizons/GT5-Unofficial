package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidcraft in flight inside an Unstable Solar System: a pure tick-driven state machine (no Minecraft world
 * access) so the mission loop is unit-testable.
 *
 * <p>
 * Lifecycle: {@code OUTBOUND} → {@code MINING} → {@code RETURNING} → complete. Leg durations come from
 * {@link USSConstants} (speed / mining power based). Cargo is built externally (see {@link USSShipCargo}) and
 * handed in via {@link #setCargo(NBTTagCompound)} during the MINING leg — this class stays free of
 * {@code Materials} so tests run without a live ore dictionary.
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
    private static final String TAG_PAYLOAD = "vc_payload";
    private static final String TAG_GATEWAY = "vc_gateway";
    private static final String TAG_BAY = "vc_bay";
    private static final String TAG_SEED = "vc_seed";
    private static final String TAG_TARGET = "vc_target";

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
     * Mission target (pass 7): the index into the system's planet list the ship works — the client hovers 0.5
     * blocks above that planet's RENDERED position (dynamic: the planet keeps orbiting). {@code -1} = the star
     * itself (Starlifters work 2.5 blocks above the star center).
     */
    private final int targetPlanet;

    private USSShipState state;
    private int ticksRemaining;

    private NBTTagCompound cargo;

    private VoidcraftActiveShip(String uuid, String name, double speed, long miningPower, boolean recoverable,
        NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed, int targetPlanet, USSShipState state,
        int ticksRemaining) {
        this.uuid = uuid;
        this.name = name;
        this.speed = speed;
        this.miningPower = miningPower;
        this.recoverable = recoverable;
        this.payload = payload;
        this.gatewayPos = gatewayPos;
        this.bayPos = bayPos;
        this.seed = seed;
        this.targetPlanet = targetPlanet;
        this.state = state;
        this.ticksRemaining = ticksRemaining;
    }

    /**
     * Create a newly launched ship (OUTBOUND leg started).
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
     */
    public static VoidcraftActiveShip launch(String uuid, String name, double speed, long miningPower,
        boolean recoverable, NBTTagCompound payload, int[] gatewayPos, int[] bayPos) {
        // seed 0 = legacy: the client falls back to the item UUID for per-ship identity
        return launch(uuid, name, speed, miningPower, recoverable, payload, gatewayPos, bayPos, 0, -1);
    }

    /**
     * Create a newly launched ship (OUTBOUND leg started) with a per-launch identity seed (see {@link #getSeed()}).
     *
     * @param seed unique per launch (the USS assigns a fresh random value) — the client's per-ship key
     */
    public static VoidcraftActiveShip launch(String uuid, String name, double speed, long miningPower,
        boolean recoverable, NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed) {
        return launch(uuid, name, speed, miningPower, recoverable, payload, gatewayPos, bayPos, seed, -1);
    }

    /**
     * Create a newly launched ship (OUTBOUND leg started) with the full pass-6/7 identity.
     *
     * @param seed         unique per launch (the USS assigns a fresh random value) — the client's per-ship key
     * @param targetPlanet the mission target: a system planet index (the ship hovers 0.5 above it) or {@code -1}
     *                     for the star itself (Starlifters hover 2.5 above the star center)
     */
    public static VoidcraftActiveShip launch(String uuid, String name, double speed, long miningPower,
        boolean recoverable, NBTTagCompound payload, int[] gatewayPos, int[] bayPos, int seed, int targetPlanet) {
        return new VoidcraftActiveShip(
            uuid,
            name,
            speed,
            miningPower,
            recoverable,
            payload,
            gatewayPos == null ? null : gatewayPos.clone(),
            bayPos == null ? null : bayPos.clone(),
            seed,
            targetPlanet,
            USSShipState.OUTBOUND,
            (int) USSConstants.travelTicks(speed));
    }

    public String getUuid() {
        return uuid;
    }

    /** @return the per-launch identity seed (0 = unset/legacy — the client uses the item UUID instead). */
    public int getSeed() {
        return seed;
    }

    /** @return the mission target: a system planet index, or {@code -1} for the star itself. */
    public int getTargetPlanet() {
        return targetPlanet;
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

    /** Ticks left in the current leg. */
    public int getTicksRemaining() {
        return ticksRemaining;
    }

    /** Cargo (null until the MINING leg completes). */
    public NBTTagCompound getCargo() {
        return cargo;
    }

    /**
     * Hand the cargo in (the MINING leg sets it when it completes; also used when restoring a ship from NBT).
     */
    public void setCargo(NBTTagCompound cargo) {
        this.cargo = cargo;
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

    /**
     * Advance the mission by one tick.
     *
     * @return true while the ship is still in flight; false once the RETURNING leg completed (the caller delivers
     *         the cargo and removes the ship)
     */
    public boolean tick() {
        if (cargo != null && state == USSShipState.RETURNING && ticksRemaining <= 0) {
            // Defensive: never tick a finished ship twice.
            return false;
        }
        if (ticksRemaining <= 0) {
            // Transition at the start of the leg's first tick.
            switch (state) {
                case OUTBOUND:
                    state = USSShipState.MINING;
                    ticksRemaining = (int) USSConstants.mineTicks(miningPower);
                    break;
                case MINING:
                    // Cargo is produced when mining finishes; if the caller did not set it, produce an empty one
                    // so delivery never sees null.
                    if (cargo == null) {
                        cargo = new NBTTagCompound();
                    }
                    state = USSShipState.RETURNING;
                    ticksRemaining = (int) USSConstants.travelTicks(speed);
                    break;
                case RETURNING:
                default:
                    return false;
            }
            return true;
        }
        ticksRemaining--;
        if (state == USSShipState.RETURNING && ticksRemaining <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Serialize the full mission state.
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
        nbt.setInteger(TAG_LEG, (int) USSConstants.legTicks(state, speed, miningPower));
        if (cargo != null) {
            nbt.setTag(TAG_CARGO, cargo);
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
        int leg = nbt.hasKey(TAG_LEG) ? nbt.getInteger(TAG_LEG) : 0;
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
            seed,
            targetPlanet,
            state,
            nbt.getInteger(TAG_TICKS));
        ship.setCargo(cargo);
        return ship;
    }

    @Override
    public String toString() {
        return "VoidcraftActiveShip[" + name
            + " "
            + state
            + " ticks="
            + ticksRemaining
            + " cargo="
            + (cargo == null ? "none" : "yes")
            + "]";
    }
}
