package com.github.technus.tectech.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
	public void registerRenderInfo() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public World getClientWorld() {
		return null;
	}

	public void renderUnicodeString(String str, int x, int y, int maxWidth, int color) {
	}

	public void setCustomRenderer() {
	}

	public void setCustomRenderers() {
	}
}
