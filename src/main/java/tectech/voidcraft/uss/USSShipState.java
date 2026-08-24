package tectech.voidcraft.uss;

/**
 * Flight states of a Voidcraft miner while it is out on a mission (plan Phase 3).
 *
 * <p>
 * OUTBOUND → MINING → RETURNING is the deterministic mission loop; delivery happens when the ship finishes the
 * RETURNING leg. A ship is either <em>recoverable</em> (integrity ≥
 * {@code tectech.voidcraft.ship.VoidcraftConstants#RECOVERABLE_INTEGRITY_THRESHOLD}) — the item reappears at the
 * gateway — or expendable: the item is consumed on return. The state is persisted as a stable id in NBT (format
 * versioned — see {@link VoidcraftActiveShip}).
 */
public enum USSShipState {

    OUTBOUND(0, "tt.voidcraft.ship.state.outbound"),
    MINING(1, "tt.voidcraft.ship.state.mining"),
    RETURNING(2, "tt.voidcraft.ship.state.returning"),
    /** Docked in the gateway (render-only state — the item is in the gateway's ship slot, not in flight). */
    DOCKED(3, "tt.voidcraft.ship.state.docked");

    private final int id;
    private final String langKey;

    USSShipState(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public int getId() {
        return id;
    }

    public String getLangKey() {
        return langKey;
    }

    /**
     * @param id persisted state id.
     * @return the matching state, or null when the id is unknown (corrupt NBT).
     */
    public static USSShipState byId(int id) {
        for (USSShipState state : values()) {
            if (state.id == id) return state;
        }
        return null;
    }
}
