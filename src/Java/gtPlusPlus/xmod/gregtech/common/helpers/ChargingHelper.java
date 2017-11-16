package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.GT_Values;
import gregtech.api.items.GT_MetaGenerated_Tool;
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

public class ChargingHelper {

	protected static Map<BlockPos, GregtechMetaWirelessCharger> mChargerMap = new HashMap<BlockPos, GregtechMetaWirelessCharger>();
	private int mTickTimer = 0;

	public static boolean addEntry(BlockPos mPos, GregtechMetaWirelessCharger mEntity){
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

	private static Map<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>> mValidPlayers = new HashMap<EntityPlayer, Pair<GregtechMetaWirelessCharger, Byte>>();

	public static boolean addValidPlayer(EntityPlayer mPlayer, GregtechMetaWirelessCharger mEntity){
		if (!mValidPlayers.containsKey(mPlayer)){
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.put(mPlayer, mEntry) == null){
				Utils.LOG_INFO("Added a Player to the Tick Map.");
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
	
	public static boolean removeValidPlayer(EntityPlayer mPlayer, GregtechMetaWirelessCharger mEntity){
		if (mValidPlayers.containsKey(mPlayer)){
			Pair<GregtechMetaWirelessCharger, Byte> mEntry = new Pair<GregtechMetaWirelessCharger, Byte>(mEntity, (byte) mEntity.getMode());
			if (mValidPlayers.remove(mPlayer, mEntry)){
				Utils.LOG_INFO("Removed a Player to the Tick Map.");
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
	
	
	//Called whenever the player is updated or ticked. 
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player != null){
			Utils.LOG_INFO("Found Player.");
			if (event.side == Side.SERVER){
				Utils.LOG_INFO("Found Server-Side.");

				mTickTimer++;				

				if (mTickTimer % 20 == 0){					
					
					long mVoltage = 0;
					long mEuStored = 0;

					if (!mChargerMap.isEmpty()  && mValidPlayers.containsKey(event.player)){

						for (GregtechMetaWirelessCharger mEntityTemp : mChargerMap.values()){
							mVoltage = mEntityTemp.getInputTier();
							mEuStored = mEntityTemp.getEUVar();
							if (mVoltage > 0 && mEuStored >= mVoltage){								
								
								InventoryPlayer mPlayerInventory = event.player.inventory;
								ItemStack[] mArmourContents = mPlayerInventory.armorInventory;
								ItemStack[] mInventoryContents = mPlayerInventory.mainInventory;

								Map<EntityPlayer, UUID> LR = mEntityTemp.getLongRangeMap();
								Map<UUID, EntityPlayer> LO = mEntityTemp.getLocalMap();

								long mStartingEu = mEntityTemp.getEUVar();
								long mCurrentEu = mEntityTemp.getEUVar();
								long mEuUsed = 0;
								if (mEntityTemp.getMode() == 0){
									
									if (!LR.isEmpty() && LR.containsKey(event.player)){
										mCurrentEu = chargeItems(mEntityTemp, mArmourContents, event.player);
										mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, event.player);
									}
								}
								else if (mEntityTemp.getMode() == 1){
									if (!LO.isEmpty() && LO.containsValue(event.player)){
										mCurrentEu = chargeItems(mEntityTemp, mArmourContents, event.player);
										mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, event.player);
									}
								}
								else {
									if (!LR.isEmpty() && LR.containsKey(event.player)){
										mCurrentEu = chargeItems(mEntityTemp, mArmourContents, event.player);
										mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, event.player);
									}
									if (!LO.isEmpty() && LO.containsValue(event.player)){
										mCurrentEu = chargeItems(mEntityTemp, mArmourContents, event.player);
										mCurrentEu = chargeItems(mEntityTemp, mInventoryContents, event.player);
									}
								}	
								
								if ((mEuUsed = (mStartingEu - mCurrentEu)) <= 0){
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
									double mDistance = calculateDistance(mEntityTemp, event);
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
								Utils.LOG_INFO("Voltage: "+mVoltage+" | Eu Storage: "+mEuStored);
							}
						}
					}
				}
			}
		}
	}
	
	public double calculateDistance(GregtechMetaWirelessCharger mEntityTemp, TickEvent.PlayerTickEvent event){
		return mEntityTemp.getDistanceBetweenTwoPositions(mEntityTemp.getTileEntityPosition(), mEntityTemp.getPositionOfEntity(event.player));
	}

	public long chargeItems(GregtechMetaWirelessCharger mEntity, ItemStack[] mItems, EntityPlayer mPlayer){
		if (mEntity == null || mItems == null || mItems.length == 0){
			return mEntity.getEUVar();
		}
		int mChargedItems = 0;
		final int mTier = mEntity.getTier();
		final long mVoltage = mEntity.getInputTier();
		long mEuStored = mEntity.getEUVar();
		final long mEuStoredOriginal = mEntity.getEUVar();
		for (ItemStack mTemp : mItems){
			if (isItemValid(mTemp)){
				ElectricItem.manager.charge(mTemp, mVoltage, mTier, false, false);
				mEntity.setEUVar(mEuStored-mVoltage);
				mEuStored = mEntity.getEUVar();
				mChargedItems++;
				Utils.LOG_INFO("Charged "+mTemp.getDisplayName()+" by "+mVoltage+" for "+mPlayer.getCommandSenderName());
			}
		}
		if (mChargedItems < 1){
			return mEuStoredOriginal;
		}
		return mEntity.getEUVar();
	}

	public boolean isItemValid(final ItemStack itemstack) {
		if ((accepts(itemstack)) || (itemstack.getItem() instanceof GT_MetaGenerated_Tool) || (itemstack.getItem() instanceof IElectricItem)) {
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

	//Called when the client ticks. 
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {

	}

	//Called when the server ticks. Usually 20 ticks a second. 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {

	}

	//Called when a new frame is displayed (See fps) 
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {

	}

	//Called when the world ticks
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {

	}

}
