package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_ThreadedSuperBuffer;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_ThreadedSuperBuffer;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.ITexture;

public class GT_MetaTileEntity_ThreadedSuperBuffer extends GT_MetaTileEntity_ThreadedChestBuffer {
	
	public GT_MetaTileEntity_ThreadedSuperBuffer(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier, 257, new String[]{
				"Server friendly",
				"Buffers up to 256 Item Stacks",
				"Use Screwdriver to regulate output stack size",
				"Consumes 1EU per moved Item",
				CORE.GT_Tooltip});
	}

	public GT_MetaTileEntity_ThreadedSuperBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_ThreadedSuperBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String[] aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GT_MetaTileEntity_ThreadedSuperBuffer(this.mName, this.mTier, this.mInventorySynchro.length,
				this.mDescriptionArray, this.mTextures);
	}

	public ITexture getOverlayIcon() {
		return (ITexture) new GT_RenderedTexture((IIconContainer) TexturesGtBlock.OVERLAY_AUTOMATION_SUPERBUFFER);
	}

	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_ThreadedSuperBuffer(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_ThreadedSuperBuffer(aPlayerInventory, aBaseMetaTileEntity);
	}
}