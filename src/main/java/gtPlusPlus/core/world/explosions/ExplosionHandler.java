package gtPlusPlus.core.world.explosions;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ExplosionHandler {

	/**
	 * Creates an explosion. Args: entity, x, y, z, strength
	 */
	public MiningExplosion createExplosion(final World world, final Entity entityObj, final double x, final double y, final double z, final float size, final boolean makesFlames, final boolean makesSmoke)
	{
		return this.newExplosion(world, entityObj, x, y, z, size, makesFlames, makesSmoke);
	}

	/**
	 * returns a new explosion.
	 */
	public MiningExplosion newExplosion(final World world, final Entity entityObj, final double x, final double y, final double z, final float size, final boolean makesFlames, final boolean makesSmoke){
		final MiningExplosion explosion = new MiningExplosion(world, entityObj, x, y, z, size);
		explosion.isFlaming = makesFlames;
		explosion.isSmoking = makesSmoke;
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) {
			return explosion;
		}
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

}
