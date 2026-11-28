package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.logic.ProcessingLogic;

public interface IProcessingLogicSetupComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IProcessingLogicSetupComponent> KEY = ComponentKey.list();

    void setupProcessingLogic(ProcessingLogic logic);
}
