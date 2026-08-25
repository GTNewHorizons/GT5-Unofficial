package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The WORK command (programming framework, Phase B) — work at the current target: mine (Miner), starlift
 * (Starlifter), or scan the ripple (Explorer). The work leg runs in real time on the game side (the existing
 * work-ticks + cargo-build/reveal completion attach here in Phase C); the executor only polls.
 *
 * <p>
 * Params: none (the ship's current target defines the work — set by the preceding MOVE).
 */
public final class USSCommandWork implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.WORK;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (!ctx.startLeg(ctx.position(), 0.0, true)) {
            ctx.log("WORK: work-leg start refused — skipping");
            return USSCommandStatus.FAILED;
        }
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.legComplete() ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
