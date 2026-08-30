package tectech.voidcraft.uss;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Materials;

/**
 * One in-flight SEND / TAKE transfer (ship-to-ship cargo transfer): the per-instruction bookkeeping + the paced
 * unit transfer.
 *
 * <p>
 * The game side holds one of these per executing ship (a ship runs at most one transfer at a time). Each machine
 * tick, {@link #tick} advances the pacing countdown; every {@code ticksPerUnit} ticks (from
 * {@code USSConstants.transferTicksPerUnit}: 1 logistics power = 1 cargo unit per second) ONE cargo unit — 1
 * item, or {@code CargoHold.MB_PER_UNIT} mB of fluid — moves from the SOURCE hold to the TARGET hold, honoring
 * the material filter, the unit limit (default -1 = ALL) and the target's capacity (a full target stops the
 * transfer; the source keeps the remainder). The holds are IMMUTABLE — the tick returns the updated holds
 * (null = unchanged) and the caller applies them to the ships (plus their derived cargo NBT).
 *
 * <p>
 * The source / target direction is owned by the CALLER (SEND: source = the executing ship, target = the target
 * ship; TAKE: the inverse) — this class only moves units between the two holds it is handed.
 *
 * <p>
 * Bare JVM ({@code CargoHold} + {@code Materials} + NBT data only), so the pacing / filter / clamp rules and the
 * serialized format are unit-testable without Forge.
 */
public final class USSCargoTransfer {

    /** The match-all filter (the SEND / TAKE default). */
    public static final String FILTER_ALL = "*";

    /** The stop reason when the target's hold has no capacity left. */
    public static final String REASON_TARGET_FULL = "target full";
    /** The stop reason when the unit limit has been reached. */
    public static final String REASON_LIMIT_REACHED = "amount limit reached";
    /** The stop reason when the source carries no matching cargo anymore. */
    public static final String REASON_NO_CARGO = "no matching cargo left";
    /** The stop reason when a ship's hold is missing (defensive). */
    public static final String REASON_NO_HOLD = "no cargo hold";

    /** The normalized filter ("" = match all). */
    private final String filter;
    /** The unit limit (-1 = ALL). */
    private final long limit;
    /** Units moved so far. */
    private long transferred;
    /** The pacing countdown (hits 0 on a transfer tick). */
    private int countdown;
    /** The pacing (machine ticks per cargo unit). */
    private final int ticksPerUnit;

    private USSCargoTransfer(String filter, long limit, int ticksPerUnit) {
        this.filter = filter;
        this.limit = Math.max(-1L, limit);
        this.ticksPerUnit = Math.max(1, ticksPerUnit);
        this.countdown = this.ticksPerUnit;
    }

    /**
     * Arm a fresh transfer.
     *
     * @param filter       the material name filter (null / empty / "*" = match all; case-insensitive material name
     *                     otherwise)
     * @param limit        the unit limit (-1 = ALL; &lt; -1 clamps to -1)
     * @param ticksPerUnit the pacing (machine ticks per cargo unit, &ge; 1)
     * @return the armed transfer (its first unit lands after {@code ticksPerUnit} ticks)
     */
    public static USSCargoTransfer arm(String filter, long limit, int ticksPerUnit) {
        return new USSCargoTransfer(normalizeFilter(filter), limit, ticksPerUnit);
    }

    /**
     * Advance the transfer by one machine tick.
     *
     * @param source the source ship's current hold (SEND: the executing ship; TAKE: the target ship)
     * @param target the target ship's current hold
     * @return the tick's result (never null)
     */
    public Result tick(CargoHold source, CargoHold target) {
        if (source == null || target == null) {
            return new Result(false, REASON_NO_HOLD, null, null);
        }
        countdown--;
        if (countdown > 0) {
            return new Result(true, null, null, null);
        }
        if (remaining() == 0) {
            return new Result(false, REASON_LIMIT_REACHED, null, null);
        }
        if (target.remainingUnits() <= 0L) {
            return new Result(false, REASON_TARGET_FULL, null, null);
        }
        // The next matching unit: items first (insertion order — deterministic), then fluids.
        for (Map.Entry<Materials, Long> e : source.getItems()
            .entrySet()) {
            if (e.getValue() != null && e.getValue() > 0L && matches(filter, e.getKey())) {
                return move(source, target, e.getKey(), true);
            }
        }
        for (Map.Entry<Materials, Long> e : source.getFluids()
            .entrySet()) {
            if (e.getValue() != null && e.getValue() >= CargoHold.MB_PER_UNIT && matches(filter, e.getKey())) {
                return move(source, target, e.getKey(), false);
            }
        }
        return new Result(false, REASON_NO_CARGO, null, null);
    }

