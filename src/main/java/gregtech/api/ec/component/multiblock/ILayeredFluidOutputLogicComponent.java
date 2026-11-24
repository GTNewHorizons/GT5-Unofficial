package gregtech.api.ec.component.multiblock;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;

public interface ILayeredFluidOutputLogicComponent extends IMultiblockFluidOutputLogicComponent {

    ComponentKey.UniqueKey<ILayeredFluidOutputLogicComponent> KEY = ComponentKey.unique();

    default boolean tryAttachFluidOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        return false;
    }

    boolean tryAttachFluidOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex, int layerIndex);

    int getLayerHatchCount(int layerIndex);

    @NotNull
    List<List<MTEHatchOutput>> getFluidOutputHatchesByLayers();
}
