package gtPlusPlus.core.tileentities.machines;

import java.util.List;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
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

public abstract class TileEntityBaseFluidCollector extends TileEntityBase implements IFluidHandler {

	public final FluidTank tank;
	private boolean needsUpdate = false;
	private int updateTimer = 0;
	private long internalTickCounter = 0;
	private BlockPos internalBlockLocation;
	
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

			if (this != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, this.getWorldObj(), this.xCoord,
						this.yCoord, this.zCoord, this.tank, 0));
			}
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
	
	public abstract AutoMap<Class> aThingsToLookFor();
	
	public abstract void onPreLogicTick();
	
	public final void logicTick() {

		if (this.worldObj == null || this.worldObj.isRemote) {
			return;
		}
		if (internalTickCounter % MathUtils.randInt(200, 300) == 0) {
			if (internalBlockLocation == null) {
				internalBlockLocation = new BlockPos(this);
			}
			BlockPos p = internalBlockLocation;
			if (p != null) {
				if (p.world != null) {
					World w = this.worldObj;
					if (w == null) {
						return;
					}
					Chunk c = w.getChunkFromBlockCoords(p.xPos, p.zPos);
					if (c != null) {
						if (c.isChunkLoaded) {
							int startX = p.xPos - 2;
							int startY = p.yPos;
							int startZ = p.zPos - 2;
							int endX = p.xPos + 3;
							int endY = p.yPos + 5;
							int endZ = p.zPos + 3;
							AxisAlignedBB box = AxisAlignedBB.getBoundingBox(startX, startY, startZ, endX, endY, endZ);
							if (box != null) {								
								for (Class c2 : aThingsToLookFor()) {
									tickEntityType(w, box, c2);										
								}														
							} else {
								return;
							}
						}
					}
				}
			}

		}

		internalTickCounter++;
	}
	
	@SuppressWarnings("unchecked")
	public final void tickEntityType(World w, AxisAlignedBB box, Class aClassToFind) {
		List<EntityAnimal> entities = w.getEntitiesWithinAABB(aClassToFind, box);	
		if (entities != null && !entities.isEmpty()) {
			onPostTick(entities);
		}
	}
	
	public final <V> void interactWithEntities(List<V> entities) {
		for (V aEntity : entities) {
			addDrop(aEntity);
			if (this.tank.getFluidAmount() < this.tank.getCapacity()) {				
				int aFluidAmount = onPostTick(aEntity);								
				aFluidAmount = Math.max(Math.min(this.tank.getCapacity()-this.tank.getFluidAmount(), aFluidAmount), 1);				
				this.tank.fill(FluidUtils.getFluidStack(fluidToProvide(), aFluidAmount), true);
			}
			else {
				ItemStack aDirtStack = ItemUtils.getSimpleStack(itemToSpawnInWorldIfTankIsFull());
				if (!this.mInventory.addItemStack(aDirtStack)) {
					EntityItem entity = new EntityItem(worldObj, xCoord, yCoord+1.5, zCoord, aDirtStack);
					worldObj.spawnEntityInWorld(entity);
				}
			}
		}	
	}
	

	/**
	 * Return the amount of fluid for this entity type
	 * @param aEntity
	 * @return
	 */
	public abstract <V> int onPostTick(V aEntity);
	
	public abstract <V> boolean addDrop(V aPooMaker);
	
	public abstract Fluid fluidToProvide();
	
	public abstract Item itemToSpawnInWorldIfTankIsFull();
	
	
	

}
