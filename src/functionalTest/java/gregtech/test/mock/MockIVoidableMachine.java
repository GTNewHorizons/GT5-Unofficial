package gregtech.test.mock;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IVoidable;

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
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        return null;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return null;
    }

    @Override
    public boolean canDumpItemToME() {
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
