package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemDistributionCable;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_ItemDistributionCable;

public class Block_ItemDistributionCable extends BlockContainer {
	
	private static Block_ItemDistributionCable instance = new Block_ItemDistributionCable();
	
	private Block_ItemDistributionCable() {
		super(Material.glass);
	}
	
	public static Block_ItemDistributionCable getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemdistributioncable_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ItemDistributionCable");
		super.setHardness(3.0f);
		super.setResistance(2.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemDistributionCable.class, blockName);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_ItemDistributionCable();
	}

}
