package gtPlusPlus.core.tileentities.machines;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.inventories.modulartable.InventoryModularMain;
import gtPlusPlus.core.inventories.modulartable.InventoryModularOutput;
import gtPlusPlus.core.item.bauble.ModularBauble;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.nbt.ModularArmourUtils;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.BT;
import gtPlusPlus.core.util.nbt.ModularArmourUtils.Modifiers;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityModularityTable extends TileEntity {

	public InventoryModularMain inventoryGrid;
	public InventoryModularOutput inventoryOutputs;
	public InventoryModularOutput mTempRecipeStorage;
	private Container_ModularityTable container;
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
		

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.mRecipeTimeRemaining = nbt.getInteger("mRecipeTime");
		this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
		this.inventoryGrid.readFromNBT(nbt.getCompoundTag("ContentsGrid"));
		this.mTempRecipeStorage.readFromNBT(nbt.getCompoundTag("ContentsRecipeTemp"));
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			
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
							}							
						} else {
							Utils.LOG_INFO("1: " + removeInputA + " | 2: " + removeInputB);
						}
					}
				}
			}
			
			if (mRecipeTimeRemaining == 0){
				this.inventoryOutputs.setInventorySlotContents(2, this.getPendingOutputItem());	
				clearRecipeData();
				this.mTempRecipeStorage.setRecipeTime(this.mRecipeTimeRemaining);
				this.markDirty();
			}
			/*else if (mRecipeTimeRemaining == -1){
				mRecipeTimeRemaining = -1;
			}*/
			else if (mRecipeTimeRemaining > 0){
				mRecipeTimeRemaining--;
				this.mTempRecipeStorage.setRecipeTime(this.mRecipeTimeRemaining);
				//Utils.LOG_INFO("Remaining: "+this.mRecipeTimeRemaining);
			}
			
		}
		super.updateEntity();
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
		generateUpgradeData(Materials.Aluminium.getPlates(1), Modifiers.BOOST_DEF, 1);
		generateUpgradeData(Materials.StainlessSteel.getPlates(1), Modifiers.BOOST_DEF, 2);
		generateUpgradeData(Materials.Tungsten.getPlates(1), Modifiers.BOOST_DEF, 3);
		generateUpgradeData(Materials.TungstenSteel.getPlates(1), Modifiers.BOOST_DEF, 4);
		generateUpgradeData(Materials.Naquadah.getPlates(1), Modifiers.BOOST_DEF, 5);

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
					int mCurrentLevel = ModularArmourUtils.getModifierLevel(tBauble, newPair);
					ModularArmourUtils.setModifierLevel(tBauble, newPair);
					return true;
				}
			}
		} catch (Throwable t) {

		}
		return false;
	}

}