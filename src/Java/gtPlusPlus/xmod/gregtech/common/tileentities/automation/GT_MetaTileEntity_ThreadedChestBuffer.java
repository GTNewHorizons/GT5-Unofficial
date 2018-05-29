package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import gregtech.common.gui.GT_GUIContainer_ChestBuffer;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ThreadedBuffer;

import gregtech.common.gui.GT_Container_ChestBuffer;
import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.ITexture;

public class GT_MetaTileEntity_ThreadedChestBuffer extends GT_MetaTileEntity_ThreadedBuffer {
	public GT_MetaTileEntity_ThreadedChestBuffer(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier, 28, new String[]{"Buffers up to 27 Item Stacks",
				"Use Screwdriver to regulate output stack size", "Consumes 1EU per moved Item"});
	}

	public GT_MetaTileEntity_ThreadedChestBuffer(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GT_MetaTileEntity_ThreadedChestBuffer(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String[] aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GT_MetaTileEntity_ThreadedChestBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_ThreadedChestBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String[] aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GT_MetaTileEntity_ThreadedChestBuffer(this.mName, this.mTier, this.mInventorySynchro.length,
				this.mDescriptionArray, this.mTextures);
	}

	public ITexture getOverlayIcon() {
		return (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.AUTOMATION_CHESTBUFFER);
	}

	public boolean isValidSlot(final int aIndex) {
		return aIndex < this.mInventorySynchro.length - 1;
	}

	protected void moveItems(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		this.fillStacksIntoFirstSlots();
		super.moveItems(aBaseMetaTileEntity, aTimer);
		this.fillStacksIntoFirstSlots();
	}

	protected synchronized void fillStacksIntoFirstSlots() {
		this.mLogicThread.fillStacksIntoFirstSlots(this);
	}

	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_ChestBuffer(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_ChestBuffer(aPlayerInventory, aBaseMetaTileEntity);
	}
}