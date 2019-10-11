package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTCasing extends Block {
	
	private static Block_TFFTCasing instance = new Block_TFFTCasing();
	
	private Block_TFFTCasing() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_TFFTCasing getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftcasingblock_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTCasing");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), blockName);
	} 
}
