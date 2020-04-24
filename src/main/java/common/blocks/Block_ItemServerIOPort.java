package common.blocks;

import common.tileentities.TE_ItemServerIOPort;
import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemServerIOPort;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_ItemServerIOPort extends BaseGTUpdateableBlock {

	private static Block_ItemServerIOPort instance = new Block_ItemServerIOPort();
		
	private Block_ItemServerIOPort() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_ItemServerIOPort getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemserverioport_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ItemServerIOPort");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemServerIOPort.class, blockName);
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
