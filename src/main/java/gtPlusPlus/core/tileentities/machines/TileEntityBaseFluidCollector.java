package gtPlusPlus.core.tileentities.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.math.MathUtils;

public abstract class TileEntityBaseFluidCollector extends TileEntityBase implements IFluidHandler {

    public final FluidTank tank;
    private boolean needsUpdate = false;
    private int updateTimer = 0;
    private long internalTickCounter = 0;

    public TileEntityBaseFluidCollector(int aInvSlotCount, int aTankCapcity) {
        super(aInvSlotCount);
        tank = new BTF_FluidTank(aTankCapcity);
    }

    @Override
    public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        needsUpdate = true;
        return this.tank.fill(resource, doFill);
    }

    @Override
    public final FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        needsUpdate = true;
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        needsUpdate = true;
        FluidStack fluid = this.tank.getFluid();
        // return this.tank.drain(maxDrain, doDrain);
        if (fluid == null) {
            return null;
        }

        int drained = maxDrain;
        if (fluid.amount < drained) {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            fluid.amount -= drained;
            if (fluid.amount <= 0) {
                fluid = null;
            }

            FluidEvent.fireEvent(
                new FluidEvent.FluidDrainingEvent(
                    fluid,
                    this.getWorldObj(),
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    this.tank,
                    0));
        }
        return stack;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public final FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { this.tank.getInfo() };
    }

    @Override
    public final void updateEntity() {
        super.updateEntity();
        onPreLogicTick();
        logicTick();
        if (needsUpdate) {
            if (updateTimer == 0) {
                updateTimer = 10; // every 10 ticks it will send an update
            } else {
                --updateTimer;
                if (updateTimer == 0) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    needsUpdate = false;
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        tank.readFromNBT(tag);
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tank.writeToNBT(tag);
        super.writeToNBT(tag);
    }

    @Override
    public final Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
    }

    @Override
    public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }

    public int getBaseTickRate() {
        return MathUtils.randInt(200, 300);
    }

    public abstract ArrayList<Class> aThingsToLookFor();

    public abstract void onPreLogicTick();

    public final void logicTick() {

        if (this.worldObj == null || this.worldObj.isRemote) {
            return;
        }
        if (internalTickCounter % getBaseTickRate() == 0) {
            final World w = this.getWorldObj();
            if (w != null) {
                Chunk c = w.getChunkFromBlockCoords(this.xCoord, this.zCoord);
                if (c != null) {
                    if (c.isChunkLoaded) {
                        int startX = this.xCoord - 2;
                        int startY = this.yCoord;
                        int startZ = this.zCoord - 2;
                        int endX = this.xCoord + 3;
                        int endY = this.yCoord + 5;
                        int endZ = this.zCoord + 3;
                        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(startX, startY, startZ, endX, endY, endZ);
                        for (Class c2 : aThingsToLookFor()) {
                            tickEntityType(w, box, c2);
                        }
                    }
                }
            }
        }
        internalTickCounter++;
    }

    @SuppressWarnings("unchecked")
    public final void tickEntityType(World w, AxisAlignedBB box, Class aClassToFind) {
        List<?> entities = w.getEntitiesWithinAABB(aClassToFind, box);
        if (entities != null && !entities.isEmpty()) {
            interactWithEntities(entities);
        }
    }

    public final <V> void interactWithEntities(List<V> entities) {
        for (V aEntity : entities) {
            addDrop(aEntity);
            if (this.tank.getFluidAmount() < this.tank.getCapacity()) {
                int aFluidAmount = onPostTick(aEntity);
                aFluidAmount = Math
                    .max(Math.min(this.tank.getCapacity() - this.tank.getFluidAmount(), aFluidAmount), 1);
                this.tank.fill(new FluidStack(fluidToProvide(), aFluidAmount), true);
            } else {
                ItemStack aDirtStack = GTUtility.copyAmount(1, itemToSpawnInWorldIfTankIsFull());

                if (aDirtStack == null) return;

                if (!this.mInventory.addItemStack(aDirtStack)) {
                    EntityItem entity = new EntityItem(worldObj, xCoord, yCoord + 1.5, zCoord, aDirtStack);
                    worldObj.spawnEntityInWorld(entity);
                }
            }
        }
    }

    /**
     * Return the amount of fluid for this entity type
     *
     * @param aEntity
     * @return
     */
    public abstract <V> int onPostTick(V aEntity);

    public abstract <V> boolean addDrop(V aPooMaker);

    public abstract Fluid fluidToProvide();

    public abstract ItemStack itemToSpawnInWorldIfTankIsFull();
}
