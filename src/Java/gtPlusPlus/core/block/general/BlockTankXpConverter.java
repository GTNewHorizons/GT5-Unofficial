package gtPlusPlus.core.block.general;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockEntityBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityXpConverter;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EnchantingUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
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
		int id = 0;
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(51, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(102, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(153, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(204, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 255, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 204, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 153, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 102, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 51, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 0));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 51));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 102));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 153));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 204));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(255, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(204, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(153, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(102, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(51, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 0, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 51, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 102, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 153, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 204, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 255));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 204));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 153));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 102));
		this.mRainbowMap.put(id++, new Triplet<Integer, Integer, Integer>(0, 255, 51));
		this.mRainbowTickMax = this.mRainbowMap.size();
		return true;
	}

	@Override
	public int getBlockColor() {
		return Utils.rgbtoHexValue(0, 0, 0);
	}

	@Override
	public int colorMultiplier(final IBlockAccess p_149720_1_, final int p_149720_2_, final int p_149720_3_,
			final int p_149720_4_) {

		if ((this.mRainbowTick < 0) || (this.mRainbowTick > this.mRainbowTickMax)) {
			this.mRainbowTick = 0;
		}
		//Utils.LOG_INFO("x: "+this.mRainbowTick);
		if (this.mRainbowTick <= this.mRainbowTickMax) {
			Triplet<Integer, Integer, Integer> mT = this.mRainbowMap.get(this.mRainbowTick);
			try {
				return Utils.rgbtoHexValue(mT.getValue_1(), mT.getValue_1(), mT.getValue_1());
			}
			catch (final Throwable t) {
				try {
					mT = this.mRainbowMap.get(this.mRainbowTick - 1);
					return Utils.rgbtoHexValue(mT.getValue_1(), mT.getValue_1(), mT.getValue_1());
				}
				catch (final Throwable t1) {
					return Utils.rgbtoHexValue(0, 0, 0);
				}
			}
		}

		return Utils.rgbtoHexValue(0, 0, 0);
	}

	@Override
	public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
		// this.mRainbowTick++;
		super.updateTick(world, x, y, z, rand);
	}

	@Override
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
		this.mRainbowTick++;
		super.randomDisplayTick(world, x, y, z, rand);
	}

	@Override
	public int tickRate(final World p_149738_1_) {
		return 20;
	}

	@Override
	public int getLightValue() {
		final int mTicker = this.mRainbowTick;
		if ((mTicker == 0) || (mTicker == 17)){
			return 1;
		}
		else if ((mTicker == 1) || (mTicker == 16)){
			return 2;
		}
		else if ((mTicker == 2) || (mTicker == 15)){
			return 3;
		}
		else if ((mTicker == 3) || (mTicker == 14)){
			return 4;
		}
		else if ((mTicker == 4) || (mTicker == 13)){
			return 6;
		}
		else if ((mTicker == 5) || (mTicker == 12)){
			return 8;
		}
		else if ((mTicker == 6) || (mTicker == 11)){
			return 10;
		}
		else if ((mTicker == 7) || (mTicker == 10)){
			return 12;
		}
		else if ((mTicker == 8) || (mTicker == 9)){
			return 14;
		}
		return 0;

	}

}
