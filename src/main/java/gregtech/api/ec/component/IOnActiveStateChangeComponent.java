package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnActiveStateChangeComponent extends IComponent {

    ComponentKey.ListKey<IOnActiveStateChangeComponent> KEY = ComponentKey.list();

    default void onMachineActivated(@NotNull IGregTechTileEntity igte) {}

    default void onMachineDeactivated(@NotNull IGregTechTileEntity igte) {}
}
