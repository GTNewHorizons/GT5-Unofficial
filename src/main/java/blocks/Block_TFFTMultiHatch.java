package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_TFFTMultiHatch;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_TFFTMultiHatch;

public class Block_TFFTMultiHatch extends BaseGTUpdateableBlock {
	
	private static Block_TFFTMultiHatch instance = new Block_TFFTMultiHatch();
	
	private Block_TFFTMultiHatch() {
		super(Material.iron);
	}
	
	public static Block_TFFTMultiHatch getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_tfftmultihatch_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "TFFTMultiHatch");
		super.setHardness(5.0f);
		super.setResistance(6.0f);
		GameRegistry.registerBlock(getInstance(), IB_TFFTMultiHatch.class, blockName);
	}

	@Override
	public TileEntity createTileEntity(World world, int p_149915_2_) {
		return new TE_TFFTMultiHatch();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
}
