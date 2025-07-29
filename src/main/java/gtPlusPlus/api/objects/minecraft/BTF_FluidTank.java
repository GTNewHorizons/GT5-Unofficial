package gtPlusPlus.api.objects.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BTF_FluidTank extends FluidTank {

    public FluidStack mFluid;

    public BTF_FluidTank(int capacity) {
        super(capacity);
    }

    /**
     * Let's replace the Default handling with GT's own handling code, because it's probably better, right?
     *
     * @author Alkalus/GregoriusT
     */
    @Override
    public FluidStack getFluid() {
        return this.getDrainableStack();
    }

    @Override
    public int getFluidAmount() {
        return this.getDrainableStack() != null ? this.getDrainableStack().amount : 0;
    }

    @Override
    public NBTTagCompound writeToNBT(@NotNull NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        if (this.mFluid != null) {
            aNBT.setTag("mFluid", this.mFluid.writeToNBT(new NBTTagCompound()));
        }
        return aNBT;
    }

    @Override
    public @NotNull FluidTank readFromNBT(@NotNull NBTTagCompound aNBT) {
        this.mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
        return this;
    }

    /*
     * public abstract boolean isLiquidInput(byte arg0); public abstract boolean isLiquidOutput(byte arg0); public
     * abstract boolean doesFillContainers(); public abstract boolean doesEmptyContainers();
     */

    public boolean canTankBeFilled() {
        return true;
    }

    public boolean canTankBeEmptied() {
        return true;
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return true;
    }

    public FluidStack getFillableStack() {
        return this.mFluid;
    }

    public FluidStack setFillableStack(FluidStack aFluid) {
        this.mFluid = aFluid;
        return this.mFluid;
    }

    public FluidStack getDrainableStack() {
        return this.mFluid;
    }

    public FluidStack setDrainableStack(FluidStack aFluid) {
        this.mFluid = aFluid;
        return this.mFluid;
    }

    public boolean isFluidChangingAllowed() {
        return true;
    }

    @Override
    public int fill(@Nullable FluidStack aFluid, boolean doFill) {
        if (aFluid != null && aFluid.getFluid()
            .getID() > 0 && aFluid.amount > 0 && this.canTankBeFilled() && this.isFluidInputAllowed(aFluid)) {
            if (this.getFillableStack() != null && this.getFillableStack()
                .getFluid()
                .getID() > 0) {
                if (!this.getFillableStack()
                    .isFluidEqual(aFluid)) {
                    return 0;
                } else {
                    int space = this.getCapacity() - this.getFillableStack().amount;
                    if (aFluid.amount <= space) {
                        if (doFill) {
                            FluidStack arg9999 = this.getFillableStack();
                            arg9999.amount += aFluid.amount;
                        }

                        return aFluid.amount;
                    } else {
                        if (doFill) {
                            this.getFillableStack().amount = this.getCapacity();
                        }

                        return space;
                    }
                }
            } else if (aFluid.amount <= this.getCapacity()) {
                if (doFill) {
                    this.setFillableStack(aFluid.copy());
                }

                return aFluid.amount;
            } else {
                if (doFill) {
                    this.setFillableStack(aFluid.copy());
                    this.getFillableStack().amount = this.getCapacity();
                }

                return this.getCapacity();
            }
        } else {
            return 0;
        }
    }

    @Override
    public @Nullable FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.getDrainableStack() != null && this.canTankBeEmptied()) {
            if (this.getDrainableStack().amount <= 0 && this.isFluidChangingAllowed()) {
                this.setDrainableStack(null);
                return null;
            } else {
                int used = Math.min(this.getDrainableStack().amount, maxDrain);

                if (doDrain) {
                    FluidStack arg9999 = this.getDrainableStack();
                    arg9999.amount -= used;
                }

                FluidStack drained = this.getDrainableStack()
                    .copy();
                drained.amount = used;
                if (this.getDrainableStack().amount <= 0 && this.isFluidChangingAllowed()) {
                    this.setDrainableStack(null);
                }

                return drained;
            }
        } else {
            return null;
        }
    }

    @Override
    public int getCapacity() {
        return super.getCapacity();
    }

    @Override
    public void setFluid(FluidStack fluid) {
        setFillableStack(fluid);
    }

    @Override
    public void setCapacity(int capacity) {
        super.setCapacity(capacity);
    }

    public FluidStack drain(@NotNull FluidStack aFluid, boolean doDrain) {
        return drain(aFluid.amount, doDrain);
    }
}
