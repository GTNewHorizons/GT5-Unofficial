package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IEnergyOutputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;

public interface IMultiblockEnergyOutputLogicComponent
    extends IEnergyOutputLogicComponent, IMultiblockEnergyHatchComponent, IHatchContainerComponent {

    ComponentKey.UniqueKey<IMultiblockEnergyOutputLogicComponent> KEY = ComponentKey.unique();

    @NotNull
    List<MTEHatchDynamo> getNormalDynamoHatches();

    @NotNull
    default Iterable<MTEHatchDynamo> getValidNormalDynamoHatches() {
        return validMTEList(getNormalDynamoHatches());
    }

    @NotNull
    List<MTEHatch> getExoticDynamoHatches();

    @NotNull
    default Iterable<MTEHatch> getValidExoticDynamoHatches() {
        return validMTEList(getExoticDynamoHatches());
    }

    @NotNull
    List<MTEHatch> getEnergyOutputHatches();

    default @NotNull List<MTEHatch> getEnergyHatches() {
        return getEnergyOutputHatches();
    }

    @NotNull
    default Iterable<MTEHatch> getValidEnergyOutputHatches() {
        return validMTEList(getEnergyOutputHatches());
    }

    boolean tryAttachNormalDynamo(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean tryAttachExoticDynamo(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean tryAttachLaserSource(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);
}
