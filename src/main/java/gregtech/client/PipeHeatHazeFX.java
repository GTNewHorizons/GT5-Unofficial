package gregtech.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A faint wisp of hot air rising off a fluid pipe full of something hot. Wobbles from side to side as it climbs,
 * swells and fades, so a row of them reads as heat shimmer above the pipe.
 */
@SideOnly(Side.CLIENT)
public class PipeHeatHazeFX extends EntityFX {

    private final float startScale;
    private final float peakAlpha;
    private final float wobblePhase;

    /** @param heat 0 to 1, how hot the pipe is; hotter pipes give stronger, faster haze */
    public PipeHeatHazeFX(World world, double x, double y, double z, float heat) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        motionX = (rand.nextDouble() - 0.5D) * 0.004D;
        motionY = 0.01D + rand.nextDouble() * 0.01D + heat * 0.01D;
        motionZ = (rand.nextDouble() - 0.5D) * 0.004D;
        final float shade = 0.92F + rand.nextFloat() * 0.08F;
        setRBGColorF(shade, shade * 0.95F, shade * 0.86F);
        startScale = particleScale * (0.6F + rand.nextFloat() * 0.5F);
        peakAlpha = 0.08F + 0.14F * heat;
        wobblePhase = rand.nextFloat() * (float) Math.PI * 2.0F;
        particleAlpha = 0.0F;
        particleMaxAge = 28 + rand.nextInt(24);
        noClip = true;
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
        particleScale = startScale * (1.0F + life * 1.2F);
        setParticleTextureIndex(Math.min(7, (int) (life * 8.0F)));

        // Hot air speeds up as it rises and weaves about
        motionX += MathHelper.sin(particleAge * 0.35F + wobblePhase) * 0.0012D;
        motionZ += MathHelper.cos(particleAge * 0.3F + wobblePhase) * 0.0012D;
        motionY += 0.0005D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.92D;
        motionY *= 0.98D;
        motionZ *= 0.92D;
    }
}
