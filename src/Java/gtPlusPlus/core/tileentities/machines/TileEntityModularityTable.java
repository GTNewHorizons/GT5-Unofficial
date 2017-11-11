package gtPlusPlus.core.tileentities.machines;

import static gtPlusPlus.core.tileentities.machines.TileEntityModularityTable.mValidUpgradeListFormChange;

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
	private Container_ModularityTable container;

	public TileEntityModularityTable(){ 
		Utils.LOG_INFO("I am created.");
		this.inventoryGrid = new InventoryModularMain();//number of slots - without product slot
		this.inventoryOutputs = new InventoryModularOutput();//number of slots - without product slot
		this.canUpdate();
		generateAllValidUpgrades();
	}

	public void setContainer(Container_ModularityTable container_ModularityTable){
		this.container = container_ModularityTable;
	}

	@SuppressWarnings("static-method")
	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag){
		if(!nbt.hasKey(tag))
		{
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt){
		super.writeToNBT(nbt);
		this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote){
			boolean removeInputA = false;
			boolean removeInputB = false;
			//Data stick
			ItemStack tBauble = this.inventoryOutputs.getStackInSlot(0);
			ItemStack tUpgrade = this.inventoryOutputs.getStackInSlot(1);
			if (tBauble != null && tUpgrade != null && this.container != null){
				if (tBauble.getItem() instanceof ModularBauble){
					if (tUpgrade != null && tBauble != null){
						removeInputA = true;

						Utils.LOG_INFO("found "+tUpgrade.getDisplayName());
						try {											
							removeInputB = addUpgrade(tUpgrade, tBauble);
							if (!removeInputB){
								Utils.LOG_INFO("Failed to add "+tUpgrade.getDisplayName());
							}
						}
						catch (Throwable t){

						}

						if (removeInputA && removeInputB){
							Utils.LOG_INFO("set new Modular bauble");
							if (this.inventoryOutputs.getStackInSlot(1).stackSize > 1){
								ItemStack mTempStack = this.inventoryOutputs.getStackInSlot(1);
								mTempStack.stackSize--;
								this.inventoryOutputs.setInventorySlotContents(1, mTempStack);
							}
							else {
								this.inventoryOutputs.setInventorySlotContents(1, null);
								
							}
							this.inventoryOutputs.setInventorySlotContents(0, null);
							this.inventoryOutputs.setInventorySlotContents(2, tBauble);
						}	
						else {
							Utils.LOG_INFO("1: "+removeInputA+" | 2: "+removeInputB);
						}
					}
				}
			}		
		}
		super.updateEntity();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	public static Map<ItemStack, Pair<Modifiers, Integer>> mValidUpgradeList = new HashMap<ItemStack, Pair<Modifiers, Integer>>();
	public static Map<ItemStack, BT> mValidUpgradeListFormChange = new HashMap<ItemStack, BT>();

	private static boolean generateAllValidUpgrades(){

		//Form Change
		generateUpgradeFormData(ItemList.Sensor_MV.get(1), BT.TYPE_RING);
		generateUpgradeFormData(ItemList.Electric_Piston_MV.get(1), BT.TYPE_BELT);
		generateUpgradeFormData(ItemList.Emitter_MV.get(1), BT.TYPE_AMULET);

		//Damage Boost
		generateUpgradeData(ItemList.Electric_Motor_LV.get(1), Modifiers.BOOST_DAMAGE, 1);
		generateUpgradeData(ItemList.Electric_Motor_MV.get(1), Modifiers.BOOST_DAMAGE, 2);
		generateUpgradeData(ItemList.Electric_Motor_HV.get(1), Modifiers.BOOST_DAMAGE, 3);
		generateUpgradeData(ItemList.Electric_Motor_EV.get(1), Modifiers.BOOST_DAMAGE, 4);
		generateUpgradeData(ItemList.Electric_Motor_IV.get(1), Modifiers.BOOST_DAMAGE, 5);

		//Defence Boost
		generateUpgradeData(Materials.Aluminium.getPlates(1), Modifiers.BOOST_DEF, 1);
		generateUpgradeData(Materials.StainlessSteel.getPlates(1), Modifiers.BOOST_DEF, 2);
		generateUpgradeData(Materials.Tungsten.getPlates(1), Modifiers.BOOST_DEF, 3);
		generateUpgradeData(Materials.TungstenSteel.getPlates(1), Modifiers.BOOST_DEF, 4);
		generateUpgradeData(Materials.Naquadah.getPlates(1), Modifiers.BOOST_DEF, 5);

		//Hp Boost
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.golden_apple, 0, 1), Modifiers.BOOST_HP, 1);
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.golden_apple, 1, 1), Modifiers.BOOST_HP, 2);
		generateUpgradeData(ItemUtils.simpleMetaStack(Items.nether_star, 0, 1), Modifiers.BOOST_HP, 3);
		generateUpgradeData(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32725", 32725, 1), Modifiers.BOOST_HP, 4);
		generateUpgradeData(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32726", 32726, 1), Modifiers.BOOST_HP, 5);

		return true;
	}

	public static boolean generateUpgradeData(ItemStack tStack, Modifiers tMod, int tLevel){
		Pair<Modifiers, Integer> tTemp = new Pair<Modifiers, Integer>(tMod, tLevel);
		if (mValidUpgradeList.put(tStack, tTemp) != null){
			return true;
		}
		return false;
	}

	public static boolean generateUpgradeFormData(ItemStack tStack, BT tMod){
		if (mValidUpgradeListFormChange.put(tStack, tMod) != null){
			return true;
		}
		return false;
	}

	public static boolean addUpgrade(ItemStack tStack, ItemStack tBauble){

		try {
			Iterator<Entry<ItemStack, BT>> it = mValidUpgradeListFormChange.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ItemStack, BT> pair = it.next();
				if (pair.getKey().getItem() == tStack.getItem()
						&& pair.getKey().getItemDamage() == tStack.getItemDamage()){
					ModularArmourUtils.setBaubleType(tBauble, pair.getValue());
					tBauble.setItemDamage(ModularArmourUtils.getBaubleTypeID(tBauble));
					//ModularArmourUtils.setBaubleType(tBauble, mValidUpgradeListFormChange.get(tStack).getThis());
					return true;
				}
			}
		} catch (Throwable t){

		}
		try {
			Iterator<Entry<ItemStack, Pair<Modifiers, Integer>>> it2 = mValidUpgradeList.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<ItemStack, Pair<Modifiers, Integer>> pair = it2.next();
				if (pair.getKey().getItem() == tStack.getItem()
						&& pair.getKey().getItemDamage() == tStack.getItemDamage()){
					Pair<Modifiers, Integer> newPair = pair.getValue();
					int mCurrentLevel = ModularArmourUtils.getModifierLevel(tBauble, newPair);
					ModularArmourUtils.setModifierLevel(tBauble, newPair);
					return true;
				}
			}
		} catch (Throwable t){

		}
		Utils.LOG_INFO("Could not find valid upgrade: "+tStack.getDisplayName()+".");
		//Utils.LOG_INFO("Bool1: "+mValidUpgradeListFormChange.containsKey(tStack));
		//Utils.LOG_INFO("Bool2: "+mValidUpgradeList.containsKey(tStack));
		return false;
	}

}