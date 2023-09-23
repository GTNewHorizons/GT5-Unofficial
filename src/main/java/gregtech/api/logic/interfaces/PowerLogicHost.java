package gregtech.api.logic.interfaces;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.logic.PowerLogic;

/**
 * Power logic class for one to use to enable a machine to use energy
 */
public interface PowerLogicHost {

    /**
     * 
     * @param side Side being access to try and get the power logic from
     * @return Can return null if the side doesn't allow the return of the logic.
     */
    @Nullable
    PowerLogic getPowerLogic(@Nonnull ForgeDirection side);

    /**
     * Gives the power logic ignoring the side.
     */
    @Nonnull
    default PowerLogic getPowerLogic() {
        return Objects.requireNonNull(getPowerLogic(ForgeDirection.UNKNOWN));
    }

    /**
     * Shortcut to the method of {@link PowerLogic#isEnergyReceiver()}
     */
    default boolean isEnergyReceiver() {
        return getPowerLogic().isEnergyReceiver();
    }

    /**
     * Shortcut to the method of {@link PowerLogic#isEnergyEmitter()}
     */
    default boolean isEnergyEmitter() {
        return getPowerLogic().isEnergyEmitter();
    }

    /**
     * Method for emitting energy to other blocks and machines. Override when it needs to be changed.
     */
    default void emitEnergyFromLogic() {
        IEnergyConnected.Util.emitEnergyToNetwork(this, getPowerOutputSide());
    }

    /**
     * From where does the machine output energy from?
     * When the output side is {@link ForgeDirection#UNKNOWN} then it won't output energy
     */
    @Nonnull
    ForgeDirection getPowerOutputSide();
}
