package gregtech.api.logic.interfaces;

import gregtech.api.logic.PowerLogic;

public interface PowerLogicHost {

    PowerLogic getPowerLogic(byte side);

    default boolean isEnergyReceiver() {
        return false;
    }

    default boolean isEnergyEmitter() {
        return false;
    }
}
