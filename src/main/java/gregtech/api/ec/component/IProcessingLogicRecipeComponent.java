package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.logic.ProcessingLogic;

public interface IProcessingLogicRecipeComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IProcessingLogicRecipeComponent> KEY = ComponentKey.unique();

    @NotNull
    ProcessingLogic getProcessingLogic();
}
