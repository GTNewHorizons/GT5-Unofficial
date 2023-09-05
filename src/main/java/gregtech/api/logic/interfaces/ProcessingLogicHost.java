package gregtech.api.logic.interfaces;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;

public interface ProcessingLogicHost<T extends ProcessingLogic<T>>
    extends IVoidable, ItemInventoryLogicHost, FluidInventoryLogicHost {

    T getProcessingLogic();

    boolean isInputSeparated();

    VoidingMode getVoidMode();
}
