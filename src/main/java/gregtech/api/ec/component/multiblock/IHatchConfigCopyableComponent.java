package gregtech.api.ec.component.multiblock;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.metatileentity.implementations.MTEHatch;

public interface IHatchConfigCopyableComponent extends IHatchContainerComponent {

    ComponentKey.ListKey<IHatchConfigCopyableComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IHatchConfigCopyableComponent::getHatchConfigCopyablePriority));

    enum HatchConfigCopyablePriority {

        STANDARD(1000),
        ME(2000);

        public final int priority;

        HatchConfigCopyablePriority(int priority) {
            this.priority = priority;
        }
    }

    @NotNull
    Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches();

    default int getHatchConfigCopyablePriority() {
        return HatchConfigCopyablePriority.STANDARD.priority;
    }
}
