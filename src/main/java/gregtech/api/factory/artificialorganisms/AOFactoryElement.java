package gregtech.api.factory.artificialorganisms;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.factory.IFactoryElement;

public interface AOFactoryElement extends IFactoryElement<AOFactoryElement, AOFactoryNetwork, AOFactoryGrid> {

    public boolean canConnectOnSide(ForgeDirection side);

    public void havocEvent();
}
