package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_GDCUnit extends BaseGTUpdateableBlock {
	
	private static Block_GDCUnit instance;
	
	private Block_GDCUnit() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_GDCUnit();
		}
		
		final String blockName = "kekztech_gdcceramicelectrolyteunit_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "GDCCeramicElectrolyteUnit");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
	}
}
