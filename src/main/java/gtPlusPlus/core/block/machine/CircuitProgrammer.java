package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.minecraft.CubicObject;
import gtPlusPlus.core.block.base.BasicTileBlockWithTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CircuitProgrammer extends BasicTileBlockWithTooltip {

	/**
	 * Determines which tooltip is displayed within the itemblock.
	 */
	private final int mTooltipID = 4;

	@Override
	public int getTooltipID() {
		return this.mTooltipID;
	}

	@SuppressWarnings("deprecation")
	public CircuitProgrammer(){
		super(Material.iron);
		LanguageRegistry.addName(this, "Circuit Programmer");
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
					final TileEntityCircuitProgrammer tile = (TileEntityCircuitProgrammer) world.getTileEntity(x, y, z);
					if (tile != null) {
						mDidScrewDriver = tile.onScrewdriverRightClick((byte) side, player, x, y, z);
					}
				}
			}
			catch (final Throwable t) {}

			if (!mDidScrewDriver) {
				final TileEntity te = world.getTileEntity(x, y, z);
				if ((te != null) && (te instanceof TileEntityCircuitProgrammer)){
					player.openGui(GTplusplus.instance, GuiHandler.GUI8, world, x, y, z);
					return true;
				}
			}
			else {
				return true;
			}
		
		}
		return false;
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
		return new TileEntityCircuitProgrammer();
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack stack) {
		if (stack.hasDisplayName()) {
			((TileEntityCircuitProgrammer) world.getTileEntity(x,y,z)).setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	@Override
	public int getMetaCount() {
		return 0;
	}

	@Override
	public String getUnlocalBlockName() {
		return "blockCircuitProgrammer";
	}

	@Override
	protected float initBlockHardness() {
		return 5f;
	}

	@Override
	protected float initBlockResistance() {
		return 1f;
	}

	@Override
	protected CreativeTabs initCreativeTab() {
		return AddToCreativeTab.tabMachines;
	}

	@Override
	protected String getTileEntityName() {
		return "Circuit Programmer";
	}

	@Override
	public CubicObject<String>[] getCustomTextureDirectoryObject() {
		String[] aTexData = new String[] {
				CORE.MODID + ":" + "metro/" + "TEXTURE_METAL_PANEL_G",
				CORE.MODID + ":" + "metro/" + "TEXTURE_TECH_PANEL_B",
				CORE.MODID + ":" + "metro/" + "TEXTURE_METAL_PANEL_I",
				CORE.MODID + ":" + "metro/" + "TEXTURE_METAL_PANEL_I",
				CORE.MODID + ":" + "metro/" + "TEXTURE_METAL_PANEL_I",
				CORE.MODID + ":" + "metro/" + "TEXTURE_METAL_PANEL_I"
		};
		CubicObject<String>[] aTextureData = new CubicObject[] {new CubicObject<String>(aTexData)};		
		return aTextureData;
	}

}