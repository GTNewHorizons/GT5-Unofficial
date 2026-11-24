package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.IECMachine;
import gregtech.api.ec.IECMultiblock;

public interface IMultiblockComponent extends IComponent {

    ComponentKey.ListKey<IMultiblockComponent> KEY = ComponentKey.list();

    @Override
    default void onAddToMachine(@NotNull IECMachine machine) {
        if (machine instanceof IECMultiblock multiblock) {
            onAddToMultiblock(multiblock);
        } else {
            throw new IllegalArgumentException("Machine must be an IECMultiblock");
        }
    }

    @Override
    default void onRemoveFromMachine(@NotNull IECMachine machine) {}

    void onAddToMultiblock(@NotNull IECMultiblock multiblock);

    @Override
    @NotNull
    IECMultiblock getMachine();
}
