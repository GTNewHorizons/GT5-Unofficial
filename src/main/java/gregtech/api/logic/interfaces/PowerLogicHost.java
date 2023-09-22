package gregtech.api.logic.interfaces;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.logic.PowerLogic;

public interface PowerLogicHost {

    @Nullable
    PowerLogic getPowerLogic(@Nonnull ForgeDirection side);

    @Nullable
    default PowerLogic getPowerLogic() {
        return getPowerLogic(ForgeDirection.UNKNOWN);
    }

    default boolean isEnergyReceiver() {
        return false;
    }

    default boolean isEnergyEmitter() {
        return false;
    }

    default void emitEnergyFromLogic() {
        IEnergyConnected.Util.emitEnergyToNetwork(this, getPowerOutputSide());
    }

    @Nonnull
    ForgeDirection getPowerOutputSide();
}
