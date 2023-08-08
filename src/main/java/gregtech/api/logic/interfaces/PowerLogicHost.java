package gregtech.api.logic.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.logic.PowerLogic;

public interface PowerLogicHost {

    PowerLogic getPowerLogic(ForgeDirection side);

    default PowerLogic getPowerLogic() {
        return getPowerLogic(ForgeDirection.UNKNOWN);
    }

    default boolean isEnergyReceiver() {
        return false;
    }

    default boolean isEnergyEmitter() {
        return false;
    }
}
