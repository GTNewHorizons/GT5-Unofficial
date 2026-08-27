package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidbase in the Unstable Solar System: an immobile station mirroring the in-flight Voidcraft (Voidbase
 * construction framework) - the same component payload, the same integrity mechanic (1 per second via
 * {@link #TICKS_PER_INTEGRITY}; at 0 the base decommissions, cargo discarded, exactly like a lost ship), and
 * the same controller program (run by the base pilot: MOVE fails = SKIP (HOME included), WORK runs a real
 * mining leg when the station has mining power (an instant no-op otherwise), and the program repeats forever —
 * the executor's invisible while — until a STOP).
 *
 * <p>
 * The base does not fly: it sits at the anchor band point (a planet anchor: within ±30° of the planet's
 * orbital plane at the ship hover distance; a star anchor: the star position), with the position recomputed
 * each tick from the live anchor - a base anchored to a planet follows that planet orbit.
 *
 * <p>
 * Bare-JVM (NBT + primitives) for unit tests.
 */
public final class VoidcraftActiveBase {

    /** Seconds of integrity decay per tick: 1 integrity per 20 ticks (1 per second, as in the USS). */
    public static final int TICKS_PER_INTEGRITY = 20;

    /** The repair work command draw (EU per tick: 1 integrity per second of repair). */
    public static final long REPAIR_DRAW = 2000L;

    /**
     * The integrity a base falls back to when its payload carries no usable integrity stat (a stale or corrupt
     * blueprint item): a base never spawns at 0.
     */
    public static final long DEFAULT_INTEGRITY = 60L;

    private static final String TAG_UUID = "vc_base_uuid";
    private static final String TAG_NAME = "vc_base_name";
    private static final String TAG_SEED = "vc_base_seed";
    private static final String TAG_ANCHOR = "vc_base_anchor";
    private static final String TAG_INTEGRITY = "vc_base_integrity";
    private static final String TAG_INTEG_TICKS = "vc_base_integ_ticks";
    private static final String TAG_CARGO = "vc_base_cargo";
    private static final String TAG_PAYLOAD = "vc_base_payload";
    private static final String TAG_POS = "vc_base_pos";
    private static final String TAG_ENERGY = "vc_base_energy";
    private static final String TAG_REPAIR_TICKS = "vc_base_repair_ticks";

    private final String uuid;
    private final String name;
    private final int seed;
    private final USSBaseAnchor anchor;
    /** The digitized station payload (VoidcraftNbt format: blueprint grid + derived stats + program). */
    private final NBTTagCompound payload;
    private USSPosition position;
    private long integrity;
    private int integrityTimer;
    private NBTTagCompound cargo;
    /** The station energy buffer content (EU; starts full - a freshly built station). */
    private long energy;
    /**
     * Repair ticks accumulated so far (0..{@link #TICKS_PER_INTEGRITY}-1 - a second of repair restores 1 integrity).
     */
    private int repairTicks;

    private VoidcraftActiveBase(String uuid, String name, int seed, USSBaseAnchor anchor, NBTTagCompound payload,
        USSPosition position) {
        this.uuid = uuid;
        this.name = name;
        this.seed = seed;
        this.anchor = anchor;
        this.payload = payload;
        this.position = position;
        this.integrity = resolveIntegrity(payload);
        this.integrityTimer = 0;
        this.cargo = null;
        this.energy = energyCapacity();
        this.repairTicks = 0;
    }

    /**
     * Spawn a base at the given position (the anchor band point at spawn time).
     *
     * @param uuid     stable identity
     * @param name     display name (from the blueprint item)
     * @param anchor   the station anchor body
     * @param payload  the digitized station payload (VoidcraftNbt format; the starting time limit is its integrity
     *                 stat, re-derived from its blueprint grid when the stat is missing)
     * @param seed     render / random seed
     * @param position spawn position (the anchor band point)
     * @return the active base
     */
    public static VoidcraftActiveBase launch(String uuid, String name, USSBaseAnchor anchor, NBTTagCompound payload,
        int seed, USSPosition position) {
        return new VoidcraftActiveBase(uuid, name, seed, anchor, payload, position);
    }

    public String uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public int seed() {
        return seed;
    }

    public USSBaseAnchor anchor() {
        return anchor;
    }

    /** The digitized station payload (read-only view of the grid + stats + program). */
    public NBTTagCompound payload() {
        return payload;
    }

    public USSPosition position() {
        return position;
    }

    public void setPosition(USSPosition position) {
        this.position = position;
    }

    /** The stations work yield accumulated so far (null = none yet). */
    public NBTTagCompound cargo() {
        return cargo;
    }

    public void setCargo(NBTTagCompound cargo) {
        this.cargo = cargo;
    }

    /** The current integrity (the remaining time limit, in seconds). */
    public long integrity() {
        return integrity;
    }

    /** The maximum integrity (the time limit at spawn). */
    public long maxIntegrity() {
        return resolveIntegrity(payload);
    }

    /**
     * The maximum integrity of the given station payload: the payload's integrity stat when present, otherwise
     * the stat re-derived from the payload's blueprint grid, otherwise {@link #DEFAULT_INTEGRITY}.
     */
    private static long resolveIntegrity(NBTTagCompound payload) {
        long max = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_INTEGRITY);
        if (max > 0L) {
            return max;
        }
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(payload);
        if (blueprint != null) {
            long derived = blueprint.computeStats().integrity;
            if (derived > 0L) {
                return derived;
            }
        }
        return DEFAULT_INTEGRITY;
    }

    /**
     * Tick the integrity decay (1 integrity per second - the same time-limit rule as the in-flight ship).
     *
     * @return true when the integrity has reached 0 - the base decommissions (the caller removes it, cargo
     *         discarded, as with a lost ship)
     */
    public boolean tickIntegrity() {
        integrityTimer++;
        if (integrityTimer >= TICKS_PER_INTEGRITY) {
            integrityTimer -= TICKS_PER_INTEGRITY;
            integrity--;
        }
        return integrity <= 0;
    }

    /**
     * Repair one integrity point (the repair work command: a second of repair restores one integrity,
     * drawing the repair bay energy at the runtime layer). No effect above the maximum.
     *
     * @param amount integrity to restore
     * @return true when the integrity actually changed
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

    /**
     * Advance the station energy by one tick: generate at the station rate (the solar panel covers), clamped to
     * the station buffer (the POWER_CELL components).
     */
    public void tickEnergy() {
        long gen = energyGen();
        if (gen > 0) {
            energy = Math.min(energyCapacity(), energy + gen);
        }
    }

    /** @return the station energy buffer capacity (EU, from the POWER_CELL components; 0 = no buffer). */
    public long energyCapacity() {
        return Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_ENERGY_BUFFER));
    }

    /** @return the station energy generation rate (EU per tick, from the SOLAR_PANEL covers). */
    public long energyGen() {
        return Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_ENERGY_GEN));
    }

    /** @return the current station energy (EU). */
    public long energy() {
        return energy;
    }

    /** Set the current station energy (clamped to [0, capacity]). */
    public void setEnergy(long value) {
        this.energy = Math.max(0L, Math.min(energyCapacity(), value));
    }

    /**
     * Consume one repair tick: draw the repair energy and accrue a second of repair; a completed second restores
     * one integrity.
     *
     * @return true when the energy was consumed (a repair tick accrued); false when the buffer is too low or the
     *         integrity is already full
     */
    public boolean addRepair() {
        if (energy < REPAIR_DRAW || integrity >= maxIntegrity()) {
            return false;
        }
        energy -= REPAIR_DRAW;
        repairTicks++;
        if (repairTicks >= TICKS_PER_INTEGRITY) {
            repairTicks = 0;
            repair(1);
        }
        return true;
    }

    /**
     * Write the base into a compound tag.
     */
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(TAG_UUID, uuid);
        nbt.setString(TAG_NAME, name);
        nbt.setInteger(TAG_SEED, seed);
        NBTTagCompound anchorTag = new NBTTagCompound();
        anchor.writeToNBT(anchorTag);
        nbt.setTag(TAG_ANCHOR, anchorTag);
        nbt.setLong(TAG_INTEGRITY, integrity);
        nbt.setInteger(TAG_INTEG_TICKS, integrityTimer);
        nbt.setLong(TAG_ENERGY, energy);
        nbt.setInteger(TAG_REPAIR_TICKS, repairTicks);
        if (cargo != null) {
            nbt.setTag(TAG_CARGO, cargo);
        }
        nbt.setTag(TAG_PAYLOAD, payload);
        if (position != null) {
            NBTTagCompound posTag = new NBTTagCompound();
            position.writeToNBT(posTag);
            nbt.setTag(TAG_POS, posTag);
        }
        return nbt;
    }

    /**
     * Read a base from a compound tag.
     *
     * @param nbt the tag written by {@link #writeToNBT()} (null / missing payload -> null)
     * @return the base, or null if the tag is missing or corrupt
     */
    public static VoidcraftActiveBase readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_PAYLOAD) || !nbt.hasKey(TAG_UUID)) {
            return null;
        }
        NBTTagCompound payload = nbt.getCompoundTag(TAG_PAYLOAD);
        if (VoidcraftNbt.readBase(payload) == null) {
            return null;
        }
        String uuid = nbt.getString(TAG_UUID);
        String name = nbt.hasKey(TAG_NAME) ? nbt.getString(TAG_NAME) : "Voidbase";
        int seed = nbt.hasKey(TAG_SEED) ? nbt.getInteger(TAG_SEED) : 0;
        USSBaseAnchor anchor = USSBaseAnchor
            .readFromNBT(nbt.hasKey(TAG_ANCHOR) ? nbt.getCompoundTag(TAG_ANCHOR) : null);
        USSPosition position = nbt.hasKey(TAG_POS) ? USSPosition.readFromNBT(nbt.getCompoundTag(TAG_POS)) : null;
        VoidcraftActiveBase base = new VoidcraftActiveBase(uuid, name, seed, anchor, payload, position);
        if (nbt.hasKey(TAG_INTEGRITY)) {
            base.integrity = Math.max(0L, nbt.getLong(TAG_INTEGRITY));
        }
        if (nbt.hasKey(TAG_INTEG_TICKS)) {
            base.integrityTimer = nbt.getInteger(TAG_INTEG_TICKS);
        }
        if (nbt.hasKey(TAG_ENERGY)) {
            base.setEnergy(nbt.getLong(TAG_ENERGY));
        }
        if (nbt.hasKey(TAG_REPAIR_TICKS)) {
            base.repairTicks = nbt.getInteger(TAG_REPAIR_TICKS);
        }
        if (nbt.hasKey(TAG_CARGO)) {
            base.cargo = nbt.getCompoundTag(TAG_CARGO);
        }
        return base;
    }

    @Override
    public String toString() {
        return "VoidcraftActiveBase[" + uuid + " " + name + " @ " + anchor + " integrity=" + integrity + "]";
    }
}
