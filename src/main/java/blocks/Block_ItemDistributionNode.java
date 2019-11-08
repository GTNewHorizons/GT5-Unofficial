package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemDistributionNode;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_ItemDistributionNode;

public class Block_ItemDistributionNode extends BlockContainer {
	
	private static Block_ItemDistributionNode instance = new Block_ItemDistributionNode();
	
	private Block_ItemDistributionNode() {
		super(Material.glass);
	}
	
	public static Block_ItemDistributionNode getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemdistributionnode_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ItemDistributionNode");
		super.setHardness(3.0f);
		super.setResistance(2.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemDistributionNode.class, blockName);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemDistributionNode();
	}

}
