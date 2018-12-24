package gtPlusPlus.core.tileentities.general;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.inventories.Inventory_DecayablesChest;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class TileEntityDecayablesChest extends TileEntity implements ISidedInventory {

	
	private final Inventory_DecayablesChest inventoryContents;
	private String customName;
	
	private int tickCount = 0;

	public TileEntityDecayablesChest() {
		this.inventoryContents = new Inventory_DecayablesChest();
	}

	public Inventory_DecayablesChest getInventory() {
		return this.inventoryContents;
	}

	@Override
	public void updateEntity() {
		try{
			if (!this.worldObj.isRemote) {
				
				this.tickCount++;
				
				if ((this.tickCount % 20) == 0) {					
					for (ItemStack inv : this.getInventory().getInventory()) {
						if (inv == null) {
							continue;
						}
						if (inv.getItem() instanceof DustDecayable) {
							DustDecayable D = (DustDecayable) inv.getItem();
							tryUpdateDecayable(D, inv, this.worldObj);
						}
					}
				}

			}
		}
		catch (final Throwable t){}
	}
	
	
	public void tryUpdateDecayable(final DustDecayable b, ItemStack iStack, final World world) {
		if (world == null || iStack == null) {
			return;
		}	
		if (world.isRemote) {
			return;
		}
		
		boolean a1, a2;		
		int u = 0;
		a1 = b.getIsActive(world, iStack);
		a2 = b.tickItemTag(world, iStack);
		while (u < 19) {
			if (!a1) {
				break;
			}
			a1 = b.getIsActive(world, iStack);
			a2 = b.tickItemTag(world, iStack);
			u++;
		}		
		
		if (!a1 && !a2) {	
				ItemStack replacement = ItemUtils.getSimpleStack(b.getDecayResult());
				replacement.stackSize=1;
				iStack = replacement.copy();
				markDirty();
		}
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
		super.writeToNBT(nbt);
		// Utils.LOG_WARNING("Trying to write NBT data to TE.");
		final NBTTagCompound chestData = new NBTTagCompound();
		this.inventoryContents.writeToNBT(chestData);
		nbt.setTag("ContentsChest", chestData);
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// Utils.LOG_WARNING("Trying to read NBT data from TE.");
		this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
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
		for (int r=0; r<this.getInventory().getSizeInventory(); r++){
			accessibleSides[r]=r;
		}
		return accessibleSides;

	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return true;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return true;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(final String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.DecayablesChest";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return (this.customName != null) && !this.customName.equals("");
	}

}
