package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemServerRackCasing;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ItemServerRackCasing extends BaseGTUpdateableBlock {

	private static Block_ItemServerRackCasing instance;
		
	private Block_ItemServerRackCasing() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_ItemServerRackCasing();
		}
		
		final String blockName = "kekztech_itemserverrackcasing_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ItemServerRackCasing");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_ItemServerRackCasing.class, blockName);
		
		return instance;
	}
	
}
