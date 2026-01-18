package gregtech.common.tileentities.machines.multi.nanochip.factory;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.factory.IFactoryElement;

public interface VacuumFactoryElement
    extends IFactoryElement<VacuumFactoryElement, VacuumFactoryNetwork, VacuumFactoryGrid> {

    public boolean canConnectOnSide(ForgeDirection side);

    public byte getColorization();

}
