package gregtech.test.mock;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.util.GTUtility;

public class MockIVoidableMachine implements IVoidable {

    protected VoidingMode voidingMode = getDefaultVoidingMode();

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return voidingMode;
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {
        voidingMode = mode;
    }

    @Override
    public List<IOutputBus> getOutputBusses() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return null;
    }

    @Override
    public boolean canDumpItemToME(List<GTUtility.ItemId> outputs) {
        return false;
    }

    @Override
    public boolean canDumpFluidToME() {
        return false;
    }

    @Override
    public VoidingMode getDefaultVoidingMode() {
        return VoidingMode.VOID_ALL;
    }
}
