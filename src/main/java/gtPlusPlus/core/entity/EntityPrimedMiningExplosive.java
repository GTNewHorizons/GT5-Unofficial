package gtPlusPlus.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.world.explosions.ExplosionHandler;

public class EntityPrimedMiningExplosive extends EntityTNTPrimed
{
	/** How long the fuse is */
	private EntityLivingBase tntPlacedBy;

	public EntityPrimedMiningExplosive(final World world){
		super(world);
		this.fuse = 160;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
	}

	public EntityPrimedMiningExplosive(final World world, final double x, final double y, final double z, final EntityLivingBase placingEntity)
	{
		this(world);
		this.setPosition(x, y, z);
		final float f = (float)(Math.random() * Math.PI * 2.0D);
		this.motionX = -((float)Math.sin(f)) * 0.02F;
		this.motionY = 0.20000000298023224D;
		this.motionZ = -((float)Math.cos(f)) * 0.02F;
		this.fuse = 160;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.tntPlacedBy = placingEntity;
	}

	@Override
	protected void entityInit() {}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
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
		this.motionY -= 0.03999999910593033D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}

		if (this.fuse-- <= 0)
		{
			this.setDead();

			if (!this.worldObj.isRemote)
			{
				this.explode();
			}
		}
		else
		{
			
			int t = MathUtils.randInt(0, 15);
			
			if (t <= 2){
				int e = MathUtils.randInt(0, 3);
				if (e <= 1){
					this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				}
				else if (e == 2){
					this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					
				}				
			}
			else if (t <= 4){
				int e = MathUtils.randInt(0, 5);
				if (e <= 1){
					this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				}
				else if (e == 2){
					this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					
				}	
			}
			else if (t <= 6){
				int e = MathUtils.randInt(0, 4);
				if (e <= 1){
					this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				}
				else if (e == 2){
					this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					
				}	
			}
			else if (t <= 8){
				int e = MathUtils.randInt(0, 1);
				if (e <= 1){
					this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				}
				else if (e == 2){
					this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					
				}	
			}
			else if (t <= 10){
				int e = MathUtils.randInt(0, 6);
				if (e <= 1){
					this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				}
				else if (e >= 2){
					this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(0, 1), this.posY+MathUtils.randDouble(0, 1), this.posZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					
				}	
			}
			
			}
	}

	private void explode()
	{
		final float f = 100.0F;
		
		ExplosionHandler explode = new ExplosionHandler();
		explode.createExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, f, false, true);
		
		/*this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, f, true);
		this.worldObj.createExplosion(this, this.posX+MathUtils.randDouble(-10, 10), this.posY, this.posZ+MathUtils.randDouble(-10, 10), f+MathUtils.randFloat(-5F, 5F), true);
		this.worldObj.createExplosion(this, this.posX+MathUtils.randDouble(-10, 10), this.posY, this.posZ+MathUtils.randDouble(-10, 10), f+MathUtils.randFloat(-5F, 5F), true);
		this.worldObj.createExplosion(this, this.posX+MathUtils.randDouble(-10, 10), this.posY, this.posZ+MathUtils.randDouble(-10, 10), f+MathUtils.randFloat(-5F, 5F), true);
		this.worldObj.createExplosion(this, this.posX+MathUtils.randDouble(-10, 10), this.posY, this.posZ+MathUtils.randDouble(-10, 10), f+MathUtils.randFloat(-5F, 5F), true);*/
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(final NBTTagCompound tag)
	{
		tag.setByte("Fuse", (byte)this.fuse);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(final NBTTagCompound tag)
	{
		this.fuse = tag.getByte("Fuse");
	}

	/**
	 * returns null or the entityliving it was placed or ignited by
	 */
	@Override
	public EntityLivingBase getTntPlacedBy()
	{
		return this.tntPlacedBy;
	}
}