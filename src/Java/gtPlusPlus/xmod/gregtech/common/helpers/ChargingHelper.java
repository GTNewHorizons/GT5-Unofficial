package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaWirelessCharger;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ChargingHelper {

	private static Map<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>> mValidPlayers = new HashMap<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>>();
	protected static Map<BlockPos, GregtechMetaWirelessCharger> mChargerMap = new HashMap<BlockPos, GregtechMetaWirelessCharger>();
	private int mTickTimer = 0;
	private final int mTickMultiplier = 20;

	//Called whenever the player is updated or ticked. 
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerTick(LivingUpdateEvent event) {
		try {
			if (event.entity != null && event.entityLiving != null){		
				if (event.entityLiving instanceof EntityPlayer){
					EntityPlayer mPlayerMan = (EntityPlayer) event.entityLiving;


					if (mPlayerMan != null){
						//Utils.LOG_WARNING("Found Player.");

						if (Utils.isServer()){
							//Utils.LOG_WARNING("Found Server-Side.");

							mTickTimer++;				
							if (mTickTimer % mTickMultiplier == 0){					

								long mVoltage = 0;
								long mEuStored = 0;

								if (!mChargerMap.isEmpty() && mValidPlayers.containsKey(mPlayerMan)){
									InventoryPlayer mPlayerInventory = mPlayerMan.inventory;
									ItemStack[] mArmourContents = mPlayerInventory.armorInventory.clone();
									ItemStack[] mInventoryContents = mPlayerInventory.mainInventory.clone();

									for (GregtechMetaWirelessCharger mEntityTemp : mChargerMap.values()){
										if (mEntityTemp != null){
											if (mPlayerMan.getEntityWorld().provider.dimensionId == mEntityTemp.getDimensionID()){										
												mVoltage = mEntityTemp.maxEUInput();
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
			//Utils.LOG_WARNING("State of Wireless Charger changed in an invalid way, this prevented a crash.");

			if (!mChargerMap.isEmpty()){				
				for (GregtechMetaWirelessCharger r : mChargerMap.values()){
					if (r == null){
						mChargerMap.remove(r);
					}
				}				
			}			
			//t.printStackTrace();
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
		Logger.WARNING("trying to map new player");
		if (mValidPlayers.containsKey(mPlayer)){
			Logger.WARNING("Key contains player already?");
			return false;
		}
		else {
			Logger.WARNING("key not found, adding");
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.put(mPlayer, mEntry) == null){
				Logger.WARNING("Added a Player to the Tick Map.");
				return true;
			}
			else {
				Logger.WARNING("Tried to add player but it was already there?");
				return false;
			}
		}
	}

	public static boolean removeValidPlayer(EntityPlayer mPlayer, GregtechMetaWirelessCharger mEntity){
		if (mEntity == null){
			return false;
		}
		Logger.WARNING("trying to remove player from map");
		if (mValidPlayers.containsKey(mPlayer)){
			Logger.WARNING("key found, removing");
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.remove(mPlayer, mEntry)){
				Logger.WARNING("Removed a Player to the Tick Map.");
				return true;
			}
			else {
				Logger.WARNING("Tried to remove player but it was not there?");
				return false;
			}
		}
		else {
			Logger.WARNING("Key does not contain player?");
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
				Logger.WARNING("Slot "+mItemSlot+" contains "+mTemp.getDisplayName());
			}
			//Is item Electrical
			if (isItemValid(mTemp)){
				Logger.WARNING("1");

				//Transfer Limit
				double mItemEuTLimit = ((IElectricItem) mTemp.getItem()).getTransferLimit(mTemp);
				//Check if Tile has more or equal EU to what can be transferred into the item.
				if (mEuStored >= mItemEuTLimit){
					Logger.WARNING("2");

					double mItemMaxCharge = ((IElectricItem) mTemp.getItem()).getMaxCharge(mTemp);
					double mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);

					if (mitemCurrentCharge >= mItemMaxCharge){
						continue;
					}

					//Try get charge direct from NBT for GT and IC2 stacks
					try { 
						Logger.WARNING("3");						
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

					Logger.WARNING("4");

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
					Logger.WARNING("5");


					int mMultiVoltage = (int) (mMulti*mVoltageIncrease);

					if ((mitemCurrentCharge + mMultiVoltage) <= mItemMaxCharge){
						Logger.WARNING("6");
						int g = 0;
						if ((g = GT_ModHandler.chargeElectricItem(mTemp, mMultiVoltage, Integer.MAX_VALUE, true, false)) > 0){
							Logger.WARNING("6.5 - "+g+" - "+mMulti);
							for (int i=0; i<mMulti;i++){
								if (ElectricItem.manager.charge(mTemp, mVoltageIncrease, Integer.MAX_VALUE, false, false) > 0){
									continue;
								}
							}
						}
						if (ElectricItem.manager.getCharge(mTemp) > mitemCurrentCharge){
							Logger.WARNING("7");
							mEntity.setEUVar(mEuStored-(mVoltage*mMulti));
							mEuStored = mEntity.getEUVar();
							Logger.WARNING("Charged "+mTemp.getDisplayName()+" | Slot: "+mItemSlot+" | EU Multiplier: "+mMulti+" | EU/t input: "+mVoltageIncrease+" | EU/t consumed by Tile: "+mVoltage+" | Item Max Charge: "+mItemMaxCharge+" | Item Start Charge: "+mitemCurrentCharge+" | Item New Charge"+ElectricItem.manager.getCharge(mTemp));
							mChargedItems++;
						}
					}
					
					//Try top up Item Chrage
					mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);
					if (mitemCurrentCharge < mItemMaxCharge && mitemCurrentCharge >= (mItemMaxCharge-mVoltage)){						
						int xDif = (int) (mItemMaxCharge - mitemCurrentCharge);						
						Logger.WARNING("8 - "+xDif);
						int g = 0;
						if ((g = GT_ModHandler.chargeElectricItem(mTemp, xDif, Integer.MAX_VALUE, true, false)) >= 0){
							Logger.WARNING("8.5 - "+g);
							if (ElectricItem.manager.getCharge(mTemp) >= mItemMaxCharge){
								Logger.WARNING("9");
								mEntity.setEUVar(mEntity.getEUVar()-(xDif));
								mEuStored = mEntity.getEUVar();
								Logger.WARNING("Charged "+mTemp.getDisplayName()+" | Slot: "+mItemSlot+" | EU Multiplier: "+mMulti+" | EU/t input: "+mVoltageIncrease+" | EU/t consumed by Tile: "+mVoltage+" | Item Max Charge: "+mItemMaxCharge+" | Item Start Charge: "+mitemCurrentCharge+" | Item New Charge"+ElectricItem.manager.getCharge(mTemp));
								mChargedItems++;
							}						
						}						
					}


				}
			}
			else {
				if (mTemp != null){
					Logger.WARNING("Found Non-Valid item. "+mTemp.getDisplayName());
				}
			}
		}

		//Return Values
		if (mChargedItems < 1){
			return mEuStoredOriginal;
		}

		return mEntity.getEUVar();
	}

	public static boolean isItemValid(final ItemStack itemstack) {
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

	public static boolean accepts(final ItemStack stack) {
		if (stack == null) {
			return false;
		}
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
				|| (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), 4, true, true, true) > 0.0D);
	}

}
