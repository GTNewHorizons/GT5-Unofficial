package gregtech.mixin.mixins.late.hee;

import org.spongepowered.asm.mixin.Mixin;

import chylex.hee.world.structure.MapGenScatteredFeatureCustom;
import chylex.hee.world.structure.island.MapGenIsland;
import gregtech.mixin.interfaces.accessors.MapGenIslandAccessor;

@Mixin(MapGenIsland.class)
public abstract class MapGenIslandMixin extends MapGenScatteredFeatureCustom implements MapGenIslandAccessor {

    public MapGenIslandMixin(int minSpacing, int maxSpacing, int minDistanceFromCenter, int featureSize) {
        super(minSpacing, maxSpacing, minDistanceFromCenter, featureSize);
    }

    @Override
    public boolean gt5u$canSpawnStructureAtCoords(int x, int z) {
        return canSpawnStructureAtCoords(x, z);
    }
}
