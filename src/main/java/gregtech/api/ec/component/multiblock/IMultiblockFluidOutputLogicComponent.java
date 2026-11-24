package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IFluidOutputLogicComponent;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.util.GTUtility;

public interface IMultiblockFluidOutputLogicComponent extends IFluidOutputLogicComponent, IHatchContainerComponent {

    ComponentKey.UniqueKey<IMultiblockFluidOutputLogicComponent> KEY = ComponentKey.unique();

    @NotNull
    List<MTEHatchOutput> getFluidOutputHatches();

    @NotNull
    default Iterable<MTEHatchOutput> getValidFluidOutputHatches() {
        return validMTEList(getFluidOutputHatches());
    }

    boolean tryAttachFluidOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean canDumpFluidToME(@NotNull List<GTUtility.FluidId> outputs);

    /**
     * @param toOutput List of fluids this machine is going to output.
     * @return List of slots available for fluid outputs.
     */
    @NotNull
    List<IOutputHatch> getOutputHatches(@NotNull List<FluidStack> toOutput);

    @Deprecated
    default @NotNull List<IOutputHatch> getOutputHatches(FluidStack[] toOutput) {
        return getOutputHatches(Arrays.asList(toOutput));
    }

    default int getFluidOutputLimit() {
        return Integer.MAX_VALUE;
    }
}
