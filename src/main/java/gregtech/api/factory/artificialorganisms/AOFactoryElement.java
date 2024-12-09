package gregtech.api.factory.artificialorganisms;

import gregtech.api.factory.IFactoryElement;
import net.minecraftforge.common.util.ForgeDirection;

public interface AOFactoryElement extends IFactoryElement<AOFactoryElement, AOFactoryNetwork, AOFactoryGrid> {

    public boolean canConnectOnSide(ForgeDirection side);
}
