package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tools.GT_Tool;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaWirelessCharger;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ChargingHelper {

	private static Map<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>> mValidPlayers = new HashMap<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>>();
	protected static Map<BlockPos, GregtechMetaWirelessCharger> mChargerMap = new HashMap<BlockPos, GregtechMetaWirelessCharger>();
	private int mTickTimer = 0;

	//Called whenever the player is updated or ticked. 
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerTick(LivingUpdateEvent event) {
		try {
		if (event.entity != null && event.entityLiving != null){		
			if (event.entityLiving instanceof EntityPlayer){
				EntityPlayer mPlayerMan = (EntityPlayer) event.entityLiving;


				if (mPlayerMan != null){
					//Utils.LOG_INFO("Found Player.");

					if (Utils.isServer()){
						//Utils.LOG_INFO("Found Server-Side.");

						mTickTimer++;				
						if (mTickTimer % 10 == 0){					

							long mVoltage = 0;
							long mEuStored = 0;

							if (!mChargerMap.isEmpty() && mValidPlayers.containsKey(mPlayerMan)){

								InventoryPlayer mPlayerInventory = mPlayerMan.inventory;
								ItemStack[] mArmourContents = mPlayerInventory.armorInventory.clone();
								ItemStack[] mInventoryContents = mPlayerInventory.mainInventory.clone();

								for (GregtechMetaWirelessCharger mEntityTemp : mChargerMap.values()){
									if (mEntityTemp != null){
										mVoltage = mEntityTemp.getInputTier();
										mEuStored = mEntityTemp.getEUVar();
										if (mVoltage > 0 && mEuStored >= mVoltage){								

											Map<EntityPlayer, UUID> LR = mEntityTemp.getLongRangeMap();
											Map<UUID, EntityPlayer> LO = mEntityTemp.getLocalMap();

											long mStartingEu = mEntityTemp.getEUVar();
											long mCurrentEu = mEntityTemp.getEUVar();
											long mEuUsed = 0;
											if (mEntityTemp.getMode() == 0){

												if (!LR.isEmpty() && LR.containsKey(mPlayerMan)){
													mCurrentEu = chargeItems(mEntityTemp, mArmourContents, mPlayerMan);
													mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, mPlayerMan);
												}
											}
											else if (mEntityTemp.getMode() == 1){
												if (!LO.isEmpty() && LO.containsValue(mPlayerMan)){
													mCurrentEu = chargeItems(mEntityTemp, mArmourContents, mPlayerMan);
													mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, mPlayerMan);
												}
											}
											else {
												if (!LR.isEmpty() && LR.containsKey(mPlayerMan)){
													mCurrentEu = chargeItems(mEntityTemp, mArmourContents, mPlayerMan);
													mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, mPlayerMan);
												}
												if (!LO.isEmpty() && LO.containsValue(mPlayerMan)){
													mCurrentEu = chargeItems(mEntityTemp, mArmourContents, mPlayerMan);
													mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, mPlayerMan);
												}
											}	

											if ((mEuUsed = (mStartingEu - mCurrentEu)) <= 0 && mEntityTemp != null){
												long mMaxDistance;
												if (mEntityTemp.getMode() == 0){
													mMaxDistance = (4*GT_Values.V[mEntityTemp.getTier()]);
												}
												else if (mEntityTemp.getMode() == 1){
													mMaxDistance = (mEntityTemp.getTier()*10);
												}
												else {
													mMaxDistance = (4*GT_Values.V[mEntityTemp.getTier()]/2);										
												}
												double mDistance = calculateDistance(mEntityTemp, mPlayerMan);
												long mVoltageCost = MathUtils.findPercentageOfInt(mMaxDistance, (float) mDistance);

												if (mVoltageCost > 0){
													if (mVoltageCost > mEntityTemp.maxEUInput()){
														mEntityTemp.setEUVar((mEntityTemp.getEUVar()-mEntityTemp.maxEUInput()));
													}
													else {
														mEntityTemp.setEUVar((mEntityTemp.getEUVar()-mVoltageCost));
													}
												}

											}

										}
										else {
											//Utils.LOG_INFO("Voltage: "+mVoltage+" | Eu Storage: "+mEuStored);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		}
		catch (Throwable t){
			Utils.LOG_INFO("State of Wireless Charger changed in an invalid way, this prevented a crash.");
		}
	}

	public static boolean addEntry(BlockPos mPos, GregtechMetaWirelessCharger mEntity){
		if (mEntity == null){
			return false;
		}
		if (!mChargerMap.containsKey(mPos)){
			if (mChargerMap.put(mPos, mEntity) == null){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public static boolean removeEntry(BlockPos mPos, GregtechMetaWirelessCharger mEntity){
		if (mEntity == null){
			return false;
		}
		if (mChargerMap.containsKey(mPos)){
			if (mChargerMap.remove(mPos, mEntity)){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public static boolean addValidPlayer(EntityPlayer mPlayer, GregtechMetaWirelessCharger mEntity){
		if (mEntity == null){
			return false;
		}
		Utils.LOG_INFO("trying to map new player");
		if (mValidPlayers.containsKey(mPlayer)){
			Utils.LOG_INFO("Key contains player already?");
			return false;
		}
		else {
			Utils.LOG_INFO("key not found, adding");
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.put(mPlayer, mEntry) == null){
				Utils.LOG_INFO("Added a Player to the Tick Map.");
				return true;
			}
			else {
				Utils.LOG_INFO("Tried to add player but it was already there?");
				return false;
			}
		}
	}

	public static boolean removeValidPlayer(EntityPlayer mPlayer, GregtechMetaWirelessCharger mEntity){
		if (mEntity == null){
			return false;
		}
		Utils.LOG_INFO("trying to remove player from map");
		if (mValidPlayers.containsKey(mPlayer)){
			Utils.LOG_INFO("key found, removing");
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.remove(mPlayer, mEntry)){
				Utils.LOG_INFO("Removed a Player to the Tick Map.");
				return true;
			}
			else {
				Utils.LOG_INFO("Tried to remove player but it was not there?");
				return false;
			}
		}
		else {
			Utils.LOG_INFO("Key does not contain player?");
			return false;
		}
	}

	public double calculateDistance(GregtechMetaWirelessCharger mEntityTemp, EntityPlayer mPlayerMan){
		if (mEntityTemp == null || mPlayerMan == null){
			return 0;
		}
		return mEntityTemp.getDistanceBetweenTwoPositions(mEntityTemp.getTileEntityPosition(), mEntityTemp.getPositionOfEntity(mPlayerMan));
	}

	public long chargeItems(GregtechMetaWirelessCharger mEntity, ItemStack[] mItems, EntityPlayer mPlayer){
		if (mEntity == null){
			return -100;
		}
		if (mItems == null || mItems.length == 0){
			return mEntity.getEUVar();
		}
		long mInitialValue = mEntity.getEUVar();
		long mReturnValue = chargeItemsEx(mEntity, mItems, mPlayer);		
		return ((mReturnValue < mInitialValue) ? mReturnValue : mInitialValue);
	}

	public long chargeItemsEx(GregtechMetaWirelessCharger mEntity, ItemStack[] mItems, EntityPlayer mPlayer){

		//Bad Entity
		if (mEntity == null){
			return -100;
		}
		//Bad Inventory
		if (mItems == null || mItems.length == 0){
			return mEntity.getEUVar();
		}
		//Set Variables to Charge
		int mChargedItems = 0;
		final int mTier = mEntity.getTier();
		final long mVoltage = mEntity.maxEUInput();
		long mEuStored = mEntity.getEUVar();
		final long mEuStoredOriginal = mEntity.getEUVar();
		//For Inventory Contents

		int mItemSlot = 0;

		for (ItemStack mTemp : mItems){
			mItemSlot++;
			if (mTemp != null){
				Utils.LOG_INFO("Slot "+mItemSlot+" contains "+mTemp.getDisplayName());
			}
			//Is item Electrical
			if (isItemValid(mTemp)){
				Utils.LOG_INFO("1");

				//Transfer Limit
				double mItemEuTLimit = ((IElectricItem) mTemp.getItem()).getTransferLimit(mTemp);
				//Check if Tile has more or equal EU to what can be transferred into the item.
				if (mEuStored >= mItemEuTLimit){
					Utils.LOG_INFO("2");

					double mItemMaxCharge = ((IElectricItem) mTemp.getItem()).getMaxCharge(mTemp);
					double mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);

					if (mitemCurrentCharge >= mItemMaxCharge){
						continue;
					}

					//Try get charge direct from NBT for GT and IC2 stacks
					try { 
						Utils.LOG_INFO("3");						
						if (mTemp.getItem() instanceof GT_MetaGenerated_Tool_01 
								|| mTemp.getItem() instanceof GT_MetaGenerated_Item_01 
								|| mTemp.getItem() instanceof GT_MetaGenerated_Item_02 
								|| Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03").isInstance(mTemp.getItem()) 
								|| mTemp.getItem().getClass().getName().toLowerCase().equals(("gregtech.common.items.GT_MetaGenerated_Tool_01").toLowerCase())){
							if (!NBTUtils.hasKey(mTemp, "GT.ItemCharge")){
								if (!mTemp.getDisplayName().toLowerCase().contains("battery")){
									if (!GT_ModHandler.isElectricItem(mTemp)){
										continue;
									}
								}
								else {
									mitemCurrentCharge = 0;
								}
							}
							else {
								mitemCurrentCharge = NBTUtils.getLong(mTemp, "GT.ItemCharge");
							}
						}
						else if (mTemp.getItem() instanceof IElectricItem){
							mitemCurrentCharge = NBTUtils.getLong(mTemp, "charge");
						}
					} catch (ClassNotFoundException e) {

					}				

					double mVoltageIncrease;
					if (mItemEuTLimit >= mVoltage){
						mVoltageIncrease = mVoltage;
					}
					else if (mItemEuTLimit < mVoltage){
						mVoltageIncrease = mItemEuTLimit;						
					}
					else {
						mVoltageIncrease = mItemEuTLimit;
					}
					
					Utils.LOG_INFO("4");

					int mMulti;
					if ((mitemCurrentCharge + (mVoltageIncrease*20)) <= (mItemMaxCharge - (mVoltageIncrease*20))){
						mMulti = 20;
					}
					else if ((mitemCurrentCharge + (mVoltageIncrease*10)) <= (mItemMaxCharge - (mVoltageIncrease*10))){
						mMulti = 10;
					}
					else if ((mitemCurrentCharge + (mVoltageIncrease*5)) <= (mItemMaxCharge - (mVoltageIncrease*5))){
						mMulti = 5;
					}
					else {
						mMulti = 1;
					}
					Utils.LOG_INFO("5");


					int mMultiVoltage = (int) (mMulti*mVoltageIncrease);

					if ((mitemCurrentCharge + mMultiVoltage) <= mItemMaxCharge){
						Utils.LOG_INFO("6");
						if (GT_ModHandler.chargeElectricItem(mTemp, mMultiVoltage, mTier, true, false) == 0){
							Utils.LOG_INFO("6.5");
							for (int i=0; i<mMulti;i++){
								if (ElectricItem.manager.charge(mTemp, mVoltageIncrease, mTier, false, false) == 0){
									continue;
								}
							}
						}
						if (ElectricItem.manager.getCharge(mTemp) > mitemCurrentCharge){
							Utils.LOG_INFO("7");
							mEntity.setEUVar((long) (mEuStored-(mVoltage*mMulti)));
							mEuStored = mEntity.getEUVar();
							Utils.LOG_INFO("Charged "+mTemp.getDisplayName()+" | Slot: "+mItemSlot+" | EU Multiplier: "+mMulti+" | EU/t input: "+mVoltageIncrease+" | EU/t consumed by Tile: "+mVoltage+" | Item Max Charge: "+mItemMaxCharge+" | Item Start Charge: "+mitemCurrentCharge+" | Item New Charge"+ElectricItem.manager.getCharge(mTemp));
							mChargedItems++;
						}
					}


				}
			}
			else {
				if (mTemp != null){
					Utils.LOG_INFO("Found Non-Valid item. "+mTemp.getDisplayName());
				}
			}
		}

		//Return Values
		if (mChargedItems < 1){
			return mEuStoredOriginal;
		}

		return mEntity.getEUVar();
	}

	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack == null){
			return false;
		}
		if (GT_ModHandler.isElectricItem(itemstack)){
			return true;
		}
		if ((accepts(itemstack)) || (itemstack.getItem() instanceof IElectricItem)) {
			return true;
		}
		return false;
	}

	public boolean accepts(final ItemStack stack) {
		if (stack == null) {
			return false;
		}
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
				|| (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), 4, true, true, true) > 0.0D);
	}

}
