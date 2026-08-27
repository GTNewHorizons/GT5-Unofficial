package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The REPAIR command (the repair work command) - restore the station integrity over time, drawing the station
 * energy buffer: one integrity per second of repair at the repair draw. A Voidbase runs this in its own program
 * (its program is the digitized controller program); on a Voidcraft the command SKIPs (v1: no repairable
 * station at a ship hover).
 *
 * <p>
 * Long-running: begin probes ({@code repairStart} - FAILED when nothing is repairable), tick accrues repair
 * ({@code repairTick} - DONE when the integrity is full).
 *
 * Params: none.
 */
public final class USSCommandRepair implements USSCommandHandler {

    @Override
    public int commandId() {
        return USSCommand.REPAIR;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        if (ctx.repairStart()) {
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
