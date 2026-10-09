package gregtech.api.factory.test;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.factory.IFactoryElement;

public interface TestFactoryElement extends IFactoryElement<TestFactoryElement, TestFactoryNetwork, TestFactoryGrid> {

    boolean canConnectOnSide(ForgeDirection side);
}
