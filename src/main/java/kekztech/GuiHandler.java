package kekztech;

import container.Container_ItemDistributionNode;
import container.Gui_ItemDistributionNode;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tileentities.TE_ItemDistributionNode;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null) {
			if(ID == 0) {
				return new Container_ItemDistributionNode((TE_ItemDistributionNode) te, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null) {
			if(ID == 0) {
				return new Gui_ItemDistributionNode((TE_ItemDistributionNode) te, player);
			}
		}
		return null;
	}

}
