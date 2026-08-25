package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The Unstable Solar System model (EoH rework, Phase 2 vertical slice).
 *
 * <p>
 * Holds the star lifecycle: {@link USSState#COLD} until a USS Controller ignites it, then {@link USSState#IGNITED}
 * with an item-selected star class (Phase 4 pass 1 — the controller item decides the star type) and a ticking-down
 * lifespan. When the lifespan reaches zero the star burns out, the
 * controller is consumed (one controller = one star life) and the model returns to {@link USSState#COLD}. The
 * {@code ships} list is the Phase 3 docking registry — a placeholder for this slice.
 *
 * <p>
 * Pure data + NBT (de)serialization, no Minecraft runtime — so it is unit-testable (see
 * {@code tectech.voidcraft.uss.VoidcraftUSSTest}). Immutable: state transitions return new instances.
 *
 * @see USSConstants
 * @see docs/Voidcraft_Implementation_Plan.md
 */
public final class VoidcraftUSS {

    /** NBT format version (no backwards-compatibility required — project directive). */
    public static final int NBT_FORMAT_VERSION = 1;

    // NBT tags (voidcraft "vc_" naming convention).
    public static final String TAG_FORMAT = "vc_uss_format";
    public static final String TAG_STATE = "vc_uss_state";
    public static final String TAG_TIER = "vc_uss_tier";
    public static final String TAG_STAR_TYPE = "vc_uss_star_type";
    public static final String TAG_LIFESPAN = "vc_uss_lifespan";
    public static final String TAG_IGNITED_AT = "vc_uss_ignited_at";
    public static final String TAG_SHIPS = "vc_uss_ships";
    /** Per-planet ore reserves (the mechanics pass — depletion). A list indexed by planet index. */
    public static final String TAG_PLANET_RESERVES = "vc_uss_planet_reserves";
    /**
     * Scanned spacetime-ripple point indices (the Explorer pass — which of the 343 grid points have been revealed).
     * An int array of point indices (0..342). Empty = nothing scanned yet (all ripples still hidden).
     */
    public static final String TAG_RIPPLE_SCANNED = "vc_uss_ripple_scanned";
    /**
     * The USS global variable space (programming framework, Phase C) — the "global variable list" ships (and the
     * future UI / external machines) read and write. A SPARSE list of {@code {i, s}} compounds (only written slots).
     */
    public static final String TAG_VARIABLES = "vc_uss_variables";
    /** Within one planet-reserve compound: the ore list (material + remaining amount). */
    public static final String RESERVE_TAG_ORES = "ores";
    /** One ore entry: the GT material name. */
    public static final String RESERVE_TAG_MATERIAL = "m";
    /** One ore entry: the remaining amount (items). */
    public static final String RESERVE_TAG_AMOUNT = "a";

    private final USSState state;
    private final int tier;
    private final USSStarType starType;
    private final long lifespanRemaining;
    private final long ignitedAt;
    private final List<String> ships;
    /**
     * Per-planet ore reserves (the mechanics pass). Index = planet index (stable within a system, since the system
     * is a pure function of star type + ignition timestamp). A null entry means "not yet mined" (the reserve is
     * initialized from the planet definition on first mine).
     */
    private final List<PlanetReserve> planetReserves;

    /**
     * Scanned spacetime-ripple point indices (the Explorer pass). A point is added to this set the moment an
     * Explorer finishes scanning it (revealing whether it is a ripple). Starts EMPTY — all ripples are hidden at
     * USS creation (user spec). Immutable: {@link #withRippleScanned} returns a new instance.
     */
    private final java.util.Set<Integer> scannedRipples;

    /**
     * The USS global variable space (programming framework, Phase C): 256 string slots shared by the whole fleet —
     * the ships' in/out channel ("This allows sending commands to Voidcraft externally and communicating data out
     * of the USS"). Immutable — {@link #withVariables} returns a new instance.
     */
    private final USSVariableSpace variableSpace;

    private VoidcraftUSS(USSState state, int tier, USSStarType starType, long lifespanRemaining, long ignitedAt,
        List<String> ships, List<PlanetReserve> planetReserves, java.util.Set<Integer> scannedRipples,
        USSVariableSpace variableSpace) {
        this.state = state;
        this.tier = tier;
        this.starType = starType;
        this.lifespanRemaining = lifespanRemaining;
        this.ignitedAt = ignitedAt;
        this.ships = ships == null ? new ArrayList<>() : new ArrayList<>(ships);
        this.planetReserves = planetReserves == null ? new ArrayList<>() : new ArrayList<>(planetReserves);
        this.scannedRipples = scannedRipples == null ? new java.util.TreeSet<Integer>()
            : new java.util.TreeSet<>(scannedRipples);
        this.variableSpace = variableSpace == null ? USSVariableSpace.fresh() : variableSpace;
    }

    /** A fresh-space constructor (cold / ignited / toCold — a new system has a fresh variable space). */
    private VoidcraftUSS(USSState state, int tier, USSStarType starType, long lifespanRemaining, long ignitedAt,
        List<String> ships, List<PlanetReserve> planetReserves, java.util.Set<Integer> scannedRipples) {
        this(
            state,
            tier,
            starType,
            lifespanRemaining,
            ignitedAt,
            ships,
            planetReserves,
            scannedRipples,
            USSVariableSpace.fresh());
    }

    /**
     * One planet's remaining ore reserve: a map of GT material → remaining amount (items). Immutable — mining
     * returns a new instance.
     *
     * <p>
     * Bare-JVM safe: only {@link gregtech.api.enums.Materials} data (no Forge fluid/block objects).
     */
    public static final class PlanetReserve {

        private final Map<gregtech.api.enums.Materials, Long> remaining;

        PlanetReserve(Map<gregtech.api.enums.Materials, Long> remaining) {
            this.remaining = new LinkedHashMap<>(remaining);
        }

        /**
         * @param material the ore material (null → 0)
         * @return the remaining amount (0 when the material is absent)
         */
        public long remaining(gregtech.api.enums.Materials material) {
            if (material == null) {
                return 0L;
            }
            Long v = remaining.get(material);
            return v == null ? 0L : v;
        }

        /**
         * @return an unmodifiable view of the remaining amounts
         */
        public Map<gregtech.api.enums.Materials, Long> getRemaining() {
            return Collections.unmodifiableMap(remaining);
        }

        /**
         * @return true when no ore has a positive remaining amount
         */
        public boolean isEmpty() {
            for (Long v : remaining.values()) {
                if (v != null && v > 0L) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @param material the ore material
         * @param amount   the amount to subtract (clamped at 0)
         * @return a new reserve with the amount subtracted from the material (all other entries preserved)
         */
        public PlanetReserve mine(gregtech.api.enums.Materials material, long amount) {
            Map<gregtech.api.enums.Materials, Long> next = new LinkedHashMap<>(remaining);
            long current = next.getOrDefault(material, 0L);
            next.put(material, Math.max(0L, current - amount));
            return new PlanetReserve(next);
        }

        /**
         * @return a new reserve with the material's remaining set to the given amount (added if absent)
         */
        public PlanetReserve setAmount(gregtech.api.enums.Materials material, long amount) {
            Map<gregtech.api.enums.Materials, Long> next = new LinkedHashMap<>(remaining);
            next.put(material, Math.max(0L, amount));
            return new PlanetReserve(next);
        }

        public void writeToNBT(NBTTagCompound nbt) {
            if (nbt == null) {
                return;
            }
            NBTTagList ores = new NBTTagList();
            for (Map.Entry<gregtech.api.enums.Materials, Long> e : remaining.entrySet()) {
                NBTTagCompound ore = new NBTTagCompound();
                ore.setString(
                    RESERVE_TAG_MATERIAL,
                    e.getKey()
                        .getName());
                ore.setLong(RESERVE_TAG_AMOUNT, e.getValue());
                ores.appendTag(ore);
            }
            nbt.setTag(RESERVE_TAG_ORES, ores);
        }

        /**
         * Initialize a planet's reserve from its registered definition: each ore's amount (in millions,
         * {@code ore.getAmount() × 1_000_000}) multiplied by the square of the planet size (user spec: "The ore
         * amount per planet is multiplied by the square of the planet size").
         *
         * @param definition the planet definition (null → empty reserve)
         * @param planetSize the planet's sampled size (the hologram scale; negative clamped to 0)
         * @return the initial reserve (never null)
         */
        public static PlanetReserve fromPlanet(USSPlanetDefinition definition, double planetSize) {
            Map<gregtech.api.enums.Materials, Long> map = new LinkedHashMap<>();
            if (definition != null) {
                double sizeSq = Math.max(0.0, planetSize) * Math.max(0.0, planetSize);
                for (USSPlanetOre ore : definition.getOres()) {
                    if (ore.getOreType() == null || ore.getOreType() == gregtech.api.enums.Materials._NULL) {
                        continue;
                    }
                    long amount = (long) (ore.getAmount() * 1_000_000L * sizeSq);
                    map.put(ore.getOreType(), Math.max(0L, amount));
                }
            }
            return new PlanetReserve(map);
        }

        public static PlanetReserve readFromNBT(NBTTagCompound nbt) {
            Map<gregtech.api.enums.Materials, Long> map = new LinkedHashMap<>();
            if (nbt != null) {
                NBTTagList ores = nbt.getTagList(RESERVE_TAG_ORES, 10);
                for (int i = 0; i < ores.tagCount(); i++) {
                    NBTTagCompound ore = ores.getCompoundTagAt(i);
                    if (ore == null) {
                        continue;
                    }
                    gregtech.api.enums.Materials m = gregtech.api.enums.Materials
                        .get(ore.getString(RESERVE_TAG_MATERIAL));
                    if (m == null || m == gregtech.api.enums.Materials._NULL) {
                        continue;
                    }
                    map.put(m, Math.max(0L, ore.getLong(RESERVE_TAG_AMOUNT)));
                }
            }
            return new PlanetReserve(map);
        }
    }

    /**
     * @return a fresh COLD model (the state a newly built or freshly reloaded system starts in).
     */
    public static VoidcraftUSS cold() {
        return new VoidcraftUSS(USSState.COLD, -1, null, 0L, 0L, null, null, null);
    }

    /**
     * Ignite the system at the given tier (clamped to 0–8) with the explicitly given star class and its full
     * lifespan (Phase 4 pass 1: the star class comes from the IGNITION ITEM — the controller in the controller
     * slot — not from the tier; the tier still scales the star's rendering and the miner ore band).
     *
     * @param tier      spacetime compression tier of the structure.
     * @param starType  the star class selected by the ignition item (Phase 4 pass 1).
     * @param nowMillis ignition timestamp (epoch millis), informational.
     * @return the ignited model.
     */
    public static VoidcraftUSS ignite(int tier, USSStarType starType, long nowMillis) {
        tier = USSConstants.clampTier(tier);
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE; // defensive: an unknown controller is rejected by the machine anyway
        }
        return new VoidcraftUSS(
            USSState.IGNITED,
            tier,
            starType,
            USSConstants.lifespanForType(starType),
            nowMillis,
            null,
            null,
            null); // a fresh system has no ore reserves yet and no scanned ripples (all hidden)
    }

    /**
     * @return this model with a new remaining lifespan (all other fields preserved).
     */
    public VoidcraftUSS withLifespan(long remaining) {
        return new VoidcraftUSS(
            state,
            tier,
            starType,
            remaining,
            ignitedAt,
            ships,
            planetReserves,
            scannedRipples,
            variableSpace);
    }

    /**
     * @return this model with the given ship UUID registered for docking (Phase 3; ignored duplicates).
     */
    public VoidcraftUSS withShip(String shipUuid) {
        List<String> next = new ArrayList<>(ships);
        if (shipUuid != null && !next.contains(shipUuid)) {
            next.add(shipUuid);
        }
        return new VoidcraftUSS(
            state,
            tier,
            starType,
            lifespanRemaining,
            ignitedAt,
            next,
            planetReserves,
            scannedRipples,
            variableSpace);
    }

    /**
     * @return a COLD copy of this model (star gone; the controller is consumed by the machine, not by this method).
     *         The ore reserves are discarded (a re-ignition is a new system with fresh reserves).
     */
    public VoidcraftUSS toCold() {
        return new VoidcraftUSS(USSState.COLD, -1, null, 0L, 0L, null, null, null);
    }

    /**
     * @param planetIndex the planet index (the stable identity within the system)
     * @return that planet's ore reserve, or null when it has not been mined yet
     */
    public PlanetReserve getPlanetReserve(int planetIndex) {
        if (planetIndex < 0 || planetIndex >= planetReserves.size()) {
            return null;
        }
        return planetReserves.get(planetIndex);
    }

    /**
     * @return an unmodifiable view of the per-planet ore reserves (index = planet index; null entries = not yet
     *         mined)
     */
    public List<PlanetReserve> getPlanetReserves() {
        return Collections.unmodifiableList(planetReserves);
    }

    /**
     * @param planetIndex the planet index
     * @param reserve     the new reserve for that planet (null clears it)
     * @return a new model with that planet's reserve updated (the list is grown with nulls as needed; all other
     *         fields preserved)
     */
    public VoidcraftUSS withPlanetReserve(int planetIndex, PlanetReserve reserve) {
        if (planetIndex < 0) {
            return this;
        }
        List<PlanetReserve> next = new ArrayList<>(planetReserves);
        while (next.size() <= planetIndex) {
            next.add(null);
        }
        next.set(planetIndex, reserve);
        return new VoidcraftUSS(
            state,
            tier,
            starType,
            lifespanRemaining,
            ignitedAt,
            ships,
            next,
            scannedRipples,
            variableSpace);
    }

    /**
     * @param index the ripple point index (0..342)
     * @return true if that point has been scanned (revealed) by an Explorer
     */
    public boolean isRippleScanned(int index) {
        return scannedRipples.contains(index);
    }

    /**
     * @return an unmodifiable view of the scanned ripple point indices (0..342). Empty = all ripples still hidden.
     */
    public java.util.Set<Integer> getScannedRipples() {
        return java.util.Collections.unmodifiableSet(scannedRipples);
    }

    /**
     * @param index the ripple point index (0..342) an Explorer has just finished scanning
     * @return a new model with that point marked scanned (revealed); all other fields preserved. No-op if
     *         {@code index} is negative.
     */
    public VoidcraftUSS withRippleScanned(int index) {
        if (index < 0) {
            return this;
        }
        java.util.Set<Integer> next = new java.util.TreeSet<>(scannedRipples);
        next.add(index);
        return new VoidcraftUSS(
            state,
            tier,
            starType,
            lifespanRemaining,
            ignitedAt,
            ships,
            planetReserves,
            next,
            variableSpace);
    }

    // region the USS global variable space (programming framework, Phase C)

    /** @return the USS global variable space (never null — 256 string slots, shared by the whole fleet). */
    public USSVariableSpace getVariables() {
        return variableSpace;
    }

    /**
     * @param space the new variable space (null → a fresh empty space)
     * @return a new model with the variable space replaced; all other fields preserved
     */
    public VoidcraftUSS withVariables(USSVariableSpace space) {
        return new VoidcraftUSS(
            state,
            tier,
            starType,
            lifespanRemaining,
            ignitedAt,
            ships,
            planetReserves,
            scannedRipples,
            space);
    }

    // endregion

    public USSState getState() {
        return state;
    }

    /**
     * @return the spacetime compression tier at ignition (0–8), or -1 while COLD.
     */
    public int getTier() {
        return tier;
    }

    /**
     * @return the star class, or null while COLD.
     */
    public USSStarType getStarType() {
        return starType;
    }

    /**
     * @return remaining lifespan in machine ticks (0 while COLD).
     */
    public long getLifespanRemaining() {
        return lifespanRemaining;
    }

    /**
     * @return ignition timestamp in epoch millis (0 while COLD).
     */
    public long getIgnitedAt() {
        return ignitedAt;
    }

    /**
     * @return an unmodifiable view of the docked ship UUIDs (Phase 3 registry).
     */
    public List<String> getShips() {
        return Collections.unmodifiableList(ships);
    }

    public boolean isIgnited() {
        return state == USSState.IGNITED;
    }

    // region NBT

    /**
     * Serialize this model into the given tag compound (writes the format version plus all fields).
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        nbt.setInteger(TAG_FORMAT, NBT_FORMAT_VERSION);
        nbt.setInteger(TAG_STATE, state.ordinal());
        nbt.setInteger(TAG_TIER, tier);
        if (starType != null) {
            nbt.setInteger(TAG_STAR_TYPE, starType.ordinal());
        }
        nbt.setLong(TAG_LIFESPAN, lifespanRemaining);
        nbt.setLong(TAG_IGNITED_AT, ignitedAt);
        if (!ships.isEmpty()) {
            NBTTagCompound shipsTag = new NBTTagCompound();
            for (int i = 0; i < ships.size(); i++) {
                shipsTag.setString(String.valueOf(i), ships.get(i));
            }
            nbt.setTag(TAG_SHIPS, shipsTag);
        }
        // The per-planet ore reserves (the mechanics pass). Only written when at least one planet has been mined.
        boolean anyReserve = false;
        for (PlanetReserve r : planetReserves) {
            if (r != null) {
                anyReserve = true;
                break;
            }
        }
        if (anyReserve) {
            NBTTagList reserves = new NBTTagList();
            for (PlanetReserve r : planetReserves) {
                NBTTagCompound entry = new NBTTagCompound();
                if (r != null) {
                    r.writeToNBT(entry);
                }
                reserves.appendTag(entry);
            }
            nbt.setTag(TAG_PLANET_RESERVES, reserves);
        }
        // The scanned spacetime-ripple point indices (the Explorer pass). Only written when at least one is scanned.
        if (!scannedRipples.isEmpty()) {
            int[] arr = new int[scannedRipples.size()];
            int i = 0;
            for (Integer v : scannedRipples) {
                arr[i++] = v;
            }
            nbt.setIntArray(TAG_RIPPLE_SCANNED, arr);
        }
        // The global variable space (programming framework, Phase C). SPARSE — only written slots (a fresh space
        // writes nothing, keeping a COLD / fresh USS tag lean).
        if (variableSpace != null && variableSpace.writtenCount() > 0) {
            nbt.setTag(TAG_VARIABLES, variableSpace.writeToNBT());
        }
    }

    /**
     * Deserialize a model from the given tag compound.
     *
     * @param nbt tag compound as written by {@link #writeToNBT(NBTTagCompound)} (may be empty).
     * @return the model, or null if the input is absent or corrupt (wrong format version, invalid enum,
     *         inconsistent ignited state).
     */
    public static VoidcraftUSS readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_FORMAT) || nbt.getInteger(TAG_FORMAT) != NBT_FORMAT_VERSION) {
            return null;
        }
        int stateOrdinal = nbt.getInteger(TAG_STATE);
        if (stateOrdinal < 0 || stateOrdinal >= USSState.values().length) {
            return null;
        }
        USSState state = USSState.values()[stateOrdinal];
        if (state == USSState.COLD) {
            return cold();
        }
        // IGNITED: every field must be present and consistent.
        int tier = nbt.getInteger(TAG_TIER);
        if (tier < USSConstants.MIN_TIER || tier > USSConstants.MAX_TIER) {
            return null;
        }
        if (!nbt.hasKey(TAG_STAR_TYPE)) {
            return null;
        }
        int starOrdinal = nbt.getInteger(TAG_STAR_TYPE);
        if (starOrdinal < 0 || starOrdinal >= USSStarType.values().length) {
            return null;
        }
        USSStarType starType = USSStarType.values()[starOrdinal];
        long lifespan = nbt.getLong(TAG_LIFESPAN);
        if (lifespan <= 0) {
            return null;
        }
        long ignitedAt = nbt.hasKey(TAG_IGNITED_AT) ? nbt.getLong(TAG_IGNITED_AT) : 0L;
        List<String> ships = new ArrayList<>();
        NBTTagCompound shipsTag = nbt.getCompoundTag(TAG_SHIPS);
        for (int i = 0; shipsTag.hasKey(String.valueOf(i)); i++) {
            String ship = shipsTag.getString(String.valueOf(i));
            if (!ship.isEmpty()) {
                ships.add(ship);
            }
        }
        // The per-planet ore reserves (the mechanics pass). Null entries = not yet mined.
        List<PlanetReserve> reserves = new ArrayList<>();
        NBTTagList reservesTag = nbt.getTagList(TAG_PLANET_RESERVES, 9);
        for (int i = 0; i < reservesTag.tagCount(); i++) {
            NBTTagCompound entry = reservesTag.getCompoundTagAt(i);
            reserves.add(PlanetReserve.readFromNBT(entry));
        }
        // The scanned spacetime-ripple point indices (the Explorer pass). Absent = nothing scanned.
        java.util.Set<Integer> scanned = new java.util.TreeSet<>();
        int[] scannedArr = nbt.getIntArray(TAG_RIPPLE_SCANNED);
        for (int v : scannedArr) {
            scanned.add(v);
        }
        // The global variable space (programming framework, Phase C). Absent tag = a fresh empty space.
        // (1.7.10: a list of COMPOUNDS has element type 10 — getTagList(key, 9) would silently return empty.)
        USSVariableSpace variables = USSVariableSpace
            .readFromNBT(nbt.hasKey(TAG_VARIABLES) ? nbt.getTagList(TAG_VARIABLES, 10) : null);
        return new VoidcraftUSS(
            USSState.IGNITED,
            tier,
            starType,
            lifespan,
            ignitedAt,
            ships,
            reserves,
            scanned,
            variables);
    }

    // endregion
}
