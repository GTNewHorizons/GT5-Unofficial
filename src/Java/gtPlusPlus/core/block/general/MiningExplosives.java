package gtPlusPlus.core.block.general;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.lib.CORE;

public class MiningExplosives extends BlockTNT {
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;

	public MiningExplosives(){
		this.setBlockName("blockMiningExplosives");
		GameRegistry.registerBlock(this, "blockMiningExplosives");
		LanguageRegistry.addName(this, "Earth Blasting Explosives");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_){
		return p_149691_1_ == 0 ? this.textureBottom : (p_149691_1_ == 1 ? this.textureTop : this.blockIcon);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z){
		super.onBlockAdded(world, x, y, z);

		if (world.isBlockIndirectlyGettingPowered(x, y, z))
		{
			this.onBlockDestroyedByPlayer(world, x, y, z, 1);
			world.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor Block
	 */
	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block neighbourblock){
		if (world.isBlockIndirectlyGettingPowered(x, y, z))
		{
			this.onBlockDestroyedByPlayer(world, x, y, z, 1);
			world.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(final Random random){
		return 1;
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	@Override
	public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion bang){
		if (!world.isRemote)
		{
			final EntityPrimedMiningExplosive EntityPrimedMiningExplosive = new EntityPrimedMiningExplosive(world, x + 0.5F, y + 0.5F, z + 0.5F, bang.getExplosivePlacedBy());
			EntityPrimedMiningExplosive.fuse = world.rand.nextInt(EntityPrimedMiningExplosive.fuse / 4) + (EntityPrimedMiningExplosive.fuse / 8);
			world.spawnEntityInWorld(EntityPrimedMiningExplosive);
		}
	}

	/**
	 * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
	 */
	@Override
	public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int meta){
		this.func_150114_a(world, x, y, z, meta, (EntityLivingBase)null);
	}

	//TODO Spawns Primed TNT?
	@Override
	public void func_150114_a(final World world, final int p_150114_2_, final int p_150114_3_, final int p_150114_4_, final int p_150114_5_, final EntityLivingBase entityLiving){
		if (!world.isRemote)
		{
			if ((p_150114_5_ & 1) == 1)
			{
				final EntityPrimedMiningExplosive EntityPrimedMiningExplosive = new EntityPrimedMiningExplosive(world, p_150114_2_ + 0.5F, p_150114_3_ + 0.5F, p_150114_4_ + 0.5F, entityLiving);
				world.spawnEntityInWorld(EntityPrimedMiningExplosive);
				world.playSoundAtEntity(EntityPrimedMiningExplosive, "game.tnt.primed", 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer clickingPlayer, final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_){
		if ((clickingPlayer.getCurrentEquippedItem() != null) && (clickingPlayer.getCurrentEquippedItem().getItem() == Items.flint_and_steel))
		{
			this.func_150114_a(world, x, y, z, 1, clickingPlayer);
			world.setBlockToAir(x, y, z);
			clickingPlayer.getCurrentEquippedItem().damageItem(1, clickingPlayer);
			return true;
		}
		else
		{
			return super.onBlockActivated(world, x, y, z, clickingPlayer, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
		}
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	@Override
	public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entityTriggering){
		if ((entityTriggering instanceof EntityArrow) && !world.isRemote)
		{
			final EntityArrow entityarrow = (EntityArrow)entityTriggering;

			if (entityarrow.isBurning())
			{
				this.func_150114_a(world, x, y, z, 1, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	@Override
	public boolean canDropFromExplosion(final Explosion bang){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister){
		//
		/*this.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_side");
		this.textureTop = iconRegister.registerIcon(this.getTextureName() + "_top");
		this.textureBottom = iconRegister.registerIcon(this.getTextureName() + "_bottom");*/
		this.blockIcon = iconRegister.registerIcon(CORE.MODID + ":" + "Chrono/" + "MetalSheet2");
		this.textureTop = iconRegister.registerIcon(CORE.MODID + ":" + "Chrono/" + "MetalFunnel");
		this.textureBottom = iconRegister.registerIcon(CORE.MODID + ":" + "Chrono/" + "MetalPanel");
	}
}