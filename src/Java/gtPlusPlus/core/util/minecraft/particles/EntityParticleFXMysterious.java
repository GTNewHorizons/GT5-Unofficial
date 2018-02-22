package gtPlusPlus.core.util.minecraft.particles;

import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.world.World;

public class EntityParticleFXMysterious extends EntityAuraFX
{
	public EntityParticleFXMysterious(final World parWorld,
			final double parX, final double parY, final double parZ,
			final double parMotionX, final double parMotionY, final double parMotionZ)
	{
		super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
		this.setParticleTextureIndex(82); // same as happy villager
		this.particleScale = 2.0F;
		this.setRBGColorF(0x88, 0x00, 0x88);
	}
}
