package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IEnergyInputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;

public interface IMultiblockEnergyInputLogicComponent
    extends IEnergyInputLogicComponent, IMultiblockEnergyHatchComponent, IHatchContainerComponent {

    ComponentKey.UniqueKey<IMultiblockEnergyInputLogicComponent> KEY = ComponentKey.unique();

    @NotNull
    List<MTEHatchEnergy> getNormalEnergyHatches();

    @NotNull
    default Iterable<MTEHatchEnergy> getValidNormalEnergyHatches() {
        return validMTEList(getNormalEnergyHatches());
    }

    @NotNull
    List<MTEHatch> getExoticEnergyHatches();

    @NotNull
    default Iterable<MTEHatch> getValidExoticEnergyHatches() {
        return validMTEList(getExoticEnergyHatches());
    }

    @NotNull
    List<MTEHatch> getEnergyInputHatches();

    default @NotNull List<MTEHatch> getEnergyHatches() {
        return getEnergyInputHatches();
    }

    @NotNull
    default Iterable<MTEHatch> getValidEnergyInputHatches() {
        return validMTEList(getEnergyInputHatches());
    }

    boolean tryAttachEnergyHatch(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean tryAttachMultiAmpEnergyInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean tryAttachExoticEnergyInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean isDebugEnergyPresent();
}
