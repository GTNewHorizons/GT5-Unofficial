package gtPlusPlus.xmod.forestry.bees.alveary;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.apiculture.MaterialBeehive;
import forestry.apiculture.multiblock.TileAlvearyPlain;
import forestry.core.blocks.BlockStructure;
import forestry.core.render.TextureManager;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FR_BlockAlveary extends BlockStructure {

	public static enum Type {
		PLAIN, ERROR, FRAME, MUTATOR,

		// Placeholder Values
		HEATER, HYGRO, STABILIZER, SIEVE;

		public static final Type[] VALUES = Type.values();

		private Type() {
		}
	}

	/* ICONS */
	public static final int	PLAIN				= 0;

	public static final int	ENTRANCE			= 1;

	public static final int	BOTTOM				= 2;

	public static final int	LEFT				= 3;

	public static final int	RIGHT				= 4;

	public static final int	ALVEARY_FRAME_OFF	= 5;

	public static final int	ALVEARY_FRAME_ON	= 6;

	public static final int	ALVEARY_MUTATOR_OFF	= 7;

	public static final int	ALVEARY_MUTATOR_ON	= 8;

	@SideOnly(Side.CLIENT)
	private IIcon[]			icons;

	public FR_BlockAlveary() {
		super(new MaterialBeehive(false));
		this.setHardness(1.0F);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setHarvestLevel("axe", 0);

	}
	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return this.createTileEntity(world, meta);
	}
	@Override
	public TileEntity createTileEntity(final World world, final int metadata) {
		if (metadata < 0 || metadata > Type.VALUES.length) {
			return null;
		}

		final Type type = Type.VALUES[metadata];
		switch (type) {
			case FRAME:
				LanguageRegistry.addName(this, "Alveary Frame Housing");
			case MUTATOR:
				LanguageRegistry.addName(this, "Alveary Mutator Block");
			case ERROR:
				LanguageRegistry.addName(this, "Invalid Alveary Block");
			default:
				LanguageRegistry.addName(this, "Unnamed Alveary Block");
		}
		switch (type) {
			case FRAME:
				return new TileAlvearyFrameHousing();
			case MUTATOR:
				return new TileAlvearyPlain();
			case ERROR:
				return new TileAlvearyPlain();
			default:
				return new TileAlvearyPlain();
		}
	}
	public ItemStack get(final Type type) {
		return new ItemStack(this, 1, type.ordinal());
	}
	@SideOnly(Side.CLIENT)
	private IIcon getBlockTextureFromSideAndTile(final IBlockAccess world, final int x, final int y, final int z,
			final int side) {
		final TileEntity tile = world.getTileEntity(x, y, z);
		if (!(tile instanceof FR_TileAlveary)) {
			return this.getIcon(side, 0);
		}
		return this.icons[((FR_TileAlveary) tile).getIcon(side)];
	}
	@Override
	public int getDamageValue(final World world, final int x, final int y, final int z) {
		final int meta = world.getBlockMetadata(x, y, z);
		return meta != 1 ? meta : 0;
	}
	@Override
	public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata,
			final int fortune) {
		final ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		drop.add(new ItemStack(this, 1, metadata != 1 ? metadata : 0));
		return drop;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		final int meta = world.getBlockMetadata(x, y, z);
		if (meta == 1) {
			return this.getIcon(side, meta);
		}
		else if (meta > 1) {
			return this.getBlockTextureFromSideAndTile(world, x, y, z, side);
		}
		final Block blockXP = world.getBlock(x + 1, y, z);
		final Block blockXM = world.getBlock(x - 1, y, z);
		if (blockXP == this && blockXM != this) {
			if (world.getBlockMetadata(x + 1, y, z) == 1) {
				if (world.getBlock(x, y, z + 1) != this) {
					return this.switchForSide(42, side);
				}
				return this.switchForSide(41, side);
			}
			return this.getIcon(side, meta);
		}
		else if (blockXP != this && blockXM == this) {
			if (world.getBlockMetadata(x - 1, y, z) == 1) {
				if (world.getBlock(x, y, z + 1) != this) {
					return this.switchForSide(41, side);
				}
				return this.switchForSide(42, side);
			}
			return this.getIcon(side, meta);
		}
		return this.getIcon(side, meta);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final int side, final int metadata) {
		if ((metadata <= 1 || metadata == Type.FRAME.ordinal() || metadata == Type.MUTATOR.ordinal())
				&& (side == 1 || side == 0)) {
			return this.icons[FR_BlockAlveary.BOTTOM];
		}
		final Type type = Type.VALUES[metadata];
		switch (type) {
			case ERROR:
				return this.icons[FR_BlockAlveary.PLAIN];
			case FRAME:
				return this.icons[FR_BlockAlveary.ALVEARY_FRAME_OFF];
			case MUTATOR:
				return this.icons[FR_BlockAlveary.ALVEARY_MUTATOR_OFF];
			case HEATER:
				return this.icons[FR_BlockAlveary.ALVEARY_MUTATOR_OFF];
			case HYGRO:
				return this.icons[FR_BlockAlveary.ALVEARY_MUTATOR_OFF];
			case STABILIZER:
				return this.icons[FR_BlockAlveary.PLAIN];
			case SIEVE:
				return this.icons[FR_BlockAlveary.PLAIN];
			default:
				return null;
		}
	}
	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for (int i = 0; i < 4; i++) {
			if (i != 1 && i != 0) {
				list.add(new ItemStack(item, 1, i));
			}
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float lx, final float ly, final float lz) {
		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileAlvearyFrameHousing) {
			player.openGui(GTplusplus.instance, 0, world, x, y, z);
			return true;
		}
		/*
		 * else if (te != null && te instanceof TileAlvearyFrameHousing) {
		 * player.openGui(GTplusplus.instance, 0, world, x, y, z); return true;
		 * }
		 */
		return false;
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		final TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof FR_TileAlveary) {
			final FR_TileAlveary tileAlveary = (FR_TileAlveary) tileEntity;
			// We must check that the slabs on top were not removed
			tileAlveary.getMultiblockLogic().getController().reassemble();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(final IIconRegister register) {
		this.icons = new IIcon[9];
		this.icons[0] = TextureManager.registerTex(register, "apiculture/alveary.plain");
		this.icons[1] = TextureManager.registerTex(register, "apiculture/alveary.entrance");
		this.icons[2] = TextureManager.registerTex(register, "apiculture/alveary.bottom");
		this.icons[3] = TextureManager.registerTex(register, "apiculture/alveary.left");
		this.icons[4] = TextureManager.registerTex(register, "apiculture/alveary.right");
		this.icons[5] = TextureManager.registerTex(register, "apiculture/alveary.framehousing.off");
		this.icons[6] = TextureManager.registerTex(register, "apiculture/alveary.framehousing.on");
		this.icons[7] = TextureManager.registerTex(register, "apiculture/alveary.mutator.off");
		this.icons[8] = TextureManager.registerTex(register, "apiculture/alveary.mutator.on");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public Block setBlockName(final String name) {
		// int meta = this.
		return super.setBlockName(name);
	}

	@SideOnly(Side.CLIENT)
	private IIcon switchForSide(final int textureId, final int side) {
		if (side == 4 || side == 5) {
			if (textureId == 41) {
				return this.icons[FR_BlockAlveary.LEFT];
			}
			return this.icons[FR_BlockAlveary.RIGHT];
		}
		else if (textureId == 41) {
			return this.icons[FR_BlockAlveary.RIGHT];
		}
		else {
			return this.icons[FR_BlockAlveary.LEFT];
		}
	}
}