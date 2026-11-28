package gregtech.api.ec.component.multiblock;

import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;

public interface IPostStructureCheckComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IPostStructureCheckComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IPostStructureCheckComponent::getPostStructureCheckPriority));

    enum PostStructureCheckPriority {

        STRUCTURE_BOUNDS(1000),
        COIL(2000);

        public final int priority;

        PostStructureCheckPriority(int priority) {
            this.priority = priority;
        }
    }

    void onPostStructureCheck(@NotNull IGregTechTileEntity te, @NotNull List<StructureError> errors);

    int getPostStructureCheckPriority();
}
