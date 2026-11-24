package gregtech.common.ec.multiblock.internal;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.IECMachine;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.ec.component.multiblock.IMultiblockComponent;
import gregtech.api.util.shutdown.ShutDownReason;

public class BaseMultiblockComponent implements IMultiblockComponent {

    private @Nullable IECMultiblock multiblock = null;

    @Override
    public final void onAddToMultiblock(@NotNull IECMultiblock multiblock) {
        this.multiblock = validMultiblock(multiblock);
    }

    protected IECMultiblock validMultiblock(@NotNull IECMultiblock multiblock) {
        return multiblock;
    }

    @Override
    public final @NotNull IECMultiblock getMachine() {
        if (multiblock == null) {
            throw new IllegalStateException(
                String.format("Component %s hasn't been attached to a machine yet.", getComponentName()));
        }
        return multiblock;
    }

    @Override
    public void onRemoveFromMachine(@NotNull IECMachine multiblock) {
        this.multiblock = null;
    }

    protected final void markDirty() {
        getMachine().markDirty();
    }

    protected final <T extends IComponent> @NotNull T getComponent(@NotNull ComponentKey.UniqueKey<T> type) {
        return getMachine().getComponent(type);
    }

    protected final <T extends IComponent> @Nullable T tryGetComponent(@NotNull ComponentKey.UniqueKey<T> type) {
        return getMachine().tryGetComponent(type);
    }

    protected final <T extends IComponent> boolean hasComponent(@NotNull ComponentKey<T> type) {
        return getMachine().hasComponent(type);
    }

    protected final <T extends IComponent> @NotNull T[] getComponents(@NotNull ComponentKey.ListKey<T> type) {
        return getMachine().getComponents(type);
    }

    protected final void stopMachine(@Nonnull ShutDownReason reason) {
        getMachine().stopMachine(reason);
    }

    protected final long getTotalRunTime() {
        return getMachine().getTotalRunTime();
    }

    protected final ItemStack getControllerSlot() {
        return getMachine().getControllerSlot();
    }

    protected final boolean isMachineFormed() {
        return getMachine().isFormed();
    }
}
