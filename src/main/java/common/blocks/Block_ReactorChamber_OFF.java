package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ReactorChamber_OFF extends BaseGTUpdateableBlock {
	
	private static Block_ReactorChamber_OFF instance = new Block_ReactorChamber_OFF();
	
	private Block_ReactorChamber_OFF() {
		super(Material.iron);
	}
	
	public static Block_ReactorChamber_OFF getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_reactorchamberoff_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ReactorChamber_OFF");
		super.setHardness(10.0f);
		super.setResistance(16.0f);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
	
}
