package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The MINE command (programming framework, Phase B) — mine the current target planet until the work leg
 * completes (a work leg of kind {@link USSWorkKind#MINE}, mined at the ship's mining power). The work leg runs in
 * real time on the game side (the shared leg-duration tables); the executor only polls.
 *
 * <p>
 * Params: none (the ship's current target defines the work — set by the preceding MOVE). The game side refuses
 * the leg when the ship has no mining power (the command then SKIPs).
 */
public final class USSCommandMine implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.MINE;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (!ctx.startLeg(ctx.position(), 0.0, USSWorkKind.MINE)) {
            ctx.log("MINE: work-leg start refused — skipping");
            return USSCommandStatus.FAILED;
        }
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.legComplete() ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
