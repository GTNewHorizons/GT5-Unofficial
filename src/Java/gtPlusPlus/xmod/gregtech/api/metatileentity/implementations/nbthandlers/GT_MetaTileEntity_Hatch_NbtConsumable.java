package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import java.lang.reflect.Constructor;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_HatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_HatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class GT_MetaTileEntity_Hatch_NbtConsumable extends GT_MetaTileEntity_Hatch {

	public GT_Recipe_Map mRecipeMap = null;
	private final int mInputslotCount;
	private final int mTotalSlotCount;	
	private final boolean mAllowDuplicateUsageTypes;

	public GT_MetaTileEntity_Hatch_NbtConsumable(int aID, String aName, String aNameRegional, int aTier, int aInputSlots, String aDescription, boolean aAllowDuplicateTypes) {
		super(aID, aName, aNameRegional, aTier, aInputSlots*2, aDescription);
		mInputslotCount = getInputSlotCount();
		mTotalSlotCount = getInputSlotCount()*2;
		mAllowDuplicateUsageTypes = aAllowDuplicateTypes;
	}

	public GT_MetaTileEntity_Hatch_NbtConsumable(String aName, int aTier, int aInputSlots, String aDescription, boolean aAllowDuplicateTypes, ITexture[][][] aTextures) {
		super(aName, aTier, aInputSlots*2, aDescription, aTextures);
		mInputslotCount = getInputSlotCount();
		mTotalSlotCount = getInputSlotCount()*2;
		mAllowDuplicateUsageTypes = aAllowDuplicateTypes;
	}

	public GT_MetaTileEntity_Hatch_NbtConsumable(String aName, int aTier, int aInputSlots, String[] aDescription, boolean aAllowDuplicateTypes, ITexture[][][] aTextures) {
		super(aName, aTier, aInputSlots*2, aDescription[0], aTextures);
		mInputslotCount = getInputSlotCount();
		mTotalSlotCount = getInputSlotCount()*2;
		mAllowDuplicateUsageTypes = aAllowDuplicateTypes;
	}

	@Override
	public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

	@Override
	public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

	public abstract int getInputSlotCount();
	
	@Override
	public final boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public final boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public final boolean isValidSlot(int aIndex) {
		return aIndex < mInputslotCount;
	}

	@Override
	public final MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {    	
		Constructor<?> aConstructor = ReflectionUtils.getConstructor(getHatchEntityClass(), new Class[] {String.class, String[].class, ITexture[][][].class});
		GT_MetaTileEntity_Hatch_NbtConsumable aInstance = ReflectionUtils.createNewInstanceFromConstructor(aConstructor, new Object[] {mName, StaticFields59.getDescriptionArray(this), mTextures});
		if (aInstance instanceof GT_MetaTileEntity_Hatch_NbtConsumable) {
			GT_MetaTileEntity_Hatch_NbtConsumable aMetaTile = (GT_MetaTileEntity_Hatch_NbtConsumable) aInstance;
			return aMetaTile;
		}
		return null;
	}

	public abstract Class<? extends GT_MetaTileEntity_Hatch_NbtConsumable> getHatchEntityClass();

	@Override
	public final boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public final Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_HatchNbtConsumable(aPlayerInventory, aBaseMetaTileEntity, mInputslotCount);
	}

	@Override
	public final Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		CONTAINER_HatchNbtConsumable aContainer = new CONTAINER_HatchNbtConsumable(aPlayerInventory, aBaseMetaTileEntity, mInputslotCount);
		return new GUI_HatchNbtConsumable(aContainer, getNameGUI());
	}

	public abstract String getNameGUI();

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
			fillStacksIntoFirstSlots();
			tryFillUsageSlots();
		}
	}

	public final void updateSlots() {
		for (int i = 0; i < mInventory.length; i++) {
			if (mInventory[i] != null && mInventory[i].stackSize <= 0) {
				mInventory[i] = null;
			}
			// Only moves items in the first four slots
			if (i <= getSlotID_LastInput()) {
				fillStacksIntoFirstSlots();
			}
		}            
	}

	// Only moves items in the first four slots
	protected final void fillStacksIntoFirstSlots() {
		for (int i = 0; i <= getSlotID_LastInput() ; i++) {
			for (int j = i + 1; j <= getSlotID_LastInput(); j++) {
				if (mInventory[j] != null && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j]))) {
					GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), j, i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
				}
			}
		}                
	}
	
	private final void tryFillUsageSlots() {
		int aSlotSpace = (mInputslotCount - getContentUsageSlots().size());
		if (aSlotSpace > 0) {
			Logger.INFO("We have empty usage slots. "+aSlotSpace);
			for (int i=getSlotID_FirstInput();i<=getSlotID_LastInput();i++) {
				ItemStack aStackToTryMove = mInventory[i];
				if (aStackToTryMove != null && isItemValidForUsageSlot(aStackToTryMove)) {
					Logger.INFO("Trying to move stack from input slot "+i);
					if (moveItemFromStockToUsageSlots(aStackToTryMove)) {
						Logger.INFO("Updating Slots.");
						updateSlots();
					}
				}
			}
		}
	}

	private int getSlotID_FirstInput() {
		return 0;
	}
	private int getSlotID_LastInput() {
		return mInputslotCount-1;
	}
	private int getSlotID_FirstUsage() {
		return mInputslotCount;
	}
	private int getSlotID_LastUsage() {
		return mTotalSlotCount-1;
	}
	
	
	public final AutoMap<ItemStack> getContentUsageSlots() {
		AutoMap<ItemStack> aItems = new AutoMap<ItemStack>();
		for (int i=mInputslotCount;i<mTotalSlotCount;i++) {
			if (mInventory[i] != null) {
				aItems.add(mInventory[i]);
			}
		}
		return aItems;
	}


	public final boolean moveItemFromStockToUsageSlots(ItemStack aStack) {
		return moveItemFromStockToUsageSlots(aStack, mAllowDuplicateUsageTypes);
	}
	
	public final boolean moveItemFromStockToUsageSlots(ItemStack aStack, boolean aAllowMultiOfSameTypeInUsageSlots) {
		if (aStack != null) {
			if (aStack.stackSize > 0) {
				
				if (!isItemValidForUsageSlot(aStack)) {
					Logger.INFO("Stack not valid: "+ItemUtils.getItemName(aStack));
					return false;
				}
				
				// Copy the input stack into a new object
				ItemStack aStackToMove = aStack.copy(); 
				// Set stack size of stack to move to 1.
				aStackToMove.stackSize = 1;    		
				// Did we set a stack in the usage slots?
				boolean aDidSet = false;
				// Did we find another of this item already in the usage slots?
				boolean aFoundMatching = false;
				// Continue processing with our new stack
				// First check for duplicates
				for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
					if (mInventory[i] != null) {
						if (GT_Utility.areStacksEqual(aStackToMove, mInventory[i], true)) {
							Logger.INFO("Found matching stack in slot "+i+".");
							aFoundMatching = true;
							break;
						}
					}  	  
				} 
				// Then Move stack to Usage slots
				for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
					if (mInventory[i] == null) {
						if ((aFoundMatching && aAllowMultiOfSameTypeInUsageSlots) || !aFoundMatching) {
							mInventory[i] = aStackToMove;
							aDidSet = true;
							Logger.INFO("Moving new stack to usage slots.");
							break;							
						}
					}    	  
				} 
				if (aDidSet) {
					Logger.INFO("Depleting input stack size by 1.");
					// Depleted one from the original input stack
					aStack.stackSize--;
				}
				return aDidSet;
			}
		}
		return false;
	}

	@Override
	public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing() && isItemValidForUsageSlot(aStack);
	}
	
	/**
	 * Items that get compared when checking for Usage Slot validity.
	 * Can return an empty map if isItemValidForUsageSlot() is overridden.
	 * @return
	 */
	public abstract AutoMap<ItemStack> getItemsValidForUsageSlots();
	
	/**
	 * Checks if the given item is valid for Usage Slots.
	 * Can be overridden for easier handling if you already have methods to check this.
	 * @param aStack
	 * @return
	 */
	public boolean isItemValidForUsageSlot(ItemStack aStack) {
		if (aStack != null) {
			for (ItemStack aValid : getItemsValidForUsageSlots()) {
				if (GT_Utility.areStacksEqual(aStack, aValid, true)) {
					return true;
				}
			}
		}
		return false;
	}
}
