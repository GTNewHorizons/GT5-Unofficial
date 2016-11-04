package gtPlusPlus.core.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTeleportFX extends Entity {
	private static final String	__OBFID	= "CL_00001716";
	/** 'x' location the eye should float towards. */
	private double				targetX;
	/** 'y' location the eye should float towards. */
	private double				targetY;
	/** 'z' location the eye should float towards. */
	private double				targetZ;
	private int					despawnTimer;
	private boolean				shatterOrDrop;

	public EntityTeleportFX(final World p_i1757_1_) {
		super(p_i1757_1_);
		this.setSize(0.25F, 0.25F);
	}

	public EntityTeleportFX(final World p_i1758_1_, final double p_i1758_2_, final double p_i1758_4_,
			final double p_i1758_6_) {
		super(p_i1758_1_);
		this.despawnTimer = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
		this.yOffset = 0.0F;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(final float p_70013_1_) {
		return 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(final float p_70070_1_) {
		return 15728880;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(final double p_70112_1_) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	/**
	 * The location the eye should float/move towards. Currently used for moving
	 * towards the nearest stronghold. Args: strongholdX, strongholdY,
	 * strongholdZ
	 */
	public void moveTowards(final double p_70220_1_, final int p_70220_3_, final double p_70220_4_) {
		final double d2 = p_70220_1_ - this.posX;
		final double d3 = p_70220_4_ - this.posZ;
		final float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);

		if (f > 12.0F) {
			this.targetX = this.posX + d2 / f * 12.0D;
			this.targetZ = this.posZ + d3 / f * 12.0D;
			this.targetY = this.posY + 8.0D;
		}
		else {
			this.targetX = p_70220_1_;
			this.targetY = p_70220_3_;
			this.targetZ = p_70220_4_;
		}

		this.despawnTimer = 0;
		this.shatterOrDrop = this.rand.nextInt(5) > 0;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

		for (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI); this.rotationPitch
				- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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

		if (!this.worldObj.isRemote) {
			final double d0 = this.targetX - this.posX;
			final double d1 = this.targetZ - this.posZ;
			final float f1 = (float) Math.sqrt(d0 * d0 + d1 * d1);
			final float f2 = (float) Math.atan2(d1, d0);
			double d2 = f + (f1 - f) * 0.0025D;

			if (f1 < 1.0F) {
				d2 *= 0.8D;
				this.motionY *= 0.8D;
			}

			this.motionX = Math.cos(f2) * d2;
			this.motionZ = Math.sin(f2) * d2;

			if (this.posY < this.targetY) {
				this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
			}
			else {
				this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
			}
		}

		final float f3 = 0.25F;

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f3, this.posY - this.motionY * f3,
						this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
			}
		}
		else {
			this.worldObj.spawnParticle("portal", this.posX - this.motionX * f3 + this.rand.nextDouble() * 0.6D - 0.3D,
					this.posY - this.motionY * f3 - 0.5D,
					this.posZ - this.motionZ * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY,
					this.motionZ);
		}

		if (!this.worldObj.isRemote) {
			this.setPosition(this.posX, this.posY, this.posZ);
			++this.despawnTimer;

			if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
				this.setDead();

				if (this.shatterOrDrop) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ,
							new ItemStack(Items.ender_eye)));
				}
				else {
					this.worldObj.playAuxSFX(2003, (int) Math.round(this.posX), (int) Math.round(this.posY),
							(int) Math.round(this.posZ), 0);
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(final NBTTagCompound p_70037_1_) {
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(final double p_70016_1_, final double p_70016_3_, final double p_70016_5_) {
		this.motionX = p_70016_1_;
		this.motionY = p_70016_3_;
		this.motionZ = p_70016_5_;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			final float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(final NBTTagCompound p_70014_1_) {
	}
}