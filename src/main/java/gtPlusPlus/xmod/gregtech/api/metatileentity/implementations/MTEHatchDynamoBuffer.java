package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchDynamoBuffer extends MTEHatchDynamo {

    public MTEHatchDynamoBuffer(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDynamoBuffer(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier + 1] };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier + 1] };
    }

    @Override
    public long getMinimumStoredEU() {
        return 0L;
    }

    @Override
    public long maxEUStore() {
        return 512L + GTValues.V[this.mTier + 1] * 2048L;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchDynamoBuffer(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        String[] g;
        g = new String[] { "Dynamo with internal storage and additional Amp capacity",
            "Does not accept more than " + (this.maxEUOutput() * this.maxAmperesIn()) + "EU/t as input",
            GTPPCore.GT_Tooltip.get() };

        return g;
    }

    @Override
    public long maxAmperesIn() {
        return 4;
    }

    @Override
    public long maxAmperesOut() {
        return 4;
    }
}
