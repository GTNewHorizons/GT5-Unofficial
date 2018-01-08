package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO")
public class Machine_ProjectTable extends BlockContainer implements ITileTooltip
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	/**
	 * Determines which tooltip is displayed within the itemblock.
	 */
	private final int mTooltipID = 3;

	@Override
	public int getTooltipID() {
		return this.mTooltipID;
	}

	@SuppressWarnings("deprecation")
	public Machine_ProjectTable()
	{
		super(Material.iron);
		this.setBlockName("blockProjectBench");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockProjectBench");
		LanguageRegistry.addName(this, "Project Workbench");

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

		ItemStack heldItem = null;
		if (world.isRemote){
			heldItem = PlayerUtils.getItemStackInPlayersHand();
		}

		boolean holdingWrench = false;

		if (heldItem != null){
			holdingWrench =  isWrench(heldItem);
		}

		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileEntityProjectTable))
		{
			if (!holdingWrench){
				player.openGui(GTplusplus.instance, 0, world, x, y, z);
				return true;
			}
			Logger.INFO("Holding a Wrench, doing wrench things instead.");
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileEntityProjectTable();
	}

	public static boolean isWrench(final ItemStack item){
		if (item.getItem() instanceof ItemToolWrench){
			return true;
		}
		if (LoadedMods.BuildCraft){
			return checkBuildcraftWrench(item);
		}
		if (LoadedMods.EnderIO){
			return checkEnderIOWrench(item);
		}
		return false;
	}

	@Optional.Method(modid = "EnderIO")
	private static boolean checkEnderIOWrench(final ItemStack item){
		if (ReflectionUtils.doesClassExist("crazypants.enderio.api.tool.ITool")){
			Class<?> wrenchClass;
			try {
				wrenchClass = Class.forName("crazypants.enderio.api.tool.ITool");
				if (wrenchClass.isInstance(item.getItem())){
					return true;
				}
			}
			catch (final ClassNotFoundException e1) {
				return false;
			}
		}
		return false;
	}

	@Optional.Method(modid = "Buildcraft")
	private static boolean checkBuildcraftWrench(final ItemStack item){
		if (ReflectionUtils.doesClassExist("buildcraft.api.tools.IToolWrench")){
			Class<?> wrenchClass;
			try {
				wrenchClass = Class.forName("buildcraft.api.tools.IToolWrench");
				if (wrenchClass.isInstance(item.getItem())){
					return true;
				}
			}
			catch (final ClassNotFoundException e1) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

}