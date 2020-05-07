package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_YSZUnit extends BaseGTUpdateableBlock {
	
	private static Block_YSZUnit instance = new Block_YSZUnit();
	
	private Block_YSZUnit() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_yszceramicelectrolyteunit_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "YSZCeramicElectrolyteUnit");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
	}
}
