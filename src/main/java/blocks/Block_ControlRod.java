package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ControlRod extends Block {
	
	private static Block_ControlRod instance = new Block_ControlRod();
	
	private Block_ControlRod() {
		super(Material.iron);
	}
	
	public static Block_ControlRod getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_controlrod_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ControlRod");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
}
