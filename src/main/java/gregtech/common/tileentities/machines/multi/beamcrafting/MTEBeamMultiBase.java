package gregtech.common.tileentities.machines.multi.beamcrafting;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class MTEBeamMultiBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    protected MTEBeamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    public final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();
    public ArrayList<MTEHatchAdvancedOutputBeamline> mAdvancedOutputBeamline = new ArrayList<>();

    public MTEBeamMultiBase(String aName) {
        super(aName);
    }

    public boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    public boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }

    public boolean addAdvancedBeamlineOutputHatch(IGregTechTileEntity te, int casingIndex, int fundamentalForce){
        // 0 = EM; 1 = Weak; 2 = Strong; 3 = Gravity; 4 = All
        // might be nice to add combinations of output sets from various forces. currently not needed
        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            if (fundamentalForce == 0) {
                hatch.setInitialParticleList(LHCModule.EM.acceptedParticles);
            }
            if (fundamentalForce == 1) {
                hatch.setInitialParticleList(LHCModule.Weak.acceptedParticles);
            }
            if (fundamentalForce == 2) {
                hatch.setInitialParticleList(LHCModule.Strong.acceptedParticles);
            }
            if (fundamentalForce == 3) {
                hatch.setInitialParticleList(LHCModule.Grav.acceptedParticles);
            }
            if (fundamentalForce == 4) {
                hatch.setInitialParticleList(LHCModule.AllParticles.acceptedParticles);
            }
            this.mAdvancedOutputBeamline.add(hatch);
            return true;
        }
        return false;
    }

    @Nullable
    public BeamInformation getNthInputParticle(int n) {
        int i = 0;
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (i == n) {
                if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
                return in.dataPacket.getContent();
            }
            i += 1;
        }
        return null;
    }

}
