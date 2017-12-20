package gtPlusPlus.core.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BlockBaseNBT;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Machine_TradeTable extends BlockBaseNBT
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;

	public Machine_TradeTable(){
		super(Material.leaves, "blockTradeBench", "Trade-o-Mat");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_){
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
		this.textureTop = p_149651_1_.registerIcon(CORE.MODID + ":" + "workbench_top");
		this.textureBottom = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
		this.textureFront = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlYellow");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz){
		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileEntityTradeTable))
		{
			//Utils.LOG_INFO("Clicked on TE - ok");
			player.openGui(GTplusplus.instance, 6, world, x, y, z);
			return true;
		}
		else {
			Logger.INFO("Bad TE");
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileEntityTradeTable();
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

}