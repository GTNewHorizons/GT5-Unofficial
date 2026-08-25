package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The MOVE command (programming framework, Phase B) — the ONE "Go to" (user spec: "All 'Go to' should be
 * implemented with a general move command with different inputs").
 *
 * <p>
 * Params: {@code target} (a {@link USSProgramDefaults} {@code TARGET_*} string) + optional {@code index}
 * (PLANET / RIPPLE / SHIP). Begin resolves the destination and starts a TRAVEL leg (RUNNING); the leg ticks in
 * real time on the game side; tick() polls completion (DONE) — arrival is where the next instruction starts.
 * Unresolvable target / refused leg → FAILED → the executor SKIPS the instruction (user decision #3).
 * "Leave the USS" = {@code target HOME}.
 */
public final class USSCommandMove implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.MOVE;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        String target = p.getString(USSProgramDefaults.PARAM_TARGET);
        if (target.isEmpty()) {
            ctx.log("MOVE: missing target — skipping");
            return USSCommandStatus.FAILED;
        }
        int index = p.hasKey(USSProgramDefaults.PARAM_INDEX) ? p.getInteger(USSProgramDefaults.PARAM_INDEX) : 0;
        USSPosition dest = ctx.resolveMoveTarget(target, index);
        if (dest == null) {
            ctx.log("MOVE: target '" + target + "' (index " + index + ") unresolvable — skipping");
            return USSCommandStatus.FAILED;
        }
        double dist = ctx.distanceTo(dest);
        if (!ctx.startLeg(dest, dist, false)) {
            ctx.log("MOVE: leg start refused — skipping");
            return USSCommandStatus.FAILED;
        }
        return USSCommandStatus.RUNNING;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.legComplete() ? USSCommandStatus.DONE : USSCommandStatus.RUNNING;
    }
}
