package gregtech.api.factory.test;

import gregtech.api.factory.standard.StandardFactoryGrid;

public class TestFactoryGrid extends StandardFactoryGrid<TestFactoryGrid, TestFactoryElement, TestFactoryNetwork> {

    public static final TestFactoryGrid INSTANCE = new TestFactoryGrid();

    @Override
    protected TestFactoryNetwork createNetwork() {
        return new TestFactoryNetwork();
    }
}
