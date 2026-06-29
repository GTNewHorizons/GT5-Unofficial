package gregtech.api.util;

import java.util.function.Predicate;

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
    private final Predicate<GTUtility.FluidId> filter;

    public OutputHatchWrapper(@NotNull MTEHatchOutput outputHatch, @NotNull Predicate<GTUtility.FluidId> filter) {
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
        return filter.test(id);
    }

    @Override
    public OutputHatchType getHatchType() {
        return outputHatch.getHatchType();
    }

    @Override
    public boolean storePartial(FluidStack stack, boolean simulate) {
        if (!isFilteredToFluid(GTUtility.FluidId.create(stack))) return false;
        return outputHatch.storePartial(stack, simulate);
    }

    @Override
    public IOutputHatchTransaction createTransaction() {
        return new FilteredTransactionWrapper();
    }

    public class FilteredTransactionWrapper
        implements IOutputHatchTransaction, IOutputHatchTransaction.IRecipeCheckAware,
        IOutputHatchTransaction.IProtectOutputAware, IOutputHatchTransaction.IDynamicCapacityOutputAware {

        private final OutputHatchWrapper hatch = OutputHatchWrapper.this;
        private final IOutputHatchTransaction transaction = OutputHatchWrapper.this.outputHatch.createTransaction();

        @Override
        public IOutputHatch getHatch() {
            return hatch;
        }

        @Override
        public void setRecipeCheck(boolean isRecipeCheck) {
            if (transaction instanceof IOutputHatchTransaction.IRecipeCheckAware rt) {
                rt.setRecipeCheck(isRecipeCheck);
            }
        }

        @Override
        public void setProtectOutput(boolean isProtectOutput) {
            if (transaction instanceof IOutputHatchTransaction.IProtectOutputAware rt) {
                rt.setProtectOutput(isProtectOutput);
            }
        }

        @Override
        public boolean isDynamicCapacity() {
            if (transaction instanceof IOutputHatchTransaction.IDynamicCapacityOutputAware rt) {
                return rt.isDynamicCapacity();
            }
            return false;
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
