package miscutil.core.util.particles;

import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.world.World;

public class EntityParticleFXMysterious extends EntityAuraFX
{
    public EntityParticleFXMysterious(World parWorld,
            double parX, double parY, double parZ,
            double parMotionX, double parMotionY, double parMotionZ) 
    {
        super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
        setParticleTextureIndex(82); // same as happy villager
        particleScale = 2.0F;
        setRBGColorF(0x88, 0x00, 0x88);
    }
}
