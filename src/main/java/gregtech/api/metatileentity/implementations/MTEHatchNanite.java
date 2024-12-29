package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING_GLOW;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

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
    protected int getItemCapacity() {
        return naniteCapacity;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchNanite(mName, mDescriptionArray, mTextures, mTier, naniteCapacity);
    }
}
