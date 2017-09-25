package gtPlusPlus.core.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySickBlaze extends EntityMob {
	/** Random offset used in floating behaviour */
	private float heightOffset = 0.5F;
	/** ticks until heightOffset is randomized */
	private int heightOffsetUpdateTime;
	private int field_70846_g;
	private final int mDataWatcherID = 30;

	public EntitySickBlaze(World p_i1731_1_) {
		super(p_i1731_1_);
		this.isImmuneToFire = true;
		this.experienceValue = 10;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(mDataWatcherID, new Byte((byte) 0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return p_70013_1_;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (!this.worldObj.isRemote) {
			if (this.isWet()) {
				this.attackEntityFrom(DamageSource.drown, 1.0F);
			}

			--this.heightOffsetUpdateTime;

			if (this.heightOffsetUpdateTime <= 0) {
				this.heightOffsetUpdateTime = 100;
				this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
			}

			if (this.getEntityToAttack() != null && this.getEntityToAttack().posY
					+ this.getEntityToAttack().getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset) {
				this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			}
		}

		if (this.rand.nextInt(24) == 0) {
			/*this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire",
					1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);*/
		}

		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		for (int i = 0; i < 2; ++i) {
			this.worldObj.spawnParticle("crit", this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
					this.posY + this.rand.nextDouble() * this.height,
					this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity entity, float p_70785_2_) {
		if (this.attackTime <= 0 && p_70785_2_ < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY
				&& entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(entity);
		}
		else if (p_70785_2_ < 30.0F) {
			double d0 = entity.posX - this.posX;
			double d1 = entity.boundingBox.minY + entity.height / 2.0F - (this.posY + this.height / 2.0F);
			double d2 = entity.posZ - this.posZ;

			if (this.attackTime == 0) {
				++this.field_70846_g;

				if (this.field_70846_g == 1) {
					this.attackTime = 60;
				}
				else if (this.field_70846_g <= 4) {
					this.attackTime = 6;
				}
				else {
					this.attackTime = 100;
					this.field_70846_g = 0;
				}

				if (this.field_70846_g > 1) {
					float f1 = MathHelper.sqrt_float(p_70785_2_) * 0.5F;
					this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) this.posX, (int) this.posY,
							(int) this.posZ, 0);

					for (int i = 0; i < 1; ++i) {
						EntityToxinballSmall entitysmalltoxinball = new EntityToxinballSmall(this.worldObj, this,
								d0 + this.rand.nextGaussian() * f1, d1, d2 + this.rand.nextGaussian() * f1);
						entitysmalltoxinball.posY = this.posY + this.height / 2.0F + 0.5D;
						this.worldObj.spawnEntityInWorld(entitysmalltoxinball);
					}
				}
			}

			this.rotationYaw = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected Item getDropItem() {
		return Items.slime_ball;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire
	 * effect on rendering.
	 */
	@Override
	public boolean isBurning() {
		return false;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		if (p_70628_1_) {
			int j = this.rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k) {
				this.dropItem(getDropItem(), 1);
			}
		}
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel() {
		return true;
	}
}