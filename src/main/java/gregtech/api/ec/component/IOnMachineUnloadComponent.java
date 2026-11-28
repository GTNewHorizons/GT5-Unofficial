package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnMachineUnloadComponent extends IComponent {

    ComponentKey.ListKey<IOnMachineUnloadComponent> KEY = ComponentKey.list();

    void onMachineUnload();
}
