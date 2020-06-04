package common.blocks;

import common.itemBlocks.IB_TFFTMultiHatch;
import common.tileentities.TE_TFFTMultiHatch;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class Block_TFFTMultiHatch extends BaseGTUpdateableBlock {
	
	private static final Block_TFFTMultiHatch INSTANCE = new Block_TFFTMultiHatch();

	private final IIcon[] tieredTexture = new IIcon[3];

	private Block_TFFTMultiHatch() {
		super(Material.iron);
	}
	
	public static Block registerBlock() {
		final String blockName = "kekztech_tfftmultihatch_block";
		INSTANCE.setBlockName(blockName);
		INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
		INSTANCE.setBlockTextureName(KekzCore.MODID + ":" + "TFFTMultiHatch");
		INSTANCE.setHardness(5.0f);
		INSTANCE.setResistance(6.0f);
		GameRegistry.registerBlock(INSTANCE, IB_TFFTMultiHatch.class, blockName);
		
		return INSTANCE;
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {
		for(int i = 0; i < tieredTexture.length; i++) {
			tieredTexture[i] = ir.registerIcon("kekztech:TFFTMultiHatch" + (i + 1));
		}
	}

	@Override
	@SuppressWarnings({"unchecked" })
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		// HV, IV, ZPM
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return tieredTexture[meta];
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
				if(te instanceof TE_TFFTMultiHatch) {
					((TE_TFFTMultiHatch) te).toggleAutoOutput();
					GT_Utility.sendSoundToPlayers(world, GregTech_API.sSoundList.get(100), 1.0F, -1.0F, x, y, z);
					// Give chat feedback
					GT_Utility.sendChatToPlayer(player, ((TE_TFFTMultiHatch) te).isOutputting() ? "Auto-output enabled" : "Auto-output disabled");
					
				}
			}
			return true;
		}
		return false;
	}
}
