package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_TFFTStorageFieldBlockT4;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT4 extends BaseGTUpdateableBlock {
	
	private static Block_TFFTStorageFieldBlockT4 instance = new Block_TFFTStorageFieldBlockT4();
	
	private Block_TFFTStorageFieldBlockT4() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_TFFTStorageFieldBlockT4 getInstance() {
		return instance;
	}
	
	public static int getCapacity() {
		return 64000000;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftstoragefieldblock4_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock4");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_TFFTStorageFieldBlockT4.class, blockName);
	} 
}
