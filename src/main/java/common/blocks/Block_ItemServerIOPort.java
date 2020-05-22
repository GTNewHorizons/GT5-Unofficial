package common.blocks;

import common.itemBlocks.IB_ItemServerIOPort;
import common.tileentities.TE_ItemServerIOPort;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemServerIOPort extends BaseGTUpdateableBlock {

	private static Block_ItemServerIOPort instance = new Block_ItemServerIOPort();
		
	private Block_ItemServerIOPort() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_itemserverioport_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ItemServerIOPort");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_ItemServerIOPort.class, blockName);
		
		return instance;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TE_ItemServerIOPort();
	}
	
}
