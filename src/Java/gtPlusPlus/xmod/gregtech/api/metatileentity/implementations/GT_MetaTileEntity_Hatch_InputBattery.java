package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.charge.*;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_InputBattery
extends
GT_MetaTileEntity_Hatch {
	public final GT_Recipe_Map mRecipeMap = null;

	public GT_MetaTileEntity_Hatch_InputBattery(int aID, String aName,
			String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, getSlots(aTier), "Chargeable Item Bus for Multiblocks");
	}

	public GT_MetaTileEntity_Hatch_InputBattery(String aName, int aTier,
			String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier,
				aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 4 : 16,
						aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {		
		int mSlots = 0;
		if (this.mTier == 2) {
			mSlots = 4;
		}
		else if (this.mTier == 4) {
			mSlots = 16;
		}
		else {
			mSlots = 16;
		}		
		return new String[]{
				this.mDescription,
				"Capacity: " + mSlots + " slots"};
	}

	@Override
    public boolean isEnetInput() {
        return true;
    }

	@Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }


    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier + 1] * 16;
    }

    @Override
    public long maxAmperesIn() {
        return 4;
    }
	
	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Charger)};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Charger)};
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return true;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_InputBattery(mName, mTier,
				mDescription, mTextures);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity,
			EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
			return true;
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mTier) {
			case 2 :
				return new CONTAINER_Electric_2by2(aPlayerInventory,
						aBaseMetaTileEntity);
			case 4 :
				return new CONTAINER_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity);
			default :
				return new CONTAINER_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity);
		}
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mTier) {
			case 2 :
				return new GUI_Electric_2by2(aPlayerInventory,
						aBaseMetaTileEntity, "Charging Bus");
			case 4 :
				return new GUI_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity, "Charging Bus");
			default :
				return new GUI_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity, "Charging Bus");
		}
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity,	long aTimer) {
		if (aBaseMetaTileEntity.isServerSide()
				&& aBaseMetaTileEntity.hasInventoryBeenModified()) {
			fillStacksIntoFirstSlots();
		}
		if (aBaseMetaTileEntity.isServerSide()){
			if (aBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity) {
				MetaTileEntity mMetaTileEntity = (MetaTileEntity) aBaseMetaTileEntity.getMetaTileEntity();				
				if (mMetaTileEntity.rechargerSlotCount() > 0 && aBaseMetaTileEntity.getStoredEU() > 0) {
					for (int i = mMetaTileEntity.rechargerSlotStartIndex(), k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
						if (aBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
							for (int u=0;u<10;u++){
								aBaseMetaTileEntity.decreaseStoredEnergyUnits(GT_ModHandler.chargeElectricItem(mMetaTileEntity.mInventory[i], (int) Math.min(V[this.mTier] * 15, aBaseMetaTileEntity.getStoredEU()), (int) Math.min(Integer.MAX_VALUE, GT_Values.V[u]), false, false), true);
								if (mMetaTileEntity.mInventory[i].stackSize <= 0){
									mMetaTileEntity.mInventory[i] = null;
								}
							}							
						}
					}
				}
				else {
					//Utils.LOG_INFO("reCharger Slot Count = "+mMetaTileEntity.rechargerSlotCount());
					//Utils.LOG_INFO("getStoredEU = "+aBaseMetaTileEntity.getStoredEU());
					//Utils.LOG_INFO("getEUVar = "+mMetaTileEntity.getEUVar());
				}
			}
		}
		super.onPostTick(aBaseMetaTileEntity, aTimer);
	}

	public void updateSlots() {
		for (int i = 0; i < mInventory.length; i++)
			if (mInventory[i] != null && mInventory[i].stackSize <= 0)
				mInventory[i] = null;
		fillStacksIntoFirstSlots();
	}

	protected void fillStacksIntoFirstSlots() {
		for (int i = 0; i < mInventory.length; i++)
			for (int j = i + 1; j < mInventory.length; j++)
				if (mInventory[j] != null
				&& (mInventory[i] == null || GT_Utility.areStacksEqual(
						mInventory[i], mInventory[j]))) {
					GT_Utility.moveStackFromSlotAToSlotB(
							getBaseMetaTileEntity(), getBaseMetaTileEntity(), j,
							i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
				}
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity,
			int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing()
				&& (mRecipeMap == null || mRecipeMap.containsInput(aStack));
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity,
			int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing()
				&& (mRecipeMap == null || mRecipeMap.containsInput(aStack));
	}

	@Override
	public int rechargerSlotStartIndex() {
		return 0;
	}

	@Override
	public int rechargerSlotCount() {
		switch (mTier) {
			case 2 :
				return 4;
			case 4 :
				return 16;
			default :
				return 16;
		}
	}

	@Override
	public int dechargerSlotStartIndex() {
		return 0;
	}

	@Override
	public int dechargerSlotCount() {
		return 0;
	}
}
