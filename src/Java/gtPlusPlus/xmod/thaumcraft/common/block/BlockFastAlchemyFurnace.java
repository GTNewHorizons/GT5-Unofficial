package gtPlusPlus.xmod.thaumcraft.common.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastAlchemyFurnace;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.InventoryUtils;

public class BlockFastAlchemyFurnace extends BlockStoneDevice {

	public IIcon[] iconFurnace;
	public IIcon[] iconPedestal;

	public BlockFastAlchemyFurnace() {
		this.iconFurnace = new IIcon[5];
		this.iconPedestal = new IIcon[2];
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setBlockName("blockFastAlchemyFurnace");
		this.setHardness(3.0f);
		this.setResistance(25.0f);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		GameRegistry.registerBlock(this, "blockFastAlchemyFurnace");
		LanguageRegistry.addName(this, "Upgraded Alchemical Furnace");
	}

	@Override
	public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
		final Block block = world.getBlock(x, y, z);
		if (block != this)
		{
			return block.getLightValue(world, x, y, z);
		}
		/**
		 * Gets the light value of the specified block coords. Args: x, y, z
		 */
		return this.getLightValue();
	}

	@Override
	public int damageDropped(final int metadata) {
		return 0;
	}

	@Override
	public TileEntity createTileEntity(final World world, final int metadata) {
		if (metadata == 0){
			return new TileFastAlchemyFurnace();
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int md) {
		return null;
	}

	@Override
	public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int rs) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileFastAlchemyFurnace)) {
			return Container.calcRedstoneFromInventory((IInventory) te);
		}
		return 0;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float par7, final float par8, final float par9) {

		if (world.isRemote) {
			return true;
		}

		final int metadata = world.getBlockMetadata(x, y, z);
		final TileEntity tileEntity = world.getTileEntity(x, y, z);

		if ((metadata == 0) && (tileEntity instanceof TileFastAlchemyFurnace) && !player.isSneaking()) {
			player.openGui(GTplusplus.instance, GuiHandler.GUI8, world, x, y, z);
			return true;
		}
		return false;
	}


	@Override
	public void addCollisionBoxesToList(final World world, final int i, final int j, final int k,
			final AxisAlignedBB axisalignedbb, final List arraylist, final Entity par7Entity) {
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int i, final int j, final int k) {
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		super.setBlockBoundsBasedOnState(world, i, j, k);
	}

	@Override
	public boolean onBlockEventReceived(final World par1World, final int par2, final int par3, final int par4,
			final int par5, final int par6) {
		if (par5 == 1) {
			if (par1World.isRemote) {
				Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 11960575, 2);
				par1World.playAuxSFX(2001, par2, par3, par4, Block.getIdFromBlock(Blocks.stonebrick) + 0);
			}
			return true;
		}
		return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z,
			final ForgeDirection side) {
		return super.isSideSolid(world, x, y, z, side);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5,
			final int par6) {
		InventoryUtils.dropItems(par1World, par2, par3, par4);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block par5) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileFastAlchemyFurnace)) {
			((TileFastAlchemyFurnace) te).getBellows();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World w, final int i, final int j, final int k, final Random r) {
		final TileEntity te = w.getTileEntity(i, j, k);
		if ((te != null) && (te instanceof TileFastAlchemyFurnace) && ((TileFastAlchemyFurnace) te).isBurning()) {
			final float f = i + 0.5f;
			final float f2 = j + 0.2f + ((r.nextFloat() * 5.0f) / 16.0f);
			final float f3 = k + 0.5f;
			final float f4 = 0.52f;
			final float f5 = (r.nextFloat() * 0.5f) - 0.25f;
			w.spawnParticle("smoke", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
			w.spawnParticle("flame", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
			w.spawnParticle("smoke", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
			w.spawnParticle("flame", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
			w.spawnParticle("smoke", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
			w.spawnParticle("flame", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
			w.spawnParticle("smoke", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
			w.spawnParticle("flame", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister ir) {
		this.iconPedestal[0] = ir.registerIcon("thaumcraft:pedestal_side");
		this.iconPedestal[1] = ir.registerIcon("thaumcraft:pedestal_top");
		this.iconFurnace[0] = ir.registerIcon("thaumcraft:al_furnace_side");
		this.iconFurnace[1] = ir.registerIcon("thaumcraft:al_furnace_top");
		this.iconFurnace[2] = ir.registerIcon("thaumcraft:al_furnace_front_off");
		this.iconFurnace[3] = ir.registerIcon("thaumcraft:al_furnace_front_on");
		this.iconFurnace[4] = ir.registerIcon("thaumcraft:al_furnace_top_filled");
	}

	@Override
	public int getRenderType() {
		return ConfigBlocks.blockStoneDeviceRI;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public IIcon getIcon(final int side, final int md) {
		if (md == 0) {
			if (side == 1) {
				return this.iconFurnace[1];
			}
			if (side > 1) {
				return this.iconFurnace[2];
			}
		} else if (md == 1) {
			if (side <= 1) {
				return this.iconPedestal[1];
			}
			if (side > 1) {
				return this.iconPedestal[0];
			}
		}
		return this.iconPedestal[1];
	}

	@Override
	public IIcon getIcon(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int side) {
		final int metadata = iblockaccess.getBlockMetadata(i, j, k);
		if (metadata == 0) {
			final TileEntity te = iblockaccess.getTileEntity(i, j, k);
			if (side == 1) {
				if ((te != null) && (te instanceof TileFastAlchemyFurnace) && (((TileFastAlchemyFurnace) te).vis > 0)) {
					return this.iconFurnace[4];
				}
				return this.iconFurnace[1];
			} else if (side > 1) {
				if ((te != null) && (te instanceof TileFastAlchemyFurnace) && ((TileFastAlchemyFurnace) te).isBurning()) {
					return this.iconFurnace[3];
				}
				return this.iconFurnace[2];
			}
		} else if ((metadata == 1) || (metadata == 5) || (metadata == 8) || (metadata == 12)) {
			return super.getIcon(iblockaccess, i, j, k, side);
		}
		return this.iconFurnace[0];
	}

}