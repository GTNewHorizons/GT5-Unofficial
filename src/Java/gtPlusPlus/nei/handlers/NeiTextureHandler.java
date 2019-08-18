package gtPlusPlus.nei.handlers;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


/**
 * Based on crazypants.enderio.gui.IconEIO
 * 
 * @author Original EIO Author
 *
 *         This is free and unencumbered software released into the public
 *         domain.
 * 
 *         Anyone is free to copy, modify, publish, use, compile, sell, or
 *         distribute this software, either in source code form or as a compiled
 *         binary, for any purpose, commercial or non-commercial, and by any
 *         means.
 * 
 *         In jurisdictions that recognize copyright laws, the author or authors
 *         of this software dedicate any and all copyright interest in the
 *         software to the public domain. We make this dedication for the
 *         benefit of the public at large and to the detriment of our heirs and
 *         successors. We intend this dedication to be an overt act of
 *         relinquishment in perpetuity of all present and future rights to this
 *         software under copyright law.
 * 
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *         EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *         MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *         NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY
 *         CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *         TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *         SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *         For more information, please refer to <http://unlicense.org/>
 *
 *	https://github.com/SleepyTrousers/EnderIO/blob/release/1.7.10/2.2/src/main/java/crazypants/render/RenderUtil.java
 *
 */
public final class NeiTextureHandler {

	public static final NeiTextureHandler RECIPE = new NeiTextureHandler(16, 132, 16, 16);
	public static final NeiTextureHandler RECIPE_BUTTON = new NeiTextureHandler(128, 116, 24, 24);

	public final double minU;
	public final double maxU;
	public final double minV;
	public final double maxV;
	public final double width;
	public final double height;

	public static final ResourceLocation TEXTURE = new ResourceLocation(CORE.MODID + ":textures/gui/nei/widgets.png");

	public NeiTextureHandler(int x, int y) {
		this(x, y, 16, 16);
	}

	public NeiTextureHandler(int x, int y, int width, int height) {
		this((double) width, (double) height, (double) ((float) (0.00390625D * (double) x)),
				(double) ((float) (0.00390625D * (double) (x + width))), (double) ((float) (0.00390625D * (double) y)),
				(double) ((float) (0.00390625D * (double) (y + height))));
	}

	public NeiTextureHandler(double width, double height, double minU, double maxU, double minV, double maxV) {
		this.width = width;
		this.height = height;
		this.minU = minU;
		this.maxU = maxU;
		this.minV = minV;
		this.maxV = maxV;
	}

	public void renderIcon(double x, double y) {
		this.renderIcon(x, y, this.width, this.height, 0.0D, false);
	}

	public void renderIcon(double x, double y, boolean doDraw) {
		this.renderIcon(x, y, this.width, this.height, 0.0D, doDraw);
	}

	public void renderIcon(double x, double y, double width, double height, double zLevel, boolean doDraw) {
		this.renderIcon(x, y, width, height, zLevel, doDraw, false);
	}

	public void renderIcon(double x, double y, double width, double height, double zLevel, boolean doDraw,
			boolean flipY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tessellator tessellator = Tessellator.instance;
		if (doDraw) {
			bindTexture(TEXTURE);
			tessellator.startDrawingQuads();
		}

		if (flipY) {
			tessellator.addVertexWithUV(x, y + height, zLevel, this.minU, this.minV);
			tessellator.addVertexWithUV(x + width, y + height, zLevel, this.maxU, this.minV);
			tessellator.addVertexWithUV(x + width, y + 0.0D, zLevel, this.maxU, this.maxV);
			tessellator.addVertexWithUV(x, y + 0.0D, zLevel, this.minU, this.maxV);
		} else {
			tessellator.addVertexWithUV(x, y + height, zLevel, this.minU, this.maxV);
			tessellator.addVertexWithUV(x + width, y + height, zLevel, this.maxU, this.maxV);
			tessellator.addVertexWithUV(x + width, y + 0.0D, zLevel, this.maxU, this.minV);
			tessellator.addVertexWithUV(x, y + 0.0D, zLevel, this.minU, this.minV);
		}

		if (doDraw) {
			tessellator.draw();
		}

	}

	public static final ResourceLocation BLOCK_TEX;
	public static final ResourceLocation ITEM_TEX;
	public static final ResourceLocation GLINT_TEX;
	public static int BRIGHTNESS_MAX;

	static {
		BLOCK_TEX = TextureMap.locationBlocksTexture;
		ITEM_TEX = TextureMap.locationItemsTexture;
		GLINT_TEX = new ResourceLocation("textures/misc/enchanted_item_glint.png");
		BRIGHTNESS_MAX = 15728880;
	}

	public static TextureManager engine() {
		return Minecraft.getMinecraft().renderEngine;
	}

	public static void bindItemTexture(ItemStack stack) {
		engine().bindTexture(stack.getItemSpriteNumber() == 0 ? BLOCK_TEX : ITEM_TEX);
	}

	public static void bindItemTexture() {
		engine().bindTexture(ITEM_TEX);
	}

	public static void bindBlockTexture() {
		engine().bindTexture(BLOCK_TEX);
	}

	public static void bindGlintTexture() {
		engine().bindTexture(BLOCK_TEX);
	}

	public static void bindTexture(String string) {
		engine().bindTexture(new ResourceLocation(string));
	}

	public static void bindTexture(ResourceLocation tex) {
		engine().bindTexture(tex);
	}

	public static FontRenderer fontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

}