package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;

public interface IPowerSetupComponent extends IProcessingLogicSetupComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IPowerSetupComponent> KEY = ComponentKey.unique();
}
