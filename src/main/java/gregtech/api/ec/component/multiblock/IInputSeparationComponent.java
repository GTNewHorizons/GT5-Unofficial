package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;

public interface IInputSeparationComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IInputSeparationComponent> KEY = ComponentKey.unique();

    boolean isInputSeparationEnabled();

    IInputSeparationComponent setInputSeparation(boolean enabled);
}
