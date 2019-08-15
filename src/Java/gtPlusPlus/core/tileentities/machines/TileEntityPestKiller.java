package gtPlusPlus.core.tileentities.machines;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BTF_FluidTank;
import gtPlusPlus.core.inventories.InventoryPestKiller;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityPestKiller extends TileEntity implements ISidedInventory, IFluidHandler {

	private final int mBaseTickRate = 20 * 30;
	private final InventoryPestKiller mInventory;
	private final FluidTank mTank;
	private int mChunkX;
	private int mChunkZ;
	private boolean mSet = false;

	private int mTickCounter = 0;
	private int mUpdateTick = 0;
	private boolean mNeedsUpdate = false;
	private String mCustomName;

	private static final AutoMap<Class<?>> mEntityMap = new AutoMap<Class<?>>();

	static {
		mEntityMap.put(EntityBat.class);
		if (LoadedMods.Forestry) {
			mEntityMap.put(ReflectionUtils.getClass("forestry.lepidopterology.entities.EntityButterfly"));
		}
	}

	public TileEntityPestKiller() {
		this.mInventory = new InventoryPestKiller();
		mTank = new BTF_FluidTank(2000);
	}

	public InventoryPestKiller getInventory() {
		return this.mInventory;
	}

	public FluidTank getTank() {
		return mTank;
	}

	private final void setup() {
		World w = this.worldObj;
		if (w != null) {
			Chunk c = w.getChunkFromBlockCoords(this.xCoord, this.zCoord);
			if (c != null) {
				mChunkX = c.xPosition;
				mChunkZ = c.zPosition;
				mSet = true;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean tryKillPests() {
		int min = 0;
		int max = 0;
		switch (getTier()) {
		case 1:
			min = -2;
			max = 3;
			break;
		case 2:
			min = -4;
			max = 5;
			break;
		default:
			// code block
		}
		int aChunkCount = 0;
		AutoMap<Entity> entities = new AutoMap<Entity>();
		if (min != 0 && max != 0) {
			for (int x = min; x < max; x++) {
				for (int z = min; z < max; z++) {
					Chunk c = getChunkFromOffsetIfLoaded(x, z);
					if (c != null) {
						if (c.hasEntities) {
							aChunkCount++;
							List[] lists = c.entityLists;
							for (List o : lists) {
								for (Object e : o) {
									if (e instanceof Entity) {
										for (Class<?> C : mEntityMap) {
											if (e.getClass().equals(C) || C.isAssignableFrom(e.getClass())) {
												entities.put((Entity) e);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			Chunk c = getChunkFromOffsetIfLoaded(0, 0);
			if (c != null) {
				if (c.hasEntities) {
					List[] lists = c.entityLists;
					for (List o : lists) {
						for (Object e : o) {
							if (e instanceof Entity) {
								for (Class<?> C : mEntityMap) {
									if (e.getClass().equals(C) || C.isAssignableFrom(e.getClass())) {
										entities.put((Entity) e);
									}
								}
							}
						}
					}
				}
			}
		}
		boolean killed = false;
		if (!entities.isEmpty()) {
			for (Entity e : entities) {
				if (e != null) {
					if (e.isEntityAlive()) {
						if (this.mTank.getFluidAmount() >= 1 || getTier() == 0) {
							if (getTier() > 0) {
								int aChanceToUse = MathUtils.randInt(1, (100 * getTier()));
								if (aChanceToUse == 1) {
									this.mTank.drain(1, true);
								}
							}
							e.performHurtAnimation();
							EntityUtils.doDamage(e, DamageSource.generic, Short.MAX_VALUE);
							e.setDead();
							killed = true;
						}
					}
				}
			}
		}
		updateTileEntity();
		return killed;
	}

	public Chunk getChunkFromOffsetIfLoaded(int x, int y) {
		Chunk c = this.worldObj.getChunkFromChunkCoords(mChunkX + x, mChunkZ + y);
		if (c.isChunkLoaded) {
			return c;
		}
		return null;
	}

	public int getTier() {
		if (this.mTank != null) {
			FluidStack f = mTank.getFluid();
			if (f != null) {
				if (f.isFluidEqual(FluidUtils.getWildcardFluidStack("formaldehyde", 1))) {
					return 1;
				} else if (f.isFluidEqual(MISC_MATERIALS.HYDROGEN_CYANIDE.getFluid(1))) {
					return 2;
				}
			}
		}
		return 0;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}
		if (!mSet) {
			setup();
		}
		this.mTickCounter++;
		if (this.mTank != null) {
			if (this.hasFluidSpace()) {
				handleInventory();
			}
		}
		if (this.mTickCounter % this.mBaseTickRate == 0) {
			tryKillPests();
		}
		updateTick();
	}

	public boolean anyPlayerInRange() {
		return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
	}

	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
		if (!nbt.hasKey(tag)) {
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		mTank.writeToNBT(nbt);
		super.writeToNBT(nbt);
		// Utils.LOG_MACHINE_INFO("Trying to write NBT data to TE.");
		final NBTTagCompound chestData = new NBTTagCompound();
		this.mInventory.writeToNBT(chestData);
		nbt.setTag("ContentsChest", chestData);
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		mTank.readFromNBT(nbt);
		super.readFromNBT(nbt);
		// Utils.LOG_MACHINE_INFO("Trying to read NBT data from TE.");
		this.mInventory.readFromNBT(nbt.getCompoundTag("ContentsChest"));
		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}
	}

	@Override
	public int getSizeInventory() {
		return this.getInventory().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.getInventory().getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int count) {
		return this.getInventory().decrStackSize(slot, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		return this.getInventory().getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		this.getInventory().setInventorySlotContents(slot, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return this.getInventory().getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
		return this.getInventory().isUseableByPlayer(entityplayer);
	}

	@Override
	public void openInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.getInventory().openInventory();
	}

	@Override
	public void closeInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.getInventory().closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return this.getInventory().isItemValidForSlot(slot, itemstack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		final int[] accessibleSides = new int[this.getSizeInventory()];
		for (int r = 0; r < this.getInventory().getSizeInventory(); r++) {
			accessibleSides[r] = r;
		}
		return accessibleSides;

	}

	@Override
	public boolean canInsertItem(final int aSlot, final ItemStack aStack, final int p_102007_3_) {
		if (this.getInventory().getInventory()[0] == null) {
			return true;
		} else if (GT_Utility.areStacksEqual(aStack, this.getInventory().getInventory()[0])) {
			if (this.getInventory().getInventory()[0].stackSize < 64) {
				int diff = 64 - this.getInventory().getInventory()[0].stackSize;
				if (aStack.stackSize <= diff) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(final int aSlot, final ItemStack aStack, final int p_102008_3_) {
		if (this.getInventory().getInventory()[1] == null) {
			return false;
		} else {
			return true;
		}
	}

	public String getCustomName() {
		return this.mCustomName;
	}

	public void setCustomName(final String customName) {
		this.mCustomName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.mCustomName : "container.pestkiller";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return (this.mCustomName != null) && !this.mCustomName.equals("");
	}

	@Override
	public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		updateTileEntity();
		return this.mTank.fill(resource, doFill);
	}

	@Override
	public final FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		updateTileEntity();
		return this.mTank.drain(resource.amount, doDrain);
	}

	@Override
	public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack fluid = this.mTank.getFluid();
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
						this.yCoord, this.zCoord, this.mTank, 0));
			}
		}
		updateTileEntity();
		return stack;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return mTank.getFluid() == null || mTank.getFluid().getFluid().equals(fluid);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public final FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.mTank.getInfo() };
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
	
	
	public boolean hasFluidSpace() {
		if (this.mTank.getFluidAmount() <= 1000) {
			return true;
		}
		return false;
	}
	
	public boolean drainCell() {
		boolean didFill = false;
		ItemStack aInput = this.getStackInSlot(0);
		if (aInput == null) {
			return false;
		}
		aInput = aInput.copy();
		if (aInput != null && (this.getStackInSlot(1) == null || this.getStackInSlot(1).stackSize < 64)) {			
			ArrayList<ItemStack> t1Cells = OreDictionary.getOres("cellFormaldehyde");
			ArrayList<ItemStack> t2Cells = OreDictionary.getOres("cellHydrogenCyanide");
			didFill = addFluid(t1Cells, aInput, FluidUtils.getWildcardFluidStack("formaldehyde", 1000));
			if (!didFill) {
				didFill = addFluid(t2Cells, aInput, MISC_MATERIALS.HYDROGEN_CYANIDE.getFluid(1000));
			}
		}
	
		return didFill;
	}
	
	public boolean handleInventory() {
		if (this.getInventory() != null && drainCell()) {
			this.decrStackSize(0, 1);
			if (this.getStackInSlot(1) == null) {
				this.setInventorySlotContents(1, CI.emptyCells(1));
			} else {
				this.getStackInSlot(1).stackSize++;
			}
			this.updateTileEntity();
			return true;
		} else {
			return false;			
		}
	}
	
	public boolean addFluid(ArrayList<ItemStack> inputs, ItemStack aInput, FluidStack aFluidForInput) {
		for (ItemStack a : inputs) {
			if (GT_Utility.areStacksEqual(a, aInput)) {
				if (mTank.getFluid() == null || mTank.getFluid()
						.isFluidEqual(aFluidForInput)) {
					boolean didFill = fill(ForgeDirection.UNKNOWN, aFluidForInput, true) > 0;
					return didFill;
				}
			} else {
				continue;
			}
		}
		return false;
	}
	
	public void updateTileEntity() {
		this.getInventory().markDirty();
		this.markDirty();
		this.mNeedsUpdate = true;
	}
	
	private final void updateTick() {
		if (mNeedsUpdate) {
			if (mUpdateTick == 0) {
				mUpdateTick = 4; // every 4 ticks it will send an update
			} else {
				--mUpdateTick;
				if (mUpdateTick == 0) {
					markDirty();
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					mNeedsUpdate = false;
				}
			}
		}
	}
	
	

}
