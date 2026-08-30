package tectech.voidcraft.uss;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * A Voidbase construction site - the ghost of a station being built in the USS (Voidbase construction
 * framework).
 *
 * <p>
 * The first Constructor to reach the anchor with a matching blueprint creates the site; subsequent
 * Constructors fill it with the parts they carry. The site holds the full blueprint (the gray wireframe the
 * client renders), the anchor, and the per-part progress. When every part of the parts list is delivered the
 * site completes and the finished station spawns at the anchor.
 *
 * <p>
 * Parts progress is keyed by the blueprint partsList() keys ("block.<component>" / "cover.<cover>").
 * Deliveries beyond the required count are consumed but not credited (overflow is discarded, the same rule as
 * the pre-rework infrastructure ledger).
 *
 * <p>
 * Bare-JVM safe (NBT + primitives) for unit tests.
 */
public final class USSBaseSite {

    private static final String TAG_ANCHOR = "vc_site_anchor";
    private static final String TAG_NAME = "vc_site_name";
    private static final String TAG_CREATED = "vc_site_created";
    private static final String TAG_PAYLOAD = "vc_site_payload";
    private static final String TAG_PARTS = "vc_site_parts";
    private static final String TAG_PART_KEY = "key";
    private static final String TAG_PART_RECEIVED = "received";
    private static final String TAG_PART_REQUIRED = "required";
    /** The site's infrastructure cargo (the Dyson Swarm pass — key -> delivered amount, UNBOUNDED). */
    private static final String TAG_CARGO = "vc_site_cargo";
    private static final String TAG_CARGO_KEY = "key";
    private static final String TAG_CARGO_AMOUNT = "amount";
    private static final String TAG_CONSTRUCT_LEG = "vc_site_construct_leg";
    private static final String TAG_CONSTRUCT_TICKS = "vc_site_construct_ticks";
    private static final String TAG_CONSTRUCT_PER_ITEM = "vc_site_construct_per_item";
    private static final String TAG_CONSTRUCT_TOTAL = "vc_site_construct_total";
    private static final String TAG_CONSTRUCT_SEED = "vc_site_construct_seed";

    private final USSBaseAnchor anchor;
    private final String name;
    private final long createdAt;
    private final VoidcraftBlueprint blueprint;
    /** key -> [received, required] (LinkedHashMap = stable display order). */
    private final LinkedHashMap<String, long[]> parts;

    /**
     * The site's infrastructure cargo (the Dyson Swarm pass): key -> delivered amount. UNBOUNDED (unlike the
     * parts, which credit up to the required count) — Power Satellites land here on the constructor's arrival and
     * are handed to the finished base's hold at spawn. Does NOT affect site completion.
     */
    private final LinkedHashMap<String, Long> cargo;

    // The active CONSTRUCT leg (server-authoritative pacing; the client animates the constructor's beam + the
    // site's fill from the synced leg id / total / seed). 0 leg id = no active leg.
    private int constructLegId;
    private long constructTotal;
    private long constructTicksLeft;
    private long constructTicksPerItem;
    private int constructSeed;

    private USSBaseSite(USSBaseAnchor anchor, String name, long createdAt, VoidcraftBlueprint blueprint,
        LinkedHashMap<String, long[]> parts, LinkedHashMap<String, Long> cargo) {
        this.anchor = anchor;
        this.name = name;
        this.createdAt = createdAt;
        this.blueprint = blueprint;
        this.parts = parts;
        this.cargo = cargo;
    }

