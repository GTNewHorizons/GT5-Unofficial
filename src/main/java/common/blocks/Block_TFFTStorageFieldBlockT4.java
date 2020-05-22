package common.blocks;

import common.itemBlocks.IB_TFFTStorageFieldBlockT4;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT4 extends BaseGTUpdateableBlock {
	
	private static Block_TFFTStorageFieldBlockT4 instance;
	
	private Block_TFFTStorageFieldBlockT4() {
		super(Material.iron);
	}
	
	public static int getCapacity() {
		return 64000000;
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_TFFTStorageFieldBlockT4();
		}
		
		final String blockName = "kekztech_tfftstoragefieldblock4_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock4");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_TFFTStorageFieldBlockT4.class, blockName);
		
		return instance;
	} 
}
