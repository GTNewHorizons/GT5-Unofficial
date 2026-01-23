package gregtech.api.factory.itempipes;

import gregtech.api.factory.standard.StandardFactoryGrid;

public class ItemPipeFactoryGrid
    extends StandardFactoryGrid<ItemPipeFactoryGrid, ItemPipeFactoryElement, ItemPipeFactoryNetwork> {

    public static final ItemPipeFactoryGrid INSTANCE = new ItemPipeFactoryGrid();

    @Override
    protected ItemPipeFactoryNetwork createNetwork() {
        return new ItemPipeFactoryNetwork();
    }
}
