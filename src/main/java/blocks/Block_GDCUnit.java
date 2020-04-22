package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_GDCUnit extends BaseGTUpdateableBlock {
	
	private static Block_GDCUnit instance = new Block_GDCUnit();
	
	private Block_GDCUnit() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_GDCUnit getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_gdcceramicelectrolyteunit_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "GDCCeramicElectrolyteUnit");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
}
