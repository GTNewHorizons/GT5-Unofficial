package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;

public class Machine_WorkbenchAdvanced extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	@SuppressWarnings("deprecation")
	public Machine_WorkbenchAdvanced()
	{
		super(Material.iron);
		this.setBlockName("blockWorkbenchGTAdvanced");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, "blockWorkbenchGTAdvanced");
		LanguageRegistry.addName(this, "Advanced Workbench");

	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_)
	{
		return p_149691_1_ == 1 ? this.textureTop : (p_149691_1_ == 0 ? this.textureBottom : ((p_149691_1_ != 2) && (p_149691_1_ != 4) ? this.blockIcon : this.textureFront));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_)
	{
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "machine_top");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "cover_crafting");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "machine_top");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "TileEntities/" + "machine_top");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz)
	{

		/*final ItemStack heldItem = PlayerUtils.getItemStackInPlayersHand(player);
		if (world.isRemote) {
			return true;
		}
		boolean holdingWrench = false;

		if (heldItem != null){
			if (heldItem.getItem() instanceof ItemToolWrench){
				holdingWrench = true;
			}
			else if (heldItem.getItem() instanceof IToolWrench){
				holdingWrench = true;
			}
			else if (heldItem.getItem() instanceof ITool){
				holdingWrench = true;
			}
			else if (heldItem.getItem() instanceof GT_MetaGenerated_Tool){
				GT_MetaGenerated_Tool testTool = (GT_MetaGenerated_Tool) heldItem.getItem();
				if (testTool.canWrench(player, x, y, z)){
					holdingWrench = true;
				}
			}
			else {
				holdingWrench = false;
			}
		}


		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileEntityWorkbenchAdvanced))
		{
			if (!holdingWrench){
				player.openGui(GTplusplus.instance, 4, world, x, y, z);
				return true;
			}
			Utils.LOG_INFO("Holding a Wrench, doing wrench things instead.");
		}*/
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileEntityWorkbenchAdvanced(128000, 2);
	}
}