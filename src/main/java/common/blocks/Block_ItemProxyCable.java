package common.blocks;

import common.tileentities.TE_ItemProxyCable;
import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemProxyCable;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemProxyCable extends Block {
	
	private static Block_ItemProxyCable instance;
	
	private Block_ItemProxyCable() {
		super(Material.glass);
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_ItemProxyCable();
		}
		
		final String blockName = "kekztech_itemproxycable_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "TFFTCasing");
		instance.setHardness(3.0f);
		instance.setResistance(2.0f);
		GameRegistry.registerBlock(instance, IB_ItemProxyCable.class, blockName);
		
		return instance;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int p_149915_2_) {
		return new TE_ItemProxyCable();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}
