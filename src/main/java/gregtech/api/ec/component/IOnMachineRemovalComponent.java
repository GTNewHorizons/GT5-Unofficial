package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnMachineRemovalComponent extends IComponent {

    ComponentKey.ListKey<IOnMachineRemovalComponent> KEY = ComponentKey.list();

    void onMachineRemoval();
}
