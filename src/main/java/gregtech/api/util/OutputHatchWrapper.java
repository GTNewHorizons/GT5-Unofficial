package gregtech.api.util;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.OutputHatchType;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.IOutputHatchTransaction;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;

/**
 * Wrapper for output hatch to allow multiblocks to apply specific filter.
 */
public class OutputHatchWrapper implements IOutputHatch {

    private final MTEHatchOutput outputHatch;
    private final FluidStack filter;

    public OutputHatchWrapper(@NotNull MTEHatchOutput outputHatch, @NotNull FluidStack filter) {
        this.outputHatch = outputHatch;
        this.filter = filter;
    }

    public MTEHatchOutput unwrap() {
        return outputHatch;
    }

    @Override
    public boolean isFiltered() {
        return true;
    }

    @Override
    public boolean isFilteredToFluid(GTUtility.FluidId id) {
        return id.matches(filter);
    }

    @Override
    public OutputHatchType getHatchType() {
        return outputHatch.getHatchType();
    }

    @Override
    public boolean storePartial(FluidStack stack, boolean simulate) {
        if (!GTUtility.areFluidsEqual(stack, filter)) return false;
        return outputHatch.storePartial(stack, simulate);
    }

    @Override
    public IOutputHatchTransaction createTransaction() {
        return new FilteredTransactionWrapper();
    }

    public class FilteredTransactionWrapper implements IOutputHatchTransaction {

        private final OutputHatchWrapper hatch = OutputHatchWrapper.this;
        private final IOutputHatchTransaction transaction = OutputHatchWrapper.this.outputHatch.createTransaction();

        @Override
        public IOutputHatch getHatch() {
            return hatch;
        }

        @Override
        public boolean hasAvailableSpace() {
            return transaction.hasAvailableSpace();
        }

        @Override
        public boolean storePartial(GTUtility.FluidId id, @NotNull FluidStack stack) {
            if (!hatch.isFilteredToFluid(id)) return false;
            return transaction.storePartial(id, stack);
        }

        @Override
        public void completeFluid(GTUtility.FluidId id) {
            transaction.completeFluid(id);
        }

        @Override
        public void commit() {
            transaction.commit();
        }
    }
}
