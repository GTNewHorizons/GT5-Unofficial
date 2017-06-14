package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SafeBlock;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SafeBlock;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaSafeBlockBase;
import net.minecraft.entity.player.InventoryPlayer;

public class GregtechMetaSafeBlock
extends GregtechMetaSafeBlockBase {

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, CORE.GT_Tooltip};
	}

	public GregtechMetaSafeBlock(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 28, "Protecting your items from sticky fingers.");
	}

	public GregtechMetaSafeBlock(final int aID, final String aName, final String aNameRegional, final int aTier, final int aInvSlotCount, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GregtechMetaSafeBlock(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaSafeBlock(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
	}

	@Override
	public ITexture getOverlayIcon() {
		return new GT_RenderedTexture(Textures.BlockIcons.VOID);
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return aIndex < (this.mInventory.length - 1);
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_SafeBlock(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SafeBlock(aPlayerInventory, aBaseMetaTileEntity);
	}
}
