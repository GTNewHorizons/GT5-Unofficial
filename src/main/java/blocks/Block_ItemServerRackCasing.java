package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_ItemServerRackCasing;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ItemServerRackCasing extends BaseGTUpdateableBlock {

	private static Block_ItemServerRackCasing instance = new Block_ItemServerRackCasing();
		
	private Block_ItemServerRackCasing() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_ItemServerRackCasing getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_itemserverrackcasing_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ItemServerRackCasing");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_ItemServerRackCasing.class, blockName);
	}
	
}
