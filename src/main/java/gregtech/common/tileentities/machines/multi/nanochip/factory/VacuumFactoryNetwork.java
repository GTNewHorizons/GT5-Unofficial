package gregtech.common.tileentities.machines.multi.nanochip.factory;

import java.util.Collection;

import gregtech.api.factory.standard.StandardFactoryNetwork;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public class VacuumFactoryNetwork
    extends StandardFactoryNetwork<VacuumFactoryNetwork, VacuumFactoryElement, VacuumFactoryGrid> {

    public CircuitComponentPacket getCircuitComponentPacket() {
        IVacuumStorage[] outputs = getComponents(IVacuumStorage.class).toArray(new IVacuumStorage[0]);
        return (outputs.length == 1) ? outputs[0].getcomponentPacket() : null;

    }

    public boolean isUpdated;

    @Override
    public Collection<VacuumFactoryElement> getElements() {
        return super.getElements();
    }
}
