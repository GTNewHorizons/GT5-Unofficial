package common.blocks;

import common.itemBlocks.IB_TFFTStorageFieldBlockT1;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT1 extends BaseGTUpdateableBlock {
	
	private static Block_TFFTStorageFieldBlockT1 instance;
	
	private Block_TFFTStorageFieldBlockT1() {
		super(Material.iron);
	}
	
	public static int getCapacity() {
		return 500000;
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_TFFTStorageFieldBlockT1();
		}
		
		final String blockName = "kekztech_tfftstoragefieldblock1_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock1");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_TFFTStorageFieldBlockT1.class, blockName);
		
		return instance;
	}
	
}
