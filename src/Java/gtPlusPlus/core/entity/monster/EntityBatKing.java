package gtPlusPlus.core.entity.monster;

import java.util.Calendar;

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
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBatKing extends EntityMob implements IRangedAttackMob {


	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);	

	public EntityBatKing(World p_i1680_1_) {
		super(p_i1680_1_);
		this.setSize(2.5F, 1.5F);

		this.setIsBatHanging(false);
		this.isImmuneToFire = true;
		this.experienceValue = 1000;

		this.tasks.addTask(3, this.aiArrowAttack);
		this.tasks.addTask(4, this.aiAttackOnCollide);
		this.tasks.addTask(4, new EntityAIRestrictSun(this));
		this.tasks.addTask(5, new EntityAIFleeSun(this, 1.0D));
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
	 public EnumCreatureAttribute getCreatureAttribute()
	 {
		 return EnumCreatureAttribute.UNDEAD;
	 }

	 /**
	  * Returns the volume for the sounds this mob makes.
	  */
	 protected float getSoundVolume() {
		 return 0.45F;
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

		 for (int i = 0; i < 2; ++i) {
			 this.worldObj.spawnParticle("largesmoke", this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
					 this.posY + this.rand.nextDouble() * (double) this.height,
					 this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
		 }		

		 if (!isFlying()) {
			 this.jump();
		 } 
		 /*
		  * if (!hasAir()) { if (isFlying()) { this.jump(); this.jump(); } }
		  */	

		 if (!this.onGround && this.motionY < 0.0D)
		 {
			 this.motionY *= 0.0001D;
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
			 for (int i=0;i<y;y++) {
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
	 }

	 /**
	  * (abstract) Protected helper method to write subclass entity data to NBT.
	  */
	 public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		 super.writeEntityToNBT(p_70014_1_);
	 }

	 /**
	  * Checks if the entity's current position is a valid location to spawn this
	  * entity.
	  */
	 public boolean getCanSpawnHere() {
		 int i = MathHelper.floor_double(this.boundingBox.minY);

		 if (i >= 63) {
			 return false;
		 } else {
			 int j = MathHelper.floor_double(this.posX);
			 int k = MathHelper.floor_double(this.posZ);
			 int l = this.worldObj.getBlockLightValue(j, i, k);
			 byte b0 = 4;
			 Calendar calendar = this.worldObj.getCurrentDate();

			 if ((calendar.get(2) + 1 != 10 || calendar.get(5) < 20)
					 && (calendar.get(2) + 1 != 11 || calendar.get(5) > 3)) {
				 if (this.rand.nextBoolean()) {
					 return false;
				 }
			 } else {
				 b0 = 7;
			 }

			 return l > this.rand.nextInt(b0) ? false : super.getCanSpawnHere();
		 }
	 }

	 /**
	  * Attack the specified entity using a ranged attack.
	  */
	 @Override
	 public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	 {
		 EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
		 int i = MathUtils.randInt(0, 4);
		 int j = MathUtils.randInt(0, 3);
		 int k = MathUtils.randInt(0, 3);
		 entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

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