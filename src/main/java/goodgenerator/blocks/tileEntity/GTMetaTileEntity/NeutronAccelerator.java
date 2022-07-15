package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;

public class NeutronAccelerator extends GT_MetaTileEntity_Hatch_Energy {

    public NeutronAccelerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public NeutronAccelerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public int getMaxEUConsume() {
        return (int) (V[mTier] * 8 / 10);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new NeutronAccelerator(mName, mTier, this.getDescription(), mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Input EU to Accelerate the Neutron!",
            "Max EU input: " + this.maxEUInput(),
            "Max EU consumption: " + this.getMaxEUConsume(),
            "Every EU can be transformed into 10~20 eV Neutron Kinetic Energy."
        };
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
