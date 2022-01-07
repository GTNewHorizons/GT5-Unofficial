package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.util.ArrayList;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_DataHatch;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_DataHatch;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_ElementalDataOrbHolder extends GT_MetaTileEntity_Hatch {
	public GT_Recipe_Map mRecipeMap = null;

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 17, new String[]{
				"Holds Data Orbs for the Elemental Duplicator",
				CORE.GT_Tooltip
		});
	}

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 17, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 17, aDescription, aTextures);
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
		return new CONTAINER_DataHatch(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_DataHatch(aPlayerInventory, aBaseMetaTileEntity, "Data Orb Repository");
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
			fillStacksIntoFirstSlots();
		}
	}

	public void updateSlots() {
		for (int i = 0; i < mInventory.length-1; i++)
			if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
		fillStacksIntoFirstSlots();
	}

	protected void fillStacksIntoFirstSlots() {
		for (int i = 0; i < mInventory.length-1; i++) {
			if (mInventory[i] != null && mInventory[i].stackSize <= 0) {
				mInventory[i] = null;				
			}
		}					
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {

	}

	public String trans(String aKey, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		Logger.INFO("Checking if we can pull "+aStack.getDisplayName()+" from slot "+aIndex);
		if (aIndex == mInventory.length-1 && ItemUtils.isControlCircuit(aStack) && aSide == getBaseMetaTileEntity().getFrontFacing()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		Logger.INFO("Checking if we can put "+aStack.getDisplayName()+" into slot "+aIndex);
		if (aIndex == mInventory.length-1 && ItemUtils.isControlCircuit(aStack) && aSide == getBaseMetaTileEntity().getFrontFacing()) {
		return true;
	}
	return false;
	}
	
	public ArrayList<ItemStack> getInventory(){
		ArrayList<ItemStack> aContents = new ArrayList<ItemStack>();
		for (int i = getBaseMetaTileEntity().getSizeInventory() - 2; i >= 0; i--) {
            if (getBaseMetaTileEntity().getStackInSlot(i) != null)
            	aContents.add(getBaseMetaTileEntity().getStackInSlot(i));
        } 
		return aContents;
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		if (aIndex == mInventory.length-1 && ItemUtils.isControlCircuit(aStack) && aSide == getBaseMetaTileEntity().getFrontFacing()) {
			Logger.INFO("Putting "+aStack.getDisplayName()+" into slot "+aIndex);
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		if (aIndex == mInventory.length-1 && ItemUtils.isControlCircuit(aStack)) {
			Logger.INFO("Pulling "+aStack.getDisplayName()+" from slot "+aIndex);
			return true;
		}
		return false;
	}

}
