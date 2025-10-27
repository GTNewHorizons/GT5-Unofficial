package gregtech.mixin.mixins.late.hee;

import net.minecraft.world.gen.structure.MapGenScatteredFeature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import chylex.hee.world.ChunkProviderHardcoreEnd;
import chylex.hee.world.structure.island.MapGenIsland;
import gregtech.mixin.interfaces.accessors.HEEChunkProviderAccessor;

@Mixin(ChunkProviderHardcoreEnd.class)
public class ChunkProviderHardcoreEndMixin implements HEEChunkProviderAccessor {

    @Shadow(remap = false)
    @Final
    private MapGenScatteredFeature islandGen;

    @Override
    public MapGenIsland gt5u$getIslandGen() {
        return (MapGenIsland) islandGen;
    }
}
