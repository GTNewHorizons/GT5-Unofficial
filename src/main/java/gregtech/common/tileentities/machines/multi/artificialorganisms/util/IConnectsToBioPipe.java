package gregtech.common.tileentities.machines.multi.artificialorganisms.util;

import java.util.ArrayList;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.GT_MetaTileEntity_Hatch_BioOutput;

public interface IConnectsToBioPipe {

    boolean canConnect(ForgeDirection side);

    ArrayList<IConnectsToBioPipe> getConnected(GT_MetaTileEntity_Hatch_BioOutput output,
        ArrayList<IConnectsToBioPipe> connections);

    boolean isComponentsInputFacing(ForgeDirection side);
}
