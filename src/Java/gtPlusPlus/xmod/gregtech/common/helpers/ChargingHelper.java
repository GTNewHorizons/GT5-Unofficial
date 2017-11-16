package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.math.MathUtils;
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
		if (event.entity != null && event.entityLiving != null){		
			if (event.entityLiving instanceof EntityPlayer){
				EntityPlayer mPlayerMan = (EntityPlayer) event.entityLiving;


				if (mPlayerMan != null){
					//Utils.LOG_INFO("Found Player.");

					if (Utils.isServer()){
						//Utils.LOG_INFO("Found Server-Side.");

						mTickTimer++;				
						if (mTickTimer % 20 == 0){					

							long mVoltage = 0;
							long mEuStored = 0;

							if (!mChargerMap.isEmpty()  && mValidPlayers.containsKey(mPlayerMan)){

								for (GregtechMetaWirelessCharger mEntityTemp : mChargerMap.values()){
									if (mEntityTemp != null){
										mVoltage = mEntityTemp.getInputTier();
										mEuStored = mEntityTemp.getEUVar();
										if (mVoltage > 0 && mEuStored >= mVoltage){								

											InventoryPlayer mPlayerInventory = mPlayerMan.inventory;
											ItemStack[] mArmourContents = mPlayerInventory.armorInventory;
											ItemStack[] mInventoryContents = mPlayerInventory.mainInventory;

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
		if (mEntity == null){
			return -100;
		}
		if (mItems == null || mItems.length == 0){
			return mEntity.getEUVar();
		}
		int mChargedItems = 0;
		final int mTier = mEntity.getTier();
		final long mVoltage = mEntity.maxEUInput();
		long mEuStored = mEntity.getEUVar();
		final long mEuStoredOriginal = mEntity.getEUVar();
		for (ItemStack mTemp : mItems){
			if (isItemValid(mTemp)){
				double mItemTier = ((IElectricItem) mTemp.getItem()).getTransferLimit(mTemp);
				if (mEuStored >= mItemTier){
					double mItemMaxCharge = ((IElectricItem) mTemp.getItem()).getMaxCharge(mTemp);
					double mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);
					
					double mVoltageDecrease;
					if (mItemTier >= mVoltage){
						mVoltageDecrease = mVoltage;
					}
					else if (mItemTier < mVoltage){
						mVoltageDecrease = mItemTier;						
					}
					else {
						mVoltageDecrease = mItemTier;
					}
					
					if ((mitemCurrentCharge + mVoltageDecrease) <= (mItemMaxCharge - mVoltageDecrease)){
						ElectricItem.manager.charge(mTemp, mVoltageDecrease, mTier, false, false);
						mEntity.setEUVar((long) (mEuStored-mVoltage));
						mEuStored = mEntity.getEUVar();
						mChargedItems++;
						Utils.LOG_INFO("Charged "+mTemp.getDisplayName()+" by "+mItemTier+" for "+mPlayer.getCommandSenderName());
					}
				}
			}
		}
		if (mChargedItems < 1){
			return mEuStoredOriginal;
		}
		return mEntity.getEUVar();
	}

	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack == null){
			return false;
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
		if (GT_ModHandler.isElectricItem(stack)){
			return true;
		}
		
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
				|| (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), 4, true, true, true) > 0.0D);
	}

}
