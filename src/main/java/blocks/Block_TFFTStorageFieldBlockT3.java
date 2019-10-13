package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_TFFTStorageFieldBlockT3;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT3 extends Block {
	
	private static Block_TFFTStorageFieldBlockT3 instance = new Block_TFFTStorageFieldBlockT3();
	
	private Block_TFFTStorageFieldBlockT3() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_TFFTStorageFieldBlockT3 getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftstoragefieldblock3_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock3");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_TFFTStorageFieldBlockT3.class, blockName);
	} 
}
