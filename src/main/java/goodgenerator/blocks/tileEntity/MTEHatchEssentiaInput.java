package goodgenerator.blocks.tileEntity;

import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.EssentiaInputHatch;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEHatchEssentiaInput extends MTEHatchEssentiaBase implements EssentiaInputHatch {

    public MTEHatchEssentiaInput(int id, String name, String nameRegional) {
        super(id, name, nameRegional, VoltageIndex.EV, new String[] { "Essentia input hatch for multiblocks." });
    }

    protected MTEHatchEssentiaInput(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, description, textures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchEssentiaInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean canMixEssentia() {
        return false;
    }

    @Override
    public boolean isEssentiaOutput() {
        return false;
    }

    @Override
    public boolean isEssentiaInput() {
        return true;
    }
}
