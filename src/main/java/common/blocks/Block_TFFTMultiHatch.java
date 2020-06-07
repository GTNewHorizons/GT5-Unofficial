package common.blocks;

import client.renderer.HatchRenderer;
import common.itemBlocks.IB_TFFTMultiHatch;
import common.tileentities.TE_TFFTMultiHatch;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.List;

public class Block_TFFTMultiHatch extends BaseGTUpdateableBlock {
	
	private static final Block_TFFTMultiHatch INSTANCE = new Block_TFFTMultiHatch();

	private IIcon casing;
	private final IIcon[] overlayOff = new IIcon[3];
	private final IIcon[] overlayOn = new IIcon[3];

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
		for(int i = 0; i < overlayOff.length; i++) {
			overlayOff[i] = ir.registerIcon("kekztech:TFFTMultiHatch" + i + "_off");
			overlayOn[i] = ir.registerIcon("kekztech:TFFTMultiHatch" + i + "_on");
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
		if(side != 3) {
			return casing;
		} else {
			return overlayOff[meta];
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		final TileEntity te = blockAccess.getTileEntity(x, y, z);
		if(te instanceof TE_TFFTMultiHatch) {
			final TE_TFFTMultiHatch hatchTE = (TE_TFFTMultiHatch) te;
			if(hatchTE.hasFacingOnSide((byte) side)) {
				final int meta = blockAccess.getBlockMetadata(x, y, z);
				if(hatchTE.isOutputting()) {
					return overlayOn[meta];
				} else {
					return overlayOff[meta];
				}
			} else {
				return casing;
			}
		} else {
			return casing;
		}
	}
	// ========= Leagris stuff
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		return 255;
	}

	@Override
	public int getLightValue() {
		return 0;
	}

	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
		return false;
	}

	//============== Leagris over
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 0;
	}

	@Override
	public int getRenderType() {
		return HatchRenderer.RID;
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		// Code block taken from GregTech's api.metatileentity.BaseMetaTileEntity.class
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
		} else if (GT_Utility.isStackInList(player.getHeldItem(), GregTech_API.sWrenchList)) {
			if (GT_ModHandler.damageOrDechargeItem(player.getHeldItem(), 1, 200, player)) {
				final TileEntity te = world.getTileEntity(x, y, z);
				if(te instanceof TE_TFFTMultiHatch) {
					((TE_TFFTMultiHatch) te).setFacingToSide((byte) side);
					GT_Utility.sendSoundToPlayers(world, GregTech_API.sSoundList.get(100), 1.0F, -1.0F, x, y, z);
				}
			}
			return true;
		}
		return false;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
		final int yaw = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		final int pitch = MathHelper.floor_double((double)(placer.rotationPitch * 4.0F / 360.0F) + 0.5D) & 3;
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TE_TFFTMultiHatch) {
			if(pitch == 0 || pitch == 2) {
				if (yaw == 0) {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 2);
				}

				if (yaw == 1) {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 5);
				}

				if (yaw == 2) {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 3);
				}

				if (yaw == 3) {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 4);
				}
			} else {
				if(pitch == 1) {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 1);
				} else {
					((TE_TFFTMultiHatch)te).setFacingToSide((byte) 0);
				}
			}
		}

	}

}
