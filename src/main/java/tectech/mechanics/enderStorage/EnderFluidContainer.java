package tectech.mechanics.enderStorage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import tectech.TecTech;

public class EnderFluidContainer implements IFluidHandler, Serializable {

    private static final long serialVersionUID = 2L;
    private static final int SERIALIZE_TYPE_WITH_NBT = 0;
    private static final int SERIALIZE_TYPE_WITHOUT_NBT = 1;
    private static final int SERIALIZE_TYPE_NULL = 2;

    private static final int CAPACITY = 64000;
    private transient FluidStack fluidStack;

    public EnderFluidContainer() {}

    private FluidStack getFluidStack() {
        return fluidStack;
    }

    private void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
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
        return new FluidTankInfo[] { new FluidTankInfo(getFluidStack(), CAPACITY) };
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (fluidStack != null) {
            out.writeByte(fluidStack.tag != null ? SERIALIZE_TYPE_WITH_NBT : SERIALIZE_TYPE_WITHOUT_NBT);
            if (fluidStack.tag != null) CompressedStreamTools.write(fluidStack.tag, out);
            out.writeUTF(
                fluidStack.getFluid()
                    .getName());
            out.writeInt(fluidStack.amount);
        } else {
            out.writeByte(SERIALIZE_TYPE_NULL);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        byte type = in.readByte();
        NBTTagCompound tag = null;
        switch (type) {
            case SERIALIZE_TYPE_WITH_NBT:
                tag = CompressedStreamTools.read(new DataInputStream(in));
            case SERIALIZE_TYPE_WITHOUT_NBT:
                fluidStack = FluidRegistry.getFluidStack(in.readUTF(), in.readInt());
                break;
            case SERIALIZE_TYPE_NULL:
                fluidStack = null;
                break;
            default:
                TecTech.LOGGER.error("Something very wrong... got a fluid container with state " + type);
                fluidStack = null;
        }
        if (fluidStack != null) fluidStack.tag = tag;
    }
}
