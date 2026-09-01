package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The STABILIZE command (the Hyperdimensional Stabilization Matrix's activation command) — a Voidbase runs a
 * fixed-duration stabilization window: the node's {@code ticks} param IS the window length. While in flight the
 * base pays the per-tick matrix draw (a shortfall stalls the window) and consumes one Field Generator (the
 * GregTech tiered component) every interval (UXV over UMV); the window's expiry weight is the tier of the last
 * Field Generator consumed.
 *
 * <p>
 * Long-running: begin probes ({@code stabilizeStart} — FAILED when the eligibility gates refuse — no matrix in
 * the blueprint, no revealed ripple anchor, no fully built stabilizer on the anchor ripple, or no Field
 * Generator on board — the executor SKIPs), tick polls ({@code stabilizeTick} — DONE when the window ran out).
 *
 * <p>
 * Params: {@code ticks} (long, the window length; 0 / absent = no-op; clamped to a sane ceiling).
 */
public final class USSCommandStabilize implements USSCommandHandler {

    /** Param key: the window length in ticks. */
    public static final String PARAM_TICKS = "ticks";
    /** Window ceiling (a sanity bound against NBT garbage; ~68 years at 20 tps — no window should need more). */
    public static final long MAX_STABILIZE_TICKS = 2_147_483_647L;

    @Override
    public int commandId() {
        return USSCommand.STABILIZE;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        long ticks = p.hasKey(PARAM_TICKS) ? p.getLong(PARAM_TICKS) : 0L;
        if (ticks <= 0L) {
            return USSCommandStatus.DONE;
        }
        if (ticks > MAX_STABILIZE_TICKS) {
            ticks = MAX_STABILIZE_TICKS;
        }
        if (ctx.stabilizeStart(ticks)) {
            return USSCommandStatus.RUNNING;
        }
        ctx.log("STABILIZE: no stabilization window available - skipping");
        return USSCommandStatus.FAILED;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.stabilizeTick() ? USSCommandStatus.RUNNING : USSCommandStatus.DONE;
    }
}
