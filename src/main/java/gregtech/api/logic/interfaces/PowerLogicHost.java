package gregtech.api.logic.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.logic.PowerLogic;

public interface PowerLogicHost {

    PowerLogic getPowerLogic(ForgeDirection side);

    default boolean isEnergyReceiver() {
        return false;
    }

    default boolean isEnergyEmitter() {
        return false;
    }
}
