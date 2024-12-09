package gregtech.api.factory.artificialorganisms;

import gregtech.api.factory.standard.StandardFactoryGrid;

public class AOFactoryGrid extends StandardFactoryGrid<AOFactoryGrid, AOFactoryElement, AOFactoryNetwork> {

    public static final AOFactoryGrid INSTANCE = new AOFactoryGrid();

    @Override
    protected AOFactoryNetwork createNetwork() {
        return new AOFactoryNetwork();
    }
}
