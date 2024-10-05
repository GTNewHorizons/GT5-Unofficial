package gtnhlanth.common.beamline;

public class BeamInformation {

    private final float energy; // in keV
    private final int rate;

    private final Particle particle;
    private final int particleId;

    private final float focus;

    public BeamInformation(float energy, int rate, int particleId, float focus) {
        this.energy = energy;
        this.rate = rate;
        this.particleId = particleId;
        this.particle = Particle.values()[particleId];
        this.focus = focus;
    }

    public float getEnergy() {
        return this.energy;
    }

    public int getRate() {
        return this.rate;
    }

    public Particle getParticle() {
        return this.particle;
    }

    public int getParticleId() {
        return this.particleId;
    }

    public float getFocus() {
        return this.focus;
    }

    @Override
    public String toString() {
        return "Energy=" + this.getEnergy()
            + ",Rate="
            + this.getRate()
            + ",Particle="
            + this.getParticleId()
            + ",Focus="
            + this.getFocus();
    }
}
