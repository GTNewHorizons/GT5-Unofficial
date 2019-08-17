package gtPlusPlus.nei.handlers;

import crazypants.render.RenderUtil;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Based on crazypants.enderio.gui.IconEIO
 * @author Original EIO Author
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
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(CORE.MODID+":textures/gui/nei/widgets.png");

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
			RenderUtil.bindTexture(TEXTURE);
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
}