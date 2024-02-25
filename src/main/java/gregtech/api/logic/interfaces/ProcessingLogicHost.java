package gregtech.api.logic.interfaces;

import javax.annotation.Nonnull;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.interfaces.tileentity.MachineProgress;
import gregtech.api.logic.MuTEProcessingLogic;

public interface ProcessingLogicHost<P extends MuTEProcessingLogic<P>>
    extends IVoidable, ItemInventoryLogicHost, FluidInventoryLogicHost, MachineProgress {

    /**
     * Get the processing logic for the current machine
     */
    @Nonnull
    P getProcessingLogic();

    boolean isInputSeparated();

    void setInputSeparation(Boolean inputSeparation);

    default boolean supportsInputSeparation() {
        return true;
    }

    default boolean getDefaultInputSeparationMode() {
        return false;
    }

    boolean isRecipeLockingEnabled();

    void setRecipeLocking(Boolean recipeLocked);

    default boolean supportsSingleRecipeLocking() {
        return true;
    }

    default boolean getDefaultRecipeLockingMode() {
        return false;
    }

    default boolean supportsBatchMode() {
        return true;
    }

    void setBatchMode(Boolean batchMode);

    boolean isBatchModeEnabled();

    default boolean getDefaultBatchMode() {
        return false;
    }

    /**
     * Get what the machine can void or not
     */
    @Nonnull
    VoidingMode getVoidMode();

    /**
     * Called when the processing logic should be updated by {@link #needsUpdate()}
     */
    default void updateProcessingLogic(@Nonnull P processingLogic) {}

    /**
     * Called before the recipe check, but after any other updates
     */
    default void setProcessingLogicPower(@Nonnull P processingLogic) {}

    /**
     * DO NOT CALL YOURSELF!!!
     *
     * If you want to make the processing logic be updated call {@link #setProcessingUpdate(boolean)}
     */
    boolean needsUpdate();

    /**
     * To be called when one needs to updated the processing logic. That can be when parallel changes, ect.
     */
    void setProcessingUpdate(boolean update);
}
