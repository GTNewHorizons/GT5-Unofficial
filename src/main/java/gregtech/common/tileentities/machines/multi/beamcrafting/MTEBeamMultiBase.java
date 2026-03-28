package gregtech.common.tileentities.machines.multi.beamcrafting;

import java.util.ArrayList;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public abstract class MTEBeamMultiBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    protected MTEBeamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    public final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();
    public final ArrayList<MTEHatchAdvancedOutputBeamline> mAdvancedOutputBeamline = new ArrayList<>();

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

    public enum FundamentalForce {
        EM,
        Weak,
        Strong,
        Gravity,
        All
    }

    public boolean addAdvancedBeamlineOutputHatch(IGregTechTileEntity te, int casingIndex, FundamentalForce forceType) {
        // 0 = EM; 1 = Weak; 2 = Strong; 3 = Gravity; 4 = All
        // might be nice to add combinations of output sets from various forces. currently not needed
        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            switch (forceType) {
                case EM:
                    hatch.setInitialParticleList(LHCModule.EM.acceptedParticles);
                case Weak:
                    hatch.setInitialParticleList(LHCModule.Weak.acceptedParticles);
                case Strong:
                    hatch.setInitialParticleList(LHCModule.Strong.acceptedParticles);
                case Gravity:
                    hatch.setInitialParticleList(LHCModule.Grav.acceptedParticles);
                case All:
                    hatch.setInitialParticleList(LHCModule.AllParticles.acceptedParticles);
            }
            this.mAdvancedOutputBeamline.add(hatch);
            return true;
        }
        return false;
    }

    public BeamInformation getNthInputParticle(int n) {
        if (this.mInputBeamline.isEmpty()) return new BeamInformation(0, 0, 0, 0);
        MTEHatchInputBeamline in = this.mInputBeamline.get(n);
        if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
        return in.dataPacket.getContent();
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // cannot have maintenance issues, so do nothing
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

}
