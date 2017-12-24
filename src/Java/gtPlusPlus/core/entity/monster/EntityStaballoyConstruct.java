package gtPlusPlus.core.entity.monster;

import java.lang.reflect.Field;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.core.world.explosions.ExplosionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityStaballoyConstruct extends EntityIronGolem {

	/*
	 * Determines whether or not the entity is in a fluid at all.
	 */
	private volatile boolean inFluid = false;
	private volatile boolean mReflectFirstUpdate = true;
	private volatile boolean isReadyToExplode = false;
	private volatile int fuse = 60;
	private volatile int attackTimer;

	public EntityStaballoyConstruct(World world) {
		super(world);
		this.experienceValue = 250;
		this.setSize(1.4F, 2.9F);
		this.getNavigator().setAvoidsWater(true);
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setCanSwim(false);
		this.getNavigator().setAvoidSun(false);
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		//this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("inFluid", this.inFluid);
		p_70014_1_.setBoolean("isReadyToExplode", this.isReadyToExplode);
		p_70014_1_.setInteger("fuse", this.fuse);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		this.inFluid = p_70037_1_.getBoolean("inFluid");
		this.isReadyToExplode = p_70037_1_.getBoolean("isReadyToExplode");
		this.fuse = p_70037_1_.getInteger("fuse");
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		super.updateAITick();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	@Override
	protected int decreaseAirSupply(int p_70682_1_) {
		return 0;
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_) {
		if (p_82167_1_ instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((EntityLivingBase) p_82167_1_);
		}

		super.collideWithEntity(p_82167_1_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D
				&& this.rand.nextInt(5) == 0) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D - this.yOffset);
			int k = MathHelper.floor_double(this.posZ);
			Block block = this.worldObj.getBlock(i, j, k);



			if (block.getMaterial() != Material.air) {
				this.worldObj.spawnParticle(
						"blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k),
						this.posX + (this.rand.nextFloat() - 0.5D) * this.width,
						this.boundingBox.minY + 0.1D,
						this.posZ + (this.rand.nextFloat() - 0.5D) * this.width,
						4.0D * (this.rand.nextFloat() - 0.5D), 0.5D,
						(this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	@Override
	public boolean canAttackClass(Class clazz) {
		return clazz.equals(this.getClass()) ? false : true;
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		this.attackTimer = 10;
		this.worldObj.setEntityState(this, (byte) 4);
		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this),
				7 + this.rand.nextInt(15));

		if (flag) {
			p_70652_1_.motionY += 0.4000000059604645D;
		}

		this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 4) {
			this.attackTimer = 10;
			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		}
		else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	public Village getVillage() {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getAttackTimer() {
		return this.attackTimer;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.irongolem.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.irongolem.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int lootingChance = p_70628_2_+1;
		int j = this.rand.nextInt(3);
		int k;

		for (k = 0; k < j; ++k) {
			this.entityDropItem(ItemUtils.getItemStackOfAmountFromOreDict("blockStaballoy", 1), 0f);
		}

		k = 3 + this.rand.nextInt(3);

		for (int l = 0; l < k; ++l) {
			this.entityDropItem(ItemUtils.getItemStackOfAmountFromOreDict("ingotStaballoy", lootingChance), 0f);
			if (MathUtils.randInt(0, 2) == 0){
				this.entityDropItem(ItemUtils.getItemStackOfAmountFromOreDict("plateStaballoy", lootingChance), 0f);
			}
		}
	}

	@Override
	public boolean isPlayerCreated() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	@Override
	public void setPlayerCreated(boolean p_70849_1_) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70849_1_) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		}
		else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);
	}

	@Override
	protected String getLivingSound() { //TODO
		return super.getLivingSound();
	}

	@Override
	public int getTalkInterval() {
		return 0;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	public void onEntityUpdate() {
		//Set Fire Immunity
		if (!this.isImmuneToFire){
			this.isImmuneToFire = true;
		} 	

		if (this.getHealth() <= (this.getMaxHealth()*MathUtils.randDouble(0.02, 0.15))){
			float r = MathUtils.randFloat(0, 1);
			if (r <= 0.1){
				this.isReadyToExplode = true;
			}
		}

		//Handle Exploding
		if (isReadyToExplode){			
			if (this.fuse-- <= 0){
				this.setDead();

				if (!this.worldObj.isRemote)
				{
					this.explode();
				}
			}
			else {
				
				int maxFuse = 60;
				int fuseUsed = maxFuse-this.fuse;
				float var2 = (float) (fuseUsed * 0.1);
				
				this.setSize(1.4F+(var2/2), 2.9F+(var2/2));
				
				float r = MathUtils.randFloat(0, 1);
				int r2 = MathUtils.randInt(5, 15);
				for (int o=0;o<r2;o++){
					if (r <= 0.3){
						this.worldObj.spawnParticle("smoke", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					else if (r <= 0.6){
						this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					if (r <= 0.3){
						this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					else if (r <= 0.7){
						this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					if (r <= 0.2){
						this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					else if (r <= 0.5){
						this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
					else if (r <= 0.7){
						this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

					}
				}
				

			}		
		}

		//Get a private field from a super class if it exists.
		try {
			if (ReflectionUtils.getField(Class.forName("net.minecraft.entity.Entity"), "firstUpdate") != null && mReflectFirstUpdate == true){
				Field x = ReflectionUtils.getField(Class.forName("net.minecraft.entity.Entity"), "firstUpdate");
				try {
					this.mReflectFirstUpdate = (boolean) x.get(this);
					Logger.REFLECTION("Successfully got 'firstUpdate' variable state via reflection.");
				}
				catch (IllegalArgumentException | IllegalAccessException e) {}
			}
		}
		catch (NoSuchFieldException | ClassNotFoundException e) {}
		super.onEntityUpdate();
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
		// Do Nothing because he weighs metric shittonnes.
	}

	@Override
	protected void setOnFireFromLava() {
		extinguish();
	}

	@Override
	public void setFire(int p_70015_1_) {
		extinguish();
	}

	@Override
	protected void dealFireDamage(int p_70081_1_) {

	}

	@Override
	public boolean isInWater() {
		if (super.isInWater()){
			return true;
		}
		else {
			this.moveForward *= 0.98F;
			return false;
		}
	}

	@Override
	public boolean handleWaterMovement() {
		this.moveForward *= 0.74F;
		return handleFluidMovement(Material.water);
	}

	@Override
	public boolean handleLavaMovement() {
		this.moveForward *= 0.74F;
		return  handleFluidMovement(Material.lava);
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters velocity to the entity
	 */
	public boolean handleFluidMovement(Material fluid){



		if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), fluid, this))
		{
			if (!this.inFluid && !this.mReflectFirstUpdate)
			{
				float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;

				if (f > 1.0F)
				{
					f = 1.0F;
				}

				this.playSound(this.getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				float f1 = MathHelper.floor_double(this.boundingBox.minY);
				int i;
				float f2;
				float f3;

				for (i = 0; i < 1.0F + this.width * 20.0F; ++i)
				{
					f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + f2, f1 + 1.0F, this.posZ + f3, this.motionX, this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ);
				}

				for (i = 0; i < 1.0F + this.width * 20.0F; ++i)
				{
					f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + f2, f1 + 1.0F, this.posZ + f3, this.motionX, this.motionY, this.motionZ);
				}
			}
			this.fallDistance = 0.0F;
			this.inFluid = true;
		}
		else
		{
			this.inFluid = false;
		}
		return this.inFluid;
	}

	@Override
	public void onChunkLoad() {
		// TODO Auto-generated method stub
		super.onChunkLoad();
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		this.isReadyToExplode = true;
		this.fuse = 20;
	}

	private void explode(){
		/* float f = 12.0F;
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, f, true);*/

		final float f = 16F;		
		ExplosionHandler explode = new ExplosionHandler();
		explode.createExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, f, true, true);	
		
		float r = MathUtils.randFloat(0, 1);
		int r2 = MathUtils.randInt(20, 40);
		for (int o=0;o<r2;o++){
			if (r <= 0.3){
				this.worldObj.spawnParticle("smoke", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(0, 3), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			else if (r <= 0.6){
				this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			if (r <= 0.3){
				this.worldObj.spawnParticle("cloud", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			else if (r <= 0.7){
				this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			if (r <= 0.2){
				this.worldObj.spawnParticle("explode", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			else if (r <= 0.5){
				this.worldObj.spawnParticle("largeexplode", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
			else if (r <= 0.7){
				this.worldObj.spawnParticle("hugeexplosion", this.posX+MathUtils.randDouble(-4, 4), this.posY+MathUtils.randDouble(-4, 4), this.posZ+MathUtils.randDouble(-4, 4), 0.0D, 0.0D, 0.0D);

			}
		}
		
	}

	@Override
	public boolean canAttackWithItem() {
		return true;
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}
}