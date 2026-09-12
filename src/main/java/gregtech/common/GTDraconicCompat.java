package gregtech.common;

import net.minecraft.world.World;

import com.brandon3055.draconicevolution.common.tileentities.multiblocktiles.reactor.ReactorExplosion;

import gregtech.api.interfaces.internal.IDraconicCompat;

/**
 * The only place in the codebase allowed to touch Draconic Evolution's internal {@code common} package - everything
 * else should go through {@link IDraconicCompat} so a future DE refactor only breaks this one file.
 */
public class GTDraconicCompat implements IDraconicCompat {

    @Override
    public void createReactorExplosion(World world, int x, int y, int z, float power) {
        // IProcess is normally driven by DE's own tick handler - run it to completion synchronously here since
        // we're triggering it standalone.
        final ReactorExplosion explosion = new ReactorExplosion(world, x, y, z, power);
        while (!explosion.isDead()) {
            explosion.updateProcess();
        }
    }
}
