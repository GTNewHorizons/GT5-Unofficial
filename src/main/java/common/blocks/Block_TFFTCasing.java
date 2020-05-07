package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTCasing extends BaseGTUpdateableBlock {
	
	private static Block_TFFTCasing instance = new Block_TFFTCasing();
	
	private Block_TFFTCasing() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_tfftcasingblock_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "TFFTCasing");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
	} 
}
