package gtPlusPlus.xmod.gregtech.api.gui.hatches;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.slots.SlotNoInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_HatchNbtConsumable extends GT_Container {

	public final int mInputslotCount;
	private final int mTotalSlotCount;
	
    public CONTAINER_HatchNbtConsumable(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aInputslotCount) {
        super(aInventoryPlayer, aTileEntity);
		mInputslotCount = aInputslotCount;
		mTotalSlotCount = aInputslotCount*2;
        mTileEntity = aTileEntity;
        if (mTileEntity != null && mTileEntity.getMetaTileEntity() != null) {
            addSlots(aInventoryPlayer);
            if (doesBindPlayerInventory()) {
            	bindPlayerInventory(aInventoryPlayer);
            }
            detectAndSendChanges();
        } else {
            aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
        }
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
    	if (mTotalSlotCount == 8) {    	
    		final int aSlotYStart = 26;
    		final int aSlotYFinish = aSlotYStart + (18*2);
    		final int aSlotXStart1 = 26;
    		final int aSlotXFinish1 = aSlotXStart1 + (18*2);
    		final int aSlotXStart2 = 116;
    		final int aSlotXFinish2 = aSlotXStart2 + (18*2);    	
    		int aSlotID = 0;
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart1; x < aSlotXFinish1; x += 18) {
    	            addSlotToContainer(new Slot(mTileEntity, aSlotID++, x, y));
        		}
    		}
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart2; x < aSlotXFinish2; x += 18) {
    	            addSlotToContainer(new ViewingSlot(mTileEntity, aSlotID++, x, y));
        		}
    		} 
    	}
    	else if (mTotalSlotCount == 18) {  
    		int aSlotYStart = 20;
    		int aSlotYFinish = aSlotYStart + (18*3);
    		int aSlotXStart1 = 26;
    		int aSlotXFinish1 = aSlotXStart1 + (18*3);
    		int aSlotXStart2 = 98;
    		int aSlotXFinish2 = aSlotXStart2 + (18*3);    		
    		int aSlotID = 0;
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart1; x < aSlotXFinish1; x += 18) {
    	            addSlotToContainer(new Slot(mTileEntity, aSlotID++, x, y));
        		}
    		}
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart2; x < aSlotXFinish2; x += 18) {
    	            addSlotToContainer(new ViewingSlot(mTileEntity, aSlotID++, x, y));
        		}
    		}     		
    	}
    	else if (mTotalSlotCount == 32) {  
    		int aSlotYStart = 8;
    		int aSlotYFinish = aSlotYStart + (18*4);
    		int aSlotXStart1 = 8;
    		int aSlotXFinish1 = aSlotXStart1 + (18*4);
    		int aSlotXStart2 = 97;
    		int aSlotXFinish2 = aSlotXStart2 + (18*4);    		
    		int aSlotID = 0;
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart1; x < aSlotXFinish1; x += 18) {
    	            addSlotToContainer(new Slot(mTileEntity, aSlotID++, x, y));
        		}
    		}
    		for (int y = aSlotYStart; y < aSlotYFinish; y += 18) {
    			for (int x = aSlotXStart2; x < aSlotXFinish2; x += 18) {
    	            addSlotToContainer(new ViewingSlot(mTileEntity, aSlotID++, x, y));
        		}
    		}    		
    	}        
    }

    @Override
    public int getSlotCount() {
        return mTotalSlotCount;
    }

    @Override
    public int getShiftClickSlotCount() {
        return mInputslotCount;
    }
    
    /*
     * Uselss stuff copied from GT
     */
    

    public int mActive = 0, mMaxProgressTime = 0, mProgressTime = 0, mEnergy = 0, mSteam = 0, mSteamStorage = 0, mStorage = 0, mOutput = 0, mInput = 0, mID = 0, mDisplayErrorCode = 0;
    private int oActive = 0, oMaxProgressTime = 0, oProgressTime = 0, oEnergy = 0, oSteam = 0, oSteamStorage = 0, oStorage = 0, oOutput = 0, oInput = 0, oID = 0, oDisplayErrorCode = 0, mTimer = 0;

    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
        mStorage = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getEUCapacity());
        mEnergy = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getStoredEU());
        mSteamStorage = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getSteamCapacity());
        mSteam = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getStoredSteam());
        mOutput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getOutputVoltage());
        mInput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getInputVoltage());
        mDisplayErrorCode = mTileEntity.getErrorDisplayID();
        mProgressTime = mTileEntity.getProgress();
        mMaxProgressTime = mTileEntity.getMaxProgress();
        mActive = mTileEntity.isActive() ? 1 : 0;
        mTimer++;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            if (mTimer % 500 == 10 || oEnergy != mEnergy) {
                var1.sendProgressBarUpdate(this, 0, mEnergy & 65535);
                var1.sendProgressBarUpdate(this, 1, mEnergy >>> 16);
            }
            if (mTimer % 500 == 10 || oStorage != mStorage) {
                var1.sendProgressBarUpdate(this, 2, mStorage & 65535);
                var1.sendProgressBarUpdate(this, 3, mStorage >>> 16);
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
        oEnergy = mEnergy;
        oStorage = mStorage;
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
            case 0:
                mEnergy = mEnergy & -65536 | par2;
                break;
            case 1:
                mEnergy = mEnergy & 65535 | par2 << 16;
                break;
            case 2:
                mStorage = mStorage & -65536 | par2;
                break;
            case 3:
                mStorage = mStorage & 65535 | par2 << 16;
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
    
    private static class ViewingSlot extends SlotNoInput {

		public ViewingSlot(IInventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemstack) {
			return false;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

		@Override
		public boolean canTakeStack(EntityPlayer p_82869_1_) {
			return true;
		}
    	
    }
}
