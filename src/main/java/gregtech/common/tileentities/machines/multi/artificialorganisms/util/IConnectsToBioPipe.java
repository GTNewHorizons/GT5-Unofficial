package gregtech.common.tileentities.machines.multi.artificialorganisms.util;

import java.util.HashSet;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.Hatch_BioOutput;

public interface IConnectsToBioPipe {

    boolean canConnect(ForgeDirection side);

    /**
     * Used by Bio Output hatches to find the connected inputs. The output hatch itself is passed in for linking.
     * Connections is used to determine which pipes in a network have already been visited.
     * Returns a HashSet containing connected input hatches.
     */
    HashSet<IConnectsToBioPipe> getConnected(Hatch_BioOutput output,
                                             HashSet<IConnectsToBioPipe> connections);

    boolean isComponentsInputFacing(ForgeDirection side);
}
