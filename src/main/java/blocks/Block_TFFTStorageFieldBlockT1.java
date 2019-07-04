package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class Block_TFFTStorageFieldBlockT1 extends Block {
	
	private static Block_TFFTStorageFieldBlockT1 instance = new Block_TFFTStorageFieldBlockT1();
	
	private Block_TFFTStorageFieldBlockT1() {
		// I am a singleton
		super(Material.iron);
	}
	
	public static Block_TFFTStorageFieldBlockT1 getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftstoragefieldblock1_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTStorageFieldBlock1");
		GameRegistry.registerBlock(getInstance(), blockName);
	}
	
	
}
