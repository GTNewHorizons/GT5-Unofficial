package gregtech.api.ec.component.multiblock;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.enums.VoidingMode;

public interface IVoidingConfigComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IVoidingConfigComponent> KEY = ComponentKey.unique();

    Set<VoidingMode> getAllowedVoidingModes();

    VoidingMode getVoidingMode();

    void setVoidingMode(@NotNull VoidingMode mode);

    /**
     * @return if this machine is configured to not void excess item.
     */
    default boolean protectsExcessItem() {
        return getVoidingMode().protectItem;
    }

    /**
     * @return if this machine is configured to not void excess fluid.
     */
    default boolean protectsExcessFluid() {
        return getVoidingMode().protectFluid;
    }

}
