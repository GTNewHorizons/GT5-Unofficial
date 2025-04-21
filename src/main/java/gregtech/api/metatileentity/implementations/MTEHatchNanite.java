package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;

public class MTEHatchNanite extends MTEHatchNonConsumableBase {

    private int naniteCapacity = 0;

    public MTEHatchNanite(int ID, String name, String nameRegional, int tier, int itemCapacity) {
        super(ID, name, nameRegional, tier, "Holds nanites for use in multiblocks");
        naniteCapacity = itemCapacity;
    }

    public MTEHatchNanite(String name, String[] description, ITexture[][][] textures, int tier, int itemCapacity) {
        super(name, tier, description, textures);
        naniteCapacity = itemCapacity;
    }

    @Override
    public int getItemCapacity() {
        return naniteCapacity;
    }

    public Materials getStoredNaniteMaterial() {
        if (getItemStack() == null) return null;
        ItemData data = GTOreDictUnificator.getAssociation(getItemStack());
        if (data == null) return null;
        return data.mMaterial.mMaterial;
    }

    @Override
    public boolean isValidItem(ItemStack item) {
        ItemData data = GTOreDictUnificator.getAssociation(item);
        if (data == null) {
            return false;
        }
        OrePrefixes prefix = data.mPrefix;
        return prefix == OrePrefixes.nanite;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_NANITE_HATCH)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_NANITE_HATCH_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_NANITE_HATCH)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_NANITE_HATCH_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchNanite(mName, mDescriptionArray, mTextures, mTier, naniteCapacity);
    }
}
