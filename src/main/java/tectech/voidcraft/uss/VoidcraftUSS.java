package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

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

    private final USSState state;
    private final int tier;
    private final USSStarType starType;
    private final long lifespanRemaining;
    private final long ignitedAt;
    private final List<String> ships;

    private VoidcraftUSS(USSState state, int tier, USSStarType starType, long lifespanRemaining, long ignitedAt,
        List<String> ships) {
        this.state = state;
        this.tier = tier;
        this.starType = starType;
        this.lifespanRemaining = lifespanRemaining;
        this.ignitedAt = ignitedAt;
        this.ships = ships == null ? new ArrayList<>() : new ArrayList<>(ships);
    }

    /**
     * @return a fresh COLD model (the state a newly built or freshly reloaded system starts in).
     */
    public static VoidcraftUSS cold() {
        return new VoidcraftUSS(USSState.COLD, -1, null, 0L, 0L, null);
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
            null);
    }

    /**
     * @return this model with a new remaining lifespan (all other fields preserved).
     */
    public VoidcraftUSS withLifespan(long remaining) {
        return new VoidcraftUSS(state, tier, starType, remaining, ignitedAt, ships);
    }

    /**
     * @return this model with the given ship UUID registered for docking (Phase 3; ignored duplicates).
     */
    public VoidcraftUSS withShip(String shipUuid) {
        List<String> next = new ArrayList<>(ships);
        if (shipUuid != null && !next.contains(shipUuid)) {
            next.add(shipUuid);
        }
        return new VoidcraftUSS(state, tier, starType, lifespanRemaining, ignitedAt, next);
    }

    /**
     * @return a COLD copy of this model (star gone; the controller is consumed by the machine, not by this method).
     */
    public VoidcraftUSS toCold() {
        return new VoidcraftUSS(USSState.COLD, -1, null, 0L, 0L, null);
    }

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
        return new VoidcraftUSS(USSState.IGNITED, tier, starType, lifespan, ignitedAt, ships);
    }

    // endregion
}
