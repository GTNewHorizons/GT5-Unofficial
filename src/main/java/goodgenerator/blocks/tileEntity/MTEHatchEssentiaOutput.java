package goodgenerator.blocks.tileEntity;

import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.EssentiaOutputHatch;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEHatchEssentiaOutput extends MTEHatchEssentiaBase implements EssentiaOutputHatch {

    public MTEHatchEssentiaOutput(int id, String name, String nameRegional) {
        super(id, name, nameRegional, VoltageIndex.EV, new String[] { "Essentia output hatch for multiblocks." });
    }

    protected MTEHatchEssentiaOutput(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, description, textures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchEssentiaOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean canMixEssentia() {
        return false;
    }

    @Override
    public boolean isEssentiaOutput() {
        return true;
    }

    @Override
    public boolean isEssentiaInput() {
        return false;
    }
}
