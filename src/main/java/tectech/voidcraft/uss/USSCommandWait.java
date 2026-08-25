package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The WAIT command (programming framework, Phase B) — hold for a number of ticks (the ship keeps doing what it
 * is doing; legs are unaffected).
 *
 * <p>
 * Params: {@code ticks} (long, default 0 = no-op; clamped to a sane ceiling). In flight: begin returns RUNNING
 * with the remaining ticks in the state compound; tick() counts down one per tick (real time — WAIT is not
 * quantized to the node-step pace).
 */
public final class USSCommandWait implements USSCommandHandler {

    /** Param key: the wait duration in ticks. */
    public static final String PARAM_TICKS = "ticks";
    /** Wait ceiling (a sanity bound against NBT garbage; ~68 years at 20 tps — no program should need more). */
    public static final long MAX_WAIT_TICKS = 2_147_483_647L;
    /** State key: the remaining ticks. */
    public static final String STATE_REMAINING = "t";

    @Override
    public int commandId() {
        return USSCommand.WAIT;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        long ticks = p.hasKey(PARAM_TICKS) ? p.getLong(PARAM_TICKS) : 0L;
        if (ticks <= 0L) {
            return USSCommandStatus.DONE;
        }
        if (ticks > MAX_WAIT_TICKS) {
            ticks = MAX_WAIT_TICKS;
        }
        state.setLong(STATE_REMAINING, ticks);
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        long remaining = state.getLong(STATE_REMAINING) - 1L;
        state.setLong(STATE_REMAINING, remaining);
        return remaining <= 0L ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
