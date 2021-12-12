package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaTreeFarmerBase;

public class GregtechMetaTreeFarmerStructural
extends GregtechMetaTreeFarmerBase {

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription};
	}

	public GregtechMetaTreeFarmerStructural(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 0, "Structural Blocks for the Tree Farmer.");
	}

	public GregtechMetaTreeFarmerStructural(final int aID, final String aName, final String aNameRegional, final int aTier, final int aInvSlotCount, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GregtechMetaTreeFarmerStructural(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTreeFarmerStructural(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
	}

	@Override
	public ITexture getOverlayIcon() {
		return new GT_RenderedTexture(Textures.BlockIcons.VOID);
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return false;
	}

	@Override
	public void saveNBTData(final NBTTagCompound paramNBTTagCompound) {
	}

	@Override
	public void loadNBTData(final NBTTagCompound paramNBTTagCompound) {
	}
}
