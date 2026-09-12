package gregtech.api.interfaces.internal;

import net.minecraft.world.World;

public interface IDraconicCompat {

    /**
     * Triggers a Draconic Evolution reactor-style spherical explosion at the given position, blocking until it
     * finishes. The power scale matches DE's reactor meltdown power, not vanilla explosion strength.
     */
    void createReactorExplosion(World world, int x, int y, int z, float power);
}
