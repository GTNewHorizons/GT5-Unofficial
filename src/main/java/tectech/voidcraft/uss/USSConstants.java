package tectech.voidcraft.uss;

/**
 * Balance tables for the Unstable Solar System (EoH rework, Phase 2 vertical slice).
 *
 * <p>
 * The star-class / lifespan tables are <em>placeholder</em> values for the vertical slice; they live here as
 * pure, unit-testable helpers so the Phase 3–6 work (star death, Star Igniter, energy model) can iterate on
 * balance without touching machine code. See {@code docs/Voidcraft_Implementation_Plan.md}.
 *
 * <p>
 * Tier numbers follow the legacy Eye of Harmony spacetime compression casings (metas 0–8).
 */
public final class USSConstants {

    /** Lowest valid spacetime compression tier (casing meta 0). */
    public static final int MIN_TIER = 0;

    /** Highest valid spacetime compression tier (casing meta 8). */
    public static final int MAX_TIER = 8;

    /**
     * Phase 4 pass 5: how many ships a single Unstable Solar System may host in flight simultaneously — a LARGE
     * fleet (dozens–hundreds, user request). All ships share one render block (a fleet anchor) that draws the
     * whole swarm, and each ship hovers at its own deterministic spread position (see {@link USSFleetOrbit}), so
     * the cost of a 100-ship fleet is ~100 small draw calls, not 100 world blocks. The USS rejects a launch once
     * every slot is occupied (the gateway then reports "fully loaded").
     */
    public static final int MAX_SHIPS_PER_USS = 100;

    private USSConstants() {
        throw new AssertionError("Constants holder");
    }

    /**
     * Clamps any value into the valid tier range (the structure only accepts casing metas 0–8 anyway, but the model
     * must never see a value outside that range).
     *
     * @param tier arbitrary tier value.
     * @return {@code min(MIN_TIER, max(MAX_TIER, tier))}.
     */
    public static int clampTier(int tier) {
        return Math.max(MIN_TIER, Math.min(MAX_TIER, tier));
    }

    /**
     * Lifespan in machine ticks for a star class (placeholder balance table — Phase 4 pass 1 the star class is
     * chosen by the ignition item, so the table is keyed by type):
     * <ul>
     * <li>{@link USSStarType#MAIN_SEQUENCE}: 120_000 ticks (10 in-game days)</li>
     * <li>{@link USSStarType#WHITE_DWARF}: 300_000 ticks (25 in-game days)</li>
     * <li>{@link USSStarType#SUPERMASSIVE}: 60_000 ticks (5 in-game days)</li>
     * </ul>
     *
     * @param starType the star class (null → main sequence, defensive).
     * @return the lifespan in ticks (always &gt; 0).
     */
    public static long lifespanForType(USSStarType starType) {
        switch (starType == null ? USSStarType.MAIN_SEQUENCE : starType) {
            case WHITE_DWARF:
                return 300_000L;
            case SUPERMASSIVE:
                return 60_000L;
            default:
                return 120_000L;
        }
    }

    /**
     * Reserved for the Phase 6 energy model (EU/t the ignited star draws from the global energy map while burning).
     * NOT consumed by the Phase 2 vertical slice — documented here so the table is complete in one place.
     */
    public static long starDrawEUt(USSStarType starType) {
        switch (starType == null ? USSStarType.MAIN_SEQUENCE : starType) {
            case WHITE_DWARF:
                return 256L;
            case SUPERMASSIVE:
                return 1024L;
            default:
                return 64L;
        }
    }

    // region Phase 3 ship flight (placeholder balance — creative-loop friendly, no RNG)

    /** Travel-leg time floor in machine ticks (1 s). */
    public static final long TRAVEL_TICKS_MIN = 20L;

    /** Travel-leg time cap in machine ticks (30 s) — keeps the creative loop fast regardless of ship speed. */
    public static final long TRAVEL_TICKS_MAX = 600L;

    /**
     * Mining-duration floor in machine ticks (4.5 s). Pass 7: the mining leg must be PERCEPTIBLE — with the old
     * 5-tick floor a high-mining-power ship "mined" for 0.25 s, which at 1/16 scale reads as "the ship reaches
     * its destination and turns right back without mining anything" (user report).
     */
    public static final long MINE_TICKS_MIN = 90L;

    /** Mining-duration cap in machine ticks (30 s — the creative loop stays minutes, not hours). */
    public static final long MINE_TICKS_MAX = 600L;

    /**
     * Mining power above which mining time no longer shrinks. Pass 7: at saturation the leg is
     * {@code MINE_TICKS_MAX / MINE_POWER_SATURATION} = 75 ticks (3.75 s) — still a clearly visible mining hover
     * over the planet, even for the strongest creative miner.
     */
    public static final long MINE_POWER_SATURATION = 8L;

    /**
     * Pass 7/8/9 — the mining hover height: 0.5 blocks above the target planet's SURFACE (user spec: "the
     * destination 0.5 blocks above that planet"). Pass 9: the rendered planet is a unit CUBE of size spec.scale
     * (0.35–0.75 = its edge length; its surface sits 0.5·scale above its center), so the renderer adds HALF that
     * scale on top of this constant — a flat 0.5 over the center would swallow the ship (and the whole laser
     * beam) inside the planets. The ship tracks the planet's live rendered position while working.
     */
    public static final double HOVER_ABOVE_PLANET = 0.5;

