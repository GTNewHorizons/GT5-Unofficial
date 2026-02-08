package gregtech.common.tileentities.machines.multi.beamcrafting;

import com.google.common.collect.ImmutableList;

import gtnhlanth.common.beamline.Particle;

public enum LHCModule {

    EM(
        ImmutableList.of(
            Particle.PHOTON,
            Particle.WBOSON,
            Particle.ELECTRON,
            Particle.MUON,
            Particle.TAU,
            Particle.PROTON,
            Particle.OMEGA)),
    Weak(
        ImmutableList.of(
            Particle.ZBOSON,
            Particle.WBOSON,
            Particle.HIGGS,
            Particle.ELECTRON,
            Particle.MUON,
            Particle.TAU,
            Particle.ELECTRONNEUTRINO,
            Particle.MUONNEUTRINO,
            Particle.TAUNEUTRINO,
            Particle.PROTON,
            Particle.NEUTRON,
            Particle.LAMBDA,
            Particle.OMEGA,
            Particle.ETA,
            Particle.JPSI,
            Particle.UPSILON)),
    Strong(
        ImmutableList.of(
            Particle.PROTON,
            Particle.NEUTRON,
            Particle.LAMBDA,
            Particle.OMEGA,
            Particle.ETA,
            Particle.JPSI,
            Particle.UPSILON)),
    Grav(ImmutableList.of(Particle.HIGGS, Particle.GRAVITON)),
    AllParticles(
        ImmutableList.of(
            Particle.PHOTON,
            Particle.WBOSON,
            Particle.ELECTRON,
            Particle.MUON,
            Particle.TAU,
            Particle.PROTON,
            Particle.OMEGA,
            Particle.ZBOSON,
            Particle.HIGGS,
            Particle.ELECTRONNEUTRINO,
            Particle.MUONNEUTRINO,
            Particle.TAUNEUTRINO,
            Particle.NEUTRON,
            Particle.LAMBDA,
            Particle.ETA,
            Particle.JPSI,
            Particle.UPSILON,
            Particle.GRAVITON));

    public final ImmutableList<Particle> acceptedParticles;

    LHCModule(ImmutableList<Particle> acceptedParticles) {
        this.acceptedParticles = acceptedParticles;
    }
}
