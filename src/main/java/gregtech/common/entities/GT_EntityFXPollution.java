package gregtech.common.entities;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class GT_EntityFXPollution extends EntityFX {

    public GT_EntityFXPollution(World world, double x, double y, double z) {
        super(world, x, y, z, 0, 0, 0);

        this.particleRed = 0.25F;
        this.particleGreen = 0.2F;
        this.particleBlue = 0.25F;

        this.motionX *= 0.1D;
        this.motionY *= -0.1D;
        this.motionZ *= 0.1F;

        Random random = world.rand;
        this.motionX += random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;
        this.motionY += random.nextFloat() * -0.5D * random.nextFloat() * 0.1D * 5.0D;
        this.motionZ += random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;

        this.particleTextureIndexX = 0;
        this.particleTextureIndexY = 0;

        this.particleMaxAge = (int) ((double) 20 / ((double) random.nextFloat() * 0.8D + 0.2D));

        this.particleScale *= 0.75F;
        this.noClip = true;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        } else {
            this.motionY -= 5.0E-4D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }
            this.motionX *= 0.96D;
            this.motionY *= 0.96D;
            this.motionZ *= 0.96D;
            if (this.onGround) {
                this.motionX *= 0.7D;
                this.motionZ *= 0.7D;
            }
        }
    }
}
