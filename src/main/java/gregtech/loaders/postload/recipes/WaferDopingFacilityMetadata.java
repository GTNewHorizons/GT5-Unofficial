package gregtech.loaders.postload.recipes;

import java.util.Objects;

public final class WaferDopingFacilityMetadata {

    public static final int PARTICLE_MODE_NONE = 0;
    public static final int PARTICLE_MODE_PARTICLES = 1;
    public static final int PARTICLE_MODE_ANOMALY = 2;
    public static final WaferDopingFacilityMetadata DEFAULT = builder().build();

    public final int structureTier;
    public final int minimumTemperature;
    public final int maximumTemperature;
    public final int particleMode;

    private WaferDopingFacilityMetadata(int structureTier, int minimumTemperature, int maximumTemperature,
        int particleMode) {
        this.structureTier = structureTier;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.particleMode = particleMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(structureTier, minimumTemperature, maximumTemperature, particleMode);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        WaferDopingFacilityMetadata that = (WaferDopingFacilityMetadata) other;
        return structureTier == that.structureTier && minimumTemperature == that.minimumTemperature
            && maximumTemperature == that.maximumTemperature
            && particleMode == that.particleMode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int structureTier = 1;
        private int minimumTemperature;
        private int maximumTemperature;
        private int particleMode = PARTICLE_MODE_NONE;

        public Builder structureTier(int structureTier) {
            this.structureTier = structureTier;
            return this;
        }

        public Builder minimumTemperature(int minimumTemperature) {
            this.minimumTemperature = minimumTemperature;
            return this;
        }

        public Builder maximumTemperature(int maximumTemperature) {
            this.maximumTemperature = maximumTemperature;
            return this;
        }

        public Builder particleMode(int particleMode) {
            this.particleMode = particleMode;
            return this;
        }

        public WaferDopingFacilityMetadata build() {
            return new WaferDopingFacilityMetadata(structureTier, minimumTemperature, maximumTemperature, particleMode);
        }
    }
}
