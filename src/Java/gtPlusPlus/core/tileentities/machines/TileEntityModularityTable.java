package gtPlusPlus.core.tileentities.machines;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.ItemList;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.inventories.modulartable.InventoryModularMain;
import gtPlusPlus.core.inventories.modulartable.InventoryModularOutput;
import gtPlusPlus.core.item.bauble.ModularBauble;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils.BT;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils.Modifiers;

public class TileEntityModularityTable extends TileEntityBase implements ISidedInventory{

	public InventoryModularMain inventoryGrid;
	public InventoryModularOutput inventoryOutputs;
	public InventoryModularOutput mTempRecipeStorage;
	private Container_ModularityTable container;
	private String customName;
	private int mRecipeTimeRemaining = -1;

	public TileEntityModularityTable() {
		this.inventoryGrid = new InventoryModularMain();
		this.inventoryOutputs = new InventoryModularOutput();
		this.mTempRecipeStorage = new InventoryModularOutput();
		this.canUpdate();
		generateAllValidUpgrades();
	}

	public void setContainer(Container_ModularityTable container_ModularityTable) {
		this.container = container_ModularityTable;
	}

	public Container_ModularityTable getContainer() {
		return this.container;
	}

	public int getRecipeTime(){
		return this.mRecipeTimeRemaining;
	}

