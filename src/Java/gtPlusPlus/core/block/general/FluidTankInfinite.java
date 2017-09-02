package gtPlusPlus.core.block.general;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;

public class FluidTankInfinite extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	@SuppressWarnings("deprecation")
	public FluidTankInfinite() {
		super(Material.iron);
		this.setBlockName("blockInfiniteFluidTank");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, "blockInfiniteFluidTank");
		LanguageRegistry.addName(this, "Infinite Fluid Tank");

	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_) {
		return p_149691_1_ == 1 ? this.textureTop
				: (p_149691_1_ == 0 ? this.textureBottom
						: ((p_149691_1_ != 2) && (p_149691_1_ != 4) ? this.blockIcon : this.textureFront));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "Generic_Creative_Texture");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "Generic_Creative_Texture");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "Generic_Creative_Texture");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "Generic_Creative_Texture");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float lx, final float ly, final float lz) {
		if (world.isRemote) {
			return true;
		}
		else {
			TileEntityInfiniteFluid tank = (TileEntityInfiniteFluid) world.getTileEntity(x, y, z);
			if (tank != null){
				Item handItem;
				try {
					handItem = player.getHeldItem().getItem();
				}
				catch (Throwable t){
					handItem = null;
				}
			if (handItem != null && (handItem instanceof IFluidContainerItem || handItem instanceof ItemFluidContainer || FluidContainerRegistry.isFilledContainer(player.getHeldItem()))){
				if (tank.tank.getFluid() == null){
					try {
						if (!FluidContainerRegistry.isFilledContainer(player.getHeldItem())){
						ItemStack handItemStack = player.getHeldItem();
						IFluidContainerItem container = (IFluidContainerItem) handItem;
						FluidStack containerFluid = container.getFluid(handItemStack);
						container.drain(handItemStack, container.getFluid(handItemStack).amount, true);
						tank.tank.setFluid(containerFluid);
						}
						else {							
							ItemStack handItemStack = player.getHeldItem();
							FluidContainerRegistry.drainFluidContainer(handItemStack);
							FluidStack containerFluid = FluidContainerRegistry.getFluidForFilledItem(handItemStack);
							ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(handItemStack);
							player.setItemInUse(emptyContainer, 0);
							
							tank.tank.setFluid(containerFluid);
						}
					}
					catch (Throwable t){
						t.printStackTrace();
					}
				}
				
			}
			if (tank.tank.getFluid() != null){
				PlayerUtils.messagePlayer(player, "This tank contains "+tank.tank.getFluidAmount()+"L of "+tank.tank.getFluid().getLocalizedName());				
			}
		}
	}
	return true;
}

@Override
public int getRenderBlockPass() {
	return 1;
}

@Override
public boolean isOpaqueCube() {
	return false;
}

@Override
public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
	return new TileEntityInfiniteFluid();
}

@Override
public void onBlockAdded(World world, int x, int y, int z) {
	super.onBlockAdded(world, x, y, z);
}

}
