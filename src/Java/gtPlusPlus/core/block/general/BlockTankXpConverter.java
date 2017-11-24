package gtPlusPlus.core.block.general;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockEntityBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityXpConverter;
import gtPlusPlus.core.util.enchanting.EnchantingUtils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTankXpConverter extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	@SuppressWarnings("deprecation")
	public BlockTankXpConverter() {
		super(Material.iron);
		this.setBlockName("blockTankXpConverter");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockEntityBase.class, "blockTankXpConverter");
		LanguageRegistry.addName(this, "Xp Converter");
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
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
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
			final TileEntityXpConverter tank = (TileEntityXpConverter) world.getTileEntity(x, y, z);
			if (tank != null){
				if (tank.tankEssence.getFluid() != null){
					PlayerUtils.messagePlayer(player, "This tank contains "+tank.tankEssence.getFluidAmount()+"L of "+tank.tankEssence.getFluid().getLocalizedName());
				}
				if (tank.tankLiquidXp.getFluid() != null){
					PlayerUtils.messagePlayer(player, "This tank contains "+tank.tankLiquidXp.getFluidAmount()+"L of "+tank.tankLiquidXp.getFluid().getLocalizedName());
				}
				if ((tank.tankEssence.getFluid() != null) && (tank.tankLiquidXp.getFluid() != null)){
					PlayerUtils.messagePlayer(player, "This is worth "+EnchantingUtils.getLevelForLiquid(tank.tankLiquidXp.getFluidAmount()));
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
		return new TileEntityXpConverter();
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
	}

}
