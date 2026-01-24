package gregtech.common.tileentities.machines.multi.nanochip.factory;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public interface IVacuumStorage {

    public CircuitComponentPacket extractPacket();

    public MTENanochipAssemblyComplex getMainController();

}
