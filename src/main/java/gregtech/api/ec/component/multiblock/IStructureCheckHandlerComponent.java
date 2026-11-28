package gregtech.api.ec.component.multiblock;

import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;

public interface IStructureCheckHandlerComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IStructureCheckHandlerComponent> KEY = ComponentKey.list(
        Comparator.comparingInt((IStructureCheckHandlerComponent c) -> c.getStructureCheckPhase().order)
            .thenComparing(IStructureCheckHandlerComponent::getStructureCheckPriority));

    enum StructureCheckPhase {

        SCAN(1000),
        VERIFY(2000);

        public final int order;

        StructureCheckPhase(int order) {
            this.order = order;
        }
    }

    enum StructureCheckPriority {

        MACHINE_CHECK(1000),
        POLLUTION(2000),
        COIL(3000);

        public final int priority;

        StructureCheckPriority(int priority) {
            this.priority = priority;
        }
    }

    void checkMachine(@NotNull IGregTechTileEntity aBaseMetaTileEntity, @NotNull List<StructureError> errors);

    default StructureCheckPhase getStructureCheckPhase() {
        return StructureCheckPhase.VERIFY;
    }

    int getStructureCheckPriority();
}
