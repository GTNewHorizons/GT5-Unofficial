package gtPlusPlus.core.tileentities.machines;

import java.util.List;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.inventories.Inventory_RoundRobinator;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityRoundRobinator extends TileEntity implements ISidedInventory, IHopper {

	private int tickCount = 0;
	private final Inventory_RoundRobinator inventoryContents;
	private String customName;
	public int locationX;
	public int locationY;
	public int locationZ;
	private int aData = 1111;
	private int aTier = 1;
	private int aTickRate = 100;

	public TileEntityRoundRobinator() {
		this.inventoryContents = new Inventory_RoundRobinator();
		this.setTileLocation();
	}

	public boolean setTileLocation() {
		if (this.hasWorldObj()) {
			if (!this.getWorldObj().isRemote) {
				this.locationX = this.xCoord;
				this.locationY = this.yCoord;
				this.locationZ = this.zCoord;
				this.aTier = this.getWorldObj().getBlockMetadata(locationX, locationY, locationZ) + 1;
				return true;
			}
		}
		return false;
	}

	//Rename to hasCircuitToConfigure
	public final boolean hasInventoryContents() {
		for (ItemStack i : this.aHopperInventory) {
			if (i == null) {
				continue;
			}
			else {
				return true;
			}
		}		
		return false;
	}

	public Inventory_RoundRobinator getInventory() {
		return this.inventoryContents;
	}
	
	public int getTier() {
		return this.aTier;
	}
	
	public int getTickRate() {
		return this.aTickRate;
	}

	@Override
	public void updateEntity() {
		try{
			// TODO
			if (this.worldObj != null && !this.worldObj.isRemote){	
				setTileLocation();
				aTickRate = (60-(aTier*10));
				if (this.getTier() == 1) {
					// 20 s
					aTickRate = 400;
				}
				else if (this.getTier() == 2) {
					// 5
					aTickRate = 100;
				}
				else if (this.getTier() == 3) {
					// 1
					aTickRate = 20;
				}
				else if (this.getTier() == 4) {
					// 1/5
					aTickRate = 10;
				}
				else if (this.getTier() == 5) {
					// 1/20
					aTickRate = 1;
				}
				else {
					aTickRate = 999999;
				}				
				
				if (tickCount % getTickRate() == 0) {
					if (hasInventoryContents()) {
						Logger.WARNING("Trying to move items. "+aTickRate);
						this.tryProcessItems();							
					}	
				}
				this.tickCount++;
			}
		}
		catch (final Throwable t){
			t.printStackTrace();
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
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
		nbt.setInteger("aCurrentMode", aData);
		this.writeToNBT2(nbt);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}
		aData = nbt.getInteger("aCurrentMode");
		this.readFromNBT2(nbt);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
		//this.getInventory().openInventory();
	}

	@Override
	public void closeInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		//this.getInventory().closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int aSide) {
			return new int[] {0, 1, 2, 3, 4};		
	}

	@Override
	public boolean canInsertItem(final int aSlot, final ItemStack aStack, final int aSide) {
		return aSide < 2;
	}

	@Override
	public boolean canExtractItem(final int aSlot, final ItemStack aStack, final int aSide) {
		return false;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(final String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.roundrobinator";
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


	public boolean onRightClick(byte side, EntityPlayer player, int x, int y, int z) {
		if (player != null && player.getHeldItem() == null) {
			if (!player.isSneaking() && !KeyboardUtils.isShiftKeyDown()) {
				player.openGui(GTplusplus.instance, GuiHandler.GUI16, player.getEntityWorld(), x, y, z);
			}
			else {
				String InventoryContents = ItemUtils.getArrayStackNames(this.aHopperInventory);
				PlayerUtils.messagePlayer(player, "Contents: "+InventoryContents+" | "+getDataString());				
			}			
			return true;
		}
		else {
			return false;			
		}	
	}

	public boolean onScrewdriverRightClick(byte side, EntityPlayer player, int x, int y, int z) {
		try {		
			if (side < 2) {
				// Top/Bottom
			}
			else {				
				if (toggleSide(side)) {
					PlayerUtils.messagePlayer(player, "Enabling side "+side+".");					
				}
				else {
					PlayerUtils.messagePlayer(player, "Disabling side "+side+".");					
				}
				PlayerUtils.messagePlayer(player, "Mode String: "+aData+"");
			}	
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
			return false;
		}		
	}

	public int getDataString() {
		return aData;
	}

	public boolean[] getActiveSides() {
		this.markDirty();
		String s = String.valueOf(aData);
		if (s == null || s.length() != 4) {
			s = "1111";
		}
		boolean[] aActiveSides = new boolean[4];
		for (int i=0;i<4;i++) {
			char ch = s.charAt(i);
			if (ch == '1') {
				aActiveSides[i] = true;
			}
			else {
				aActiveSides[i] = false;
			}
		}		
		return aActiveSides;
	}

	/**
	 * Toggle active state of side
	 * @param aSide - Forge Direction / Side
	 * @return - True if the side is now Active, false if now disabled.
	 */
	public boolean toggleSide(int aSide) {
		setSideActive(!getSideActive(aSide), aSide);
		return getSideActive(aSide);
	}


	public void setSideActive(boolean aActive, int aSide) {
		try {		
			if (aSide < 2) {
			}
			else {
				if (aData < 1111) {
					aData = 1111;
				}
				else if (aData > 2222) {
					aData = 2222;
				}				
				String s = String.valueOf(aData);
				StringBuilder aDataString = new StringBuilder(s);
				int aIndex = aSide - 2;
				if (aActive) {
					aDataString.setCharAt(aIndex, '1');
				}
				else {
					aDataString.setCharAt(aIndex, '2');					
				}
				aData = Integer.valueOf(aDataString.toString());
				this.markDirty();
			}	
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public boolean getSideActive(int aSide) {
		this.markDirty();
		try {		
			if (aSide < 2) {
				return false;
			}
			else {
				if (aData < 1111) {
					aData = 1111;
				}
				else if (aData > 2222) {
					aData = 2222;
				}				
				String s = String.valueOf(aData);
				int aIndex = aSide - 2;
				char ch = s.charAt(aIndex);
				if (ch == '1') {
					return true;
				}
				else {
					return false;				
				}

			}	
		}
		catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public double getXPos() {
		return this.locationX;
	}

	@Override
	public double getYPos() {
		return this.locationY;
	}

	@Override
	public double getZPos() {
		return this.locationZ;
	}








	// TODO



	/*
	 * Hopper Code
	 */


	private ItemStack[] aHopperInventory = new ItemStack[5];

	public int getSizeInventory() {
		return this.aHopperInventory.length;
	}

	public ItemStack getStackInSlot(int aSlot) {
		return this.aHopperInventory[aSlot];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	public ItemStack decrStackSize(int aSlot, int aMinimumSizeOfExistingStack)
	{
		if (this.aHopperInventory[aSlot] != null)
		{
			ItemStack itemstack;

			if (this.aHopperInventory[aSlot].stackSize <= aMinimumSizeOfExistingStack)
			{
				itemstack = this.aHopperInventory[aSlot];
				this.aHopperInventory[aSlot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.aHopperInventory[aSlot].splitStack(aMinimumSizeOfExistingStack);

				if (this.aHopperInventory[aSlot].stackSize == 0)
				{
					this.aHopperInventory[aSlot] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int aSlot)
	{
		if (this.aHopperInventory[aSlot] != null)
		{
			ItemStack itemstack = this.aHopperInventory[aSlot];
			this.aHopperInventory[aSlot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int aSlot, ItemStack aStack)
	{
		this.aHopperInventory[aSlot] = aStack;

		if (aStack != null && aStack.stackSize > this.getInventoryStackLimit())
		{
			aStack.stackSize = this.getInventoryStackLimit();
		}
	}

	public boolean tryProcessItems() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			boolean didSomething = false;
			if (!this.isEmpty()) {
				Logger.WARNING("Has Items, Trying to push to all active directions.");
				didSomething = this.tryPushItemsIntoNeighbours();
			}			
			if (didSomething) {
				this.markDirty();
				return true;
			}
		}
		return false;
	}

	/**
	 * Is Empty
	 * @return
	 */
	private boolean isEmpty() {
		ItemStack[] aitemstack = this.aHopperInventory;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack != null) {
				return false;
			}
		}

		return true;
	}

	private boolean tryPushItemsIntoNeighbours() {

		boolean aDidPush = false;

		for (int u = 2; u < 6; u++) {
			if (!this.getSideActive(u)) {
				Logger.WARNING("Not pushing on side "+u);
				continue;
			}			
			
			Logger.WARNING("Pushing on side "+u);
			IInventory iinventory = this.getInventoryFromFacing(u);

			if (iinventory == null) {
				Logger.WARNING("No inventory found.");
				continue;
			}
			else {			

				int i = Facing.oppositeSide[u];
				Logger.WARNING("Using Opposite direction: "+i);

				if (this.isInventoryFull(iinventory, i)) {
					Logger.WARNING("Target is full, skipping.");
					continue;
				}
				else {
					Logger.WARNING("Target has space, let's move a single item.");
					for (int j = 0; j < this.getSizeInventory(); ++j) {
						if (this.getStackInSlot(j) != null) {
							ItemStack itemstack = this.getStackInSlot(j).copy();
							ItemStack itemstack1 = setStackInNeighbour(iinventory, this.decrStackSize(j, 1), i);
							if (itemstack1 == null || itemstack1.stackSize == 0) {
								iinventory.markDirty();
								aDidPush = true;
								continue;
							}
							this.setInventorySlotContents(j, itemstack);							
						}
					}					
				}
			}
		}

		return aDidPush;
	}

	private boolean isInventoryFull(IInventory aInv, int aSide) {
		if (aInv instanceof ISidedInventory && aSide > -1) {
			ISidedInventory isidedinventory = (ISidedInventory)aInv;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(aSide);

			for (int l = 0; l < aint.length; ++l)
			{
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[l]);

				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
				{
					return false;
				}
			}
		}
		else {
			int j = aInv.getSizeInventory();

			for (int k = 0; k < j; ++k)
			{
				ItemStack itemstack = aInv.getStackInSlot(k);

				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
				{
					return false;
				}
			}
		}
		return true;
	}

	public static ItemStack setStackInNeighbour(IInventory aNeighbour, ItemStack aStack, int aSide) {
		if (aNeighbour instanceof ISidedInventory && aSide > -1)
		{
			ISidedInventory isidedinventory = (ISidedInventory)aNeighbour;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(aSide);

			for (int l = 0; l < aint.length && aStack != null && aStack.stackSize > 0; ++l)
			{
				aStack = tryMoveStack(aNeighbour, aStack, aint[l], aSide);
			}
		}
		else
		{
			int j = aNeighbour.getSizeInventory();

			for (int k = 0; k < j && aStack != null && aStack.stackSize > 0; ++k)
			{
				aStack = tryMoveStack(aNeighbour, aStack, k, aSide);
			}
		}

		if (aStack != null && aStack.stackSize == 0)
		{
			aStack = null;
		}

		return aStack;
	}

	private static boolean canInsertItemIntoNeighbour(IInventory aNeighbour, ItemStack aStack, int aSlot, int aSide) {
		return !aNeighbour.isItemValidForSlot(aSlot, aStack) ? false : !(aNeighbour instanceof ISidedInventory) || ((ISidedInventory)aNeighbour).canInsertItem(aSlot, aStack, aSide);
	}

	private static ItemStack tryMoveStack(IInventory aNeighbour, ItemStack aStack, int aSlot, int aSide) {
		ItemStack itemstack1 = aNeighbour.getStackInSlot(aSlot);
		if (canInsertItemIntoNeighbour(aNeighbour, aStack, aSlot, aSide)) {
			boolean aDidSomething = false;
			if (itemstack1 == null) {
				//Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(aStack.getMaxStackSize(), aNeighbour.getInventoryStackLimit());
				if (max >= aStack.stackSize) {
					aNeighbour.setInventorySlotContents(aSlot, aStack);
					aStack = null;
				}
				else {
					aNeighbour.setInventorySlotContents(aSlot, aStack.splitStack(max));
				}
				aDidSomething = true;
			}
			else if (areItemStacksEqual(itemstack1, aStack)) {
				//Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(aStack.getMaxStackSize(), aNeighbour.getInventoryStackLimit());
				if (max > itemstack1.stackSize)	{
					int l = Math.min(aStack.stackSize, max - itemstack1.stackSize);
					aStack.stackSize -= l;
					itemstack1.stackSize += l;
					aDidSomething = l > 0;
				}
			}
			if (aDidSomething){
				aNeighbour.markDirty();
			}
		}
		return aStack;
	}

	private IInventory getInventoryFromFacing(int aSide)
	{
		int i = aSide;
		return tryFindInvetoryAtXYZ(this.getWorldObj(), (double)(this.xCoord + Facing.offsetsXForSide[i]), (double)(this.yCoord + Facing.offsetsYForSide[i]), (double)(this.zCoord + Facing.offsetsZForSide[i]));
	}

	public static IInventory tryFindInvetoryAtXYZ(World aWorld, double aX, double aY, double aZ)
	{
		IInventory iinventory = null;
		int sX = MathHelper.floor_double(aX);
		int sY = MathHelper.floor_double(aY);
		int sZ = MathHelper.floor_double(aZ);
		TileEntity tileentity = aWorld.getTileEntity(sX, sY, sZ);

		if (tileentity != null && tileentity instanceof IInventory)
		{
			iinventory = (IInventory)tileentity;

			if (iinventory instanceof TileEntityChest)
			{
				Block block = aWorld.getBlock(sX, sY, sZ);

				if (block instanceof BlockChest)
				{
					iinventory = ((BlockChest)block).func_149951_m(aWorld, sX, sY, sZ);
				}
			}
		}

		if (iinventory == null)
		{
			List list = aWorld.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1.0D, aY + 1.0D, aZ + 1.0D), IEntitySelector.selectInventories);

			if (list != null && list.size() > 0)
			{
				iinventory = (IInventory)list.get(aWorld.rand.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	private static boolean areItemStacksEqual(ItemStack aStack, ItemStack aStack2) {
		return aStack.getItem() != aStack2.getItem() ? false : (aStack.getItemDamage() != aStack2.getItemDamage() ? false : (aStack.stackSize > aStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(aStack, aStack2)));
	}

	public void readFromNBT2(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		this.aHopperInventory = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			if (b0 >= 0 && b0 < this.aHopperInventory.length) {
				this.aHopperInventory[b0] = ItemStack.loadItemStackFromNBT(
						nbttagcompound1
				);
			}
		}
	}

	public void writeToNBT2(NBTTagCompound aNBT) {
		super.writeToNBT(aNBT);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.aHopperInventory.length; ++i) {
			if (this.aHopperInventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.aHopperInventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		aNBT.setTag("Items", nbttaglist);
	}







}
