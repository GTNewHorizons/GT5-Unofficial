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
            + Particle.getParticleFromId(this.getParticleId())
            + "("
            + this.getParticleId()
            + ")"
            + ",Focus="
            + this.getFocus();
    }

    public BeamInformation copy() {
        return new BeamInformation(this.energy, this.rate, this.particleId, this.focus);
    }

    public boolean isEqual(BeamInformation other) {
        return (this.energy == other.energy) && (this.rate == other.rate)
            && (this.particleId == other.particleId)
            && (this.focus == other.focus);
    }

}
