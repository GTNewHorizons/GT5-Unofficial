package tectech.mechanics.boseEinsteinCondensate;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.factory.INotableFactoryElement;

public interface NotableBECFactoryElement extends INotableFactoryElement<NotableBECFactoryElement, BECRouteInfo> {

    default boolean allowsCondensateThrough(Fluid condensate) {
        return true;
    }

}
