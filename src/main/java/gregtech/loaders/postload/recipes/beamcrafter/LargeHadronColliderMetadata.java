package gregtech.loaders.postload.recipes.beamcrafter;

import java.util.List;

import com.google.common.collect.ImmutableList;

import gtnhlanth.common.beamline.Particle;

public class LargeHadronColliderMetadata {

    public final List<Particle> particleList;
    public final int progressBarTextureIndex;

    private LargeHadronColliderMetadata(List<Particle> particleList, int progressBarTextureIndex) {
        this.particleList = particleList;
        this.progressBarTextureIndex = progressBarTextureIndex;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImmutableList<Particle> particleList;
        private int progressBarTextureIndex;

        public Builder particleList(ImmutableList<Particle> particleList) {
            this.particleList = particleList;
            return this;
        }

        public Builder progressBarTextureIndex(int progressBarTextureIndex) {
            this.progressBarTextureIndex = progressBarTextureIndex;
            return this;
        }

        public LargeHadronColliderMetadata build() {
            return new LargeHadronColliderMetadata(particleList, progressBarTextureIndex);
        }
    }
}