    /**
     * Pass 7 — the Starlifter hover height: 2.5 blocks above the star's center (user spec). Clears the largest
     * star (radius ≤ 1.4) with margin.
     */
    public static final double HOVER_ABOVE_STAR = 2.5;

    /** Ore-dust multiplier: a miner carries {@code miningPower * MINER_ORE_DUST_FACTOR} dust of each tier ore. */
    public static final long MINER_ORE_DUST_FACTOR = 10L;

    /** Cap on the ore-dust amount per cargo entry (protects the pool and the save file). */
    public static final long MINER_ORE_DUST_CAP = 10_000L;

    /**
     * One travel leg (USS edge → target, or target → USS edge) in machine ticks. Faster ships travel shorter; the
     * result is clamped to {@link #TRAVEL_TICKS_MIN}–{@link #TRAVEL_TICKS_MAX}.
     *
     * @param ship speed in [0, 1] (from the ship stats).
     * @return the leg time in ticks (always &gt; 0).
     */
    public static long travelTicks(double speed) {
        if (speed <= 0.0) {
            return TRAVEL_TICKS_MAX;
        }
        long ticks = (long) (300.0 / speed);
        return Math.max(TRAVEL_TICKS_MIN, Math.min(TRAVEL_TICKS_MAX, ticks));
    }

    /**
     * Mining duration in machine ticks for a given mining power: {@code MINE_TICKS_MAX / min(power, saturation)},
     * clamped to {@link #MINE_TICKS_MIN}–{@link #MINE_TICKS_MAX} (pass 7: 3.75 s–30 s window, always a visible
     * mining hover).
     *
     * @param miningPower the ship's total mining power (&lt;= 0 uses the cap — a broken ship still "mines" slowly).
     * @return the mining time in ticks (always &gt; 0).
     */
    public static long mineTicks(long miningPower) {
        long power = Math.max(1L, Math.min(miningPower, MINE_POWER_SATURATION));
        long ticks = MINE_TICKS_MAX / power;
        return Math.max(MINE_TICKS_MIN, Math.min(MINE_TICKS_MAX, ticks));
    }

    /**
     * Ore-dust amount a miner carries per tier ore: {@code min(miningPower * factor, cap)} (at least 1).
     *
     * @param miningPower the ship's total mining power.
     * @return the dust amount (always &gt;= 1).
     */
    public static long minerOreAmount(long miningPower) {
        long amount = Math.max(1L, miningPower) * MINER_ORE_DUST_FACTOR;
        return Math.min(amount, MINER_ORE_DUST_CAP);
    }

    /**
     * Stone-dust amount a miner carries: the legacy EoH multiplier
     * ({@link USSVeinMath#STONE_DUST_MULTIPLIER}) applied to the ore-dust amount.
     *
     * @param miningPower the ship's total mining power.
     * @return the stone dust amount (always &gt;= 1).
     */
    public static long minerStoneDustAmount(long miningPower) {
        return minerOreAmount(miningPower) * USSVeinMath.STONE_DUST_MULTIPLIER;
    }

    // region Phase 4 pass 1 Starlifter (fluid production on top of the miner item cargo)

    /**
     * Starlifter plasma multiplier: a starlifter carries {@code miningPower * STARLIFTER_PLASMA_FACTOR} mB of Stellar
     * Plasma.
     */
    public static final long STARLIFTER_PLASMA_FACTOR = 1_000L;

    /** Cap on the Stellar Plasma amount per cargo entry (mB; keeps the bay pool and the save file bounded). */
    public static final long STARLIFTER_PLASMA_CAP = 10_000_000L;

    /**
     * Stellar Plasma (mB) a starlifter carries from one mining leg: {@code min(miningPower * factor, cap)} (at
     * least 1). The same magnitude window as the legacy EoH star outputs (1_152–18_432 mB).
     *
     * @param miningPower the ship's total mining power.
     * @return the plasma amount in mB (always &gt;= 1).
     */
    public static long starlifterPlasmaAmount(long miningPower) {
        long amount = Math.max(1L, miningPower) * STARLIFTER_PLASMA_FACTOR;
        return Math.min(amount, STARLIFTER_PLASMA_CAP);
    }

    /**
     * Dwarf-matter dust amount a starlifter carries (white-dwarf or supermassive stars only — the miner's
     * per-ore dust amount, same scale; a main-sequence star yields plasma only).
     *
     * @param miningPower the ship's total mining power.
     * @return the dust amount (always &gt;= 1).
     */
    public static long starlifterMatterAmount(long miningPower) {
        return minerOreAmount(miningPower);
    }

    // endregion

    /**
     * Duration of one mission leg in machine ticks (persisted per ship for infodata / client animation).
     *
     * @param state       the leg's state ({@link USSShipState#OUTBOUND}/{@link USSShipState#MINING}/
     *                    {@link USSShipState#RETURNING})
     * @param speed       ship speed in [0, 1]
     * @param miningPower ship mining power
     * @return the leg duration in ticks; 0 for unknown states (e.g. {@code DOCKED})
     */
    public static long legTicks(USSShipState state, double speed, long miningPower) {
        if (state == null) {
            return 0L;
        }
        switch (state) {
            case OUTBOUND:
            case RETURNING:
                return travelTicks(speed);
            case MINING:
                return mineTicks(miningPower);
            default:
                return 0L;
        }
    }

    // endregion
}
