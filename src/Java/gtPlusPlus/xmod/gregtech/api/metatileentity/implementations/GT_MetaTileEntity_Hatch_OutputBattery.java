package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
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
}
