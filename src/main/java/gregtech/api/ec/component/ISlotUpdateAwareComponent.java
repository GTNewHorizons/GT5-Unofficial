package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface ISlotUpdateAwareComponent extends IComponent {

    ComponentKey.ListKey<ISlotUpdateAwareComponent> KEY = ComponentKey.list();

    void updateSlots();
}
