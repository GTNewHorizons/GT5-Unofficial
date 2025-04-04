package gtPlusPlus.core.tileentities.general;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileEntityInfiniteFluid extends TileEntity implements IFluidHandler, IFluidTank {

    public static final int SINGLE_FLUID = 0;
    public static final int SUPPLY_ALL_FLUIDS = 1;

    private boolean needsUpdate = false;
    private int updateTimer = 0;
    public FluidStack fluid = null;
    public int mode = SINGLE_FLUID;

    public TileEntityInfiniteFluid() {}

    public int changeMode() {
        if (this.mode == SINGLE_FLUID) {
            this.mode = SUPPLY_ALL_FLUIDS;
        } else {
            this.mode = SINGLE_FLUID;
        }
        return this.mode;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return this.fill(resource, doFill);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        switch (this.mode) {
            case SINGLE_FLUID:
                if (this.fluid == null) {
                    // set fluid if none is present
                    if (doFill) {
                        this.fluid = resource.copy();
                        this.fluid.amount = this.getCapacity();
                        this.needsUpdate = true;
                        FluidEvent.fireEvent(
                            new FluidEvent.FluidFillingEvent(
                                fluid,
                                this.getWorldObj(),
                                this.xCoord,
                                this.yCoord,
                                this.zCoord,
                                this,
                                resource.amount));
                    }
                    return resource.amount;
                } else if (this.fluid.isFluidEqual(resource)) {
                    if (doFill) {
                        // infinite capacity = it can be filled with an infinite amount of the fluid.
                        // it's still going to advertize it has max int capacity tho,
                        // but the capacity will effectively be infinite.
                        FluidEvent.fireEvent(
                            new FluidEvent.FluidFillingEvent(
                                fluid,
                                this.getWorldObj(),
                                this.xCoord,
                                this.yCoord,
                                this.zCoord,
                                this,
                                resource.amount));
                    }
                    return resource.amount;
                }
                return 0;
            case SUPPLY_ALL_FLUIDS:
            default:
                return 0;
        }

    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        switch (this.mode) {
            case SINGLE_FLUID:
                if (this.fluid == null) return null;
                if (this.fluid.isFluidEqual(resource)) {
                    if (doDrain) {
                        FluidEvent.fireEvent(
                            new FluidEvent.FluidDrainingEvent(
                                fluid,
                                this.getWorldObj(),
                                this.xCoord,
                                this.yCoord,
                                this.zCoord,
                                this,
                                resource.amount));
                    }
                    return resource.copy();
                }
                break;
            case SUPPLY_ALL_FLUIDS:
                if (doDrain) {
                    FluidEvent.fireEvent(
                        new FluidEvent.FluidDrainingEvent(
                            fluid,
                            this.getWorldObj(),
                            this.xCoord,
                            this.yCoord,
                            this.zCoord,
                            this,
                            resource.amount));
                }
                return resource.copy();
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.drain(maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.fluid == null) return null;
        FluidStack fs = this.fluid.copy();
        fs.amount = maxDrain;
        if (doDrain) {
            FluidEvent.fireEvent(
                new FluidEvent.FluidDrainingEvent(
                    fluid,
                    this.getWorldObj(),
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    this,
                    0));
        }
        return fs;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return this.mode == SINGLE_FLUID && this.fluid == null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        if (this.mode == SINGLE_FLUID) {
            return this.fluid != null && this.fluid.getFluid() == fluid;
        }
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { this.getInfo() };
    }

    @Override
    public void updateEntity() {
        if (needsUpdate) {
            updateTimer++;
            updateTimer %= 10;
            if (updateTimer == 0) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                needsUpdate = false;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.fluid = tag.hasKey("Empty") ? null : FluidStack.loadFluidStackFromNBT(tag);
        if (tag.getInteger("mode") == SUPPLY_ALL_FLUIDS) {
            this.mode = SUPPLY_ALL_FLUIDS;
        } else {
            this.mode = SINGLE_FLUID;
        }
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        if (this.fluid == null) {
            tag.setBoolean("Empty", true);
        } else {
            fluid.writeToNBT(tag);
        }
        tag.setInteger("mode", this.mode);
        super.writeToNBT(tag);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }

    public void setFluid(FluidStack fluid) {
        if (fluid == null) {
            this.fluid = null;
        } else {
            this.fluid = fluid.copy();
            this.fluid.amount = getCapacity();
        }
        this.needsUpdate = true;
    }

    @Override
    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public int getFluidAmount() {
        return this.fluid == null ? 0 : this.getCapacity();
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

}
