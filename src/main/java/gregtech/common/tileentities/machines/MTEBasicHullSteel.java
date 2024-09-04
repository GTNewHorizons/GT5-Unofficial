package gregtech.common.tileentities.machines;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicHullNonElectric;
import gregtech.api.render.TextureFactory;

public class MTEBasicHullSteel extends MTEBasicHullNonElectric {

    public MTEBasicHullSteel(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public MTEBasicHullSteel(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public MTEBasicHullSteel(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBasicHullSteel(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = {
                TextureFactory.of(Textures.BlockIcons.MACHINE_STEEL_BOTTOM, Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = {
                TextureFactory.of(Textures.BlockIcons.MACHINE_STEEL_TOP, Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = {
                TextureFactory.of(Textures.BlockIcons.MACHINE_STEEL_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
            rTextures[2][(i + 1)] = tmp2;
        }
        return rTextures;
    }
}
