package com.github.technus.tectech.proxy;

import com.github.technus.tectech.blocks.QuantumGlassRender;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy {
	public void registerRenderInfo() {
		QuantumGlassRender.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(QuantumGlassRender.renderID, new QuantumGlassRender());
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void renderUnicodeString(String str, int x, int y, int maxWidth, int color) {
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer fontRenderer = mc.fontRenderer;

		boolean origFont = fontRenderer.getUnicodeFlag();

		if ((mc.gameSettings.guiScale) == 3) {
			fontRenderer.setUnicodeFlag(true);
			float dist = 0.08F;
			y--;
			for (int cycle = 0; cycle < 2; cycle++) {
				GL11.glTranslatef(-dist, 0F, 0F);
				fontRenderer.drawSplitString(str, x, y, maxWidth, color);
				GL11.glTranslatef(dist, -dist, 0F);
				fontRenderer.drawSplitString(str, x, y, maxWidth, color);
				GL11.glTranslatef(dist, 0F, 0F);
				fontRenderer.drawSplitString(str, x, y, maxWidth, color);
				GL11.glTranslatef(-dist, dist, 0F);

				dist = -dist;
			}
			fontRenderer.setUnicodeFlag(origFont);
		} else
			fontRenderer.drawSplitString(str, x, y, maxWidth, color);

	}
}
