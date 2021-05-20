package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.gui.GT_Container_4by4;
import gregtech.api.gui.GT_GUIContainer_4by4;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_ElementalDataOrbHolder extends GT_MetaTileEntity_Hatch {
	public GT_Recipe_Map mRecipeMap = null;
	public boolean disableSort;

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 16, new String[]{
				"Holds Data Orbs for the Elemental Duplicator",
				});
		disableSort = true;
	}

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 16, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 16, aDescription, aTextures);
	}

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Cyber_Interface)};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Cyber_Interface)};
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
		return new GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(mName, mTier, mDescriptionArray, mTextures);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) return true;
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, "Steam Input Bus");
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
			fillStacksIntoFirstSlots();
		}
	}

	public void updateSlots() {
		for (int i = 0; i < mInventory.length; i++)
			if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
		fillStacksIntoFirstSlots();
	}

	protected void fillStacksIntoFirstSlots() {
		if (disableSort) {
			for (int i = 0; i < mInventory.length; i++)
				if (mInventory[i] != null && mInventory[i].stackSize <= 0)
					mInventory[i] = null;
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("disableSort", disableSort);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		disableSort = aNBT.getBoolean("disableSort");
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		
	}

	public String trans(String aKey, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing() && (mRecipeMap == null || mRecipeMap.containsInput(aStack));
	}

}
