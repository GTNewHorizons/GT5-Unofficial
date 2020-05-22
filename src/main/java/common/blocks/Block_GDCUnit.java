package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_GDCUnit extends BaseGTUpdateableBlock {
	
	private static final Block_GDCUnit instance = new Block_GDCUnit();
	
	private Block_GDCUnit() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
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
