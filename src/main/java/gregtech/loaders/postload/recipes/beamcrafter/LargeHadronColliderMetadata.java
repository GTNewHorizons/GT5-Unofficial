package gregtech.loaders.postload.recipes.beamcrafter;

import com.google.common.collect.ImmutableList;
import gregtech.common.tileentities.machines.multi.beamcrafting.LHCModule;
import gtnhlanth.common.beamline.Particle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
