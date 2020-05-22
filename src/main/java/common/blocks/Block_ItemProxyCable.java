package common.blocks;

import common.itemBlocks.IB_ItemProxyCable;
import common.tileentities.TE_ItemProxyCable;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import client.renderer.ConduitRenderer;

public class Block_ItemProxyCable extends Block {
	
	private static Block_ItemProxyCable instance = new Block_ItemProxyCable();
	
	private Block_ItemProxyCable() {
		super(Material.glass);
	}
	
	public static Block registerBlock() {
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

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getRenderType() {
		return ConduitRenderer.RID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
