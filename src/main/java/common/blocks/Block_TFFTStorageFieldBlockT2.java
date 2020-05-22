package common.blocks;

import common.itemBlocks.IB_TFFTStorageFieldBlockT2;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT2 extends BaseGTUpdateableBlock {
	
	private static Block_TFFTStorageFieldBlockT2 instance;
	
	private Block_TFFTStorageFieldBlockT2() {
		super(Material.iron);
	}
	
	public static int getCapacity() {
		return 4000000;
	}
	
	public static Block registerBlock() {
		if(instance == null) {
			instance = new Block_TFFTStorageFieldBlockT2();
		}
		
		final String blockName = "kekztech_tfftstoragefieldblock2_block";
		instance.setBlockName(blockName);
		instance.setCreativeTab(CreativeTabs.tabMisc);
		instance.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock2");
		instance.setHardness(5.0f);
		instance.setResistance(6.0f);
		GameRegistry.registerBlock(instance, IB_TFFTStorageFieldBlockT2.class, blockName);
		
		return instance;
	} 
}
