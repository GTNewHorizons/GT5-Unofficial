package gregtech.api.util;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;

/**
 * Wrapper for output hatch to allow multiblocks to apply specific filter.
 */
public class OutputHatchWrapper implements IFluidStore {

    private final GT_MetaTileEntity_Hatch_Output outputHatch;
    private final Predicate<FluidStack> filter;

    public OutputHatchWrapper(GT_MetaTileEntity_Hatch_Output outputHatch, Predicate<FluidStack> filter) {
        this.outputHatch = outputHatch;
        this.filter = filter;
    }

    @Override
    public FluidStack getFluid() {
        return outputHatch.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return outputHatch.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return outputHatch.getCapacity();
    }

    @Override
    public FluidTankInfo getInfo() {
        return outputHatch.getInfo();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return outputHatch.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return outputHatch.drain(maxDrain, doDrain);
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return outputHatch.isEmptyAndAcceptsAnyFluid();
    }

    @Override
    public boolean canStoreFluid(@NotNull FluidStack fluidStack) {
        return outputHatch.canStoreFluid(fluidStack) && filter.test(fluidStack);
    }
}
