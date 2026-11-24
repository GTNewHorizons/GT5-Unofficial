package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnWorkStateChangeComponent extends IComponent {

    ComponentKey.ListKey<IOnWorkStateChangeComponent> KEY = ComponentKey.list();

    default void onEnableWorking() {}

    default void onDisableWorking() {}
}
