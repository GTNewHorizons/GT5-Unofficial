package gtPlusPlus.core.block.general;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gregtech.common.items.GT_MetaGenerated_Tool_01;

import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockEntityBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityXpConverter;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EnchantingUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class BlockTankXpConverter extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	private int mRainbowTick = 0;
	private int mRainbowTickMax = 0;
	private final Map<Integer, Triplet<Integer, Integer, Integer>> mRainbowMap = new HashMap<Integer, Triplet<Integer, Integer, Integer>>();

	@SuppressWarnings("deprecation")
	public BlockTankXpConverter() {
		super(Material.iron);
		this.setBlockName("blockTankXpConverter");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockEntityBase.class, "blockTankXpConverter");
		LanguageRegistry.addName(this, "Xp Converter");
		this.generateRainbowMap();
		if (!this.getTickRandomly()) {
			this.setTickRandomly(true);
		}
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
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlGray");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlGray");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlGray");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlGray");
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
			boolean mDidScrewDriver = false;
			// Check For Screwdriver
			try {
				final ItemStack mHandStack = PlayerUtils.getItemStackInPlayersHand(world, player.getDisplayName());
				final Item mHandItem = mHandStack.getItem();
				if (((mHandItem instanceof GT_MetaGenerated_Tool_01)
						&& ((mHandItem.getDamage(mHandStack) == 22) || (mHandItem.getDamage(mHandStack) == 150)))) {
					final TileEntityXpConverter tile = (TileEntityXpConverter) world.getTileEntity(x, y, z);
					if (tile != null) {
						mDidScrewDriver = true;
						tile.onScrewdriverRightClick((byte) side, player, x, y, z);
					}

				}
			}
			catch (final Throwable t) {
				mDidScrewDriver = false;
			}

			if (!mDidScrewDriver) {

				try {
					final TileEntityXpConverter tile = (TileEntityXpConverter) world.getTileEntity(x, y, z);
					if (tile != null) {
						tile.onRightClick((byte) side, player, x, y, z);
					}
				}
				catch (final Throwable t) {
				}

				final TileEntityXpConverter tank = (TileEntityXpConverter) world.getTileEntity(x, y, z);
				if (tank != null) {
					if (tank.tankEssence.getFluid() != null) {
						PlayerUtils.messagePlayer(player, "This tank contains " + tank.tankEssence.getFluidAmount()
						+ "L of " + tank.tankEssence.getFluid().getLocalizedName());
					}
					if (tank.tankLiquidXp.getFluid() != null) {
						PlayerUtils.messagePlayer(player, "This tank contains " + tank.tankLiquidXp.getFluidAmount()
						+ "L of " + tank.tankLiquidXp.getFluid().getLocalizedName());
					}
					if ((tank.tankEssence.getFluid() != null) && (tank.tankLiquidXp.getFluid() != null)) {
						PlayerUtils.messagePlayer(player, "This is worth "
								+ EnchantingUtils.getLevelForLiquid(tank.tankLiquidXp.getFluidAmount()) + " levels.");
					}
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

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
			final int z) {
		return false;
	}

	private final boolean generateRainbowMap() {
		return true;
	}

	@Override
	public int getBlockColor() {
		return super.getBlockColor();
	}

	@Override
	public int colorMultiplier(final IBlockAccess p_149720_1_, final int p_149720_2_, final int p_149720_3_,
			final int p_149720_4_) {
		return super.colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);		
	}

	@Override
	public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
		// this.mRainbowTick++;
		super.updateTick(world, x, y, z, rand);
	}

	@Override
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
		//this.mRainbowTick++;
		super.randomDisplayTick(world, x, y, z, rand);
	}

	@Override
	public int tickRate(final World p_149738_1_) {
		return 20;
	}

	@Override
	public int getLightValue() {
			return 6;
			}

}
