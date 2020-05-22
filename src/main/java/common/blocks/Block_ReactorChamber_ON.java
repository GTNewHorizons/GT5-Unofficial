package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ReactorChamber_ON extends BaseGTUpdateableBlock {
	
	private static Block_ReactorChamber_ON instance = new Block_ReactorChamber_ON();
	
	private Block_ReactorChamber_ON() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_reactorchamberon_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "ReactorChamber_ON");
		instance.setHardness(-1.0f);
		instance.setResistance(16.0f);
		GameRegistry.registerBlock(instance, blockName);
		
		return instance;
	}
	
	@Override
	public int getLightValue() {
		return 15;
	}

}
