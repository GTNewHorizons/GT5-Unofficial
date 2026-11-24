package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IItemOutputLogicComponent;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.GTUtility;

public interface IMultiblockItemOutputLogicComponent extends IItemOutputLogicComponent, IHatchContainerComponent {

    ComponentKey.UniqueKey<IMultiblockItemOutputLogicComponent> KEY = ComponentKey.unique();

    @NotNull
    List<MTEHatchOutputBus> getItemOutputHatches();

    @NotNull
    default Iterable<MTEHatchOutputBus> getValidItemOutputHatches() {
        return validMTEList(getItemOutputHatches());
    }

    boolean tryAttachItemOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean canDumpItemToME(@NotNull List<GTUtility.ItemId> outputs);

    @NotNull
    List<IOutputBus> getOutputBusses();

    default int getItemOutputLimit() {
        return Integer.MAX_VALUE;
    }
}
