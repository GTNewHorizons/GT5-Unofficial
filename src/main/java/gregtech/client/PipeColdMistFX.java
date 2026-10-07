package gregtech.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A wisp of cold vapour coming off a fluid pipe full of something very cold. Sinks rather than rises, swells and fades
 * as it goes, and spreads out along the floor if it reaches it.
 */
@SideOnly(Side.CLIENT)
public class PipeColdMistFX extends EntityFX {

    private final float startScale;
    private final float peakAlpha;

    /** @param strength 0 to 1, how cold the pipe is; colder pipes give thicker vapour */
    public PipeColdMistFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ,
        float strength) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        final float shade = 0.88F + rand.nextFloat() * 0.12F;
        setRBGColorF(shade * 0.9F, shade * 0.96F, shade);
        startScale = particleScale * (0.8F + rand.nextFloat() * 0.6F);
        peakAlpha = 0.18F + 0.22F * strength;
        particleAlpha = 0.0F;
        particleMaxAge = 40 + rand.nextInt(40);
        setParticleTextureIndex(0);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) {
            setDead();
            return;
        }

        final float life = (float) particleAge / particleMaxAge;
        particleAlpha = peakAlpha * MathHelper.sin(life * (float) Math.PI);
        particleScale = startScale * (1.0F + life * 1.5F);
        // Vanilla's smoke puffs, smallest first, so it billows out
        setParticleTextureIndex(Math.min(7, (int) (life * 8.0F)));

        motionY -= 0.0006D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.95D;
        motionY *= 0.97D;
        motionZ *= 0.95D;
        if (onGround) {
            // Cold air pools and spreads out along the floor
            motionX = motionX * 0.8D + (rand.nextDouble() - 0.5D) * 0.006D;
            motionZ = motionZ * 0.8D + (rand.nextDouble() - 0.5D) * 0.006D;
        }
    }
}