    private Result move(CargoHold source, CargoHold target, Materials material, boolean item) {
        CargoHold nextSource;
        CargoHold nextTarget;
        if (item) {
            nextSource = source.removeItems(material, 1L);
            nextTarget = target.addItems(material, 1L);
        } else {
            nextSource = source.removeFluids(material, CargoHold.MB_PER_UNIT);
            nextTarget = target.addFluids(material, CargoHold.MB_PER_UNIT);
        }
        transferred++;
        if (remaining() == 0) {
            return new Result(false, REASON_LIMIT_REACHED, nextSource, nextTarget);
        }
        countdown = ticksPerUnit;
        return new Result(true, null, nextSource, nextTarget);
    }

    /** @return the units moved so far */
    public long transferred() {
        return transferred;
    }

    /** @return the units left to move (-1 = unlimited) */
    public long remaining() {
        return (limit < 0L) ? -1L : Math.max(0L, limit - transferred);
    }

    /** @return the normalized filter ("" = match all) */
    public String getFilter() {
        return filter;
    }

    /**
     * Normalize a filter: null / blank / "*" → match-all (the empty string).
     *
     * @param filter the raw filter param
     * @return the normalized filter (never null)
     */
    public static String normalizeFilter(String filter) {
        if (filter == null) {
            return "";
        }
        String f = filter.trim();
        if (f.isEmpty() || FILTER_ALL.equals(f)) {
            return "";
        }
        return f;
    }

    /**
     * Whether a material passes the (normalized) filter: match-all or a case-insensitive material-name match.
     *
     * @param normalizedFilter the filter from {@link #normalizeFilter} (null = match-all)
     * @param material         the cargo material
     * @return true when the material may transfer
     */
    public static boolean matches(String normalizedFilter, Materials material) {
        if (normalizedFilter == null || normalizedFilter.isEmpty()) {
            return true;
        }
        if (material == null || material == Materials._NULL) {
            return false;
        }
        return normalizedFilter.equalsIgnoreCase(material.getName());
    }

    /**
     * Serialize the transfer's state (filter, limit, progress, pacing phase) into {@code nbt}. The fleet MTE
     * persists one of these per in-flight transfer, so a ship saved mid-SEND / mid-TAKE resumes its transfer
     * exactly where it left off (the program cursor that resumes the node is persisted separately, per ship).
     */
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString(TAG_FILTER, filter);
        nbt.setLong(TAG_LIMIT, limit);
        nbt.setLong(TAG_TRANSFERRED, transferred);
        nbt.setInteger(TAG_COUNTDOWN, countdown);
        nbt.setInteger(TAG_TICKS_PER_UNIT, ticksPerUnit);
    }

    /**
     * Restore a transfer from {@link #writeToNBT}.
     *
     * @param nbt the serialized transfer
     * @return the restored transfer (progress + pacing phase included), or null when the record is corrupt (missing
     *         its core tags) — the caller drops the transfer, so a half-leg is never resurrected as a fresh one
     */
    public static USSCargoTransfer readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_FILTER) || !nbt.hasKey(TAG_LIMIT) || !nbt.hasKey(TAG_TICKS_PER_UNIT)) {
            return null;
        }
        USSCargoTransfer transfer = new USSCargoTransfer(
            normalizeFilter(nbt.getString(TAG_FILTER)),
            nbt.getLong(TAG_LIMIT),
            nbt.getInteger(TAG_TICKS_PER_UNIT));
        transfer.transferred = nbt.hasKey(TAG_TRANSFERRED) ? Math.max(0L, nbt.getLong(TAG_TRANSFERRED)) : 0L;
        int countdown = nbt.hasKey(TAG_COUNTDOWN) ? nbt.getInteger(TAG_COUNTDOWN) : transfer.ticksPerUnit;
        if (countdown < 0 || countdown > transfer.ticksPerUnit) {
            countdown = transfer.ticksPerUnit;
        }
        transfer.countdown = countdown;
        return transfer;
    }

    /**
     * One tick's result.
     */
    public static final class Result {

        /** Whether the transfer is still running (keep polling). */
        public final boolean running;
        /** Why it stopped (null while running). */
        public final String reason;
        /** The source hold after this tick (null = unchanged). */
        public final CargoHold source;
        /** The target hold after this tick (null = unchanged). */
        public final CargoHold target;

        Result(boolean running, String reason, CargoHold source, CargoHold target) {
            this.running = running;
            this.reason = reason;
            this.source = source;
            this.target = target;
        }
    }

    // NBT keys (the vc_ naming convention) — the serialized transfer format.
    private static final String TAG_FILTER = "vc_tr_filter";
    private static final String TAG_LIMIT = "vc_tr_limit";
    private static final String TAG_TRANSFERRED = "vc_tr_transferred";
    private static final String TAG_COUNTDOWN = "vc_tr_countdown";
    private static final String TAG_TICKS_PER_UNIT = "vc_tr_ticks_per_unit";
}
