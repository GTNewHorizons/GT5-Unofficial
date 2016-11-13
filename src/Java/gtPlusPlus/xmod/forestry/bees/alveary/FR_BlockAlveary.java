package gtPlusPlus.xmod.forestry.bees.alveary;

import java.util.ArrayList;
import java.util.List;

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
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.apiculture.MaterialBeehive;
import forestry.apiculture.multiblock.TileAlvearyPlain;
import forestry.core.blocks.BlockForestry;
import forestry.core.render.TextureManager;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class FR_BlockAlveary extends BlockForestry
{

	public static enum Type
	{
		PLAIN,
		ERROR,
		FRAME,
		MUTATOR, 
		
		//Placeholder Values
		HEATER, HYGRO, STABILIZER, SIEVE;

		public static final Type[] VALUES = values();

		private Type() {}
	}

	public FR_BlockAlveary()
	{
		super(new MaterialBeehive(false));
		setHardness(1.0F);
		setCreativeTab(AddToCreativeTab.tabBlock);
		setHarvestLevel("axe", 0);
		
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz)
    {
        if (world.isRemote) return true;
 
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileAlvearyFrameHousing)
        {
            player.openGui(GTplusplus.instance, 0, world, x, y, z);
            return true;
        }
        /*else if (te != null && te instanceof TileAlvearyFrameHousing)
        {
            player.openGui(GTplusplus.instance, 0, world, x, y, z);
            return true;
        }*/
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 4; i++) {
			if (i != 1 && i != 0) {
				list.add(new ItemStack(item, 1, i));
			}
		}
	}

	@Override
	public int getRenderType()
	{
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		drop.add(new ItemStack(this, 1, metadata != 1 ? metadata : 0));
		return drop;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return meta != 1 ? meta : 0;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if ((metadata < 0) || (metadata > Type.VALUES.length)) {
			return null;
		}
		
		
		
		Type type = Type.VALUES[metadata];
		switch (type)
		{
		case FRAME: 
			LanguageRegistry.addName(this, "Alveary Frame Housing");
		case MUTATOR: 
			LanguageRegistry.addName(this, "Alveary Mutator Block");
		case ERROR: 
			LanguageRegistry.addName(this, "Invalid Alveary Block");
		default:
			LanguageRegistry.addName(this, "Unnamed Alveary Block");   
		}
		switch (type)
		{
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

	@Override
	public Block setBlockName(String name) {
		//int meta = this.
		return super.setBlockName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return createTileEntity(world, meta);
	}

	/* ICONS */
	public static final int PLAIN = 0;
	public static final int ENTRANCE = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int ALVEARY_FRAME_OFF = 5;
	public static final int ALVEARY_FRAME_ON = 6;
	public static final int ALVEARY_MUTATOR_OFF = 7;
	public static final int ALVEARY_MUTATOR_ON = 8;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[9];
		icons[0] = TextureManager.registerTex(register, "apiculture/alveary.plain");
		icons[1] = TextureManager.registerTex(register, "apiculture/alveary.entrance");
		icons[2] = TextureManager.registerTex(register, "apiculture/alveary.bottom");
		icons[3] = TextureManager.registerTex(register, "apiculture/alveary.left");
		icons[4] = TextureManager.registerTex(register, "apiculture/alveary.right");
		icons[5] = TextureManager.registerTex(register, "apiculture/alveary.framehousing.off");
		icons[6] = TextureManager.registerTex(register, "apiculture/alveary.framehousing.on");
		icons[7] = TextureManager.registerTex(register, "apiculture/alveary.mutator.off");
		icons[8] = TextureManager.registerTex(register, "apiculture/alveary.mutator.on");
	}
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		if ((metadata <= 1
				|| metadata == Type.FRAME.ordinal() || metadata == Type.MUTATOR.ordinal())
				&& (side == 1 || side == 0)) {
			return icons[BOTTOM];
		}
		Type type = Type.VALUES[metadata];
		switch (type) {
		case ERROR:
			return icons[PLAIN];
		case FRAME:
			return icons[ALVEARY_FRAME_OFF];
		case MUTATOR:
			return icons[ALVEARY_MUTATOR_OFF];
		case HEATER:
			return icons[ALVEARY_MUTATOR_OFF];
		case HYGRO:
			return icons[ALVEARY_MUTATOR_OFF];
		case STABILIZER:
			return icons[PLAIN];
		case SIEVE:
			return icons[PLAIN];
		default:
			return null;
		}
	}
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 1) {
			return this.getIcon(side, meta);
		} else if (meta > 1) {
			return getBlockTextureFromSideAndTile(world, x, y, z, side);
		}
		Block blockXP = world.getBlock(x + 1, y, z);
		Block blockXM = world.getBlock(x - 1, y, z);
		if (blockXP == this && blockXM != this) {
			if (world.getBlockMetadata(x + 1, y, z) == 1) {
				if (world.getBlock(x, y, z + 1) != this) {
					return switchForSide(42, side);
				}
				return switchForSide(41, side);
			}
			return this.getIcon(side, meta);
		} else if (blockXP != this && blockXM == this) {
			if (world.getBlockMetadata(x - 1, y, z) == 1) {
				if (world.getBlock(x, y, z + 1) != this) {
					return switchForSide(41, side);
				}
				return switchForSide(42, side);
			}
			return this.getIcon(side, meta);
		}
		return this.getIcon(side, meta);
	}
	@SideOnly(Side.CLIENT)
	private IIcon getBlockTextureFromSideAndTile(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (!(tile instanceof FR_TileAlveary)) {
			return getIcon(side, 0);
		}
		return icons[((FR_TileAlveary) tile).getIcon(side)];
	}
	@SideOnly(Side.CLIENT)
	private IIcon switchForSide(int textureId, int side) {
		if (side == 4 || side == 5) {
			if (textureId == 41) {
				return icons[LEFT];
			}
			return icons[RIGHT];
		} else if (textureId == 41) {
			return icons[RIGHT];
		} else {
			return icons[LEFT];
		}
	}
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof FR_TileAlveary) {
			FR_TileAlveary tileAlveary = (FR_TileAlveary) tileEntity;
			// We must check that the slabs on top were not removed
			tileAlveary.getMultiblockLogic().getController().reassemble();
		}
	}
	public ItemStack get(Type type) {
		return new ItemStack(this, 1, type.ordinal());
	}
}