package gregtech.common.tileentities.machines.multi.LHC;

import com.google.common.collect.ImmutableList;
import gtnhlanth.common.beamline.Particle;

public enum LHCModules {

    EM("EM",ImmutableList.of(
        Particle.PHOTON,
        Particle.WBOSON,
        Particle.ELECTRON,
        Particle.MUON,
        Particle.TAU,
        Particle.PROTON,
        Particle.OMEGA
    )),
    Weak("Weak",ImmutableList.of(
        Particle.PHOTON,
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
        Particle.UPSILON
    )),
    Strong("Strong",ImmutableList.of(
        Particle.PROTON,
        Particle.NEUTRON,
        Particle.LAMBDA,
        Particle.OMEGA,
        Particle.ETA,
        Particle.JPSI,
        Particle.UPSILON
    )),
    Grav("Grav",ImmutableList.of(
        Particle.HIGGS,
        Particle.GRAVITON
    ));

    public final String structurePiece;
    public final ImmutableList<Particle> acceptedParticles;

    private LHCModules(String structurePiece, ImmutableList<Particle> acceptedParticles){
        this.structurePiece = structurePiece;
        this.acceptedParticles = acceptedParticles;
    }
}
