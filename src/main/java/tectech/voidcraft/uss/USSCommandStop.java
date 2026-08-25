package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The STOP command (programming framework, Phase B) — terminate the program right now (any remaining nodes are
 * dropped). The ship HOLDS in place — no implicit return, no delivery (user decision #2: "programming the ship
 * is part of the challenge").
 *
 * <p>
 * Params: none. Immediate (STOP at begin).
 */
public final class USSCommandStop implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.STOP;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return USSCommandStatus.STOP;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return USSCommandStatus.DONE; // begin already terminated the program — tick is unreachable
    }
}
