package gtPlusPlus.core.entity.monster;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.ItemGemShards;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraftforge.common.ForgeHooks;

public class EntityGiantChickenBase extends EntityChicken {
	
    /** The time until the next egg is spawned. */
    public int timeUntilNextBigEgg;

    public EntityGiantChickenBase(World aWorld)
    {
        super(aWorld);
        this.setSize(0.6F, 1.4F); //Double
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.timeUntilNextBigEgg = this.rand.nextInt(16000) + 4000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, ModItems.itemGemShards, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(8, new EntityAIEatGrass(this));
        
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.field_70888_h = this.field_70886_e;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);

        if (this.destPos < 0.0F)
        {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.field_70889_i < 1.0F)
        {
            this.field_70889_i = 1.0F;
        }

        this.field_70889_i = (float)((double)this.field_70889_i * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.field_70886_e += this.field_70889_i * 2.0F;

        if (!this.worldObj.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
        if (!this.worldObj.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(ModItems.itemBigEgg, MathUtils.randInt(1, 4));
            this.timeUntilNextBigEgg = this.rand.nextInt(16000) + 4000;
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_) {}

    protected Item getDropItem()
    {
        return ModItems.itemBigEgg;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean recentHit, int lootLevel)
    {
        int j = this.rand.nextInt(3) + this.rand.nextInt(1 + lootLevel);

        for (int k = 0; k < j; ++k)
        {
            this.dropItem(Items.feather, 1);
        }
        
        //Large Egg Chance
        for (int k = 0; k < j*2; ++k)
        {
            this.dropItem(ModItems.itemBigEgg, MathUtils.getRandomFromArray(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2}));
        }

        //Chicken Corpses Dropped
        int mBodies = MathUtils.getRandomFromArray(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 6});
        if (this.isBurning())
        {
            this.dropItem(Items.cooked_chicken, mBodies);
        }
        else
        {
            this.dropItem(Items.chicken, mBodies);
        }
    }

    public EntityGiantChickenBase createChild(EntityAgeable p_90011_1_)
    {
        return new EntityGiantChickenBase(this.worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack aStack)
    {
        return aStack != null && aStack.getItem() instanceof ItemGemShards;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound aNBT)
    {
        super.readEntityFromNBT(aNBT);
        timeUntilNextBigEgg = aNBT.getInteger("timeUntilNextBigEgg");
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        return this.isChickenJockey() ? 20 : super.getExperiencePoints(p_70693_1_);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound aNBT)
    {
        super.writeEntityToNBT(aNBT);
       	aNBT.setInteger("timeUntilNextBigEgg", timeUntilNextBigEgg);
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return this.isChickenJockey() && this.riddenByEntity == null;
    }

    public void updateRiderPosition()
    {
        super.updateRiderPosition();
        float f = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0F);
        float f1 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        this.riddenByEntity.setPosition(this.posX + (double)(f2 * f), this.posY + (double)(this.height * 0.5F) + this.riddenByEntity.getYOffset() + (double)f3, this.posZ - (double)(f2 * f1));

        if (this.riddenByEntity instanceof EntityLivingBase)
        {
            ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
        }
    }
    
    public boolean isChickenJockey() {
    	return func_152116_bZ();
    }
    public void setIsChickenJockey(boolean isJockey) {
    	func_152117_i(isJockey);
    }

	@Override
	protected Entity findPlayerToAttack() {
		// TODO Auto-generated method stub
		return super.findPlayerToAttack();
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		// TODO Auto-generated method stub
		return super.interact(p_70085_1_);
	}

	@Override
	public void setHomeArea(int p_110171_1_, int p_110171_2_, int p_110171_3_, int p_110171_4_) {
		// TODO Auto-generated method stub
		super.setHomeArea(p_110171_1_, p_110171_2_, p_110171_3_, p_110171_4_);
	}

	@Override
	public ChunkCoordinates getHomePosition() {
		// TODO Auto-generated method stub
		return super.getHomePosition();
	}

	@Override
	public void detachHome() {
		// TODO Auto-generated method stub
		super.detachHome();
	}

	@Override
	public boolean hasHome() {
		// TODO Auto-generated method stub
		return super.hasHome();
	}

	@Override
	public void eatGrassBonus() {
        if (this.isChild()) {
            this.addGrowth(60);
        }
	}

	@Override
	public float getRenderSizeModifier() {
		return 1.5f;
	}

	@Override
	protected void jump() {
		this.motionY = 0.68999998688697815D;
        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.5F);
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		this.spawnExplosionParticle();
		super.onStruckByLightning(p_70077_1_);
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return true;
	}
}