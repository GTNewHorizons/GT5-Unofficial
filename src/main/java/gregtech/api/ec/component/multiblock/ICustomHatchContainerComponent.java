package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;

public interface ICustomHatchContainerComponent extends IHatchContainerComponent, IUniqueComponent {

    ComponentKey.UniqueKey<ICustomHatchContainerComponent> KEY = ComponentKey.unique();

    <T extends MTEHatch> boolean supports(@NotNull CustomHatchKey<T> key);

    <T extends MTEHatch> boolean tryAttach(@NotNull CustomHatchKey<T> key, @NotNull IMetaTileEntity metaTile,
        int baseCasingIndex);

    <T extends MTEHatch> @NotNull List<T> getHatches(@NotNull CustomHatchKey<T> key);

    default <T extends MTEHatch> Iterable<T> getValidHatches(@NotNull CustomHatchKey<T> key) {
        return validMTEList(getHatches(key));
    }

    default <T extends MTEHatch> int getHatchCount(@NotNull CustomHatchKey<T> key) {
        return getHatches(key).size();
    }
}