	@Override
	@SuppressWarnings("static-method")
	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
		if (!nbt.hasKey(tag)) {
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("mRecipeTime", this.mRecipeTimeRemaining);
		this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));
		this.inventoryGrid.writeToNBT(this.getTag(nbt, "ContentsGrid"));
		this.mTempRecipeStorage.writeToNBT(this.getTag(nbt, "ContentsRecipeTemp"));
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.mRecipeTimeRemaining = nbt.getInteger("mRecipeTime");
		this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
		this.inventoryGrid.readFromNBT(nbt.getCompoundTag("ContentsGrid"));
		this.mTempRecipeStorage.readFromNBT(nbt.getCompoundTag("ContentsRecipeTemp"));
		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}
	}

	protected ItemStack mOutputStack; //Upgraded Bauble
	protected ItemStack mInputstackA; //Bauble
	protected ItemStack mInputstackB; //Upgrade

	public ItemStack getPendingOutputItem(){
		this.mRecipeTimeRemaining--;
		return this.mOutputStack;
	}

	public ItemStack[] getCurrentInputItems(){
		if (this.mRecipeTimeRemaining < 0){
			return null;
		}
		else {
			return new ItemStack[]{this.mInputstackA, this.mInputstackB};
		}
	}

	public boolean setInputStacks(ItemStack tBauble, ItemStack tUpgrade){
		if (tBauble != null){
			this.mInputstackA = tBauble;
		}
		else {
			this.mInputstackA = null;			
		}
		if (tUpgrade != null){
			this.mInputstackB = tBauble;
		}
		else {
			this.mInputstackB = null;			
		}
		if (this.mInputstackA != null && this.mInputstackB != null){
			return true;
		}
		return false;
	}

	public boolean setOutputStack(ItemStack mNewBauble){
		if (mNewBauble != null){
			this.mOutputStack = mNewBauble;
			return true;
		}
		else {
			this.mOutputStack = null;	
			return false;		
		}
	}

	public boolean clearRecipeData(){
		this.mInputstackA = null;
		this.mInputstackB = null;
		this.mOutputStack = null;
		return true;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	public static Map<ItemStack, Pair<Modifiers, Integer>> mValidUpgradeList = new HashMap<ItemStack, Pair<Modifiers, Integer>>();
	public static Map<ItemStack, BT> mValidUpgradeListFormChange = new HashMap<ItemStack, BT>();

	private static boolean generateAllValidUpgrades() {

		// Form Change
		generateUpgradeFormData(ItemList.Sensor_MV.get(1), BT.TYPE_RING);
		generateUpgradeFormData(ItemList.Electric_Piston_MV.get(1), BT.TYPE_BELT);
		generateUpgradeFormData(ItemList.Emitter_MV.get(1), BT.TYPE_AMULET);

		// Damage Boost
		generateUpgradeData(ItemList.Electric_Motor_LV.get(1), Modifiers.BOOST_DAMAGE, 1);
		generateUpgradeData(ItemList.Electric_Motor_MV.get(1), Modifiers.BOOST_DAMAGE, 2);
		generateUpgradeData(ItemList.Electric_Motor_HV.get(1), Modifiers.BOOST_DAMAGE, 3);
		generateUpgradeData(ItemList.Electric_Motor_EV.get(1), Modifiers.BOOST_DAMAGE, 4);
		generateUpgradeData(ItemList.Electric_Motor_IV.get(1), Modifiers.BOOST_DAMAGE, 5);

		// Defence Boost
		generateUpgradeData(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateAluminium", 1), Modifiers.BOOST_DEF, 1);
		generateUpgradeData(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateStainlessSteel", 1), Modifiers.BOOST_DEF, 2);
		generateUpgradeData(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateTungsten", 1), Modifiers.BOOST_DEF, 3);
		generateUpgradeData(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateTungstenSteel", 1), Modifiers.BOOST_DEF, 4);
		generateUpgradeData(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateNaquadah", 1), Modifiers.BOOST_DEF, 5);

		// Hp Boost
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.golden_apple, 0, 1), Modifiers.BOOST_HP, 1);
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.golden_apple, 1, 1), Modifiers.BOOST_HP, 2);
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.nether_star, 0, 1), Modifiers.BOOST_HP, 3);
		generateUpgradeData(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32725", 32725, 1), Modifiers.BOOST_HP,
				4);
		generateUpgradeData(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32726", 32726, 1), Modifiers.BOOST_HP,
				5);

		return true;
	}

	public static boolean generateUpgradeData(ItemStack tStack, Modifiers tMod, int tLevel) {
		Pair<Modifiers, Integer> tTemp = new Pair<Modifiers, Integer>(tMod, tLevel);
		if (mValidUpgradeList.put(tStack, tTemp) != null) {
			return true;
		}
		return false;
	}

	public static boolean generateUpgradeFormData(ItemStack tStack, BT tMod) {
		if (mValidUpgradeListFormChange.put(tStack, tMod) != null) {
			return true;
		}
		return false;
	}

	public static boolean addUpgrade(ItemStack tStack, ItemStack tBauble) {

		try {
			Iterator<Entry<ItemStack, BT>> it = mValidUpgradeListFormChange.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ItemStack, BT> pair = it.next();
				if (pair.getKey().getItem() == tStack.getItem()
						&& pair.getKey().getItemDamage() == tStack.getItemDamage()) {
					ModularArmourUtils.setBaubleType(tBauble, pair.getValue());
					tBauble.setItemDamage(ModularArmourUtils.getBaubleTypeID(tBauble));
					return true;
				}
			}
		} catch (Throwable t) {

		}
		try {
			Iterator<Entry<ItemStack, Pair<Modifiers, Integer>>> it2 = mValidUpgradeList.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<ItemStack, Pair<Modifiers, Integer>> pair = it2.next();
				if (pair.getKey().getItem() == tStack.getItem()
						&& pair.getKey().getItemDamage() == tStack.getItemDamage()) {
					Pair<Modifiers, Integer> newPair = pair.getValue();
					ModularArmourUtils.setModifierLevel(tBauble, newPair);
					return true;
				}
			}
		} catch (Throwable t) {

		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 11;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= this.inventoryGrid.getSizeInventory()){
			return this.inventoryOutputs.getStackInSlot(slot-9);
		}
		else if (slot < this.inventoryGrid.getSizeInventory()){
			return this.inventoryGrid.getStackInSlot(slot);
		}
		else {
			return null;
		}		
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		if (slot < this.inventoryGrid.getSizeInventory()){
			return this.inventoryGrid.decrStackSize(slot, count);
		}
		else if (slot >= this.inventoryGrid.getSizeInventory()){
			return this.inventoryOutputs.decrStackSize(slot-9, count);
		}
		else {
			return null;
		}	
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return this.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot >= this.inventoryGrid.getSizeInventory()){
			this.inventoryOutputs.setInventorySlotContents(slot-9, stack);
		}
		else if (slot < this.inventoryGrid.getSizeInventory()){
			this.inventoryGrid.setInventorySlotContents(slot, stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.inventoryGrid.openInventory();		
		this.inventoryOutputs.openInventory();	
	}

	@Override
	public void closeInventory() {
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		this.inventoryGrid.openInventory();		
		this.inventoryOutputs.openInventory();	
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if (slot >= this.inventoryGrid.getSizeInventory()){
			return this.inventoryOutputs.isItemValidForSlot(slot-9, itemstack);
		}
		else if (slot < this.inventoryGrid.getSizeInventory()){
			return this.inventoryGrid.isItemValidForSlot(slot, itemstack);
		}
		else {
			return false;
		}	
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] accessibleSides = new int[this.getSizeInventory()];	
		for (int r=0; r<this.getSizeInventory(); r++){
			accessibleSides[r]=r;
		}
		return accessibleSides;

	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		Logger.INFO("Slot:"+slot+" | side? "+side);

		/*if (side == 1){
			return this.inventoryOutputs.isItemValidForSlot(slot-9, item);
		}	*/	
		if (slot >= 9){
			return this.inventoryOutputs.isItemValidForSlot(slot-9, item);
		}
		else {
			return this.inventoryGrid.isItemValidForSlot(slot, item);
		}
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		Logger.INFO("Slot:"+slot+" | side? "+side);
		if (slot == 11 || slot <= 8){
			return true;
		}
		return false;
	}

	@Override
	public String getCustomName() {
		return this.customName;
	}

	@Override
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.fishrap";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && !this.customName.equals("");
	}

	@Override
	public boolean onPreTick() {
		//Check for active recipe
		if (this.mRecipeTimeRemaining > -1 || (this.mTempRecipeStorage != null) && (this.mTempRecipeStorage.getRecipeTime() > -1)){
			if ((this.mTempRecipeStorage != null) && this.mTempRecipeStorage.getRecipeTime() > -1){
				if (this.mRecipeTimeRemaining < this.mTempRecipeStorage.getRecipeTime()){
					this.mRecipeTimeRemaining = this.mTempRecipeStorage.getRecipeTime();
					this.markDirty();
				}
			}
			if (this.mInputstackA != null && this.mInputstackB != null && this.mOutputStack != null){
				this.mTempRecipeStorage.setInventorySlotContents(0, this.mInputstackA);
				this.mTempRecipeStorage.setInventorySlotContents(1, this.mInputstackB);
				this.mTempRecipeStorage.setInventorySlotContents(2, this.mOutputStack);
				this.mTempRecipeStorage.setRecipeTime(this.mRecipeTimeRemaining);
				this.markDirty();
			}
		}		
		return true;
	}

	@Override
	public boolean onPostTick() {
		if (mRecipeTimeRemaining == 0){
			this.inventoryOutputs.setInventorySlotContents(2, this.getPendingOutputItem());	
			clearRecipeData();
			this.mTempRecipeStorage.setRecipeTime(this.mRecipeTimeRemaining);
			this.markDirty();
		}
		else if (mRecipeTimeRemaining > 0){
			mRecipeTimeRemaining--;
			this.mTempRecipeStorage.setRecipeTime(this.mRecipeTimeRemaining);
		}
		return true;
	}

	@Override
	public boolean processRecipe() {
		boolean removeInputA = false;
		boolean removeInputB = false;
		// Data stick
		ItemStack tBauble = this.inventoryOutputs.getStackInSlot(0);
		ItemStack tUpgrade = this.inventoryOutputs.getStackInSlot(1);
		if (tBauble != null && tUpgrade != null && this.container != null) {
			if (tBauble.getItem() instanceof ModularBauble && this.mRecipeTimeRemaining == -1) {
				if (tUpgrade != null && tBauble != null) {
					removeInputA = true;
					this.setInputStacks(tBauble, tUpgrade);
					try {
						removeInputB = addUpgrade(tUpgrade, tBauble);
						if (!removeInputB) {
						}
					} catch (Throwable t) {
					}
					if (removeInputA && removeInputB) {
						if (this.setOutputStack(tBauble)){
							if (this.inventoryOutputs.getStackInSlot(1).stackSize > 1) {
								ItemStack mTempStack = this.inventoryOutputs.getStackInSlot(1);
								mTempStack.stackSize--;
								this.inventoryOutputs.setInventorySlotContents(1, mTempStack);
							} else {
								this.inventoryOutputs.setInventorySlotContents(1, null);
							}
							this.inventoryOutputs.setInventorySlotContents(0, null);
							this.mRecipeTimeRemaining = 80;
							this.markDirty();
							return true;
						}							
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isValidModularPiece(final ItemStack itemstack){
		if (itemstack.getItem() instanceof ModularBauble){
			return true;
		}
		return false;
	}
	
	public static boolean isValidUpgrade(final ItemStack itemstack) {
		boolean isValid = false;
		if (itemstack != null){			
			Iterator<Entry<ItemStack, BT>> it = mValidUpgradeListFormChange.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ItemStack, BT> pair = it.next();
				if (pair.getKey().getItem() == itemstack.getItem()
						&& pair.getKey().getItemDamage() == itemstack.getItemDamage()){
					isValid = true;
				}
			}
			Iterator<Entry<ItemStack, Pair<Modifiers, Integer>>> it2 = mValidUpgradeList.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<ItemStack, Pair<Modifiers, Integer>> pair = it2.next();
				if (pair.getKey().getItem() == itemstack.getItem()
						&& pair.getKey().getItemDamage() == itemstack.getItemDamage()){
					isValid = true;
				}
			}			
		}
		return isValid;
	}
	
}