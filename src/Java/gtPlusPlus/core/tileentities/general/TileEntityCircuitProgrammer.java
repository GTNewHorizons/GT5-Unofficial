package gtPlusPlus.core.tileentities.general;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.inventories.InventoryCircuitProgrammer;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.slots.SlotIntegratedCircuit;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class TileEntityCircuitProgrammer extends TileEntity implements ISidedInventory {

	private int tickCount = 0;
	private final InventoryCircuitProgrammer inventoryContents;
	private String customName;
	public int locationX;
	public int locationY;
	public int locationZ;
	private int aCurrentMode = 0;

	public TileEntityCircuitProgrammer() {
		this.inventoryContents = new InventoryCircuitProgrammer();
		this.setTileLocation();
	}

	public boolean setTileLocation() {
		if (this.hasWorldObj()) {
			if (!this.getWorldObj().isRemote) {
				this.locationX = this.xCoord;
				this.locationY = this.yCoord;
				this.locationZ = this.zCoord;
				return true;
			}
		}
		return false;
	}

	//Rename to hasCircuitToConfigure
	public final boolean hasCircuitToConfigure() {
		for (ItemStack i : this.getInventory().getInventory()) {
			if (i == null) {
				continue;
			}
			else {
				return true;
			}
		}		
		return false;
	}

	public InventoryCircuitProgrammer getInventory() {
		return this.inventoryContents;
	}

	public boolean addOutput() {		
		ItemStack[] aInputs = this.getInventory().getInventory().clone();
		//Check if there is output in slot.
		Boolean hasOutput = false;
		if (aInputs[25] != null) {
			hasOutput = true;
		}		
		AutoMap<Integer> aValidSlots = new AutoMap<Integer>();
		int aSlotCount = 0;
		for (ItemStack i : aInputs) {
			if (i != null) {
				aValidSlots.put(aSlotCount);
			}
			aSlotCount++;
		}
		for (int e : aValidSlots) {
			boolean doAdd = false;
			ItemStack g = this.getStackInSlot(e);
			int aSize = 0;
			ItemStack aInputStack = null;	
			int aTypeInSlot = SlotIntegratedCircuit.isRegularProgrammableCircuit(g);
			if (aTypeInSlot >= 0 && g != null) {
				// No Existing Output
				if (!hasOutput) {
					aSize = g.stackSize;
					doAdd = true;
				}
				// Existing Output
				else {
					ItemStack f = this.getStackInSlot(25);
					int aTypeInCheckedSlot = SlotIntegratedCircuit.isRegularProgrammableCircuit(f);					
					// Check that the Circuit in the Output slot is not null and the same type as the circuit input.
					if (aTypeInCheckedSlot >= 0 && (aTypeInSlot == aTypeInCheckedSlot) && f != null) {
						if (g.getItem() == f.getItem() && f.getItemDamage() == e) {
							aSize = f.stackSize + g.stackSize;							
							if (aSize > 64) {
								aInputStack = g.copy();
								aInputStack.stackSize = (aSize-64);
							}
							doAdd = true;							
						}
					}					
				}				
				if (doAdd) {	
					// Check Circuit Type					
					ItemStack aOutput;
					if (aTypeInSlot == 0) {
						aOutput = CI.getNumberedCircuit(e);
					}
					else if (aTypeInSlot == 1) {
						aOutput = CI.getNumberedBioCircuit(e);
					}
					else {
						aOutput = null;
					}			
					
					if (aOutput != null) {
						aOutput.stackSize = aSize;
						this.setInventorySlotContents(e, aInputStack);
						this.setInventorySlotContents(25, aOutput);
						return true;
					}
				}
			}
			continue;
		}		
		return false;
	}

	@Override
	public void updateEntity() {
		try{
			if (!this.worldObj.isRemote) {
				if (tickCount % 10 == 0) {
					if (hasCircuitToConfigure()) {
						this.addOutput();
						this.markDirty();
					}	
				}
				this.tickCount++;				
			}			
		}
		catch (final Throwable t){}
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
		nbt.setInteger("aCurrentMode", aCurrentMode);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// Utils.LOG_WARNING("Trying to read NBT data from TE.");
		this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}
		aCurrentMode = nbt.getInteger("aCurrentMode");
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
		return p_102007_1_ >= 0 && p_102007_1_ <= 24;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return p_102008_1_ == 25;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(final String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.circuitprogrammer";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return (this.customName != null) && !this.customName.equals("");
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final NBTTagCompound tag = pkt.func_148857_g();
		this.readFromNBT(tag);
	}

	public boolean onScrewdriverRightClick(byte side, EntityPlayer player, int x, int y, int z) {
		try {			
			if (aCurrentMode == 24) {
				aCurrentMode = 0;
			}
			else {
				aCurrentMode++;
			}
			PlayerUtils.messagePlayer(player, "Now configuring units for type "+aCurrentMode+".");			
			return true;
		}
		catch (Throwable t) {
			return false;
		}		
	}

}
