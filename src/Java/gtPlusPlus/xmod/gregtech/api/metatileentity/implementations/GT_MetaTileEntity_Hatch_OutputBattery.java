package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.charge.CONTAINER_Electric_2by2;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.charge.CONTAINER_Electric_4by4;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.charge.GUI_Electric_2by2;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.charge.GUI_Electric_4by4;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Hatch_OutputBattery
extends
GT_MetaTileEntity_Hatch {
	public GT_MetaTileEntity_Hatch_OutputBattery(int aID, String aName,
			String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, getSlots(aTier),
				new String[]{"Dischargeable Item Bus for Multiblocks",
						"Capacity: " + getSlots(aTier) + " stack"
								+ (getSlots(aTier) >= 2 ? "s" : "")});
	}

	public GT_MetaTileEntity_Hatch_OutputBattery(String aName, int aTier,
			String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier,
				aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16,
						aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_OutputBattery(String aName, int aTier,
			String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier,
				aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16,
						aDescription, aTextures);
	}

	@Override
	public boolean isEnetOutput() {
		return true;
	}
	
	@Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUOutput() {
		return V[mTier];
	}

	@Override
	public long maxEUStore() {
		return 512 + V[mTier + 1] * 8;
	}

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Discharger)};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Discharger)};
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
		return new GT_MetaTileEntity_Hatch_OutputBattery(mName, mTier,
				mDescriptionArray, mTextures);
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
						aBaseMetaTileEntity, "Discharging Bus");
			case 4 :
				return new GUI_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity, "Discharging Bus");
			default :
				return new GUI_Electric_4by4(aPlayerInventory,
						aBaseMetaTileEntity, "Discharging Bus");
		}
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity,
			int aIndex, byte aSide, ItemStack aStack) {
		return aSide == aBaseMetaTileEntity.getFrontFacing();
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity,
			int aIndex, byte aSide, ItemStack aStack) {
		return aSide == aBaseMetaTileEntity.getFrontFacing();
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

	public int rechargerSlotStartIndex() {
		return 0;
	}

	public int rechargerSlotCount() {
		return 0;
	}

	public int dechargerSlotStartIndex() {
		return 0;
	}

	public int dechargerSlotCount() {
		return mTier == 2 ? 4 : 16;
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
				if (mMetaTileEntity.dechargerSlotCount() > 0 && mMetaTileEntity.getEUVar() < aBaseMetaTileEntity.getEUCapacity()) {
					Utils.LOG_INFO("3");
					for (int i = mMetaTileEntity.dechargerSlotStartIndex(), k = mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
						if (mMetaTileEntity.mInventory[i] != null && mMetaTileEntity.getEUVar() < aBaseMetaTileEntity.getEUCapacity()) {
							aBaseMetaTileEntity.increaseStoredEnergyUnits(GT_ModHandler.dischargeElectricItem(mMetaTileEntity.mInventory[i], (int) Math.min(V[mTier] * 15, aBaseMetaTileEntity.getEUCapacity() - aBaseMetaTileEntity.getStoredEU()), (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()), true, false, false), true);
							if (mMetaTileEntity.mInventory[i].stackSize <= 0)
								mMetaTileEntity.mInventory[i] = null;
						}
					}
				}
			}
		}
		super.onPostTick(aBaseMetaTileEntity, aTimer);
	}
	
	

}
