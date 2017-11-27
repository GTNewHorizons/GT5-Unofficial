package gtPlusPlus.core.block.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCompressedObsidian extends BlockObsidian {

	private final IIcon textureArray[] = new IIcon[6];

	public BlockCompressedObsidian() {
		this.setBlockName("blockCompressedObsidian");
		this.setHardness(50.0F);
		this.setResistance(2000.0F);
		this.setStepSound(soundTypePiston);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockMeta.class, "blockCompressedObsidian");
	}

	@Override
	public MapColor getMapColor(final int meta) {
		if (meta != 5) {
			return MapColor.obsidianColor;
		}
		else {
			return MapColor.sandColor;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iicon) {
		this.textureArray[0] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian1");
		this.textureArray[1] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian2");
		this.textureArray[2] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian3");
		this.textureArray[3] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian4");
		this.textureArray[4] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian5");
		this.textureArray[5] = iicon.registerIcon(CORE.MODID + ":" + "compressed/" + "obsidian_invert");
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int meta) {
		return this.textureArray[meta];
	}

	@Override
	public int damageDropped(final int damage) {
		return damage;
	}

	@Override
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for (int i = 0; i < 6; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public Item getItemDropped(final int meta, final Random rand, final int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata,
			final int fortune) {
		int m = metadata;
		if (m == 5) {
			m = 1;
		}
		return super.getDrops(world, x, y, z, m, fortune);
	}

}
