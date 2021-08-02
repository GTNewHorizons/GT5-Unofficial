package common.blocks;

import common.itemBlocks.IB_TFFTMultiHatch;
import common.tileentities.TE_TFFTMultiHatch;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

@Deprecated
public class Block_TFFTMultiHatch extends BaseGTUpdateableBlock {
	
	private static final Block_TFFTMultiHatch INSTANCE = new Block_TFFTMultiHatch();

	private IIcon casing;

	private Block_TFFTMultiHatch() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_tfftmultihatch_block";
		INSTANCE.setBlockName(blockName);
		INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
		INSTANCE.setHardness(5.0f);
		INSTANCE.setResistance(6.0f);
		GameRegistry.registerBlock(INSTANCE, IB_TFFTMultiHatch.class, blockName);

		return INSTANCE;
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {
		casing = ir.registerIcon("kekztech:TFFTCasing");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return casing;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TE_TFFTMultiHatch();
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

}
