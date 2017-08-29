package gtPlusPlus.core.entity.projectile;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.World;

public abstract class EntityToxinball extends EntityFireball {
	protected int entityX = -1;
	protected int entityY = -1;
	protected int entityZ = -1;
	private Block block;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	public EntityToxinball(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	public EntityToxinball(World world, double x, double y, double z, double f1, double f2, double f3) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
		this.setPosition(x, y, z);
		double d6 = MathHelper
				.sqrt_double(f1 * f1 + f2 * f2 + f3 * f3);
		this.accelerationX = f1 / d6 * 0.1D;
		this.accelerationY = f2 / d6 * 0.1D;
		this.accelerationZ = f3 / d6 * 0.1D;
	}

	public EntityToxinball(World world, EntityLivingBase entity, double x, double y, double z) {
		super(world);
		this.shootingEntity = entity;
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw,
				entity.rotationPitch);
		this.setPosition(this.entityX, this.entityY, this.entityZ);
		this.yOffset = 0.0F;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		x += this.rand.nextGaussian() * 0.4D;
		y += this.rand.nextGaussian() * 0.4D;
		z += this.rand.nextGaussian() * 0.4D;
		double d3 = MathHelper.sqrt_double(x * x + y * y + z * z);
		this.accelerationX = x / d3 * 0.1D;
		this.accelerationY = y / d3 * 0.1D;
		this.accelerationZ = z / d3 * 0.1D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead
				|| !this.worldObj.blockExists(this.entityX, this.entityY, this.entityZ))) {
			this.setDead();
		}
		else {
			super.onUpdate();
			this.setFire(1);

			if (this.inGround) {
				if (this.worldObj.getBlock(this.entityX, this.entityY,
						this.entityZ) == this.block) {
					++this.ticksAlive;

					if (this.ticksAlive == 600) {
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= this.rand.nextFloat() * 0.2F;
				this.motionY *= this.rand.nextFloat() * 0.2F;
				this.motionZ *= this.rand.nextFloat() * 0.2F;
				this.ticksAlive = 0;
				this.ticksInAir = 0;
			}
			else {
				++this.ticksInAir;
			}

			Vec3 vec3 = Vec3.createVectorHelper(this.entityX, this.entityY, this.entityZ);
			Vec3 vec31 = Vec3.createVectorHelper(this.entityX + this.motionX, this.entityY + this.motionY,
					this.entityZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
			vec3 = Vec3.createVectorHelper(this.entityX, this.entityY, this.entityZ);
			vec31 = Vec3.createVectorHelper(this.entityX + this.motionX, this.entityY + this.motionY,
					this.entityZ + this.motionZ);

			if (movingobjectposition != null) {
				vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
					this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith()
						&& (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null) {
				this.onImpact(movingobjectposition);
			}

			this.entityX += this.motionX;
			this.entityY += this.motionY;
			this.entityZ += this.motionZ;
			float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

			for (this.rotationPitch = (float) (Math.atan2(f1, this.motionY) * 180.0D / Math.PI)
					- 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f2 = this.getMotionFactor();

			if (this.isInWater()) {
				for (int j = 0; j < 4; ++j) {
					float f3 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.entityX - this.motionX * f3, this.entityY - this.motionY * f3,
							this.entityZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
				}

				f2 = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= f2;
			this.motionY *= f2;
			this.motionZ *= f2;
			this.worldObj.spawnParticle("smoke", this.entityX, this.entityY + 0.5D, this.entityZ, 0.0D, 0.0D, 0.0D);
			this.setPosition(this.entityX, this.entityY, this.entityZ);
		}
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by
	 * the original motion.
	 */
	@Override
	protected float getMotionFactor() {
		return 0.95F;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	@Override
	protected abstract void onImpact(MovingObjectPosition p_70227_1_);

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound aTag) {
		aTag.setShort("xTile", (short) this.entityX);
		aTag.setShort("yTile", (short) this.entityY);
		aTag.setShort("zTile", (short) this.entityZ);
		aTag.setByte("inTile", (byte) Block.getIdFromBlock(this.block));
		aTag.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		aTag.setTag("direction",
				this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound aTag) {
		this.entityX = aTag.getShort("xTile");
		this.entityY = aTag.getShort("yTile");
		this.entityZ = aTag.getShort("zTile");
		this.block = Block.getBlockById(aTag.getByte("inTile") & 255);
		this.inGround = aTag.getByte("inGround") == 1;

		if (aTag.hasKey("direction", 9)) {
			NBTTagList nbttaglist = aTag.getTagList("direction", 6);
			this.motionX = nbttaglist.func_150309_d(0);
			this.motionY = nbttaglist.func_150309_d(1);
			this.motionZ = nbttaglist.func_150309_d(2);
		}
		else {
			this.setDead();
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damage, float p_70097_2_) {
		if (this.isEntityInvulnerable()) {
			return false;
		}
		else {
			this.setBeenAttacked();

			if (damage.getEntity() != null) {
				Vec3 vec3 = damage.getEntity().getLookVec();

				if (vec3 != null) {
					this.motionX = vec3.xCoord;
					this.motionY = vec3.yCoord;
					this.motionZ = vec3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}

				if (damage.getEntity() instanceof EntityLivingBase) {
					this.shootingEntity = (EntityLivingBase) damage.getEntity();
				}

				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}
}