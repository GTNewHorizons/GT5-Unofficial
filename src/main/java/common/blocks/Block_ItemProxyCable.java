package common.blocks;

import common.tileentities.TE_ItemProxyCable;
import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemProxyCable;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemProxyCable extends BlockContainer {
	
	private static Block_ItemProxyCable instance = new Block_ItemProxyCable();
	
	private Block_ItemProxyCable() {
		super(Material.glass);
	}
	
	public static Block_ItemProxyCable getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemproxycable_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTCasing");
		super.setHardness(3.0f);
		super.setResistance(2.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemProxyCable.class, blockName);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemProxyCable();
	}

}
