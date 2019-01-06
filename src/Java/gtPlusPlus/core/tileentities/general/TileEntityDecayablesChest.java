package gtPlusPlus.core.tileentities.general;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.inventories.Inventory_DecayablesChest;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityDecayablesChest extends TileEntity implements ISidedInventory {

	private final Inventory_DecayablesChest inventoryContents;

	/** Determines if the check for adjacent chests has taken place. */
	public boolean adjacentChestChecked;
	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityDecayablesChest adjacentChestZNeg;
	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityDecayablesChest adjacentChestXPos;
	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityDecayablesChest adjacentChestXNeg;
	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityDecayablesChest adjacentChestZPos;
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;
	/** The number of players currently using this chest */
	public int numPlayersUsing;
	private int cachedChestType;

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

		// Try do chesty stuff
		try {
			this.updateEntityChest();
		} catch (Throwable t) {

		}		

		try {
			if (!this.worldObj.isRemote) {
				this.tickCount++;
				if ((this.tickCount % 10) == 0) {
					cachedChestType = 1;
				}

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
				updateSlots();
			}
		} catch (final Throwable t) {
		}
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
		Logger.INFO("| "+b.getUnlocalizedName()+" | "+a1+"/"+a2);

		if (!a1 && !a2) {
			ItemStack replacement = ItemUtils.getSimpleStack(b.getDecayResult());
			replacement.stackSize = 1;
			//iStack = replacement.copy();
			for (int fff = 0; fff < this.inventoryContents.getSizeInventory(); fff++) {
				if (this.inventoryContents.getStackInSlot(fff) == iStack) {
					this.inventoryContents.setInventorySlotContents(fff, replacement.copy());
				}
			}			

			updateSlots();
			this.inventoryContents.
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
		if (this.numPlayersUsing < 0) {
			this.numPlayersUsing = 0;
		}
		if (!this.worldObj.isRemote) {
			this.numPlayersUsing++;
			cachedChestType = 1;
		}
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.getInventory().openInventory();
	}

	@Override
	public void closeInventory() {		
		if (!this.worldObj.isRemote) {
			this.numPlayersUsing--;
			cachedChestType = 1;
		}
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
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
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return this.getInventory().isItemValidForSlot(0, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return this.getInventory().isItemValidForSlot(0, p_102008_2_);
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

	/**
	 * Causes the TileEntity to reset all it's cached values for it's container
	 * Block, metadata and in the case of chests, the adjacent chest check
	 */
	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		this.adjacentChestChecked = false;
	}

	/**
	 * Performs the check for adjacent chests to determine if this chest is double
	 * or not.
	 */
	public void checkForAdjacentChests() {
		if (!this.adjacentChestChecked) {
			this.adjacentChestChecked = true;
			this.adjacentChestZNeg = null;
			this.adjacentChestXPos = null;
			this.adjacentChestXNeg = null;
			this.adjacentChestZPos = null;
		}
	}

	public void updateEntityChest() {
		float f;
		this.prevLidAngle = this.lidAngle;
		f = 0.04F;
		double d2;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null
				&& this.adjacentChestXNeg == null) {
			double d1 = (double) this.xCoord + 0.5D;
			d2 = (double) this.zCoord + 0.5D;
			this.worldObj.playSoundEffect(d1, (double) this.yCoord + 0.5D, d2, "random.chestopen", 0.5F,
					this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f1 = this.lidAngle;
			if (this.numPlayersUsing > 0) {
				//this.lidAngle += f;
				this.lidAngle += (float) (f * (1 + 0.10 * 0.01));
			} else {
				//this.lidAngle -= f;
				this.lidAngle -= (float) (f * (1 + 0.10 * 0.01));
			}
			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}
			float f2 = 0.5F;
			if (this.lidAngle < f2 && f1 >= f2 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
				d2 = (double) this.xCoord + 0.5D;
				double d0 = (double) this.zCoord + 0.5D;
				this.worldObj.playSoundEffect(d2, (double) this.yCoord + 0.5D, d0, "random.chestclosed", 0.5F,
						this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}

	/**
	 * Called when a client event is received with the event number and argument,
	 * see World.sendClientEvent
	 */
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		if (p_145842_1_ == 1)
		{
			this.numPlayersUsing = p_145842_2_;
			return true;
		}
		else
		{
			return super.receiveClientEvent(p_145842_1_, p_145842_2_);
		}	
	}

	/**
	 * invalidates a tile entity
	 */
	public final void invalidate() {
		super.invalidate();
		cachedChestType = 1;
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
	}

	private final int updateSlots() {		
		//Have slots changed?
		if (cachedChestType == 0) {
			return 0;
		}
		ItemUtils.organiseInventory(getInventory());
		cachedChestType = 0;		
		return cachedChestType;
		
/*		//Try merge stacks
		for (int i = 0; i < this.getSizeInventory(); i++) {
			for (int i2 = 0; i2 < this.getSizeInventory(); i2++) {
				if (i != i2) {
					ItemStack[] t1 = new ItemStack[] {this.getStackInSlot(i), this.getStackInSlot(i2)};
					if (t1[0] == null || t1[1] == null) {
						continue;
					}
					else if (!GT_Utility.areStacksEqual(t1[0], t1[1])) {
						continue;
					}
					//Try Merge
					else {						
						
						if (GT_Utility.areStacksEqual(t1[0], t1[1])) {						
						while ((t1[0].stackSize < 64 && t1[1].stackSize > 0)) {							
							t1[0].stackSize++;
							t1[1].stackSize--;
							if (t1[1].stackSize <= 0) {
								t1[1] = null;
								break;
							}
							if (t1[0].stackSize == 64) {
								break;
							}							
						}
						this.setInventorySlotContents(i,  t1[1]);
						this.setInventorySlotContents(i2,  t1[0]);	
						
						}
					}
				}
			}
		}
		
		//Move nulls to end
		int count2 = 0;
		for (int i = 0; i < this.getSizeInventory(); i++) 
			if (this.getStackInSlot(i) != null) 
				this.setInventorySlotContents(count2++,  this.getStackInSlot(i));
		while (count2 < this.getSizeInventory()) 
			this.setInventorySlotContents(count2++,  null);

		//Sort by name
		int arraySlot = 0;
		HashMap<Integer, Pair<String, ItemStack>> aNameMap = new HashMap<Integer, Pair<String, ItemStack>>();
		
		for (ItemStack ggg : this.inventoryContents.getInventory()) {
			aNameMap.put(arraySlot++, new Pair<String, ItemStack>(ggg != null ? ggg.getDisplayName() : "", ggg));
		}	
		arraySlot = 0;
		String[] aNameMapInternal = new String[aNameMap.size()];
		for (Pair pp : aNameMap.values()) {			
			aNameMapInternal[arraySlot++] = pp.getKey().toString();			
		}
		Arrays.sort(aNameMapInternal);	*/	

	}

}
