package gregtech.api.fluid;

import java.util.Iterator;

import net.minecraftforge.fluids.Fluid;
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
    public FluidStack getFluidInTank(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getFluidInTank(result.getRight());
    }

    @Override
    public FluidStack insertFluid(int tank, Fluid fluid, long amount, boolean simulate) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .insertFluid(result.getRight(), fluid, amount, simulate);
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
    public long getTankCapacity(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getTankCapacity(result.getRight());
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

    @Override
    public void setFluidInTank(int tank, Fluid fluid, long amount) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        result.getLeft()
            .setFluidInTank(result.getRight(), fluid, amount);
    }

    @Override
    public FluidStackHolder getFluidHolderInTank(int tank) {
        if (tank < 0) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.getTanks() + ")");
        }

        Pair<? extends FluidHandler, Integer> result = findFluidHandler(tank);
        return result.getLeft()
            .getFluidHolderInTank(tank);
    }
}
