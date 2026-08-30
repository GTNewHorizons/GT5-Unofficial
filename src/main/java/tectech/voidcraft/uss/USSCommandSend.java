package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The SEND command (ship-to-ship cargo transfer) — transfer cargo from THIS ship's hold to the target ship's
 * hold. The transfer ALWAYS succeeds (there is no refusal from the target); what can fail at begin is the
 * ship-side preconditions only: a missing target param, an unresolvable target ship, a target that is this
 * ship, no logistics power, or the two ships not sharing a LOCATION (a planet orbit, the star, a ripple site,
 * or one of the two ships' own position — see {@link USSLocation#shared}).
 *
 * <p>
 * Params: {@code amount} (cargo units, default -1 = ALL), {@code filter} (material name, default "*" = match
 * all), {@code target} (the target ship — a fleet index, a ship name, or {@link
 * USSProgramDefaults#TARGET_NEARBY} = the first viable fleet ship at a shared location (carrying cargo for
 * TAKE, free hold space for SEND)). In flight the
 * command runs RUNNING and the executor polls {@link #tick} every machine tick; the game side moves one cargo
 * unit per
 * {@code USSConstants.transferTicksPerUnit} (1 logistics power = 1 cargo unit per second) until the limit is
 * reached, the target fills, the source runs out of matching cargo, or the ships stop sharing a location.
 */
public final class USSCommandSend implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.SEND;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        String target = p.getString(USSProgramDefaults.PARAM_TARGET);
        if (target.isEmpty()) {
            ctx.log("SEND: missing target — skipping");
            return USSCommandStatus.FAILED;
        }
        long amount = readLongParam(p, USSProgramDefaults.PARAM_AMOUNT, -1L);
        String filter = p.getString(USSProgramDefaults.PARAM_FILTER);
        if (ctx.transferStart(USSCommand.SEND, target, amount, filter)) {
            return USSCommandStatus.RUNNING;
        }
        ctx.log("SEND: cannot transfer to '" + target + "' — skipping");
        return USSCommandStatus.FAILED;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.transferTick(USSCommand.SEND) ? USSCommandStatus.RUNNING : USSCommandStatus.DONE;
    }

    /**
     * Read a numeric command param tolerantly (the editor stores it as an NBT number; a hand-edited program may
     * carry a string): int / long / parseable string, the default otherwise.
     */
    static long readLongParam(NBTTagCompound p, String key, long def) {
        if (p == null || !p.hasKey(key)) {
            return def;
        }
        if (p.hasKey(key, 3)) { // NBTTagInt
            return p.getInteger(key);
        }
        if (p.hasKey(key, 4)) { // NBTTagLong
            return p.getLong(key);
        }
        if (p.hasKey(key, 8)) { // NBTTagString
            try {
                return Long.parseLong(
                    p.getString(key)
                        .trim());
            } catch (RuntimeException ex) {
                return def;
            }
        }
        return def;
    }
}
