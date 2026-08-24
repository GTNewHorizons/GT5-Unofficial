package tectech.voidcraft.ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Voidcraft roles, derived from which capability stats the ship has.
 *
 * <p>
 * A ship whose stats contribute to exactly one role is "dedicated" (100% efficiency for that role). Every
 * additional active role is a hybridization and applies the {@link VoidcraftConstants#HYBRID_ROLE_PENALTY}
 * multiplier again (multiplicative). The role set and the penalty are the ship-level "role / hybrid penalty"
 * output required by the implementation plan.
 */
public enum VoidcraftRole {

    MINER(0x1, "tt.voidcraft.role.miner"),
    CONSTRUCTOR(0x2, "tt.voidcraft.role.constructor"),
    STARLIFTER(0x4, "tt.voidcraft.role.starlifter"),
    EXPLORER(0x8, "tt.voidcraft.role.explorer");

    /** Bitmask of all four roles. */
    public static final int ALL_ROLES = MINER.bit | CONSTRUCTOR.bit | STARLIFTER.bit | EXPLORER.bit;

    private final int bit;
    private final String langKey;

    VoidcraftRole(int bit, String langKey) {
        this.bit = bit;
        this.langKey = langKey;
    }

    public int getBit() {
        return bit;
    }

    public String getLangKey() {
        return langKey;
    }

    /**
     * @param roleBitmask packed role mask (see {@link #getBit()})
     * @return true if this role is active
     */
    public boolean isActive(int roleBitmask) {
        return (roleBitmask & bit) != 0;
    }

    /**
     * Compute the active role mask from the ship's capability stats.
     *
     * @param miningPower       total mining power
     * @param scanPower         total scan power
     * @param constructionPower total construction power
     * @param starlifterPower   total starlifter power
     * @return packed role mask (0 = no role, i.e. a pure transport/hauler ship)
     */
    public static int computeRoles(long miningPower, long scanPower, long constructionPower, long starlifterPower) {
        int mask = 0;
        if (miningPower > 0) mask |= MINER.bit;
        if (scanPower > 0) mask |= EXPLORER.bit;
        if (constructionPower > 0) mask |= CONSTRUCTOR.bit;
        if (starlifterPower > 0) mask |= STARLIFTER.bit;
        return mask;
    }

    /**
     * @param roleBitmask packed role mask
     * @return number of active roles (0-4)
     */
    public static int countRoles(int roleBitmask) {
        int count = 0;
        for (VoidcraftRole role : values()) {
            if (role.isActive(roleBitmask)) count++;
        }
        return count;
    }

    /**
     * Hybrid penalty: efficiency multiplier for a ship with {@code activeRoleCount} roles.
     *
     * <p>
     * 0 roles (pure hauler) and 1 role (dedicated) both get 1.0 — the penalty only kicks in from the second active
     * role onward, matching "dedicated ships are most efficient, hybrids pay per extra role".
     *
     * @param activeRoleCount number of active roles
     * @return efficiency multiplier in (0, 1]
     */
    public static double efficiencyMultiplier(int activeRoleCount) {
        if (activeRoleCount <= 1) {
            return 1.0;
        }
        return Math.pow(VoidcraftConstants.HYBRID_ROLE_PENALTY, activeRoleCount - 1);
    }

    /**
     * @param roleBitmask packed role mask
     * @return the active roles in enum order (never null)
     */
    public static List<VoidcraftRole> activeRoles(int roleBitmask) {
        List<VoidcraftRole> result = new ArrayList<>(values().length);
        for (VoidcraftRole role : values()) {
            if (role.isActive(roleBitmask)) {
                result.add(role);
            }
        }
        return Collections.unmodifiableList(result);
    }
}
