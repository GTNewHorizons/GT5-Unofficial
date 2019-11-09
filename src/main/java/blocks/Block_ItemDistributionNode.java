package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemDistributionNode;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
		if(world.isRemote) {
			return true;
		}
		
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TE_ItemDistributionNode) {
			player.openGui(KekzCore.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemDistributionNode();
	}

}
