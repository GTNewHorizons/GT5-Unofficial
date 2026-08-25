package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The READ command (programming framework, Phase B) — copy one USS variable slot into another within the
 * variable space (the space is the ship's in/out channel, so an external machine reading slot B sees the value
 * the ship moved there).
 *
 * <p>
 * Params: {@code from} (0..255), {@code to} (0..255). Immediate (DONE at begin).
 */
public final class USSCommandRead implements USSCommandHandler {

    /** Param key: the source slot (0..255). */
    public static final String PARAM_FROM = "from";
    /** Param key: the destination slot (0..255). */
    public static final String PARAM_TO = "to";

    @Override
    public int commandId() {
        return USSCommand.READ;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        int from = p.hasKey(PARAM_FROM) ? p.getInteger(PARAM_FROM) : 0;
        int to = p.hasKey(PARAM_TO) ? p.getInteger(PARAM_TO) : 0;
        ctx.writeVar(to, ctx.readVar(from));
        return USSCommandStatus.DONE;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return USSCommandStatus.DONE; // immediate command — never in flight
    }
}