    /**
     * Create a fresh site at zero progress from a validated base blueprint.
     *
     * @param anchor    the build anchor (STAR / PLANET i / RIPPLE j)
     * @param name      display name (from the blueprint item)
     * @param blueprint the station blueprint
     * @param createdAt epoch millis
     * @return the new site
     */
    public static USSBaseSite create(USSBaseAnchor anchor, String name, VoidcraftBlueprint blueprint, long createdAt) {
        LinkedHashMap<String, long[]> parts = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : blueprint.partsList()
            .entrySet()) {
            parts.put(entry.getKey(), new long[] { 0L, entry.getValue() });
        }
        return new USSBaseSite(anchor, name, createdAt, blueprint, parts, new LinkedHashMap<String, Long>());
    }

    public USSBaseAnchor anchor() {
        return anchor;
    }

    public String name() {
        return name;
    }

    public long createdAt() {
        return createdAt;
    }

    /** The full station blueprint (the wireframe grid + the parts list it requires). */
    public VoidcraftBlueprint blueprint() {
        return blueprint;
    }

    /** @return the number of parts still missing for the given key (0 when complete) */
    public long remaining(String key) {
        long[] p = parts.get(key);
        return p == null ? 0L : Math.max(0L, p[1] - p[0]);
    }

    /** @return parts delivered so far for the given key (0 for unknown keys) */
    public long received(String key) {
        long[] p = parts.get(key);
        return p == null ? 0L : p[0];
    }

    /** @return the required count for the given key (0 for unknown keys) */
    public long required(String key) {
        long[] p = parts.get(key);
        return p == null ? 0L : p[1];
    }

    /**
     * Credit a delivery against the site.
     *
     * @param key    a parts-list key
     * @param amount how many of that part were delivered
     * @return the number actually credited (0 for unknown keys or when already complete - the excess is
     *         consumed, not stored)
     */
    public long add(String key, long amount) {
        long[] p = parts.get(key);
        if (p == null || amount <= 0) {
            return 0L;
        }
        long credited = Math.min(amount, p[1] - p[0]);
        p[0] += credited;
        return credited;
    }

    public long totalRequired() {
        long total = 0;
        for (long[] p : parts.values()) {
            total += p[1];
        }
        return total;
    }

    public long totalReceived() {
        long total = 0;
        for (long[] p : parts.values()) {
            total += p[0];
        }
        return total;
    }

    /** @return true when every part of the parts list is fully delivered */
    public boolean isComplete() {
        for (long[] p : parts.values()) {
            if (p[0] < p[1]) {
                return false;
            }
        }
        return true;
    }

    /** @return overall progress 0..1 (total delivered / total required) */
    public double progressFraction() {
        long required = totalRequired();
        if (required == 0) {
            return 1.0;
        }
        return (double) totalReceived() / (double) required;
    }

    // region CONSTRUCT leg (the Constructor's timed part transfer - one part every ticksPerItem machine ticks)

    /**
     * Start (or restart) the CONSTRUCT leg: a fresh countdown over {@code totalTicks} (one part is deposited
     * every {@code ticksPerItem} machine ticks) owned by the Constructor with the given seed (the client pairs
     * the beam on it). The leg id increments (a new leg even when a second Constructor takes over the site).
     *
     * @param totalTicks   the leg's full duration in machine ticks (clamped to &gt;= 1)
     * @param ticksPerItem the pacing in machine ticks per part (clamped to &gt;= 1)
     * @param seed         the executing Constructor's per-launch seed (0 = none)
     */
    public void startConstructLeg(long totalTicks, long ticksPerItem, int seed) {
        constructLegId++;
        constructTotal = Math.max(1L, totalTicks);
        constructTicksLeft = constructTotal;
        constructTicksPerItem = Math.max(1L, ticksPerItem);
        constructSeed = seed;
    }

    /**
     * Advance the leg's countdown by one machine tick (no-op without an active leg).
     */
    public void tickConstruct() {
        if (constructTicksLeft > 0L) {
            constructTicksLeft--;
        }
    }

    /**
     * Mark the leg over (countdown done, or the site completed first): the leg's pacing fields are zeroed (the
     * id is kept - a new leg re-arms via {@link #startConstructLeg}); the client draws no beam for a zeroed leg.
     */
    public void finishConstructLeg() {
        constructTotal = 0L;
        constructTicksLeft = 0L;
        constructTicksPerItem = 0L;
        constructSeed = 0;
    }

    /** @return the active leg's monotonic id (0 = no active leg - the client's beam animation key) */
    public int constructLegId() {
        return constructLegId;
    }

    /** @return the active leg's full duration in machine ticks (0 = no active leg) */
    public long constructTotal() {
        return constructTotal;
    }

    /** @return machine ticks left in the active leg (0 = no active leg / done) */
    public long constructTicksLeft() {
        return constructTicksLeft;
    }

    /** @return the active leg's pacing in machine ticks per part (0 = no active leg) */
    public long constructTicksPerItem() {
        return constructTicksPerItem;
    }

    /** @return the active leg's Constructor seed (0 = none) */
    public int constructSeed() {
        return constructSeed;
    }

    // endregion

    /** @return an unmodifiable view of the part progress (key -> [received, required]) */
    public Map<String, long[]> partsView() {
        return Collections.unmodifiableMap(parts);
    }

    // region infrastructure cargo (the Dyson Swarm pass — unbounded, does not gate completion)

    /**
     * A loadout key is an INFRASTRUCTURE CARGO key (not a build part) when it carries the {@code item.} prefix —
     * such keys route to the site's cargo map instead of its parts.
     */
    public static boolean isCargoKey(String key) {
        return key != null && key.startsWith("item.");
    }

    /** @return the infrastructure cargo delivered so far for the given key (0 for unknown keys) */
    public long cargoOf(String key) {
        Long v = cargo.get(key);
        return v == null ? 0L : v;
    }

    /**
     * Deposit infrastructure cargo (unbounded — no required-count cap; the excess is NOT discarded).
     *
     * @param key    the cargo key (null → no-op)
     * @param amount the amount to deposit (≤ 0 → no-op)
     * @return the amount actually deposited (0 for a null key or non-positive amount)
     */
    public long addCargo(String key, long amount) {
        if (key == null || amount <= 0L) {
            return 0L;
        }
        Long cur = cargo.get(key);
        cargo.put(key, (cur == null ? 0L : cur) + amount);
        return amount;
    }

    /** @return an unmodifiable view of the infrastructure cargo (key -> delivered amount) */
    public Map<String, Long> cargoView() {
        return Collections.unmodifiableMap(cargo);
    }

    // endregion

    /**
     * Write the site into a compound tag.
     */
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound anchorTag = new NBTTagCompound();
        anchor.writeToNBT(anchorTag);
        nbt.setTag(TAG_ANCHOR, anchorTag);
        nbt.setString(TAG_NAME, name);
        nbt.setLong(TAG_CREATED, createdAt);
        NBTTagCompound payload = new NBTTagCompound();
        VoidcraftNbt.write(payload, blueprint, "site", name, createdAt);
        nbt.setTag(TAG_PAYLOAD, payload);
        NBTTagList partsList = new NBTTagList();
        for (Map.Entry<String, long[]> entry : parts.entrySet()) {
            NBTTagCompound part = new NBTTagCompound();
            part.setString(TAG_PART_KEY, entry.getKey());
            part.setLong(TAG_PART_RECEIVED, entry.getValue()[0]);
            part.setLong(TAG_PART_REQUIRED, entry.getValue()[1]);
            partsList.appendTag(part);
        }
        nbt.setTag(TAG_PARTS, partsList);
        // The infrastructure cargo (the Dyson Swarm pass). Absent = none delivered.
        if (!cargo.isEmpty()) {
            NBTTagList cargoList = new NBTTagList();
            for (Map.Entry<String, Long> entry : cargo.entrySet()) {
                if (entry.getValue() == null || entry.getValue() <= 0L) {
                    continue;
                }
                NBTTagCompound cargoEntry = new NBTTagCompound();
                cargoEntry.setString(TAG_CARGO_KEY, entry.getKey());
                cargoEntry.setLong(TAG_CARGO_AMOUNT, entry.getValue());
                cargoList.appendTag(cargoEntry);
            }
            nbt.setTag(TAG_CARGO, cargoList);
        }
        // The active CONSTRUCT leg (absent = never constructed; a leg in flight survives a server restart).
        if (constructLegId > 0) {
            nbt.setInteger(TAG_CONSTRUCT_LEG, constructLegId);
            nbt.setLong(TAG_CONSTRUCT_TOTAL, constructTotal);
            nbt.setLong(TAG_CONSTRUCT_TICKS, constructTicksLeft);
            nbt.setLong(TAG_CONSTRUCT_PER_ITEM, constructTicksPerItem);
            nbt.setInteger(TAG_CONSTRUCT_SEED, constructSeed);
        }
    }

    /**
     * Read a site from a compound tag.
     *
     * @param nbt source tag
     * @return the site, or null if the tag is missing / corrupt (payload unreadable, parts list inconsistent)
     */
    public static USSBaseSite readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_PAYLOAD) || !nbt.hasKey(TAG_PARTS)) {
            return null;
        }
        NBTTagCompound payload = (NBTTagCompound) nbt.getTag(TAG_PAYLOAD);
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(payload);
        if (blueprint == null) {
            return null;
        }
        NBTTagList partsTag = nbt.getTagList(TAG_PARTS, 10);
        LinkedHashMap<String, long[]> parts = new LinkedHashMap<>();
        for (int i = 0; i < partsTag.tagCount(); i++) {
            NBTTagCompound part = partsTag.getCompoundTagAt(i);
            if (part == null) {
                return null;
            }
            String key = part.getString(TAG_PART_KEY);
            long required = part.getLong(TAG_PART_REQUIRED);
            if (blueprint.partsList()
                .get(key) == null || required <= 0) {
                return null;
            }
            long received = part.getLong(TAG_PART_RECEIVED);
            if (received < 0 || received > required) {
                return null;
            }
            parts.put(key, new long[] { received, required });
        }
        if (parts.size() != blueprint.partsList()
            .size()) {
            return null;
        }
        // The infrastructure cargo (the Dyson Swarm pass). Absent = none delivered.
        LinkedHashMap<String, Long> cargo = new LinkedHashMap<>();
        NBTTagList cargoTag = nbt.getTagList(TAG_CARGO, 10);
        for (int i = 0; i < cargoTag.tagCount(); i++) {
            NBTTagCompound cargoEntry = cargoTag.getCompoundTagAt(i);
            if (cargoEntry == null) {
                continue;
            }
            String cargoKey = cargoEntry.getString(TAG_CARGO_KEY);
            long cargoAmount = cargoEntry.getLong(TAG_CARGO_AMOUNT);
            if (cargoKey == null || cargoKey.isEmpty() || cargoAmount <= 0L) {
                continue;
            }
            Long cur = cargo.get(cargoKey);
            cargo.put(cargoKey, (cur == null ? 0L : cur) + cargoAmount);
        }
        String name = nbt.hasKey(TAG_NAME) ? nbt.getString(TAG_NAME) : "Voidbase";
        long createdAt = nbt.hasKey(TAG_CREATED) ? nbt.getLong(TAG_CREATED) : 0L;
        USSBaseAnchor anchor = USSBaseAnchor
            .readFromNBT(nbt.hasKey(TAG_ANCHOR) ? (NBTTagCompound) nbt.getTag(TAG_ANCHOR) : null);
        USSBaseSite site = new USSBaseSite(anchor, name, createdAt, blueprint, parts, cargo);
        // The active CONSTRUCT leg (corrupt or incomplete leg tags degrade to NO leg - the site keeps its
        // parts progress, a new Constructor leg re-paces it).
        if (nbt.hasKey(TAG_CONSTRUCT_LEG) && nbt.hasKey(TAG_CONSTRUCT_TOTAL)) {
            long total = nbt.getLong(TAG_CONSTRUCT_TOTAL);
            long perItem = nbt.getLong(TAG_CONSTRUCT_PER_ITEM);
            if (total > 0L && perItem > 0L) {
                site.constructLegId = nbt.getInteger(TAG_CONSTRUCT_LEG);
                site.constructTotal = total;
                site.constructTicksLeft = Math.min(Math.max(0L, nbt.getLong(TAG_CONSTRUCT_TICKS)), total);
                site.constructTicksPerItem = perItem;
                site.constructSeed = nbt.getInteger(TAG_CONSTRUCT_SEED);
            }
        }
        return site;
    }

    @Override
    public String toString() {
        return "USSBaseSite[" + anchor + " " + name + " " + totalReceived() + "/" + totalRequired() + "]";
    }
}
