package gregtech.loaders.postload.recipes.beamcrafter;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import gtnhlanth.common.beamline.Particle;

public class BeamCrafterMetadata {

    public final int particleID_A;
    public final int particleID_B;
    public final int amount_A;
    public final int amount_B;
    public final float minEnergy_A;
    public final float minEnergy_B;

    private BeamCrafterMetadata(int particleID_A, int particleID_B, int amount_A, int amount_B, float minEnergy_A,
        float minEnergy_B) {
        this.particleID_A = particleID_A;
        this.particleID_B = particleID_B;
        this.amount_A = amount_A;
        this.amount_B = amount_B;
        this.minEnergy_A = minEnergy_A;
        this.minEnergy_B = minEnergy_B;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particleID_A, particleID_B, amount_A, amount_B, minEnergy_A, minEnergy_B);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeamCrafterMetadata that = (BeamCrafterMetadata) o;

        if (particleID_A != that.particleID_A) return false;
        if (particleID_B != that.particleID_B) return false;
        if (amount_A != that.amount_A) return false;
        if (amount_B != that.amount_B) return false;
        if (minEnergy_A != that.minEnergy_A) return false;
        if (minEnergy_B != that.minEnergy_B) return false;
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("particleID_A", particleID_A)
            .append("particleID_B", particleID_B)
            .append("amount_A", amount_A)
            .append("amount_B", amount_B)
            .append("minEnergy_A", minEnergy_A)
            .append("minEnergy_B", minEnergy_B)
            .toString();
    }

    public static class Builder {

        private int particleID_A;
        private int particleID_B;
        private int amount_A;
        private int amount_B;
        private float minEnergy_A;
        private float minEnergy_B;

        public Builder particleID_A(int particleID_A) {
            if (particleID_A < 0 || particleID_A >= Particle.VALUES.length) {
                throw new IllegalArgumentException("'particleID_A' must be within bounds for the Particle enum!");
            }
            this.particleID_A = particleID_A;
            return this;
        }

        public Builder particleID_B(int particleID_B) {
            if (particleID_B < 0 || particleID_B >= Particle.VALUES.length) {
                throw new IllegalArgumentException("'particleID_B' must be within bounds for the Particle enum!");
            }
            this.particleID_B = particleID_B;
            return this;
        }

        public Builder amount_A(int amount_A) {
            if (amount_A < 0) throw new IllegalArgumentException("'amount_A' must be greater than or equal to zero!");
            this.amount_A = amount_A;
            return this;
        }

        public Builder amount_B(int amount_B) {
            if (amount_B < 0) throw new IllegalArgumentException("'amount_B' must be greater than or equal to zero!");
            this.amount_B = amount_B;
            return this;
        }

        public Builder energy_A(float minEnergy_A) {
            if (minEnergy_A < 0) throw new IllegalArgumentException("'minEnergy_A' must be greater than zero!");
            this.minEnergy_A = minEnergy_A;
            return this;
        }

        public Builder energy_B(float minEnergy_B) {
            if (minEnergy_B < 0) throw new IllegalArgumentException("'minEnergy_B' must be greater than zero!");
            this.minEnergy_B = minEnergy_B;
            return this;
        }

        public BeamCrafterMetadata build() {
            return new BeamCrafterMetadata(particleID_A, particleID_B, amount_A, amount_B, minEnergy_A, minEnergy_B);
        }
    }
}
