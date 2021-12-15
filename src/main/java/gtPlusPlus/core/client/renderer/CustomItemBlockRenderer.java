package gtPlusPlus.core.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;


/**
 * Easy way of rendering an item which should look like a block.
 * Borrowed.
 * 
 * @author King Lemming
 * 
 */
public class CustomItemBlockRenderer implements IItemRenderer {

	public static CustomItemBlockRenderer instance = new CustomItemBlockRenderer();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		double offset = -0.5;
		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			offset = 0;
		} else if (type == ItemRenderType.ENTITY) {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
		}
		renderItemAsBlock((RenderBlocks) data[0], item, offset, offset, offset);
	}
	
	public static void renderItemAsBlock(RenderBlocks renderer, ItemStack item, double translateX, double translateY, double translateZ) {

		renderTextureAsBlock(renderer, item.getIconIndex(), translateX, translateY, translateZ);
	}

	public static void renderTextureAsBlock(RenderBlocks renderer, IIcon texture, double translateX, double translateY, double translateZ) {

		Tessellator tessellator = Tessellator.instance;
		Block block = Blocks.stone;

		if (texture == null) {
			return;
		}
		renderer.setRenderBoundsFromBlock(block);
		GL11.glTranslated(translateX, translateY, translateZ);
		tessellator.startDrawingQuads();

		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, texture);

		tessellator.draw();
	}

}
