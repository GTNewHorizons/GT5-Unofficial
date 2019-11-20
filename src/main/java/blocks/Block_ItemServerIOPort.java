package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemServerIOPort;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_ItemServerIOPort;

public class Block_ItemServerIOPort extends BlockContainer {

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
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemServerIOPort();
	}
	
}
