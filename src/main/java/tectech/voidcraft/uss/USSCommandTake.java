package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The TAKE command (ship-to-ship cargo transfer) — transfer cargo from the target ship's hold into THIS
 * ship's hold. The exact inverse of {@link USSCommandSend}: the same params ({@code amount}, {@code filter},
 * {@code target}), the same shared-location rule, the same logistics-power rate, and the same "always
 * succeeds" contract (no refusal from the target).
 */
public final class USSCommandTake implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.TAKE;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        String target = p.getString(USSProgramDefaults.PARAM_TARGET);
        if (target.isEmpty()) {
            ctx.log("TAKE: missing target — skipping");
            return USSCommandStatus.FAILED;
        }
        long amount = USSCommandSend.readLongParam(p, USSProgramDefaults.PARAM_AMOUNT, -1L);
        String filter = p.getString(USSProgramDefaults.PARAM_FILTER);
        if (ctx.transferStart(USSCommand.TAKE, target, amount, filter)) {
            return USSCommandStatus.RUNNING;
        }
        ctx.log("TAKE: cannot take from '" + target + "' — skipping");
        return USSCommandStatus.FAILED;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.transferTick(USSCommand.TAKE) ? USSCommandStatus.RUNNING : USSCommandStatus.DONE;
    }
}
