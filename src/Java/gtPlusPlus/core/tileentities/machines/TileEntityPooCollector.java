package gtPlusPlus.core.tileentities.machines;

import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
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

public class TileEntityPooCollector extends TileEntityBase implements IFluidHandler {

	public FluidTank tank = new BTF_FluidTank(64000);
	private boolean needsUpdate = false;
	private int updateTimer = 0;
	private boolean hasRegistered = false;
	private long internalTickCounter = 0;
	private BlockPos internalBlockLocation;
	
	public TileEntityPooCollector() {	
		super(9);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		needsUpdate = true;
		return this.tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		needsUpdate = true;
		return this.tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
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
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	@Override
	public void updateEntity() {			
		onPreTick();
		onTick();		
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
	
	public void onPreTick() {
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
	
	public void onTick() {

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
								@SuppressWarnings("unchecked")
								List<EntityAnimal> animals = w.getEntitiesWithinAABB(EntityAnimal.class, box);
								if (animals != null && !animals.isEmpty()) {
									onPostTick(animals);
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

	public void onPostTick(List<EntityAnimal> animals) {
		for (EntityAnimal aPooMaker : animals) {
			//Logger.INFO("Sewer found "+aPooMaker.getCommandSenderName());
			addDrop(aPooMaker);
			if (this.tank.getFluidAmount() < this.tank.getCapacity()) {
				
				int aPooAmount = 0;
				if (aPooMaker instanceof EntityChicken) {
					aPooAmount = MathUtils.randInt(1, 40);
				}
				else if (aPooMaker instanceof EntityHorse) {
					aPooAmount = MathUtils.randInt(20, 40);
				}
				else if (aPooMaker instanceof EntityCow) {
					aPooAmount = MathUtils.randInt(18, 45);
				}
				else if (aPooMaker instanceof EntityMooshroom) {
					aPooAmount = 17;
				}
				else if (aPooMaker instanceof EntitySheep) {
					aPooAmount = MathUtils.randInt(8, 30);
				}
				else {
					aPooAmount = MathUtils.randInt(5, 35);
				}
				
				aPooAmount = Math.max(Math.min(this.tank.getCapacity()-this.tank.getFluidAmount(), aPooAmount), 1);
				
				this.tank.fill(FluidUtils.getFluidStack(AgriculturalChem.PoopJuice, aPooAmount), true);
			}
			else {
				ItemStack aDirtStack = ItemUtils.getSimpleStack(AgriculturalChem.dustDirt);
				if (!this.mInventory.addItemStack(aDirtStack)) {
					EntityItem entity = new EntityItem(worldObj, xCoord, yCoord+1.5, zCoord, aDirtStack);
					worldObj.spawnEntityInWorld(entity);
				}
			}
		}
		
	}
	
	public boolean addDrop(EntityAnimal aAnimal) {
		int aChance = MathUtils.randInt(0, 50000);
		if (aChance > 0) {
			ItemStack aPoop;
			if (aChance<= 100) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1);
			}
			else if (aChance <= 500) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);				
			}
			else if (aChance <= 1250) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);				
			}
			else {
				return false;
			}
			//Add poop to world
			Logger.INFO("Adding animal waste for "+aAnimal.getCommandSenderName());
			
			//Add to inventory if not full, else espawn in world
			if (!this.mInventory.addItemStack(aPoop)) {
				EntityItem entity = new EntityItem(worldObj, xCoord, yCoord+1.5, zCoord, aPoop);
				worldObj.spawnEntityInWorld(entity);
			}			
			
		}
		
		
		return false;
	}

}
