package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The REPAIR command (the repair work command) - restore integrity over time, drawing the executor's energy
 * buffer: one integrity per second of repair at the repair draw. A Voidbase runs this at its anchor (its program
 * is the digitized controller program): the target is the base itself (the default) or a ship standing at the
 * base. A Voidcraft SKIPs the command (a ship has no repair bay).
 *
 * <p>
 * Long-running: begin probes ({@code repairStart} - FAILED when nothing can be repaired), tick accrues repair
 * ({@code repairTick} - DONE when the target's integrity is full, or the target left the shared location / was
 * lost).
 *
 * Params: {@code target} (optional - empty or {@code SELF} = the executing entity itself; otherwise a fleet
 * index or name, resolved with the same pattern and shared-location rule as SEND / TAKE).
 */
public final class USSCommandRepair implements USSCommandHandler {

    /** The target param that repairs the executing entity itself. */
    public static final String TARGET_SELF = "SELF";

    @Override
    public int commandId() {
        return USSCommand.REPAIR;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        String target = node.params()
            .getString(USSProgramDefaults.PARAM_TARGET);
        if (ctx.repairStart(target)) {
            return USSCommandStatus.RUNNING;
        }
        ctx.log("REPAIR: nothing to repair - skipping");
        return USSCommandStatus.FAILED;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.repairTick() ? USSCommandStatus.RUNNING : USSCommandStatus.DONE;
    }
}
