package gregtech.mixin.mixins.late.xu;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime;

@Mixin(ChunkProviderEndOfTime.class)
@SuppressWarnings("unused")
public abstract class ChunkProviderEndOfTimeMixin implements IChunkProvider {

    /**
     * @author bart
     * @reason Disable creature spawning in the Last Millenium entirely
     */
    @Overwrite
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_,
        int p_73155_3_, int p_73155_4_) {
        return Collections.emptyList();
    }
}
