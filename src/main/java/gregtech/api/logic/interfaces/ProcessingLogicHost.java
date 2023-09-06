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
     * Called before the processing logic is used for a recipe check
     */
    default void updateProcessingLogic(P processingLogic) {}

    /**
     * called before the recipe check, but after any other updates
     */
    default void setProcessingLogicPower(P processingLogic) {}

    boolean needsUpdate();

    void setProcessingUpdate(boolean update);
}
