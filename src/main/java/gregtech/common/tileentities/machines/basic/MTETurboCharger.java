package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by danie_000 on 15.10.2016.
 */
public class MTETurboCharger extends MTECharger {

    public MTETurboCharger(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    public MTETurboCharger(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETurboCharger(mName, mTier, mDescriptionArray, mTextures, mInventory.length);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1] };
            rTextures[1][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
        }
        return rTextures;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 1536L * mInventory.length;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 6144L * mInventory.length;
    }

    @Override
    public long maxAmperesIn() {
        return 16L * mInventory.length;
    }

    @Override
    public long maxAmperesOut() {
        return 4L * mInventory.length;
    }

    @Override
    protected boolean forceCharge() {
        assert getBaseMetaTileEntity() != null;
        return !getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    protected long getTransferMultiplier() {
        return 120;
    }
}
