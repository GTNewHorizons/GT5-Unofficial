package tectech.mechanics.boseEinsteinCondensate;

import gregtech.api.factory.standard.StandardFactoryGrid;

public class BECFactoryGrid extends StandardFactoryGrid<BECFactoryGrid, BECFactoryElement, BECFactoryNetwork> {

    public static final BECFactoryGrid INSTANCE = new BECFactoryGrid();

    @Override
    protected BECFactoryNetwork createNetwork() {
        return new BECFactoryNetwork();
    }
}
