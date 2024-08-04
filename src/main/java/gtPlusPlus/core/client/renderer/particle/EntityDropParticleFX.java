package gtPlusPlus.core.client.renderer.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityDropParticleFX extends EntityFX {

    private int bobTimer;

    public EntityDropParticleFX(World world, double x, double y, double z, float particleRed, float particleGreen,
        float particleBlue) {

        this(world, x, y, z, particleRed, particleGreen, particleBlue, -1);
    }

    public EntityDropParticleFX(World world, double x, double y, double z, float particleRed, float particleGreen,
        float particleBlue, int gravityMod) {

        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = this.motionY = this.motionZ = 0.0D;

        this.particleRed = particleRed;
        this.particleGreen = particleGreen;
        this.particleBlue = particleBlue;

        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = -0.06F * gravityMod;
        this.bobTimer = 40;
        this.particleMaxAge = (int) (48.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
    }

    @Override
    public void onUpdate() {

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= this.particleGravity;

        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
            this.setParticleTextureIndex(113);
        } else {
            this.setParticleTextureIndex(112);
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
        if (this.onGround) {
            this.setParticleTextureIndex(114);
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
        if (this.particleGravity > 0) {
            Material material = this.worldObj
                .getBlock(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))
                .getMaterial();

            if (material.isLiquid() || material.isSolid()) {
                double d0 = MathHelper.floor(this.posY) + 1
                    - BlockLiquid.getLiquidHeightPercent(
                        this.worldObj.getBlockMetadata(
                            MathHelper.floor(this.posX),
                            MathHelper.floor(this.posY),
                            MathHelper.floor(this.posZ)));
                if (this.posY < d0) {
                    this.setDead();
                }
            }
        } else {
            Material material = this.worldObj
                .getBlock(MathHelper.ceil(this.posX), MathHelper.ceil(this.posY), MathHelper.ceil(this.posZ))
                .getMaterial();

            if (material.isLiquid() || material.isSolid()) {
                double d0 = MathHelper.ceil(this.posY) + 1
                    - BlockLiquid.getLiquidHeightPercent(
                        this.worldObj.getBlockMetadata(
                            MathHelper.ceil(this.posX),
                            MathHelper.ceil(this.posY),
                            MathHelper.ceil(this.posZ)));
                if (this.posY > d0) {
                    this.setDead();
                }
            }
        }
    }
}
