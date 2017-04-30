package gtPlusPlus.core.world.dimensionA.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityTreeFX extends EntityFX{
	
	//TODO: add abailty to change particle color in constructor.

	private float portalParticleScale;
	private double portalPosX;
	private double portalPosY;
	private double portalPosZ;

	@SuppressWarnings("unused")
	private static final String __OBFID = "CL_000003921";

	public EntityTreeFX(World world, double posX, double posY, double posZ, double motionPosX, double motionPosY, double motionPosZ)
	{

		super(world, posX, posY, posZ, motionPosX, motionPosY, motionPosZ);
		this.motionX = motionPosX;
		this.motionY = motionPosY;
		this.motionZ = motionPosZ;
		this.portalPosX = this.posX = posX;
		this.portalPosY = this.posY = posY;
		this.portalPosZ = this.posZ = posZ;
		this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
		this.particleRed = 0.0F;
		this.particleGreen = 0.0F;
		this.particleBlue = 1.0F;
		this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
		this.noClip = true;
		this.setParticleTextureIndex((int)(Math.random() * 8.0D));
	}

	@Override
	public void renderParticle(Tessellator tessellator, float posX, float posY, float posZ, float movePosX, float movePosY, float movePosZ)
	{
		float f6 = (this.particleAge + posX) / this.particleMaxAge;
		f6 = 1.0F - f6;
		f6 *= f6;
		f6 = 1.0F - f6;
		this.particleScale = this.portalParticleScale * f6;
		super.renderParticle(tessellator, posX, posY, posZ, movePosX, movePosY, movePosZ);
	}

	@Override
	public int getBrightnessForRender(float amount)
	{
		int i = super.getBrightnessForRender(amount);
		float f1 = (float)this.particleAge / (float)this.particleMaxAge;
		f1 *= f1;
		f1 *= f1;
		int j = i & 255;
		int k = i >> 16 & 255;
		k += (int)(f1 * 15.0F * 16.0F);

		if (k > 240)
		{
			k = 240;
		}

		return j | k << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float amount)
	{
		float f1 = super.getBrightness(amount);
		float f2 = (float)this.particleAge / (float)this.particleMaxAge;
		f2 = f2 * f2 * f2 * f2;
		return f1 * (1.0F - f2) + f2;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float f = (float)this.particleAge / (float)this.particleMaxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		f = 1.0F - f;
		this.posX = this.portalPosX + this.motionX * f;
		this.posY = this.portalPosY + this.motionY * f + (1.0F - f1);
		this.posZ = this.portalPosZ + this.motionZ * f;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}
	}

}
