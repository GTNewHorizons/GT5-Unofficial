package gtPlusPlus.core.block.general.redstone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.tileentities.general.redstone.TileEntityRedstoneHandler;
import gtPlusPlus.core.util.minecraft.InventoryUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockGenericRedstone extends BlockContainer {


	@SuppressWarnings("deprecation")
	public BlockGenericRedstone(String aUnlocalizedSuffix, String aDisplayName) {
		super(Material.redstoneLight);
		this.setBlockName("blockGenericRedstone." + aUnlocalizedSuffix);
		this.setHardness(3f);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockMeta.class, "blockGenericRedstone." + aUnlocalizedSuffix);
		LanguageRegistry.addName(this, aDisplayName);

	}


	private final HashMap<Integer, HashMap<ForgeDirection, IIcon>> mTextures = new HashMap<Integer, HashMap<ForgeDirection, IIcon>>();

	/**
	 * A map of the textures used for this blocks. The key is the meta, then each internal map holds textures tied to each forge direction. Do not use unknown direction.
	 * @return
	 */
	public HashMap<Integer, HashMap<ForgeDirection, IIcon>> getTextureArray() {		
		return mTextures;
	}
	
	public abstract void generateTextureArray(final IIconRegister iicon);

	
	@Override
	@SideOnly(Side.CLIENT)
	public final void registerBlockIcons(final IIconRegister iicon) {
		generateTextureArray(iicon);
		this.blockIcon = iicon.registerIcon("redstone_block");		
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

		boolean mDidTool = false;
		// Check For Tools
		try {
			final ItemStack mHandStack = PlayerUtils.getItemStackInPlayersHand(world, player.getDisplayName());
			final Item mHandItem = mHandStack.getItem();
			if (mHandItem instanceof GT_MetaGenerated_Tool_01) {

				final TileEntityRedstoneHandler tile = (TileEntityRedstoneHandler) world.getTileEntity(x, y, z);
				if (tile != null) {					
					if (tile.isScrewdriverable()) {
						if ((mHandItem.getDamage(mHandStack) == 22) || (mHandItem.getDamage(mHandStack) == 150)){
							mDidTool = tile.onScrewdriverRMB();
						}
					}
					if (tile.isMalletable()) {
						if ((mHandItem.getDamage(mHandStack) == 24) || (mHandItem.getDamage(mHandStack) == 154)){
							mDidTool = tile.onMalletRMB();
						}
					}
					if (tile.isWrenchable()) {
						if ((mHandItem.getDamage(mHandStack) == 26) || (mHandItem.getDamage(mHandStack) == 164)){
							mDidTool = tile.onWrenchRMB();
						}
					}					
				}				
			}
		}
		catch (final Throwable t) {}
		if (mDidTool) {
			return true;
		}
		
		return false;
	}



	@Override
	public void onBlockClicked(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {

		boolean mDidTool = false;
		// Check For Tools
		try {
			final ItemStack mHandStack = PlayerUtils.getItemStackInPlayersHand(aWorld, aPlayer.getDisplayName());
			final Item mHandItem = mHandStack.getItem();
			if (mHandItem instanceof GT_MetaGenerated_Tool_01) {

				final TileEntityRedstoneHandler tile = (TileEntityRedstoneHandler) aWorld.getTileEntity(aX, aY, aZ);
				if (tile != null) {					
					if (tile.isScrewdriverable()) {
						if ((mHandItem.getDamage(mHandStack) == 22) || (mHandItem.getDamage(mHandStack) == 150)){
							mDidTool = tile.onScrewdriverLMB();
						}
					}
					if (tile.isMalletable()) {
						if ((mHandItem.getDamage(mHandStack) == 24) || (mHandItem.getDamage(mHandStack) == 154)){
							mDidTool = tile.onMalletLMB();
						}
					}
					if (tile.isWrenchable()) {
						if ((mHandItem.getDamage(mHandStack) == 26) || (mHandItem.getDamage(mHandStack) == 164)){
							mDidTool = tile.onWrenchLMB();
						}
					}					
				}				
			}
		}
		catch (Throwable t) {}

		if (!mDidTool) {
			super.onBlockClicked(aWorld, aX, aY, aZ, aPlayer);			
		}
		else {
			return;
		}		
	}

	@Override
	public abstract TileEntity createNewTileEntity(final World world, final int p_149915_2_);

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block,
			final int number) {
		InventoryUtils.dropInventoryItems(world, x, y, z, block);
		super.breakBlock(world, x, y, z, block, number);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity,
			final ItemStack stack) {
		if (stack.hasDisplayName()) {
			((TileEntityRedstoneHandler) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
			final int z) {
		return false;
	}

	@Override
	public int getLightValue() {
		return super.getLightValue();
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		// TODO Auto-generated method stub
		return super.onBlockPlaced(p_149660_1_, p_149660_2_, p_149660_3_, p_149660_4_, p_149660_5_, p_149660_6_,
				p_149660_7_, p_149660_8_, p_149660_9_);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityRedstoneHandler aThis = getTileEntity(world, x, y, z);
		if (aThis != null) {
			return aThis.isProvidingWeakPower(world, x, y, z, side);
		}
		return 0;	
	}

	@Override
	public boolean canProvidePower() {
		return false;	
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {		
		TileEntityRedstoneHandler aThis = getTileEntity(world, x, y, z);
		if (aThis != null) {
			return aThis.isProvidingStrongPower(world, x, y, z, side);
		}
		return 0;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		// TODO Auto-generated method stub
		return super.hasComparatorInputOverride();
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		// TODO Auto-generated method stub
		return super.getComparatorInputOverride(p_149736_1_, p_149736_2_, p_149736_3_, p_149736_4_, p_149736_5_);
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityRedstoneHandler aThis = getTileEntity(world, x, y, z);
		if (aThis != null) {
			return aThis.canConnectRedstone(world, x, y, z, side);
		}
		return false;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityRedstoneHandler aThis = getTileEntity(world, x, y, z);
		if (aThis != null) {
			return aThis.shouldCheckWeakPower(world, x, y, z, side);
		}
		return false;
	}

	@Override
	public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
		TileEntityRedstoneHandler aThis = getTileEntity(world, x, y, z);
		if (aThis != null) {
			return aThis.getWeakChanges(world, x, y, z);
		}
		return false;
	}


	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public abstract IIcon getIcon(final int side, final int meta);

	@Override
	public int damageDropped(final int damage) {
		return damage;
	}

	@Override
	public abstract void getSubBlocks(final Item item, final CreativeTabs tab, final List list);


	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
		TileEntityRedstoneHandler aThis = getTileEntity(aWorld, aX, aY, aZ);

		if (!aWorld.isRemote) {
			if (aThis.hasUpdatedRecently() && !aWorld.isBlockIndirectlyGettingPowered(aX, aY, aZ)) {
				aWorld.scheduleBlockUpdate(aX, aY, aZ, this, 4);
			} else if (!aThis.hasUpdatedRecently() && aWorld.isBlockIndirectlyGettingPowered(aX, aY, aZ)) {
				aWorld.setBlock(aX, aY, aZ, Blocks.lit_redstone_lamp, 0, 2);
			}
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * Block
	 */
	public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block p_149695_5_) {

		TileEntityRedstoneHandler aThis = getTileEntity(aWorld, aX, aY, aZ);

		if (!aWorld.isRemote) {
			if (aThis.hasUpdatedRecently() && !aWorld.isBlockIndirectlyGettingPowered(aX, aY, aZ)) {
				aWorld.scheduleBlockUpdate(aX, aY, aZ, this, 4);
			} else if (!aThis.hasUpdatedRecently() && aWorld.isBlockIndirectlyGettingPowered(aX, aY, aZ)) {
				aWorld.setBlock(aX, aY, aZ, Blocks.lit_redstone_lamp, 0, 2);
			}
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World aWorld, int aX, int aY, int aZ, Random p_149674_5_) {
		TileEntityRedstoneHandler aTile = getTileEntity(aWorld, aX, aY, aZ);
		// Client side handling
		if (aTile != null) {
			this.setLightLevel(aTile.getLightBrightness());
		}
		// Only continue on server
		if (aWorld.isRemote) {
			return;
		}
		if (aTile != null) {
			if (aTile.isGettingIndirectlyPowered()) {
			}
		}
	}

	public TileEntityRedstoneHandler getTileEntity(IBlockAccess world, int aX, int aY, int aZ) {
		TileEntity aTemp = world.getTileEntity(aX, aY, aZ);
		if (aTemp != null) {
			if (aTemp instanceof TileEntityRedstoneHandler) {
				TileEntityRedstoneHandler g = (TileEntityRedstoneHandler) aTemp;
				if (g != null) {
					return g;
				}
			}
		}
		return null;
	}


	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public abstract Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_);

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	@Override
	protected abstract ItemStack createStackedBlock(int p_149644_1_);

	@Override
	public abstract ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata,	final int fortune);

	@Override
	public abstract Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_);


}