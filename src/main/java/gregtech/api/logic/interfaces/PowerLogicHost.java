package gregtech.api.logic.interfaces;

import gregtech.api.logic.PowerLogic;
import net.minecraftforge.common.util.ForgeDirection;

public interface PowerLogicHost {

    PowerLogic getPowerLogic(ForgeDirection facing);

    default boolean isEnergyReceiver() {
        return false;
    }

    default boolean isEnergyEmitter() {
        return false;
    }
}
