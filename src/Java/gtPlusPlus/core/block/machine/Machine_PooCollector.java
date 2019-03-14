package gtPlusPlus.core.block.machine;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityAdvPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityBaseFluidCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityPooCollector;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class Machine_PooCollector extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureTop2;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide2;

	public Machine_PooCollector() {
		super(Material.iron);
		this.setBlockName("blockPooCollector");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockMeta.class,"blockPooCollector");
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int aSide, final int aMeta) {
		if (aMeta <= 7) {
			blockIcon = textureSide;
			return aSide <= 1 ? this.textureTop : this.textureSide;			
		}
		else {
			blockIcon = textureSide2;
			return aSide <= 1 ? this.textureTop2 : this.textureSide2;			
		}	
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_) {
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "sewer_top");
		this.textureTop2 = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "sewer_adv_top");
		this.textureSide = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "sewer_sides");
		this.textureSide2 = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "sewer_adv_sides");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float lx, final float ly, final float lz) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntityBaseFluidCollector tank = (TileEntityBaseFluidCollector) world.getTileEntity(x, y, z);
			if (tank != null) {
				Item handItem;
				try {
					handItem = player.getHeldItem().getItem();
				} catch (Throwable t) {
					handItem = null;
				}
				
				//Fluid container code
				/*if (handItem != null
						&& (handItem instanceof IFluidContainerItem || handItem instanceof ItemFluidContainer
								|| FluidContainerRegistry.isFilledContainer(player.getHeldItem()))) {
					if (tank.tank.getFluid() == null) {
						try {
							if (!FluidContainerRegistry.isFilledContainer(player.getHeldItem())) {
								ItemStack handItemStack = player.getHeldItem();
								IFluidContainerItem container = (IFluidContainerItem) handItem;
								FluidStack containerFluid = container.getFluid(handItemStack);
								container.drain(handItemStack, container.getFluid(handItemStack).amount, true);
								tank.tank.setFluid(containerFluid);
							} else {
								ItemStack handItemStack = player.getHeldItem();
								FluidContainerRegistry.drainFluidContainer(handItemStack);
								FluidStack containerFluid = FluidContainerRegistry.getFluidForFilledItem(handItemStack);
								ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(handItemStack);
								player.setItemInUse(emptyContainer, 0);

								tank.tank.setFluid(containerFluid);
							}
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}

				}*/
				
				if (!tank.mInventory.isEmpty()) {
					PlayerUtils.messagePlayer(player, "Inventory contains:");
					PlayerUtils.messagePlayer(player, ItemUtils.getArrayStackNames(tank.mInventory.getRealInventory()));
				}
				else {
					PlayerUtils.messagePlayer(player, "No solids collected yet.");					
				}
				if (tank.tank.getFluid() != null) {
					PlayerUtils.messagePlayer(player, "Tank contains " + tank.tank.getFluidAmount() + "L of "
							+ tank.tank.getFluid().getLocalizedName());
				}
			}
		}
		return true;
	}

	@Override
	public int getRenderBlockPass() {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return super.isOpaqueCube();
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int aMeta) {
		return aMeta <= 7 ? new TileEntityPooCollector() : new TileEntityAdvPooCollector();
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public int getBlockColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int damageDropped(final int damage) {
		return damage;
	}

	@Override
	public Item getItemDropped(final int meta, final Random rand, final int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int getRenderColor(int aMeta) {
		if (aMeta <= 7) {
			return super.getRenderColor(aMeta);			
		}
		else {
			return Utils.rgbtoHexValue(128, 45, 45);		
		}
	}

	@Override
	public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
		aList.add(new ItemStack(aItem, 1, 0));
		aList.add(new ItemStack(aItem, 1, 8));		
	}

}
