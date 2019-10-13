package gtPlusPlus.core.block.machine;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockRoundRobinator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import gtPlusPlus.core.util.minecraft.InventoryUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Machine_RoundRobinator extends BlockContainer implements ITileTooltip
{
	@SideOnly(Side.CLIENT)
	private IIcon[] textureTop = new IIcon[5];
	@SideOnly(Side.CLIENT)
	private IIcon[] textureFront = new IIcon[5];

	/**
	 * Determines which tooltip is displayed within the itemblock.
	 */
	private final int mTooltipID = 7;

	@Override
	public int getTooltipID() {
		return this.mTooltipID;
	}

	@SuppressWarnings("deprecation")
	public Machine_RoundRobinator(){
		super(Material.iron);
		this.setHardness(1f);
		this.setResistance(1f);
		this.setBlockName("blockRoundRobinator");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockRoundRobinator.class, "blockRoundRobinator");
		//LanguageRegistry.addName(this, "Round-Robinator");

	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int aSide, final int aMeta) {
		if (aSide < 2) {
			return this.textureTop[aMeta];
		}
		else {
			return this.textureFront[aMeta];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_){
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "RoundRobinator_Side");
		for (int i=0;i<5;i++) {
			this.textureTop[i] = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/RoundRobinator/" + "RoundRobinator_Top_"+i);
			this.textureFront[i] = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/RoundRobinator/" + "RoundRobinator_Side_"+i);			
		}		
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz)
	{
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
					final TileEntityRoundRobinator tile = (TileEntityRoundRobinator) world.getTileEntity(x, y, z);
					if (tile != null) {
						mDidScrewDriver = tile.onScrewdriverRightClick((byte) side, player, x, y, z);
					}
				}
			}
			catch (final Throwable t) {}

			if (!mDidScrewDriver) {
				final TileEntity te = world.getTileEntity(x, y, z);
				if ((te != null) && (te instanceof TileEntityRoundRobinator)){
					return ((TileEntityRoundRobinator) te).onRightClick((byte) side, player, x, y, z);
				}
				return false;
			}
			else {
				return true;
			}		
		}
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
		return new TileEntityRoundRobinator();
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int number) {
		InventoryUtils.dropInventoryItems(world, x, y, z, block);
		super.breakBlock(world, x, y, z, block, number);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	@Override
	public void getSubBlocks(Item aItem, CreativeTabs p_149666_2_, List aList) {		
		//super.getSubBlocks(aItem, p_149666_2_, aList);
		for (int i=0;i<5;i++) {
			aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
		}		
	}

	@Override
	public IIcon getIcon(IBlockAccess aBlockAccess, int x, int y, int z, int aSide) {		
		return super.getIcon(aBlockAccess, x, y, z, aSide);
	}

}