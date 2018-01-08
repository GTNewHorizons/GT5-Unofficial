package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.util.inventory.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FishTrap extends BlockContainer implements ITileTooltip
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	/**
	 * Determines which tooltip is displayed within the itemblock.
	 */
	private final int mTooltipID = 0;

	@Override
	public int getTooltipID() {
		return this.mTooltipID;
	}

	@SuppressWarnings("deprecation")
	public FishTrap()
	{
		super(Material.iron);
		this.setBlockName("blockFishTrap");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockFishTrap");
		LanguageRegistry.addName(this, "Fish Catcher");

	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_)
	{
		return p_149691_1_ == 1 ? this.textureTop : (p_149691_1_ == 0 ? this.textureBottom : ((p_149691_1_ != 2) && (p_149691_1_ != 4) ? this.blockIcon : this.textureFront));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_)
	{
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "fishtrap");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz)
	{
		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileEntityFishTrap)){
			player.openGui(GTplusplus.instance, 5, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileEntityFishTrap();
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int number) {
		InventoryUtils.dropInventoryItems(world, x, y, z, block);
		super.breakBlock(world, x, y, z, block, number);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack stack) {
		if (stack.hasDisplayName()) {
			((TileEntityFishTrap) world.getTileEntity(x,y,z)).setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	/*@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		TileEntityFishTrap te = (TileEntityFishTrap) world.getTileEntity(pos);
	    InventoryHelper.dropInventoryItems(world, pos, te);
	    super.breakBlock(world, pos, blockstate);
	}


	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	    if (stack.hasDisplayName()) {
	        ((TileEntityFishTrap) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	    }
	}*/

}