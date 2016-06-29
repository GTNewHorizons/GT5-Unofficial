/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package miscutil.core.intermod.forestry.apiculture.blocks;

import java.util.ArrayList;
import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.intermod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.apiculture.MaterialBeehive;
import forestry.apiculture.multiblock.TileAlveary;
import forestry.apiculture.multiblock.TileAlvearyPlain;
import forestry.core.blocks.BlockStructure;
import forestry.core.render.TextureManager;

public class FR_BlockAlveary extends BlockStructure {
	public enum Type {
		PLAIN,
		MUTATRON;
		public static final Type[] VALUES = values();
	}

	public FR_BlockAlveary() {
		super(new MaterialBeehive(false));
		setHardness(1.0f);
		setCreativeTab(AddToCreativeTab.tabMisc);
		setHarvestLevel("axe", 0);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 8; i++) {
			if (i == 1) {
				continue;
			}
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drop = new ArrayList<>();
		drop.add(new ItemStack(this, 1, metadata != 1 ? metadata : 0));
		return drop;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return meta != 1 ? meta : 0;
	}

	/* TILE ENTITY CREATION */
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		if (metadata < 0 || metadata > Type.VALUES.length) {
			return null;
		}

		Type type = Type.VALUES[metadata];
		switch (type) {
			case MUTATRON:
				return new TileAlvearyMutatron();
			case PLAIN:
			default:
				return new TileAlvearyPlain();
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return createTileEntity(world, meta);
	}

	/* ICONS */
	public static final int PLAIN = 0;
	public static final int ENTRANCE = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int MUTATRON_OFF = 5;
	public static final int MUTATRON_ON = 6;

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[14];
		icons[0] = TextureManager.registerTex(register, "apiculture/alveary.plain");
		icons[1] = TextureManager.registerTex(register, "apiculture/alveary.entrance");
		icons[2] = TextureManager.registerTex(register, "apiculture/alveary.bottom");
		icons[3] = TextureManager.registerTex(register, "apiculture/alveary.left");
		icons[4] = TextureManager.registerTex(register, "apiculture/alveary.right");
		icons[5] = TextureManager.registerTex(register, "apiculture/alveary.mutatron.off");
		icons[6] = TextureManager.registerTex(register, "apiculture/alveary.mutatron.on");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		if ((metadata <= 1	|| metadata == Type.MUTATRON.ordinal())
				&& (side == 1 || side == 0)) {
			return icons[BOTTOM];
		}

		Type type = Type.VALUES[metadata];

		switch (type) {
			case PLAIN:
				return icons[PLAIN];
			case MUTATRON:
				return icons[MUTATRON_OFF];			
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
				} else {
					return switchForSide(41, side);
				}

			} else {
				return this.getIcon(side, meta);
			}

		} else if (blockXP != this && blockXM == this) {
			if (world.getBlockMetadata(x - 1, y, z) == 1) {

				if (world.getBlock(x, y, z + 1) != this) {
					return switchForSide(41, side);
				} else {
					return switchForSide(42, side);
				}

			} else {
				return this.getIcon(side, meta);
			}
		}

		return this.getIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	private IIcon getBlockTextureFromSideAndTile(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (!(tile instanceof TileAlveary)) {
			return getIcon(side, 0);
		}

		return icons[((TileAlveary) tile).getIcon(side)];
	}

	@SideOnly(Side.CLIENT)
	private IIcon switchForSide(int textureId, int side) {

		if (side == 4 || side == 5) {
			if (textureId == 41) {
				return icons[LEFT];
			} else {
				return icons[RIGHT];
			}
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
		if (tileEntity instanceof TileAlveary) {
			TileAlveary tileAlveary = (TileAlveary) tileEntity;

			// We must check that the slabs on top were not removed
			tileAlveary.getMultiblockLogic().getController().reassemble();
		}
	}

	public ItemStack get(Type type) {
		return new ItemStack(this, 1, type.ordinal());
	}
}
