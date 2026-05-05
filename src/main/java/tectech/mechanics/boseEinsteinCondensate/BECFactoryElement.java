package tectech.mechanics.boseEinsteinCondensate;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.factory.IFactoryElement;

public interface BECFactoryElement extends IFactoryElement<BECFactoryElement, BECFactoryNetwork, BECFactoryGrid> {

    enum ConnectionType {
        CONNECTABLE,
        NONE,
    }

    ConnectionType getConnectionOnSide(ForgeDirection side);
}
