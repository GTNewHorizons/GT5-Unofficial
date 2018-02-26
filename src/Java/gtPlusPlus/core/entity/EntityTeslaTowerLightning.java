package gtPlusPlus.core.entity;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import gtPlusPlus.api.damage.DamageTeslaTower;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.util.minecraft.EntityUtils;

public class EntityTeslaTowerLightning extends EntityWeatherEffect
{
	/** Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc. */
	private int lightningState;
	/** A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt */
	public long boltVertex;
	/** Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time. */
	private int boltLivingTime;

	private final UUID boltID;
	private final UUID boltOwnerID;

	private final Entity boltValidDamageTarget;


	public EntityTeslaTowerLightning(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_, Entity valid, UUID owner)
	{
		super(p_i1703_1_);
		Logger.INFO("Plasma Bolt - Created.");
		Random rand = new XSTR(p_i1703_1_.getSeed());
		this.setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = rand.nextLong();
		this.boltLivingTime = (1) + rand.nextInt(3) + 1;
		this.boltValidDamageTarget = valid;
		this.boltID = UUID.randomUUID();
		this.boltOwnerID = owner;

		//Puts fires out
		if (!p_i1703_1_.isRemote && p_i1703_1_.getGameRules().getGameRuleBooleanValue("doFireTick") && (p_i1703_1_.difficultySetting == EnumDifficulty.NORMAL || p_i1703_1_.difficultySetting == EnumDifficulty.HARD) && p_i1703_1_.doChunksNearChunkExist(MathHelper.floor_double(p_i1703_2_), MathHelper.floor_double(p_i1703_4_), MathHelper.floor_double(p_i1703_6_), 10))
		{
			Logger.INFO("Plasma Bolt - Putting out fires?.");
			int i = MathHelper.floor_double(p_i1703_2_);
			int j = MathHelper.floor_double(p_i1703_4_);
			int k = MathHelper.floor_double(p_i1703_6_);

			if (p_i1703_1_.getBlock(i, j, k).getMaterial() == Material.fire)
			{
				p_i1703_1_.setBlock(i, j, k, Blocks.air);
			}

			for (i = 0; i < 4; ++i)
			{
				j = MathHelper.floor_double(p_i1703_2_) + rand.nextInt(3) - 1;
				k = MathHelper.floor_double(p_i1703_4_) + rand.nextInt(3) - 1;
				int l = MathHelper.floor_double(p_i1703_6_) + rand.nextInt(3) - 1;

				if (p_i1703_1_.getBlock(j, k, l).getMaterial() == Material.fire)
				{
					p_i1703_1_.setBlock(j, k, l, Blocks.air);
				}
			}
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		//Logger.INFO("Zap");
		super.onUpdate();
		Logger.INFO("Plasma Bolt - Tick.");
		Random rand = new XSTR(this.worldObj.getSeed());

		if (this.lightningState == 2)
		{
			Logger.INFO("Plasma Bolt - Playing Sound.");
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
		}
		--this.lightningState;


		if (this.lightningState >= 0)
		{
			Logger.INFO("Plasma Bolt - state >= 0.");
			if (this.worldObj.isRemote)
			{
				Logger.INFO("Plasma Bolt - World is remote, resetting state to 2.");
				this.worldObj.lastLightningBolt = 2;
			}
			else
			{
				Logger.INFO("Plasma Bolt - World is server side.");
				double d0 = 3.0D;
				List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));

				for (int l = 0; l < list.size(); ++l)
				{
					Entity entity = list.get(l);
					if (this.boltValidDamageTarget.getUniqueID().equals(entity.getUniqueID())){
						if (!entity.getUniqueID().equals(boltOwnerID)){
							Logger.INFO("Plasma Bolt - Hurting Entity.");
							Logger.INFO("Plasma Bolt - "+entity.getCommandSenderName()+".");
							//if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this))
							EntityUtils.doFireDamage(entity, 5);
							EntityUtils.doDamage(entity, new DamageTeslaTower(entity), 20);
						}
					}
				}                	

			}
		}

		if (this.lightningState < 0)
		{
			Logger.INFO("Plasma Bolt - state < 0.");
			if (this.boltLivingTime == 0)
			{
				Logger.INFO("Plasma Bolt - setting dead.");
				this.setDead();
			}
			else if (this.lightningState < -rand.nextInt(10))
			{
				Logger.INFO("Plasma Bolt - dunno.");
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = rand.nextLong();    
				//Puts fires out.
				if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10))
				{
					Logger.INFO("Plasma Bolt - Putting fires out [2].");
					int i = MathHelper.floor_double(this.posX);
					int j = MathHelper.floor_double(this.posY);
					int k = MathHelper.floor_double(this.posZ);

					if (this.worldObj.getBlock(i, j, k).getMaterial() == Material.fire)
					{
						this.worldObj.setBlock(i, j, k, Blocks.air);
					}
				}                

			}
		}
	}

	protected void entityInit() {}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}
}