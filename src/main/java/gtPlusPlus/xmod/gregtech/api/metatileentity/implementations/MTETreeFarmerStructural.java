package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.MTETreeFarmerBase;

public class MTETreeFarmerStructural extends MTETreeFarmerBase {

    public MTETreeFarmerStructural(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Structural Blocks for the Tree Farmer.");
    }

    public MTETreeFarmerStructural(final String aName, final int aTier, final int aInvSlotCount,
        final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTETreeFarmerStructural(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return new GTRenderedTexture(Textures.BlockIcons.VOID);
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return false;
    }

    @Override
    public void saveNBTData(final NBTTagCompound paramNBTTagCompound) {}

    @Override
    public void loadNBTData(final NBTTagCompound paramNBTTagCompound) {}
}
