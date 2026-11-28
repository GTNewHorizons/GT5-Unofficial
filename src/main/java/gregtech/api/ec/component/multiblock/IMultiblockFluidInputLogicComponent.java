package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IFluidInputLogicComponent;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.GTUtility;

public interface IMultiblockFluidInputLogicComponent
    extends IFluidInputLogicComponent, IHatchContainerComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IMultiblockFluidInputLogicComponent> KEY = ComponentKey.unique();

    @NotNull
    List<? extends MTEHatchInput> getFluidInputHatches();

    @NotNull
    default Iterable<? extends MTEHatchInput> getValidFluidInputHatches() {
        return validMTEList(getFluidInputHatches());
    }

    boolean tryAttachFluidInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    short getHatchColors();

    @NotNull
    Map<GTUtility.FluidId, FluidStack> getStoredFluidsFromME();
}
