package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IOnMachineRemovalComponent;
import gregtech.api.ec.component.IOnMachineUnloadComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IHatchContainerComponent
    extends IMultiblockComponent, IPreStructureCheckComponent, IOnMachineUnloadComponent, IOnMachineRemovalComponent {

    ComponentKey.ListKey<IHatchContainerComponent> KEY = ComponentKey.list();

    void detachAllHatches();

    void explodeAllHatches(long power);

    @Override
    default void onPreStructureCheck(@NotNull IGregTechTileEntity te) {
        detachAllHatches();
    }

    @Override
    default void onMachineUnload() {
        detachAllHatches();
    }

    @Override
    default void onMachineRemoval() {
        detachAllHatches();
    }
}
