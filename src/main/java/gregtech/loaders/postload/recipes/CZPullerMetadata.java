package gregtech.loaders.postload.recipes;

import java.util.Objects;

public final class CZPullerMetadata {

    public static final CZPullerMetadata DEFAULT = builder().build();

    public final int structureTier;
    public final int temperature;

    private CZPullerMetadata(int structureTier, int temperature) {
        this.structureTier = structureTier;
        this.temperature = temperature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(structureTier, temperature);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        CZPullerMetadata that = (CZPullerMetadata) other;
        return structureTier == that.structureTier && temperature == that.temperature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int structureTier = 1;
        private int temperature;

        public Builder structureTier(int structureTier) {
            this.structureTier = structureTier;
            return this;
        }

        public Builder temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public CZPullerMetadata build() {
            return new CZPullerMetadata(structureTier, temperature);
        }
    }
}
