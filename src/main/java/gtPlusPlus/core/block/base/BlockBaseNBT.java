package gtPlusPlus.core.block.base;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockNBT;

public abstract class BlockBaseNBT extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	@SuppressWarnings("deprecation")
	public BlockBaseNBT(final Material material, final String unlocalName, final String displayName){
		super(material);
		this.setBlockName(unlocalName);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockNBT.class, unlocalName);
		//LanguageRegistry.addName(this, displayName);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_){
		return p_149691_1_ == 1 ? this.textureTop : (p_149691_1_ == 0 ? this.textureBottom : ((p_149691_1_ != 2) && (p_149691_1_ != 4) ? this.blockIcon : this.textureFront));
	}

	@Override
	public abstract TileEntity createNewTileEntity(final World world, final int p_149915_2_);

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int meta) {
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int meta) {
		super.onBlockDestroyedByPlayer(world, x, y, z, meta);
	}

	@Override
	public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion) {
		super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
	}

	@Override
	public void onBlockHarvested(final World world, final int x, final int y, final int z, final int meta, final EntityPlayer player) {
		super.onBlockHarvested(world, x, y, z, meta, player);
	}

	@Override
	public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
		super.onBlockExploded(world, x, y, z, explosion);
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

}