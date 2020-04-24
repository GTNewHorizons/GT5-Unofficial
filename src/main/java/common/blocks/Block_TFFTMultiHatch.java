package common.blocks;

import common.tileentities.TE_TFFTMultiHatch;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import itemBlocks.IB_TFFTMultiHatch;
import kekztech.KekzCore;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		// Code block taken from GregTech's BaseMetaTileEntity.class
		if (GT_Utility.isStackInList(player.getHeldItem(), GregTech_API.sScrewdriverList)) {
			if (GT_ModHandler.damageOrDechargeItem(player.getHeldItem(), 1, 200, player)) {
				final TileEntity te = world.getTileEntity(x, y, z);
				if(te != null && te instanceof TE_TFFTMultiHatch) {
					((TE_TFFTMultiHatch) te).toggleAutoOutput();
					GT_Utility.sendSoundToPlayers(world, (String) GregTech_API.sSoundList.get(100), 1.0F, -1.0F, x, y, z);
				}
			}
			return true;
		}
		return false;
	}
}
