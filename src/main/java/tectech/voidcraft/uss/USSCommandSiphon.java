package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The SIPHON command (programming framework, Phase B) — siphon the star until the work leg completes (a work leg
 * of kind {@link USSWorkKind#SIPHON}, siphoned at the ship's starlifter power). The yield is the star's produced
 * fluids, each capped by the star's remaining fluid reserve (the reserve depletes over the star's life). The work
 * leg runs in real time on the game side; the executor only polls.
 *
 * <p>
 * Params: none (the ship's current target defines the work — set by the preceding MOVE). The game side refuses
 * the leg when the ship has no siphon power (the command then SKIPs).
 */
public final class USSCommandSiphon implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.SIPHON;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (!ctx.startLeg(ctx.position(), 0.0, USSWorkKind.SIPHON)) {
            ctx.log("SIPHON: work-leg start refused — skipping");
            return USSCommandStatus.FAILED;
        }
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.legComplete() ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
