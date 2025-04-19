package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SourceChamberMetadata {

    public final int particleID;
    public final int rate;
    public final float maxEnergy;
    public final float focus;
    public final float energyRatio;

    private SourceChamberMetadata(int particleID, int rate, float maxEnergy, float focus, float energyRatio) {
        this.particleID = particleID;
        this.rate = rate;
        this.maxEnergy = maxEnergy;
        this.focus = focus;
        this.energyRatio = energyRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particleID, rate, maxEnergy, focus, energyRatio);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceChamberMetadata that = (SourceChamberMetadata) o;

        if (particleID != that.particleID) return false;
        if (rate != that.rate) return false;
        if (maxEnergy != that.maxEnergy) return false;
        if (focus != that.focus) return false;
        return energyRatio == that.energyRatio;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("particleID", particleID)
            .append("rate", rate)
            .append("maxEnergy", maxEnergy)
            .append("focus", focus)
            .append("energyRatio", energyRatio)
            .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int particleID;
        private int rate;
        private float maxEnergy;
        private float focus;
        private float energyRatio;

        private Builder() {}

        public Builder particleID(int particleID) {
            this.particleID = particleID;
            return this;
        }

        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }

        public Builder energy(float maxEnergy, float energyRatio) {
            this.maxEnergy = maxEnergy;
            this.energyRatio = energyRatio;
            return this;
        }

        public Builder focus(float focus) {
            this.focus = focus;
            return this;
        }

        public SourceChamberMetadata build() {
            return new SourceChamberMetadata(particleID, rate, maxEnergy, focus, energyRatio);
        }
    }
}
