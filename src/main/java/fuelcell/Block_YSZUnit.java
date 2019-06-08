package fuelcell;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_YSZUnit extends Block {
	
	private static Block_YSZUnit instance = new Block_YSZUnit();
	
	private Block_YSZUnit() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_YSZUnit getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_yszceramicelectrolyteunit_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "YSZCeramicElectrolyteUnit");
		GameRegistry.registerBlock(getInstance(), blockName);
	}
}
