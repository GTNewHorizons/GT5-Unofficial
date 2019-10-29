package blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import itemBlocks.IB_TFFTMultiHatch;
import kekztech.KekzCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_FluidConduit;

public class Block_FluidConduit extends BlockContainer {
	
	private static Block_FluidConduit instance = new Block_FluidConduit();
	
	private Block_FluidConduit() {
		super(Material.glass);
	}
	
	public static Block_FluidConduit getInstance() {
		return instance;
	}
	
	public void registerBlock() {
		final String blockName = "kekztech_fluidconduit_block";
		super.setBlockName(blockName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setBlockTextureName(KekzCore.MODID + ":" + "FluidConduit");
		super.setHardness(3.0f);
		super.setResistance(2.0f);
		GameRegistry.registerBlock(getInstance(), IB_TFFTMultiHatch.class, blockName);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TE_FluidConduit(1000);
	}

}
