package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_TFFTStorageFieldBlockT5;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT5 extends BaseGTUpdateableBlock {
	
	private static Block_TFFTStorageFieldBlockT5 instance = new Block_TFFTStorageFieldBlockT5();
	
	private Block_TFFTStorageFieldBlockT5() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_TFFTStorageFieldBlockT5 getInstance() {
		return instance;
	}
	
	public static int getCapacity() {
		return 256000000;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftstoragefieldblock5_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock5");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_TFFTStorageFieldBlockT5.class, blockName);
	} 
}