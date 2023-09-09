package gregtech.api.logic.interfaces;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;

public interface ProcessingLogicHost<P extends ProcessingLogic<P>>
    extends IVoidable, ItemInventoryLogicHost, FluidInventoryLogicHost, IMachineProgress {

    P getProcessingLogic();

    boolean isInputSeparated();

    VoidingMode getVoidMode();

    /**
     * Called when the processing logic should be updated by {@link #needsUpdate()}
     */
    default void updateProcessingLogic(P processingLogic) {}

    /**
     * called before the recipe check, but after any other updates
     */
    default void setProcessingLogicPower(P processingLogic) {}

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
