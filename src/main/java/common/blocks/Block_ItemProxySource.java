package common.blocks;

import common.itemBlocks.IB_ItemProxySource;
import common.tileentities.TE_ItemProxySource;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.GuiHandler;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemProxySource extends Block {
	
	private static final Block_ItemProxySource instance = new Block_ItemProxySource();
	
	private Block_ItemProxySource() {
		super(Material.glass);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_itemproxysource_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ItemProxySource");
		instance.setHardness(3.0f);
		instance.setResistance(2.0f);
		instance.setHarvestLevel("wrench", 2);
		GameRegistry.registerBlock(instance, IB_ItemProxySource.class, blockName);
		
		return instance;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
		if(world.isRemote) {
			return true;
		}
		
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ItemProxySource) {
			player.openGui(KekzCore.instance, GuiHandler.ITEM_PROXY_SOURCE, world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, int p_149915_2_) {
		return new TE_ItemProxySource();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}
