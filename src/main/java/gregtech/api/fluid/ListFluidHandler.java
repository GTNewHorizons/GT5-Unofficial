package gregtech.api.fluid;

import java.util.Iterator;

import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

public class ListFluidHandler implements FluidHandler {

    protected final Iterable<? extends FluidHandler> fluidHandlers;

    public ListFluidHandler(Iterable<? extends FluidHandler> fluidHandlers) {
        this.fluidHandlers = fluidHandlers;
    }

    @Override
    public int getTanks() {
        int tanks = 0;
        FluidHandler fluidHandler;
        for (Iterator<? extends FluidHandler> iterator = fluidHandlers.iterator(); iterator
            .hasNext(); tanks += fluidHandler.getTanks()) {
            fluidHandler = iterator.next();
        }
        return tanks;
    }

    @Override
    public FluidStack getFluidInSlot(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getFluidInSlot(result.getRight());
    }

    @Override
    public FluidStack insertFluid(int tank, FluidStack fluid, boolean simulate) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .insertFluid(result.getRight(), fluid, simulate);
    }

    @Override
    public FluidStack extractFluid(int tank, int amount, boolean simulate) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .extractFluid(result.getRight(), amount, simulate);
    }

    @Override
    public long getTankLimit(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getTankLimit(result.getRight());
    }

    @Override
    public long getTankAmount(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getTankAmount(result.getRight());
    }

    @Override
    public void setFluidInTank(int tank, FluidStack fluid) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        result.getLeft()
            .setFluidInTank(result.getRight(), fluid);
    }

    protected Pair<? extends FluidHandler, Integer> findFluidHandler(int tank) {
        int searching = 0;
        int amountOfTanks;

        for (Iterator<? extends FluidHandler> iterator = fluidHandlers.iterator(); iterator
            .hasNext(); searching += amountOfTanks) {
            FluidHandler fluidHandler = iterator.next();
            amountOfTanks = fluidHandler.getTanks();
            if (tank >= searching && tank < searching + amountOfTanks) {
                return Pair.of(fluidHandler, tank - searching);
            }
        }

        throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
    }
}
