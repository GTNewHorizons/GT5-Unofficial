package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IOnMachineRemovalComponent;
import gregtech.api.ec.component.IOnMachineUnloadComponent;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface ISmartHatchLogicComponent extends IMultiblockComponent, IPreStructureCheckComponent,
    IOnMachineUnloadComponent, IOnMachineRemovalComponent, IUniqueComponent, IOnHatchAttachedComponent {

    ComponentKey.UniqueKey<ISmartHatchLogicComponent> KEY = ComponentKey.unique();

    @Override
    default void onHatchAttached(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        tryAttachSmartHatch(metaTile, baseCasingIndex);
    }

    boolean tryAttachSmartHatch(@NotNull IMetaTileEntity part, int baseCasingIndex);

    boolean needsPeriodicChecks();

    void detachAllHatches();

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
