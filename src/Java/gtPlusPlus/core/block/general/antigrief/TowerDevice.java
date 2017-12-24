package gtPlusPlus.core.block.general.antigrief;

import static gtPlusPlus.core.block.ModBlocks.blockGriefSaver;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityReverter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TowerDevice extends Block {
	private static IIcon TEX_ANTIBUILDER;
	public static final int META_ANTIBUILDER = 9;
	private boolean bUnbreakable;

	public TowerDevice()
	{
		super(Material.wood);
		this.setHardness(10.0F);
		this.setResistance(35.0F);
		this.setStepSound(Block.soundTypeWood);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	public int tickRate()
	{
		return 15;
	}

	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setBoolean("bUnbreakable", this.bUnbreakable);
	}

	public void loadNBTData(final NBTTagCompound aNBT) {
		this.bUnbreakable = aNBT.getBoolean("bUnbreakable");
	}

	@Override
	public IIcon getIcon(final int side, final int meta)
	{
		return TEX_ANTIBUILDER;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister par1IconRegister)
	{
		TEX_ANTIBUILDER = par1IconRegister.registerIcon(CORE.MODID + ":" + "blockAntiGrief");
	}

	@Override
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 9));
	}

	@Override
	public boolean onBlockActivated(final World par1World, final int x, final int y, final int z, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9)
	{
		final int meta = par1World.getBlockMetadata(x, y, z);
		return false;
	}

	@Override
	public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y, final int z, final double explosionX, final double explosionY, final double explosionZ)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public float getBlockHardness(final World world, final int x, final int y, final int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		return super.getBlockHardness(world, x, y, z);
	}

	public static boolean areNearbyLockBlocks(final World world, final int x, final int y, final int z)
	{
		boolean locked = false;
		for (int dx = x - 2; dx <= (x + 2); dx++) {
			for (int dy = y - 2; dy <= (y + 2); dy++) {
				for (int dz = z - 2; dz <= (z + 2); dz++) {
					if ((world.getBlock(dx, dy, dz) == blockGriefSaver) && (world.getBlockMetadata(dx, dy, dz) == 4)) {
						locked = true;
					}
				}
			}
		}
		return locked;
	}

	public static void unlockBlock(final World par1World, final int x, final int y, final int z)
	{
		final Block thereBlockID = par1World.getBlock(x, y, z);
		final int thereBlockMeta = par1World.getBlockMetadata(x, y, z);
		if ((thereBlockID == blockGriefSaver) || (thereBlockMeta == 4))
		{
			changeToBlockMeta(par1World, x, y, z, 5);
			par1World.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.click", 0.3F, 0.6F);
		}
	}

	private static void changeToBlockMeta(final World par1World, final int x, final int y, final int z, final int meta)
	{
		final Block thereBlockID = par1World.getBlock(x, y, z);
		if ((thereBlockID == blockGriefSaver))
		{
			par1World.setBlock(x, y, z, thereBlockID, meta, 3);
			par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
			par1World.notifyBlocksOfNeighborChange(x, y, z, thereBlockID);
		}
	}

	@Override
	public void onBlockAdded(final World par1World, final int x, final int y, final int z)
	{
		final int meta = par1World.getBlockMetadata(x, y, z);
		if (!par1World.isRemote) {

		}
	}

	@Override
	public void onNeighborBlockChange(final World par1World, final int x, final int y, final int z, final Block myBlockID)
	{
		final int meta = par1World.getBlockMetadata(x, y, z);
		if (!par1World.isRemote)
		{

		}
	}

	@Override
	public void updateTick(final World par1World, final int x, final int y, final int z, final Random par5Random)
	{
		if (!par1World.isRemote)
		{
			final int meta = par1World.getBlockMetadata(x, y, z);
		}
	}

	private void letsBuild(final World par1World, final int x, final int y, final int z)
	{

	}

	private boolean isInactiveTrapCharged(final World par1World, final int x, final int y, final int z)
	{
		return false;
	}

	private boolean isReactorReady(final World world, final int x, final int y, final int z)
	{
		if ((world.getBlock(x, y + 1, z) != Blocks.redstone_block) ||
				(world.getBlock(x, y - 1, z) != Blocks.redstone_block) ||
				(world.getBlock(x + 1, y, z) != Blocks.redstone_block) ||
				(world.getBlock(x - 1, y, z) != Blocks.redstone_block) ||
				(world.getBlock(x, y, z + 1) != Blocks.redstone_block) ||
				(world.getBlock(x, y, z - 1) != Blocks.redstone_block)) {
			return false;
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World par1World, final int x, final int y, final int z, final Random par5Random)
	{
		final int meta = par1World.getBlockMetadata(x, y, z);
		if ((meta == 3) || (meta == 1) || (meta == 9)) {
			for (int i = 0; i < 1; i++) {
				this.sparkle(par1World, x, y, z, par5Random);
			}
		}
	}

	public void sparkle(final World world, final int x, final int y, final int z, final Random rand)
	{
		final double offset = 0.0625D;
		for (int side = 0; side < 6; side++)
		{
			double rx = x + rand.nextFloat();
			double ry = y + rand.nextFloat();
			double rz = z + rand.nextFloat();
			if ((side == 0) && (!world.getBlock(x, y + 1, z).isOpaqueCube())) {
				ry = y + 1 + offset;
			}
			if ((side == 1) && (!world.getBlock(x, y - 1, z).isOpaqueCube())) {
				ry = (y + 0) - offset;
			}
			if ((side == 2) && (!world.getBlock(x, y, z + 1).isOpaqueCube())) {
				rz = z + 1 + offset;
			}
			if ((side == 3) && (!world.getBlock(x, y, z - 1).isOpaqueCube())) {
				rz = (z + 0) - offset;
			}
			if ((side == 4) && (!world.getBlock(x + 1, y, z).isOpaqueCube())) {
				rx = x + 1 + offset;
			}
			if ((side == 5) && (!world.getBlock(x - 1, y, z).isOpaqueCube())) {
				rx = (x + 0) - offset;
			}
			if ((rx < x) || (rx > (x + 1)) || (ry < 0.0D) || (ry > (y + 1)) || (rz < z) || (rz > (z + 1))) {
				world.spawnParticle("reddust", rx, ry, rz, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public static void checkAndActivateVanishBlock(final World world, final int x, final int y, final int z)
	{
		final Block thereID = world.getBlock(x, y, z);
		final int thereMeta = world.getBlockMetadata(x, y, z);
	}

	public static void changeToActiveVanishBlock(final World par1World, final int x, final int y, final int z, final int meta)
	{
		changeToBlockMeta(par1World, x, y, z, meta);
		par1World.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.pop", 0.3F, 0.6F);

		final Block thereBlockID = par1World.getBlock(x, y, z);
		par1World.scheduleBlockUpdate(x, y, z, thereBlockID, getTickRateFor(thereBlockID, meta, par1World.rand));
	}

	private static int getTickRateFor(final Block thereBlockID, final int meta, final Random rand)
	{
		return 15;
	}

	@Override
	public int getLightValue(final IBlockAccess world, final int x, final int y, final int z)
	{
		final Block blockID = world.getBlock(x, y, z);
		final int meta = world.getBlockMetadata(x, y, z);
		if (blockID != this) {
			return 0;
		}
		return 10;
	}

	@Override
	public boolean hasTileEntity(final int metadata)
	{
		return (metadata == 0);
	}

	@Override
	public TileEntity createTileEntity(final World world, final int metadata)
	{
		if (metadata == 0) {
			Logger.INFO("I have been created. [Antigriefer]"+this.getLocalizedName());
			return new TileEntityReverter();
		}
		return null;
	}

	@Override
	public Item getItemDropped(final int meta, final Random par2Random, final int par3)
	{
		switch (meta)
		{
		case 0:
			return null;
		}
		return Item.getItemFromBlock(this);
	}

	@Override
	public int damageDropped(final int meta)
	{
		return meta;
	}
}
