package kekztech;

import client.gui.Gui_ItemProxyEndpoint;
import client.gui.Gui_ItemProxySource;
import common.container.Container_ItemProxyEndpoint;
import common.container.Container_ItemProxySource;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
	
	public static final int ITEM_PROXY_SOURCE = 0;
	public static final int ITEM_PROXY_ENDPOINT = 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null) {
			switch(ID) {
			case ITEM_PROXY_SOURCE: return new Container_ItemProxySource(te, player);
			case ITEM_PROXY_ENDPOINT: return new Container_ItemProxyEndpoint(te, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te != null) {
			switch(ID) {
			case ITEM_PROXY_SOURCE: return new Gui_ItemProxySource(te, player);
			case ITEM_PROXY_ENDPOINT: return new Gui_ItemProxyEndpoint(te, player);
			}
		}
		return null;
	}

}
