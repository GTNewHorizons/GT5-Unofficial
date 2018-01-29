package gtPlusPlus.core.world.explosions;

import java.util.*;

import gtPlusPlus.api.objects.XSTR;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class MiningExplosion extends Explosion {
	/** whether or not the explosion sets fire to blocks around it */
	public boolean isFlaming = false;
	/** whether or not this explosion spawns smoke particles */
	public boolean isSmoking = true;
	private final int field_77289_h = 16;
	private final Random explosionRNG = new XSTR();
	private final World worldObj;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public Entity exploder;
	public float explosionSize;
	/** A list of ChunkPositions of blocks affected by this explosion */
	public List<ChunkPosition> affectedBlockPositions = new ArrayList<>();
	private final Map<Entity, Vec3> field_77288_k = new HashMap<>();
	public MiningExplosion(final World worldObj, final Entity entityObj, final double x, final double y, final double z, final float size)
	{
		super(worldObj, entityObj, x, y, z, size);
		this.worldObj = worldObj;
		this.exploder = entityObj;
		this.explosionSize = size;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doExplosionA()
	{
		final float f = this.explosionSize;
		final HashSet<ChunkPosition> hashset = new HashSet<>();
		int i;
		int j;
		int k;
		double d5;
		double d6;
		double d7;

		for (i = 0; i < this.field_77289_h; ++i)
		{
			for (j = 0; j < this.field_77289_h; ++j)
			{
				for (k = 0; k < this.field_77289_h; ++k)
				{
					if ((i == 0) || (i == (this.field_77289_h - 1)) || (j == 0) || (j == (this.field_77289_h - 1)) || (k == 0) || (k == (this.field_77289_h - 1)))
					{
						double d0 = ((i / (this.field_77289_h - 1.0F)) * 2.0F) - 1.0F;
						double d1 = ((j / (this.field_77289_h - 1.0F)) * 2.0F) - 1.0F;
						double d2 = ((k / (this.field_77289_h - 1.0F)) * 2.0F) - 1.0F;
						final double d3 = Math.sqrt((d0 * d0) + (d1 * d1) + (d2 * d2));
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f1 = this.explosionSize * (0.7F + (this.worldObj.rand.nextFloat() * 0.6F));
						d5 = this.explosionX;
						d6 = this.explosionY;
						d7 = this.explosionZ;

						for (final float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F)
						{
							final int j1 = MathHelper.floor_double(d5);
							final int k1 = MathHelper.floor_double(d6);
							final int l1 = MathHelper.floor_double(d7);
							final Block block = this.worldObj.getBlock(j1, k1, l1);

							if (block.getMaterial() != Material.air)
							{
								final float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.worldObj, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, this.worldObj, j1, k1, l1, this.explosionX, this.explosionY, this.explosionZ);
								f1 -= (f3 + 0.3F) * f2;
							}

							if ((f1 > 0.0F) && ((this.exploder == null) || this.exploder.func_145774_a(this, this.worldObj, j1, k1, l1, block, f1)))
							{
								hashset.add(new ChunkPosition(j1, k1, l1));
							}

							d5 += d0 * f2;
							d6 += d1 * f2;
							d7 += d2 * f2;
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(hashset);
		this.explosionSize *= 2.0F;
		i = MathHelper.floor_double(this.explosionX - this.explosionSize - 1.0D);
		j = MathHelper.floor_double(this.explosionX + this.explosionSize + 1.0D);
		k = MathHelper.floor_double(this.explosionY - this.explosionSize - 1.0D);
		final int i2 = MathHelper.floor_double(this.explosionY + this.explosionSize + 1.0D);
		final int l = MathHelper.floor_double(this.explosionZ - this.explosionSize - 1.0D);
		final int j2 = MathHelper.floor_double(this.explosionZ + this.explosionSize + 1.0D);
		final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox(i, k, l, j, i2, j2));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, this.explosionSize);
		final Vec3 vec3 = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

		for (int i1 = 0; i1 < list.size(); ++i1)
		{
			final Entity entity = list.get(i1);
			final double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / this.explosionSize;

			if (d4 <= 1.0D)
			{
				d5 = entity.posX - this.explosionX;
				d6 = (entity.posY + entity.getEyeHeight()) - this.explosionY;
				d7 = entity.posZ - this.explosionZ;
				final double d9 = MathHelper.sqrt_double((d5 * d5) + (d6 * d6) + (d7 * d7));

				if (d9 != 0.0D)
				{
					d5 /= d9;
					d6 /= d9;
					d7 /= d9;
					final double d10 = this.worldObj.getBlockDensity(vec3, entity.boundingBox);
					final double d11 = (1.0D - d4) * d10;
					entity.attackEntityFrom(DamageSource.setExplosionSource(this), ((int)(((((d11 * d11) + d11) / 2.0D) * 8.0D * this.explosionSize) + 1.0D)));
					final double d8 = EnchantmentProtection.func_92092_a(entity, d11);
					entity.motionX += d5 * d8;
					entity.motionY += d6 * d8;
					entity.motionZ += d7 * d8;

					if (entity instanceof EntityPlayer)
					{
						this.field_77288_k.put(entity, Vec3.createVectorHelper(d5 * d11, d6 * d11, d7 * d11));
					}
				}
			}
		}

		this.explosionSize = f;
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	@Override
	public void doExplosionB(final boolean p_77279_1_)
	{
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + ((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F)) * 0.7F);

		if ((this.explosionSize >= 2.0F) && this.isSmoking)
		{
			this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("smoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("largesmoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("cloud", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("flame", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("explode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("largeexplode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("hugeexplosion", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
		}
		else
		{
			this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("smoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("largesmoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("cloud", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("flame", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("explode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("largeexplode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
		}

		Iterator<ChunkPosition> iterator;
		ChunkPosition chunkposition;
		int i;
		int j;
		int k;
		Block block;

		if (this.isSmoking)
		{
			iterator = this.affectedBlockPositions.iterator();

			while (iterator.hasNext())
			{
				chunkposition = iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = this.worldObj.getBlock(i, j, k);

				if (p_77279_1_)
				{
					final double d0 = i + this.worldObj.rand.nextFloat();
					final double d1 = j + this.worldObj.rand.nextFloat();
					final double d2 = k + this.worldObj.rand.nextFloat();
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					final double d6 = MathHelper.sqrt_double((d3 * d3) + (d4 * d4) + (d5 * d5));
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / ((d6 / this.explosionSize) + 0.1D);
					d7 *= (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat()) + 0.3F;
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					this.worldObj.spawnParticle("explode", (d0 + (this.explosionX * 1.0D)) / 2.0D, (d1 + (this.explosionY * 1.0D)) / 2.0D, (d2 + (this.explosionZ * 1.0D)) / 2.0D, d3, d4, d5);
					this.worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
					this.worldObj.spawnParticle("smoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("cloud", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("flame", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("explode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largeexplode", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("hugeexplosion", this.explosionX+MathUtils.randDouble(0, 1), this.explosionY+MathUtils.randDouble(0, 1), this.explosionZ+MathUtils.randDouble(0, 1), 0.0D, 0.0D, 0.0D);
				
				}

				if (block.getMaterial() != Material.air)
				{
					if (block.canDropFromExplosion(this))
					{
						//world, x, y, z, world.getBlockMetadata(x, y, z), dropProb, 0
						block.dropBlockAsItemWithChance(this.worldObj, i, j, k, this.worldObj.getBlockMetadata(i, j, k), 1F, 0);
					}

					block.onBlockExploded(this.worldObj, i, j, k, this);
				}
			}
		}

		if (this.isFlaming)
		{
			iterator = this.affectedBlockPositions.iterator();

			while (iterator.hasNext())
			{
				chunkposition = iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = this.worldObj.getBlock(i, j, k);
				final Block block1 = this.worldObj.getBlock(i, j - 1, k);

				if ((block.getMaterial() == Material.air) && block1.func_149730_j() && (this.explosionRNG.nextInt(3) == 0))
				{
					this.worldObj.setBlock(i, j, k, Blocks.fire);
				}
			}
		}
	}

	@Override
	public Map<Entity, Vec3> func_77277_b()
	{
		return this.field_77288_k;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
	 */
	@Override
	public EntityLivingBase getExplosivePlacedBy()
	{
		return this.exploder == null ? null : (this.exploder instanceof EntityPrimedMiningExplosive ? ((EntityPrimedMiningExplosive)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
	}
}