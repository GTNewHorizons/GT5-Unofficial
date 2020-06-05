package common.blocks;

import common.Blocks;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class Block_TFFTMultiHatch extends BaseGTUpdateableBlock {
	
	private static final Block_TFFTMultiHatch INSTANCE = new Block_TFFTMultiHatch();

	private IIcon casing;
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
		casing = ir.registerIcon("kekztech:TFFTCasing");
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
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		final TileEntity te = blockAccess.getTileEntity(x, y, z);
		if(te instanceof TE_TFFTMultiHatch && ((TE_TFFTMultiHatch) te).hasFacingOnSide((byte) side)){
			return tieredTexture[blockAccess.getBlockMetadata(x, y, z)];
		}
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

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TE_TFFTMultiHatch) {
			final TE_TFFTMultiHatch hatchTE = (TE_TFFTMultiHatch) te;
			for(int i = 0; i < 6; i++) {
				final ForgeDirection d = ForgeDirection.getOrientation(i);
				final Block n = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
				KekzCore.LOGGER.info("Block on side " + i + " is " + n.getUnlocalizedName());
				// Always implement your tiered blocks as sub-blocks so you don't have to do this
				if(n.equals(Blocks.tfftStorageField1)
						|| n.equals(Blocks.tfftStorageField2)
						|| n.equals(Blocks.tfftStorageField3)
						|| n.equals(Blocks.tfftStorageField4)
						|| n.equals(Blocks.tfftStorageField5)) {
					KekzCore.LOGGER.info("Found Storage Field at side: " + i);
					hatchTE.setFacingOnSide((byte) ForgeDirection.OPPOSITES[i], true);
				}
			}
		}
	}

}
