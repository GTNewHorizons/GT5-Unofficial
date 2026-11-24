package gregtech.api.ec;

import org.jetbrains.annotations.NotNull;

public interface IComponent {

    ComponentKey.ListKey<IComponent> KEY = ComponentKey.list();

    default String getComponentName() {
        return this.getClass()
            .getSimpleName();
    }

    void onAddToMachine(@NotNull IECMachine machine);

    void onRemoveFromMachine(@NotNull IECMachine machine);

    @NotNull
    IECMachine getMachine();

    default void onComponentsReady() {}
}
