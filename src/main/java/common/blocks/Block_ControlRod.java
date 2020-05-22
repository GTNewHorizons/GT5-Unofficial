package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ControlRod extends BaseGTUpdateableBlock {
	
	private static final Block_ControlRod instance = new Block_ControlRod();
	
	private Block_ControlRod() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_controlrod_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ControlRod");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
	}
}
