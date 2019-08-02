package gtPlusPlus.core.entity.monster;

import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityBatKing extends EntityMob implements IRangedAttackMob {

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity;
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;
	private int explosionStrength = 1;

	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D,
			false);

	public EntityBatKing(World p_i1680_1_) {
		super(p_i1680_1_);
		this.setSize(2.5F, 1.5F);

		this.setIsBatHanging(false);
		this.isImmuneToFire = true;
		this.experienceValue = 1000;

		//this.tasks.addTask(3, this.aiArrowAttack);
		//this.tasks.addTask(4, this.aiAttackOnCollide);
		//this.tasks.addTask(4, new EntityAIRestrictSun(this));
		//this.tasks.addTask(5, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityBat.class, 0, false));

	}

	protected void entityInit() {
		super.entityInit();
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.15F;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		int aRand = MathUtils.randInt(0, 10);
		if (aRand < 6) {
			return null;
		} else if (aRand <= 8) {
			return "mob.bat.idle";
		} else {
			return "mob.blaze.breathe";
		}
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.bat.death";
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when
	 * colliding.
	 */
	public boolean canBePushed() {
		return false;
	}

	protected void collideWithEntity(Entity aEntity) {
		if (aEntity != null) {
			if (aEntity instanceof EntityPlayer) {
				EntityUtils.doDamage(aEntity, DamageSource.magic, (int) (((EntityPlayer) aEntity).getHealth() / 20));
			}
		}
	}

	protected void collideWithNearbyEntities() {
	}

	protected void applyEntityAttributes() {

		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange);

		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32.0D);
	}

	public boolean getIsBatHanging() {
		return false;
	}

	public void setIsBatHanging(boolean p_82236_1_) {

	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		generateParticles(this);
	}

	private static void generateParticles(EntityBatKing aKing) {
		for (int i = 0; i < 20; ++i) {
			//
			if (MathUtils.randInt(0, 50) <= 1) {
				String particleName = "smoke";
				if (MathUtils.randInt(0, 3) <= 2) {
					particleName = "largesmoke";
				}
				aKing.worldObj.spawnParticle(particleName,
						aKing.posX + (aKing.rand.nextDouble() - 0.5D) * (double) aKing.width,
						aKing.posY + aKing.rand.nextDouble() * (double) aKing.height,
						aKing.posZ + (aKing.rand.nextDouble() - 0.5D) * (double) aKing.width, 0.0D, 0.0D, 0.0D);
			}
			if (MathUtils.randInt(0, 100) <= 1) {
				String particleName = "smoke";
				int test2 = MathUtils.randInt(0, 3);
				if (test2 == 2) {
					particleName = "dripLava";
				} else if (test2 == 3) {
					particleName = "portal";
				}
				aKing.worldObj.spawnParticle(particleName,
						aKing.posX + (aKing.rand.nextDouble() - 0.5D) * (double) aKing.width,
						aKing.posY + aKing.rand.nextDouble() * (double) aKing.height,
						aKing.posZ + (aKing.rand.nextDouble() - 0.5D) * (double) aKing.width, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	protected void updateAITasks() {
		super.updateAITasks();
	}

	private boolean isFlying() {
		if (this.onGround) {
			return false;
		}
		return true;
	}

	private boolean hasAir() {
		BlockPos p = EntityUtils.findBlockPosUnderEntity(this);
		int y = p.yPos;
		int yOriginal = p.yPos;
		int breaker = 0;
		while (y > 0) {
			if (breaker > 50) {
				break;
			}
			if (!this.worldObj.isAirBlock(p.xPos, y, p.zPos)) {
				break;
			}
			y--;
			breaker++;
		}
		if (yOriginal - y < 3) {
			for (int i = 0; i < y; y++) {
				this.jump();
			}
			return true;
		}
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk
	 * on. used for spiders and wolves to prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Return whether this entity should NOT trigger a pressure plate or a tripwire.
	 */
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			if (!this.worldObj.isRemote && this.getIsBatHanging()) {
				this.setIsBatHanging(false);
			}

			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		if (p_70037_1_.hasKey("ExplosionPower", 99)) {
			this.explosionStrength = p_70037_1_.getInteger("ExplosionPower");
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("ExplosionPower", this.explosionStrength);
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {

		double d4 = 64.0D;
		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4) {
			double d5 = this.targetedEntity.posX - this.posX;
			double d6 = this.targetedEntity.boundingBox.minY + (double) (this.targetedEntity.height / 2.0F)
					- (this.posY + (double) (this.height / 2.0F));
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;

			++this.attackCounter;
			
			if (this.canEntityBeSeen(this.targetedEntity)) {				


				if (this.attackCounter == 20) {
					this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) this.posX, (int) this.posY,
							(int) this.posZ, 0);
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.worldObj, this, d5, d6, d7);
					entitylargefireball.field_92057_e = this.explosionStrength;
					double d8 = 4.0D;
					Vec3 vec3 = this.getLook(1.0F);
					entitylargefireball.posX = this.posX + vec3.xCoord * d8;
					entitylargefireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
					entitylargefireball.posZ = this.posZ + vec3.zCoord * d8;
					this.worldObj.spawnEntityInWorld(entitylargefireball);
					this.attackCounter = -40;
				}
			} else if (this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F
					/ (float) Math.PI;

			if (this.attackCounter > 0) {
				--this.attackCounter;
				EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F,
						(float) (14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
				int i = MathUtils.randInt(0, 4);
				int j = MathUtils.randInt(0, 3);
				int k = MathUtils.randInt(0, 3);
				entityarrow.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D
						+ (double) ((float) this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

				boolean boostAttack = MathUtils.randInt(0, 100) <= 21;
				if (boostAttack) {
					if (i > 0) {
						entityarrow.setDamage(entityarrow.getDamage() + (double) i * 0.5D + 0.5D);
					}

					if (j > 0) {
						entityarrow.setKnockbackStrength(j);
					}
					if (k > 0) {
						entityarrow.setFire(50 * k);
					}
				}

				this.playSound("mob.skeleton.say", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entityarrow);
			}

		}

	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 10.0F;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL;
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	protected Item getDropItem() {
		return Items.gunpowder;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
		int k;

		for (k = 0; k < j; ++k) {
			this.dropItem(Items.ghast_tear, 1);
		}

		j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

		for (k = 0; k < j; ++k) {
			this.dropItem(Items.gunpowder, 1);
		}
	}

	@Override
	protected void updateEntityActionState() {
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			this.setDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double d0 = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D) {
			this.waypointX = this.posX + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if (this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			d3 = (double) MathHelper.sqrt_double(d3);

			if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
				this.motionX += d0 / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			} else {
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if (this.targetedEntity != null && this.targetedEntity.isDead) {
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
			this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);

			if (this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;

		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4) {
			double d5 = this.targetedEntity.posX - this.posX;
			double d6 = this.targetedEntity.boundingBox.minY + (double) (this.targetedEntity.height / 2.0F)
					- (this.posY + (double) (this.height / 2.0F));
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;

			if (this.canEntityBeSeen(this.targetedEntity)) {
				if (this.attackCounter == 10) {
					this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1007, (int) this.posX, (int) this.posY,
							(int) this.posZ, 0);
				}

				++this.attackCounter;

				if (this.attackCounter == 20) {
					this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) this.posX, (int) this.posY,
							(int) this.posZ, 0);
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.worldObj, this, d5, d6, d7);
					entitylargefireball.field_92057_e = this.explosionStrength;
					double d8 = 4.0D;
					Vec3 vec3 = this.getLook(1.0F);
					entitylargefireball.posX = this.posX + vec3.xCoord * d8;
					entitylargefireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
					entitylargefireball.posZ = this.posZ + vec3.zCoord * d8;
					this.worldObj.spawnEntityInWorld(entitylargefireball);
					this.attackCounter = -40;
				}
			} else if (this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F
					/ (float) Math.PI;

			if (this.attackCounter > 0) {
				--this.attackCounter;
			}
		}

		if (!this.worldObj.isRemote) {
			byte b1 = this.dataWatcher.getWatchableObjectByte(16);
			byte b0 = (byte) (this.attackCounter > 10 ? 1 : 0);

			if (b1 != b0) {
				this.dataWatcher.updateObject(16, Byte.valueOf(b0));
			}
		}
	}

	/**
	 * True if the ghast has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double p_70790_1_, double p_70790_3_, double p_70790_5_, double p_70790_7_) {
		double d4 = (this.waypointX - this.posX) / p_70790_7_;
		double d5 = (this.waypointY - this.posY) / p_70790_7_;
		double d6 = (this.waypointZ - this.posZ) / p_70790_7_;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for (int i = 1; (double) i < p_70790_7_; ++i) {
			axisalignedbb.offset(d4, d5, d6);

			/*
			 * if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
			 * { return false; }
			 */
		}

		return true;
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float p_70069_1_) {
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on the
	 * ground to update the fall distance and deal fall damage if landing on the
	 * ground. Args: distanceFallenThisTick, onGround
	 */
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (this.isInWater()) {
			this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
		} else if (this.handleLavaMovement()) {
			this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
		} else {
			float f2 = 0.91F;

			if (this.onGround) {
				f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX),
						MathHelper.floor_double(this.boundingBox.minY) - 1,
						MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * f3 : 0.02F);
			f2 = 0.91F;

			if (this.onGround) {
				f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX),
						MathHelper.floor_double(this.boundingBox.minY) - 1,
						MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double) f2;
			this.motionY *= (double) f2;
			this.motionZ *= (double) f2;
		}

		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d1 = this.posX - this.prevPosX;
		double d0 = this.posZ - this.prevPosZ;
		float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder() {
		return false;
	}

}