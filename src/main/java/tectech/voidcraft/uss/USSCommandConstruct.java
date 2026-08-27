package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The CONSTRUCT command (Voidbase construction framework) - build a Voidbase at the current hover point. A
 * constructor carrying a Voidbase blueprint + parts loadout reaches its target and builds or fills the
 * construction site there (the first constructor creates it, later ones fill it; a completed site spawns the
 * base). TIMED: begin arms the site's part-transfer leg (one part per second per 100 construction power) and the
 * command runs RUNNING - the executor polls {@link #tick} every machine tick until the leg completes, the site
 * fills, or the leg counts down (leftover parts then stay on board). Unresolvable hover / no blueprint / an
 * occupied anchor -&gt; FAILED -&gt; the executor SKIPS the instruction.
 *
 * <p>
 * Params: none - the ship hover anchor (the preceding MOVE) defines where the base is built.
 */
public final class USSCommandConstruct implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.CONSTRUCT;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (ctx.constructStart()) {
            return USSCommandStatus.RUNNING;
        }
        ctx.log("CONSTRUCT: nothing to construct at this hover point - skipping");
        return USSCommandStatus.FAILED;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return ctx.constructTick() ? USSCommandStatus.RUNNING : USSCommandStatus.DONE;
    }
}
