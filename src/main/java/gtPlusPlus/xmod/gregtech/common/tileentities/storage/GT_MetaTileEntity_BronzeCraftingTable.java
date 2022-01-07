package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.workbench.GT_Container_BronzeWorkbench;
import gtPlusPlus.xmod.gregtech.api.gui.workbench.GT_GUIContainer_BronzeWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_MetaTileEntity_BronzeCraftingTable extends GT_MetaTileEntity_AdvancedCraftingTable {
	
	public GT_MetaTileEntity_BronzeCraftingTable(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aDescription);
	}

	public GT_MetaTileEntity_BronzeCraftingTable(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}
	
	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_BronzeWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_BronzeWorkbench(aPlayerInventory, aBaseMetaTileEntity, mLocalName);
	}
	
	@Override
	public boolean isTransformerUpgradable() {
		return false;
	}
	
	@Override
	public boolean isInputFacing(byte aSide) {
		return false;
	}
    
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_BronzeCraftingTable(this.mName, this.mTier, this.mDescription, this.mTextures);
	}
	
	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return GregTech_API.getCoverBehavior(aStack.toStack()).isSimpleCover();
	}

	@Override
    public int rechargerSlotStartIndex() {
    	return 0;
    }
	
	@Override
    public int rechargerSlotCount() {
    	return 0;
    }
	
	@Override
	public String[] getDescription() {
		return new String[] { 
				"For the smaller Projects",
				this.mDescription, 
				CORE.GT_Tooltip };
	}
	
	@Override
	public int getCapacity() {
		return 16000;
	}
	
	@Override
	protected boolean isAdvanced() {
		return false;
	}
}
