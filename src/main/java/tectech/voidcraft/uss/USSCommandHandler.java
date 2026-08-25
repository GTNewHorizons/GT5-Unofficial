package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A Voidcraft command handler (programming framework, Phase B — the modularity seam).
 *
 * <p>
 * A handler is a STATELESS singleton: per-ship, per-instruction state lives in the {@code state} compound the
 * executor hands in (it is persisted with the executor's cursor, so a handler survives save/reload).
 *
 * <ul>
 * <li>{@link #begin} — the node's first step. Return {@link USSCommandStatus#RUNNING} for long-running commands
 * (MOVE / WORK / WAIT) — the executor then polls {@link #tick} every tick until it reports otherwise — or
 * DONE / FAILED / STOP for immediate ones (WRITE / READ / STOP).</li>
 * <li>{@link #tick} — called every tick while the command is in flight. MOVE/WORK poll {@link
 * USSExecutionContext#legComplete()} (the legs tick in real time on the game side); WAIT counts down.</li>
 * </ul>
 *
 * <p>
 * Adding a command = one new handler class + {@link USSCommandRegistry#register} + an id in
 * {@link USSCommand}. The executor and the program format never change.
 */
public interface USSCommandHandler {

    /** @return the command id this handler serves (see {@link USSCommand}) */
    int commandId();

    /**
     * @param ctx   the execution context (the game seam)
     * @param node  the COMMAND node being executed (read its params here)
     * @param state this instruction's private state (persisted with the executor; initially empty, reusable
     *              across save/reload)
     * @return RUNNING (in flight — the executor polls tick) / DONE / FAILED (skip) / STOP (terminate program)
     */
    USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state);

    /**
     * @param ctx   the execution context
     * @param node  the COMMAND node still being executed
     * @param state this instruction's private state (as persisted)
     * @return RUNNING (keep polling) / DONE / FAILED / STOP
     */
    USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state);
}
