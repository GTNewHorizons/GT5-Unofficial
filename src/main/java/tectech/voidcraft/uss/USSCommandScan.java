package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The SCAN command (programming framework, Phase B) — scan the current ripple point until the work leg completes
 * (a work leg of kind {@link USSWorkKind#SCAN}, scanned at the ship's scan power). The yield is the ripple
 * REVEAL (the point becomes visible to the fleet), not cargo. The work leg runs in real time on the game side;
 * the executor only polls.
 *
 * <p>
 * Params: none (the ship's current target defines the work — set by the preceding MOVE). The game side refuses
 * the leg when the ship has no scan power (the command then SKIPs).
 */
public final class USSCommandScan implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.SCAN;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (!ctx.startLeg(ctx.position(), 0.0, USSWorkKind.SCAN)) {
            ctx.log("SCAN: work-leg start refused — skipping");
            return USSCommandStatus.FAILED;
        }
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.legComplete() ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
