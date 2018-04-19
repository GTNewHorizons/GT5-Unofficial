package gtPlusPlus.xmod.gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import gregtech.api.gui.GT_Container;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import java.util.Iterator;

public class CONTAINER_MetaTileMachineLong extends GT_Container {

	public int mActive = 0, mMaxProgressTime = 0, mProgressTime = 0, mSteam = 0, mSteamStorage = 0, mOutput = 0, mInput = 0, mID = 0, mDisplayErrorCode = 0;
	private int oActive = 0, oMaxProgressTime = 0, oProgressTime = 0, oSteam = 0, oSteamStorage = 0, oOutput = 0, oInput = 0, oID = 0, oDisplayErrorCode = 0, mTimer = 0;

	public long pEnergy, pStorage;
	private long nEnergy, nStorage;

	public short pIntMaxCountCurrent, pIntMaxCountMax;
	private short nIntMaxCountCurrent, nIntMaxCountMax;

	public CONTAINER_MetaTileMachineLong(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);

		mTileEntity = aTileEntity;

		if (mTileEntity != null && mTileEntity.getMetaTileEntity() != null) {
			addSlots(aInventoryPlayer);
			if (doesBindPlayerInventory()) bindPlayerInventory(aInventoryPlayer);
			detectAndSendChanges();
		} else {
			aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
		}
	}
	public CONTAINER_MetaTileMachineLong(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean doesBindInventory) {
		super(aInventoryPlayer, aTileEntity);
		mTileEntity = aTileEntity;

		if (mTileEntity != null && mTileEntity.getMetaTileEntity() != null) {
			addSlots(aInventoryPlayer);
			if (doesBindPlayerInventory() && doesBindInventory) bindPlayerInventory(aInventoryPlayer);
			detectAndSendChanges();
		} else {
			aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
		}
	}

	private Pair<Integer, Integer> getPowerStored(){
		final long mStoredPower = this.pEnergy;
		long mMaxInts = (mStoredPower/Integer.MAX_VALUE);
		long mLeftOver = (mStoredPower % Integer.MAX_VALUE);		
		if (mMaxInts < 0 || mMaxInts >= /*4294967298L*/ Integer.MAX_VALUE) {
			return new Pair<Integer, Integer>(-1, 0);
		}
		if (mLeftOver < 0 || mLeftOver > Integer.MAX_VALUE) {
			mLeftOver = 0;
		}		
		Pair<Integer, Integer> power = new Pair<Integer, Integer>((int) mMaxInts, (int) mLeftOver);
		return power;
	}
	
	private Pair<Integer, Integer> getMaxPowerStored(){
		final long mMaxStoredPower = this.pStorage;
		long mMaxInts = (mMaxStoredPower/Integer.MAX_VALUE);
		long mLeftOver = (mMaxStoredPower % Integer.MAX_VALUE);		
		if (mMaxInts < 0 || mMaxInts >= /*4294967298L*/ Integer.MAX_VALUE) {
			return new Pair<Integer, Integer>(-1, 0);
		}
		if (mLeftOver < 0 || mLeftOver > Integer.MAX_VALUE) {
			mLeftOver = 0;
		}		
		Pair<Integer, Integer> power = new Pair<Integer, Integer>((int) mMaxInts, (int) mLeftOver);
		return power;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
		pStorage = Math.min(Long.MAX_VALUE, mTileEntity.getEUCapacity());
		pEnergy = Math.min(Long.MAX_VALUE, mTileEntity.getStoredEU());
		mSteamStorage = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getSteamCapacity());
		mSteam = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getStoredSteam());
		mOutput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getOutputVoltage());
		mInput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getInputVoltage());
		mDisplayErrorCode = mTileEntity.getErrorDisplayID();
		mProgressTime = mTileEntity.getProgress();
		mMaxProgressTime = mTileEntity.getMaxProgress();
		mActive = mTileEntity.isActive() ? 1 : 0;
		mTimer++;
		
		Pair<Integer, Integer> powerPacket = getPowerStored();
		Pair<Integer, Integer> maxPowerPacket = getMaxPowerStored();
		
		int mPacketsToSend = powerPacket.getKey();
		int mLeftOverPowerStored = powerPacket.getValue();

		int mMaxStorageA = maxPowerPacket.getKey();
		int mMaxStorageB = maxPowerPacket.getValue();
		
		//Someone has a VERY FULL power supply.
		if (mPacketsToSend == -1) {
			Logger.INFO("TOO MUCH MATH - USE BIGINTEGER - GAME BREAKING BUG MAY OCCUR");
			return;
		}
		

		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
			if (mTimer % 500 == 10 || nEnergy != pEnergy) {
				var1.sendProgressBarUpdate(this, 0, mLeftOverPowerStored & 65535);
				var1.sendProgressBarUpdate(this, 1, mLeftOverPowerStored >>> 16);
				var1.sendProgressBarUpdate(this, 7730, mPacketsToSend);
			}
			if (mTimer % 500 == 10 || nStorage != pStorage) {
				var1.sendProgressBarUpdate(this, 2, mMaxStorageB & 65535);
				var1.sendProgressBarUpdate(this, 3, mMaxStorageB >>> 16);
				var1.sendProgressBarUpdate(this, 7731, mMaxStorageA);
			}
			if (mTimer % 500 == 10 || oOutput != mOutput) {
				var1.sendProgressBarUpdate(this, 4, mOutput);
			}
			if (mTimer % 500 == 10 || oInput != mInput) {
				var1.sendProgressBarUpdate(this, 5, mInput);
			}
			if (mTimer % 500 == 10 || oDisplayErrorCode != mDisplayErrorCode) {
				var1.sendProgressBarUpdate(this, 6, mDisplayErrorCode);
			}
			if (mTimer % 500 == 10 || oProgressTime != mProgressTime) {
				var1.sendProgressBarUpdate(this, 11, mProgressTime & 65535);
				var1.sendProgressBarUpdate(this, 12, mProgressTime >>> 16);
			}
			if (mTimer % 500 == 10 || oMaxProgressTime != mMaxProgressTime) {
				var1.sendProgressBarUpdate(this, 13, mMaxProgressTime & 65535);
				var1.sendProgressBarUpdate(this, 14, mMaxProgressTime >>> 16);
			}
			if (mTimer % 500 == 10 || oID != mID) {
				var1.sendProgressBarUpdate(this, 15, mID);
			}
			if (mTimer % 500 == 10 || oActive != mActive) {
				var1.sendProgressBarUpdate(this, 16, mActive);
			}
			if (mTimer % 500 == 10 || oSteam != mSteam) {
				var1.sendProgressBarUpdate(this, 17, mSteam & 65535);
				var1.sendProgressBarUpdate(this, 18, mSteam >>> 16);
			}
			if (mTimer % 500 == 10 || oSteamStorage != mSteamStorage) {
				var1.sendProgressBarUpdate(this, 19, mSteamStorage & 65535);
				var1.sendProgressBarUpdate(this, 20, mSteamStorage >>> 16);
			}
		}

		oID = mID;
		oSteam = mSteam;
		oInput = mInput;
		oActive = mActive;
		oOutput = mOutput;
		nEnergy = pEnergy;
		nStorage = pStorage;
		oSteamStorage = mSteamStorage;
		oProgressTime = mProgressTime;
		oMaxProgressTime = mMaxProgressTime;
		oDisplayErrorCode = mDisplayErrorCode;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			/*case 7730:
				pIntMaxCountCurrent = par2;*/
			case 0:
				pEnergy = pEnergy & -65536 | par2;
				break;
			case 1:
				pEnergy = pEnergy & 65535 | par2 << 16;
				break;
			case 2:
				pStorage = pStorage & -65536 | par2;
				break;
			case 3:
				pStorage = pStorage & 65535 | par2 << 16;
				break;
			case 4:
				mOutput = par2;
				break;
			case 5:
				mInput = par2;
				break;
			case 6:
				mDisplayErrorCode = par2;
				break;
			case 11:
				mProgressTime = mProgressTime & -65536 | par2;
				break;
			case 12:
				mProgressTime = mProgressTime & 65535 | par2 << 16;
				break;
			case 13:
				mMaxProgressTime = mMaxProgressTime & -65536 | par2;
				break;
			case 14:
				mMaxProgressTime = mMaxProgressTime & 65535 | par2 << 16;
				break;
			case 15:
				mID = par2;
				break;
			case 16:
				mActive = par2;
				break;
			case 17:
				mSteam = mSteam & -65536 | par2;
				break;
			case 18:
				mSteam = mSteam & 65535 | par2 << 16;
				break;
			case 19:
				mSteamStorage = mSteamStorage & -65536 | par2;
				break;
			case 20:
				mSteamStorage = mSteamStorage & 65535 | par2 << 16;
				break;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return mTileEntity.isUseableByPlayer(player);
	}

	public String trans(String aKey, String aEnglish){
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_"+aKey, aEnglish, false);
	}
}