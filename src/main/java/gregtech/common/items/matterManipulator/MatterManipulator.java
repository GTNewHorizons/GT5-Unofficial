package gregtech.common.items.matterManipulator;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.MTEMMUplink;

/*
 * A dummy class that exposes some matter manipulator internals to gt5u.
 * I decided to make all of the util classes for the matter manipulator package private to prevent namespace cluttering.
 * I also didn't want to add a prefix to all of them.
 */
public class MatterManipulator {

    private MatterManipulator() {}

    public static void sendUplinkStateUpdate(MTEMMUplink uplink) {
        IGregTechTileEntity igte = uplink.getBaseMetaTileEntity();

        NBTState.Location l = new NBTState.Location(
            igte.getWorld(),
            igte.getXCoord(),
            igte.getYCoord(),
            igte.getZCoord());

        Messages.UpdateUplinkState.sendToPlayersAround(l, uplink);
    }
}
