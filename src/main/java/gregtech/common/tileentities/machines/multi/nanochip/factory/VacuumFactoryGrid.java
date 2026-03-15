package gregtech.common.tileentities.machines.multi.nanochip.factory;

import gregtech.api.factory.standard.StandardFactoryGrid;

public class VacuumFactoryGrid
    extends StandardFactoryGrid<VacuumFactoryGrid, VacuumFactoryElement, VacuumFactoryNetwork> {

    public static final VacuumFactoryGrid INSTANCE = new VacuumFactoryGrid();

    @Override
    protected VacuumFactoryNetwork createNetwork() {
        return new VacuumFactoryNetwork();
    }
}
