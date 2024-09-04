package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import static gregtech.api.enums.GTValues.V;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;

public class MTENeutronAccelerator extends MTEHatchEnergy {

    public MTENeutronAccelerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTENeutronAccelerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public int getMaxEUConsume() {
        return (int) (V[mTier] * 8 / 10);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENeutronAccelerator(mName, mTier, this.getDescription(), mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Input EU to Accelerate the Neutron!", "Max EU input: " + this.maxEUInput(),
            "Max EU consumption: " + this.getMaxEUConsume(),
            "Every EU can be transformed into 10~20 eV Neutron Kinetic Energy." };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getStoredEU() >= getMaxEUConsume() && aBaseMetaTileEntity.isAllowedToWork()) {
                setEUVar(aBaseMetaTileEntity.getStoredEU() - getMaxEUConsume());
                aBaseMetaTileEntity.setActive(true);
            } else {
                aBaseMetaTileEntity.setActive(false);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
}
