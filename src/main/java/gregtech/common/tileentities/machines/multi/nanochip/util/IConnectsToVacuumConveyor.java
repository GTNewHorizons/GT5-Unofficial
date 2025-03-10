package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraftforge.common.util.ForgeDirection;

public interface IConnectsToVacuumConveyor {

    boolean canConnect(ForgeDirection side);

    IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source);

    boolean isComponentsInputFacing(ForgeDirection side);

    byte getColorization();
}
