package gtnhlanth.common.beamline;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public interface IConnectsToBeamline extends IMetaTileEntity {

    boolean canConnect(ForgeDirection side);

    IConnectsToBeamline getNext(IConnectsToBeamline source);

    boolean isDataInputFacing(ForgeDirection side);
}
