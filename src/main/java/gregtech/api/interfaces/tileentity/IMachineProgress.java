package gregtech.api.interfaces.tileentity;

import javax.annotation.Nonnull;

import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

/**
 * For Machines which have Progress
 */
public interface IMachineProgress extends IHasWorldObjectAndCoords, MachineProgress {

    /**
     * returns the Progress this Machine has made. Warning, this can also be negative!
     */
    int getProgress();

    /**
     * returns the Progress the Machine needs to complete its task.
     */
    int getMaxProgress();

    /**
     * Manually increases the Progress of the Machine by vent cover.
     */
    boolean increaseProgress(int aProgressAmountInTicks);

    /**
     * returns if the Machine just got enableWorking called after being disabled. Used for Translocators, which need to
     * check if they need to transfer immediately.
     */
    boolean hasWorkJustBeenEnabled();

    /**
     * used to control Machines via Redstone Signal Strength by special Covers In case of 0 the Machine is very likely
     * doing nothing, or is just not being controlled at all.
     */
    default byte getWorkDataValue() {
        return 0;
    }

    /**
     * used to control Machines via Redstone Signal Strength by special Covers only Values between 0 and 15!
     */
    default void setWorkDataValue(byte aValue) {}

    /**
     * Indicates if the object in question was forced to shut down (i.e. loss of power)
     */
    default boolean wasShutdown() {
        return false;
    }

    @Nonnull
    default ShutDownReason getLastShutDownReason() {
        return ShutDownReasonRegistry.NONE;
    }
}
