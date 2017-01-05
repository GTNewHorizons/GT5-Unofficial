package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaTreeFarmerBase;
import net.minecraft.nbt.NBTTagCompound;

public class GregtechMetaTreeFarmerStructural
        extends GregtechMetaTreeFarmerBase {
	
	@Override
	public String[] getDescription() {
		return new String[] {mDescription, CORE.GT_Tooltip};
	}
	
    public GregtechMetaTreeFarmerStructural(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Structural Blocks for the Tree Farmer.");
    }

    public GregtechMetaTreeFarmerStructural(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GregtechMetaTreeFarmerStructural(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTreeFarmerStructural(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
    }

    @Override
	public ITexture getOverlayIcon() {
        return new GT_RenderedTexture(Textures.BlockIcons.VOID);
    }

    @Override
	public boolean isValidSlot(int aIndex) {
        return false;
    }

	@Override
	public void saveNBTData(NBTTagCompound paramNBTTagCompound) {		
	}

	@Override
	public void loadNBTData(NBTTagCompound paramNBTTagCompound) {		
	}
}
