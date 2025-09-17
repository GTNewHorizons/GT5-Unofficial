package gregtech.common.tileentities.machines.multi.LHC;

import com.google.common.collect.ImmutableList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

import java.util.List;

public class MTEHatchAdvancedOutputBeamline extends MTEHatchOutputBeamline {

    public MTEHatchAdvancedOutputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
    }

    public MTEHatchAdvancedOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEHatchAdvancedOutputBeamline(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean hasBlacklist() {
        return true;
    }

    @Override
    public List<Integer> getBlacklist() {
        return ImmutableList.of(Particle.PHOTON.getId());
    }


}
