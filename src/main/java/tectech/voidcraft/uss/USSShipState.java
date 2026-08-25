package tectech.voidcraft.uss;

/**
 * Flight states of a Voidcraft while it is out in the Unstable Solar System.
 *
 * <p>
 * Programming framework (Phase C): the states no longer form a fixed OUTBOUND → MINING → RETURNING loop — the
 * ship's PROGRAM decides its legs. OUTBOUND = a travel leg toward a body, MINING = a work leg at a body,
 * RETURNING = the travel leg home ({@code MOVE HOME}) whose completion delivers the ship, HOVERING = holding in
 * place (program finished without a return — the ship stays). A ship is either <em>recoverable</em> (integrity ≥
 * {@code tectech.voidcraft.ship.VoidcraftConstants#RECOVERABLE_INTEGRITY_THRESHOLD}) — the item reappears at the
 * gateway — or expendable: the item is consumed on return. The state is persisted as a stable id in NBT (see
 * {@link VoidcraftActiveShip}).
 */
public enum USSShipState {

    OUTBOUND(0, "tt.voidcraft.ship.state.outbound"),
    MINING(1, "tt.voidcraft.ship.state.mining"),
    RETURNING(2, "tt.voidcraft.ship.state.returning"),
    /** Docked in the gateway (render-only state — the item is in the gateway's ship slot, not in flight). */
    DOCKED(3, "tt.voidcraft.ship.state.docked"),
    /**
     * Holding in place (programming framework, Phase C): the ship's program finished (or was stopped) and the
     * ship is NOT returning — it HOLDS at its current position (user decision: no implicit MOVE HOME at program
     * end; "programming the ship is part of the challenge"). The client renders it hovering at its last body.
     */
    HOVERING(4, "tt.voidcraft.ship.state.hovering");

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
