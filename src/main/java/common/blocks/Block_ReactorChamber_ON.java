package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_ReactorChamber_ON extends BaseGTUpdateableBlock {
	
	private static Block_ReactorChamber_ON instance = new Block_ReactorChamber_ON();
	
	private Block_ReactorChamber_ON() {
		super(Material.iron);
	}
	
	public static Block_ReactorChamber_ON getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_reactorchamberon_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "ReactorChamber_ON");
		super.setHardness(-1.0f);
		super.setResistance(16.0f);
		GameRegistry.registerBlock(getInstance(), blockName);
	}
	
	@Override
	public int getLightValue() {
		return 15;
	}

}
