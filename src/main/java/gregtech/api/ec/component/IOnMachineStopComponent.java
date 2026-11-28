package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.util.shutdown.ShutDownReason;

public interface IOnMachineStopComponent extends IComponent {

    ComponentKey.ListKey<IOnMachineStopComponent> KEY = ComponentKey.list();

    void onMachineStop(@NotNull ShutDownReason reason);
}
