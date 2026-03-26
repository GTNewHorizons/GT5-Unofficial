package gregtech.loaders.postload.recipes.beamcrafter;

import java.util.List;

import com.google.common.collect.ImmutableList;

import gtnhlanth.common.beamline.Particle;

public class LargeHadronColliderMetadata {

    public final List<Particle> particleList;

    private LargeHadronColliderMetadata(List<Particle> particleList) {
        this.particleList = particleList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImmutableList<Particle> particleList;

        public Builder particleList(ImmutableList<Particle> particleList) {
            this.particleList = particleList;
            return this;
        }

        public LargeHadronColliderMetadata build() {
            return new LargeHadronColliderMetadata(particleList);
        }
    }
}
