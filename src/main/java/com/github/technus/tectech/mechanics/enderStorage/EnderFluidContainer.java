package com.github.technus.tectech.mechanics.enderStorage;

import java.io.Serializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class EnderFluidContainer implements IFluidHandler, Serializable {
    private static final int CAPACITY = 64000;

    @Deprecated()
    private int fluidID = -1;

    @Deprecated()
    private int fluidQuantity = 0;

    private NBTTagCompound fluid;

    public EnderFluidContainer() {}

    private FluidStack getFluidStack() {
        FluidStack fluidStack = null;
        if (fluidID >= 0) {
            fluid = new FluidStack(fluidID, fluidQuantity).writeToNBT(new NBTTagCompound());
            fluidID = -1;
            fluidQuantity = 0;
        }
        if (fluid != null) {
            fluidStack = FluidStack.loadFluidStackFromNBT(fluid);
        }
        return fluidStack;
    }

    private void setFluidStack(FluidStack fluidStack) {
        if (fluidStack != null) {
            fluid = fluidStack.writeToNBT(new NBTTagCompound());
        } else {
            fluid = null;
        }
    }

    @Override
    public int fill(ForgeDirection side, FluidStack fluidStackIn, boolean doFill) {
        int filledFluid = 0;
        FluidStack fluidStackStored = getFluidStack();
        if (fluidStackIn != null) {
            if (fluidStackStored == null) {
                fluidStackStored = fluidStackIn.copy();
                fluidStackStored.amount = 0;
            }
            if (fluidStackStored.amount < CAPACITY && fluidStackIn.isFluidEqual(fluidStackStored)) {
                filledFluid = Math.min(CAPACITY - fluidStackStored.amount, fluidStackIn.amount);
                if (doFill) {
                    fluidStackStored.amount += filledFluid;
                    setFluidStack(fluidStackStored);
                }
            }
        }
        return filledFluid;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluidStack, boolean doDrain) {
        FluidStack fluidStackOutput = null;
        if (fluidStack != null && fluidStack.isFluidEqual(getFluidStack())) {
            fluidStackOutput = drain(side, fluidStack.amount, doDrain);
        }
        return fluidStackOutput;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int amount, boolean doDrain) {
        FluidStack fluidStackOutput = null;
        FluidStack fluidStackStored = getFluidStack();
        if (fluidStackStored != null && fluidStackStored.amount > 0) {
            int drainedFluid = Math.min(fluidStackStored.amount, amount);
            fluidStackOutput = fluidStackStored.copy();
            fluidStackOutput.amount = drainedFluid;
            if (doDrain) {
                fluidStackStored.amount -= drainedFluid;
                if (fluidStackStored.amount == 0) {
                    fluidStackStored = null;
                }
                setFluidStack(fluidStackStored);
            }
        }
        return fluidStackOutput;
    }

    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        return new FluidTankInfo[] {new FluidTankInfo(getFluidStack(), CAPACITY)};
    }
}
